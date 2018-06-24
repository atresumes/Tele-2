package com.esotericsoftware.kryo.io;

import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.util.UnsafeUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteBufferInput extends Input {
    protected static final ByteOrder nativeOrder = ByteOrder.nativeOrder();
    ByteOrder byteOrder;
    protected ByteBuffer niobuffer;
    protected boolean varIntsEnabled;

    public ByteBufferInput() {
        this.varIntsEnabled = true;
        this.byteOrder = ByteOrder.BIG_ENDIAN;
    }

    public ByteBufferInput(int bufferSize) {
        this.varIntsEnabled = true;
        this.byteOrder = ByteOrder.BIG_ENDIAN;
        this.capacity = bufferSize;
        this.niobuffer = ByteBuffer.allocateDirect(bufferSize);
        this.niobuffer.order(this.byteOrder);
    }

    public ByteBufferInput(byte[] buffer) {
        this.varIntsEnabled = true;
        this.byteOrder = ByteOrder.BIG_ENDIAN;
        setBuffer(buffer);
    }

    public ByteBufferInput(ByteBuffer buffer) {
        this.varIntsEnabled = true;
        this.byteOrder = ByteOrder.BIG_ENDIAN;
        setBuffer(buffer);
    }

    public ByteBufferInput(InputStream inputStream) {
        this(4096);
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream cannot be null.");
        }
        this.inputStream = inputStream;
    }

    public ByteBufferInput(InputStream inputStream, int bufferSize) {
        this(bufferSize);
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream cannot be null.");
        }
        this.inputStream = inputStream;
    }

    public ByteOrder order() {
        return this.byteOrder;
    }

    public void order(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

    public void setBuffer(byte[] bytes) {
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(bytes.length);
        directBuffer.put(bytes);
        directBuffer.position(0);
        directBuffer.limit(bytes.length);
        directBuffer.order(this.byteOrder);
        setBuffer(directBuffer);
    }

    public void setBuffer(ByteBuffer buffer) {
        if (buffer == null) {
            throw new IllegalArgumentException("buffer cannot be null.");
        }
        this.niobuffer = buffer;
        this.position = buffer.position();
        this.limit = buffer.limit();
        this.capacity = buffer.capacity();
        this.byteOrder = buffer.order();
        this.total = 0;
        this.inputStream = null;
    }

    public void release() {
        close();
        UnsafeUtil.releaseBuffer(this.niobuffer);
        this.niobuffer = null;
    }

    public ByteBufferInput(long address, int size) {
        this.varIntsEnabled = true;
        this.byteOrder = ByteOrder.BIG_ENDIAN;
        setBuffer(UnsafeUtil.getDirectBufferAt(address, size));
    }

    public ByteBuffer getByteBuffer() {
        return this.niobuffer;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.limit = 0;
        rewind();
    }

    public void rewind() {
        super.rewind();
        this.niobuffer.position(0);
    }

    protected int fill(ByteBuffer buffer, int offset, int count) throws KryoException {
        if (this.inputStream == null) {
            return -1;
        }
        try {
            byte[] tmp = new byte[count];
            int result = this.inputStream.read(tmp, 0, count);
            buffer.position(offset);
            if (result < 0) {
                return result;
            }
            buffer.put(tmp, 0, result);
            buffer.position(offset);
            return result;
        } catch (Throwable ex) {
            throw new KryoException(ex);
        }
    }

    protected final int require(int required) throws KryoException {
        int i = this.limit - this.position;
        if (i >= required) {
            return i;
        }
        if (required > this.capacity) {
            throw new KryoException("Buffer too small: capacity: " + this.capacity + ", required: " + required);
        }
        int count;
        if (i > 0) {
            count = fill(this.niobuffer, this.limit, this.capacity - this.limit);
            if (count == -1) {
                throw new KryoException("Buffer underflow.");
            }
            i += count;
            if (i >= required) {
                this.limit += count;
                return i;
            }
        }
        this.niobuffer.position(this.position);
        this.niobuffer.compact();
        this.total += (long) this.position;
        this.position = 0;
        do {
            count = fill(this.niobuffer, i, this.capacity - i);
            if (count != -1) {
                i += count;
            } else if (i < required) {
                throw new KryoException("Buffer underflow.");
            }
            this.limit = i;
            this.niobuffer.position(0);
            return i;
        } while (i < required);
        this.limit = i;
        this.niobuffer.position(0);
        return i;
    }

    private int optional(int optional) throws KryoException {
        int remaining = this.limit - this.position;
        if (remaining >= optional) {
            return optional;
        }
        optional = Math.min(optional, this.capacity);
        int count = fill(this.niobuffer, this.limit, this.capacity - this.limit);
        if (count != -1) {
            remaining += count;
            if (remaining >= optional) {
                this.limit += count;
                return optional;
            }
            this.niobuffer.compact();
            this.total += (long) this.position;
            this.position = 0;
            do {
                count = fill(this.niobuffer, remaining, this.capacity - remaining);
                if (count == -1) {
                    break;
                }
                remaining += count;
            } while (remaining < optional);
            this.limit = remaining;
            this.niobuffer.position(this.position);
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

    public int read() throws KryoException {
        if (optional(1) <= 0) {
            return -1;
        }
        this.niobuffer.position(this.position);
        this.position++;
        return this.niobuffer.get() & 255;
    }

    public int read(byte[] bytes) throws KryoException {
        this.niobuffer.position(this.position);
        return read(bytes, 0, bytes.length);
    }

    public int read(byte[] bytes, int offset, int count) throws KryoException {
        this.niobuffer.position(this.position);
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null.");
        }
        int startingCount = count;
        int copyCount = Math.min(this.limit - this.position, count);
        do {
            this.niobuffer.get(bytes, offset, copyCount);
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

    public void setPosition(int position) {
        this.position = position;
        this.niobuffer.position(position);
    }

    public void setLimit(int limit) {
        this.limit = limit;
        this.niobuffer.limit(limit);
    }

    public void skip(int count) throws KryoException {
        super.skip(count);
        this.niobuffer.position(position());
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
        this.niobuffer.position(this.position);
        require(1);
        this.position++;
        return this.niobuffer.get();
    }

    public int readByteUnsigned() throws KryoException {
        require(1);
        this.position++;
        return this.niobuffer.get() & 255;
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
            this.niobuffer.get(bytes, offset, copyCount);
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
        this.position += 4;
        return this.niobuffer.getInt();
    }

    public int readInt(boolean optimizePositive) throws KryoException {
        if (this.varIntsEnabled) {
            return readVarInt(optimizePositive);
        }
        return readInt();
    }

    public int readVarInt(boolean optimizePositive) throws KryoException {
        this.niobuffer.position(this.position);
        if (require(1) < 5) {
            return readInt_slow(optimizePositive);
        }
        this.position++;
        int b = this.niobuffer.get();
        int result = b & 127;
        if ((b & 128) != 0) {
            this.position++;
            b = this.niobuffer.get();
            result |= (b & 127) << 7;
            if ((b & 128) != 0) {
                this.position++;
                b = this.niobuffer.get();
                result |= (b & 127) << 14;
                if ((b & 128) != 0) {
                    this.position++;
                    b = this.niobuffer.get();
                    result |= (b & 127) << 21;
                    if ((b & 128) != 0) {
                        this.position++;
                        result |= (this.niobuffer.get() & 127) << 28;
                    }
                }
            }
        }
        return !optimizePositive ? (result >>> 1) ^ (-(result & 1)) : result;
    }

    private int readInt_slow(boolean optimizePositive) {
        this.position++;
        int b = this.niobuffer.get();
        int result = b & 127;
        if ((b & 128) != 0) {
            require(1);
            this.position++;
            b = this.niobuffer.get();
            result |= (b & 127) << 7;
            if ((b & 128) != 0) {
                require(1);
                this.position++;
                b = this.niobuffer.get();
                result |= (b & 127) << 14;
                if ((b & 128) != 0) {
                    require(1);
                    this.position++;
                    b = this.niobuffer.get();
                    result |= (b & 127) << 21;
                    if ((b & 128) != 0) {
                        require(1);
                        this.position++;
                        result |= (this.niobuffer.get() & 127) << 28;
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
        if ((this.niobuffer.get(p) & 128) == 0) {
            return true;
        }
        if (p2 == this.limit) {
            return false;
        }
        p = p2 + 1;
        if ((this.niobuffer.get(p2) & 128) == 0) {
            return true;
        }
        if (p == this.limit) {
            return false;
        }
        p2 = p + 1;
        if ((this.niobuffer.get(p) & 128) == 0) {
            return true;
        }
        if (p2 == this.limit) {
            return false;
        }
        p = p2 + 1;
        if ((this.niobuffer.get(p2) & 128) == 0 || p != this.limit) {
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
        if ((this.niobuffer.get(p) & 128) == 0) {
            return true;
        }
        if (p2 == this.limit) {
            return false;
        }
        p = p2 + 1;
        if ((this.niobuffer.get(p2) & 128) == 0) {
            return true;
        }
        if (p == this.limit) {
            return false;
        }
        p2 = p + 1;
        if ((this.niobuffer.get(p) & 128) == 0) {
            return true;
        }
        if (p2 == this.limit) {
            return false;
        }
        p = p2 + 1;
        if ((this.niobuffer.get(p2) & 128) == 0) {
            return true;
        }
        if (p == this.limit) {
            return false;
        }
        p2 = p + 1;
        if ((this.niobuffer.get(p) & 128) == 0) {
            return true;
        }
        if (p2 == this.limit) {
            return false;
        }
        p = p2 + 1;
        if ((this.niobuffer.get(p2) & 128) == 0) {
            return true;
        }
        if (p == this.limit) {
            return false;
        }
        p2 = p + 1;
        if ((this.niobuffer.get(p) & 128) == 0) {
            return true;
        }
        if (p2 == this.limit) {
            return false;
        }
        p = p2 + 1;
        if ((this.niobuffer.get(p2) & 128) == 0 || p != this.limit) {
            return true;
        }
        return false;
    }

    public String readString() {
        this.niobuffer.position(this.position);
        int available = require(1);
        this.position++;
        int b = this.niobuffer.get();
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
        this.position++;
        b = this.niobuffer.get();
        result |= (b & 127) << 6;
        if ((b & 128) == 0) {
            return result;
        }
        this.position++;
        b = this.niobuffer.get();
        result |= (b & 127) << 13;
        if ((b & 128) == 0) {
            return result;
        }
        this.position++;
        b = this.niobuffer.get();
        result |= (b & 127) << 20;
        if ((b & 128) == 0) {
            return result;
        }
        this.position++;
        return result | ((this.niobuffer.get() & 127) << 27);
    }

    private int readUtf8Length_slow(int b) {
        int result = b & 63;
        if ((b & 64) == 0) {
            return result;
        }
        require(1);
        this.position++;
        b = this.niobuffer.get();
        result |= (b & 127) << 6;
        if ((b & 128) == 0) {
            return result;
        }
        require(1);
        this.position++;
        b = this.niobuffer.get();
        result |= (b & 127) << 13;
        if ((b & 128) == 0) {
            return result;
        }
        require(1);
        this.position++;
        b = this.niobuffer.get();
        result |= (b & 127) << 20;
        if ((b & 128) == 0) {
            return result;
        }
        require(1);
        this.position++;
        return result | ((this.niobuffer.get() & 127) << 27);
    }

    private void readUtf8(int charCount) {
        char[] chars = this.chars;
        int count = Math.min(require(1), charCount);
        int position = this.position;
        int i = 0;
        while (i < count) {
            position++;
            int b = this.niobuffer.get();
            if (b < 0) {
                position--;
                break;
            }
            int charIndex = i + 1;
            chars[i] = (char) b;
            i = charIndex;
        }
        this.position = position;
        if (i < charCount) {
            this.niobuffer.position(position);
            readUtf8_slow(charCount, i);
        }
    }

    private void readUtf8_slow(int charCount, int charIndex) {
        char[] chars = this.chars;
        while (charIndex < charCount) {
            if (this.position == this.limit) {
                require(1);
            }
            this.position++;
            int b = this.niobuffer.get() & 255;
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
                    this.position++;
                    chars[charIndex] = (char) (((b & 31) << 6) | (this.niobuffer.get() & 63));
                    break;
                case 14:
                    require(2);
                    this.position += 2;
                    chars[charIndex] = (char) ((((b & 15) << 12) | ((this.niobuffer.get() & 63) << 6)) | (this.niobuffer.get() & 63));
                    break;
                default:
                    break;
            }
            charIndex++;
        }
    }

    private String readAscii() {
        int end = this.position;
        int start = end - 1;
        int limit = this.limit;
        while (end != limit) {
            end++;
            if ((this.niobuffer.get() & 128) != 0) {
                this.niobuffer.put(end - 1, (byte) (this.niobuffer.get(end - 1) & 127));
                byte[] tmp = new byte[(end - start)];
                this.niobuffer.position(start);
                this.niobuffer.get(tmp);
                String value = new String(tmp, 0, 0, end - start);
                this.niobuffer.put(end - 1, (byte) (this.niobuffer.get(end - 1) | 128));
                this.position = end;
                this.niobuffer.position(this.position);
                return value;
            }
        }
        return readAscii_slow();
    }

    private String readAscii_slow() {
        this.position--;
        int charCount = this.limit - this.position;
        if (charCount > this.chars.length) {
            this.chars = new char[(charCount * 2)];
        }
        char[] chars = this.chars;
        int i = this.position;
        int ii = 0;
        int n = this.limit;
        while (i < n) {
            chars[ii] = (char) this.niobuffer.get(i);
            i++;
            ii++;
        }
        this.position = this.limit;
        int charCount2 = charCount;
        while (true) {
            require(1);
            this.position++;
            int b = this.niobuffer.get();
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
        this.niobuffer.position(this.position);
        int available = require(1);
        this.position++;
        int b = this.niobuffer.get();
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
        require(4);
        this.position += 4;
        return this.niobuffer.getFloat();
    }

    public float readFloat(float precision, boolean optimizePositive) throws KryoException {
        return ((float) readInt(optimizePositive)) / precision;
    }

    public short readShort() throws KryoException {
        require(2);
        this.position += 2;
        return this.niobuffer.getShort();
    }

    public int readShortUnsigned() throws KryoException {
        require(2);
        this.position += 2;
        return this.niobuffer.getShort();
    }

    public long readLong() throws KryoException {
        require(8);
        this.position += 8;
        return this.niobuffer.getLong();
    }

    public long readLong(boolean optimizePositive) throws KryoException {
        if (this.varIntsEnabled) {
            return readVarLong(optimizePositive);
        }
        return readLong();
    }

    public long readVarLong(boolean optimizePositive) throws KryoException {
        this.niobuffer.position(this.position);
        if (require(1) < 9) {
            return readLong_slow(optimizePositive);
        }
        this.position++;
        int b = this.niobuffer.get();
        long result = (long) (b & 127);
        if ((b & 128) != 0) {
            this.position++;
            b = this.niobuffer.get();
            result |= (long) ((b & 127) << 7);
            if ((b & 128) != 0) {
                this.position++;
                b = this.niobuffer.get();
                result |= (long) ((b & 127) << 14);
                if ((b & 128) != 0) {
                    this.position++;
                    b = this.niobuffer.get();
                    result |= (long) ((b & 127) << 21);
                    if ((b & 128) != 0) {
                        this.position++;
                        b = this.niobuffer.get();
                        result |= ((long) (b & 127)) << 28;
                        if ((b & 128) != 0) {
                            this.position++;
                            b = this.niobuffer.get();
                            result |= ((long) (b & 127)) << 35;
                            if ((b & 128) != 0) {
                                this.position++;
                                b = this.niobuffer.get();
                                result |= ((long) (b & 127)) << 42;
                                if ((b & 128) != 0) {
                                    this.position++;
                                    b = this.niobuffer.get();
                                    result |= ((long) (b & 127)) << 49;
                                    if ((b & 128) != 0) {
                                        this.position++;
                                        result |= ((long) this.niobuffer.get()) << 56;
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
        this.position++;
        int b = this.niobuffer.get();
        long result = (long) (b & 127);
        if ((b & 128) != 0) {
            require(1);
            this.position++;
            b = this.niobuffer.get();
            result |= (long) ((b & 127) << 7);
            if ((b & 128) != 0) {
                require(1);
                this.position++;
                b = this.niobuffer.get();
                result |= (long) ((b & 127) << 14);
                if ((b & 128) != 0) {
                    require(1);
                    this.position++;
                    b = this.niobuffer.get();
                    result |= (long) ((b & 127) << 21);
                    if ((b & 128) != 0) {
                        require(1);
                        this.position++;
                        b = this.niobuffer.get();
                        result |= ((long) (b & 127)) << 28;
                        if ((b & 128) != 0) {
                            require(1);
                            this.position++;
                            b = this.niobuffer.get();
                            result |= ((long) (b & 127)) << 35;
                            if ((b & 128) != 0) {
                                require(1);
                                this.position++;
                                b = this.niobuffer.get();
                                result |= ((long) (b & 127)) << 42;
                                if ((b & 128) != 0) {
                                    require(1);
                                    this.position++;
                                    b = this.niobuffer.get();
                                    result |= ((long) (b & 127)) << 49;
                                    if ((b & 128) != 0) {
                                        require(1);
                                        this.position++;
                                        result |= ((long) this.niobuffer.get()) << 56;
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
        this.position++;
        if (this.niobuffer.get() == (byte) 1) {
            return true;
        }
        return false;
    }

    public char readChar() throws KryoException {
        require(2);
        this.position += 2;
        return this.niobuffer.getChar();
    }

    public double readDouble() throws KryoException {
        require(8);
        this.position += 8;
        return this.niobuffer.getDouble();
    }

    public double readDouble(double precision, boolean optimizePositive) throws KryoException {
        return ((double) readLong(optimizePositive)) / precision;
    }

    public int[] readInts(int length) throws KryoException {
        if (this.capacity - this.position < length * 4 || !isNativeOrder()) {
            return super.readInts(length);
        }
        int[] array = new int[length];
        this.niobuffer.asIntBuffer().get(array);
        this.position += length * 4;
        this.niobuffer.position(this.position);
        return array;
    }

    public long[] readLongs(int length) throws KryoException {
        if (this.capacity - this.position < length * 8 || !isNativeOrder()) {
            return super.readLongs(length);
        }
        long[] array = new long[length];
        this.niobuffer.asLongBuffer().get(array);
        this.position += length * 8;
        this.niobuffer.position(this.position);
        return array;
    }

    public float[] readFloats(int length) throws KryoException {
        if (this.capacity - this.position < length * 4 || !isNativeOrder()) {
            return super.readFloats(length);
        }
        float[] array = new float[length];
        this.niobuffer.asFloatBuffer().get(array);
        this.position += length * 4;
        this.niobuffer.position(this.position);
        return array;
    }

    public short[] readShorts(int length) throws KryoException {
        if (this.capacity - this.position < length * 2 || !isNativeOrder()) {
            return super.readShorts(length);
        }
        short[] array = new short[length];
        this.niobuffer.asShortBuffer().get(array);
        this.position += length * 2;
        this.niobuffer.position(this.position);
        return array;
    }

    public char[] readChars(int length) throws KryoException {
        if (this.capacity - this.position < length * 2 || !isNativeOrder()) {
            return super.readChars(length);
        }
        char[] array = new char[length];
        this.niobuffer.asCharBuffer().get(array);
        this.position += length * 2;
        this.niobuffer.position(this.position);
        return array;
    }

    public double[] readDoubles(int length) throws KryoException {
        if (this.capacity - this.position < length * 8 || !isNativeOrder()) {
            return super.readDoubles(length);
        }
        double[] array = new double[length];
        this.niobuffer.asDoubleBuffer().get(array);
        this.position += length * 8;
        this.niobuffer.position(this.position);
        return array;
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
