package org.objenesis.instantiator.basic;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import org.objectweb.asm.Opcodes;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class ObjectInputStreamInstantiator<T> implements ObjectInstantiator<T> {
    private ObjectInputStream inputStream;

    private static class MockStream extends InputStream {
        private static byte[] HEADER;
        private static final int[] NEXT = new int[]{1, 2, 2};
        private static byte[] REPEATING_DATA;
        private final byte[] FIRST_DATA;
        private byte[][] buffers;
        private byte[] data = HEADER;
        private int pointer = 0;
        private int sequence = 0;

        static {
            initialize();
        }

        private static void initialize() {
            try {
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                DataOutputStream dout = new DataOutputStream(byteOut);
                dout.writeShort(-21267);
                dout.writeShort(5);
                HEADER = byteOut.toByteArray();
                byteOut = new ByteArrayOutputStream();
                dout = new DataOutputStream(byteOut);
                dout.writeByte(Opcodes.DREM);
                dout.writeByte(113);
                dout.writeInt(8257536);
                REPEATING_DATA = byteOut.toByteArray();
            } catch (IOException e) {
                throw new Error("IOException: " + e.getMessage());
            }
        }

        public MockStream(Class<?> clazz) {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(byteOut);
            try {
                dout.writeByte(Opcodes.DREM);
                dout.writeByte(114);
                dout.writeUTF(clazz.getName());
                dout.writeLong(ObjectStreamClass.lookup(clazz).getSerialVersionUID());
                dout.writeByte(2);
                dout.writeShort(0);
                dout.writeByte(Opcodes.ISHL);
                dout.writeByte(112);
                this.FIRST_DATA = byteOut.toByteArray();
                this.buffers = new byte[][]{HEADER, this.FIRST_DATA, REPEATING_DATA};
            } catch (IOException e) {
                throw new Error("IOException: " + e.getMessage());
            }
        }

        private void advanceBuffer() {
            this.pointer = 0;
            this.sequence = NEXT[this.sequence];
            this.data = this.buffers[this.sequence];
        }

        public int read() throws IOException {
            byte[] bArr = this.data;
            int i = this.pointer;
            this.pointer = i + 1;
            int result = bArr[i];
            if (this.pointer >= this.data.length) {
                advanceBuffer();
            }
            return result;
        }

        public int available() throws IOException {
            return Integer.MAX_VALUE;
        }

        public int read(byte[] b, int off, int len) throws IOException {
            int left = len;
            int remaining = this.data.length - this.pointer;
            while (remaining <= left) {
                System.arraycopy(this.data, this.pointer, b, off, remaining);
                off += remaining;
                left -= remaining;
                advanceBuffer();
                remaining = this.data.length - this.pointer;
            }
            if (left > 0) {
                System.arraycopy(this.data, this.pointer, b, off, left);
                this.pointer += left;
            }
            return len;
        }
    }

    public ObjectInputStreamInstantiator(Class<T> clazz) {
        if (Serializable.class.isAssignableFrom(clazz)) {
            try {
                this.inputStream = new ObjectInputStream(new MockStream(clazz));
                return;
            } catch (IOException e) {
                throw new Error("IOException: " + e.getMessage());
            }
        }
        throw new ObjenesisException(new NotSerializableException(clazz + " not serializable"));
    }

    public T newInstance() {
        try {
            return this.inputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new Error("ClassNotFoundException: " + e.getMessage());
        } catch (Throwable e2) {
            throw new ObjenesisException(e2);
        }
    }
}
