package com.esotericsoftware.reflectasm;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public abstract class MethodAccess {
    private String[] methodNames;
    private Class[][] parameterTypes;
    private Class[] returnTypes;

    public abstract Object invoke(Object obj, int i, Object... objArr);

    public Object invoke(Object object, String methodName, Class[] paramTypes, Object... args) {
        return invoke(object, getIndex(methodName, paramTypes), args);
    }

    public Object invoke(Object object, String methodName, Object... args) {
        return invoke(object, getIndex(methodName, args == null ? 0 : args.length), args);
    }

    public int getIndex(String methodName) {
        int n = this.methodNames.length;
        for (int i = 0; i < n; i++) {
            if (this.methodNames[i].equals(methodName)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unable to find non-private method: " + methodName);
    }

    public int getIndex(String methodName, Class... paramTypes) {
        int i = 0;
        int n = this.methodNames.length;
        while (i < n) {
            if (this.methodNames[i].equals(methodName) && Arrays.equals(paramTypes, this.parameterTypes[i])) {
                return i;
            }
            i++;
        }
        throw new IllegalArgumentException("Unable to find non-private method: " + methodName + " " + Arrays.toString(paramTypes));
    }

    public int getIndex(String methodName, int paramsCount) {
        int i = 0;
        int n = this.methodNames.length;
        while (i < n) {
            if (this.methodNames[i].equals(methodName) && this.parameterTypes[i].length == paramsCount) {
                return i;
            }
            i++;
        }
        throw new IllegalArgumentException("Unable to find non-private method: " + methodName + " with " + paramsCount + " params.");
    }

    public String[] getMethodNames() {
        return this.methodNames;
    }

    public Class[][] getParameterTypes() {
        return this.parameterTypes;
    }

    public Class[] getReturnTypes() {
        return this.returnTypes;
    }

    public static MethodAccess get(Class type) {
        int i;
        Class accessClass;
        ArrayList<Method> methods = new ArrayList();
        boolean isInterface = type.isInterface();
        if (isInterface) {
            recursiveAddInterfaceMethodsToList(type, methods);
        } else {
            for (Class nextClass = type; nextClass != Object.class; nextClass = nextClass.getSuperclass()) {
                addDeclaredMethodsToList(nextClass, methods);
            }
        }
        int n = methods.size();
        String[] methodNames = new String[n];
        Class[][] parameterTypes = new Class[n][];
        Class[] returnTypes = new Class[n];
        for (i = 0; i < n; i++) {
            Method method = (Method) methods.get(i);
            methodNames[i] = method.getName();
            parameterTypes[i] = method.getParameterTypes();
            returnTypes[i] = method.getReturnType();
        }
        String className = type.getName();
        String accessClassName = className + "MethodAccess";
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
                    ClassWriter cw = new ClassWriter(1);
                    cw.visit(Opcodes.V1_1, 33, accessClassNameInternal, null, "com/esotericsoftware/reflectasm/MethodAccess", null);
                    MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", null, null);
                    mv.visitCode();
                    mv.visitVarInsn(25, 0);
                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/esotericsoftware/reflectasm/MethodAccess", "<init>", "()V");
                    mv.visitInsn(Opcodes.RETURN);
                    mv.visitMaxs(0, 0);
                    mv.visitEnd();
                    mv = cw.visitMethod(Opcodes.LOR, "invoke", "(Ljava/lang/Object;I[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
                    mv.visitCode();
                    if (!methods.isEmpty()) {
                        mv.visitVarInsn(25, 1);
                        mv.visitTypeInsn(Opcodes.CHECKCAST, classNameInternal);
                        mv.visitVarInsn(58, 4);
                        mv.visitVarInsn(21, 2);
                        Label[] labels = new Label[n];
                        for (i = 0; i < n; i++) {
                            labels[i] = new Label();
                        }
                        Label defaultLabel = new Label();
                        mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);
                        StringBuilder buffer = new StringBuilder(128);
                        for (i = 0; i < n; i++) {
                            int invoke;
                            mv.visitLabel(labels[i]);
                            if (i == 0) {
                                mv.visitFrame(1, 1, new Object[]{classNameInternal}, 0, null);
                            } else {
                                mv.visitFrame(3, 0, null, 0, null);
                            }
                            mv.visitVarInsn(25, 4);
                            buffer.setLength(0);
                            buffer.append('(');
                            Class[] paramTypes = parameterTypes[i];
                            Class returnType = returnTypes[i];
                            for (int paramIndex = 0; paramIndex < paramTypes.length; paramIndex++) {
                                mv.visitVarInsn(25, 3);
                                mv.visitIntInsn(16, paramIndex);
                                mv.visitInsn(50);
                                Type paramType = Type.getType(paramTypes[paramIndex]);
                                switch (paramType.getSort()) {
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
                                        mv.visitTypeInsn(Opcodes.CHECKCAST, paramType.getDescriptor());
                                        break;
                                    case 10:
                                        mv.visitTypeInsn(Opcodes.CHECKCAST, paramType.getInternalName());
                                        break;
                                    default:
                                        break;
                                }
                                buffer.append(paramType.getDescriptor());
                            }
                            buffer.append(')');
                            buffer.append(Type.getDescriptor(returnType));
                            if (isInterface) {
                                invoke = Opcodes.INVOKEINTERFACE;
                            } else if (Modifier.isStatic(((Method) methods.get(i)).getModifiers())) {
                                invoke = Opcodes.INVOKESTATIC;
                            } else {
                                invoke = Opcodes.INVOKEVIRTUAL;
                            }
                            mv.visitMethodInsn(invoke, classNameInternal, methodNames[i], buffer.toString());
                            switch (Type.getType(returnType).getSort()) {
                                case 0:
                                    mv.visitInsn(1);
                                    break;
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
                    mv.visitTypeInsn(Opcodes.NEW, "java/lang/IllegalArgumentException");
                    mv.visitInsn(89);
                    mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
                    mv.visitInsn(89);
                    mv.visitLdcInsn("Method not found: ");
                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
                    mv.visitVarInsn(21, 2);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
                    mv.visitInsn(Opcodes.ATHROW);
                    mv.visitMaxs(0, 0);
                    mv.visitEnd();
                    cw.visitEnd();
                    accessClass = loader.defineClass(accessClassName, cw.toByteArray());
                }
            }
        }
        try {
            MethodAccess access = (MethodAccess) accessClass.newInstance();
            access.methodNames = methodNames;
            access.parameterTypes = parameterTypes;
            access.returnTypes = returnTypes;
            return access;
        } catch (Throwable t) {
            RuntimeException runtimeException = new RuntimeException("Error constructing method access class: " + accessClassName, t);
        }
    }

    private static void addDeclaredMethodsToList(Class type, ArrayList<Method> methods) {
        for (Method method : type.getDeclaredMethods()) {
            if (!Modifier.isPrivate(method.getModifiers())) {
                methods.add(method);
            }
        }
    }

    private static void recursiveAddInterfaceMethodsToList(Class interfaceType, ArrayList<Method> methods) {
        addDeclaredMethodsToList(interfaceType, methods);
        for (Class nextInterface : interfaceType.getInterfaces()) {
            recursiveAddInterfaceMethodsToList(nextInterface, methods);
        }
    }
}
