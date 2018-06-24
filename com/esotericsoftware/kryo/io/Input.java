package com.esotericsoftware.kryo.io;

import com.esotericsoftware.kryo.KryoException;
import java.io.IOException;
import java.io.InputStream;

public class Input extends InputStream {
    protected byte[] buffer;
    protected int capacity;
    protected char[] chars;
    protected InputStream inputStream;
    protected int limit;
    protected int position;
    protected long total;

    public Input() {
        this.chars = new char[32];
    }

    public Input(int bufferSize) {
        this.chars = new char[32];
        this.capacity = bufferSize;
        this.buffer = new byte[bufferSize];
    }

    public Input(byte[] buffer) {
        this.chars = new char[32];
        setBuffer(buffer, 0, buffer.length);
    }

    public Input(byte[] buffer, int offset, int count) {
        this.chars = new char[32];
        setBuffer(buffer, offset, count);
    }

    public Input(InputStream inputStream) {
        this(4096);
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream cannot be null.");
        }
        this.inputStream = inputStream;
    }

    public Input(InputStream inputStream, int bufferSize) {
        this(bufferSize);
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream cannot be null.");
        }
        this.inputStream = inputStream;
    }

    public void setBuffer(byte[] bytes) {
        setBuffer(bytes, 0, bytes.length);
    }

    public void setBuffer(byte[] bytes, int offset, int count) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        }
        this.buffer = bytes;
        this.position = offset;
        this.limit = offset + count;
        this.capacity = bytes.length;
        this.total = 0;
        this.inputStream = null;
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.limit = 0;
        rewind();
    }

    public long total() {
        return this.total + ((long) this.position);
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int position() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int limit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void rewind() {
        this.position = 0;
        this.total = 0;
    }

    public void skip(int count) throws KryoException {
        int skipCount = Math.min(this.limit - this.position, count);
        while (true) {
            this.position += skipCount;
            count -= skipCount;
            if (count != 0) {
                skipCount = Math.min(count, this.capacity);
                require(skipCount);
            } else {
                return;
            }
        }
    }

    protected int fill(byte[] buffer, int offset, int count) throws KryoException {
        if (this.inputStream == null) {
            return -1;
        }
        try {
            return this.inputStream.read(buffer, offset, count);
        } catch (Throwable ex) {
            throw new KryoException(ex);
        }
    }

    protected int require(int required) throws KryoException {
        int remaining = this.limit - this.position;
        if (remaining >= required) {
            return remaining;
        }
        if (required > this.capacity) {
            throw new KryoException("Buffer too small: capacity: " + this.capacity + ", required: " + required);
        }
        int count;
        if (remaining > 0) {
            count = fill(this.buffer, this.limit, this.capacity - this.limit);
            if (count == -1) {
                throw new KryoException("Buffer underflow.");
            }
            remaining += count;
            if (remaining >= required) {
                this.limit += count;
                return remaining;
            }
        }
        System.arraycopy(this.buffer, this.position, this.buffer, 0, remaining);
        this.total += (long) this.position;
        this.position = 0;
        do {
            count = fill(this.buffer, remaining, this.capacity - remaining);
            if (count != -1) {
                remaining += count;
            } else if (remaining < required) {
                throw new KryoException("Buffer underflow.");
            }
            this.limit = remaining;
            return remaining;
        } while (remaining < required);
        this.limit = remaining;
        return remaining;
    }

    private int optional(int optional) throws KryoException {
        int remaining = this.limit - this.position;
        if (remaining >= optional) {
            return optional;
        }
        optional = Math.min(optional, this.capacity);
        int count = fill(this.buffer, this.limit, this.capacity - this.limit);
        if (count != -1) {
            remaining += count;
            if (remaining >= optional) {
                this.limit += count;
                return optional;
            }
            System.arraycopy(this.buffer, this.position, this.buffer, 0, remaining);
            this.total += (long) this.position;
            this.position = 0;
            do {
                count = fill(this.buffer, remaining, this.capacity - remaining);
                if (count == -1) {
                    break;
                }
                remaining += count;
            } while (remaining < optional);
            this.limit = remaining;
            if (remaining != 0) {
                return Math.min(remaining, optional);
            }
            return -1;
        } else if (remaining != 0) {
            return Math.min(remaining, optional);
        } else {
            return -1;
        }
    }

    public boolean eof() {
        return optional(1) <= 0;
    }

    public int available() throws IOException {
        return (this.inputStream != null ? this.inputStream.available() : 0) + (this.limit - this.position);
    }

    public int read() throws KryoException {
        if (optional(1) <= 0) {
            return -1;
        }
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        return bArr[i] & 255;
    }

    public int read(byte[] bytes) throws KryoException {
        return read(bytes, 0, bytes.length);
    }

    public int read(byte[] bytes, int offset, int count) throws KryoException {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        }
        int startingCount = count;
        int copyCount = Math.min(this.limit - this.position, count);
        do {
            System.arraycopy(this.buffer, this.position, bytes, offset, copyCount);
            this.position += copyCount;
            count -= copyCount;
            if (count == 0) {
                break;
            }
            offset += copyCount;
            copyCount = optional(count);
            if (copyCount == -1) {
                if (startingCount == count) {
                    return -1;
                }
            }
        } while (this.position != this.limit);
        return startingCount - count;
    }

    public long skip(long count) throws KryoException {
        long remaining = count;
        while (remaining > 0) {
            int skip = (int) Math.min(2147483647L, remaining);
            skip(skip);
            remaining -= (long) skip;
        }
        return count;
    }

    public void close() throws KryoException {
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public byte readByte() throws KryoException {
        require(1);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        return bArr[i];
    }

    public int readByteUnsigned() throws KryoException {
        require(1);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        return bArr[i] & 255;
    }

    public byte[] readBytes(int length) throws KryoException {
        byte[] bytes = new byte[length];
        readBytes(bytes, 0, length);
        return bytes;
    }

    public void readBytes(byte[] bytes) throws KryoException {
        readBytes(bytes, 0, bytes.length);
    }

    public void readBytes(byte[] bytes, int offset, int count) throws KryoException {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        }
        int copyCount = Math.min(this.limit - this.position, count);
        while (true) {
            System.arraycopy(this.buffer, this.position, bytes, offset, copyCount);
            this.position += copyCount;
            count -= copyCount;
            if (count != 0) {
                offset += copyCount;
                copyCount = Math.min(count, this.capacity);
                require(copyCount);
            } else {
                return;
            }
        }
    }

    public int readInt() throws KryoException {
        require(4);
        byte[] buffer = this.buffer;
        int position = this.position;
        this.position = position + 4;
        return ((((buffer[position] & 255) << 24) | ((buffer[position + 1] & 255) << 16)) | ((buffer[position + 2] & 255) << 8)) | (buffer[position + 3] & 255);
    }

    public int readInt(boolean optimizePositive) throws KryoException {
        return readVarInt(optimizePositive);
    }

    public int readVarInt(boolean optimizePositive) throws KryoException {
        if (require(1) < 5) {
            return readInt_slow(optimizePositive);
        }
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        int b = bArr[i];
        int result = b & 127;
        if ((b & 128) != 0) {
            byte[] buffer = this.buffer;
            int i2 = this.position;
            this.position = i2 + 1;
            b = buffer[i2];
            result |= (b & 127) << 7;
            if ((b & 128) != 0) {
                i2 = this.position;
                this.position = i2 + 1;
                b = buffer[i2];
                result |= (b & 127) << 14;
                if ((b & 128) != 0) {
                    i2 = this.position;
                    this.position = i2 + 1;
                    b = buffer[i2];
                    result |= (b & 127) << 21;
                    if ((b & 128) != 0) {
                        i2 = this.position;
                        this.position = i2 + 1;
                        result |= (buffer[i2] & 127) << 28;
                    }
                }
            }
        }
        return !optimizePositive ? (result >>> 1) ^ (-(result & 1)) : result;
    }

    private int readInt_slow(boolean optimizePositive) {
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        int b = bArr[i];
        int result = b & 127;
        if ((b & 128) != 0) {
            require(1);
            byte[] buffer = this.buffer;
            int i2 = this.position;
            this.position = i2 + 1;
            b = buffer[i2];
            result |= (b & 127) << 7;
            if ((b & 128) != 0) {
                require(1);
                i2 = this.position;
                this.position = i2 + 1;
                b = buffer[i2];
                result |= (b & 127) << 14;
                if ((b & 128) != 0) {
                    require(1);
                    i2 = this.position;
                    this.position = i2 + 1;
                    b = buffer[i2];
                    result |= (b & 127) << 21;
                    if ((b & 128) != 0) {
                        require(1);
                        i2 = this.position;
                        this.position = i2 + 1;
                        result |= (buffer[i2] & 127) << 28;
                    }
                }
            }
        }
        return optimizePositive ? result : (result >>> 1) ^ (-(result & 1));
    }

    public boolean canReadInt() throws KryoException {
        if (this.limit - this.position >= 5) {
            return true;
        }
        if (optional(5) <= 0) {
            return false;
        }
        int p = this.position;
        int p2 = p + 1;
        if ((this.buffer[p] & 128) == 0) {
            return true;
        }
        if (p2 == this.limit) {
            return false;
        }
        p = p2 + 1;
        if ((this.buffer[p2] & 128) == 0) {
            return true;
        }
        if (p == this.limit) {
            return false;
        }
        p2 = p + 1;
        if ((this.buffer[p] & 128) == 0) {
            return true;
        }
        if (p2 == this.limit) {
            return false;
        }
        p = p2 + 1;
        if ((this.buffer[p2] & 128) == 0 || p != this.limit) {
            return true;
        }
        return false;
    }

    public boolean canReadLong() throws KryoException {
        if (this.limit - this.position >= 9) {
            return true;
        }
        if (optional(5) <= 0) {
            return false;
        }
        int p = this.position;
        int p2 = p + 1;
        if ((this.buffer[p] & 128) == 0) {
            return true;
        }
        if (p2 == this.limit) {
            return false;
        }
        p = p2 + 1;
        if ((this.buffer[p2] & 128) == 0) {
            return true;
        }
        if (p == this.limit) {
            return false;
        }
        p2 = p + 1;
        if ((this.buffer[p] & 128) == 0) {
            return true;
        }
        if (p2 == this.limit) {
            return false;
        }
        p = p2 + 1;
        if ((this.buffer[p2] & 128) == 0) {
            return true;
        }
        if (p == this.limit) {
            return false;
        }
        p2 = p + 1;
        if ((this.buffer[p] & 128) == 0) {
            return true;
        }
        if (p2 == this.limit) {
            return false;
        }
        p = p2 + 1;
        if ((this.buffer[p2] & 128) == 0) {
            return true;
        }
        if (p == this.limit) {
            return false;
        }
        p2 = p + 1;
        if ((this.buffer[p] & 128) == 0) {
            return true;
        }
        if (p2 == this.limit) {
            return false;
        }
        p = p2 + 1;
        if ((this.buffer[p2] & 128) == 0 || p != this.limit) {
            return true;
        }
        return false;
    }

    public String readString() {
        int available = require(1);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        int b = bArr[i];
        if ((b & 128) == 0) {
            return readAscii();
        }
        int charCount = available >= 5 ? readUtf8Length(b) : readUtf8Length_slow(b);
        switch (charCount) {
            case 0:
                return null;
            case 1:
                return "";
            default:
                charCount--;
                if (this.chars.length < charCount) {
                    this.chars = new char[charCount];
                }
                readUtf8(charCount);
                return new String(this.chars, 0, charCount);
        }
    }

    private int readUtf8Length(int b) {
        int result = b & 63;
        if ((b & 64) == 0) {
            return result;
        }
        byte[] buffer = this.buffer;
        int i = this.position;
        this.position = i + 1;
        b = buffer[i];
        result |= (b & 127) << 6;
        if ((b & 128) == 0) {
            return result;
        }
        i = this.position;
        this.position = i + 1;
        b = buffer[i];
        result |= (b & 127) << 13;
        if ((b & 128) == 0) {
            return result;
        }
        i = this.position;
        this.position = i + 1;
        b = buffer[i];
        result |= (b & 127) << 20;
        if ((b & 128) == 0) {
            return result;
        }
        i = this.position;
        this.position = i + 1;
        return result | ((buffer[i] & 127) << 27);
    }

    private int readUtf8Length_slow(int b) {
        int result = b & 63;
        if ((b & 64) == 0) {
            return result;
        }
        require(1);
        byte[] buffer = this.buffer;
        int i = this.position;
        this.position = i + 1;
        b = buffer[i];
        result |= (b & 127) << 6;
        if ((b & 128) == 0) {
            return result;
        }
        require(1);
        i = this.position;
        this.position = i + 1;
        b = buffer[i];
        result |= (b & 127) << 13;
        if ((b & 128) == 0) {
            return result;
        }
        require(1);
        i = this.position;
        this.position = i + 1;
        b = buffer[i];
        result |= (b & 127) << 20;
        if ((b & 128) == 0) {
            return result;
        }
        require(1);
        i = this.position;
        this.position = i + 1;
        return result | ((buffer[i] & 127) << 27);
    }

    private void readUtf8(int charCount) {
        int position;
        byte[] buffer = this.buffer;
        char[] chars = this.chars;
        int count = Math.min(require(1), charCount);
        int position2 = this.position;
        int i = 0;
        while (i < count) {
            position = position2 + 1;
            int b = buffer[position2];
            if (b < 0) {
                position--;
                break;
            }
            int charIndex = i + 1;
            chars[i] = (char) b;
            position2 = position;
            i = charIndex;
        }
        position = position2;
        this.position = position;
        if (i < charCount) {
            readUtf8_slow(charCount, i);
        }
    }

    private void readUtf8_slow(int charCount, int charIndex) {
        char[] chars = this.chars;
        byte[] buffer = this.buffer;
        while (charIndex < charCount) {
            if (this.position == this.limit) {
                require(1);
            }
            int i = this.position;
            this.position = i + 1;
            int b = buffer[i] & 255;
            int i2;
            switch (b >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    chars[charIndex] = (char) b;
                    break;
                case 12:
                case 13:
                    if (this.position == this.limit) {
                        require(1);
                    }
                    i = (b & 31) << 6;
                    i2 = this.position;
                    this.position = i2 + 1;
                    chars[charIndex] = (char) (i | (buffer[i2] & 63));
                    break;
                case 14:
                    require(2);
                    i = (b & 15) << 12;
                    i2 = this.position;
                    this.position = i2 + 1;
                    i |= (buffer[i2] & 63) << 6;
                    i2 = this.position;
                    this.position = i2 + 1;
                    chars[charIndex] = (char) (i | (buffer[i2] & 63));
                    break;
                default:
                    break;
            }
            charIndex++;
        }
    }

    private String readAscii() {
        byte[] buffer = this.buffer;
        int end = this.position;
        int start = end - 1;
        int limit = this.limit;
        int end2 = end;
        while (end2 != limit) {
            end = end2 + 1;
            if ((buffer[end2] & 128) != 0) {
                int i = end - 1;
                buffer[i] = (byte) (buffer[i] & 127);
                String value = new String(buffer, 0, start, end - start);
                i = end - 1;
                buffer[i] = (byte) (buffer[i] | 128);
                this.position = end;
                return value;
            }
            end2 = end;
        }
        end = end2;
        return readAscii_slow();
    }

    private String readAscii_slow() {
        this.position--;
        int charCount = this.limit - this.position;
        if (charCount > this.chars.length) {
            this.chars = new char[(charCount * 2)];
        }
        char[] chars = this.chars;
        byte[] buffer = this.buffer;
        int i = this.position;
        int ii = 0;
        int n = this.limit;
        while (i < n) {
            chars[ii] = (char) buffer[i];
            i++;
            ii++;
        }
        this.position = this.limit;
        int charCount2 = charCount;
        while (true) {
            require(1);
            int i2 = this.position;
            this.position = i2 + 1;
            int b = buffer[i2];
            if (charCount2 == chars.length) {
                char[] newChars = new char[(charCount2 * 2)];
                System.arraycopy(chars, 0, newChars, 0, charCount2);
                chars = newChars;
                this.chars = newChars;
            }
            if ((b & 128) == 128) {
                charCount = charCount2 + 1;
                chars[charCount2] = (char) (b & 127);
                return new String(chars, 0, charCount);
            }
            charCount = charCount2 + 1;
            chars[charCount2] = (char) b;
            charCount2 = charCount;
        }
    }

    public StringBuilder readStringBuilder() {
        int available = require(1);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        int b = bArr[i];
        if ((b & 128) == 0) {
            return new StringBuilder(readAscii());
        }
        int charCount = available >= 5 ? readUtf8Length(b) : readUtf8Length_slow(b);
        switch (charCount) {
            case 0:
                return null;
            case 1:
                return new StringBuilder("");
            default:
                charCount--;
                if (this.chars.length < charCount) {
                    this.chars = new char[charCount];
                }
                readUtf8(charCount);
                StringBuilder builder = new StringBuilder(charCount);
                builder.append(this.chars, 0, charCount);
                return builder;
        }
    }

    public float readFloat() throws KryoException {
        return Float.intBitsToFloat(readInt());
    }

    public float readFloat(float precision, boolean optimizePositive) throws KryoException {
        return ((float) readInt(optimizePositive)) / precision;
    }

    public short readShort() throws KryoException {
        require(2);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & 255) << 8;
        byte[] bArr2 = this.buffer;
        int i3 = this.position;
        this.position = i3 + 1;
        return (short) (i2 | (bArr2[i3] & 255));
    }

    public int readShortUnsigned() throws KryoException {
        require(2);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & 255) << 8;
        byte[] bArr2 = this.buffer;
        int i3 = this.position;
        this.position = i3 + 1;
        return i2 | (bArr2[i3] & 255);
    }

    public long readLong() throws KryoException {
        require(8);
        byte[] buffer = this.buffer;
        int i = this.position;
        this.position = i + 1;
        long j = ((long) buffer[i]) << 56;
        i = this.position;
        this.position = i + 1;
        j |= ((long) (buffer[i] & 255)) << 48;
        i = this.position;
        this.position = i + 1;
        j |= ((long) (buffer[i] & 255)) << 40;
        i = this.position;
        this.position = i + 1;
        j |= ((long) (buffer[i] & 255)) << 32;
        i = this.position;
        this.position = i + 1;
        j |= ((long) (buffer[i] & 255)) << 24;
        i = this.position;
        this.position = i + 1;
        j |= (long) ((buffer[i] & 255) << 16);
        i = this.position;
        this.position = i + 1;
        j |= (long) ((buffer[i] & 255) << 8);
        i = this.position;
        this.position = i + 1;
        return j | ((long) (buffer[i] & 255));
    }

    public long readLong(boolean optimizePositive) throws KryoException {
        return readVarLong(optimizePositive);
    }

    public long readVarLong(boolean optimizePositive) throws KryoException {
        if (require(1) < 9) {
            return readLong_slow(optimizePositive);
        }
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        int b = bArr[i];
        long result = (long) (b & 127);
        if ((b & 128) != 0) {
            byte[] buffer = this.buffer;
            int i2 = this.position;
            this.position = i2 + 1;
            b = buffer[i2];
            result |= (long) ((b & 127) << 7);
            if ((b & 128) != 0) {
                i2 = this.position;
                this.position = i2 + 1;
                b = buffer[i2];
                result |= (long) ((b & 127) << 14);
                if ((b & 128) != 0) {
                    i2 = this.position;
                    this.position = i2 + 1;
                    b = buffer[i2];
                    result |= (long) ((b & 127) << 21);
                    if ((b & 128) != 0) {
                        i2 = this.position;
                        this.position = i2 + 1;
                        b = buffer[i2];
                        result |= ((long) (b & 127)) << 28;
                        if ((b & 128) != 0) {
                            i2 = this.position;
                            this.position = i2 + 1;
                            b = buffer[i2];
                            result |= ((long) (b & 127)) << 35;
                            if ((b & 128) != 0) {
                                i2 = this.position;
                                this.position = i2 + 1;
                                b = buffer[i2];
                                result |= ((long) (b & 127)) << 42;
                                if ((b & 128) != 0) {
                                    i2 = this.position;
                                    this.position = i2 + 1;
                                    b = buffer[i2];
                                    result |= ((long) (b & 127)) << 49;
                                    if ((b & 128) != 0) {
                                        i2 = this.position;
                                        this.position = i2 + 1;
                                        result |= ((long) buffer[i2]) << 56;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (optimizePositive) {
            return result;
        }
        return (result >>> 1) ^ (-(1 & result));
    }

    private long readLong_slow(boolean optimizePositive) {
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        int b = bArr[i];
        long result = (long) (b & 127);
        if ((b & 128) != 0) {
            require(1);
            byte[] buffer = this.buffer;
            int i2 = this.position;
            this.position = i2 + 1;
            b = buffer[i2];
            result |= (long) ((b & 127) << 7);
            if ((b & 128) != 0) {
                require(1);
                i2 = this.position;
                this.position = i2 + 1;
                b = buffer[i2];
                result |= (long) ((b & 127) << 14);
                if ((b & 128) != 0) {
                    require(1);
                    i2 = this.position;
                    this.position = i2 + 1;
                    b = buffer[i2];
                    result |= (long) ((b & 127) << 21);
                    if ((b & 128) != 0) {
                        require(1);
                        i2 = this.position;
                        this.position = i2 + 1;
                        b = buffer[i2];
                        result |= ((long) (b & 127)) << 28;
                        if ((b & 128) != 0) {
                            require(1);
                            i2 = this.position;
                            this.position = i2 + 1;
                            b = buffer[i2];
                            result |= ((long) (b & 127)) << 35;
                            if ((b & 128) != 0) {
                                require(1);
                                i2 = this.position;
                                this.position = i2 + 1;
                                b = buffer[i2];
                                result |= ((long) (b & 127)) << 42;
                                if ((b & 128) != 0) {
                                    require(1);
                                    i2 = this.position;
                                    this.position = i2 + 1;
                                    b = buffer[i2];
                                    result |= ((long) (b & 127)) << 49;
                                    if ((b & 128) != 0) {
                                        require(1);
                                        i2 = this.position;
                                        this.position = i2 + 1;
                                        result |= ((long) buffer[i2]) << 56;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (optimizePositive) {
            return result;
        }
        return (result >>> 1) ^ (-(1 & result));
    }

    public boolean readBoolean() throws KryoException {
        require(1);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        if (bArr[i] == (byte) 1) {
            return true;
        }
        return false;
    }

    public char readChar() throws KryoException {
        require(2);
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & 255) << 8;
        byte[] bArr2 = this.buffer;
        int i3 = this.position;
        this.position = i3 + 1;
        return (char) (i2 | (bArr2[i3] & 255));
    }

    public double readDouble() throws KryoException {
        return Double.longBitsToDouble(readLong());
    }

    public double readDouble(double precision, boolean optimizePositive) throws KryoException {
        return ((double) readLong(optimizePositive)) / precision;
    }

    public int[] readInts(int length, boolean optimizePositive) throws KryoException {
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = readInt(optimizePositive);
        }
        return array;
    }

    public long[] readLongs(int length, boolean optimizePositive) throws KryoException {
        long[] array = new long[length];
        for (int i = 0; i < length; i++) {
            array[i] = readLong(optimizePositive);
        }
        return array;
    }

    public int[] readInts(int length) throws KryoException {
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = readInt();
        }
        return array;
    }

    public long[] readLongs(int length) throws KryoException {
        long[] array = new long[length];
        for (int i = 0; i < length; i++) {
            array[i] = readLong();
        }
        return array;
    }

    public float[] readFloats(int length) throws KryoException {
        float[] array = new float[length];
        for (int i = 0; i < length; i++) {
            array[i] = readFloat();
        }
        return array;
    }

    public short[] readShorts(int length) throws KryoException {
        short[] array = new short[length];
        for (int i = 0; i < length; i++) {
            array[i] = readShort();
        }
        return array;
    }

    public char[] readChars(int length) throws KryoException {
        char[] array = new char[length];
        for (int i = 0; i < length; i++) {
            array[i] = readChar();
        }
        return array;
    }

    public double[] readDoubles(int length) throws KryoException {
        double[] array = new double[length];
        for (int i = 0; i < length; i++) {
            array[i] = readDouble();
        }
        return array;
    }
}
