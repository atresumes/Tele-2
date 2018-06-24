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

public class UnmodifiableCollectionsSerializer extends Serializer<Object> {
    private static final Field SOURCE_COLLECTION_FIELD;
    private static final Field SOURCE_MAP_FIELD;

    private enum UnmodifiableCollection {
        COLLECTION(Collections.unmodifiableCollection(Arrays.asList(new String[]{""})).getClass(), UnmodifiableCollectionsSerializer.SOURCE_COLLECTION_FIELD) {
            public Object create(Object sourceCollection) {
                return Collections.unmodifiableCollection((Collection) sourceCollection);
            }
        },
        RANDOM_ACCESS_LIST(Collections.unmodifiableList(new ArrayList()).getClass(), UnmodifiableCollectionsSerializer.SOURCE_COLLECTION_FIELD) {
            public Object create(Object sourceCollection) {
                return Collections.unmodifiableList((List) sourceCollection);
            }
        },
        LIST(Collections.unmodifiableList(new LinkedList()).getClass(), UnmodifiableCollectionsSerializer.SOURCE_COLLECTION_FIELD) {
            public Object create(Object sourceCollection) {
                return Collections.unmodifiableList((List) sourceCollection);
            }
        },
        SET(Collections.unmodifiableSet(new HashSet()).getClass(), UnmodifiableCollectionsSerializer.SOURCE_COLLECTION_FIELD) {
            public Object create(Object sourceCollection) {
                return Collections.unmodifiableSet((Set) sourceCollection);
            }
        },
        SORTED_SET(Collections.unmodifiableSortedSet(new TreeSet()).getClass(), UnmodifiableCollectionsSerializer.SOURCE_COLLECTION_FIELD) {
            public Object create(Object sourceCollection) {
                return Collections.unmodifiableSortedSet((SortedSet) sourceCollection);
            }
        },
        MAP(Collections.unmodifiableMap(new HashMap()).getClass(), UnmodifiableCollectionsSerializer.SOURCE_MAP_FIELD) {
            public Object create(Object sourceCollection) {
                return Collections.unmodifiableMap((Map) sourceCollection);
            }
        },
        SORTED_MAP(Collections.unmodifiableSortedMap(new TreeMap()).getClass(), UnmodifiableCollectionsSerializer.SOURCE_MAP_FIELD) {
            public Object create(Object sourceCollection) {
                return Collections.unmodifiableSortedMap((SortedMap) sourceCollection);
            }
        };
        
        private final Field sourceCollectionField;
        private final Class<?> type;

        public abstract Object create(Object obj);

        private UnmodifiableCollection(Class<?> type, Field sourceCollectionField) {
            this.type = type;
            this.sourceCollectionField = sourceCollectionField;
        }

        static UnmodifiableCollection valueOfType(Class<?> type) {
            for (UnmodifiableCollection item : values()) {
                if (item.type.equals(type)) {
                    return item;
                }
            }
            throw new IllegalArgumentException("The type " + type + " is not supported.");
        }
    }

    static {
        try {
            SOURCE_COLLECTION_FIELD = Class.forName("java.util.Collections$UnmodifiableCollection").getDeclaredField("c");
            SOURCE_COLLECTION_FIELD.setAccessible(true);
            SOURCE_MAP_FIELD = Class.forName("java.util.Collections$UnmodifiableMap").getDeclaredField("m");
            SOURCE_MAP_FIELD.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException("Could not access source collection field in java.util.Collections$UnmodifiableCollection.", e);
        }
    }

    public Object read(Kryo kryo, Input input, Class<Object> cls) {
        return UnmodifiableCollection.values()[input.readInt(true)].create(kryo.readClassAndObject(input));
    }

    public void write(Kryo kryo, Output output, Object object) {
        try {
            UnmodifiableCollection unmodifiableCollection = UnmodifiableCollection.valueOfType(object.getClass());
            output.writeInt(unmodifiableCollection.ordinal(), true);
            kryo.writeClassAndObject(output, unmodifiableCollection.sourceCollectionField.get(object));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    public Object copy(Kryo kryo, Object original) {
        try {
            UnmodifiableCollection unmodifiableCollection = UnmodifiableCollection.valueOfType(original.getClass());
            return unmodifiableCollection.create(kryo.copy(unmodifiableCollection.sourceCollectionField.get(original)));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    public static void registerSerializers(Kryo kryo) {
        Serializer serializer = new UnmodifiableCollectionsSerializer();
        UnmodifiableCollection.values();
        for (UnmodifiableCollection item : UnmodifiableCollection.values()) {
            kryo.register(item.type, serializer);
        }
    }
}
