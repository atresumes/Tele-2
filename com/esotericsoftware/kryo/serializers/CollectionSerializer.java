package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;

public class CollectionSerializer extends Serializer<Collection> {
    private Class elementClass;
    private boolean elementsCanBeNull = true;
    private Class genericType;
    private Serializer serializer;

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface BindCollection {
        Class<?> elementClass() default Object.class;

        Class<? extends Serializer> elementSerializer() default Serializer.class;

        boolean elementsCanBeNull() default true;
    }

    public CollectionSerializer(Class elementClass, Serializer serializer) {
        setElementClass(elementClass, serializer);
    }

    public CollectionSerializer(Class elementClass, Serializer serializer, boolean elementsCanBeNull) {
        setElementClass(elementClass, serializer);
        this.elementsCanBeNull = elementsCanBeNull;
    }

    public void setElementsCanBeNull(boolean elementsCanBeNull) {
        this.elementsCanBeNull = elementsCanBeNull;
    }

    public void setElementClass(Class elementClass, Serializer serializer) {
        this.elementClass = elementClass;
        this.serializer = serializer;
    }

    public void setGenerics(Kryo kryo, Class[] generics) {
        this.genericType = null;
        if (generics != null && generics.length > 0 && kryo.isFinal(generics[0])) {
            this.genericType = generics[0];
        }
    }

    public void write(Kryo kryo, Output output, Collection collection) {
        output.writeVarInt(collection.size(), true);
        Serializer serializer = this.serializer;
        if (this.genericType != null) {
            if (serializer == null) {
                serializer = kryo.getSerializer(this.genericType);
            }
            this.genericType = null;
        }
        if (serializer == null) {
            for (Object element : collection) {
                kryo.writeClassAndObject(output, element);
            }
        } else if (this.elementsCanBeNull) {
            for (Object element2 : collection) {
                kryo.writeObjectOrNull(output, element2, serializer);
            }
        } else {
            for (Object element22 : collection) {
                kryo.writeObject(output, element22, serializer);
            }
        }
    }

    protected Collection create(Kryo kryo, Input input, Class<Collection> type) {
        return (Collection) kryo.newInstance(type);
    }

    public Collection read(Kryo kryo, Input input, Class<Collection> type) {
        Collection collection = create(kryo, input, type);
        kryo.reference(collection);
        int length = input.readVarInt(true);
        if (collection instanceof ArrayList) {
            ((ArrayList) collection).ensureCapacity(length);
        }
        Class elementClass = this.elementClass;
        Serializer serializer = this.serializer;
        if (this.genericType != null) {
            if (serializer == null) {
                elementClass = this.genericType;
                serializer = kryo.getSerializer(this.genericType);
            }
            this.genericType = null;
        }
        int i;
        if (serializer == null) {
            for (i = 0; i < length; i++) {
                collection.add(kryo.readClassAndObject(input));
            }
        } else if (this.elementsCanBeNull) {
            for (i = 0; i < length; i++) {
                collection.add(kryo.readObjectOrNull(input, elementClass, serializer));
            }
        } else {
            for (i = 0; i < length; i++) {
                collection.add(kryo.readObject(input, elementClass, serializer));
            }
        }
        return collection;
    }

    protected Collection createCopy(Kryo kryo, Collection original) {
        return (Collection) kryo.newInstance(original.getClass());
    }

    public Collection copy(Kryo kryo, Collection original) {
        Collection copy = createCopy(kryo, original);
        kryo.reference(copy);
        for (Object element : original) {
            copy.add(kryo.copy(element));
        }
        return copy;
    }
}
