package org.objectweb.asm;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import java.io.IOException;
import java.io.InputStream;

public class ClassReader {
    public static final int EXPAND_FRAMES = 8;
    public static final int SKIP_CODE = 1;
    public static final int SKIP_DEBUG = 2;
    public static final int SKIP_FRAMES = 4;
    private final int[] f51a;
    public final byte[] f52b;
    private final String[] f53c;
    private final int f54d;
    public final int header;

    public ClassReader(InputStream inputStream) throws IOException {
        this(m88a(inputStream, false));
    }

    public ClassReader(String str) throws IOException {
        this(m88a(ClassLoader.getSystemResourceAsStream(new StringBuffer().append(str.replace('.', '/')).append(".class").toString()), true));
    }

    public ClassReader(byte[] bArr) {
        this(bArr, 0, bArr.length);
    }

    public ClassReader(byte[] bArr, int i, int i2) {
        this.f52b = bArr;
        if (readShort(i + 6) > (short) 52) {
            throw new IllegalArgumentException();
        }
        this.f51a = new int[readUnsignedShort(i + 8)];
        int length = this.f51a.length;
        this.f53c = new String[length];
        int i3 = 0;
        int i4 = 1;
        int i5 = i + 10;
        while (i4 < length) {
            int readUnsignedShort;
            this.f51a[i4] = i5 + 1;
            switch (bArr[i5]) {
                case (byte) 1:
                    readUnsignedShort = readUnsignedShort(i5 + 1) + 3;
                    if (readUnsignedShort <= i3) {
                        break;
                    }
                    i3 = readUnsignedShort;
                    break;
                case (byte) 3:
                case (byte) 4:
                case (byte) 9:
                case (byte) 10:
                case (byte) 11:
                case (byte) 12:
                case (byte) 18:
                    readUnsignedShort = 5;
                    break;
                case (byte) 5:
                case (byte) 6:
                    readUnsignedShort = 9;
                    i4++;
                    break;
                case (byte) 15:
                    readUnsignedShort = 4;
                    break;
                default:
                    readUnsignedShort = 3;
                    break;
            }
            i4++;
            i5 = readUnsignedShort + i5;
        }
        this.f54d = i3;
        this.header = i5;
    }

    private int m76a() {
        int readUnsignedShort;
        int readUnsignedShort2 = (readUnsignedShort(this.header + 6) * 2) + (this.header + 8);
        for (readUnsignedShort = readUnsignedShort(readUnsignedShort2); readUnsignedShort > 0; readUnsignedShort--) {
            int readUnsignedShort3;
            for (readUnsignedShort3 = readUnsignedShort(readUnsignedShort2 + 8); readUnsignedShort3 > 0; readUnsignedShort3--) {
                readUnsignedShort2 += readInt(readUnsignedShort2 + 12) + 6;
            }
            readUnsignedShort2 += 8;
        }
        readUnsignedShort2 += 2;
        for (readUnsignedShort = readUnsignedShort(readUnsignedShort2); readUnsignedShort > 0; readUnsignedShort--) {
            for (readUnsignedShort3 = readUnsignedShort(readUnsignedShort2 + 8); readUnsignedShort3 > 0; readUnsignedShort3--) {
                readUnsignedShort2 += readInt(readUnsignedShort2 + 12) + 6;
            }
            readUnsignedShort2 += 8;
        }
        return readUnsignedShort2 + 2;
    }

    private int m77a(int i, boolean z, boolean z2, Context context) {
        int i2;
        int i3;
        char[] cArr = context.f97c;
        Label[] labelArr = context.f102h;
        if (z) {
            i2 = i + 1;
            i3 = this.f52b[i] & 255;
        } else {
            context.f108o = -1;
            i3 = 255;
            i2 = i;
        }
        context.f111r = 0;
        if (i3 < 64) {
            context.f109p = 3;
            context.f113t = 0;
        } else if (i3 < 128) {
            i3 -= 64;
            i2 = m82a(context.f114u, 0, i2, cArr, labelArr);
            context.f109p = 4;
            context.f113t = 1;
        } else {
            int readUnsignedShort = readUnsignedShort(i2);
            i2 += 2;
            if (i3 == 247) {
                i2 = m82a(context.f114u, 0, i2, cArr, labelArr);
                context.f109p = 4;
                context.f113t = 1;
                i3 = readUnsignedShort;
            } else if (i3 >= 248 && i3 < 251) {
                context.f109p = 2;
                context.f111r = 251 - i3;
                context.f110q -= context.f111r;
                context.f113t = 0;
                i3 = readUnsignedShort;
            } else if (i3 == 251) {
                context.f109p = 3;
                context.f113t = 0;
                i3 = readUnsignedShort;
            } else if (i3 < 255) {
                r8 = i3 - 251;
                r2 = z2 ? context.f110q : 0;
                while (r8 > 0) {
                    int i4 = r2 + 1;
                    i2 = m82a(context.f112s, r2, i2, cArr, labelArr);
                    r8--;
                    r2 = i4;
                }
                context.f109p = 1;
                context.f111r = i3 - 251;
                context.f110q += context.f111r;
                context.f113t = 0;
                i3 = readUnsignedShort;
            } else {
                context.f109p = 0;
                int readUnsignedShort2 = readUnsignedShort(i2);
                i2 += 2;
                context.f111r = readUnsignedShort2;
                context.f110q = readUnsignedShort2;
                r2 = 0;
                for (r8 = readUnsignedShort2; r8 > 0; r8--) {
                    i3 = r2 + 1;
                    i2 = m82a(context.f112s, r2, i2, cArr, labelArr);
                    r2 = i3;
                }
                readUnsignedShort2 = readUnsignedShort(i2);
                i2 += 2;
                context.f113t = readUnsignedShort2;
                r2 = 0;
                for (r8 = readUnsignedShort2; r8 > 0; r8--) {
                    i3 = r2 + 1;
                    i2 = m82a(context.f114u, r2, i2, cArr, labelArr);
                    r2 = i3;
                }
                i3 = readUnsignedShort;
            }
        }
        context.f108o += i3 + 1;
        readLabel(context.f108o, labelArr);
        return i2;
    }

    private int m78a(int i, char[] cArr, String str, AnnotationVisitor annotationVisitor) {
        int i2 = false;
        if (annotationVisitor == null) {
            switch (this.f52b[i] & 255) {
                case 64:
                    return m79a(i + 3, cArr, true, null);
                case 91:
                    return m79a(i + 1, cArr, false, null);
                case 101:
                    return i + 5;
                default:
                    return i + 3;
            }
        }
        int i3 = i + 1;
        switch (this.f52b[i] & 255) {
            case 64:
                return m79a(i3 + 2, cArr, true, annotationVisitor.visitAnnotation(str, readUTF8(i3, cArr)));
            case 66:
                annotationVisitor.visit(str, new Byte((byte) readInt(this.f51a[readUnsignedShort(i3)])));
                return i3 + 2;
            case 67:
                annotationVisitor.visit(str, new Character((char) readInt(this.f51a[readUnsignedShort(i3)])));
                return i3 + 2;
            case 68:
            case 70:
            case 73:
            case 74:
                annotationVisitor.visit(str, readConst(readUnsignedShort(i3), cArr));
                return i3 + 2;
            case 83:
                annotationVisitor.visit(str, new Short((short) readInt(this.f51a[readUnsignedShort(i3)])));
                return i3 + 2;
            case 90:
                annotationVisitor.visit(str, readInt(this.f51a[readUnsignedShort(i3)]) == 0 ? Boolean.FALSE : Boolean.TRUE);
                return i3 + 2;
            case 91:
                int readUnsignedShort = readUnsignedShort(i3);
                i3 += 2;
                if (readUnsignedShort == 0) {
                    return m79a(i3 - 2, cArr, false, annotationVisitor.visitArray(str));
                }
                int i4 = i3 + 1;
                Object obj;
                switch (this.f52b[i3] & 255) {
                    case 66:
                        obj = new byte[readUnsignedShort];
                        while (i2 < readUnsignedShort) {
                            obj[i2] = (byte) readInt(this.f51a[readUnsignedShort(i4)]);
                            i4 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, obj);
                        return i4 - 1;
                    case 67:
                        obj = new char[readUnsignedShort];
                        while (i2 < readUnsignedShort) {
                            obj[i2] = (char) readInt(this.f51a[readUnsignedShort(i4)]);
                            i4 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, obj);
                        return i4 - 1;
                    case 68:
                        obj = new double[readUnsignedShort];
                        while (i2 < readUnsignedShort) {
                            obj[i2] = Double.longBitsToDouble(readLong(this.f51a[readUnsignedShort(i4)]));
                            i4 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, obj);
                        return i4 - 1;
                    case 70:
                        obj = new float[readUnsignedShort];
                        while (i2 < readUnsignedShort) {
                            obj[i2] = Float.intBitsToFloat(readInt(this.f51a[readUnsignedShort(i4)]));
                            i4 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, obj);
                        return i4 - 1;
                    case 73:
                        obj = new int[readUnsignedShort];
                        while (i2 < readUnsignedShort) {
                            obj[i2] = readInt(this.f51a[readUnsignedShort(i4)]);
                            i4 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, obj);
                        return i4 - 1;
                    case 74:
                        obj = new long[readUnsignedShort];
                        while (i2 < readUnsignedShort) {
                            obj[i2] = readLong(this.f51a[readUnsignedShort(i4)]);
                            i4 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, obj);
                        return i4 - 1;
                    case 83:
                        obj = new short[readUnsignedShort];
                        while (i2 < readUnsignedShort) {
                            obj[i2] = (short) readInt(this.f51a[readUnsignedShort(i4)]);
                            i4 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, obj);
                        return i4 - 1;
                    case 90:
                        Object obj2 = new boolean[readUnsignedShort];
                        int i5 = i4;
                        for (i3 = 0; i3 < readUnsignedShort; i3++) {
                            obj2[i3] = readInt(this.f51a[readUnsignedShort(i5)]) != 0;
                            i5 += 3;
                        }
                        annotationVisitor.visit(str, obj2);
                        return i5 - 1;
                    default:
                        return m79a(i4 - 3, cArr, false, annotationVisitor.visitArray(str));
                }
            case 99:
                annotationVisitor.visit(str, Type.getType(readUTF8(i3, cArr)));
                return i3 + 2;
            case 101:
                annotationVisitor.visitEnum(str, readUTF8(i3, cArr), readUTF8(i3 + 2, cArr));
                return i3 + 4;
            case Opcodes.DREM /*115*/:
                annotationVisitor.visit(str, readUTF8(i3, cArr));
                return i3 + 2;
            default:
                return i3;
        }
    }

