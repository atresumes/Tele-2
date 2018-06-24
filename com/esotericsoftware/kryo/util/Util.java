package com.esotericsoftware.kryo.util;

import android.support.v4.view.MotionEventCompat;
import com.esotericsoftware.minlog.Log;
import java.util.concurrent.ConcurrentHashMap;
import org.objenesis.strategy.PlatformDescription;

public class Util {
    private static final ConcurrentHashMap<String, Boolean> classAvailabilities = new ConcurrentHashMap();
    public static boolean isAndroid = PlatformDescription.DALVIK.equals(System.getProperty("java.vm.name"));

    public static boolean isClassAvailable(String className) {
        Boolean result = (Boolean) classAvailabilities.get(className);
        if (result == null) {
            try {
                Class.forName(className);
                result = Boolean.valueOf(true);
            } catch (Exception e) {
                Log.debug("kryo", "Class not available: " + className);
                result = Boolean.valueOf(false);
            }
            classAvailabilities.put(className, result);
        }
        return result.booleanValue();
    }

    public static Class getWrapperClass(Class type) {
        if (type == Integer.TYPE) {
            return Integer.class;
        }
        if (type == Float.TYPE) {
            return Float.class;
        }
        if (type == Boolean.TYPE) {
            return Boolean.class;
        }
        if (type == Long.TYPE) {
            return Long.class;
        }
        if (type == Byte.TYPE) {
            return Byte.class;
        }
        if (type == Character.TYPE) {
            return Character.class;
        }
        if (type == Short.TYPE) {
            return Short.class;
        }
        if (type == Double.TYPE) {
            return Double.class;
        }
        return Void.class;
    }

    public static Class getPrimitiveClass(Class type) {
        if (type == Integer.class) {
            return Integer.TYPE;
        }
        if (type == Float.class) {
            return Float.TYPE;
        }
        if (type == Boolean.class) {
            return Boolean.TYPE;
        }
        if (type == Long.class) {
            return Long.TYPE;
        }
        if (type == Byte.class) {
            return Byte.TYPE;
        }
        if (type == Character.class) {
            return Character.TYPE;
        }
        if (type == Short.class) {
            return Short.TYPE;
        }
        if (type == Double.class) {
            return Double.TYPE;
        }
        if (type == Void.class) {
            return Void.TYPE;
        }
        return type;
    }

    public static boolean isWrapperClass(Class type) {
        return type == Integer.class || type == Float.class || type == Boolean.class || type == Long.class || type == Byte.class || type == Character.class || type == Short.class || type == Double.class;
    }

    public static void log(String message, Object object) {
        if (object != null) {
            Class type = object.getClass();
            if (!type.isPrimitive() && type != Boolean.class && type != Byte.class && type != Character.class && type != Short.class && type != Integer.class && type != Long.class && type != Float.class && type != Double.class && type != String.class) {
                Log.debug("kryo", message + ": " + string(object));
            } else if (Log.TRACE) {
                Log.trace("kryo", message + ": " + string(object));
            }
        } else if (Log.TRACE) {
            Log.trace("kryo", message + ": null");
        }
    }

    public static String string(Object object) {
        if (object == null) {
            return "null";
        }
        Class type = object.getClass();
        if (type.isArray()) {
            return className(type);
        }
        try {
            if (type.getMethod("toString", new Class[0]).getDeclaringClass() == Object.class) {
                return Log.TRACE ? className(type) : type.getSimpleName();
            }
        } catch (Exception e) {
        }
        try {
            return String.valueOf(object);
        } catch (Throwable e2) {
            return (Log.TRACE ? className(type) : type.getSimpleName()) + "(Exception " + e2 + " in toString)";
        }
    }

    public static String className(Class type) {
        if (type.isArray()) {
            Class elementClass = getElementClass(type);
            StringBuilder buffer = new StringBuilder(16);
            int n = getDimensionCount(type);
            for (int i = 0; i < n; i++) {
                buffer.append("[]");
            }
            return className(elementClass) + buffer;
        } else if (type.isPrimitive() || type == Object.class || type == Boolean.class || type == Byte.class || type == Character.class || type == Short.class || type == Integer.class || type == Long.class || type == Float.class || type == Double.class || type == String.class) {
            return type.getSimpleName();
        } else {
            return type.getName();
        }
    }

    public static int getDimensionCount(Class arrayClass) {
        int depth = 0;
        for (Class nextClass = arrayClass.getComponentType(); nextClass != null; nextClass = nextClass.getComponentType()) {
            depth++;
        }
        return depth;
    }

    public static Class getElementClass(Class arrayClass) {
        Class elementClass = arrayClass;
        while (elementClass.getComponentType() != null) {
            elementClass = elementClass.getComponentType();
        }
        return elementClass;
    }

    public static int swapInt(int i) {
        return ((((i & 255) << 24) | ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i) << 8)) | ((16711680 & i) >> 8)) | ((i >> 24) & 255);
    }

    public static long swapLong(long value) {
        return (((((((((value >> 0) & 255) << 56) | (((value >> 8) & 255) << 48)) | (((value >> 16) & 255) << 40)) | (((value >> 24) & 255) << 32)) | (((value >> 32) & 255) << 24)) | (((value >> 40) & 255) << 16)) | (((value >> 48) & 255) << 8)) | (((value >> 56) & 255) << 0);
    }
}
