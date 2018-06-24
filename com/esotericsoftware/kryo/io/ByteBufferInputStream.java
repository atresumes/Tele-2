package com.esotericsoftware.kryo.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {
    private ByteBuffer byteBuffer;

    public ByteBufferInputStream(int bufferSize) {
        this(ByteBuffer.allocate(bufferSize));
        this.byteBuffer.flip();
    }

    public ByteBufferInputStream(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }

    public void setByteBuffer(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public int read() throws IOException {
        if (this.byteBuffer.hasRemaining()) {
            return this.byteBuffer.get() & 255;
        }
        return -1;
    }

    public int read(byte[] bytes, int offset, int length) throws IOException {
        if (length == 0) {
            return 0;
        }
        int count = Math.min(this.byteBuffer.remaining(), length);
        if (count == 0) {
            return -1;
        }
        this.byteBuffer.get(bytes, offset, count);
        return count;
    }

    public int available() throws IOException {
        return this.byteBuffer.remaining();
    }
}
