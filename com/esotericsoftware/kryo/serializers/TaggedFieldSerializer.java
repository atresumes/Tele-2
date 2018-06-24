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
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

public class TaggedFieldSerializer<T> extends FieldSerializer<T> {
    private boolean[] deprecated;
    private int[] tags;
    private int writeFieldCount;

    class C04761 implements Comparator<CachedField> {
        C04761() {
        }

        public int compare(CachedField o1, CachedField o2) {
            return ((Tag) o1.getField().getAnnotation(Tag.class)).value() - ((Tag) o2.getField().getAnnotation(Tag.class)).value();
        }
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Tag {
        int value();
    }

    public TaggedFieldSerializer(Kryo kryo, Class type) {
        super(kryo, type, null, kryo.getTaggedFieldSerializerConfig().clone());
    }

    public void setIgnoreUnknownTags(boolean ignoreUnknownTags) {
        ((TaggedFieldSerializerConfig) this.config).setIgnoreUnknownTags(ignoreUnknownTags);
        rebuildCachedFields();
    }

    public boolean isIgnoreUnkownTags() {
        return ((TaggedFieldSerializerConfig) this.config).isIgnoreUnknownTags();
    }

    protected void initializeCachedFields() {
        int i;
        CachedField[] fields = getFields();
        int n = fields.length;
        for (i = 0; i < n; i++) {
            if (fields[i].getField().getAnnotation(Tag.class) == null) {
                if (Log.TRACE) {
                    Log.trace("kryo", "Ignoring field without tag: " + fields[i]);
                }
                super.removeField(fields[i]);
            }
        }
        fields = getFields();
        this.tags = new int[fields.length];
        this.deprecated = new boolean[fields.length];
        this.writeFieldCount = fields.length;
        Arrays.sort(fields, new C04761());
        n = fields.length;
        for (i = 0; i < n; i++) {
            Field field = fields[i].getField();
            this.tags[i] = ((Tag) field.getAnnotation(Tag.class)).value();
            if (field.getAnnotation(Deprecated.class) != null) {
                this.deprecated[i] = true;
                this.writeFieldCount--;
            }
        }
        this.removedFields.clear();
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
        output.writeVarInt(this.writeFieldCount, true);
        int n = fields.length;
        for (int i = 0; i < n; i++) {
            if (!this.deprecated[i]) {
                output.writeVarInt(this.tags[i], true);
                fields[i].write(output, object);
            }
        }
    }

    public T read(Kryo kryo, Input input, Class<T> type) {
        T object = create(kryo, input, type);
        kryo.reference(object);
        int fieldCount = input.readVarInt(true);
        int[] tags = this.tags;
        CachedField[] fields = getFields();
        int n = fieldCount;
        for (int i = 0; i < n; i++) {
            int tag = input.readVarInt(true);
            CachedField cachedField = null;
            int nn = tags.length;
            for (int ii = 0; ii < nn; ii++) {
                if (tags[ii] == tag) {
                    cachedField = fields[ii];
                    break;
                }
            }
            if (cachedField != null) {
                cachedField.read(input, object);
            } else if (!isIgnoreUnkownTags()) {
                throw new KryoException("Unknown field tag: " + tag + " (" + getType().getName() + ")");
            }
        }
        return object;
    }
}
