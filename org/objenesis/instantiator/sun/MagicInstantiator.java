package org.objenesis.instantiator.sun;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.instantiator.basic.ClassDefinitionUtils;

public class MagicInstantiator<T> implements ObjectInstantiator<T> {
    private static int CONSTANT_POOL_COUNT = 19;
    private static final byte[] CONSTRUCTOR_CODE = new byte[]{ClassDefinitionUtils.OPS_aload_0, ClassDefinitionUtils.OPS_invokespecial, (byte) 0, (byte) 13, ClassDefinitionUtils.OPS_return};
    private static final int CONSTRUCTOR_CODE_ATTRIBUTE_LENGTH = (CONSTRUCTOR_CODE.length + 12);
    private static final String CONSTRUCTOR_DESC = "()V";
    private static final String CONSTRUCTOR_NAME = "<init>";
    private static final int INDEX_CLASS_INTERFACE = 9;
    private static final int INDEX_CLASS_OBJECT = 14;
    private static final int INDEX_CLASS_SUPERCLASS = 2;
    private static final int INDEX_CLASS_THIS = 1;
    private static final int INDEX_CLASS_TYPE = 17;
    private static final int INDEX_METHODREF_OBJECT_CONSTRUCTOR = 13;
    private static final int INDEX_NAMEANDTYPE_DEFAULT_CONSTRUCTOR = 16;
    private static final int INDEX_UTF8_CODE_ATTRIBUTE = 5;
    private static final int INDEX_UTF8_CONSTRUCTOR_DESC = 4;
    private static final int INDEX_UTF8_CONSTRUCTOR_NAME = 3;
    private static final int INDEX_UTF8_INSTANTIATOR_CLASS = 7;
    private static final int INDEX_UTF8_INTERFACE = 10;
    private static final int INDEX_UTF8_NEWINSTANCE_DESC = 12;
    private static final int INDEX_UTF8_NEWINSTANCE_NAME = 11;
    private static final int INDEX_UTF8_OBJECT = 15;
    private static final int INDEX_UTF8_SUPERCLASS = 8;
    private static final int INDEX_UTF8_TYPE = 18;
    private static final byte[] NEWINSTANCE_CODE = new byte[]{ClassDefinitionUtils.OPS_new, (byte) 0, (byte) 17, ClassDefinitionUtils.OPS_dup, ClassDefinitionUtils.OPS_invokespecial, (byte) 0, (byte) 13, ClassDefinitionUtils.OPS_areturn};
    private static final int NEWINSTANCE_CODE_ATTRIBUTE_LENGTH = (NEWINSTANCE_CODE.length + 12);
    private ObjectInstantiator<T> instantiator;

    public MagicInstantiator(Class<T> type) {
        this.instantiator = newInstantiatorOf(type);
    }

    private <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
        String className = getClass().getName() + "$$$" + type.getSimpleName();
        Class<ObjectInstantiator<T>> clazz = ClassDefinitionUtils.getExistingClass(getClass().getClassLoader(), className);
        if (clazz == null) {
            try {
                clazz = ClassDefinitionUtils.defineClass(className, writeExtendingClass(type, className), getClass().getClassLoader());
            } catch (Throwable e) {
                throw new ObjenesisException(e);
            }
        }
        try {
            return (ObjectInstantiator) clazz.newInstance();
        } catch (Throwable e2) {
            throw new ObjenesisException(e2);
        } catch (Throwable e22) {
            throw new ObjenesisException(e22);
        }
    }

    private byte[] writeExtendingClass(Class<?> type, String className) {
        Throwable e;
        Throwable th;
        String clazz = ClassDefinitionUtils.classNameToInternalClassName(className);
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
                in2.writeUTF("sun/reflect/MagicAccessorImpl");
                in2.writeByte(7);
                in2.writeShort(10);
                in2.writeByte(1);
                in2.writeUTF(ObjectInstantiator.class.getName().replace('.', '/'));
                in2.writeByte(1);
                in2.writeUTF("newInstance");
                in2.writeByte(1);
                in2.writeUTF("()Ljava/lang/Object;");
                in2.writeByte(10);
                in2.writeShort(14);
                in2.writeShort(16);
                in2.writeByte(7);
                in2.writeShort(15);
                in2.writeByte(1);
                in2.writeUTF("java/lang/Object");
                in2.writeByte(12);
                in2.writeShort(3);
                in2.writeShort(4);
                in2.writeByte(7);
                in2.writeShort(18);
                in2.writeByte(1);
                in2.writeUTF(ClassDefinitionUtils.classNameToInternalClassName(type.getName()));
                in2.writeShort(49);
                in2.writeShort(1);
                in2.writeShort(2);
                in2.writeShort(1);
                in2.writeShort(9);
                in2.writeShort(0);
                in2.writeShort(2);
                in2.writeShort(1);
                in2.writeShort(3);
                in2.writeShort(4);
                in2.writeShort(1);
                in2.writeShort(5);
                in2.writeInt(CONSTRUCTOR_CODE_ATTRIBUTE_LENGTH);
                in2.writeShort(0);
                in2.writeShort(1);
                in2.writeInt(CONSTRUCTOR_CODE.length);
                in2.write(CONSTRUCTOR_CODE);
                in2.writeShort(0);
                in2.writeShort(0);
                in2.writeShort(1);
                in2.writeShort(11);
                in2.writeShort(12);
                in2.writeShort(1);
                in2.writeShort(5);
                in2.writeInt(NEWINSTANCE_CODE_ATTRIBUTE_LENGTH);
                in2.writeShort(2);
                in2.writeShort(1);
                in2.writeInt(NEWINSTANCE_CODE.length);
                in2.write(NEWINSTANCE_CODE);
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

    public T newInstance() {
        return this.instantiator.newInstance();
    }
}
