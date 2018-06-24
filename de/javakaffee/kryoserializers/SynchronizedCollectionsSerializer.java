package de.javakaffee.kryoserializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class SynchronizedCollectionsSerializer extends Serializer<Object> {
    private static final Field SOURCE_COLLECTION_FIELD;
    private static final Field SOURCE_MAP_FIELD;

    private enum SynchronizedCollection {
        COLLECTION(Collections.synchronizedCollection(Arrays.asList(new String[]{""})).getClass(), SynchronizedCollectionsSerializer.SOURCE_COLLECTION_FIELD) {
            public Object create(Object sourceCollection) {
                return Collections.synchronizedCollection((Collection) sourceCollection);
            }
        },
        RANDOM_ACCESS_LIST(Collections.synchronizedList(new ArrayList()).getClass(), SynchronizedCollectionsSerializer.SOURCE_COLLECTION_FIELD) {
            public Object create(Object sourceCollection) {
                return Collections.synchronizedList((List) sourceCollection);
            }
        },
        LIST(Collections.synchronizedList(new LinkedList()).getClass(), SynchronizedCollectionsSerializer.SOURCE_COLLECTION_FIELD) {
            public Object create(Object sourceCollection) {
                return Collections.synchronizedList((List) sourceCollection);
            }
        },
        SET(Collections.synchronizedSet(new HashSet()).getClass(), SynchronizedCollectionsSerializer.SOURCE_COLLECTION_FIELD) {
            public Object create(Object sourceCollection) {
                return Collections.synchronizedSet((Set) sourceCollection);
            }
        },
        SORTED_SET(Collections.synchronizedSortedSet(new TreeSet()).getClass(), SynchronizedCollectionsSerializer.SOURCE_COLLECTION_FIELD) {
            public Object create(Object sourceCollection) {
                return Collections.synchronizedSortedSet((SortedSet) sourceCollection);
            }
        },
        MAP(Collections.synchronizedMap(new HashMap()).getClass(), SynchronizedCollectionsSerializer.SOURCE_MAP_FIELD) {
            public Object create(Object sourceCollection) {
                return Collections.synchronizedMap((Map) sourceCollection);
            }
        },
        SORTED_MAP(Collections.synchronizedSortedMap(new TreeMap()).getClass(), SynchronizedCollectionsSerializer.SOURCE_MAP_FIELD) {
            public Object create(Object sourceCollection) {
                return Collections.synchronizedSortedMap((SortedMap) sourceCollection);
            }
        };
        
        private final Field sourceCollectionField;
        private final Class<?> type;

        public abstract Object create(Object obj);

        private SynchronizedCollection(Class<?> type, Field sourceCollectionField) {
            this.type = type;
            this.sourceCollectionField = sourceCollectionField;
        }

        static SynchronizedCollection valueOfType(Class<?> type) {
            for (SynchronizedCollection item : values()) {
                if (item.type.equals(type)) {
                    return item;
                }
            }
            throw new IllegalArgumentException("The type " + type + " is not supported.");
        }
    }

    static {
        try {
            SOURCE_COLLECTION_FIELD = Class.forName("java.util.Collections$SynchronizedCollection").getDeclaredField("c");
            SOURCE_COLLECTION_FIELD.setAccessible(true);
            SOURCE_MAP_FIELD = Class.forName("java.util.Collections$SynchronizedMap").getDeclaredField("m");
            SOURCE_MAP_FIELD.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException("Could not access source collection field in java.util.Collections$SynchronizedCollection.", e);
        }
    }

    public Object read(Kryo kryo, Input input, Class<Object> cls) {
        return SynchronizedCollection.values()[input.readInt(true)].create(kryo.readClassAndObject(input));
    }

    public void write(Kryo kryo, Output output, Object object) {
        try {
            SynchronizedCollection collection = SynchronizedCollection.valueOfType(object.getClass());
            output.writeInt(collection.ordinal(), true);
            kryo.writeClassAndObject(output, collection.sourceCollectionField.get(object));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    public Object copy(Kryo kryo, Object original) {
        try {
            SynchronizedCollection collection = SynchronizedCollection.valueOfType(original.getClass());
            return collection.create(kryo.copy(collection.sourceCollectionField.get(original)));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    public static void registerSerializers(Kryo kryo) {
        Serializer serializer = new SynchronizedCollectionsSerializer();
        SynchronizedCollection.values();
        for (SynchronizedCollection item : SynchronizedCollection.values()) {
            kryo.register(item.type, serializer);
        }
    }
}
