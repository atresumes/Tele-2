package com.esotericsoftware.kryo.io;

import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.util.UnsafeUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.objectweb.asm.Opcodes;

public class ByteBufferOutput extends Output {
    protected static final ByteOrder nativeOrder = ByteOrder.nativeOrder();
    ByteOrder byteOrder;
    protected ByteBuffer niobuffer;
    protected boolean varIntsEnabled;

    public ByteBufferOutput() {
        this.varIntsEnabled = true;
        this.byteOrder = ByteOrder.BIG_ENDIAN;
    }

    public ByteBufferOutput(int bufferSize) {
        this(bufferSize, bufferSize);
    }

    public ByteBufferOutput(int bufferSize, int maxBufferSize) {
        this.varIntsEnabled = true;
        this.byteOrder = ByteOrder.BIG_ENDIAN;
        if (maxBufferSize < -1) {
            throw new IllegalArgumentException("maxBufferSize cannot be < -1: " + maxBufferSize);
        }
        this.capacity = bufferSize;
        if (maxBufferSize == -1) {
            maxBufferSize = Integer.MAX_VALUE;
        }
        this.maxCapacity = maxBufferSize;
        this.niobuffer = ByteBuffer.allocateDirect(bufferSize);
        this.niobuffer.order(this.byteOrder);
    }

