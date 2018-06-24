package com.esotericsoftware.kryo.io;

import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.util.UnsafeUtil;
import java.io.InputStream;
import java.nio.ByteBuffer;
import sun.nio.ch.DirectBuffer;

public final class UnsafeMemoryInput extends ByteBufferInput {
    private long bufaddress;

    public UnsafeMemoryInput() {
        this.varIntsEnabled = false;
    }

    public UnsafeMemoryInput(int bufferSize) {
        super(bufferSize);
        this.varIntsEnabled = false;
        updateBufferAddress();
    }

    public UnsafeMemoryInput(byte[] buffer) {
        super(buffer);
        this.varIntsEnabled = false;
        updateBufferAddress();
    }

    public UnsafeMemoryInput(ByteBuffer buffer) {
        super(buffer);
        this.varIntsEnabled = false;
        updateBufferAddress();
    }

    public UnsafeMemoryInput(long address, int maxBufferSize) {
        super(address, maxBufferSize);
        this.varIntsEnabled = false;
        updateBufferAddress();
    }

    public UnsafeMemoryInput(InputStream inputStream) {
        super(inputStream);
        this.varIntsEnabled = false;
        updateBufferAddress();
    }

    public UnsafeMemoryInput(InputStream inputStream, int bufferSize) {
        super(inputStream, bufferSize);
        this.varIntsEnabled = false;
        updateBufferAddress();
    }

    public void setBuffer(ByteBuffer buffer) {
        super.setBuffer(buffer);
        updateBufferAddress();
    }

    private void updateBufferAddress() {
        this.bufaddress = ((DirectBuffer) this.niobuffer).address();
    }

    public int readInt() throws KryoException {
        require(4);
        int result = UnsafeUtil.unsafe().getInt(this.bufaddress + ((long) this.position));
        this.position += 4;
        return result;
    }

    public float readFloat() throws KryoException {
        require(4);
        float result = UnsafeUtil.unsafe().getFloat(this.bufaddress + ((long) this.position));
        this.position += 4;
        return result;
    }

    public short readShort() throws KryoException {
        require(2);
        short result = UnsafeUtil.unsafe().getShort(this.bufaddress + ((long) this.position));
        this.position += 2;
        return result;
    }

    public long readLong() throws KryoException {
        require(8);
        long result = UnsafeUtil.unsafe().getLong(this.bufaddress + ((long) this.position));
        this.position += 8;
        return result;
    }

    public boolean readBoolean() throws KryoException {
        this.niobuffer.position(this.position);
        return super.readBoolean();
    }

    public byte readByte() throws KryoException {
        this.niobuffer.position(this.position);
        return super.readByte();
    }

    public char readChar() throws KryoException {
        require(2);
        char result = UnsafeUtil.unsafe().getChar(this.bufaddress + ((long) this.position));
        this.position += 2;
        return result;
    }

    public double readDouble() throws KryoException {
        require(8);
        double result = UnsafeUtil.unsafe().getDouble(this.bufaddress + ((long) this.position));
        this.position += 8;
        return result;
    }

    public int readInt(boolean optimizePositive) throws KryoException {
        if (this.varIntsEnabled) {
            return super.readInt(optimizePositive);
        }
        return readInt();
    }

    public long readLong(boolean optimizePositive) throws KryoException {
        if (this.varIntsEnabled) {
            return super.readLong(optimizePositive);
        }
        return readLong();
    }

    public final int[] readInts(int length, boolean optimizePositive) throws KryoException {
        if (this.varIntsEnabled) {
            return super.readInts(length, optimizePositive);
        }
        int[] array = new int[length];
        readBytes(array, UnsafeUtil.intArrayBaseOffset, 0, length << 2);
        return array;
    }

    public final long[] readLongs(int length, boolean optimizePositive) throws KryoException {
        if (this.varIntsEnabled) {
            return super.readLongs(length, optimizePositive);
        }
        long[] array = new long[length];
        readBytes(array, UnsafeUtil.longArrayBaseOffset, 0, length << 3);
        return array;
    }

    public final float[] readFloats(int length) throws KryoException {
        float[] array = new float[length];
        readBytes(array, UnsafeUtil.floatArrayBaseOffset, 0, length << 2);
        return array;
    }

    public final short[] readShorts(int length) throws KryoException {
        short[] array = new short[length];
        readBytes(array, UnsafeUtil.shortArrayBaseOffset, 0, length << 1);
        return array;
    }

    public final char[] readChars(int length) throws KryoException {
        char[] array = new char[length];
        readBytes(array, UnsafeUtil.charArrayBaseOffset, 0, length << 1);
        return array;
    }

    public final double[] readDoubles(int length) throws KryoException {
        double[] array = new double[length];
        readBytes(array, UnsafeUtil.doubleArrayBaseOffset, 0, length << 3);
        return array;
    }

    public byte[] readBytes(int length) throws KryoException {
        byte[] bytes = new byte[length];
        readBytes(bytes, 0, (long) bytes.length);
        return bytes;
    }

    public final void readBytes(Object dstObj, long offset, long count) throws KryoException {
        if (dstObj.getClass().isArray()) {
            readBytes(dstObj, UnsafeUtil.byteArrayBaseOffset, offset, (int) count);
            return;
        }
        throw new KryoException("Only bulk reads of arrays is supported");
    }

    private final void readBytes(Object dstObj, long dstArrayTypeOffset, long offset, int count) throws KryoException {
        int copyCount = Math.min(this.limit - this.position, count);
        while (true) {
            UnsafeUtil.unsafe().copyMemory(null, this.bufaddress + ((long) this.position), dstObj, dstArrayTypeOffset + offset, (long) copyCount);
            this.position += copyCount;
            count -= copyCount;
            if (count != 0) {
                offset += (long) copyCount;
                copyCount = Math.min(count, this.capacity);
                require(copyCount);
            } else {
                return;
            }
        }
    }
}
