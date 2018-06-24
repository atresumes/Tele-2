package io.paperdb;

import android.content.Context;
import com.esotericsoftware.kryo.Serializer;
import java.util.HashMap;
import java.util.List;

public class Book {
    private final Storage mStorage;

    protected Book(Context context, String dbName, HashMap<Class, Serializer> serializers) {
        this.mStorage = new DbStoragePlainFile(context.getApplicationContext(), dbName, serializers);
    }

    public void destroy() {
        this.mStorage.destroy();
    }

    public <T> Book write(String key, T value) {
        if (value == null) {
            throw new PaperDbException("Paper doesn't support writing null root values");
        }
        this.mStorage.insert(key, value);
        return this;
    }

    public <T> T read(String key) {
        return read(key, null);
    }

    public <T> T read(String key, T defaultValue) {
        T value = this.mStorage.select(key);
        return value == null ? defaultValue : value;
    }

    public boolean exist(String key) {
        return this.mStorage.exist(key);
    }

    public void delete(String key) {
        this.mStorage.deleteIfExists(key);
    }

    public List<String> getAllKeys() {
        return this.mStorage.getAllKeys();
    }
}
