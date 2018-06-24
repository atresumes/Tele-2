package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

public class BlowfishSerializer extends Serializer {
    private static SecretKeySpec keySpec;
    private final Serializer serializer;

    public BlowfishSerializer(Serializer serializer, byte[] key) {
        this.serializer = serializer;
        keySpec = new SecretKeySpec(key, "Blowfish");
    }

    public void write(Kryo kryo, Output output, Object object) {
        CipherOutputStream cipherStream = new CipherOutputStream(output, getCipher(1));
        Output cipherOutput = new Output(cipherStream, 256) {
            public void close() throws KryoException {
            }
        };
        this.serializer.write(kryo, cipherOutput, object);
        cipherOutput.flush();
        try {
            cipherStream.close();
        } catch (Throwable ex) {
            throw new KryoException(ex);
        }
    }

    public Object read(Kryo kryo, Input input, Class type) {
        return this.serializer.read(kryo, new Input(new CipherInputStream(input, getCipher(2)), 256), type);
    }

    public Object copy(Kryo kryo, Object original) {
        return this.serializer.copy(kryo, original);
    }

    private static Cipher getCipher(int mode) {
        try {
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(mode, keySpec);
            return cipher;
        } catch (Throwable ex) {
            throw new KryoException(ex);
        }
    }
}
