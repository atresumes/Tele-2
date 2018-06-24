package org.objectweb.asm;

import android.support.v4.internal.view.SupportMenu;

final class FieldWriter extends FieldVisitor {
    private final ClassWriter f118b;
    private final int f119c;
    private final int f120d;
    private final int f121e;
    private int f122f;
    private int f123g;
    private AnnotationWriter f124h;
    private AnnotationWriter f125i;
    private Attribute f126j;
    private AnnotationWriter f127k;
    private AnnotationWriter f128l;

    FieldWriter(ClassWriter classWriter, int i, String str, String str2, String str3, Object obj) {
        super(Opcodes.ASM5);
        if (classWriter.f57B == null) {
            classWriter.f57B = this;
        } else {
            classWriter.f58C.fv = this;
        }
        classWriter.f58C = this;
        this.f118b = classWriter;
        this.f119c = i;
        this.f120d = classWriter.newUTF8(str);
        this.f121e = classWriter.newUTF8(str2);
        if (str3 != null) {
            this.f122f = classWriter.newUTF8(str3);
        }
        if (obj != null) {
            this.f123g = classWriter.m106a(obj).f148a;
        }
    }

    int m114a() {
        int a;
        int i = 8;
        if (this.f123g != 0) {
            this.f118b.newUTF8("ConstantValue");
            i = 16;
        }
        if ((this.f119c & 4096) != 0 && ((this.f118b.f70b & SupportMenu.USER_MASK) < 49 || (this.f119c & 262144) != 0)) {
            this.f118b.newUTF8("Synthetic");
            i += 6;
        }
        if ((this.f119c & 131072) != 0) {
            this.f118b.newUTF8("Deprecated");
            i += 6;
        }
        if (this.f122f != 0) {
            this.f118b.newUTF8("Signature");
            i += 8;
        }
        if (this.f124h != null) {
            this.f118b.newUTF8("RuntimeVisibleAnnotations");
            i += this.f124h.m67a() + 8;
        }
        if (this.f125i != null) {
            this.f118b.newUTF8("RuntimeInvisibleAnnotations");
            i += this.f125i.m67a() + 8;
        }
        if (this.f127k != null) {
            this.f118b.newUTF8("RuntimeVisibleTypeAnnotations");
            i += this.f127k.m67a() + 8;
        }
        if (this.f128l != null) {
            this.f118b.newUTF8("RuntimeInvisibleTypeAnnotations");
            a = i + (this.f128l.m67a() + 8);
        } else {
            a = i;
        }
        return this.f126j != null ? a + this.f126j.m70a(this.f118b, null, 0, -1, -1) : a;
    }

    void m115a(ByteVector byteVector) {
        byteVector.putShort(((393216 | ((this.f119c & 262144) / 64)) ^ -1) & this.f119c).putShort(this.f120d).putShort(this.f121e);
        int i = this.f123g != 0 ? 1 : 0;
        if ((this.f119c & 4096) != 0 && ((this.f118b.f70b & SupportMenu.USER_MASK) < 49 || (this.f119c & 262144) != 0)) {
            i++;
        }
        if ((this.f119c & 131072) != 0) {
            i++;
        }
        if (this.f122f != 0) {
            i++;
        }
        if (this.f124h != null) {
            i++;
        }
        if (this.f125i != null) {
            i++;
        }
        if (this.f127k != null) {
            i++;
        }
        if (this.f128l != null) {
            i++;
        }
        if (this.f126j != null) {
            i += this.f126j.m69a();
        }
        byteVector.putShort(i);
        if (this.f123g != 0) {
            byteVector.putShort(this.f118b.newUTF8("ConstantValue"));
            byteVector.putInt(2).putShort(this.f123g);
        }
        if ((this.f119c & 4096) != 0 && ((this.f118b.f70b & SupportMenu.USER_MASK) < 49 || (this.f119c & 262144) != 0)) {
            byteVector.putShort(this.f118b.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.f119c & 131072) != 0) {
            byteVector.putShort(this.f118b.newUTF8("Deprecated")).putInt(0);
        }
        if (this.f122f != 0) {
            byteVector.putShort(this.f118b.newUTF8("Signature"));
            byteVector.putInt(2).putShort(this.f122f);
        }
        if (this.f124h != null) {
            byteVector.putShort(this.f118b.newUTF8("RuntimeVisibleAnnotations"));
            this.f124h.m68a(byteVector);
        }
        if (this.f125i != null) {
            byteVector.putShort(this.f118b.newUTF8("RuntimeInvisibleAnnotations"));
            this.f125i.m68a(byteVector);
        }
        if (this.f127k != null) {
            byteVector.putShort(this.f118b.newUTF8("RuntimeVisibleTypeAnnotations"));
            this.f127k.m68a(byteVector);
        }
        if (this.f128l != null) {
            byteVector.putShort(this.f118b.newUTF8("RuntimeInvisibleTypeAnnotations"));
            this.f128l.m68a(byteVector);
        }
        if (this.f126j != null) {
            this.f126j.m71a(this.f118b, null, 0, -1, -1, byteVector);
        }
    }

    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.f118b.newUTF8(str)).putShort(0);
        AnnotationVisitor annotationWriter = new AnnotationWriter(this.f118b, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.f45g = this.f124h;
            this.f124h = annotationWriter;
        } else {
            annotationWriter.f45g = this.f125i;
            this.f125i = annotationWriter;
        }
        return annotationWriter;
    }

    public void visitAttribute(Attribute attribute) {
        attribute.f47a = this.f126j;
        this.f126j = attribute;
    }

    public void visitEnd() {
    }

    public AnnotationVisitor visitTypeAnnotation(int i, TypePath typePath, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        AnnotationWriter.m65a(i, typePath, byteVector);
        byteVector.putShort(this.f118b.newUTF8(str)).putShort(0);
        AnnotationVisitor annotationWriter = new AnnotationWriter(this.f118b, true, byteVector, byteVector, byteVector.f50b - 2);
        if (z) {
            annotationWriter.f45g = this.f127k;
            this.f127k = annotationWriter;
        } else {
            annotationWriter.f45g = this.f128l;
            this.f128l = annotationWriter;
        }
        return annotationWriter;
    }
}
