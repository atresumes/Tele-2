package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.reflectasm.FieldAccess;

class ObjectField extends CachedField {
    final FieldSerializer fieldSerializer;
    public Class[] generics;
    final Kryo kryo;
    final Class type;

    static final class ObjectBooleanField extends ObjectField {
        public ObjectBooleanField(FieldSerializer fieldSerializer) {
            super(fieldSerializer);
        }

        public Object getField(Object object) throws IllegalArgumentException, IllegalAccessException {
            return Boolean.valueOf(this.field.getBoolean(object));
        }

        public void write(Output output, Object object) {
            try {
                output.writeBoolean(this.field.getBoolean(object));
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void read(Input input, Object object) {
            try {
                this.field.setBoolean(object, input.readBoolean());
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void copy(Object original, Object copy) {
            try {
                this.field.setBoolean(copy, this.field.getBoolean(original));
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }
    }

    static final class ObjectByteField extends ObjectField {
        public ObjectByteField(FieldSerializer fieldSerializer) {
            super(fieldSerializer);
        }

        public Object getField(Object object) throws IllegalArgumentException, IllegalAccessException {
            return Byte.valueOf(this.field.getByte(object));
        }

        public void write(Output output, Object object) {
            try {
                output.writeByte(this.field.getByte(object));
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void read(Input input, Object object) {
            try {
                this.field.setByte(object, input.readByte());
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void copy(Object original, Object copy) {
            try {
                this.field.setByte(copy, this.field.getByte(original));
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }
    }

    static final class ObjectCharField extends ObjectField {
        public ObjectCharField(FieldSerializer fieldSerializer) {
            super(fieldSerializer);
        }

        public Object getField(Object object) throws IllegalArgumentException, IllegalAccessException {
            return Character.valueOf(this.field.getChar(object));
        }

        public void write(Output output, Object object) {
            try {
                output.writeChar(this.field.getChar(object));
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void read(Input input, Object object) {
            try {
                this.field.setChar(object, input.readChar());
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void copy(Object original, Object copy) {
            try {
                this.field.setChar(copy, this.field.getChar(original));
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }
    }

    static final class ObjectDoubleField extends ObjectField {
        public ObjectDoubleField(FieldSerializer fieldSerializer) {
            super(fieldSerializer);
        }

        public Object getField(Object object) throws IllegalArgumentException, IllegalAccessException {
            return Double.valueOf(this.field.getDouble(object));
        }

        public void write(Output output, Object object) {
            try {
                output.writeDouble(this.field.getDouble(object));
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void read(Input input, Object object) {
            try {
                this.field.setDouble(object, input.readDouble());
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void copy(Object original, Object copy) {
            try {
                this.field.setDouble(copy, this.field.getDouble(original));
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }
    }

    static final class ObjectFloatField extends ObjectField {
        public ObjectFloatField(FieldSerializer fieldSerializer) {
            super(fieldSerializer);
        }

        public Object getField(Object object) throws IllegalArgumentException, IllegalAccessException {
            return Float.valueOf(this.field.getFloat(object));
        }

        public void write(Output output, Object object) {
            try {
                output.writeFloat(this.field.getFloat(object));
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void read(Input input, Object object) {
            try {
                this.field.setFloat(object, input.readFloat());
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void copy(Object original, Object copy) {
            try {
                this.field.setFloat(copy, this.field.getFloat(original));
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }
    }

    static final class ObjectIntField extends ObjectField {
        public ObjectIntField(FieldSerializer fieldSerializer) {
            super(fieldSerializer);
        }

        public Object getField(Object object) throws IllegalArgumentException, IllegalAccessException {
            return Integer.valueOf(this.field.getInt(object));
        }

        public void write(Output output, Object object) {
            try {
                if (this.varIntsEnabled) {
                    output.writeInt(this.field.getInt(object), false);
                } else {
                    output.writeInt(this.field.getInt(object));
                }
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void read(Input input, Object object) {
            try {
                if (this.varIntsEnabled) {
                    this.field.setInt(object, input.readInt(false));
                } else {
                    this.field.setInt(object, input.readInt());
                }
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void copy(Object original, Object copy) {
            try {
                this.field.setInt(copy, this.field.getInt(original));
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }
    }

    static final class ObjectLongField extends ObjectField {
        public ObjectLongField(FieldSerializer fieldSerializer) {
            super(fieldSerializer);
        }

        public Object getField(Object object) throws IllegalArgumentException, IllegalAccessException {
            return Long.valueOf(this.field.getLong(object));
        }

        public void write(Output output, Object object) {
            try {
                if (this.varIntsEnabled) {
                    output.writeLong(this.field.getLong(object), false);
                } else {
                    output.writeLong(this.field.getLong(object));
                }
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void read(Input input, Object object) {
            try {
                if (this.varIntsEnabled) {
                    this.field.setLong(object, input.readLong(false));
                } else {
                    this.field.setLong(object, input.readLong());
                }
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void copy(Object original, Object copy) {
            try {
                this.field.setLong(copy, this.field.getLong(original));
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }
    }

    static final class ObjectShortField extends ObjectField {
        public ObjectShortField(FieldSerializer fieldSerializer) {
            super(fieldSerializer);
        }

        public Object getField(Object object) throws IllegalArgumentException, IllegalAccessException {
            return Short.valueOf(this.field.getShort(object));
        }

        public void write(Output output, Object object) {
            try {
                output.writeShort(this.field.getShort(object));
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void read(Input input, Object object) {
            try {
                this.field.setShort(object, input.readShort());
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }

        public void copy(Object original, Object copy) {
            try {
                this.field.setShort(copy, this.field.getShort(original));
            } catch (Throwable e) {
                KryoException ex = new KryoException(e);
                ex.addTrace(this + " (" + this.type.getName() + ")");
                throw ex;
            }
        }
    }

    ObjectField(FieldSerializer fieldSerializer) {
        this.fieldSerializer = fieldSerializer;
        this.kryo = fieldSerializer.kryo;
        this.type = fieldSerializer.type;
    }

    public Object getField(Object object) throws IllegalArgumentException, IllegalAccessException {
        return this.field.get(object);
    }

    public void setField(Object object, Object value) throws IllegalArgumentException, IllegalAccessException {
        this.field.set(object, value);
    }

    public void write(Output output, Object object) {
        KryoException ex;
        try {
            if (Log.TRACE) {
                Log.trace("kryo", "Write field: " + this + " (" + object.getClass().getName() + ") pos=" + output.position());
            }
            Object value = getField(object);
            Serializer serializer = this.serializer;
            if (this.valueClass != null) {
                if (serializer == null) {
                    serializer = this.kryo.getSerializer(this.valueClass);
                    this.serializer = serializer;
                }
                serializer.setGenerics(this.kryo, this.generics);
                if (this.canBeNull) {
                    this.kryo.writeObjectOrNull(output, value, serializer);
                } else if (value == null) {
                    throw new KryoException("Field value is null but canBeNull is false: " + this + " (" + object.getClass().getName() + ")");
                } else {
                    this.kryo.writeObject(output, value, serializer);
                }
            } else if (value == null) {
                this.kryo.writeClass(output, null);
            } else {
                Registration registration = this.kryo.writeClass(output, value.getClass());
                if (serializer == null) {
                    serializer = registration.getSerializer();
                }
                serializer.setGenerics(this.kryo, this.generics);
                this.kryo.writeObject(output, value, serializer);
            }
        } catch (IllegalAccessException ex2) {
            throw new KryoException("Error accessing field: " + this + " (" + object.getClass().getName() + ")", ex2);
        } catch (KryoException ex3) {
            ex3.addTrace(this + " (" + object.getClass().getName() + ")");
            throw ex3;
        } catch (Throwable runtimeEx) {
            ex3 = new KryoException(runtimeEx);
            ex3.addTrace(this + " (" + object.getClass().getName() + ")");
            throw ex3;
        }
    }

    public void read(Input input, Object object) {
        KryoException ex;
        try {
            Object obj;
            if (Log.TRACE) {
                Log.trace("kryo", "Read field: " + this + " (" + this.type.getName() + ") pos=" + input.position());
            }
            Class concreteType = this.valueClass;
            Serializer serializer = this.serializer;
            if (concreteType == null) {
                Registration registration = this.kryo.readClass(input);
                if (registration == null) {
                    obj = null;
                } else {
                    if (serializer == null) {
                        serializer = registration.getSerializer();
                    }
                    serializer.setGenerics(this.kryo, this.generics);
                    obj = this.kryo.readObject(input, registration.getType(), serializer);
                }
            } else {
                if (serializer == null) {
                    serializer = this.kryo.getSerializer(this.valueClass);
                    this.serializer = serializer;
                }
                serializer.setGenerics(this.kryo, this.generics);
                if (this.canBeNull) {
                    obj = this.kryo.readObjectOrNull(input, concreteType, serializer);
                } else {
                    obj = this.kryo.readObject(input, concreteType, serializer);
                }
            }
            setField(object, obj);
        } catch (IllegalAccessException ex2) {
            throw new KryoException("Error accessing field: " + this + " (" + this.type.getName() + ")", ex2);
        } catch (KryoException ex3) {
            ex3.addTrace(this + " (" + this.type.getName() + ")");
            throw ex3;
        } catch (Throwable runtimeEx) {
            ex3 = new KryoException(runtimeEx);
            ex3.addTrace(this + " (" + this.type.getName() + ")");
            throw ex3;
        }
    }

    public void copy(Object original, Object copy) {
        KryoException ex;
        try {
            if (this.accessIndex != -1) {
                FieldAccess access = this.fieldSerializer.access;
                access.set(copy, this.accessIndex, this.kryo.copy(access.get(original, this.accessIndex)));
                return;
            }
            setField(copy, this.kryo.copy(getField(original)));
        } catch (IllegalAccessException ex2) {
            throw new KryoException("Error accessing field: " + this + " (" + this.type.getName() + ")", ex2);
        } catch (KryoException ex3) {
            ex3.addTrace(this + " (" + this.type.getName() + ")");
            throw ex3;
        } catch (Throwable runtimeEx) {
            ex3 = new KryoException(runtimeEx);
            ex3.addTrace(this + " (" + this.type.getName() + ")");
            throw ex3;
        }
    }
}
