package org.objectweb.asm;

public abstract class FieldVisitor {
    protected final int api;
    protected FieldVisitor fv;

    public FieldVisitor(int i) {
        this(i, null);
    }

    public FieldVisitor(int i, FieldVisitor fieldVisitor) {
        if (i == 262144 || i == Opcodes.ASM5) {
            this.api = i;
            this.fv = fieldVisitor;
            return;
        }
        throw new IllegalArgumentException();
    }

    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        return this.fv != null ? this.fv.visitAnnotation(str, z) : null;
    }

    public void visitAttribute(Attribute attribute) {
        if (this.fv != null) {
            this.fv.visitAttribute(attribute);
        }
    }

    public void visitEnd() {
        if (this.fv != null) {
            this.fv.visitEnd();
        }
    }

    public AnnotationVisitor visitTypeAnnotation(int i, TypePath typePath, String str, boolean z) {
        if (this.api >= Opcodes.ASM5) {
            return this.fv != null ? this.fv.visitTypeAnnotation(i, typePath, str, z) : null;
        } else {
            throw new RuntimeException();
        }
    }
}
