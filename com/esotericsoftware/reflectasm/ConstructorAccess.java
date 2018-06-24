package com.esotericsoftware.reflectasm;

import java.lang.reflect.Modifier;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public abstract class ConstructorAccess<T> {
    boolean isNonStaticMemberClass;

    public abstract T newInstance();

    public abstract T newInstance(Object obj);

    public boolean isNonStaticMemberClass() {
        return this.isNonStaticMemberClass;
    }

    public static <T> ConstructorAccess<T> get(Class<T> type) {
        Class accessClass;
        int modifiers;
        Class enclosingType = type.getEnclosingClass();
        boolean isNonStaticMemberClass = (enclosingType == null || !type.isMemberClass() || Modifier.isStatic(type.getModifiers())) ? false : true;
        String className = type.getName();
        String accessClassName = className + "ConstructorAccess";
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
                } catch (Throwable ex) {
                    throw new RuntimeException("Non-static member class cannot be created (missing enclosing class constructor): " + type.getName(), ex);
                } catch (Throwable ex2) {
                    throw new RuntimeException("Class cannot be created (missing no-arg constructor): " + type.getName(), ex2);
                } catch (ClassNotFoundException e2) {
                    String enclosingClassNameInternal;
                    String accessClassNameInternal = accessClassName.replace('.', '/');
                    String classNameInternal = className.replace('.', '/');
                    if (isNonStaticMemberClass) {
                        enclosingClassNameInternal = enclosingType.getName().replace('.', '/');
                        modifiers = type.getDeclaredConstructor(new Class[]{enclosingType}).getModifiers();
                        if (Modifier.isPrivate(modifiers)) {
                            throw new RuntimeException("Non-static member class cannot be created (the enclosing class constructor is private): " + type.getName());
                        }
                    }
                    enclosingClassNameInternal = null;
                    modifiers = type.getDeclaredConstructor((Class[]) null).getModifiers();
                    if (Modifier.isPrivate(modifiers)) {
                        throw new RuntimeException("Class cannot be created (the no-arg constructor is private): " + type.getName());
                    }
                    String superclassNameInternal = Modifier.isPublic(modifiers) ? "com/esotericsoftware/reflectasm/PublicConstructorAccess" : "com/esotericsoftware/reflectasm/ConstructorAccess";
                    ClassWriter cw = new ClassWriter(0);
                    cw.visit(Opcodes.V1_1, 33, accessClassNameInternal, null, superclassNameInternal, null);
                    insertConstructor(cw, superclassNameInternal);
                    insertNewInstance(cw, classNameInternal);
                    insertNewInstanceInner(cw, classNameInternal, enclosingClassNameInternal);
                    cw.visitEnd();
                    accessClass = loader.defineClass(accessClassName, cw.toByteArray());
                }
            }
        }
        try {
            ConstructorAccess<T> access = (ConstructorAccess) accessClass.newInstance();
            if ((access instanceof PublicConstructorAccess) || AccessClassLoader.areInSameRuntimeClassLoader(type, accessClass)) {
                access.isNonStaticMemberClass = isNonStaticMemberClass;
                return access;
            }
            throw new RuntimeException((!isNonStaticMemberClass ? "Class cannot be created (the no-arg constructor is protected or package-protected, and its ConstructorAccess could not be defined in the same class loader): " : "Non-static member class cannot be created (the enclosing class constructor is protected or package-protected, and its ConstructorAccess could not be defined in the same class loader): ") + type.getName());
        } catch (Throwable t) {
            RuntimeException runtimeException = new RuntimeException("Exception constructing constructor access class: " + accessClassName, t);
        }
    }

    private static void insertConstructor(ClassWriter cw, String superclassNameInternal) {
        MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, superclassNameInternal, "<init>", "()V");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    static void insertNewInstance(ClassWriter cw, String classNameInternal) {
        MethodVisitor mv = cw.visitMethod(1, "newInstance", "()Ljava/lang/Object;", null, null);
        mv.visitCode();
        mv.visitTypeInsn(Opcodes.NEW, classNameInternal);
        mv.visitInsn(89);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, classNameInternal, "<init>", "()V");
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
    }

    static void insertNewInstanceInner(ClassWriter cw, String classNameInternal, String enclosingClassNameInternal) {
        MethodVisitor mv = cw.visitMethod(1, "newInstance", "(Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        mv.visitCode();
        if (enclosingClassNameInternal != null) {
            mv.visitTypeInsn(Opcodes.NEW, classNameInternal);
            mv.visitInsn(89);
            mv.visitVarInsn(25, 1);
            mv.visitTypeInsn(Opcodes.CHECKCAST, enclosingClassNameInternal);
            mv.visitInsn(89);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
            mv.visitInsn(87);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, classNameInternal, "<init>", "(L" + enclosingClassNameInternal + ";)V");
            mv.visitInsn(Opcodes.ARETURN);
            mv.visitMaxs(4, 2);
        } else {
            mv.visitTypeInsn(Opcodes.NEW, "java/lang/UnsupportedOperationException");
            mv.visitInsn(89);
            mv.visitLdcInsn("Not an inner class.");
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/UnsupportedOperationException", "<init>", "(Ljava/lang/String;)V");
            mv.visitInsn(Opcodes.ATHROW);
            mv.visitMaxs(3, 2);
        }
        mv.visitEnd();
    }
}
