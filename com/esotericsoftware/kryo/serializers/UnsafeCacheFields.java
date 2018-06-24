package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.UnsafeMemoryOutput;
import com.esotericsoftware.kryo.io.UnsafeOutput;
import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import com.esotericsoftware.kryo.util.UnsafeUtil;
import java.lang.reflect.Field;
import sun.misc.Unsafe;

class UnsafeCacheFields {

    static abstract class UnsafeCachedField extends CachedField {
        UnsafeCachedField(long offset) {
            this.offset = offset;
        }
    }

    static final class UnsafeBooleanField extends UnsafeCachedField {
        public UnsafeBooleanField(Field f) {
            super(UnsafeUtil.unsafe().objectFieldOffset(f));
        }

        public void write(Output output, Object object) {
            output.writeBoolean(UnsafeUtil.unsafe().getBoolean(object, this.offset));
        }

        public void read(Input input, Object object) {
            UnsafeUtil.unsafe().putBoolean(object, this.offset, input.readBoolean());
        }

        public void copy(Object original, Object copy) {
            UnsafeUtil.unsafe().putBoolean(copy, this.offset, UnsafeUtil.unsafe().getBoolean(original, this.offset));
        }
    }

    static final class UnsafeByteField extends UnsafeCachedField {
        public UnsafeByteField(Field f) {
            super(UnsafeUtil.unsafe().objectFieldOffset(f));
        }

        public void write(Output output, Object object) {
            output.writeByte(UnsafeUtil.unsafe().getByte(object, this.offset));
        }

        public void read(Input input, Object object) {
            UnsafeUtil.unsafe().putByte(object, this.offset, input.readByte());
        }

        public void copy(Object original, Object copy) {
            UnsafeUtil.unsafe().putByte(copy, this.offset, UnsafeUtil.unsafe().getByte(original, this.offset));
        }
    }

    static final class UnsafeCharField extends UnsafeCachedField {
        public UnsafeCharField(Field f) {
            super(UnsafeUtil.unsafe().objectFieldOffset(f));
        }

        public void write(Output output, Object object) {
            output.writeChar(UnsafeUtil.unsafe().getChar(object, this.offset));
        }

        public void read(Input input, Object object) {
            UnsafeUtil.unsafe().putChar(object, this.offset, input.readChar());
        }

        public void copy(Object original, Object copy) {
            UnsafeUtil.unsafe().putChar(copy, this.offset, UnsafeUtil.unsafe().getChar(original, this.offset));
        }
    }

    static final class UnsafeDoubleField extends UnsafeCachedField {
        public UnsafeDoubleField(Field f) {
            super(UnsafeUtil.unsafe().objectFieldOffset(f));
        }

        public void write(Output output, Object object) {
            output.writeDouble(UnsafeUtil.unsafe().getDouble(object, this.offset));
        }

        public void read(Input input, Object object) {
            UnsafeUtil.unsafe().putDouble(object, this.offset, input.readDouble());
        }

        public void copy(Object original, Object copy) {
            UnsafeUtil.unsafe().putDouble(copy, this.offset, UnsafeUtil.unsafe().getDouble(original, this.offset));
        }
    }

    static final class UnsafeFloatField extends UnsafeCachedField {
        public UnsafeFloatField(Field f) {
            super(UnsafeUtil.unsafe().objectFieldOffset(f));
        }

        public void write(Output output, Object object) {
            output.writeFloat(UnsafeUtil.unsafe().getFloat(object, this.offset));
        }

        public void read(Input input, Object object) {
            UnsafeUtil.unsafe().putFloat(object, this.offset, input.readFloat());
        }

        public void copy(Object original, Object copy) {
            UnsafeUtil.unsafe().putFloat(copy, this.offset, UnsafeUtil.unsafe().getFloat(original, this.offset));
        }
    }

    static final class UnsafeIntField extends UnsafeCachedField {
        public UnsafeIntField(Field f) {
            super(UnsafeUtil.unsafe().objectFieldOffset(f));
        }

        public void write(Output output, Object object) {
            if (this.varIntsEnabled) {
                output.writeInt(UnsafeUtil.unsafe().getInt(object, this.offset), false);
            } else {
                output.writeInt(UnsafeUtil.unsafe().getInt(object, this.offset));
            }
        }

        public void read(Input input, Object object) {
            if (this.varIntsEnabled) {
                UnsafeUtil.unsafe().putInt(object, this.offset, input.readInt(false));
            } else {
                UnsafeUtil.unsafe().putInt(object, this.offset, input.readInt());
            }
        }

        public void copy(Object original, Object copy) {
            UnsafeUtil.unsafe().putInt(copy, this.offset, UnsafeUtil.unsafe().getInt(original, this.offset));
        }
    }

    static final class UnsafeLongField extends UnsafeCachedField {
        public UnsafeLongField(Field f) {
            super(UnsafeUtil.unsafe().objectFieldOffset(f));
        }

