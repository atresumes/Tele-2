package com.esotericsoftware.kryo.io;

import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.minlog.Log;
import java.io.InputStream;

public class InputChunked extends Input {
    private int chunkSize = -1;

    public InputChunked() {
        super(2048);
    }

    public InputChunked(int bufferSize) {
        super(bufferSize);
    }

    public InputChunked(InputStream inputStream) {
        super(inputStream, 2048);
    }

    public InputChunked(InputStream inputStream, int bufferSize) {
        super(inputStream, bufferSize);
    }

    public void setInputStream(InputStream inputStream) {
        super.setInputStream(inputStream);
        this.chunkSize = -1;
    }

    public void setBuffer(byte[] bytes, int offset, int count) {
        super.setBuffer(bytes, offset, count);
        this.chunkSize = -1;
    }

    public void rewind() {
        super.rewind();
        this.chunkSize = -1;
    }

    protected int fill(byte[] buffer, int offset, int count) throws KryoException {
        int actual = -1;
        if (this.chunkSize == -1) {
            readChunkSize();
        } else if (this.chunkSize == 0) {
            return actual;
        }
        actual = super.fill(buffer, offset, Math.min(this.chunkSize, count));
        this.chunkSize -= actual;
        if (this.chunkSize == 0) {
            readChunkSize();
        }
        return actual;
    }

    private void readChunkSize() {
        try {
            InputStream inputStream = getInputStream();
            int result = 0;
            for (int offset = 0; offset < 32; offset += 7) {
                int b = inputStream.read();
                if (b == -1) {
                    throw new KryoException("Buffer underflow.");
                }
                result |= (b & 127) << offset;
                if ((b & 128) == 0) {
                    this.chunkSize = result;
                    if (Log.TRACE) {
                        Log.trace("kryo", "Read chunk: " + this.chunkSize);
                        return;
                    }
                    return;
                }
            }
            throw new KryoException("Malformed integer.");
        } catch (Throwable ex) {
            throw new KryoException(ex);
        }
    }

    public void nextChunks() {
        if (this.chunkSize == -1) {
            readChunkSize();
        }
        while (this.chunkSize > 0) {
            skip(this.chunkSize);
        }
        this.chunkSize = -1;
        if (Log.TRACE) {
            Log.trace("kryo", "Next chunks.");
        }
    }
}
