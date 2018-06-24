package io.paperdb;

import android.content.Context;
import com.esotericsoftware.kryo.Serializer;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Paper {
    static final String DEFAULT_DB_NAME = "io.paperdb";
    static final String TAG = "paperdb";
    private static final ConcurrentHashMap<String, Book> mBookMap = new ConcurrentHashMap();
    private static Context mContext;
    private static final HashMap<Class, Serializer> mCustomSerializers = new HashMap();

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public static Book book(String name) {
        if (!name.equals("io.paperdb")) {
            return getBook(name);
        }
        throw new PaperDbException("io.paperdb name is reserved for default library name");
    }

    public static Book book() {
        return getBook("io.paperdb");
    }

    private static Book getBook(String name) {
        if (mContext == null) {
            throw new PaperDbException("Paper.init is not called");
        }
        Book book;
        synchronized (mBookMap) {
            book = (Book) mBookMap.get(name);
            if (book == null) {
                book = new Book(mContext, name, mCustomSerializers);
                mBookMap.put(name, book);
            }
        }
        return book;
    }

    public static <T> Book put(String key, T value) {
        return book().write(key, value);
    }

    public static <T> T get(String key) {
        return book().read(key);
    }

    public static <T> T get(String key, T defaultValue) {
        return book().read(key, defaultValue);
    }

    public static boolean exist(String key) {
        return book().exist(key);
    }

    public static void delete(String key) {
        book().delete(key);
    }

    public static void clear(Context context) {
        init(context);
        book().destroy();
    }

    public static <T> void addSerializer(Class<T> clazz, Serializer<T> serializer) {
        if (!mCustomSerializers.containsKey(clazz)) {
            mCustomSerializers.put(clazz, serializer);
        }
    }
}