    private int m79a(int i, char[] cArr, boolean z, AnnotationVisitor annotationVisitor) {
        int readUnsignedShort = readUnsignedShort(i);
        int i2 = i + 2;
        int i3;
        if (z) {
            i3 = readUnsignedShort;
            readUnsignedShort = i2;
            i2 = i3;
            while (i2 > 0) {
                i2--;
                readUnsignedShort = m78a(readUnsignedShort + 2, cArr, readUTF8(readUnsignedShort, cArr), annotationVisitor);
            }
        } else {
            i3 = readUnsignedShort;
            readUnsignedShort = i2;
            i2 = i3;
            while (i2 > 0) {
                i2--;
                readUnsignedShort = m78a(readUnsignedShort, cArr, null, annotationVisitor);
            }
        }
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
        return readUnsignedShort;
    }

    private int m80a(ClassVisitor classVisitor, Context context, int i) {
        int readUnsignedShort;
        int i2;
        char[] cArr = context.f97c;
        int readUnsignedShort2 = readUnsignedShort(i);
        String readUTF8 = readUTF8(i + 2, cArr);
        String readUTF82 = readUTF8(i + 4, cArr);
        int i3 = i + 6;
        String str = null;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        Object obj = null;
        Attribute attribute = null;
        int readUnsignedShort3 = readUnsignedShort(i3);
        int i8 = i3;
        while (readUnsignedShort3 > 0) {
            Attribute attribute2;
            int i9;
            int i10;
            int i11;
            String readUTF83 = readUTF8(i8 + 2, cArr);
            if ("ConstantValue".equals(readUTF83)) {
                readUnsignedShort = readUnsignedShort(i8 + 8);
                obj = readUnsignedShort == 0 ? null : readConst(readUnsignedShort, cArr);
                attribute2 = attribute;
                i3 = i7;
                i2 = i6;
                i9 = i5;
                i10 = i4;
                i11 = readUnsignedShort2;
            } else if ("Signature".equals(readUTF83)) {
                str = readUTF8(i8 + 8, cArr);
                attribute2 = attribute;
                i3 = i7;
                i2 = i6;
                i9 = i5;
                i10 = i4;
                i11 = readUnsignedShort2;
            } else if ("Deprecated".equals(readUTF83)) {
                i3 = i7;
                i2 = i6;
                i9 = i5;
                i10 = i4;
                i11 = 131072 | readUnsignedShort2;
                attribute2 = attribute;
            } else if ("Synthetic".equals(readUTF83)) {
                i3 = i7;
                i2 = i6;
                i9 = i5;
                i10 = i4;
                i11 = 266240 | readUnsignedShort2;
                attribute2 = attribute;
            } else if ("RuntimeVisibleAnnotations".equals(readUTF83)) {
                i3 = i7;
                i2 = i6;
                i9 = i5;
                i10 = i8 + 8;
                i11 = readUnsignedShort2;
                attribute2 = attribute;
            } else if ("RuntimeVisibleTypeAnnotations".equals(readUTF83)) {
                i3 = i7;
                i2 = i8 + 8;
                i9 = i5;
                i10 = i4;
                i11 = readUnsignedShort2;
                attribute2 = attribute;
            } else if ("RuntimeInvisibleAnnotations".equals(readUTF83)) {
                i3 = i7;
                i2 = i6;
                i9 = i8 + 8;
                i10 = i4;
                i11 = readUnsignedShort2;
                attribute2 = attribute;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(readUTF83)) {
                i3 = i8 + 8;
                i2 = i6;
                i9 = i5;
                i10 = i4;
                i11 = readUnsignedShort2;
                attribute2 = attribute;
            } else {
                attribute2 = m84a(context.f95a, readUTF83, i8 + 8, readInt(i8 + 4), cArr, -1, null);
                if (attribute2 != null) {
                    attribute2.f47a = attribute;
                    i3 = i7;
                    i2 = i6;
                    i9 = i5;
                    i10 = i4;
                    i11 = readUnsignedShort2;
                } else {
                    attribute2 = attribute;
                    i3 = i7;
                    i2 = i6;
                    i9 = i5;
                    i10 = i4;
                    i11 = readUnsignedShort2;
                }
            }
            readUnsignedShort3--;
            i7 = i3;
            i6 = i2;
            i5 = i9;
            i4 = i10;
            readUnsignedShort2 = i11;
            i8 += readInt(i8 + 4) + 6;
            attribute = attribute2;
        }
        readUnsignedShort = i8 + 2;
        FieldVisitor visitField = classVisitor.visitField(readUnsignedShort2, readUTF8, readUTF82, str, obj);
        if (visitField != null) {
            if (i4 != 0) {
                i3 = i4 + 2;
                for (i2 = readUnsignedShort(i4); i2 > 0; i2--) {
                    i3 = m79a(i3 + 2, cArr, true, visitField.visitAnnotation(readUTF8(i3, cArr), true));
                }
            }
            if (i5 != 0) {
                i3 = i5 + 2;
                for (i2 = readUnsignedShort(i5); i2 > 0; i2--) {
                    i3 = m79a(i3 + 2, cArr, true, visitField.visitAnnotation(readUTF8(i3, cArr), false));
                }
            }
            if (i6 != 0) {
                i3 = i6 + 2;
                for (i2 = readUnsignedShort(i6); i2 > 0; i2--) {
                    i3 = m81a(context, i3);
                    i3 = m79a(i3 + 2, cArr, true, visitField.visitTypeAnnotation(context.f103i, context.f104j, readUTF8(i3, cArr), true));
                }
            }
            if (i7 != 0) {
                i3 = i7 + 2;
                for (i2 = readUnsignedShort(i7); i2 > 0; i2--) {
                    i3 = m81a(context, i3);
                    i3 = m79a(i3 + 2, cArr, true, visitField.visitTypeAnnotation(context.f103i, context.f104j, readUTF8(i3, cArr), false));
                }
            }
            while (attribute != null) {
                Attribute attribute3 = attribute.f47a;
                attribute.f47a = null;
                visitField.visitAttribute(attribute);
                attribute = attribute3;
            }
            visitField.visitEnd();
        }
        return readUnsignedShort;
    }

    private int m81a(Context context, int i) {
        int i2;
        int i3 = -16777216;
        int readInt = readInt(i);
        switch (readInt >>> 24) {
            case 0:
            case 1:
            case 22:
                i3 = SupportMenu.CATEGORY_MASK & readInt;
                readInt = i + 2;
                break;
            case 19:
            case 20:
            case 21:
                i3 = -16777216 & readInt;
                readInt = i + 1;
                break;
            case 64:
            case 65:
                i3 = -16777216 & readInt;
                int readUnsignedShort = readUnsignedShort(i + 1);
                context.f105l = new Label[readUnsignedShort];
                context.f106m = new Label[readUnsignedShort];
                context.f107n = new int[readUnsignedShort];
                readInt = i + 3;
                i2 = 0;
                while (i2 < readUnsignedShort) {
                    int readUnsignedShort2 = readUnsignedShort(readInt);
                    int readUnsignedShort3 = readUnsignedShort(readInt + 2);
                    context.f105l[i2] = readLabel(readUnsignedShort2, context.f102h);
                    context.f106m[i2] = readLabel(readUnsignedShort2 + readUnsignedShort3, context.f102h);
                    context.f107n[i2] = readUnsignedShort(readInt + 4);
                    i2++;
                    readInt += 6;
                }
                break;
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
                i3 = -16776961 & readInt;
                readInt = i + 4;
                break;
            default:
                if ((readInt >>> 24) < 67) {
                    i3 = InputDeviceCompat.SOURCE_ANY;
                }
                i3 &= readInt;
                readInt = i + 3;
                break;
        }
        i2 = readByte(readInt);
        context.f103i = i3;
        context.f104j = i2 == 0 ? null : new TypePath(this.f52b, readInt);
        return (readInt + 1) + (i2 * 2);
    }

    private int m82a(Object[] objArr, int i, int i2, char[] cArr, Label[] labelArr) {
        int i3 = i2 + 1;
        switch (this.f52b[i2] & 255) {
            case 0:
                objArr[i] = Opcodes.TOP;
                return i3;
            case 1:
                objArr[i] = Opcodes.INTEGER;
                return i3;
            case 2:
                objArr[i] = Opcodes.FLOAT;
                return i3;
            case 3:
                objArr[i] = Opcodes.DOUBLE;
                return i3;
            case 4:
                objArr[i] = Opcodes.LONG;
                return i3;
            case 5:
                objArr[i] = Opcodes.NULL;
                return i3;
            case 6:
                objArr[i] = Opcodes.UNINITIALIZED_THIS;
                return i3;
            case 7:
                objArr[i] = readClass(i3, cArr);
                return i3 + 2;
            default:
                objArr[i] = readLabel(readUnsignedShort(i3), labelArr);
                return i3 + 2;
        }
    }

    private String m83a(int i, int i2, char[] cArr) {
        int i3 = i + i2;
        byte[] bArr = this.f52b;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (i < i3) {
            int i7;
            int i8 = i + 1;
            byte b = bArr[i];
            switch (i5) {
                case 0:
                    int i9 = b & 255;
                    if (i9 >= 128) {
                        if (i9 < 224 && i9 > Opcodes.ATHROW) {
                            i4 = (char) (i9 & 31);
                            i5 = 1;
                            i7 = i6;
                            break;
                        }
                        i4 = (char) (i9 & 15);
                        i5 = 2;
                        i7 = i6;
                        break;
                    }
                    i7 = i6 + 1;
                    cArr[i6] = (char) i9;
                    break;
                case 1:
                    i5 = i6 + 1;
                    cArr[i6] = (char) ((b & 63) | (i4 << 6));
                    i7 = i5;
                    i5 = 0;
                    break;
                case 2:
                    i4 = (char) ((i4 << 6) | (b & 63));
                    i5 = 1;
                    i7 = i6;
                    break;
                default:
                    i7 = i6;
                    break;
            }
            i6 = i7;
            i = i8;
        }
        return new String(cArr, 0, i6);
    }

    private Attribute m84a(Attribute[] attributeArr, String str, int i, int i2, char[] cArr, int i3, Label[] labelArr) {
        for (int i4 = 0; i4 < attributeArr.length; i4++) {
            if (attributeArr[i4].type.equals(str)) {
                return attributeArr[i4].read(this, i, i2, cArr, i3, labelArr);
            }
        }
        return new Attribute(str).read(this, i, i2, null, -1, null);
    }

