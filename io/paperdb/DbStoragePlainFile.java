package io.paperdb;

import android.content.Context;
import android.util.Log;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Kryo.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import de.javakaffee.kryoserializers.ArraysAsListSerializer;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import de.javakaffee.kryoserializers.UUIDSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;
import io.paperdb.serializer.NoArgCollectionSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.objenesis.strategy.StdInstantiatorStrategy;

public class DbStoragePlainFile implements Storage {
    private final Context mContext;
    private final HashMap<Class, Serializer> mCustomSerializers;
    private final String mDbName;
    private String mFilesDir;
    private final ThreadLocal<Kryo> mKryo = new C14731();
    private boolean mPaperDirIsCreated;

    class C14731 extends ThreadLocal<Kryo> {
        C14731() {
        }

        protected Kryo initialValue() {
            return DbStoragePlainFile.this.createKryoInstance();
        }
    }

    private Kryo getKryo() {
        return (Kryo) this.mKryo.get();
    }

    private Kryo createKryoInstance() {
        Kryo kryo = new Kryo();
        kryo.register(PaperTable.class);
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        kryo.setReferences(false);
        kryo.register(Arrays.asList(new String[]{""}).getClass(), new ArraysAsListSerializer());
        UnmodifiableCollectionsSerializer.registerSerializers(kryo);
        SynchronizedCollectionsSerializer.registerSerializers(kryo);
        kryo.addDefaultSerializer(new ArrayList().subList(0, 0).getClass(), new NoArgCollectionSerializer());
        kryo.addDefaultSerializer(new LinkedList().subList(0, 0).getClass(), new NoArgCollectionSerializer());
        kryo.register(UUID.class, new UUIDSerializer());
        for (Class clazz : this.mCustomSerializers.keySet()) {
            kryo.register(clazz, (Serializer) this.mCustomSerializers.get(clazz));
        }
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        return kryo;
    }

    public DbStoragePlainFile(Context context, String dbName, HashMap<Class, Serializer> serializers) {
        this.mContext = context;
        this.mDbName = dbName;
        this.mCustomSerializers = serializers;
    }

    public synchronized void destroy() {
        assertInit();
        String dbPath = getDbPath(this.mContext, this.mDbName);
        if (!deleteDirectory(dbPath)) {
            Log.e("paperdb", "Couldn't delete Paper dir " + dbPath);
        }
        this.mPaperDirIsCreated = false;
    }

    public synchronized <E> void insert(String key, E value) {
        assertInit();
        PaperTable<E> paperTable = new PaperTable(value);
        File originalFile = getOriginalFile(key);
        File backupFile = makeBackupFile(originalFile);
        if (originalFile.exists()) {
            if (backupFile.exists()) {
                originalFile.delete();
            } else if (!originalFile.renameTo(backupFile)) {
                throw new PaperDbException("Couldn't rename file " + originalFile + " to backup file " + backupFile);
            }
        }
        writeTableFile(key, paperTable, originalFile, backupFile);
    }

    public synchronized <E> E select(String key) {
        E readTableFile;
        assertInit();
        File originalFile = getOriginalFile(key);
        File backupFile = makeBackupFile(originalFile);
        if (backupFile.exists()) {
            originalFile.delete();
            backupFile.renameTo(originalFile);
        }
        if (exist(key)) {
            readTableFile = readTableFile(key, originalFile);
        } else {
            readTableFile = null;
        }
        return readTableFile;
    }

    public synchronized boolean exist(String key) {
        assertInit();
        return getOriginalFile(key).exists();
    }

    public List<String> getAllKeys() {
        assertInit();
        String[] names = new File(this.mFilesDir).list();
        if (names == null) {
            return new ArrayList();
        }
        for (int i = 0; i < names.length; i++) {
            names[i] = names[i].replace(".pt", "");
        }
        return Arrays.asList(names);
    }

