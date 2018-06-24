package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;

class AsmCacheFields {

    static abstract class AsmCachedField extends CachedField {
        AsmCachedField() {
        }
    }

    static final class AsmBooleanField extends AsmCachedField {
        AsmBooleanField() {
        }

        public void write(Output output, Object object) {
            output.writeBoolean(this.access.getBoolean(object, this.accessIndex));
        }

        public void read(Input input, Object object) {
            this.access.setBoolean(object, this.accessIndex, input.readBoolean());
        }

        public void copy(Object original, Object copy) {
            this.access.setBoolean(copy, this.accessIndex, this.access.getBoolean(original, this.accessIndex));
        }
    }

    static final class AsmByteField extends AsmCachedField {
        AsmByteField() {
        }

        public void write(Output output, Object object) {
            output.writeByte(this.access.getByte(object, this.accessIndex));
        }

        public void read(Input input, Object object) {
            this.access.setByte(object, this.accessIndex, input.readByte());
        }

        public void copy(Object original, Object copy) {
            this.access.setByte(copy, this.accessIndex, this.access.getByte(original, this.accessIndex));
        }
    }

    static final class AsmCharField extends AsmCachedField {
        AsmCharField() {
        }

        public void write(Output output, Object object) {
            output.writeChar(this.access.getChar(object, this.accessIndex));
        }

        public void read(Input input, Object object) {
            this.access.setChar(object, this.accessIndex, input.readChar());
        }

        public void copy(Object original, Object copy) {
            this.access.setChar(copy, this.accessIndex, this.access.getChar(original, this.accessIndex));
        }
    }

    static final class AsmDoubleField extends AsmCachedField {
        AsmDoubleField() {
        }

        public void write(Output output, Object object) {
            output.writeDouble(this.access.getDouble(object, this.accessIndex));
        }

        public void read(Input input, Object object) {
            this.access.setDouble(object, this.accessIndex, input.readDouble());
        }

        public void copy(Object original, Object copy) {
            this.access.setDouble(copy, this.accessIndex, this.access.getDouble(original, this.accessIndex));
        }
    }

    static final class AsmFloatField extends AsmCachedField {
        AsmFloatField() {
        }

        public void write(Output output, Object object) {
            output.writeFloat(this.access.getFloat(object, this.accessIndex));
        }

        public void read(Input input, Object object) {
            this.access.setFloat(object, this.accessIndex, input.readFloat());
        }

        public void copy(Object original, Object copy) {
            this.access.setFloat(copy, this.accessIndex, this.access.getFloat(original, this.accessIndex));
        }
    }

    static final class AsmIntField extends AsmCachedField {
        AsmIntField() {
        }

        public void write(Output output, Object object) {
            if (this.varIntsEnabled) {
                output.writeInt(this.access.getInt(object, this.accessIndex), false);
            } else {
                output.writeInt(this.access.getInt(object, this.accessIndex));
            }
        }

        public void read(Input input, Object object) {
            if (this.varIntsEnabled) {
                this.access.setInt(object, this.accessIndex, input.readInt(false));
            } else {
                this.access.setInt(object, this.accessIndex, input.readInt());
            }
        }

        public void copy(Object original, Object copy) {
            this.access.setInt(copy, this.accessIndex, this.access.getInt(original, this.accessIndex));
        }
    }

    static final class AsmLongField extends AsmCachedField {
        AsmLongField() {
        }

        public void write(Output output, Object object) {
            if (this.varIntsEnabled) {
                output.writeLong(this.access.getLong(object, this.accessIndex), false);
            } else {
                output.writeLong(this.access.getLong(object, this.accessIndex));
            }
        }

        public void read(Input input, Object object) {
            if (this.varIntsEnabled) {
                this.access.setLong(object, this.accessIndex, input.readLong(false));
            } else {
                this.access.setLong(object, this.accessIndex, input.readLong());
            }
        }

        public void copy(Object original, Object copy) {
            this.access.setLong(copy, this.accessIndex, this.access.getLong(original, this.accessIndex));
        }
    }

    static final class AsmObjectField extends ObjectField {
        public AsmObjectField(FieldSerializer fieldSerializer) {
            super(fieldSerializer);
        }

        public Object getField(Object object) throws IllegalArgumentException, IllegalAccessException {
            if (this.accessIndex != -1) {
                return this.access.get(object, this.accessIndex);
            }
            throw new KryoException("Unknown acess index");
        }

        public void setField(Object object, Object value) throws IllegalArgumentException, IllegalAccessException {
            if (this.accessIndex != -1) {
                this.access.set(object, this.accessIndex, value);
                return;
            }
            throw new KryoException("Unknown acess index");
        }

        public void copy(Object original, Object copy) {
            KryoException ex;
            try {
                if (this.accessIndex != -1) {
                    this.access.set(copy, this.accessIndex, this.kryo.copy(this.access.get(original, this.accessIndex)));
                    return;
                }
                throw new KryoException("Unknown acess index");
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

    static final class AsmShortField extends AsmCachedField {
        AsmShortField() {
        }

        public void write(Output output, Object object) {
            output.writeShort(this.access.getShort(object, this.accessIndex));
        }

        public void read(Input input, Object object) {
            this.access.setShort(object, this.accessIndex, input.readShort());
        }

        public void copy(Object original, Object copy) {
            this.access.setShort(copy, this.accessIndex, this.access.getShort(original, this.accessIndex));
        }
    }

    static final class AsmStringField extends AsmCachedField {
        AsmStringField() {
        }

        public void write(Output output, Object object) {
            output.writeString(this.access.getString(object, this.accessIndex));
        }

        public void read(Input input, Object object) {
            this.access.set(object, this.accessIndex, input.readString());
        }

        public void copy(Object original, Object copy) {
            this.access.set(copy, this.accessIndex, this.access.getString(original, this.accessIndex));
        }
    }

    AsmCacheFields() {
    }
}