    private void m85a(ClassWriter classWriter, Item[] itemArr, char[] cArr) {
        Object obj;
        int a = m76a();
        int readUnsignedShort = readUnsignedShort(a);
        int i = a;
        while (readUnsignedShort > 0) {
            if ("BootstrapMethods".equals(readUTF8(i + 2, cArr))) {
                obj = 1;
                break;
            } else {
                readUnsignedShort--;
                i = (readInt(i + 4) + 6) + i;
            }
        }
        obj = null;
        if (obj != null) {
            int readUnsignedShort2;
            int readUnsignedShort3 = readUnsignedShort(i + 8);
            a = i + 10;
            for (int i2 = 0; i2 < readUnsignedShort3; i2++) {
                int i3 = (a - i) - 10;
                readUnsignedShort = readConst(readUnsignedShort(a), cArr).hashCode();
                for (readUnsignedShort2 = readUnsignedShort(a + 2); readUnsignedShort2 > 0; readUnsignedShort2--) {
                    readUnsignedShort ^= readConst(readUnsignedShort(a + 4), cArr).hashCode();
                    a += 2;
                }
                a += 4;
                Item item = new Item(i2);
                item.m134a(i3, readUnsignedShort & Integer.MAX_VALUE);
                readUnsignedShort = item.f155j % itemArr.length;
                item.f156k = itemArr[readUnsignedShort];
                itemArr[readUnsignedShort] = item;
            }
            readUnsignedShort2 = readInt(i + 4);
            ByteVector byteVector = new ByteVector(readUnsignedShort2 + 62);
            byteVector.putByteArray(this.f52b, i + 10, readUnsignedShort2 - 2);
            classWriter.f94z = readUnsignedShort3;
            classWriter.f56A = byteVector;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void m86a(org.objectweb.asm.Context r9) {
        /*
        r8 = this;
        r7 = 59;
        r1 = 1;
        r4 = r9.f101g;
        r5 = r9.f112s;
        r0 = 0;
        r2 = r9.f99e;
        r2 = r2 & 8;
        if (r2 != 0) goto L_0x001d;
    L_0x000e:
        r2 = "<init>";
        r3 = r9.f100f;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0029;
    L_0x0018:
        r2 = org.objectweb.asm.Opcodes.UNINITIALIZED_THIS;
        r5[r0] = r2;
        r0 = r1;
    L_0x001d:
        r2 = r1 + 1;
        r3 = r4.charAt(r1);
        switch(r3) {
            case 66: goto L_0x0037;
            case 67: goto L_0x0037;
            case 68: goto L_0x0052;
            case 70: goto L_0x0040;
            case 73: goto L_0x0037;
            case 74: goto L_0x0049;
            case 76: goto L_0x0086;
            case 83: goto L_0x0037;
            case 90: goto L_0x0037;
            case 91: goto L_0x005b;
            default: goto L_0x0026;
        };
    L_0x0026:
        r9.f110q = r0;
        return;
    L_0x0029:
        r2 = r8.header;
        r2 = r2 + 2;
        r3 = r9.f97c;
        r2 = r8.readClass(r2, r3);
        r5[r0] = r2;
        r0 = r1;
        goto L_0x001d;
    L_0x0037:
        r1 = r0 + 1;
        r3 = org.objectweb.asm.Opcodes.INTEGER;
        r5[r0] = r3;
        r0 = r1;
        r1 = r2;
        goto L_0x001d;
    L_0x0040:
        r1 = r0 + 1;
        r3 = org.objectweb.asm.Opcodes.FLOAT;
        r5[r0] = r3;
        r0 = r1;
        r1 = r2;
        goto L_0x001d;
    L_0x0049:
        r1 = r0 + 1;
        r3 = org.objectweb.asm.Opcodes.LONG;
        r5[r0] = r3;
        r0 = r1;
        r1 = r2;
        goto L_0x001d;
    L_0x0052:
        r1 = r0 + 1;
        r3 = org.objectweb.asm.Opcodes.DOUBLE;
        r5[r0] = r3;
        r0 = r1;
        r1 = r2;
        goto L_0x001d;
    L_0x005b:
        r3 = r4.charAt(r2);
        r6 = 91;
        if (r3 != r6) goto L_0x0066;
    L_0x0063:
        r2 = r2 + 1;
        goto L_0x005b;
    L_0x0066:
        r3 = r4.charAt(r2);
        r6 = 76;
        if (r3 != r6) goto L_0x0079;
    L_0x006e:
        r2 = r2 + 1;
    L_0x0070:
        r3 = r4.charAt(r2);
        if (r3 == r7) goto L_0x0079;
    L_0x0076:
        r2 = r2 + 1;
        goto L_0x0070;
    L_0x0079:
        r3 = r0 + 1;
        r2 = r2 + 1;
        r1 = r4.substring(r1, r2);
        r5[r0] = r1;
        r1 = r2;
        r0 = r3;
        goto L_0x001d;
    L_0x0086:
        r3 = r4.charAt(r2);
        if (r3 == r7) goto L_0x008f;
    L_0x008c:
        r2 = r2 + 1;
        goto L_0x0086;
    L_0x008f:
        r3 = r0 + 1;
        r6 = r1 + 1;
        r1 = r2 + 1;
        r2 = r4.substring(r6, r2);
        r5[r0] = r2;
        r0 = r3;
        goto L_0x001d;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.objectweb.asm.ClassReader.a(org.objectweb.asm.Context):void");
    }

    private void m87a(MethodVisitor methodVisitor, Context context, int i) {
        int i2;
        int i3;
        int readUnsignedShort;
        int readUnsignedShort2;
        int i4;
        int i5;
        Context context2;
        byte[] bArr = this.f52b;
        char[] cArr = context.f97c;
        int readUnsignedShort3 = readUnsignedShort(i);
        int readUnsignedShort4 = readUnsignedShort(i + 2);
        int readInt = readInt(i + 4);
        int i6 = i + 8;
        int i7 = i6 + readInt;
        Label[] labelArr = new Label[(readInt + 2)];
        context.f102h = labelArr;
        readLabel(readInt + 1, labelArr);
        int i8 = i6;
        while (i8 < i7) {
            i2 = i8 - i6;
            switch (ClassWriter.f55a[bArr[i8] & 255]) {
                case (byte) 0:
                case (byte) 4:
                    i3 = i8 + 1;
                    break;
                case (byte) 1:
                case (byte) 3:
                case (byte) 11:
                    i3 = i8 + 2;
                    break;
                case (byte) 2:
                case (byte) 5:
                case (byte) 6:
                case (byte) 12:
                case (byte) 13:
                    i3 = i8 + 3;
                    break;
                case (byte) 7:
                case (byte) 8:
                    i3 = i8 + 5;
                    break;
                case (byte) 9:
                    readLabel(readShort(i8 + 1) + i2, labelArr);
                    i3 = i8 + 3;
                    break;
                case (byte) 10:
                    readLabel(readInt(i8 + 1) + i2, labelArr);
                    i3 = i8 + 5;
                    break;
                case (byte) 14:
                    i8 = (i8 + 4) - (i2 & 3);
                    readLabel(readInt(i8) + i2, labelArr);
                    for (i3 = (readInt(i8 + 8) - readInt(i8 + 4)) + 1; i3 > 0; i3--) {
                        readLabel(readInt(i8 + 12) + i2, labelArr);
                        i8 += 4;
                    }
                    i3 = i8 + 12;
                    break;
                case (byte) 15:
                    i8 = (i8 + 4) - (i2 & 3);
                    readLabel(readInt(i8) + i2, labelArr);
                    for (i3 = readInt(i8 + 4); i3 > 0; i3--) {
                        readLabel(readInt(i8 + 12) + i2, labelArr);
                        i8 += 8;
                    }
                    i3 = i8 + 8;
                    break;
                case (byte) 17:
                    if ((bArr[i8 + 1] & 255) != Opcodes.IINC) {
                        i3 = i8 + 4;
                        break;
                    } else {
                        i3 = i8 + 6;
                        break;
                    }
                default:
                    i3 = i8 + 4;
                    break;
            }
            i8 = i3;
        }
        for (i3 = readUnsignedShort(i8); i3 > 0; i3--) {
            methodVisitor.visitTryCatchBlock(readLabel(readUnsignedShort(i8 + 2), labelArr), readLabel(readUnsignedShort(i8 + 4), labelArr), readLabel(readUnsignedShort(i8 + 6), labelArr), readUTF8(this.f51a[readUnsignedShort(i8 + 8)], cArr));
            i8 += 8;
        }
        int i9 = i8 + 2;
        int[] iArr = null;
        int[] iArr2 = null;
        int i10 = -1;
        int i11 = -1;
        int i12 = 0;
        int i13 = 0;
        boolean z = true;
        boolean z2 = (context.f96b & 8) != 0;
        int i14 = 0;
        int i15 = 0;
        int i16 = 0;
        Attribute attribute = null;
        int readUnsignedShort5 = readUnsignedShort(i9);
        while (readUnsignedShort5 > 0) {
            boolean z3;
            int[] iArr3;
            int[] iArr4;
            Attribute attribute2;
            String readUTF8 = readUTF8(i9 + 2, cArr);
            Label readLabel;
            if ("LocalVariableTable".equals(readUTF8)) {
                if ((context.f96b & 2) == 0) {
                    i3 = i9 + 8;
                    i2 = i9;
                    for (readUnsignedShort = readUnsignedShort(i9 + 8); readUnsignedShort > 0; readUnsignedShort--) {
                        readUnsignedShort2 = readUnsignedShort(i2 + 10);
                        if (labelArr[readUnsignedShort2] == null) {
                            Label readLabel2 = readLabel(readUnsignedShort2, labelArr);
                            readLabel2.f157a |= 1;
                        }
                        readUnsignedShort2 += readUnsignedShort(i2 + 12);
                        if (labelArr[readUnsignedShort2] == null) {
                            readLabel = readLabel(readUnsignedShort2, labelArr);
                            readLabel.f157a |= 1;
                        }
                        i2 += 10;
                    }
                    i2 = i15;
                    readUnsignedShort = i14;
                    z3 = z;
                    i4 = i13;
                    i15 = i11;
                    i14 = i10;
                    iArr3 = iArr2;
                    iArr4 = iArr;
                    int i17 = i16;
                    i16 = i3;
                    attribute2 = attribute;
                    i8 = i17;
                }
                attribute2 = attribute;
                i2 = i15;
                readUnsignedShort = i14;
                z3 = z;
                i4 = i13;
                i8 = i16;
                i15 = i11;
                i14 = i10;
                i16 = i12;
                iArr3 = iArr2;
                iArr4 = iArr;
            } else if ("LocalVariableTypeTable".equals(readUTF8)) {
                i2 = i15;
                readUnsignedShort = i14;
                z3 = z;
                i4 = i9 + 8;
                attribute2 = attribute;
                i15 = i11;
                i14 = i10;
                i8 = i16;
                iArr3 = iArr2;
                iArr4 = iArr;
                i16 = i12;
            } else if ("LineNumberTable".equals(readUTF8)) {
                if ((context.f96b & 2) == 0) {
                    i2 = i9;
                    readUnsignedShort = readUnsignedShort(i9 + 8);
                    while (readUnsignedShort > 0) {
                        i3 = readUnsignedShort(i2 + 10);
                        if (labelArr[i3] == null) {
                            readLabel = readLabel(i3, labelArr);
                            readLabel.f157a |= 1;
                        }
                        Label label = labelArr[i3];
                        while (label.f158b > 0) {
                            if (label.f167k == null) {
                                label.f167k = new Label();
                            }
                            label = label.f167k;
                        }
                        label.f158b = readUnsignedShort(i2 + 12);
                        readUnsignedShort--;
                        i2 += 4;
                    }
                    attribute2 = attribute;
                    i2 = i15;
                    readUnsignedShort = i14;
                    z3 = z;
                    i4 = i13;
                    i8 = i16;
                    i15 = i11;
                    i14 = i10;
                    i16 = i12;
                    iArr3 = iArr2;
                    iArr4 = iArr;
                }
                attribute2 = attribute;
                i2 = i15;
                readUnsignedShort = i14;
                z3 = z;
                i4 = i13;
                i8 = i16;
                i15 = i11;
                i14 = i10;
                i16 = i12;
                iArr3 = iArr2;
                iArr4 = iArr;
            } else if ("RuntimeVisibleTypeAnnotations".equals(readUTF8)) {
                r7 = m89a(methodVisitor, context, i9 + 8, true);
                i3 = (r7.length == 0 || readByte(r7[0]) < 67) ? -1 : readUnsignedShort(r7[0] + 1);
                readUnsignedShort = i14;
                z3 = z;
                i4 = i13;
                iArr4 = r7;
                i2 = i15;
                i14 = i3;
                i15 = i11;
                attribute2 = attribute;
                i8 = i16;
                iArr3 = iArr2;
                i16 = i12;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(readUTF8)) {
                r7 = m89a(methodVisitor, context, i9 + 8, false);
                i3 = (r7.length == 0 || readByte(r7[0]) < 67) ? -1 : readUnsignedShort(r7[0] + 1);
                readUnsignedShort = i14;
                z3 = z;
                i4 = i13;
                iArr3 = r7;
                i2 = i15;
                i14 = i10;
                i15 = i3;
                iArr4 = iArr;
                attribute2 = attribute;
                i8 = i16;
                i16 = i12;
            } else if ("StackMapTable".equals(readUTF8)) {
                if ((context.f96b & 4) == 0) {
                    readUnsignedShort = i9 + 10;
                    i2 = readInt(i9 + 4);
                    z3 = z;
                    i4 = i13;
                    i16 = i12;
                    i15 = i11;
                    i14 = i10;
                    iArr3 = iArr2;
                    iArr4 = iArr;
                    attribute2 = attribute;
                    i8 = readUnsignedShort(i9 + 8);
                }
                attribute2 = attribute;
                i2 = i15;
                readUnsignedShort = i14;
                z3 = z;
                i4 = i13;
                i8 = i16;
                i15 = i11;
                i14 = i10;
                i16 = i12;
                iArr3 = iArr2;
                iArr4 = iArr;
            } else if ("StackMap".equals(readUTF8)) {
                if ((context.f96b & 4) == 0) {
                    z3 = false;
                    readUnsignedShort = i9 + 10;
                    i2 = readInt(i9 + 4);
                    i4 = i13;
                    i16 = i12;
                    i15 = i11;
                    i14 = i10;
                    iArr3 = iArr2;
                    iArr4 = iArr;
                    Attribute attribute3 = attribute;
                    i8 = readUnsignedShort(i9 + 8);
                    attribute2 = attribute3;
                }
                attribute2 = attribute;
                i2 = i15;
                readUnsignedShort = i14;
                z3 = z;
                i4 = i13;
                i8 = i16;
                i15 = i11;
                i14 = i10;
                i16 = i12;
                iArr3 = iArr2;
                iArr4 = iArr;
            } else {
                Attribute attribute4 = attribute;
                for (i5 = 0; i5 < context.f95a.length; i5++) {
                    if (context.f95a[i5].type.equals(readUTF8)) {
                        attribute2 = context.f95a[i5].read(this, i9 + 8, readInt(i9 + 4), cArr, i6 - 8, labelArr);
                        if (attribute2 != null) {
                            attribute2.f47a = attribute4;
                            attribute4 = attribute2;
                        }
                    }
                }
                attribute2 = attribute4;
                i8 = i16;
                i2 = i15;
                readUnsignedShort = i14;
                z3 = z;
                i4 = i13;
                i16 = i12;
                i15 = i11;
                i14 = i10;
                iArr3 = iArr2;
                iArr4 = iArr;
            }
            i9 += readInt(i9 + 4) + 6;
            readUnsignedShort5--;
            i13 = i4;
            i12 = i16;
            iArr2 = iArr3;
            iArr = iArr4;
            z = z3;
            i16 = i8;
            i11 = i15;
            i10 = i14;
            attribute = attribute2;
            i15 = i2;
            i14 = readUnsignedShort;
        }
        i3 = i9 + 2;
        if (i14 != 0) {
            context.f108o = -1;
            context.f109p = 0;
            context.f110q = 0;
            context.f111r = 0;
            context.f113t = 0;
            context.f112s = new Object[readUnsignedShort4];
            context.f114u = new Object[readUnsignedShort3];
            if (z2) {
                m86a(context);
            }
            for (i3 = i14; i3 < (i14 + i15) - 2; i3++) {
                if (bArr[i3] == (byte) 8) {
                    i2 = readUnsignedShort(i3 + 1);
                    if (i2 >= 0 && i2 < readInt && (bArr[i6 + i2] & 255) == Opcodes.NEW) {
                        readLabel(i2, labelArr);
                    }
                }
            }
            context2 = context;
        } else {
            context2 = null;
        }
        i2 = i16;
        i4 = i14;
        readUnsignedShort = i11;
        i5 = i10;
        readUnsignedShort2 = 0;
        int i18 = 0;
        readUnsignedShort5 = i6;
        while (readUnsignedShort5 < i7) {
            String readUTF82;
            int i19 = readUnsignedShort5 - i6;
            Label label2 = labelArr[i19];
            if (label2 != null) {
                label2.f167k = null;
                methodVisitor.visitLabel(label2);
                if ((context.f96b & 2) == 0 && label2.f158b > 0) {
                    methodVisitor.visitLineNumber(label2.f158b, label2);
                    for (Label label3 = label2.f167k; label3 != null; label3 = label3.f167k) {
                        methodVisitor.visitLineNumber(label3.f158b, label2);
                    }
                }
            }
            Context context3 = context2;
            int i20 = i2;
            i9 = i4;
            while (context3 != null && (context3.f108o == i19 || context3.f108o == -1)) {
                if (context3.f108o != -1) {
                    if (!z || z2) {
                        methodVisitor.visitFrame(-1, context3.f110q, context3.f112s, context3.f113t, context3.f114u);
                    } else {
                        methodVisitor.visitFrame(context3.f109p, context3.f111r, context3.f112s, context3.f113t, context3.f114u);
                    }
                }
                if (i20 > 0) {
                    i20--;
                    i9 = m77a(i9, z, z2, context3);
                } else {
                    context3 = null;
                }
            }
            i16 = bArr[readUnsignedShort5] & 255;
            Label[] labelArr2;
            switch (ClassWriter.f55a[i16]) {
                case (byte) 0:
                    methodVisitor.visitInsn(i16);
                    i4 = i5;
                    i16 = i18;
                    i15 = readUnsignedShort5 + 1;
                    break;
                case (byte) 1:
                    methodVisitor.visitIntInsn(i16, bArr[readUnsignedShort5 + 1]);
                    i4 = i5;
                    i16 = i18;
                    i15 = readUnsignedShort5 + 2;
                    break;
                case (byte) 2:
                    methodVisitor.visitIntInsn(i16, readShort(readUnsignedShort5 + 1));
                    i4 = i5;
                    i16 = i18;
                    i15 = readUnsignedShort5 + 3;
                    break;
                case (byte) 3:
                    methodVisitor.visitVarInsn(i16, bArr[readUnsignedShort5 + 1] & 255);
                    i4 = i5;
                    i16 = i18;
                    i15 = readUnsignedShort5 + 2;
                    break;
                case (byte) 4:
                    if (i16 > 54) {
                        i3 = i16 - 59;
                        methodVisitor.visitVarInsn((i3 >> 2) + 54, i3 & 3);
                    } else {
                        i3 = i16 - 26;
                        methodVisitor.visitVarInsn((i3 >> 2) + 21, i3 & 3);
                    }
                    i4 = i5;
                    i16 = i18;
                    i15 = readUnsignedShort5 + 1;
                    break;
                case (byte) 5:
                    methodVisitor.visitTypeInsn(i16, readClass(readUnsignedShort5 + 1, cArr));
                    i4 = i5;
                    i16 = i18;
                    i15 = readUnsignedShort5 + 3;
                    break;
                case (byte) 6:
                case (byte) 7:
                    i3 = this.f51a[readUnsignedShort(readUnsignedShort5 + 1)];
                    boolean z4 = bArr[i3 + -1] == (byte) 11;
                    String readClass = readClass(i3, cArr);
                    i3 = this.f51a[readUnsignedShort(i3 + 2)];
                    readUTF82 = readUTF8(i3, cArr);
                    String readUTF83 = readUTF8(i3 + 2, cArr);
                    if (i16 < Opcodes.INVOKEVIRTUAL) {
                        methodVisitor.visitFieldInsn(i16, readClass, readUTF82, readUTF83);
                    } else {
                        methodVisitor.visitMethodInsn(i16, readClass, readUTF82, readUTF83, z4);
                    }
                    if (i16 != Opcodes.INVOKEINTERFACE) {
                        i4 = i5;
                        i16 = i18;
                        i15 = readUnsignedShort5 + 3;
                        break;
                    }
                    i4 = i5;
                    i16 = i18;
                    i15 = readUnsignedShort5 + 5;
                    break;
                case (byte) 8:
                    i16 = this.f51a[readUnsignedShort(readUnsignedShort5 + 1)];
                    i2 = context.f98d[readUnsignedShort(i16)];
                    Handle handle = (Handle) readConst(readUnsignedShort(i2), cArr);
                    i15 = readUnsignedShort(i2 + 2);
                    Object[] objArr = new Object[i15];
                    i4 = i2 + 4;
                    for (i2 = 0; i2 < i15; i2++) {
                        objArr[i2] = readConst(readUnsignedShort(i4), cArr);
                        i4 += 2;
                    }
                    i2 = this.f51a[readUnsignedShort(i16 + 2)];
                    methodVisitor.visitInvokeDynamicInsn(readUTF8(i2, cArr), readUTF8(i2 + 2, cArr), handle, objArr);
                    i4 = i5;
                    i16 = i18;
                    i15 = readUnsignedShort5 + 5;
                    break;
                case (byte) 9:
                    methodVisitor.visitJumpInsn(i16, labelArr[readShort(readUnsignedShort5 + 1) + i19]);
                    i4 = i5;
                    i16 = i18;
                    i15 = readUnsignedShort5 + 3;
                    break;
                case (byte) 10:
                    methodVisitor.visitJumpInsn(i16 - 33, labelArr[readInt(readUnsignedShort5 + 1) + i19]);
                    i4 = i5;
                    i16 = i18;
                    i15 = readUnsignedShort5 + 5;
                    break;
                case (byte) 11:
                    methodVisitor.visitLdcInsn(readConst(bArr[readUnsignedShort5 + 1] & 255, cArr));
                    i4 = i5;
                    i16 = i18;
                    i15 = readUnsignedShort5 + 2;
                    break;
                case (byte) 12:
                    methodVisitor.visitLdcInsn(readConst(readUnsignedShort(readUnsignedShort5 + 1), cArr));
                    i4 = i5;
                    i16 = i18;
                    i15 = readUnsignedShort5 + 3;
                    break;
                case (byte) 13:
                    methodVisitor.visitIincInsn(bArr[readUnsignedShort5 + 1] & 255, bArr[readUnsignedShort5 + 2]);
                    i4 = i5;
                    i16 = i18;
                    i15 = readUnsignedShort5 + 3;
                    break;
                case (byte) 14:
                    i3 = (readUnsignedShort5 + 4) - (i19 & 3);
                    i4 = i19 + readInt(i3);
                    i16 = readInt(i3 + 4);
                    i15 = readInt(i3 + 8);
                    labelArr2 = new Label[((i15 - i16) + 1)];
                    i2 = i3 + 12;
                    for (i3 = 0; i3 < labelArr2.length; i3++) {
                        labelArr2[i3] = labelArr[readInt(i2) + i19];
                        i2 += 4;
                    }
                    methodVisitor.visitTableSwitchInsn(i16, i15, labelArr[i4], labelArr2);
                    i4 = i5;
                    i16 = i18;
                    i15 = i2;
                    break;
                case (byte) 15:
                    i3 = (readUnsignedShort5 + 4) - (i19 & 3);
                    i4 = i19 + readInt(i3);
                    i16 = readInt(i3 + 4);
                    int[] iArr5 = new int[i16];
                    labelArr2 = new Label[i16];
                    i2 = i3 + 8;
                    for (i3 = 0; i3 < i16; i3++) {
                        iArr5[i3] = readInt(i2);
                        labelArr2[i3] = labelArr[readInt(i2 + 4) + i19];
                        i2 += 8;
                    }
                    methodVisitor.visitLookupSwitchInsn(labelArr[i4], iArr5, labelArr2);
                    i4 = i5;
                    i16 = i18;
                    i15 = i2;
                    break;
                case (byte) 17:
                    i3 = bArr[readUnsignedShort5 + 1] & 255;
                    if (i3 != Opcodes.IINC) {
                        methodVisitor.visitVarInsn(i3, readUnsignedShort(readUnsignedShort5 + 2));
                        i4 = i5;
                        i16 = i18;
                        i15 = readUnsignedShort5 + 4;
                        break;
                    }
                    methodVisitor.visitIincInsn(readUnsignedShort(readUnsignedShort5 + 2), readShort(readUnsignedShort5 + 4));
                    i4 = i5;
                    i16 = i18;
                    i15 = readUnsignedShort5 + 6;
                    break;
                default:
                    methodVisitor.visitMultiANewArrayInsn(readClass(readUnsignedShort5 + 1, cArr), bArr[readUnsignedShort5 + 3] & 255);
                    i4 = i5;
                    i16 = i18;
                    i15 = readUnsignedShort5 + 4;
                    break;
            }
            while (iArr != null && i16 < iArr.length && i4 <= i19) {
                if (i4 == i19) {
                    i3 = m81a(context, iArr[i16]);
                    m79a(i3 + 2, cArr, true, methodVisitor.visitInsnAnnotation(context.f103i, context.f104j, readUTF8(i3, cArr), true));
                }
                i2 = i16 + 1;
                i3 = (i2 >= iArr.length || readByte(iArr[i2]) < 67) ? -1 : readUnsignedShort(iArr[i2] + 1);
                i4 = i3;
                i16 = i2;
            }
            i3 = readUnsignedShort;
            i2 = readUnsignedShort2;
            while (iArr2 != null && i2 < iArr2.length && i3 <= i19) {
                if (i3 == i19) {
                    i3 = m81a(context, iArr2[i2]);
                    m79a(i3 + 2, cArr, true, methodVisitor.visitInsnAnnotation(context.f103i, context.f104j, readUTF8(i3, cArr), false));
                }
                i2++;
                i3 = (i2 >= iArr2.length || readByte(iArr2[i2]) < 67) ? -1 : readUnsignedShort(iArr2[i2] + 1);
            }
            readUnsignedShort = i3;
            i5 = i4;
            readUnsignedShort2 = i2;
            i18 = i16;
            readUnsignedShort5 = i15;
            i4 = i9;
            context2 = context3;
            i2 = i20;
        }
        if (labelArr[readInt] != null) {
            methodVisitor.visitLabel(labelArr[readInt]);
        }
        if ((context.f96b & 2) == 0 && i12 != 0) {
            int[] iArr6 = null;
            if (i13 != 0) {
                readUnsignedShort = i13 + 2;
                iArr6 = new int[(readUnsignedShort(i13) * 3)];
                i2 = iArr6.length;
                while (i2 > 0) {
                    i2--;
                    iArr6[i2] = readUnsignedShort + 6;
                    i2--;
                    iArr6[i2] = readUnsignedShort(readUnsignedShort + 8);
                    i2--;
                    iArr6[i2] = readUnsignedShort(readUnsignedShort);
                    readUnsignedShort += 10;
                }
            }
            readUnsignedShort2 = i12 + 2;
            for (readUnsignedShort = readUnsignedShort(i12); readUnsignedShort > 0; readUnsignedShort--) {
                i4 = readUnsignedShort(readUnsignedShort2);
                i10 = readUnsignedShort(readUnsignedShort2 + 2);
                int readUnsignedShort6 = readUnsignedShort(readUnsignedShort2 + 8);
                readUTF82 = null;
                if (iArr6 != null) {
                    i2 = 0;
                    while (i2 < iArr6.length) {
                        if (iArr6[i2] == i4 && iArr6[i2 + 1] == readUnsignedShort6) {
                            readUTF82 = readUTF8(iArr6[i2 + 2], cArr);
                        } else {
                            i2 += 3;
                        }
                    }
                }
                methodVisitor.visitLocalVariable(readUTF8(readUnsignedShort2 + 4, cArr), readUTF8(readUnsignedShort2 + 6, cArr), readUTF82, labelArr[i4], labelArr[i4 + i10], readUnsignedShort6);
                readUnsignedShort2 += 10;
            }
        }
        if (iArr != null) {
            for (i3 = 0; i3 < iArr.length; i3++) {
                if ((readByte(iArr[i3]) >> 1) == 32) {
                    i2 = m81a(context, iArr[i3]);
                    m79a(i2 + 2, cArr, true, methodVisitor.visitLocalVariableAnnotation(context.f103i, context.f104j, context.f105l, context.f106m, context.f107n, readUTF8(i2, cArr), true));
                }
            }
        }
        if (iArr2 != null) {
            for (i3 = 0; i3 < iArr2.length; i3++) {
                if ((readByte(iArr2[i3]) >> 1) == 32) {
                    i2 = m81a(context, iArr2[i3]);
                    m79a(i2 + 2, cArr, true, methodVisitor.visitLocalVariableAnnotation(context.f103i, context.f104j, context.f105l, context.f106m, context.f107n, readUTF8(i2, cArr), false));
                }
            }
        }
        while (attribute != null) {
            attribute2 = attribute.f47a;
            attribute.f47a = null;
            methodVisitor.visitAttribute(attribute);
            attribute = attribute2;
        }
        methodVisitor.visitMaxs(readUnsignedShort3, readUnsignedShort4);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] m88a(java.io.InputStream r6, boolean r7) throws java.io.IOException {
        /*
        r0 = 0;
        if (r6 != 0) goto L_0x000b;
    L_0x0003:
        r0 = new java.io.IOException;
        r1 = "Class not found";
        r0.<init>(r1);
        throw r0;
    L_0x000b:
        r1 = r6.available();	 Catch:{ all -> 0x004e }
        r1 = new byte[r1];	 Catch:{ all -> 0x004e }
        r2 = r0;
    L_0x0012:
        r0 = r1.length;	 Catch:{ all -> 0x004e }
        r0 = r0 - r2;
        r0 = r6.read(r1, r2, r0);	 Catch:{ all -> 0x004e }
        r3 = -1;
        if (r0 != r3) goto L_0x002b;
    L_0x001b:
        r0 = r1.length;	 Catch:{ all -> 0x004e }
        if (r2 >= r0) goto L_0x0057;
    L_0x001e:
        r0 = new byte[r2];	 Catch:{ all -> 0x004e }
        r3 = 0;
        r4 = 0;
        java.lang.System.arraycopy(r1, r3, r0, r4, r2);	 Catch:{ all -> 0x004e }
    L_0x0025:
        if (r7 == 0) goto L_0x002a;
    L_0x0027:
        r6.close();
    L_0x002a:
        return r0;
    L_0x002b:
        r2 = r2 + r0;
        r0 = r1.length;	 Catch:{ all -> 0x004e }
        if (r2 != r0) goto L_0x0055;
    L_0x002f:
        r4 = r6.read();	 Catch:{ all -> 0x004e }
        if (r4 >= 0) goto L_0x003c;
    L_0x0035:
        if (r7 == 0) goto L_0x003a;
    L_0x0037:
        r6.close();
    L_0x003a:
        r0 = r1;
        goto L_0x002a;
    L_0x003c:
        r0 = r1.length;	 Catch:{ all -> 0x004e }
        r0 = r0 + 1000;
        r3 = new byte[r0];	 Catch:{ all -> 0x004e }
        r0 = 0;
        r5 = 0;
        java.lang.System.arraycopy(r1, r0, r3, r5, r2);	 Catch:{ all -> 0x004e }
        r0 = r2 + 1;
        r1 = (byte) r4;	 Catch:{ all -> 0x004e }
        r3[r2] = r1;	 Catch:{ all -> 0x004e }
        r1 = r3;
    L_0x004c:
        r2 = r0;
        goto L_0x0012;
    L_0x004e:
        r0 = move-exception;
        if (r7 == 0) goto L_0x0054;
    L_0x0051:
        r6.close();
    L_0x0054:
        throw r0;
    L_0x0055:
        r0 = r2;
        goto L_0x004c;
    L_0x0057:
        r0 = r1;
        goto L_0x0025;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.objectweb.asm.ClassReader.a(java.io.InputStream, boolean):byte[]");
    }

    private int[] m89a(MethodVisitor methodVisitor, Context context, int i, boolean z) {
        char[] cArr = context.f97c;
        int[] iArr = new int[readUnsignedShort(i)];
        int i2 = i + 2;
        for (int i3 = 0; i3 < iArr.length; i3++) {
            int readUnsignedShort;
            iArr[i3] = i2;
            int readInt = readInt(i2);
            switch (readInt >>> 24) {
                case 0:
                case 1:
                case 22:
                    i2 += 2;
                    break;
                case 19:
                case 20:
                case 21:
                    i2++;
                    break;
                case 64:
                case 65:
                    int i4 = i2;
                    for (i2 = readUnsignedShort(i2 + 1); i2 > 0; i2--) {
                        readUnsignedShort = readUnsignedShort(i4 + 3);
                        int readUnsignedShort2 = readUnsignedShort(i4 + 5);
                        readLabel(readUnsignedShort, context.f102h);
                        readLabel(readUnsignedShort + readUnsignedShort2, context.f102h);
                        i4 += 6;
                    }
                    i2 = i4 + 3;
                    break;
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                    i2 += 4;
                    break;
                default:
                    i2 += 3;
                    break;
            }
            readUnsignedShort = readByte(i2);
            if ((readInt >>> 24) == 66) {
                TypePath typePath = readUnsignedShort == 0 ? null : new TypePath(this.f52b, i2);
                i2 += (readUnsignedShort * 2) + 1;
                i2 = m79a(i2 + 2, cArr, true, methodVisitor.visitTryCatchAnnotation(readInt, typePath, readUTF8(i2, cArr), z));
            } else {
                i2 = m79a((i2 + 3) + (readUnsignedShort * 2), cArr, true, null);
            }
        }
        return iArr;
    }

    private int m90b(ClassVisitor classVisitor, Context context, int i) {
        int i2;
        int i3;
        int i4;
        Attribute attribute;
        int i5;
        char[] cArr = context.f97c;
        context.f99e = readUnsignedShort(i);
        context.f100f = readUTF8(i + 2, cArr);
        context.f101g = readUTF8(i + 4, cArr);
        int i6 = i + 6;
        int i7 = 0;
        int i8 = 0;
        String[] strArr = null;
        String str = null;
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        int i15 = 0;
        int i16 = 0;
        Attribute attribute2 = null;
        int readUnsignedShort = readUnsignedShort(i6);
        int i17 = i6;
        while (readUnsignedShort > 0) {
            int i18;
            int i19;
            int i20;
            int i21;
            int i22;
            int i23;
            String readUTF8 = readUTF8(i17 + 2, cArr);
            if ("Code".equals(readUTF8)) {
                if ((context.f96b & 1) == 0) {
                    i18 = i16;
                    i2 = i15;
                    i3 = i14;
                    i4 = i13;
                    i19 = i12;
                    i20 = i11;
                    i21 = i10;
                    i22 = i9;
                    i16 = i17 + 8;
                    attribute = attribute2;
                    i23 = i8;
                }
                attribute = attribute2;
                i18 = i16;
                i2 = i15;
                i3 = i14;
                i4 = i13;
                i19 = i12;
                i20 = i11;
                i21 = i10;
                i22 = i9;
                i23 = i8;
                i16 = i7;
            } else if ("Exceptions".equals(readUTF8)) {
                strArr = new String[readUnsignedShort(i17 + 8)];
                i5 = i17 + 10;
                i18 = 0;
                while (i18 < strArr.length) {
                    strArr[i18] = readClass(i5, cArr);
                    i18++;
                    i5 += 2;
                }
                i18 = i16;
                i2 = i15;
                i3 = i14;
                i4 = i13;
                i19 = i12;
                i20 = i11;
                i21 = i10;
                i22 = i9;
                i16 = i7;
                int i24 = i5;
                attribute = attribute2;
                i23 = i24;
            } else if ("Signature".equals(readUTF8)) {
                str = readUTF8(i17 + 8, cArr);
                attribute = attribute2;
                i18 = i16;
                i2 = i15;
                i3 = i14;
                i4 = i13;
                i19 = i12;
                i20 = i11;
                i21 = i10;
                i22 = i9;
                i23 = i8;
                i16 = i7;
            } else if ("Deprecated".equals(readUTF8)) {
                context.f99e |= 131072;
                attribute = attribute2;
                i18 = i16;
                i2 = i15;
                i3 = i14;
                i4 = i13;
                i19 = i12;
                i20 = i11;
                i21 = i10;
                i22 = i9;
                i23 = i8;
                i16 = i7;
            } else if ("RuntimeVisibleAnnotations".equals(readUTF8)) {
                i18 = i16;
                i2 = i15;
                i3 = i14;
                i4 = i13;
                i19 = i12;
                i20 = i11;
                i21 = i17 + 8;
                i22 = i9;
                attribute = attribute2;
                i16 = i7;
                i23 = i8;
            } else if ("RuntimeVisibleTypeAnnotations".equals(readUTF8)) {
                i18 = i16;
                i2 = i15;
                i3 = i14;
                i4 = i13;
                i19 = i17 + 8;
                i20 = i11;
                i21 = i10;
                i22 = i9;
                attribute = attribute2;
                i16 = i7;
                i23 = i8;
            } else if ("AnnotationDefault".equals(readUTF8)) {
                i18 = i16;
                i2 = i15;
                i3 = i17 + 8;
                i4 = i13;
                i19 = i12;
                i20 = i11;
                i21 = i10;
                i22 = i9;
                attribute = attribute2;
                i16 = i7;
                i23 = i8;
            } else if ("Synthetic".equals(readUTF8)) {
                context.f99e |= 266240;
                attribute = attribute2;
                i18 = i16;
                i2 = i15;
                i3 = i14;
                i4 = i13;
                i19 = i12;
                i20 = i11;
                i21 = i10;
                i22 = i9;
                i23 = i8;
                i16 = i7;
            } else if ("RuntimeInvisibleAnnotations".equals(readUTF8)) {
                i18 = i16;
                i2 = i15;
                i3 = i14;
                i4 = i13;
                i19 = i12;
                i20 = i17 + 8;
                i21 = i10;
                i22 = i9;
                attribute = attribute2;
                i16 = i7;
                i23 = i8;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(readUTF8)) {
                i18 = i16;
                i2 = i15;
                i3 = i14;
                i4 = i17 + 8;
                i19 = i12;
                i20 = i11;
                i21 = i10;
                i22 = i9;
                attribute = attribute2;
                i16 = i7;
                i23 = i8;
            } else if ("RuntimeVisibleParameterAnnotations".equals(readUTF8)) {
                i18 = i16;
                i2 = i17 + 8;
                i3 = i14;
                i4 = i13;
                i19 = i12;
                i20 = i11;
                i21 = i10;
                i22 = i9;
                attribute = attribute2;
                i16 = i7;
                i23 = i8;
            } else if ("RuntimeInvisibleParameterAnnotations".equals(readUTF8)) {
                i18 = i17 + 8;
                i2 = i15;
                i3 = i14;
                i4 = i13;
                i19 = i12;
                i20 = i11;
                i21 = i10;
                i22 = i9;
                i16 = i7;
                attribute = attribute2;
                i23 = i8;
            } else if ("MethodParameters".equals(readUTF8)) {
                i18 = i16;
                i2 = i15;
                i3 = i14;
                i4 = i13;
                i19 = i12;
                i20 = i11;
                i21 = i10;
                i22 = i17 + 8;
                attribute = attribute2;
                i16 = i7;
                i23 = i8;
            } else {
                attribute = m84a(context.f95a, readUTF8, i17 + 8, readInt(i17 + 4), cArr, -1, null);
                if (attribute != null) {
                    attribute.f47a = attribute2;
                    i18 = i16;
                    i2 = i15;
                    i3 = i14;
                    i4 = i13;
                    i19 = i12;
                    i20 = i11;
                    i21 = i10;
                    i22 = i9;
                    i23 = i8;
                    i16 = i7;
                }
                attribute = attribute2;
                i18 = i16;
                i2 = i15;
                i3 = i14;
                i4 = i13;
                i19 = i12;
                i20 = i11;
                i21 = i10;
                i22 = i9;
                i23 = i8;
                i16 = i7;
            }
            readUnsignedShort--;
            i13 = i4;
            i12 = i19;
            i11 = i20;
            i10 = i21;
            i9 = i22;
            i8 = i23;
            i7 = i16;
            i17 += readInt(i17 + 4) + 6;
            attribute2 = attribute;
            i16 = i18;
            i15 = i2;
            i14 = i3;
        }
        i3 = i17 + 2;
        MethodVisitor visitMethod = classVisitor.visitMethod(context.f99e, context.f100f, context.f101g, str, strArr);
        if (visitMethod == null) {
            return i3;
        }
        if (visitMethod instanceof MethodWriter) {
            MethodWriter methodWriter = (MethodWriter) visitMethod;
            if (methodWriter.f195b.f67M == this && str == methodWriter.f200g) {
                Object obj = null;
                if (strArr == null) {
                    obj = methodWriter.f203j == 0 ? 1 : null;
                } else if (strArr.length == methodWriter.f203j) {
                    obj = 1;
                    for (i4 = strArr.length - 1; i4 >= 0; i4--) {
                        i8 -= 2;
                        if (methodWriter.f204k[i4] != readUnsignedShort(i8)) {
                            obj = null;
                            break;
                        }
                    }
                }
                if (obj != null) {
                    methodWriter.f201h = i6;
                    methodWriter.f202i = i3 - i6;
                    return i3;
                }
            }
        }
        if (i9 != 0) {
            i2 = this.f52b[i9] & 255;
            i5 = i9 + 1;
            while (i2 > 0) {
                visitMethod.visitParameter(readUTF8(i5, cArr), readUnsignedShort(i5 + 2));
                i2--;
                i5 += 4;
            }
        }
        if (i14 != 0) {
            AnnotationVisitor visitAnnotationDefault = visitMethod.visitAnnotationDefault();
            m78a(i14, cArr, null, visitAnnotationDefault);
            if (visitAnnotationDefault != null) {
                visitAnnotationDefault.visitEnd();
            }
        }
        if (i10 != 0) {
            i5 = i10 + 2;
            for (i2 = readUnsignedShort(i10); i2 > 0; i2--) {
                i5 = m79a(i5 + 2, cArr, true, visitMethod.visitAnnotation(readUTF8(i5, cArr), true));
            }
        }
        if (i11 != 0) {
            i5 = i11 + 2;
            for (i2 = readUnsignedShort(i11); i2 > 0; i2--) {
                i5 = m79a(i5 + 2, cArr, true, visitMethod.visitAnnotation(readUTF8(i5, cArr), false));
            }
        }
        if (i12 != 0) {
            i5 = i12 + 2;
            for (i2 = readUnsignedShort(i12); i2 > 0; i2--) {
                i5 = m81a(context, i5);
                i5 = m79a(i5 + 2, cArr, true, visitMethod.visitTypeAnnotation(context.f103i, context.f104j, readUTF8(i5, cArr), true));
            }
        }
        if (i13 != 0) {
            i5 = i13 + 2;
            for (i2 = readUnsignedShort(i13); i2 > 0; i2--) {
                i5 = m81a(context, i5);
                i5 = m79a(i5 + 2, cArr, true, visitMethod.visitTypeAnnotation(context.f103i, context.f104j, readUTF8(i5, cArr), false));
            }
        }
        if (i15 != 0) {
            m91b(visitMethod, context, i15, true);
        }
        if (i16 != 0) {
            m91b(visitMethod, context, i16, false);
        }
        while (attribute2 != null) {
            attribute = attribute2.f47a;
            attribute2.f47a = null;
            visitMethod.visitAttribute(attribute2);
            attribute2 = attribute;
        }
        if (i7 != 0) {
            visitMethod.visitCode();
            m87a(visitMethod, context, i7);
        }
        visitMethod.visitEnd();
        return i3;
    }

    private void m91b(MethodVisitor methodVisitor, Context context, int i, boolean z) {
        int i2 = i + 1;
        int i3 = this.f52b[i] & 255;
        int length = Type.getArgumentTypes(context.f101g).length - i3;
        int i4 = 0;
        while (i4 < length) {
            AnnotationVisitor visitParameterAnnotation = methodVisitor.visitParameterAnnotation(i4, "Ljava/lang/Synthetic;", false);
            if (visitParameterAnnotation != null) {
                visitParameterAnnotation.visitEnd();
            }
            i4++;
        }
        char[] cArr = context.f97c;
        for (int i5 = i4; i5 < i3 + length; i5++) {
            i2 += 2;
            for (i4 = readUnsignedShort(i2); i4 > 0; i4--) {
                i2 = m79a(i2 + 2, cArr, true, methodVisitor.visitParameterAnnotation(i5, readUTF8(i2, cArr), z));
            }
        }
    }

    void m92a(ClassWriter classWriter) {
        int i;
        char[] cArr = new char[this.f54d];
        int length = this.f51a.length;
        Item[] itemArr = new Item[length];
        int i2 = 1;
        while (i2 < length) {
            i = this.f51a[i2];
            byte b = this.f52b[i - 1];
            Item item = new Item(i2);
            int i3;
            int i4;
            switch (b) {
                case (byte) 1:
                    String str = this.f53c[i2];
                    if (str == null) {
                        i = this.f51a[i2];
                        String[] strArr = this.f53c;
                        str = m83a(i + 2, readUnsignedShort(i), cArr);
                        strArr[i2] = str;
                    }
                    item.m135a(b, str, null, null);
                    i = i2;
                    break;
                case (byte) 3:
                    item.m133a(readInt(i));
                    i = i2;
                    break;
                case (byte) 4:
                    item.m132a(Float.intBitsToFloat(readInt(i)));
                    i = i2;
                    break;
                case (byte) 5:
                    item.m136a(readLong(i));
                    i = i2 + 1;
                    break;
                case (byte) 6:
                    item.m131a(Double.longBitsToDouble(readLong(i)));
                    i = i2 + 1;
                    break;
                case (byte) 9:
                case (byte) 10:
                case (byte) 11:
                    i3 = this.f51a[readUnsignedShort(i + 2)];
                    item.m135a(b, readClass(i, cArr), readUTF8(i3, cArr), readUTF8(i3 + 2, cArr));
                    i = i2;
                    break;
                case (byte) 12:
                    item.m135a(b, readUTF8(i, cArr), readUTF8(i + 2, cArr), null);
                    i = i2;
                    break;
                case (byte) 15:
                    i4 = this.f51a[readUnsignedShort(i + 1)];
                    i3 = this.f51a[readUnsignedShort(i4 + 2)];
                    item.m135a(readByte(i) + 20, readClass(i4, cArr), readUTF8(i3, cArr), readUTF8(i3 + 2, cArr));
                    i = i2;
                    break;
                case (byte) 18:
                    if (classWriter.f56A == null) {
                        m85a(classWriter, itemArr, cArr);
                    }
                    i4 = this.f51a[readUnsignedShort(i + 2)];
                    item.m137a(readUTF8(i4, cArr), readUTF8(i4 + 2, cArr), readUnsignedShort(i));
                    i = i2;
                    break;
                default:
                    item.m135a(b, readUTF8(i, cArr), null, null);
                    i = i2;
                    break;
            }
            i2 = item.f155j % itemArr.length;
            item.f156k = itemArr[i2];
            itemArr[i2] = item;
            i2 = i + 1;
        }
        i = this.f51a[1] - 1;
        classWriter.f72d.putByteArray(this.f52b, i, this.header - i);
        classWriter.f73e = itemArr;
        classWriter.f74f = (int) (0.75d * ((double) length));
        classWriter.f71c = length;
    }

    public void accept(ClassVisitor classVisitor, int i) {
        accept(classVisitor, new Attribute[0], i);
    }

    public void accept(ClassVisitor classVisitor, Attribute[] attributeArr, int i) {
        int i2 = this.header;
        char[] cArr = new char[this.f54d];
        Context context = new Context();
        context.f95a = attributeArr;
        context.f96b = i;
        context.f97c = cArr;
        int readUnsignedShort = readUnsignedShort(i2);
        String readClass = readClass(i2 + 2, cArr);
        String readClass2 = readClass(i2 + 4, cArr);
        String[] strArr = new String[readUnsignedShort(i2 + 6)];
        int i3 = i2 + 8;
        for (i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = readClass(i3, cArr);
            i3 += 2;
        }
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        Attribute attribute = null;
        i3 = m76a();
        int readUnsignedShort2 = readUnsignedShort(i3);
        int i9 = i3;
        while (readUnsignedShort2 > 0) {
            int i10;
            int i11;
            int i12;
            int i13;
            String str7;
            String readUTF8;
            String str8;
            Attribute attribute2;
            String str9;
            String str10;
            String readUTF82 = readUTF8(i9 + 2, cArr);
            if ("SourceFile".equals(readUTF82)) {
                i3 = i8;
                i10 = i7;
                i11 = i6;
                i12 = i5;
                i13 = i4;
                str7 = str6;
                readUTF8 = readUTF8(i9 + 8, cArr);
                str8 = str;
                i6 = readUnsignedShort;
                attribute2 = attribute;
                str9 = str5;
                str = str4;
                str10 = str3;
            } else if ("InnerClasses".equals(readUTF82)) {
                i3 = i9 + 8;
                i10 = i7;
                i11 = i6;
                i12 = i5;
                i13 = i4;
                str7 = str6;
                readUTF8 = str2;
                attribute2 = attribute;
                str8 = str;
                i6 = readUnsignedShort;
                str9 = str5;
                str = str4;
                str10 = str3;
            } else if ("EnclosingMethod".equals(readUTF82)) {
                String readUTF83;
                String readUTF84;
                readUTF82 = readClass(i9 + 8, cArr);
                i2 = readUnsignedShort(i9 + 10);
                if (i2 != 0) {
                    readUTF83 = readUTF8(this.f51a[i2], cArr);
                    readUTF84 = readUTF8(this.f51a[i2] + 2, cArr);
                } else {
                    readUTF84 = str6;
                    readUTF83 = str5;
                }
                i11 = i6;
                i12 = i5;
                i13 = i4;
                str7 = readUTF84;
                attribute2 = attribute;
                i6 = readUnsignedShort;
                str9 = readUTF83;
                str10 = str3;
                i3 = i8;
                readUTF8 = str2;
                r31 = readUTF82;
                i10 = i7;
                str8 = str;
                str = r31;
            } else if ("Signature".equals(readUTF82)) {
                attribute2 = attribute;
                i3 = i8;
                i10 = i7;
                i11 = i6;
                i12 = i5;
                i13 = i4;
                str7 = str6;
                str10 = str3;
                readUTF8 = str2;
                str8 = readUTF8(i9 + 8, cArr);
                i6 = readUnsignedShort;
                str9 = str5;
                str = str4;
            } else if ("RuntimeVisibleAnnotations".equals(readUTF82)) {
                i3 = i8;
                i10 = i7;
                i11 = i6;
                i12 = i5;
                i13 = i9 + 8;
                str7 = str6;
                attribute2 = attribute;
                readUTF8 = str2;
                str8 = str;
                i6 = readUnsignedShort;
                str9 = str5;
                str = str4;
                str10 = str3;
            } else if ("RuntimeVisibleTypeAnnotations".equals(readUTF82)) {
                i3 = i8;
                i10 = i7;
                i11 = i9 + 8;
                i12 = i5;
                i13 = i4;
                str7 = str6;
                i6 = readUnsignedShort;
                str9 = str5;
                readUTF8 = str2;
                str8 = str;
                attribute2 = attribute;
                str10 = str3;
                str = str4;
            } else if ("Deprecated".equals(readUTF82)) {
                attribute2 = attribute;
                i3 = i8;
                i10 = i7;
                i11 = i6;
                i12 = i5;
                i13 = i4;
                str7 = str6;
                str10 = str3;
                readUTF8 = str2;
                str8 = str;
                i6 = readUnsignedShort | 131072;
                str9 = str5;
                str = str4;
            } else if ("Synthetic".equals(readUTF82)) {
                attribute2 = attribute;
                i3 = i8;
                i10 = i7;
                i11 = i6;
                i12 = i5;
                i13 = i4;
                str7 = str6;
                str10 = str3;
                readUTF8 = str2;
                str8 = str;
                i6 = readUnsignedShort | 266240;
                str9 = str5;
                str = str4;
            } else if ("SourceDebugExtension".equals(readUTF82)) {
                i2 = readInt(i9 + 4);
                i3 = i8;
                i10 = i7;
                i11 = i6;
                i12 = i5;
                i13 = i4;
                str7 = str6;
                readUTF8 = str2;
                str8 = str;
                i6 = readUnsignedShort;
                str9 = str5;
                str = str4;
                r31 = m83a(i9 + 8, i2, new char[i2]);
                attribute2 = attribute;
                str10 = r31;
            } else if ("RuntimeInvisibleAnnotations".equals(readUTF82)) {
                i3 = i8;
                i10 = i7;
                i11 = i6;
                i12 = i9 + 8;
                i13 = i4;
                str7 = str6;
                attribute2 = attribute;
                readUTF8 = str2;
                str8 = str;
                i6 = readUnsignedShort;
                str9 = str5;
                str = str4;
                str10 = str3;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(readUTF82)) {
                i3 = i8;
                i10 = i9 + 8;
                i11 = i6;
                i12 = i5;
                i13 = i4;
                str7 = str6;
                str8 = str;
                str = str4;
                readUTF8 = str2;
                attribute2 = attribute;
                i6 = readUnsignedShort;
                str10 = str3;
                str9 = str5;
            } else if ("BootstrapMethods".equals(readUTF82)) {
                int[] iArr = new int[readUnsignedShort(i9 + 8)];
                i2 = i9 + 10;
                for (i3 = 0; i3 < iArr.length; i3++) {
                    iArr[i3] = i2;
                    i2 += (readUnsignedShort(i2 + 2) + 2) << 1;
                }
                context.f98d = iArr;
                attribute2 = attribute;
                i3 = i8;
                i10 = i7;
                i11 = i6;
                i12 = i5;
                i13 = i4;
                str7 = str6;
                str10 = str3;
                readUTF8 = str2;
                str8 = str;
                i6 = readUnsignedShort;
                str9 = str5;
                str = str4;
            } else {
                attribute2 = m84a(attributeArr, readUTF82, i9 + 8, readInt(i9 + 4), cArr, -1, null);
                if (attribute2 != null) {
                    attribute2.f47a = attribute;
                    i3 = i8;
                    i10 = i7;
                    i11 = i6;
                    i12 = i5;
                    i13 = i4;
                    str7 = str6;
                    str10 = str3;
                    readUTF8 = str2;
                    str8 = str;
                    i6 = readUnsignedShort;
                    str9 = str5;
                    str = str4;
                } else {
                    attribute2 = attribute;
                    i3 = i8;
                    i10 = i7;
                    i11 = i6;
                    i12 = i5;
                    i13 = i4;
                    str7 = str6;
                    str10 = str3;
                    readUTF8 = str2;
                    str8 = str;
                    i6 = readUnsignedShort;
                    str9 = str5;
                    str = str4;
                }
            }
            readUnsignedShort2--;
            str6 = str7;
            str5 = str9;
            str4 = str;
            str3 = str10;
            str2 = readUTF8;
            i9 += readInt(i9 + 4) + 6;
            attribute = attribute2;
            i8 = i3;
            i5 = i12;
            str = str8;
            readUnsignedShort = i6;
            i4 = i13;
            i7 = i10;
            i6 = i11;
        }
        classVisitor.visit(readInt(this.f51a[1] - 7), readUnsignedShort, readClass, str, readClass2, strArr);
        if ((i & 2) == 0 && !(str2 == null && str3 == null)) {
            classVisitor.visitSource(str2, str3);
        }
        if (str4 != null) {
            classVisitor.visitOuterClass(str4, str5, str6);
        }
        if (i4 != 0) {
            i2 = i4 + 2;
            for (i3 = readUnsignedShort(i4); i3 > 0; i3--) {
                i2 = m79a(i2 + 2, cArr, true, classVisitor.visitAnnotation(readUTF8(i2, cArr), true));
            }
        }
        if (i5 != 0) {
            i2 = i5 + 2;
            for (i3 = readUnsignedShort(i5); i3 > 0; i3--) {
                i2 = m79a(i2 + 2, cArr, true, classVisitor.visitAnnotation(readUTF8(i2, cArr), false));
            }
        }
        if (i6 != 0) {
            i2 = i6 + 2;
            for (i3 = readUnsignedShort(i6); i3 > 0; i3--) {
                i2 = m81a(context, i2);
                i2 = m79a(i2 + 2, cArr, true, classVisitor.visitTypeAnnotation(context.f103i, context.f104j, readUTF8(i2, cArr), true));
            }
        }
        if (i7 != 0) {
            i2 = i7 + 2;
            for (i3 = readUnsignedShort(i7); i3 > 0; i3--) {
                i2 = m81a(context, i2);
                i2 = m79a(i2 + 2, cArr, true, classVisitor.visitTypeAnnotation(context.f103i, context.f104j, readUTF8(i2, cArr), false));
            }
        }
        while (attribute != null) {
            attribute2 = attribute.f47a;
            attribute.f47a = null;
            classVisitor.visitAttribute(attribute);
            attribute = attribute2;
        }
        if (i8 != 0) {
            i3 = i8 + 2;
            for (i2 = readUnsignedShort(i8); i2 > 0; i2--) {
                classVisitor.visitInnerClass(readClass(i3, cArr), readClass(i3 + 2, cArr), readUTF8(i3 + 4, cArr), readUnsignedShort(i3 + 6));
                i3 += 8;
            }
        }
        i3 = (strArr.length * 2) + (this.header + 10);
        for (i2 = readUnsignedShort(i3 - 2); i2 > 0; i2--) {
            i3 = m80a(classVisitor, context, i3);
        }
        i3 += 2;
        for (i2 = readUnsignedShort(i3 - 2); i2 > 0; i2--) {
            i3 = m90b(classVisitor, context, i3);
        }
        classVisitor.visitEnd();
    }

    public int getAccess() {
        return readUnsignedShort(this.header);
    }

    public String getClassName() {
        return readClass(this.header + 2, new char[this.f54d]);
    }

    public String[] getInterfaces() {
        int i = this.header + 6;
        int readUnsignedShort = readUnsignedShort(i);
        String[] strArr = new String[readUnsignedShort];
        if (readUnsignedShort > 0) {
            char[] cArr = new char[this.f54d];
            for (int i2 = 0; i2 < readUnsignedShort; i2++) {
                i += 2;
                strArr[i2] = readClass(i, cArr);
            }
        }
        return strArr;
    }

    public int getItem(int i) {
        return this.f51a[i];
    }

    public int getItemCount() {
        return this.f51a.length;
    }

    public int getMaxStringLength() {
        return this.f54d;
    }

    public String getSuperName() {
        return readClass(this.header + 4, new char[this.f54d]);
    }

    public int readByte(int i) {
        return this.f52b[i] & 255;
    }

    public String readClass(int i, char[] cArr) {
        return readUTF8(this.f51a[readUnsignedShort(i)], cArr);
    }

    public Object readConst(int i, char[] cArr) {
        int i2 = this.f51a[i];
        switch (this.f52b[i2 - 1]) {
            case (byte) 3:
                return new Integer(readInt(i2));
            case (byte) 4:
                return new Float(Float.intBitsToFloat(readInt(i2)));
            case (byte) 5:
                return new Long(readLong(i2));
            case (byte) 6:
                return new Double(Double.longBitsToDouble(readLong(i2)));
            case (byte) 7:
                return Type.getObjectType(readUTF8(i2, cArr));
            case (byte) 8:
                return readUTF8(i2, cArr);
            case (byte) 16:
                return Type.getMethodType(readUTF8(i2, cArr));
            default:
                int readByte = readByte(i2);
                int[] iArr = this.f51a;
                i2 = iArr[readUnsignedShort(i2 + 1)];
                String readClass = readClass(i2, cArr);
                i2 = iArr[readUnsignedShort(i2 + 2)];
                return new Handle(readByte, readClass, readUTF8(i2, cArr), readUTF8(i2 + 2, cArr));
        }
    }

    public int readInt(int i) {
        byte[] bArr = this.f52b;
        return (bArr[i + 3] & 255) | ((((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16)) | ((bArr[i + 2] & 255) << 8));
    }

    protected Label readLabel(int i, Label[] labelArr) {
        if (labelArr[i] == null) {
            labelArr[i] = new Label();
        }
        return labelArr[i];
    }

    public long readLong(int i) {
        return (((long) readInt(i)) << 32) | (((long) readInt(i + 4)) & 4294967295L);
    }

    public short readShort(int i) {
        byte[] bArr = this.f52b;
        return (short) ((bArr[i + 1] & 255) | ((bArr[i] & 255) << 8));
    }

    public String readUTF8(int i, char[] cArr) {
        int readUnsignedShort = readUnsignedShort(i);
        if (i == 0 || readUnsignedShort == 0) {
            return null;
        }
        String str = this.f53c[readUnsignedShort];
        if (str != null) {
            return str;
        }
        int i2 = this.f51a[readUnsignedShort];
        String[] strArr = this.f53c;
        str = m83a(i2 + 2, readUnsignedShort(i2), cArr);
        strArr[readUnsignedShort] = str;
        return str;
    }

    public int readUnsignedShort(int i) {
        byte[] bArr = this.f52b;
        return (bArr[i + 1] & 255) | ((bArr[i] & 255) << 8);
    }
}
