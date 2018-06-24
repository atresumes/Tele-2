package com.esotericsoftware.kryo.io;

import android.support.v4.media.session.PlaybackStateCompat;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.util.UnsafeUtil;
import com.esotericsoftware.kryo.util.Util;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import sun.nio.ch.DirectBuffer;

public final class UnsafeMemoryOutput extends ByteBufferOutput {
    private static final boolean isLittleEndian = ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN);
    private long bufaddress;

    public UnsafeMemoryOutput() {
        this.varIntsEnabled = false;
    }

    public UnsafeMemoryOutput(int bufferSize) {
        this(bufferSize, bufferSize);
    }

    public UnsafeMemoryOutput(int bufferSize, int maxBufferSize) {
        super(bufferSize, maxBufferSize);
        this.varIntsEnabled = false;
        updateBufferAddress();
    }

    public UnsafeMemoryOutput(OutputStream outputStream) {
        super(outputStream);
        this.varIntsEnabled = false;
        updateBufferAddress();
    }

    public UnsafeMemoryOutput(OutputStream outputStream, int bufferSize) {
        super(outputStream, bufferSize);
        this.varIntsEnabled = false;
        updateBufferAddress();
    }

    public UnsafeMemoryOutput(long address, int maxBufferSize) {
        super(address, maxBufferSize);
        this.varIntsEnabled = false;
        updateBufferAddress();
    }

    public void setBuffer(ByteBuffer buffer, int maxBufferSize) {
        super.setBuffer(buffer, maxBufferSize);
        updateBufferAddress();
    }

    private void updateBufferAddress() {
        this.bufaddress = ((DirectBuffer) this.niobuffer).address();
    }

    public final void writeInt(int value) throws KryoException {
        require(4);
        UnsafeUtil.unsafe().putInt(this.bufaddress + ((long) this.position), value);
        this.position += 4;
    }

    public final void writeFloat(float value) throws KryoException {
        require(4);
        UnsafeUtil.unsafe().putFloat(this.bufaddress + ((long) this.position), value);
        this.position += 4;
    }

    public final void writeShort(int value) throws KryoException {
        require(2);
        UnsafeUtil.unsafe().putShort(this.bufaddress + ((long) this.position), (short) value);
        this.position += 2;
    }

    public final void writeLong(long value) throws KryoException {
        require(8);
        UnsafeUtil.unsafe().putLong(this.bufaddress + ((long) this.position), value);
        this.position += 8;
    }

    public final void writeByte(int value) throws KryoException {
        this.niobuffer.position(this.position);
        super.writeByte(value);
    }

    public void writeByte(byte value) throws KryoException {
        this.niobuffer.position(this.position);
        super.writeByte(value);
    }

    public final void writeBoolean(boolean value) throws KryoException {
        this.niobuffer.position(this.position);
        super.writeBoolean(value);
    }

    public final void writeChar(char value) throws KryoException {
        require(2);
        UnsafeUtil.unsafe().putChar(this.bufaddress + ((long) this.position), value);
        this.position += 2;
    }

    public final void writeDouble(double value) throws KryoException {
        require(8);
        UnsafeUtil.unsafe().putDouble(this.bufaddress + ((long) this.position), value);
        this.position += 8;
    }

    public final int writeInt(int value, boolean optimizePositive) throws KryoException {
        if (this.varIntsEnabled) {
            return writeVarInt(value, optimizePositive);
        }
        writeInt(value);
        return 4;
    }

    public final int writeLong(long value, boolean optimizePositive) throws KryoException {
        if (this.varIntsEnabled) {
            return writeVarLong(value, optimizePositive);
        }
        writeLong(value);
        return 8;
    }

    public final int writeVarInt(int val, boolean optimizePositive) throws KryoException {
        long value = (long) val;
        if (!optimizePositive) {
            value = (value << 1) ^ (value >> 31);
        }
        long varInt = value & 127;
        value >>>= 7;
        if (value == 0) {
            writeByte((byte) ((int) varInt));
            return 1;
        }
        varInt = (varInt | 128) | ((127 & value) << 8);
        value >>>= 7;
        if (value == 0) {
            writeLittleEndianInt((int) varInt);
            this.position -= 2;
            return 2;
        }
        varInt = (varInt | PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID) | ((127 & value) << 16);
        value >>>= 7;
        if (value == 0) {
            writeLittleEndianInt((int) varInt);
            this.position--;
            return 3;
        }
        varInt = (varInt | 8388608) | ((127 & value) << 24);
        value >>>= 7;
        if (value == 0) {
            writeLittleEndianInt((int) varInt);
            this.position += 0;
            return 4;
        }
        writeLittleEndianLong(((varInt | 2147483648L) | ((127 & value) << 32)) & 68719476735L);
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
        long varLong = (((long) ((int) (((long) varInt) | 2147483648L))) & 4294967295L) | ((127 & value) << 32);
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
        writeByte((int) (255 & value));
        return 9;
    }

    private final void writeLittleEndianInt(int val) {
        if (isLittleEndian) {
            writeInt(val);
        } else {
            writeInt(Util.swapInt(val));
        }
    }

    private final void writeLittleEndianLong(long val) {
        if (isLittleEndian) {
            writeLong(val);
        } else {
            writeLong(Util.swapLong(val));
        }
    }

    public final void writeInts(int[] object, boolean optimizePositive) throws KryoException {
        if (this.varIntsEnabled) {
            super.writeInts(object, optimizePositive);
            return;
        }
        Object obj = object;
        writeBytes(obj, UnsafeUtil.intArrayBaseOffset, 0, (long) (object.length << 2));
    }

    public final void writeLongs(long[] object, boolean optimizePositive) throws KryoException {
        if (this.varIntsEnabled) {
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

    public void writeBytes(byte[] bytes) throws KryoException {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        }
        writeBytes(bytes, 0, (long) bytes.length);
    }

    public final void writeBytes(Object obj, long offset, long count) throws KryoException {
        writeBytes(obj, UnsafeUtil.byteArrayBaseOffset, offset, count);
    }

    private final void writeBytes(Object srcArray, long srcArrayTypeOffset, long srcOffset, long count) throws KryoException {
        int copyCount = Math.min(this.capacity - this.position, (int) count);
        while (true) {
            UnsafeUtil.unsafe().copyMemory(srcArray, srcArrayTypeOffset + srcOffset, null, this.bufaddress + ((long) this.position), (long) copyCount);
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
}
