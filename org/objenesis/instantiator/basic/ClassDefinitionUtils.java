package org.objenesis.instantiator.basic;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import org.objenesis.ObjenesisException;

public final class ClassDefinitionUtils {
    public static final int ACC_ABSTRACT = 1024;
    public static final int ACC_ANNOTATION = 8192;
    public static final int ACC_ENUM = 16384;
    public static final int ACC_FINAL = 16;
    public static final int ACC_INTERFACE = 512;
    public static final int ACC_PUBLIC = 1;
    public static final int ACC_SUPER = 32;
    public static final int ACC_SYNTHETIC = 4096;
    public static final int CONSTANT_Class = 7;
    public static final int CONSTANT_Double = 6;
    public static final int CONSTANT_Fieldref = 9;
    public static final int CONSTANT_Float = 4;
    public static final int CONSTANT_Integer = 3;
    public static final int CONSTANT_InterfaceMethodref = 11;
    public static final int CONSTANT_InvokeDynamic = 18;
    public static final int CONSTANT_Long = 5;
    public static final int CONSTANT_MethodHandle = 15;
    public static final int CONSTANT_MethodType = 16;
    public static final int CONSTANT_Methodref = 10;
    public static final int CONSTANT_NameAndType = 12;
    public static final int CONSTANT_String = 8;
    public static final int CONSTANT_Utf8 = 1;
    private static Method DEFINE_CLASS = null;
    public static final byte[] MAGIC = new byte[]{(byte) -54, (byte) -2, (byte) -70, (byte) -66};
    public static final byte OPS_aload_0 = (byte) 42;
    public static final byte OPS_areturn = (byte) -80;
    public static final byte OPS_dup = (byte) 89;
    public static final byte OPS_invokespecial = (byte) -73;
    public static final byte OPS_new = (byte) -69;
    public static final byte OPS_return = (byte) -79;
    private static final ProtectionDomain PROTECTION_DOMAIN = ((ProtectionDomain) AccessController.doPrivileged(new C15971()));
    public static final byte[] VERSION = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 49};

    static class C15971 implements PrivilegedAction<ProtectionDomain> {
        C15971() {
        }

        public ProtectionDomain run() {
            return ClassDefinitionUtils.class.getProtectionDomain();
        }
    }

    static class C15982 implements PrivilegedAction<Object> {
        C15982() {
        }

        public Object run() {
            try {
                ClassDefinitionUtils.DEFINE_CLASS = Class.forName("java.lang.ClassLoader").getDeclaredMethod("defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class});
                ClassDefinitionUtils.DEFINE_CLASS.setAccessible(true);
                return null;
            } catch (Throwable e) {
                throw new ObjenesisException(e);
            } catch (Throwable e2) {
                throw new ObjenesisException(e2);
            }
        }
    }

    static {
        AccessController.doPrivileged(new C15982());
    }

    private ClassDefinitionUtils() {
    }

    public static <T> Class<T> defineClass(String className, byte[] b, ClassLoader loader) throws Exception {
        Class<T> c = (Class) DEFINE_CLASS.invoke(loader, new Object[]{className, b, new Integer(0), new Integer(b.length), PROTECTION_DOMAIN});
        Class.forName(className, true, loader);
        return c;
    }

    public static byte[] readClass(String className) throws IOException {
        byte[] b = new byte[2500];
        InputStream in = ClassDefinitionUtils.class.getClassLoader().getResourceAsStream(classNameToResource(className));
        try {
            int length = in.read(b);
            if (length >= 2500) {
                throw new IllegalArgumentException("The class is longer that 2500 bytes which is currently unsupported");
            }
            byte[] copy = new byte[length];
            System.arraycopy(b, 0, copy, 0, length);
            return copy;
        } finally {
            in.close();
        }
    }

    public static void writeClass(String fileName, byte[] bytes) throws IOException {
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileName));
        try {
            out.write(bytes);
        } finally {
            out.close();
        }
    }

    public static String classNameToInternalClassName(String className) {
        return className.replace('.', '/');
    }

    public static String classNameToResource(String className) {
        return classNameToInternalClassName(className) + ".class";
    }

    public static <T> Class<T> getExistingClass(ClassLoader classLoader, String className) {
        try {
            return Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
