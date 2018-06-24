package com.esotericsoftware.kryo.io;

import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.io.OutputStream;

public class OutputChunked extends Output {
    public OutputChunked() {
        super(2048);
    }

    public OutputChunked(int bufferSize) {
        super(bufferSize);
    }

    public OutputChunked(OutputStream outputStream) {
        super(outputStream, 2048);
    }

    public OutputChunked(OutputStream outputStream, int bufferSize) {
        super(outputStream, bufferSize);
    }

    public void flush() throws KryoException {
        if (position() > 0) {
            try {
                writeChunkSize();
                super.flush();
            } catch (Throwable ex) {
                throw new KryoException(ex);
            }
        }
        super.flush();
    }

    private void writeChunkSize() throws IOException {
        int size = position();
        if (Log.TRACE) {
            Log.trace("kryo", "Write chunk: " + size);
        }
        OutputStream outputStream = getOutputStream();
        if ((size & -128) == 0) {
            outputStream.write(size);
            return;
        }
        outputStream.write((size & 127) | 128);
        size >>>= 7;
        if ((size & -128) == 0) {
            outputStream.write(size);
            return;
        }
        outputStream.write((size & 127) | 128);
        size >>>= 7;
        if ((size & -128) == 0) {
            outputStream.write(size);
            return;
        }
        outputStream.write((size & 127) | 128);
        size >>>= 7;
        if ((size & -128) == 0) {
            outputStream.write(size);
            return;
        }
        outputStream.write((size & 127) | 128);
        outputStream.write(size >>> 7);
    }

    public void endChunks() {
        flush();
        if (Log.TRACE) {
            Log.trace("kryo", "End chunks.");
        }
        try {
            getOutputStream().write(0);
        } catch (Throwable ex) {
            throw new KryoException(ex);
        }
    }
}
