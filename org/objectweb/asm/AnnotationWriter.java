package org.objectweb.asm;

final class AnnotationWriter extends AnnotationVisitor {
    private final ClassWriter f39a;
    private int f40b;
    private final boolean f41c;
    private final ByteVector f42d;
    private final ByteVector f43e;
    private final int f44f;
    AnnotationWriter f45g;
    AnnotationWriter f46h;

    AnnotationWriter(ClassWriter classWriter, boolean z, ByteVector byteVector, ByteVector byteVector2, int i) {
        super(Opcodes.ASM5);
        this.f39a = classWriter;
        this.f41c = z;
        this.f42d = byteVector;
        this.f43e = byteVector2;
        this.f44f = i;
    }

    static void m65a(int i, TypePath typePath, ByteVector byteVector) {
        switch (i >>> 24) {
            case 0:
            case 1:
            case 22:
                byteVector.putShort(i >>> 16);
                break;
            case 19:
            case 20:
            case 21:
                byteVector.putByte(i >>> 24);
                break;
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
                byteVector.putInt(i);
                break;
            default:
                byteVector.m74b(i >>> 24, (16776960 & i) >> 8);
                break;
        }
        if (typePath == null) {
            byteVector.putByte(0);
            return;
        }
        byteVector.putByteArray(typePath.f223a, typePath.f224b, (typePath.f223a[typePath.f224b] * 2) + 1);
    }

    static void m66a(AnnotationWriter[] annotationWriterArr, int i, ByteVector byteVector) {
        int length = ((annotationWriterArr.length - i) * 2) + 1;
        for (int i2 = i; i2 < annotationWriterArr.length; i2++) {
            length += annotationWriterArr[i2] == null ? 0 : annotationWriterArr[i2].m67a();
        }
        byteVector.putInt(length).putByte(annotationWriterArr.length - i);
        while (i < annotationWriterArr.length) {
            AnnotationWriter annotationWriter = annotationWriterArr[i];
            AnnotationWriter annotationWriter2 = null;
            length = 0;
            while (annotationWriter != null) {
                length++;
                annotationWriter.visitEnd();
                annotationWriter.f46h = annotationWriter2;
                AnnotationWriter annotationWriter3 = annotationWriter;
                annotationWriter = annotationWriter.f45g;
                annotationWriter2 = annotationWriter3;
            }
            byteVector.putShort(length);
            while (annotationWriter2 != null) {
                byteVector.putByteArray(annotationWriter2.f42d.f49a, 0, annotationWriter2.f42d.f50b);
                annotationWriter2 = annotationWriter2.f46h;
            }
            i++;
        }
    }

    int m67a() {
        int i = 0;
        while (this != null) {
            i += this.f42d.f50b;
            this = this.f45g;
        }
        return i;
    }

    void m68a(ByteVector byteVector) {
        AnnotationWriter annotationWriter = null;
        int i = 2;
        int i2 = 0;
        for (AnnotationWriter annotationWriter2 = this; annotationWriter2 != null; annotationWriter2 = annotationWriter2.f45g) {
            i2++;
            i += annotationWriter2.f42d.f50b;
            annotationWriter2.visitEnd();
            annotationWriter2.f46h = annotationWriter;
            annotationWriter = annotationWriter2;
        }
        byteVector.putInt(i);
        byteVector.putShort(i2);
        while (annotationWriter != null) {
            byteVector.putByteArray(annotationWriter.f42d.f49a, 0, annotationWriter.f42d.f50b);
            annotationWriter = annotationWriter.f46h;
        }
    }

