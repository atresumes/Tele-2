package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.InputChunked;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.OutputChunked;
import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import com.esotericsoftware.kryo.util.ObjectMap;
import com.esotericsoftware.minlog.Log;

public class CompatibleFieldSerializer<T> extends FieldSerializer<T> {
    private static final int THRESHOLD_BINARY_SEARCH = 32;

    public CompatibleFieldSerializer(Kryo kryo, Class type) {
        super(kryo, type);
    }

    public void write(Kryo kryo, Output output, T object) {
        CachedField[] fields = getFields();
        ObjectMap context = kryo.getGraphContext();
        if (!context.containsKey(this)) {
            context.put(this, null);
            if (Log.TRACE) {
                Log.trace("kryo", "Write " + fields.length + " field names.");
            }
            output.writeVarInt(fields.length, true);
            for (CachedField cachedFieldName : fields) {
                output.writeString(getCachedFieldName(cachedFieldName));
            }
        }
        OutputChunked outputChunked = new OutputChunked(output, 1024);
        for (CachedField cachedFieldName2 : fields) {
            cachedFieldName2.write(outputChunked, object);
            outputChunked.endChunks();
        }
    }

    public T read(Kryo kryo, Input input, Class<T> type) {
        int i;
        T object = create(kryo, input, type);
        kryo.reference(object);
        ObjectMap context = kryo.getGraphContext();
        CachedField[] fields = (CachedField[]) context.get(this);
        if (fields == null) {
            int length = input.readVarInt(true);
            if (Log.TRACE) {
                Log.trace("kryo", "Read " + length + " field names.");
            }
            String[] names = new String[length];
            for (i = 0; i < length; i++) {
                names[i] = input.readString();
            }
            fields = new CachedField[length];
            CachedField[] allFields = getFields();
            String schemaName;
            if (length < 32) {
                for (i = 0; i < length; i++) {
                    schemaName = names[i];
                    int nn = allFields.length;
                    for (int ii = 0; ii < nn; ii++) {
                        if (getCachedFieldName(allFields[ii]).equals(schemaName)) {
                            fields[i] = allFields[ii];
                            break;
                        }
                    }
                    if (Log.TRACE) {
                        Log.trace("kryo", "Ignore obsolete field: " + schemaName);
                    }
                }
            } else {
                for (i = 0; i < length; i++) {
                    schemaName = names[i];
                    int low = 0;
                    int high = length - 1;
                    while (low <= high) {
                        int mid = (low + high) >>> 1;
                        int compare = schemaName.compareTo(getCachedFieldName(allFields[mid]));
                        if (compare >= 0) {
                            if (compare <= 0) {
                                fields[i] = allFields[mid];
                                break;
                            }
                            low = mid + 1;
                        } else {
                            high = mid - 1;
                        }
                    }
                    if (Log.TRACE) {
                        Log.trace("kryo", "Ignore obsolete field: " + schemaName);
                    }
                }
            }
            context.put(this, fields);
        }
        InputChunked inputChunked = new InputChunked(input, 1024);
        boolean hasGenerics = getGenerics() != null;
        for (CachedField cachedField : fields) {
            CachedField cachedField2;
            if (cachedField2 != null && hasGenerics) {
                cachedField2 = getField(getCachedFieldName(cachedField2));
            }
            if (cachedField2 == null) {
                if (Log.TRACE) {
                    Log.trace("kryo", "Skip obsolete field.");
                }
                inputChunked.nextChunks();
            } else {
                cachedField2.read(inputChunked, object);
                inputChunked.nextChunks();
            }
        }
        return object;
    }
}
