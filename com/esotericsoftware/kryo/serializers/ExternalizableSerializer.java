package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.KryoObjectInput;
import com.esotericsoftware.kryo.io.KryoObjectOutput;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.ObjectMap;
import java.io.Externalizable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;

public class ExternalizableSerializer extends Serializer {
    private ObjectMap<Class, JavaSerializer> javaSerializerByType;
    private KryoObjectInput objectInput = null;
    private KryoObjectOutput objectOutput = null;

    public void write(Kryo kryo, Output output, Object object) {
        JavaSerializer serializer = getJavaSerializerIfRequired(object.getClass());
        if (serializer == null) {
            writeExternal(kryo, output, object);
        } else {
            serializer.write(kryo, output, object);
        }
    }

    public Object read(Kryo kryo, Input input, Class type) {
        JavaSerializer serializer = getJavaSerializerIfRequired(type);
        if (serializer == null) {
            return readExternal(kryo, input, type);
        }
        return serializer.read(kryo, input, type);
    }

    private void writeExternal(Kryo kryo, Output output, Object object) {
        try {
            ((Externalizable) object).writeExternal(getObjectOutput(kryo, output));
        } catch (Throwable e) {
            throw new KryoException(e);
        } catch (Throwable e2) {
            throw new KryoException(e2);
        }
    }

    private Object readExternal(Kryo kryo, Input input, Class type) {
        try {
            Externalizable object = (Externalizable) kryo.newInstance(type);
            object.readExternal(getObjectInput(kryo, input));
            return object;
        } catch (Throwable e) {
            throw new KryoException(e);
        } catch (Throwable e2) {
            throw new KryoException(e2);
        } catch (Throwable e22) {
            throw new KryoException(e22);
        }
    }

    private ObjectOutput getObjectOutput(Kryo kryo, Output output) {
        if (this.objectOutput == null) {
            this.objectOutput = new KryoObjectOutput(kryo, output);
        } else {
            this.objectOutput.setOutput(output);
        }
        return this.objectOutput;
    }

    private ObjectInput getObjectInput(Kryo kryo, Input input) {
        if (this.objectInput == null) {
            this.objectInput = new KryoObjectInput(kryo, input);
        } else {
            this.objectInput.setInput(input);
        }
        return this.objectInput;
    }

    private JavaSerializer getJavaSerializerIfRequired(Class type) {
        JavaSerializer javaSerializer = getCachedSerializer(type);
        if (javaSerializer == null && isJavaSerializerRequired(type)) {
            return new JavaSerializer();
        }
        return javaSerializer;
    }

    private JavaSerializer getCachedSerializer(Class type) {
        if (this.javaSerializerByType != null) {
            return (JavaSerializer) this.javaSerializerByType.get(type);
        }
        this.javaSerializerByType = new ObjectMap();
        return null;
    }

    private boolean isJavaSerializerRequired(Class type) {
        return hasInheritableReplaceMethod(type, "writeReplace") || hasInheritableReplaceMethod(type, "readResolve");
    }

    private static boolean hasInheritableReplaceMethod(Class type, String methodName) {
        Method method = null;
        Class<?> current = type;
        while (current != null) {
            try {
                method = current.getDeclaredMethod(methodName, new Class[0]);
                break;
            } catch (NoSuchMethodException e) {
                current = current.getSuperclass();
            }
        }
        if (method == null || method.getReturnType() != Object.class) {
            return false;
        }
        return true;
    }
}
