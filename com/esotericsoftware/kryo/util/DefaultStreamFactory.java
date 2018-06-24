package com.esotericsoftware.kryo.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.StreamFactory;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.InputStream;
import java.io.OutputStream;

public class DefaultStreamFactory implements StreamFactory {
    public Input getInput() {
        return new Input();
    }

    public Input getInput(int bufferSize) {
        return new Input(bufferSize);
    }

    public Input getInput(byte[] buffer) {
        return new Input(buffer);
    }

    public Input getInput(byte[] buffer, int offset, int count) {
        return new Input(buffer, offset, count);
    }

    public Input getInput(InputStream inputStream) {
        return new Input(inputStream);
    }

    public Input getInput(InputStream inputStream, int bufferSize) {
        return new Input(inputStream, bufferSize);
    }

    public Output getOutput() {
        return new Output();
    }

    public Output getOutput(int bufferSize) {
        return new Output(bufferSize);
    }

    public Output getOutput(int bufferSize, int maxBufferSize) {
        return new Output(bufferSize, maxBufferSize);
    }

    public Output getOutput(byte[] buffer) {
        return new Output(buffer);
    }

    public Output getOutput(byte[] buffer, int maxBufferSize) {
        return new Output(buffer, maxBufferSize);
    }

    public Output getOutput(OutputStream outputStream) {
        return new Output(outputStream);
    }

    public Output getOutput(OutputStream outputStream, int bufferSize) {
        return new Output(outputStream, bufferSize);
    }

    public void setKryo(Kryo kryo) {
    }
}
