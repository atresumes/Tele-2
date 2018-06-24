package com.esotericsoftware.kryo.io;

import com.esotericsoftware.kryo.KryoException;
import java.io.OutputStream;

public final class FastOutput extends Output {
    public FastOutput(int bufferSize) {
        this(bufferSize, bufferSize);
    }

    public FastOutput(int bufferSize, int maxBufferSize) {
        super(bufferSize, maxBufferSize);
    }

    public FastOutput(byte[] buffer) {
        this(buffer, buffer.length);
    }

    public FastOutput(byte[] buffer, int maxBufferSize) {
        super(buffer, maxBufferSize);
    }

    public FastOutput(OutputStream outputStream) {
        super(outputStream);
    }

    public FastOutput(OutputStream outputStream, int bufferSize) {
        super(outputStream, bufferSize);
    }

    public int writeInt(int value, boolean optimizePositive) throws KryoException {
        writeInt(value);
        return 4;
    }

    public int writeLong(long value, boolean optimizePositive) throws KryoException {
        writeLong(value);
        return 8;
    }
}
