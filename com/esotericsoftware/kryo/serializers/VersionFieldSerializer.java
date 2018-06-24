package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import com.esotericsoftware.minlog.Log;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class VersionFieldSerializer<T> extends FieldSerializer<T> {
    private boolean compatible;
    private int[] fieldVersion;
    private int typeVersion;

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Since {
        int value() default 0;
    }

    public VersionFieldSerializer(Kryo kryo, Class type) {
        super(kryo, type);
        this.typeVersion = 0;
        this.compatible = true;
        initializeCachedFields();
    }

    public VersionFieldSerializer(Kryo kryo, Class type, boolean compatible) {
        this(kryo, type);
        this.compatible = compatible;
    }

    protected void initializeCachedFields() {
        CachedField[] fields = getFields();
        this.fieldVersion = new int[fields.length];
        int n = fields.length;
        for (int i = 0; i < n; i++) {
            Since since = (Since) fields[i].getField().getAnnotation(Since.class);
            if (since != null) {
                this.fieldVersion[i] = since.value();
                this.typeVersion = Math.max(this.fieldVersion[i], this.typeVersion);
            } else {
                this.fieldVersion[i] = 0;
            }
        }
        this.removedFields.clear();
        if (Log.DEBUG) {
            Log.debug("Version for type " + getType().getName() + " is " + this.typeVersion);
        }
    }

    public void removeField(String fieldName) {
        super.removeField(fieldName);
        initializeCachedFields();
    }

    public void removeField(CachedField field) {
        super.removeField(field);
        initializeCachedFields();
    }

    public void write(Kryo kryo, Output output, T object) {
        CachedField[] fields = getFields();
        output.writeVarInt(this.typeVersion, true);
        for (CachedField write : fields) {
            write.write(output, object);
        }
    }

    public T read(Kryo kryo, Input input, Class<T> type) {
        T object = create(kryo, input, type);
        kryo.reference(object);
        int version = input.readVarInt(true);
        if (this.compatible || version == this.typeVersion) {
            CachedField[] fields = getFields();
            int n = fields.length;
            for (int i = 0; i < n; i++) {
                if (this.fieldVersion[i] <= version) {
                    fields[i].read(input, object);
                } else if (Log.DEBUG) {
                    Log.debug("Skip field " + fields[i].getField().getName());
                }
            }
            return object;
        }
        throw new KryoException("Version not compatible: " + version + " <-> " + this.typeVersion);
    }
}
