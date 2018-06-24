package com.esotericsoftware.reflectasm;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.WeakHashMap;

class AccessClassLoader extends ClassLoader {
    private static final WeakHashMap<ClassLoader, WeakReference<AccessClassLoader>> accessClassLoaders = new WeakHashMap();
    private static volatile Method defineClassMethod;
    private static volatile AccessClassLoader selfContextAccessClassLoader = new AccessClassLoader(selfContextParentClassLoader);
    private static final ClassLoader selfContextParentClassLoader = getParentClassLoader(AccessClassLoader.class);

    static AccessClassLoader get(Class type) {
        ClassLoader parent = getParentClassLoader(type);
        if (selfContextParentClassLoader.equals(parent)) {
            if (selfContextAccessClassLoader == null) {
                synchronized (accessClassLoaders) {
                    if (selfContextAccessClassLoader == null) {
                        selfContextAccessClassLoader = new AccessClassLoader(selfContextParentClassLoader);
                    }
                }
            }
            return selfContextAccessClassLoader;
        }
        synchronized (accessClassLoaders) {
            AccessClassLoader accessClassLoader;
            WeakReference<AccessClassLoader> ref = (WeakReference) accessClassLoaders.get(parent);
            if (ref != null) {
                accessClassLoader = (AccessClassLoader) ref.get();
                if (accessClassLoader != null) {
                    return accessClassLoader;
                }
                accessClassLoaders.remove(parent);
            }
            accessClassLoader = new AccessClassLoader(parent);
            accessClassLoaders.put(parent, new WeakReference(accessClassLoader));
            return accessClassLoader;
        }
    }

    public static void remove(ClassLoader parent) {
        if (selfContextParentClassLoader.equals(parent)) {
            selfContextAccessClassLoader = null;
            return;
        }
        synchronized (accessClassLoaders) {
            accessClassLoaders.remove(parent);
        }
    }

    public static int activeAccessClassLoaders() {
        int sz = accessClassLoaders.size();
        if (selfContextAccessClassLoader != null) {
            return sz + 1;
        }
        return sz;
    }

    private AccessClassLoader(ClassLoader parent) {
        super(parent);
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (name.equals(FieldAccess.class.getName())) {
            return FieldAccess.class;
        }
        if (name.equals(MethodAccess.class.getName())) {
            return MethodAccess.class;
        }
        if (name.equals(ConstructorAccess.class.getName())) {
            return ConstructorAccess.class;
        }
        if (name.equals(PublicConstructorAccess.class.getName())) {
            return PublicConstructorAccess.class;
        }
        return super.loadClass(name, resolve);
    }

    Class<?> defineClass(String name, byte[] bytes) throws ClassFormatError {
        try {
            return (Class) getDefineClassMethod().invoke(getParent(), new Object[]{name, bytes, Integer.valueOf(0), Integer.valueOf(bytes.length), getClass().getProtectionDomain()});
        } catch (Exception e) {
            return defineClass(name, bytes, 0, bytes.length, getClass().getProtectionDomain());
        }
    }

    static boolean areInSameRuntimeClassLoader(Class type1, Class type2) {
        boolean z = true;
        if (type1.getPackage() != type2.getPackage()) {
            return false;
        }
        ClassLoader loader1 = type1.getClassLoader();
        ClassLoader loader2 = type2.getClassLoader();
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        if (loader1 == null) {
            if (loader2 == null || loader2 == systemClassLoader) {
                return true;
            }
            return false;
        } else if (loader2 == null) {
            if (loader1 != systemClassLoader) {
                z = false;
            }
            return z;
        } else {
            if (loader1 != loader2) {
                z = false;
            }
            return z;
        }
    }

    private static ClassLoader getParentClassLoader(Class type) {
        ClassLoader parent = type.getClassLoader();
        if (parent == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return parent;
    }

    private static Method getDefineClassMethod() throws Exception {
        if (defineClassMethod == null) {
            synchronized (accessClassLoaders) {
                defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class});
                try {
                    defineClassMethod.setAccessible(true);
                } catch (Exception e) {
                }
            }
        }
        return defineClassMethod;
    }
}
