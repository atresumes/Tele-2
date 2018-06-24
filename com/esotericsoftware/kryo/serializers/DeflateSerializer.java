package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.InputChunked;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.OutputChunked;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class DeflateSerializer extends Serializer {
    private int compressionLevel = 4;
    private boolean noHeaders = true;
    private final Serializer serializer;

    public DeflateSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    public void write(Kryo kryo, Output output, Object object) {
        OutputChunked outputChunked = new OutputChunked(output, 256);
        Deflater deflater = new Deflater(this.compressionLevel, this.noHeaders);
        try {
            OutputStream deflaterStream = new DeflaterOutputStream(outputChunked, deflater);
            Output deflaterOutput = new Output(deflaterStream, 256);
            this.serializer.write(kryo, deflaterOutput, object);
            deflaterOutput.flush();
            deflaterStream.finish();
            deflater.end();
            outputChunked.endChunks();
        } catch (Throwable ex) {
            throw new KryoException(ex);
        } catch (Throwable th) {
            deflater.end();
        }
    }

    public Object read(Kryo kryo, Input input, Class type) {
        Inflater inflater = new Inflater(this.noHeaders);
        try {
            Object read = this.serializer.read(kryo, new Input(new InflaterInputStream(new InputChunked(input, 256), inflater), 256), type);
            return read;
        } finally {
            inflater.end();
        }
    }

    public void setNoHeaders(boolean noHeaders) {
        this.noHeaders = noHeaders;
    }

    public void setCompressionLevel(int compressionLevel) {
        this.compressionLevel = compressionLevel;
    }

    public Object copy(Kryo kryo, Object original) {
        return this.serializer.copy(kryo, original);
    }
}
