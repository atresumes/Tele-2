package com.esotericsoftware.kryo.io;

import com.esotericsoftware.kryo.Kryo;
import java.io.IOException;
import java.io.ObjectInput;

public class KryoObjectInput extends KryoDataInput implements ObjectInput {
    private final Kryo kryo;

    public KryoObjectInput(Kryo kryo, Input in) {
        super(in);
        this.kryo = kryo;
    }

    public Object readObject() throws ClassNotFoundException, IOException {
        return this.kryo.readClassAndObject(this.input);
    }

    public int read() throws IOException {
        return this.input.read();
    }

    public int read(byte[] b) throws IOException {
        return this.input.read(b);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return this.input.read(b, off, len);
    }

    public long skip(long n) throws IOException {
        return this.input.skip(n);
    }

    public int available() throws IOException {
        return 0;
    }

    public void close() throws IOException {
        this.input.close();
    }
}
