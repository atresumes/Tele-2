package com.esotericsoftware.kryo.io;

import com.esotericsoftware.kryo.KryoException;
import java.io.IOException;
import java.io.OutputStream;
import org.objectweb.asm.Opcodes;

public class Output extends OutputStream {
    protected byte[] buffer;
    protected int capacity;
    protected int maxCapacity;
    protected OutputStream outputStream;
    protected int position;
    protected long total;

    public Output(int bufferSize) {
        this(bufferSize, bufferSize);
    }

    public Output(int bufferSize, int maxBufferSize) {
        if (bufferSize > maxBufferSize && maxBufferSize != -1) {
            throw new IllegalArgumentException("bufferSize: " + bufferSize + " cannot be greater than maxBufferSize: " + maxBufferSize);
        } else if (maxBufferSize < -1) {
            throw new IllegalArgumentException("maxBufferSize cannot be < -1: " + maxBufferSize);
        } else {
            this.capacity = bufferSize;
            if (maxBufferSize == -1) {
                maxBufferSize = Integer.MAX_VALUE;
            }
            this.maxCapacity = maxBufferSize;
            this.buffer = new byte[bufferSize];
        }
    }

    public Output(byte[] buffer) {
        this(buffer, buffer.length);
    }

    public Output(byte[] buffer, int maxBufferSize) {
        if (buffer == null) {
            throw new IllegalArgumentException("buffer cannot be null.");
        }
        setBuffer(buffer, maxBufferSize);
    }

