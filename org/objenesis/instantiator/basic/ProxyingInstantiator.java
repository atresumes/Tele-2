package org.objenesis.instantiator.basic;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class ProxyingInstantiator<T> implements ObjectInstantiator<T> {
    private static final byte[] CODE = new byte[]{ClassDefinitionUtils.OPS_aload_0, ClassDefinitionUtils.OPS_return};
    private static final int CODE_ATTRIBUTE_LENGTH = (CODE.length + 12);
    private static int CONSTANT_POOL_COUNT = 9;
    private static final String CONSTRUCTOR_DESC = "()V";
    private static final String CONSTRUCTOR_NAME = "<init>";
    private static final int INDEX_CLASS_SUPERCLASS = 2;
    private static final int INDEX_CLASS_THIS = 1;
    private static final int INDEX_UTF8_CLASS = 7;
    private static final int INDEX_UTF8_CODE_ATTRIBUTE = 5;
    private static final int INDEX_UTF8_CONSTRUCTOR_DESC = 4;
    private static final int INDEX_UTF8_CONSTRUCTOR_NAME = 3;
    private static final int INDEX_UTF8_SUPERCLASS = 8;
    private static final String SUFFIX = "$$$Objenesis";
    private final Class<?> newType;

    public ProxyingInstantiator(Class<T> type) {
        try {
            this.newType = ClassDefinitionUtils.defineClass(type.getName() + SUFFIX, writeExtendingClass(type, SUFFIX), type.getClassLoader());
        } catch (Throwable e) {
            throw new ObjenesisException(e);
        }
    }

    public T newInstance() {
        try {
            return this.newType.newInstance();
        } catch (Throwable e) {
            throw new ObjenesisException(e);
        } catch (Throwable e2) {
            throw new ObjenesisException(e2);
        }
    }

    private static byte[] writeExtendingClass(Class<?> type, String suffix) {
        Throwable e;
        Throwable th;
        String parentClazz = ClassDefinitionUtils.classNameToInternalClassName(type.getName());
        String clazz = parentClazz + suffix;
        DataOutputStream in = null;
        ByteArrayOutputStream bIn = new ByteArrayOutputStream(1000);
        try {
            DataOutputStream in2 = new DataOutputStream(bIn);
            try {
                in2.write(ClassDefinitionUtils.MAGIC);
                in2.write(ClassDefinitionUtils.VERSION);
                in2.writeShort(CONSTANT_POOL_COUNT);
                in2.writeByte(7);
                in2.writeShort(7);
                in2.writeByte(7);
                in2.writeShort(8);
                in2.writeByte(1);
                in2.writeUTF(CONSTRUCTOR_NAME);
                in2.writeByte(1);
                in2.writeUTF(CONSTRUCTOR_DESC);
                in2.writeByte(1);
                in2.writeUTF("Code");
                in2.writeByte(1);
                in2.writeUTF("L" + clazz + ";");
                in2.writeByte(1);
                in2.writeUTF(clazz);
                in2.writeByte(1);
                in2.writeUTF(parentClazz);
                in2.writeShort(33);
                in2.writeShort(1);
                in2.writeShort(2);
                in2.writeShort(0);
                in2.writeShort(0);
                in2.writeShort(1);
                in2.writeShort(1);
                in2.writeShort(3);
                in2.writeShort(4);
                in2.writeShort(1);
                in2.writeShort(5);
                in2.writeInt(CODE_ATTRIBUTE_LENGTH);
                in2.writeShort(1);
                in2.writeShort(1);
                in2.writeInt(CODE.length);
                in2.write(CODE);
                in2.writeShort(0);
                in2.writeShort(0);
                in2.writeShort(0);
                if (in2 != null) {
                    try {
                        in2.close();
                    } catch (Throwable e2) {
                        throw new ObjenesisException(e2);
                    }
                }
                return bIn.toByteArray();
            } catch (IOException e3) {
                e2 = e3;
                in = in2;
                try {
                    throw new ObjenesisException(e2);
                } catch (Throwable th2) {
                    th = th2;
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Throwable e22) {
                            throw new ObjenesisException(e22);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                in = in2;
                if (in != null) {
                    in.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            e22 = e4;
            throw new ObjenesisException(e22);
        }
    }
}