        public void write(Output output, Object object) {
            if (this.varIntsEnabled) {
                output.writeLong(UnsafeUtil.unsafe().getLong(object, this.offset), false);
            } else {
                output.writeLong(UnsafeUtil.unsafe().getLong(object, this.offset));
            }
        }

        public void read(Input input, Object object) {
            if (this.varIntsEnabled) {
                UnsafeUtil.unsafe().putLong(object, this.offset, input.readLong(false));
                return;
            }
            UnsafeUtil.unsafe().putLong(object, this.offset, input.readLong());
        }

        public void copy(Object original, Object copy) {
            UnsafeUtil.unsafe().putLong(copy, this.offset, UnsafeUtil.unsafe().getLong(original, this.offset));
        }
    }

    static final class UnsafeObjectField extends ObjectField {
        public UnsafeObjectField(FieldSerializer fieldSerializer) {
            super(fieldSerializer);
        }

        public Object getField(Object object) throws IllegalArgumentException, IllegalAccessException {
            if (this.offset >= 0) {
                return UnsafeUtil.unsafe().getObject(object, this.offset);
            }
            throw new KryoException("Unknown offset");
        }

        public void setField(Object object, Object value) throws IllegalArgumentException, IllegalAccessException {
            if (this.offset != -1) {
                UnsafeUtil.unsafe().putObject(object, this.offset, value);
                return;
            }
            throw new KryoException("Unknown offset");
        }

        public void copy(Object original, Object copy) {
            KryoException ex;
            try {
                if (this.offset != -1) {
                    UnsafeUtil.unsafe().putObject(copy, this.offset, this.kryo.copy(UnsafeUtil.unsafe().getObject(original, this.offset)));
                    return;
                }
                throw new KryoException("Unknown offset");
            } catch (KryoException ex2) {
                ex2.addTrace(this + " (" + this.type.getName() + ")");
                throw ex2;
            } catch (Throwable runtimeEx) {
                ex2 = new KryoException(runtimeEx);
                ex2.addTrace(this + " (" + this.type.getName() + ")");
                throw ex2;
            }
        }
    }

    static final class UnsafeRegionField extends UnsafeCachedField {
        static final boolean bulkReadsSupported = false;
        final long len;

        public UnsafeRegionField(long offset, long len) {
            super(offset);
            this.len = len;
        }

        public final void write(Output output, Object object) {
            if (output instanceof UnsafeOutput) {
                ((UnsafeOutput) output).writeBytes(object, this.offset, this.len);
            } else if (output instanceof UnsafeMemoryOutput) {
                ((UnsafeMemoryOutput) output).writeBytes(object, this.offset, this.len);
            } else {
                Unsafe unsafe = UnsafeUtil.unsafe();
                long off = this.offset;
                while (off < (this.offset + this.len) - 8) {
                    output.writeLong(unsafe.getLong(object, off));
                    off += 8;
                }
                if (off < this.offset + this.len) {
                    while (off < this.offset + this.len) {
                        output.write(unsafe.getByte(object, off));
                        off++;
                    }
                }
            }
        }

        public final void read(Input input, Object object) {
            readSlow(input, object);
        }

        private void readSlow(Input input, Object object) {
            Unsafe unsafe = UnsafeUtil.unsafe();
            long off = this.offset;
            while (off < (this.offset + this.len) - 8) {
                unsafe.putLong(object, off, input.readLong());
                off += 8;
            }
            if (off < this.offset + this.len) {
                while (off < this.offset + this.len) {
                    unsafe.putByte(object, off, input.readByte());
                    off++;
                }
            }
        }

        public void copy(Object original, Object copy) {
            UnsafeUtil.unsafe().copyMemory(original, this.offset, copy, this.offset, this.len);
        }
    }

    static final class UnsafeShortField extends UnsafeCachedField {
        public UnsafeShortField(Field f) {
            super(UnsafeUtil.unsafe().objectFieldOffset(f));
        }

        public void write(Output output, Object object) {
            output.writeShort(UnsafeUtil.unsafe().getShort(object, this.offset));
        }

        public void read(Input input, Object object) {
            UnsafeUtil.unsafe().putShort(object, this.offset, input.readShort());
        }

        public void copy(Object original, Object copy) {
            UnsafeUtil.unsafe().putShort(copy, this.offset, UnsafeUtil.unsafe().getShort(original, this.offset));
        }
    }

    static final class UnsafeStringField extends UnsafeCachedField {
        public UnsafeStringField(Field f) {
            super(UnsafeUtil.unsafe().objectFieldOffset(f));
        }

        public void write(Output output, Object object) {
            output.writeString((String) UnsafeUtil.unsafe().getObject(object, this.offset));
        }

        public void read(Input input, Object object) {
            UnsafeUtil.unsafe().putObject(object, this.offset, input.readString());
        }

        public void copy(Object original, Object copy) {
            UnsafeUtil.unsafe().putObject(copy, this.offset, UnsafeUtil.unsafe().getObject(original, this.offset));
        }
    }

    UnsafeCacheFields() {
    }
}
