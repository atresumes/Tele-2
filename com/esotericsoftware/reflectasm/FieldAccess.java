package com.esotericsoftware.reflectasm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public abstract class FieldAccess {
    private String[] fieldNames;
    private Class[] fieldTypes;

    public abstract Object get(Object obj, int i);

    public abstract boolean getBoolean(Object obj, int i);

    public abstract byte getByte(Object obj, int i);

    public abstract char getChar(Object obj, int i);

    public abstract double getDouble(Object obj, int i);

    public abstract float getFloat(Object obj, int i);

    public abstract int getInt(Object obj, int i);

    public abstract long getLong(Object obj, int i);

    public abstract short getShort(Object obj, int i);

    public abstract String getString(Object obj, int i);

    public abstract void set(Object obj, int i, Object obj2);

    public abstract void setBoolean(Object obj, int i, boolean z);

    public abstract void setByte(Object obj, int i, byte b);

    public abstract void setChar(Object obj, int i, char c);

    public abstract void setDouble(Object obj, int i, double d);

    public abstract void setFloat(Object obj, int i, float f);

    public abstract void setInt(Object obj, int i, int i2);

    public abstract void setLong(Object obj, int i, long j);

    public abstract void setShort(Object obj, int i, short s);

    public int getIndex(String fieldName) {
        int n = this.fieldNames.length;
        for (int i = 0; i < n; i++) {
            if (this.fieldNames[i].equals(fieldName)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unable to find non-private field: " + fieldName);
    }

    public void set(Object instance, String fieldName, Object value) {
        set(instance, getIndex(fieldName), value);
    }

    public Object get(Object instance, String fieldName) {
        return get(instance, getIndex(fieldName));
    }

    public String[] getFieldNames() {
        return this.fieldNames;
    }

    public Class[] getFieldTypes() {
        return this.fieldTypes;
    }

    public int getFieldCount() {
        return this.fieldTypes.length;
    }

    public static FieldAccess get(Class type) {
        int i;
        Class accessClass;
        ArrayList<Field> fields = new ArrayList();
        for (Class nextClass = type; nextClass != Object.class; nextClass = nextClass.getSuperclass()) {
            for (Field field : nextClass.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (!(Modifier.isStatic(modifiers) || Modifier.isPrivate(modifiers))) {
                    fields.add(field);
                }
            }
        }
        String[] fieldNames = new String[fields.size()];
        Class[] fieldTypes = new Class[fields.size()];
        int n = fieldNames.length;
        for (i = 0; i < n; i++) {
            fieldNames[i] = ((Field) fields.get(i)).getName();
            fieldTypes[i] = ((Field) fields.get(i)).getType();
        }
        String className = type.getName();
        String accessClassName = className + "FieldAccess";
        if (accessClassName.startsWith("java.")) {
            accessClassName = "reflectasm." + accessClassName;
        }
        AccessClassLoader loader = AccessClassLoader.get(type);
        try {
            accessClass = loader.loadClass(accessClassName);
        } catch (ClassNotFoundException e) {
            synchronized (loader) {
                try {
                    accessClass = loader.loadClass(accessClassName);
                } catch (ClassNotFoundException e2) {
                    String accessClassNameInternal = accessClassName.replace('.', '/');
                    String classNameInternal = className.replace('.', '/');
                    ClassWriter cw = new ClassWriter(0);
                    cw.visit(Opcodes.V1_1, 33, accessClassNameInternal, null, "com/esotericsoftware/reflectasm/FieldAccess", null);
                    insertConstructor(cw);
                    insertGetObject(cw, classNameInternal, fields);
                    insertSetObject(cw, classNameInternal, fields);
                    insertGetPrimitive(cw, classNameInternal, fields, Type.BOOLEAN_TYPE);
                    insertSetPrimitive(cw, classNameInternal, fields, Type.BOOLEAN_TYPE);
                    insertGetPrimitive(cw, classNameInternal, fields, Type.BYTE_TYPE);
                    insertSetPrimitive(cw, classNameInternal, fields, Type.BYTE_TYPE);
                    insertGetPrimitive(cw, classNameInternal, fields, Type.SHORT_TYPE);
                    insertSetPrimitive(cw, classNameInternal, fields, Type.SHORT_TYPE);
                    insertGetPrimitive(cw, classNameInternal, fields, Type.INT_TYPE);
                    insertSetPrimitive(cw, classNameInternal, fields, Type.INT_TYPE);
                    insertGetPrimitive(cw, classNameInternal, fields, Type.LONG_TYPE);
                    insertSetPrimitive(cw, classNameInternal, fields, Type.LONG_TYPE);
                    insertGetPrimitive(cw, classNameInternal, fields, Type.DOUBLE_TYPE);
                    insertSetPrimitive(cw, classNameInternal, fields, Type.DOUBLE_TYPE);
                    insertGetPrimitive(cw, classNameInternal, fields, Type.FLOAT_TYPE);
                    insertSetPrimitive(cw, classNameInternal, fields, Type.FLOAT_TYPE);
                    insertGetPrimitive(cw, classNameInternal, fields, Type.CHAR_TYPE);
                    insertSetPrimitive(cw, classNameInternal, fields, Type.CHAR_TYPE);
                    insertGetString(cw, classNameInternal, fields);
                    cw.visitEnd();
                    accessClass = loader.defineClass(accessClassName, cw.toByteArray());
                }
            }
        }
        try {
            FieldAccess access = (FieldAccess) accessClass.newInstance();
            access.fieldNames = fieldNames;
            access.fieldTypes = fieldTypes;
            return access;
        } catch (Throwable t) {
            RuntimeException runtimeException = new RuntimeException("Error constructing field access class: " + accessClassName, t);
        }
    }

    private static void insertConstructor(ClassWriter cw) {
        MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/esotericsoftware/reflectasm/FieldAccess", "<init>", "()V");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    private static void insertSetObject(ClassWriter cw, String classNameInternal, ArrayList<Field> fields) {
        int maxStack = 6;
        MethodVisitor mv = cw.visitMethod(1, "set", "(Ljava/lang/Object;ILjava/lang/Object;)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(21, 2);
        if (!fields.isEmpty()) {
            int i;
            maxStack = 6 - 1;
            Label[] labels = new Label[fields.size()];
            int n = labels.length;
            for (i = 0; i < n; i++) {
                labels[i] = new Label();
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);
            n = labels.length;
            for (i = 0; i < n; i++) {
                Field field = (Field) fields.get(i);
                Type fieldType = Type.getType(field.getType());
                mv.visitLabel(labels[i]);
                mv.visitFrame(3, 0, null, 0, null);
                mv.visitVarInsn(25, 1);
                mv.visitTypeInsn(Opcodes.CHECKCAST, classNameInternal);
                mv.visitVarInsn(25, 3);
                switch (fieldType.getSort()) {
                    case 1:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Boolean");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z");
                        break;
                    case 2:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Character");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C");
                        break;
                    case 3:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Byte");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B");
                        break;
                    case 4:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Short");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S");
                        break;
                    case 5:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Integer");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");
                        break;
                    case 6:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Float");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F");
                        break;
                    case 7:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Long");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J");
                        break;
                    case 8:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Double");
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D");
                        break;
                    case 9:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, fieldType.getDescriptor());
                        break;
                    case 10:
                        mv.visitTypeInsn(Opcodes.CHECKCAST, fieldType.getInternalName());
                        break;
                    default:
                        break;
                }
                mv.visitFieldInsn(Opcodes.PUTFIELD, classNameInternal, field.getName(), fieldType.getDescriptor());
                mv.visitInsn(Opcodes.RETURN);
            }
            mv.visitLabel(defaultLabel);
            mv.visitFrame(3, 0, null, 0, null);
        }
        mv = insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 4);
        mv.visitEnd();
    }

    private static void insertGetObject(ClassWriter cw, String classNameInternal, ArrayList<Field> fields) {
        int maxStack = 6;
        MethodVisitor mv = cw.visitMethod(1, "get", "(Ljava/lang/Object;I)Ljava/lang/Object;", null, null);
        mv.visitCode();
        mv.visitVarInsn(21, 2);
        if (!fields.isEmpty()) {
            int i;
            maxStack = 6 - 1;
            Label[] labels = new Label[fields.size()];
            int n = labels.length;
            for (i = 0; i < n; i++) {
                labels[i] = new Label();
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);
            n = labels.length;
            for (i = 0; i < n; i++) {
                Field field = (Field) fields.get(i);
                mv.visitLabel(labels[i]);
                mv.visitFrame(3, 0, null, 0, null);
                mv.visitVarInsn(25, 1);
                mv.visitTypeInsn(Opcodes.CHECKCAST, classNameInternal);
                mv.visitFieldInsn(Opcodes.GETFIELD, classNameInternal, field.getName(), Type.getDescriptor(field.getType()));
                switch (Type.getType(field.getType()).getSort()) {
                    case 1:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
                        break;
                    case 2:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
                        break;
                    case 3:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
                        break;
                    case 4:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
                        break;
                    case 5:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
                        break;
                    case 6:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
                        break;
                    case 7:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
                        break;
                    case 8:
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
                        break;
                    default:
                        break;
                }
                mv.visitInsn(Opcodes.ARETURN);
            }
            mv.visitLabel(defaultLabel);
            mv.visitFrame(3, 0, null, 0, null);
        }
        insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 3);
        mv.visitEnd();
    }

    private static void insertGetString(ClassWriter cw, String classNameInternal, ArrayList<Field> fields) {
        int maxStack = 6;
        MethodVisitor mv = cw.visitMethod(1, "getString", "(Ljava/lang/Object;I)Ljava/lang/String;", null, null);
        mv.visitCode();
        mv.visitVarInsn(21, 2);
        if (!fields.isEmpty()) {
            int i;
            maxStack = 6 - 1;
            Label[] labels = new Label[fields.size()];
            Label labelForInvalidTypes = new Label();
            boolean hasAnyBadTypeLabel = false;
            int n = labels.length;
            for (i = 0; i < n; i++) {
                if (((Field) fields.get(i)).getType().equals(String.class)) {
                    labels[i] = new Label();
                } else {
                    labels[i] = labelForInvalidTypes;
                    hasAnyBadTypeLabel = true;
                }
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);
            n = labels.length;
            for (i = 0; i < n; i++) {
                if (!labels[i].equals(labelForInvalidTypes)) {
                    mv.visitLabel(labels[i]);
                    mv.visitFrame(3, 0, null, 0, null);
                    mv.visitVarInsn(25, 1);
                    mv.visitTypeInsn(Opcodes.CHECKCAST, classNameInternal);
                    mv.visitFieldInsn(Opcodes.GETFIELD, classNameInternal, ((Field) fields.get(i)).getName(), "Ljava/lang/String;");
                    mv.visitInsn(Opcodes.ARETURN);
                }
            }
            if (hasAnyBadTypeLabel) {
                mv.visitLabel(labelForInvalidTypes);
                mv.visitFrame(3, 0, null, 0, null);
                insertThrowExceptionForFieldType(mv, "String");
            }
            mv.visitLabel(defaultLabel);
            mv.visitFrame(3, 0, null, 0, null);
        }
        insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 3);
        mv.visitEnd();
    }

    private static void insertSetPrimitive(ClassWriter cw, String classNameInternal, ArrayList<Field> fields, Type primitiveType) {
        String setterMethodName;
        int loadValueInstruction;
        int maxStack = 6;
        int maxLocals = 4;
        String typeNameInternal = primitiveType.getDescriptor();
        switch (primitiveType.getSort()) {
            case 1:
                setterMethodName = "setBoolean";
                loadValueInstruction = 21;
                break;
            case 2:
                setterMethodName = "setChar";
                loadValueInstruction = 21;
                break;
            case 3:
                setterMethodName = "setByte";
                loadValueInstruction = 21;
                break;
            case 4:
                setterMethodName = "setShort";
                loadValueInstruction = 21;
                break;
            case 5:
                setterMethodName = "setInt";
                loadValueInstruction = 21;
                break;
            case 6:
                setterMethodName = "setFloat";
                loadValueInstruction = 23;
                break;
            case 7:
                setterMethodName = "setLong";
                loadValueInstruction = 22;
                maxLocals = 4 + 1;
                break;
            case 8:
                setterMethodName = "setDouble";
                loadValueInstruction = 24;
                maxLocals = 4 + 1;
                break;
            default:
                setterMethodName = "set";
                loadValueInstruction = 25;
                break;
        }
        MethodVisitor mv = cw.visitMethod(1, setterMethodName, "(Ljava/lang/Object;I" + typeNameInternal + ")V", null, null);
        mv.visitCode();
        mv.visitVarInsn(21, 2);
        if (!fields.isEmpty()) {
            int i;
            maxStack = 6 - 1;
            Label[] labels = new Label[fields.size()];
            Label labelForInvalidTypes = new Label();
            boolean hasAnyBadTypeLabel = false;
            int n = labels.length;
            for (i = 0; i < n; i++) {
                if (Type.getType(((Field) fields.get(i)).getType()).equals(primitiveType)) {
                    labels[i] = new Label();
                } else {
                    labels[i] = labelForInvalidTypes;
                    hasAnyBadTypeLabel = true;
                }
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);
            n = labels.length;
            for (i = 0; i < n; i++) {
                if (!labels[i].equals(labelForInvalidTypes)) {
                    mv.visitLabel(labels[i]);
                    mv.visitFrame(3, 0, null, 0, null);
                    mv.visitVarInsn(25, 1);
                    mv.visitTypeInsn(Opcodes.CHECKCAST, classNameInternal);
                    mv.visitVarInsn(loadValueInstruction, 3);
                    mv.visitFieldInsn(Opcodes.PUTFIELD, classNameInternal, ((Field) fields.get(i)).getName(), typeNameInternal);
                    mv.visitInsn(Opcodes.RETURN);
                }
            }
            if (hasAnyBadTypeLabel) {
                mv.visitLabel(labelForInvalidTypes);
                mv.visitFrame(3, 0, null, 0, null);
                insertThrowExceptionForFieldType(mv, primitiveType.getClassName());
            }
            mv.visitLabel(defaultLabel);
            mv.visitFrame(3, 0, null, 0, null);
        }
        mv = insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, maxLocals);
        mv.visitEnd();
    }

    private static void insertGetPrimitive(ClassWriter cw, String classNameInternal, ArrayList<Field> fields, Type primitiveType) {
        String getterMethodName;
        int returnValueInstruction;
        int maxStack = 6;
        String typeNameInternal = primitiveType.getDescriptor();
        switch (primitiveType.getSort()) {
            case 1:
                getterMethodName = "getBoolean";
                returnValueInstruction = Opcodes.IRETURN;
                break;
            case 2:
                getterMethodName = "getChar";
                returnValueInstruction = Opcodes.IRETURN;
                break;
            case 3:
                getterMethodName = "getByte";
                returnValueInstruction = Opcodes.IRETURN;
                break;
            case 4:
                getterMethodName = "getShort";
                returnValueInstruction = Opcodes.IRETURN;
                break;
            case 5:
                getterMethodName = "getInt";
                returnValueInstruction = Opcodes.IRETURN;
                break;
            case 6:
                getterMethodName = "getFloat";
                returnValueInstruction = Opcodes.FRETURN;
                break;
            case 7:
                getterMethodName = "getLong";
                returnValueInstruction = Opcodes.LRETURN;
                break;
            case 8:
                getterMethodName = "getDouble";
                returnValueInstruction = Opcodes.DRETURN;
                break;
            default:
                getterMethodName = "get";
                returnValueInstruction = Opcodes.ARETURN;
                break;
        }
        MethodVisitor mv = cw.visitMethod(1, getterMethodName, "(Ljava/lang/Object;I)" + typeNameInternal, null, null);
        mv.visitCode();
        mv.visitVarInsn(21, 2);
        if (!fields.isEmpty()) {
            int i;
            maxStack = 6 - 1;
            Label[] labels = new Label[fields.size()];
            Label labelForInvalidTypes = new Label();
            boolean hasAnyBadTypeLabel = false;
            int n = labels.length;
            for (i = 0; i < n; i++) {
                if (Type.getType(((Field) fields.get(i)).getType()).equals(primitiveType)) {
                    labels[i] = new Label();
                } else {
                    labels[i] = labelForInvalidTypes;
                    hasAnyBadTypeLabel = true;
                }
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);
            n = labels.length;
            for (i = 0; i < n; i++) {
                Field field = (Field) fields.get(i);
                if (!labels[i].equals(labelForInvalidTypes)) {
                    mv.visitLabel(labels[i]);
                    mv.visitFrame(3, 0, null, 0, null);
                    mv.visitVarInsn(25, 1);
                    mv.visitTypeInsn(Opcodes.CHECKCAST, classNameInternal);
                    mv.visitFieldInsn(Opcodes.GETFIELD, classNameInternal, field.getName(), typeNameInternal);
                    mv.visitInsn(returnValueInstruction);
                }
            }
            if (hasAnyBadTypeLabel) {
                mv.visitLabel(labelForInvalidTypes);
                mv.visitFrame(3, 0, null, 0, null);
                insertThrowExceptionForFieldType(mv, primitiveType.getClassName());
            }
            mv.visitLabel(defaultLabel);
            mv.visitFrame(3, 0, null, 0, null);
        }
        mv = insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 3);
        mv.visitEnd();
    }

    private static MethodVisitor insertThrowExceptionForFieldNotFound(MethodVisitor mv) {
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(89);
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        mv.visitInsn(89);
        mv.visitLdcInsn("Field not found: ");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
        mv.visitVarInsn(21, 2);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
        mv.visitInsn(Opcodes.ATHROW);
        return mv;
    }

    private static MethodVisitor insertThrowExceptionForFieldType(MethodVisitor mv, String fieldType) {
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(89);
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        mv.visitInsn(89);
        mv.visitLdcInsn("Field not declared as " + fieldType + ": ");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
        mv.visitVarInsn(21, 2);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
        mv.visitInsn(Opcodes.ATHROW);
        return mv;
    }
}