    public void visit(String str, Object obj) {
        int i = 1;
        int i2 = 0;
        this.f40b++;
        if (this.f41c) {
            this.f42d.putShort(this.f39a.newUTF8(str));
        }
        if (obj instanceof String) {
            this.f42d.m74b(Opcodes.DREM, this.f39a.newUTF8((String) obj));
        } else if (obj instanceof Byte) {
            this.f42d.m74b(66, this.f39a.m103a(((Byte) obj).byteValue()).f148a);
        } else if (obj instanceof Boolean) {
            if (!((Boolean) obj).booleanValue()) {
                i = 0;
            }
            this.f42d.m74b(90, this.f39a.m103a(i).f148a);
        } else if (obj instanceof Character) {
            this.f42d.m74b(67, this.f39a.m103a(((Character) obj).charValue()).f148a);
        } else if (obj instanceof Short) {
            this.f42d.m74b(83, this.f39a.m103a(((Short) obj).shortValue()).f148a);
        } else if (obj instanceof Type) {
            this.f42d.m74b(99, this.f39a.newUTF8(((Type) obj).getDescriptor()));
        } else if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            this.f42d.m74b(91, bArr.length);
            while (i2 < bArr.length) {
                this.f42d.m74b(66, this.f39a.m103a(bArr[i2]).f148a);
                i2++;
            }
        } else if (obj instanceof boolean[]) {
            boolean[] zArr = (boolean[]) obj;
            this.f42d.m74b(91, zArr.length);
            for (boolean z : zArr) {
                this.f42d.m74b(90, this.f39a.m103a(z ? 1 : 0).f148a);
            }
        } else if (obj instanceof short[]) {
            short[] sArr = (short[]) obj;
            this.f42d.m74b(91, sArr.length);
            while (i2 < sArr.length) {
                this.f42d.m74b(83, this.f39a.m103a(sArr[i2]).f148a);
                i2++;
            }
        } else if (obj instanceof char[]) {
            char[] cArr = (char[]) obj;
            this.f42d.m74b(91, cArr.length);
            while (i2 < cArr.length) {
                this.f42d.m74b(67, this.f39a.m103a(cArr[i2]).f148a);
                i2++;
            }
        } else if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            this.f42d.m74b(91, iArr.length);
            while (i2 < iArr.length) {
                this.f42d.m74b(73, this.f39a.m103a(iArr[i2]).f148a);
                i2++;
            }
        } else if (obj instanceof long[]) {
            long[] jArr = (long[]) obj;
            this.f42d.m74b(91, jArr.length);
            while (i2 < jArr.length) {
                this.f42d.m74b(74, this.f39a.m105a(jArr[i2]).f148a);
                i2++;
            }
        } else if (obj instanceof float[]) {
            float[] fArr = (float[]) obj;
            this.f42d.m74b(91, fArr.length);
            while (i2 < fArr.length) {
                this.f42d.m74b(70, this.f39a.m102a(fArr[i2]).f148a);
                i2++;
            }
        } else if (obj instanceof double[]) {
            double[] dArr = (double[]) obj;
            this.f42d.m74b(91, dArr.length);
            while (i2 < dArr.length) {
                this.f42d.m74b(68, this.f39a.m101a(dArr[i2]).f148a);
                i2++;
            }
        } else {
            Item a = this.f39a.m106a(obj);
            this.f42d.m74b(".s.IFJDCS".charAt(a.f149b), a.f148a);
        }
    }

    public AnnotationVisitor visitAnnotation(String str, String str2) {
        this.f40b++;
        if (this.f41c) {
            this.f42d.putShort(this.f39a.newUTF8(str));
        }
        this.f42d.m74b(64, this.f39a.newUTF8(str2)).putShort(0);
        return new AnnotationWriter(this.f39a, true, this.f42d, this.f42d, this.f42d.f50b - 2);
    }

    public AnnotationVisitor visitArray(String str) {
        this.f40b++;
        if (this.f41c) {
            this.f42d.putShort(this.f39a.newUTF8(str));
        }
        this.f42d.m74b(91, 0);
        return new AnnotationWriter(this.f39a, false, this.f42d, this.f42d, this.f42d.f50b - 2);
    }

    public void visitEnd() {
        if (this.f43e != null) {
            byte[] bArr = this.f43e.f49a;
            bArr[this.f44f] = (byte) (this.f40b >>> 8);
            bArr[this.f44f + 1] = (byte) this.f40b;
        }
    }

    public void visitEnum(String str, String str2, String str3) {
        this.f40b++;
        if (this.f41c) {
            this.f42d.putShort(this.f39a.newUTF8(str));
        }
        this.f42d.m74b(101, this.f39a.newUTF8(str2)).putShort(this.f39a.newUTF8(str3));
    }
}