    public ByteBufferOutput(OutputStream outputStream) {
        this(4096, 4096);
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream cannot be null.");
        }
        this.outputStream = outputStream;
    }

    public ByteBufferOutput(OutputStream outputStream, int bufferSize) {
        this(bufferSize, bufferSize);
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream cannot be null.");
        }
        this.outputStream = outputStream;
    }

    public ByteBufferOutput(ByteBuffer buffer) {
        this.varIntsEnabled = true;
        this.byteOrder = ByteOrder.BIG_ENDIAN;
        setBuffer(buffer);
    }

    public ByteBufferOutput(ByteBuffer buffer, int maxBufferSize) {
        this.varIntsEnabled = true;
        this.byteOrder = ByteOrder.BIG_ENDIAN;
        setBuffer(buffer, maxBufferSize);
    }

    public ByteBufferOutput(long address, int maxBufferSize) {
        this.varIntsEnabled = true;
        this.byteOrder = ByteOrder.BIG_ENDIAN;
        this.niobuffer = UnsafeUtil.getDirectBufferAt(address, maxBufferSize);
        setBuffer(this.niobuffer, maxBufferSize);
    }

    public void release() {
        clear();
        UnsafeUtil.releaseBuffer(this.niobuffer);
        this.niobuffer = null;
    }

    public ByteOrder order() {
        return this.byteOrder;
    }

    public void order(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
        this.niobuffer.order(byteOrder);
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.position = 0;
        this.total = 0;
    }

    public void setBuffer(ByteBuffer buffer) {
        setBuffer(buffer, buffer.capacity());
    }

    public void setBuffer(ByteBuffer buffer, int maxBufferSize) {
        if (buffer == null) {
            throw new IllegalArgumentException("buffer cannot be null.");
        } else if (maxBufferSize < -1) {
            throw new IllegalArgumentException("maxBufferSize cannot be < -1: " + maxBufferSize);
        } else {
            this.niobuffer = buffer;
            if (maxBufferSize == -1) {
                maxBufferSize = Integer.MAX_VALUE;
            }
            this.maxCapacity = maxBufferSize;
            this.byteOrder = buffer.order();
            this.capacity = buffer.capacity();
            this.position = buffer.position();
            this.total = 0;
            this.outputStream = null;
        }
    }

    public ByteBuffer getByteBuffer() {
        this.niobuffer.position(this.position);
        return this.niobuffer;
    }

    public byte[] toBytes() {
        byte[] newBuffer = new byte[this.position];
        this.niobuffer.position(0);
        this.niobuffer.get(newBuffer, 0, this.position);
        return newBuffer;
    }

    public void setPosition(int position) {
        this.position = position;
        this.niobuffer.position(position);
    }

    public void clear() {
        this.niobuffer.clear();
        this.position = 0;
        this.total = 0;
    }

    protected boolean require(int required) throws KryoException {
        if (this.capacity - this.position >= required) {
            return false;
        }
        if (required > this.maxCapacity) {
            this.niobuffer.order(this.byteOrder);
            throw new KryoException("Buffer overflow. Max capacity: " + this.maxCapacity + ", required: " + required);
        }
        flush();
        while (this.capacity - this.position < required) {
            if (this.capacity == this.maxCapacity) {
                this.niobuffer.order(this.byteOrder);
                throw new KryoException("Buffer overflow. Available: " + (this.capacity - this.position) + ", required: " + required);
            }
            ByteBuffer newBuffer;
            if (this.capacity == 0) {
                this.capacity = 1;
            }
            this.capacity = Math.min(this.capacity * 2, this.maxCapacity);
            if (this.capacity < 0) {
                this.capacity = this.maxCapacity;
            }
            if (this.niobuffer == null || this.niobuffer.isDirect()) {
                newBuffer = ByteBuffer.allocateDirect(this.capacity);
            } else {
                newBuffer = ByteBuffer.allocate(this.capacity);
            }
            this.niobuffer.position(0);
            this.niobuffer.limit(this.position);
            newBuffer.put(this.niobuffer);
            newBuffer.order(this.niobuffer.order());
            setBuffer(newBuffer, this.maxCapacity);
        }
        return true;
    }

    public void flush() throws KryoException {
        if (this.outputStream != null) {
            try {
                byte[] tmp = new byte[this.position];
                this.niobuffer.position(0);
                this.niobuffer.get(tmp);
                this.niobuffer.position(0);
                this.outputStream.write(tmp, 0, this.position);
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
        this.niobuffer.put((byte) value);
        this.position++;
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
        this.niobuffer.put(value);
        this.position++;
    }

    public void writeByte(int value) throws KryoException {
        if (this.position == this.capacity) {
            require(1);
        }
        this.niobuffer.put((byte) value);
        this.position++;
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
            this.niobuffer.put(bytes, offset, copyCount);
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
        this.niobuffer.putInt(value);
        this.position += 4;
    }

    public int writeInt(int value, boolean optimizePositive) throws KryoException {
        if (this.varIntsEnabled) {
            return writeVarInt(value, optimizePositive);
        }
        writeInt(value);
        return 4;
    }

    public int writeVarInt(int val, boolean optimizePositive) throws KryoException {
        this.niobuffer.position(this.position);
        int value = val;
        if (!optimizePositive) {
            value = (value << 1) ^ (value >> 31);
        }
        int varInt = value & 127;
        value >>>= 7;
        if (value == 0) {
            writeByte(varInt);
            return 1;
        }
        varInt = (varInt | 128) | ((value & 127) << 8);
        value >>>= 7;
        if (value == 0) {
            this.niobuffer.order(ByteOrder.LITTLE_ENDIAN);
            writeInt(varInt);
            this.niobuffer.order(this.byteOrder);
            this.position -= 2;
            this.niobuffer.position(this.position);
            return 2;
        }
        varInt = (varInt | 32768) | ((value & 127) << 16);
        value >>>= 7;
        if (value == 0) {
            this.niobuffer.order(ByteOrder.LITTLE_ENDIAN);
            writeInt(varInt);
            this.niobuffer.order(this.byteOrder);
            this.position--;
            this.niobuffer.position(this.position);
            return 3;
        }
        varInt = (varInt | 8388608) | ((value & 127) << 24);
        value >>>= 7;
        if (value == 0) {
            this.niobuffer.order(ByteOrder.LITTLE_ENDIAN);
            writeInt(varInt);
            this.niobuffer.order(this.byteOrder);
            this.position += 0;
            return 4;
        }
        long varLong = (((long) (varInt | Integer.MIN_VALUE)) & 4294967295L) | (((long) value) << 32);
        this.niobuffer.order(ByteOrder.LITTLE_ENDIAN);
        writeLong(varLong);
        this.niobuffer.order(this.byteOrder);
        this.position -= 3;
        this.niobuffer.position(this.position);
        return 5;
    }

    public void writeString(String value) throws KryoException {
        this.niobuffer.position(this.position);
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
                byte[] tmp = value.getBytes();
                this.niobuffer.put(tmp, 0, tmp.length);
                this.position += charCount;
            }
            this.niobuffer.put(this.position - 1, (byte) (this.niobuffer.get(this.position - 1) | 128));
            return;
        }
        writeUtf8Length(charCount + 1);
        int charIndex = 0;
        if (this.capacity - this.position >= charCount) {
            int position = this.position;
            while (charIndex < charCount) {
                int c = value.charAt(charIndex);
                if (c > 127) {
                    break;
                }
                int position2 = position + 1;
                this.niobuffer.put(position, (byte) c);
                charIndex++;
                position = position2;
            }
            this.position = position;
            this.niobuffer.position(position);
        }
        if (charIndex < charCount) {
            writeString_slow(value, charCount, charIndex);
        }
        this.niobuffer.position(this.position);
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
            int position = this.position;
            while (charIndex < charCount) {
                int c = value.charAt(charIndex);
                if (c > 127) {
                    break;
                }
                int position2 = position + 1;
                this.niobuffer.put(position, (byte) c);
                charIndex++;
                position = position2;
            }
            this.position = position;
            this.niobuffer.position(position);
        }
        if (charIndex < charCount) {
            writeString_slow(value, charCount, charIndex);
        }
        this.niobuffer.position(this.position);
    }

    public void writeAscii(String value) throws KryoException {
        if (value == null) {
            writeByte(128);
            return;
        }
        int charCount = value.length();
        if (charCount == 0) {
            writeByte((int) Opcodes.LOR);
            return;
        }
        if (this.capacity - this.position < charCount) {
            writeAscii_slow(value, charCount);
        } else {
            byte[] tmp = value.getBytes();
            this.niobuffer.put(tmp, 0, tmp.length);
            this.position += charCount;
        }
        this.niobuffer.put(this.position - 1, (byte) (this.niobuffer.get(this.position - 1) | 128));
    }

    private void writeUtf8Length(int value) {
        if ((value >>> 6) == 0) {
            require(1);
            this.niobuffer.put((byte) (value | 128));
            this.position++;
        } else if ((value >>> 13) == 0) {
            require(2);
            this.niobuffer.put((byte) ((value | 64) | 128));
            this.niobuffer.put((byte) (value >>> 6));
            this.position += 2;
        } else if ((value >>> 20) == 0) {
            require(3);
            this.niobuffer.put((byte) ((value | 64) | 128));
            this.niobuffer.put((byte) ((value >>> 6) | 128));
            this.niobuffer.put((byte) (value >>> 13));
            this.position += 3;
        } else if ((value >>> 27) == 0) {
            require(4);
            this.niobuffer.put((byte) ((value | 64) | 128));
            this.niobuffer.put((byte) ((value >>> 6) | 128));
            this.niobuffer.put((byte) ((value >>> 13) | 128));
            this.niobuffer.put((byte) (value >>> 20));
            this.position += 4;
        } else {
            require(5);
            this.niobuffer.put((byte) ((value | 64) | 128));
            this.niobuffer.put((byte) ((value >>> 6) | 128));
            this.niobuffer.put((byte) ((value >>> 13) | 128));
            this.niobuffer.put((byte) ((value >>> 20) | 128));
            this.niobuffer.put((byte) (value >>> 27));
            this.position += 5;
        }
    }

    private void writeString_slow(CharSequence value, int charCount, int charIndex) {
        while (charIndex < charCount) {
            if (this.position == this.capacity) {
                require(Math.min(this.capacity, charCount - charIndex));
            }
            int c = value.charAt(charIndex);
            ByteBuffer byteBuffer;
            int i;
            if (c <= 127) {
                byteBuffer = this.niobuffer;
                i = this.position;
                this.position = i + 1;
                byteBuffer.put(i, (byte) c);
            } else if (c > 2047) {
                byteBuffer = this.niobuffer;
                i = this.position;
                this.position = i + 1;
                byteBuffer.put(i, (byte) (((c >> 12) & 15) | 224));
                require(2);
                byteBuffer = this.niobuffer;
                i = this.position;
                this.position = i + 1;
                byteBuffer.put(i, (byte) (((c >> 6) & 63) | 128));
                byteBuffer = this.niobuffer;
                i = this.position;
                this.position = i + 1;
                byteBuffer.put(i, (byte) ((c & 63) | 128));
            } else {
                byteBuffer = this.niobuffer;
                i = this.position;
                this.position = i + 1;
                byteBuffer.put(i, (byte) (((c >> 6) & 31) | Opcodes.CHECKCAST));
                require(1);
                byteBuffer = this.niobuffer;
                i = this.position;
                this.position = i + 1;
                byteBuffer.put(i, (byte) ((c & 63) | 128));
            }
            charIndex++;
        }
    }

    private void writeAscii_slow(String value, int charCount) throws KryoException {
        ByteBuffer buffer = this.niobuffer;
        int charIndex = 0;
        int charsToWrite = Math.min(charCount, this.capacity - this.position);
        while (charIndex < charCount) {
            byte[] tmp = new byte[charCount];
            value.getBytes(charIndex, charIndex + charsToWrite, tmp, 0);
            buffer.put(tmp, 0, charsToWrite);
            charIndex += charsToWrite;
            this.position += charsToWrite;
            charsToWrite = Math.min(charCount - charIndex, this.capacity);
            if (require(charsToWrite)) {
                buffer = this.niobuffer;
            }
        }
    }

    public void writeFloat(float value) throws KryoException {
        require(4);
        this.niobuffer.putFloat(value);
        this.position += 4;
    }

    public int writeFloat(float value, float precision, boolean optimizePositive) throws KryoException {
        return writeInt((int) (value * precision), optimizePositive);
    }

    public void writeShort(int value) throws KryoException {
        require(2);
        this.niobuffer.putShort((short) value);
        this.position += 2;
    }

    public void writeLong(long value) throws KryoException {
        require(8);
        this.niobuffer.putLong(value);
        this.position += 8;
    }

    public int writeLong(long value, boolean optimizePositive) throws KryoException {
        if (this.varIntsEnabled) {
            return writeVarLong(value, optimizePositive);
        }
        writeLong(value);
        return 8;
    }

    public int writeVarLong(long value, boolean optimizePositive) throws KryoException {
        if (!optimizePositive) {
            value = (value << 1) ^ (value >> 63);
        }
        int varInt = (int) (127 & value);
        value >>>= 7;
        if (value == 0) {
            writeByte(varInt);
            return 1;
        }
        varInt = (int) (((long) (varInt | 128)) | ((127 & value) << 8));
        value >>>= 7;
        if (value == 0) {
            this.niobuffer.order(ByteOrder.LITTLE_ENDIAN);
            writeInt(varInt);
            this.niobuffer.order(this.byteOrder);
            this.position -= 2;
            this.niobuffer.position(this.position);
            return 2;
        }
        varInt = (int) (((long) (varInt | 32768)) | ((127 & value) << 16));
        value >>>= 7;
        if (value == 0) {
            this.niobuffer.order(ByteOrder.LITTLE_ENDIAN);
            writeInt(varInt);
            this.niobuffer.order(this.byteOrder);
            this.position--;
            this.niobuffer.position(this.position);
            return 3;
        }
        varInt = (int) (((long) (varInt | 8388608)) | ((127 & value) << 24));
        value >>>= 7;
        if (value == 0) {
            this.niobuffer.order(ByteOrder.LITTLE_ENDIAN);
            writeInt(varInt);
            this.niobuffer.order(this.byteOrder);
            this.position += 0;
            return 4;
        }
        long varLong = (((long) (varInt | Integer.MIN_VALUE)) & 4294967295L) | ((127 & value) << 32);
        value >>>= 7;
        if (value == 0) {
            this.niobuffer.order(ByteOrder.LITTLE_ENDIAN);
            writeLong(varLong);
            this.niobuffer.order(this.byteOrder);
            this.position -= 3;
            this.niobuffer.position(this.position);
            return 5;
        }
        varLong = (varLong | 549755813888L) | ((127 & value) << 40);
        value >>>= 7;
        if (value == 0) {
            this.niobuffer.order(ByteOrder.LITTLE_ENDIAN);
            writeLong(varLong);
            this.niobuffer.order(this.byteOrder);
            this.position -= 2;
            this.niobuffer.position(this.position);
            return 6;
        }
        varLong = (varLong | 140737488355328L) | ((127 & value) << 48);
        value >>>= 7;
        if (value == 0) {
            this.niobuffer.order(ByteOrder.LITTLE_ENDIAN);
            writeLong(varLong);
            this.niobuffer.order(this.byteOrder);
            this.position--;
            this.niobuffer.position(this.position);
            return 7;
        }
        varLong = (varLong | 36028797018963968L) | ((127 & value) << 56);
        value >>>= 7;
        if (value == 0) {
            this.niobuffer.order(ByteOrder.LITTLE_ENDIAN);
            writeLong(varLong);
            this.niobuffer.order(this.byteOrder);
            return 8;
        }
        varLong |= Long.MIN_VALUE;
        this.niobuffer.order(ByteOrder.LITTLE_ENDIAN);
        writeLong(varLong);
        this.niobuffer.order(this.byteOrder);
        write((byte) ((int) value));
        return 9;
    }

    public int writeLongS(long value, boolean optimizePositive) throws KryoException {
        if (!optimizePositive) {
            value = (value << 1) ^ (value >> 63);
        }
        if ((value >>> 7) == 0) {
            require(1);
            this.niobuffer.put((byte) ((int) value));
            this.position++;
            return 1;
        } else if ((value >>> 14) == 0) {
            require(2);
            this.niobuffer.put((byte) ((int) ((127 & value) | 128)));
            this.niobuffer.put((byte) ((int) (value >>> 7)));
            this.position += 2;
            return 2;
        } else if ((value >>> 21) == 0) {
            require(3);
            this.niobuffer.put((byte) ((int) ((127 & value) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 7) | 128)));
            this.niobuffer.put((byte) ((int) (value >>> 14)));
            this.position += 3;
            return 3;
        } else if ((value >>> 28) == 0) {
            require(4);
            this.niobuffer.put((byte) ((int) ((127 & value) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 7) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 14) | 128)));
            this.niobuffer.put((byte) ((int) (value >>> 21)));
            this.position += 4;
            return 4;
        } else if ((value >>> 35) == 0) {
            require(5);
            this.niobuffer.put((byte) ((int) ((127 & value) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 7) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 14) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 21) | 128)));
            this.niobuffer.put((byte) ((int) (value >>> 28)));
            this.position += 5;
            return 5;
        } else if ((value >>> 42) == 0) {
            require(6);
            this.niobuffer.put((byte) ((int) ((127 & value) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 7) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 14) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 21) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 28) | 128)));
            this.niobuffer.put((byte) ((int) (value >>> 35)));
            this.position += 6;
            return 6;
        } else if ((value >>> 49) == 0) {
            require(7);
            this.niobuffer.put((byte) ((int) ((127 & value) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 7) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 14) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 21) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 28) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 35) | 128)));
            this.niobuffer.put((byte) ((int) (value >>> 42)));
            this.position += 7;
            return 7;
        } else if ((value >>> 56) == 0) {
            require(8);
            this.niobuffer.put((byte) ((int) ((127 & value) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 7) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 14) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 21) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 28) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 35) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 42) | 128)));
            this.niobuffer.put((byte) ((int) (value >>> 49)));
            this.position += 8;
            return 8;
        } else {
            require(9);
            this.niobuffer.put((byte) ((int) ((127 & value) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 7) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 14) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 21) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 28) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 35) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 42) | 128)));
            this.niobuffer.put((byte) ((int) ((value >>> 49) | 128)));
            this.niobuffer.put((byte) ((int) (value >>> 56)));
            this.position += 9;
            return 9;
        }
    }

    public void writeBoolean(boolean value) throws KryoException {
        int i = 1;
        require(1);
        ByteBuffer byteBuffer = this.niobuffer;
        if (!value) {
            i = 0;
        }
        byteBuffer.put((byte) i);
        this.position++;
    }

    public void writeChar(char value) throws KryoException {
        require(2);
        this.niobuffer.putChar(value);
        this.position += 2;
    }

    public void writeDouble(double value) throws KryoException {
        require(8);
        this.niobuffer.putDouble(value);
        this.position += 8;
    }

    public int writeDouble(double value, double precision, boolean optimizePositive) throws KryoException {
        return writeLong((long) (value * precision), optimizePositive);
    }

    public void writeInts(int[] object) throws KryoException {
        if (this.capacity - this.position < object.length * 4 || !isNativeOrder()) {
            super.writeInts(object);
            return;
        }
        this.niobuffer.asIntBuffer().put(object);
        this.position += object.length * 4;
    }

    public void writeLongs(long[] object) throws KryoException {
        if (this.capacity - this.position < object.length * 8 || !isNativeOrder()) {
            super.writeLongs(object);
            return;
        }
        this.niobuffer.asLongBuffer().put(object);
        this.position += object.length * 8;
    }

    public void writeFloats(float[] object) throws KryoException {
        if (this.capacity - this.position < object.length * 4 || !isNativeOrder()) {
            super.writeFloats(object);
            return;
        }
        this.niobuffer.asFloatBuffer().put(object);
        this.position += object.length * 4;
    }

    public void writeShorts(short[] object) throws KryoException {
        if (this.capacity - this.position < object.length * 2 || !isNativeOrder()) {
            super.writeShorts(object);
            return;
        }
        this.niobuffer.asShortBuffer().put(object);
        this.position += object.length * 2;
    }

    public void writeChars(char[] object) throws KryoException {
        if (this.capacity - this.position < object.length * 2 || !isNativeOrder()) {
            super.writeChars(object);
            return;
        }
        this.niobuffer.asCharBuffer().put(object);
        this.position += object.length * 2;
    }

    public void writeDoubles(double[] object) throws KryoException {
        if (this.capacity - this.position < object.length * 8 || !isNativeOrder()) {
            super.writeDoubles(object);
            return;
        }
        this.niobuffer.asDoubleBuffer().put(object);
        this.position += object.length * 8;
    }

    private boolean isNativeOrder() {
        return this.byteOrder == nativeOrder;
    }

    public boolean getVarIntsEnabled() {
        return this.varIntsEnabled;
    }

    public void setVarIntsEnabled(boolean varIntsEnabled) {
        this.varIntsEnabled = varIntsEnabled;
    }
}
