package com.esotericsoftware.kryo.util;

import com.esotericsoftware.minlog.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import sun.misc.Cleaner;
import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

public class UnsafeUtil {
    private static final Unsafe _unsafe;
    public static final long byteArrayBaseOffset;
    public static final long charArrayBaseOffset;
    static Constructor<? extends ByteBuffer> directByteBufferConstr;
    public static final long doubleArrayBaseOffset;
    public static final long floatArrayBaseOffset;
    public static final long intArrayBaseOffset;
    public static final long longArrayBaseOffset;
    public static final long shortArrayBaseOffset;

    static class C04781 implements Comparator<Field> {
        C04781() {
        }

        public int compare(Field f1, Field f2) {
            long offset1 = UnsafeUtil.unsafe().objectFieldOffset(f1);
            long offset2 = UnsafeUtil.unsafe().objectFieldOffset(f2);
            if (offset1 < offset2) {
                return -1;
            }
            if (offset1 == offset2) {
                return 0;
            }
            return 1;
        }
    }

    static {
        Unsafe tmpUnsafe = null;
        long tmpByteArrayBaseOffset = 0;
        long tmpFloatArrayBaseOffset = 0;
        long tmpDoubleArrayBaseOffset = 0;
        long tmpIntArrayBaseOffset = 0;
        long tmpLongArrayBaseOffset = 0;
        long tmpShortArrayBaseOffset = 0;
        long tmpCharArrayBaseOffset = 0;
        try {
            if (!Util.isAndroid) {
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                tmpUnsafe = (Unsafe) field.get(null);
                tmpByteArrayBaseOffset = (long) tmpUnsafe.arrayBaseOffset(byte[].class);
                tmpCharArrayBaseOffset = (long) tmpUnsafe.arrayBaseOffset(char[].class);
                tmpShortArrayBaseOffset = (long) tmpUnsafe.arrayBaseOffset(short[].class);
                tmpIntArrayBaseOffset = (long) tmpUnsafe.arrayBaseOffset(int[].class);
                tmpFloatArrayBaseOffset = (long) tmpUnsafe.arrayBaseOffset(float[].class);
                tmpLongArrayBaseOffset = (long) tmpUnsafe.arrayBaseOffset(long[].class);
                tmpDoubleArrayBaseOffset = (long) tmpUnsafe.arrayBaseOffset(double[].class);
            } else if (Log.TRACE) {
                Log.trace("kryo", "Running on Android platform. Use of sun.misc.Unsafe should be disabled");
            }
        } catch (Exception e) {
            if (Log.TRACE) {
                Log.trace("kryo", "sun.misc.Unsafe is not accessible or not available. Use of sun.misc.Unsafe should be disabled");
            }
        }
        byteArrayBaseOffset = tmpByteArrayBaseOffset;
        charArrayBaseOffset = tmpCharArrayBaseOffset;
        shortArrayBaseOffset = tmpShortArrayBaseOffset;
        intArrayBaseOffset = tmpIntArrayBaseOffset;
        floatArrayBaseOffset = tmpFloatArrayBaseOffset;
        longArrayBaseOffset = tmpLongArrayBaseOffset;
        doubleArrayBaseOffset = tmpDoubleArrayBaseOffset;
        _unsafe = tmpUnsafe;
        try {
            directByteBufferConstr = ByteBuffer.allocateDirect(1).getClass().getDeclaredConstructor(new Class[]{Long.TYPE, Integer.TYPE, Object.class});
            directByteBufferConstr.setAccessible(true);
        } catch (Exception e2) {
            directByteBufferConstr = null;
        }
    }

    public static final Unsafe unsafe() {
        return _unsafe;
    }

    public static Field[] sortFieldsByOffset(List<Field> allFields) {
        Field[] allFieldsArray = (Field[]) allFields.toArray(new Field[0]);
        Arrays.sort(allFieldsArray, new C04781());
        for (Field f : allFields) {
            if (Log.TRACE) {
                Log.trace("kryo", "Field '" + f.getName() + "' at offset " + unsafe().objectFieldOffset(f));
            }
        }
        return allFieldsArray;
    }

    public static final ByteBuffer getDirectBufferAt(long address, int size) {
        ByteBuffer byteBuffer = null;
        if (directByteBufferConstr != null) {
            try {
                byteBuffer = (ByteBuffer) directByteBufferConstr.newInstance(new Object[]{Long.valueOf(address), Integer.valueOf(size), null});
            } catch (Exception e) {
                throw new RuntimeException("Cannot allocate ByteBuffer at a given address: " + address, e);
            }
        }
        return byteBuffer;
    }

    public static void releaseBuffer(ByteBuffer niobuffer) {
        if (niobuffer != null && niobuffer.isDirect()) {
            Cleaner cleaner = ((DirectBuffer) niobuffer).cleaner();
            if (cleaner != null) {
                cleaner.clean();
            }
        }
    }
}
