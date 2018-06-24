package com.esotericsoftware.kryo.io;

import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.util.UnsafeUtil;
import com.esotericsoftware.kryo.util.Util;
import java.io.OutputStream;
import java.nio.ByteOrder;

public final class UnsafeOutput extends Output {
    private static final boolean isLittleEndian = ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN);
    private boolean supportVarInts;

    public UnsafeOutput() {
        this.supportVarInts = false;
    }

    public UnsafeOutput(int bufferSize) {
        this(bufferSize, bufferSize);
    }

    public UnsafeOutput(int bufferSize, int maxBufferSize) {
        super(bufferSize, maxBufferSize);
        this.supportVarInts = false;
    }

    public UnsafeOutput(byte[] buffer) {
        this(buffer, buffer.length);
    }

    public UnsafeOutput(byte[] buffer, int maxBufferSize) {
        super(buffer, maxBufferSize);
        this.supportVarInts = false;
    }

    public UnsafeOutput(OutputStream outputStream) {
        super(outputStream);
        this.supportVarInts = false;
    }

    public UnsafeOutput(OutputStream outputStream, int bufferSize) {
        super(outputStream, bufferSize);
        this.supportVarInts = false;
    }

    public final void writeInt(int value) throws KryoException {
        require(4);
        UnsafeUtil.unsafe().putInt(this.buffer, UnsafeUtil.byteArrayBaseOffset + ((long) this.position), value);
        this.position += 4;
    }

    private final void writeLittleEndianInt(int val) {
        if (isLittleEndian) {
            writeInt(val);
        } else {
            writeInt(Util.swapInt(val));
        }
    }

    public final void writeFloat(float value) throws KryoException {
        require(4);
        UnsafeUtil.unsafe().putFloat(this.buffer, UnsafeUtil.byteArrayBaseOffset + ((long) this.position), value);
        this.position += 4;
    }

    public final void writeShort(int value) throws KryoException {
        require(2);
        UnsafeUtil.unsafe().putShort(this.buffer, UnsafeUtil.byteArrayBaseOffset + ((long) this.position), (short) value);
        this.position += 2;
    }

    public final void writeLong(long value) throws KryoException {
        require(8);
        UnsafeUtil.unsafe().putLong(this.buffer, UnsafeUtil.byteArrayBaseOffset + ((long) this.position), value);
        this.position += 8;
    }

    private final void writeLittleEndianLong(long val) {
        if (isLittleEndian) {
            writeLong(val);
        } else {
            writeLong(Util.swapLong(val));
        }
    }

    public final void writeDouble(double value) throws KryoException {
        require(8);
        UnsafeUtil.unsafe().putDouble(this.buffer, UnsafeUtil.byteArrayBaseOffset + ((long) this.position), value);
        this.position += 8;
    }

    public final void writeChar(char value) throws KryoException {
        require(2);
        UnsafeUtil.unsafe().putChar(this.buffer, UnsafeUtil.byteArrayBaseOffset + ((long) this.position), value);
        this.position += 2;
    }

    public final int writeInt(int value, boolean optimizePositive) throws KryoException {
        if (this.supportVarInts) {
            return writeVarInt(value, optimizePositive);
        }
        writeInt(value);
        return 4;
    }

    public final int writeLong(long value, boolean optimizePositive) throws KryoException {
        if (this.supportVarInts) {
            return writeVarLong(value, optimizePositive);
        }
        writeLong(value);
        return 8;
    }

    public final int writeVarInt(int val, boolean optimizePositive) throws KryoException {
        int value = val;
        if (!optimizePositive) {
            value = (value << 1) ^ (value >> 31);
        }
        int varInt = value & 127;
        value >>>= 7;
        if (value == 0) {
            write(varInt);
            return 1;
        }
        varInt = (varInt | 128) | ((value & 127) << 8);
        value >>>= 7;
        if (value == 0) {
            writeLittleEndianInt(varInt);
            this.position -= 2;
            return 2;
        }
        varInt = (varInt | 32768) | ((value & 127) << 16);
        value >>>= 7;
        if (value == 0) {
            writeLittleEndianInt(varInt);
            this.position--;
            return 3;
        }
        varInt = (varInt | 8388608) | ((value & 127) << 24);
        value >>>= 7;
        if (value == 0) {
            writeLittleEndianInt(varInt);
            this.position += 0;
            return 4;
        }
        writeLittleEndianLong(((((long) varInt) | 2147483648L) | (((long) (value & 127)) << 32)) & 68719476735L);
        this.position -= 3;
        return 5;
    }

    public final int writeVarLong(long value, boolean optimizePositive) throws KryoException {
        if (!optimizePositive) {
            value = (value << 1) ^ (value >> 63);
        }
        int varInt = (int) (127 & value);
        value >>>= 7;
        if (value == 0) {
            writeByte(varInt);
            return 1;
        }
        varInt = (int) (((long) (varInt | 128)) | ((127 & value) << 8));
        value >>>= 7;
        if (value == 0) {
            writeLittleEndianInt(varInt);
            this.position -= 2;
            return 2;
        }
        varInt = (int) (((long) (varInt | 32768)) | ((127 & value) << 16));
        value >>>= 7;
        if (value == 0) {
            writeLittleEndianInt(varInt);
            this.position--;
            return 3;
        }
        varInt = (int) (((long) (varInt | 8388608)) | ((127 & value) << 24));
        value >>>= 7;
        if (value == 0) {
            writeLittleEndianInt(varInt);
            this.position += 0;
            return 4;
        }
        long varLong = ((((long) varInt) & 4294967295L) | 2147483648L) | ((127 & value) << 32);
        value >>>= 7;
        if (value == 0) {
            writeLittleEndianLong(varLong);
            this.position -= 3;
            return 5;
        }
        varLong = (varLong | 549755813888L) | ((127 & value) << 40);
        value >>>= 7;
        if (value == 0) {
            writeLittleEndianLong(varLong);
            this.position -= 2;
            return 6;
        }
        varLong = (varLong | 140737488355328L) | ((127 & value) << 48);
        value >>>= 7;
        if (value == 0) {
            writeLittleEndianLong(varLong);
            this.position--;
            return 7;
        }
        varLong = (varLong | 36028797018963968L) | ((127 & value) << 56);
        value >>>= 7;
        if (value == 0) {
            writeLittleEndianLong(varLong);
            return 8;
        }
        writeLittleEndianLong(varLong | Long.MIN_VALUE);
        write((int) (255 & value));
        return 9;
    }

    public final void writeInts(int[] object, boolean optimizePositive) throws KryoException {
        if (this.supportVarInts) {
            super.writeInts(object, optimizePositive);
            return;
        }
        Object obj = object;
        writeBytes(obj, UnsafeUtil.intArrayBaseOffset, 0, (long) (object.length << 2));
    }

    public final void writeLongs(long[] object, boolean optimizePositive) throws KryoException {
        if (this.supportVarInts) {
            super.writeLongs(object, optimizePositive);
            return;
        }
        Object obj = object;
        writeBytes(obj, UnsafeUtil.longArrayBaseOffset, 0, (long) (object.length << 3));
    }

    public final void writeInts(int[] object) throws KryoException {
        Object obj = object;
        writeBytes(obj, UnsafeUtil.intArrayBaseOffset, 0, (long) (object.length << 2));
    }

    public final void writeLongs(long[] object) throws KryoException {
        Object obj = object;
        writeBytes(obj, UnsafeUtil.longArrayBaseOffset, 0, (long) (object.length << 3));
    }

    public final void writeFloats(float[] object) throws KryoException {
        Object obj = object;
        writeBytes(obj, UnsafeUtil.floatArrayBaseOffset, 0, (long) (object.length << 2));
    }

    public final void writeShorts(short[] object) throws KryoException {
        Object obj = object;
        writeBytes(obj, UnsafeUtil.shortArrayBaseOffset, 0, (long) (object.length << 1));
    }

    public final void writeChars(char[] object) throws KryoException {
        Object obj = object;
        writeBytes(obj, UnsafeUtil.charArrayBaseOffset, 0, (long) (object.length << 1));
    }

    public final void writeDoubles(double[] object) throws KryoException {
        Object obj = object;
        writeBytes(obj, UnsafeUtil.doubleArrayBaseOffset, 0, (long) (object.length << 3));
    }

    public final void writeBytes(Object obj, long offset, long count) throws KryoException {
        writeBytes(obj, 0, offset, count);
    }

    private final void writeBytes(Object srcArray, long srcArrayTypeOffset, long srcOffset, long count) throws KryoException {
        int copyCount = Math.min(this.capacity - this.position, (int) count);
        while (true) {
            UnsafeUtil.unsafe().copyMemory(srcArray, srcArrayTypeOffset + srcOffset, this.buffer, UnsafeUtil.byteArrayBaseOffset + ((long) this.position), (long) copyCount);
            this.position += copyCount;
            count -= (long) copyCount;
            if (count != 0) {
                srcOffset += (long) copyCount;
                copyCount = Math.min(this.capacity, (int) count);
                require(copyCount);
            } else {
                return;
            }
        }
    }

    public boolean supportVarInts() {
        return this.supportVarInts;
    }

    public void supportVarInts(boolean supportVarInts) {
        this.supportVarInts = supportVarInts;
    }
}