    public Output(OutputStream outputStream) {
        this(4096, 4096);
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream cannot be null.");
        }
        this.outputStream = outputStream;
    }

    public Output(OutputStream outputStream, int bufferSize) {
        this(bufferSize, bufferSize);
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream cannot be null.");
        }
        this.outputStream = outputStream;
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.position = 0;
        this.total = 0;
    }

    public void setBuffer(byte[] buffer) {
        setBuffer(buffer, buffer.length);
    }

    public void setBuffer(byte[] buffer, int maxBufferSize) {
        if (buffer == null) {
            throw new IllegalArgumentException("buffer cannot be null.");
        } else if (buffer.length > maxBufferSize && maxBufferSize != -1) {
            throw new IllegalArgumentException("buffer has length: " + buffer.length + " cannot be greater than maxBufferSize: " + maxBufferSize);
        } else if (maxBufferSize < -1) {
            throw new IllegalArgumentException("maxBufferSize cannot be < -1: " + maxBufferSize);
        } else {
            this.buffer = buffer;
            if (maxBufferSize == -1) {
                maxBufferSize = Integer.MAX_VALUE;
            }
            this.maxCapacity = maxBufferSize;
            this.capacity = buffer.length;
            this.position = 0;
            this.total = 0;
            this.outputStream = null;
        }
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public byte[] toBytes() {
        byte[] newBuffer = new byte[this.position];
        System.arraycopy(this.buffer, 0, newBuffer, 0, this.position);
        return newBuffer;
    }

    public int position() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long total() {
        return this.total + ((long) this.position);
    }

    public void clear() {
        this.position = 0;
        this.total = 0;
    }

    protected boolean require(int required) throws KryoException {
        if (this.capacity - this.position >= required) {
            return false;
        }
        if (required > this.maxCapacity) {
            throw new KryoException("Buffer overflow. Max capacity: " + this.maxCapacity + ", required: " + required);
        }
        flush();
        while (this.capacity - this.position < required) {
            if (this.capacity == this.maxCapacity) {
                throw new KryoException("Buffer overflow. Available: " + (this.capacity - this.position) + ", required: " + required);
            }
            if (this.capacity == 0) {
                this.capacity = 1;
            }
            this.capacity = Math.min(this.capacity * 2, this.maxCapacity);
            if (this.capacity < 0) {
                this.capacity = this.maxCapacity;
            }
            byte[] newBuffer = new byte[this.capacity];
            System.arraycopy(this.buffer, 0, newBuffer, 0, this.position);
            this.buffer = newBuffer;
        }
        return true;
    }

    public void flush() throws KryoException {
        if (this.outputStream != null) {
            try {
                this.outputStream.write(this.buffer, 0, this.position);
                this.outputStream.flush();
                this.total += (long) this.position;
                this.position = 0;
            } catch (Throwable ex) {
                throw new KryoException(ex);
            }
        }
    }

    public void close() throws KryoException {
        flush();
        if (this.outputStream != null) {
            try {
                this.outputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public void write(int value) throws KryoException {
        if (this.position == this.capacity) {
            require(1);
        }
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) value;
    }

    public void write(byte[] bytes) throws KryoException {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        }
        writeBytes(bytes, 0, bytes.length);
    }

    public void write(byte[] bytes, int offset, int length) throws KryoException {
        writeBytes(bytes, offset, length);
    }

    public void writeByte(byte value) throws KryoException {
        if (this.position == this.capacity) {
            require(1);
        }
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        bArr[i] = value;
    }

    public void writeByte(int value) throws KryoException {
        if (this.position == this.capacity) {
            require(1);
        }
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) value;
    }

    public void writeBytes(byte[] bytes) throws KryoException {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        }
        writeBytes(bytes, 0, bytes.length);
    }

    public void writeBytes(byte[] bytes, int offset, int count) throws KryoException {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        }
        int copyCount = Math.min(this.capacity - this.position, count);
        while (true) {
            System.arraycopy(bytes, offset, this.buffer, this.position, copyCount);
            this.position += copyCount;
            count -= copyCount;
            if (count != 0) {
                offset += copyCount;
                copyCount = Math.min(this.capacity, count);
                require(copyCount);
            } else {
                return;
            }
        }
    }

    public void writeInt(int value) throws KryoException {
        require(4);
        byte[] buffer = this.buffer;
        int i = this.position;
        this.position = i + 1;
        buffer[i] = (byte) (value >> 24);
        i = this.position;
        this.position = i + 1;
        buffer[i] = (byte) (value >> 16);
        i = this.position;
        this.position = i + 1;
        buffer[i] = (byte) (value >> 8);
        i = this.position;
        this.position = i + 1;
        buffer[i] = (byte) value;
    }

    public int writeInt(int value, boolean optimizePositive) throws KryoException {
        return writeVarInt(value, optimizePositive);
    }

    public int writeVarInt(int value, boolean optimizePositive) throws KryoException {
        if (!optimizePositive) {
            value = (value << 1) ^ (value >> 31);
        }
        int i;
        if ((value >>> 7) == 0) {
            require(1);
            byte[] bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) value;
            return 1;
        } else if ((value >>> 14) == 0) {
            require(2);
            r0 = this.buffer;
            i = this.position;
            this.position = i + 1;
            r0[i] = (byte) ((value & 127) | 128);
            r0 = this.buffer;
            i = this.position;
            this.position = i + 1;
            r0[i] = (byte) (value >>> 7);
            return 2;
        } else if ((value >>> 21) == 0) {
            require(3);
            r0 = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            r0[r1] = (byte) ((value & 127) | 128);
            r0 = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            r0[r1] = (byte) ((value >>> 7) | 128);
            r0 = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            r0[r1] = (byte) (value >>> 14);
            return 3;
        } else if ((value >>> 28) == 0) {
            require(4);
            r0 = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            r0[r1] = (byte) ((value & 127) | 128);
            r0 = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            r0[r1] = (byte) ((value >>> 7) | 128);
            r0 = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            r0[r1] = (byte) ((value >>> 14) | 128);
            r0 = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            r0[r1] = (byte) (value >>> 21);
            return 4;
        } else {
            require(5);
            r0 = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            r0[r1] = (byte) ((value & 127) | 128);
            r0 = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            r0[r1] = (byte) ((value >>> 7) | 128);
            r0 = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            r0[r1] = (byte) ((value >>> 14) | 128);
            r0 = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            r0[r1] = (byte) ((value >>> 21) | 128);
            r0 = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            r0[r1] = (byte) (value >>> 28);
            return 5;
        }
    }

    public void writeString(String value) throws KryoException {
        if (value == null) {
            writeByte(128);
            return;
        }
        int charCount = value.length();
        if (charCount == 0) {
            writeByte((int) Opcodes.LOR);
            return;
        }
        boolean ascii = false;
        if (charCount > 1 && charCount < 64) {
            ascii = true;
            for (int i = 0; i < charCount; i++) {
                if (value.charAt(i) > 127) {
                    ascii = false;
                    break;
                }
            }
        }
        if (ascii) {
            if (this.capacity - this.position < charCount) {
                writeAscii_slow(value, charCount);
            } else {
                value.getBytes(0, charCount, this.buffer, this.position);
                this.position += charCount;
            }
            byte[] bArr = this.buffer;
            int i2 = this.position - 1;
            bArr[i2] = (byte) (bArr[i2] | 128);
            return;
        }
        writeUtf8Length(charCount + 1);
        int charIndex = 0;
        if (this.capacity - this.position >= charCount) {
            byte[] buffer = this.buffer;
            int position = this.position;
            while (charIndex < charCount) {
                int c = value.charAt(charIndex);
                if (c > 127) {
                    break;
                }
                int position2 = position + 1;
                buffer[position] = (byte) c;
                charIndex++;
                position = position2;
            }
            this.position = position;
        }
        if (charIndex < charCount) {
            writeString_slow(value, charCount, charIndex);
        }
    }

    public void writeString(CharSequence value) throws KryoException {
        if (value == null) {
            writeByte(128);
            return;
        }
        int charCount = value.length();
        if (charCount == 0) {
            writeByte((int) Opcodes.LOR);
            return;
        }
        writeUtf8Length(charCount + 1);
        int charIndex = 0;
        if (this.capacity - this.position >= charCount) {
            byte[] buffer = this.buffer;
            int position = this.position;
            while (charIndex < charCount) {
                int c = value.charAt(charIndex);
                if (c > 127) {
                    break;
                }
                int position2 = position + 1;
                buffer[position] = (byte) c;
                charIndex++;
                position = position2;
            }
            this.position = position;
        }
        if (charIndex < charCount) {
            writeString_slow(value, charCount, charIndex);
        }
    }

    public void writeAscii(String value) throws KryoException {
        if (value == null) {
            writeByte(128);
            return;
        }
        int charCount = value.length();
        switch (charCount) {
            case 0:
                writeByte((int) Opcodes.LOR);
                return;
            case 1:
                writeByte(130);
                writeByte(value.charAt(0));
                return;
            default:
                if (this.capacity - this.position < charCount) {
                    writeAscii_slow(value, charCount);
                } else {
                    value.getBytes(0, charCount, this.buffer, this.position);
                    this.position += charCount;
                }
                byte[] bArr = this.buffer;
                int i = this.position - 1;
                bArr[i] = (byte) (bArr[i] | 128);
                return;
        }
    }

    private void writeUtf8Length(int value) {
        if ((value >>> 6) == 0) {
            require(1);
            byte[] bArr = this.buffer;
            int i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) (value | 128);
        } else if ((value >>> 13) == 0) {
            require(2);
            buffer = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            buffer[r1] = (byte) ((value | 64) | 128);
            r1 = this.position;
            this.position = r1 + 1;
            buffer[r1] = (byte) (value >>> 6);
        } else if ((value >>> 20) == 0) {
            require(3);
            buffer = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            buffer[r1] = (byte) ((value | 64) | 128);
            r1 = this.position;
            this.position = r1 + 1;
            buffer[r1] = (byte) ((value >>> 6) | 128);
            r1 = this.position;
            this.position = r1 + 1;
            buffer[r1] = (byte) (value >>> 13);
        } else if ((value >>> 27) == 0) {
            require(4);
            buffer = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            buffer[r1] = (byte) ((value | 64) | 128);
            r1 = this.position;
            this.position = r1 + 1;
            buffer[r1] = (byte) ((value >>> 6) | 128);
            r1 = this.position;
            this.position = r1 + 1;
            buffer[r1] = (byte) ((value >>> 13) | 128);
            r1 = this.position;
            this.position = r1 + 1;
            buffer[r1] = (byte) (value >>> 20);
        } else {
            require(5);
            buffer = this.buffer;
            r1 = this.position;
            this.position = r1 + 1;
            buffer[r1] = (byte) ((value | 64) | 128);
            r1 = this.position;
            this.position = r1 + 1;
            buffer[r1] = (byte) ((value >>> 6) | 128);
            r1 = this.position;
            this.position = r1 + 1;
            buffer[r1] = (byte) ((value >>> 13) | 128);
            r1 = this.position;
            this.position = r1 + 1;
            buffer[r1] = (byte) ((value >>> 20) | 128);
            r1 = this.position;
            this.position = r1 + 1;
            buffer[r1] = (byte) (value >>> 27);
        }
    }

    private void writeString_slow(CharSequence value, int charCount, int charIndex) {
        while (charIndex < charCount) {
            if (this.position == this.capacity) {
                require(Math.min(this.capacity, charCount - charIndex));
            }
            int c = value.charAt(charIndex);
            byte[] bArr;
            int i;
            if (c <= 127) {
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) c;
            } else if (c > 2047) {
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) (((c >> 12) & 15) | 224);
                require(2);
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) (((c >> 6) & 63) | 128);
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) ((c & 63) | 128);
            } else {
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) (((c >> 6) & 31) | Opcodes.CHECKCAST);
                require(1);
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) ((c & 63) | 128);
            }
            charIndex++;
        }
    }

    private void writeAscii_slow(String value, int charCount) throws KryoException {
        byte[] buffer = this.buffer;
        int charIndex = 0;
        int charsToWrite = Math.min(charCount, this.capacity - this.position);
        while (charIndex < charCount) {
            value.getBytes(charIndex, charIndex + charsToWrite, buffer, this.position);
            charIndex += charsToWrite;
            this.position += charsToWrite;
            charsToWrite = Math.min(charCount - charIndex, this.capacity);
            if (require(charsToWrite)) {
                buffer = this.buffer;
            }
        }
    }

    public void writeFloat(float value) throws KryoException {
        writeInt(Float.floatToIntBits(value));
    }

    public int writeFloat(float value, float precision, boolean optimizePositive) throws KryoException {
        return writeInt((int) (value * precision), optimizePositive);
    }

    public void writeShort(int value) throws KryoException {
        require(2);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) (value >>> 8);
        bArr = this.buffer;
        i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) value;
    }

    public void writeLong(long value) throws KryoException {
        require(8);
        byte[] buffer = this.buffer;
        int i = this.position;
        this.position = i + 1;
        buffer[i] = (byte) ((int) (value >>> 56));
        i = this.position;
        this.position = i + 1;
        buffer[i] = (byte) ((int) (value >>> 48));
        i = this.position;
        this.position = i + 1;
        buffer[i] = (byte) ((int) (value >>> 40));
        i = this.position;
        this.position = i + 1;
        buffer[i] = (byte) ((int) (value >>> 32));
        i = this.position;
        this.position = i + 1;
        buffer[i] = (byte) ((int) (value >>> 24));
        i = this.position;
        this.position = i + 1;
        buffer[i] = (byte) ((int) (value >>> 16));
        i = this.position;
        this.position = i + 1;
        buffer[i] = (byte) ((int) (value >>> 8));
        i = this.position;
        this.position = i + 1;
        buffer[i] = (byte) ((int) value);
    }

    public int writeLong(long value, boolean optimizePositive) throws KryoException {
        return writeVarLong(value, optimizePositive);
    }

    public int writeVarLong(long value, boolean optimizePositive) throws KryoException {
        if (!optimizePositive) {
            value = (value << 1) ^ (value >> 63);
        }
        byte[] bArr;
        int i;
        if ((value >>> 7) == 0) {
            require(1);
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) value);
            return 1;
        } else if ((value >>> 14) == 0) {
            require(2);
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((127 & value) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) (value >>> 7));
            return 2;
        } else if ((value >>> 21) == 0) {
            require(3);
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((127 & value) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 7) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) (value >>> 14));
            return 3;
        } else if ((value >>> 28) == 0) {
            require(4);
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((127 & value) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 7) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 14) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) (value >>> 21));
            return 4;
        } else if ((value >>> 35) == 0) {
            require(5);
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((127 & value) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 7) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 14) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 21) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) (value >>> 28));
            return 5;
        } else if ((value >>> 42) == 0) {
            require(6);
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((127 & value) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 7) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 14) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 21) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 28) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) (value >>> 35));
            return 6;
        } else if ((value >>> 49) == 0) {
            require(7);
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((127 & value) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 7) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 14) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 21) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 28) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 35) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) (value >>> 42));
            return 7;
        } else if ((value >>> 56) == 0) {
            require(8);
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((127 & value) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 7) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 14) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 21) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 28) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 35) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 42) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) (value >>> 49));
            return 8;
        } else {
            require(9);
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((127 & value) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 7) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 14) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 21) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 28) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 35) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 42) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) ((value >>> 49) | 128));
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            bArr[i] = (byte) ((int) (value >>> 56));
            return 9;
        }
    }

    public void writeBoolean(boolean value) throws KryoException {
        int i = 1;
        if (this.position == this.capacity) {
            require(1);
        }
        byte[] bArr = this.buffer;
        int i2 = this.position;
        this.position = i2 + 1;
        if (!value) {
            i = 0;
        }
        bArr[i2] = (byte) i;
    }

    public void writeChar(char value) throws KryoException {
        require(2);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) (value >>> 8);
        bArr = this.buffer;
        i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) value;
    }

    public void writeDouble(double value) throws KryoException {
        writeLong(Double.doubleToLongBits(value));
    }

    public int writeDouble(double value, double precision, boolean optimizePositive) throws KryoException {
        return writeLong((long) (value * precision), optimizePositive);
    }

    public static int intLength(int value, boolean optimizePositive) {
        if (!optimizePositive) {
            value = (value << 1) ^ (value >> 31);
        }
        if ((value >>> 7) == 0) {
            return 1;
        }
        if ((value >>> 14) == 0) {
            return 2;
        }
        if ((value >>> 21) == 0) {
            return 3;
        }
        if ((value >>> 28) == 0) {
            return 4;
        }
        return 5;
    }

    public static int longLength(long value, boolean optimizePositive) {
        if (!optimizePositive) {
            value = (value << 1) ^ (value >> 63);
        }
        if ((value >>> 7) == 0) {
            return 1;
        }
        if ((value >>> 14) == 0) {
            return 2;
        }
        if ((value >>> 21) == 0) {
            return 3;
        }
        if ((value >>> 28) == 0) {
            return 4;
        }
        if ((value >>> 35) == 0) {
            return 5;
        }
        if ((value >>> 42) == 0) {
            return 6;
        }
        if ((value >>> 49) == 0) {
            return 7;
        }
        if ((value >>> 56) == 0) {
            return 8;
        }
        return 9;
    }

    public void writeInts(int[] object, boolean optimizePositive) throws KryoException {
        for (int writeInt : object) {
            writeInt(writeInt, optimizePositive);
        }
    }

    public void writeLongs(long[] object, boolean optimizePositive) throws KryoException {
        for (long writeLong : object) {
            writeLong(writeLong, optimizePositive);
        }
    }

    public void writeInts(int[] object) throws KryoException {
        for (int writeInt : object) {
            writeInt(writeInt);
        }
    }

    public void writeLongs(long[] object) throws KryoException {
        for (long writeLong : object) {
            writeLong(writeLong);
        }
    }

    public void writeFloats(float[] object) throws KryoException {
        for (float writeFloat : object) {
            writeFloat(writeFloat);
        }
    }

    public void writeShorts(short[] object) throws KryoException {
        for (short writeShort : object) {
            writeShort(writeShort);
        }
    }

    public void writeChars(char[] object) throws KryoException {
        for (char writeChar : object) {
            writeChar(writeChar);
        }
    }

    public void writeDoubles(double[] object) throws KryoException {
        for (double writeDouble : object) {
            writeDouble(writeDouble);
        }
    }
}