    public synchronized void deleteIfExists(String key) {
        assertInit();
        File originalFile = getOriginalFile(key);
        if (originalFile.exists()) {
            if (!originalFile.delete()) {
                throw new PaperDbException("Couldn't delete file " + originalFile + " for table " + key);
            }
        }
    }

    private File getOriginalFile(String key) {
        return new File(this.mFilesDir + File.separator + key + ".pt");
    }

    private <E> void writeTableFile(String key, PaperTable<E> paperTable, File originalFile, File backupFile) {
        Exception e;
        try {
            OutputStream fileStream = new FileOutputStream(originalFile);
            Output kryoOutput = new Output(fileStream);
            getKryo().writeObject(kryoOutput, paperTable);
            kryoOutput.flush();
            fileStream.flush();
            sync(fileStream);
            kryoOutput.close();
            backupFile.delete();
        } catch (IOException e2) {
            e = e2;
            if (originalFile.exists() || originalFile.delete()) {
                throw new PaperDbException("Couldn't save table: " + key + ". " + "Backed up table will be used on next read attempt", e);
            }
            throw new PaperDbException("Couldn't clean up partially-written file " + originalFile, e);
        } catch (KryoException e3) {
            e = e3;
            if (originalFile.exists()) {
            }
            throw new PaperDbException("Couldn't save table: " + key + ". " + "Backed up table will be used on next read attempt", e);
        }
    }

    private <E> E readTableFile(String key, File originalFile) {
        return readTableFile(key, originalFile, false);
    }

    private <E> E readTableFile(String key, File originalFile, boolean v1CompatibilityMode) {
        Exception e;
        try {
            Input i = new Input(new FileInputStream(originalFile));
            Kryo kryo = getKryo();
            if (v1CompatibilityMode) {
                kryo.getFieldSerializerConfig().setOptimizedGenerics(true);
            }
            PaperTable<E> paperTable = (PaperTable) kryo.readObject(i, PaperTable.class);
            i.close();
            if (v1CompatibilityMode) {
                kryo.getFieldSerializerConfig().setOptimizedGenerics(false);
            }
            return paperTable.mContent;
        } catch (Exception e2) {
            e = e2;
            if (v1CompatibilityMode) {
                return readTableFile(key, originalFile, true);
            }
            if (originalFile.exists() || originalFile.delete()) {
                throw new PaperDbException("Couldn't read/deserialize file " + originalFile + " for table " + key, e);
            }
            throw new PaperDbException("Couldn't clean up broken/unserializable file " + originalFile, e);
        } catch (Exception e22) {
            e = e22;
            if (v1CompatibilityMode) {
                return readTableFile(key, originalFile, true);
            }
            if (originalFile.exists()) {
            }
            throw new PaperDbException("Couldn't read/deserialize file " + originalFile + " for table " + key, e);
        } catch (Exception e222) {
            e = e222;
            if (v1CompatibilityMode) {
                return readTableFile(key, originalFile, true);
            }
            if (originalFile.exists()) {
            }
            throw new PaperDbException("Couldn't read/deserialize file " + originalFile + " for table " + key, e);
        }
    }

    private String getDbPath(Context context, String dbName) {
        return context.getFilesDir() + File.separator + dbName;
    }

    private void assertInit() {
        if (!this.mPaperDirIsCreated) {
            createPaperDir();
            this.mPaperDirIsCreated = true;
        }
    }

    private void createPaperDir() {
        this.mFilesDir = getDbPath(this.mContext, this.mDbName);
        if (!new File(this.mFilesDir).exists() && !new File(this.mFilesDir).mkdirs()) {
            throw new RuntimeException("Couldn't create Paper dir: " + this.mFilesDir);
        }
    }

    private static boolean deleteDirectory(String dirPath) {
        File directory = new File(dirPath);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file.toString());
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return directory.delete();
    }

    private File makeBackupFile(File originalFile) {
        return new File(originalFile.getPath() + ".bak");
    }

    private static boolean sync(FileOutputStream stream) {
        if (stream != null) {
            try {
                stream.getFD().sync();
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }
}
