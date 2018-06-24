package com.esotericsoftware.kryo.io;

import com.esotericsoftware.kryo.KryoException;
import java.io.InputStream;

public final class FastInput extends Input {
    public FastInput(int bufferSize) {
        super(bufferSize);
    }

    public FastInput(byte[] buffer) {
        super(buffer);
    }

    public FastInput(byte[] buffer, int offset, int count) {
        super(buffer, offset, count);
    }

    public FastInput(InputStream outputStream) {
        super(outputStream);
    }

    public FastInput(InputStream outputStream, int bufferSize) {
        super(outputStream, bufferSize);
    }

    public int readInt(boolean optimizePositive) throws KryoException {
        return readInt();
    }

    public long readLong(boolean optimizePositive) throws KryoException {
        return readLong();
    }
}
