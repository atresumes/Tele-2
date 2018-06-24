package com.esotericsoftware.kryo.io;

import java.io.DataOutput;
import java.io.IOException;

public class KryoDataOutput implements DataOutput {
    protected Output output;

    public KryoDataOutput(Output output) {
        setOutput(output);
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public void write(int b) throws IOException {
        this.output.write(b);
    }

    public void write(byte[] b) throws IOException {
        this.output.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        this.output.write(b, off, len);
    }

    public void writeBoolean(boolean v) throws IOException {
        this.output.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        this.output.writeByte(v);
    }

    public void writeShort(int v) throws IOException {
        this.output.writeShort(v);
    }

    public void writeChar(int v) throws IOException {
        this.output.writeChar((char) v);
    }

    public void writeInt(int v) throws IOException {
        this.output.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        this.output.writeLong(v);
    }

    public void writeFloat(float v) throws IOException {
        this.output.writeFloat(v);
    }

    public void writeDouble(double v) throws IOException {
        this.output.writeDouble(v);
    }

    public void writeBytes(String s) throws IOException {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            this.output.write((byte) s.charAt(i));
        }
    }

    public void writeChars(String s) throws IOException {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            int v = s.charAt(i);
            this.output.write((v >>> 8) & 255);
            this.output.write((v >>> 0) & 255);
        }
    }

    public void writeUTF(String s) throws IOException {
        this.output.writeString(s);
    }
}
