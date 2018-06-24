package org.objectweb.asm;

import android.support.v4.internal.view.SupportMenu;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.facebook.login.widget.ProfilePictureView;

class MethodWriter extends MethodVisitor {
    private ByteVector f168$;
    private int f169A;
    private Handler f170B;
    private Handler f171C;
    private int f172D;
    private ByteVector f173E;
    private int f174F;
    private ByteVector f175G;
    private int f176H;
    private ByteVector f177I;
    private Attribute f178J;
    private boolean f179K;
    private int f180L;
    private final int f181M;
    private Label f182N;
    private Label f183O;
    private Label f184P;
    private int f185Q;
    private int f186R;
    private int f187S;
    private int f188T;
    private AnnotationWriter f189U;
    private AnnotationWriter f190V;
    private AnnotationWriter f191W;
    private AnnotationWriter f192X;
    private int f193Y;
    private int f194Z;
    final ClassWriter f195b;
    private int f196c;
    private final int f197d;
    private final int f198e;
    private final String f199f;
    String f200g;
    int f201h;
    int f202i;
    int f203j;
    int[] f204k;
    private ByteVector f205l;
    private AnnotationWriter f206m;
    private AnnotationWriter f207n;
    private AnnotationWriter[] f208o;
    private AnnotationWriter[] f209p;
    private Attribute f210q;
    private ByteVector f211r = new ByteVector();
    private int f212s;
    private int f213t;
    private int f214u;
    private ByteVector f215v;
    private int f216w;
    private int[] f217x;
    private int[] f218z;

    MethodWriter(ClassWriter classWriter, int i, String str, String str2, String str3, String[] strArr, boolean z, boolean z2) {
        int i2;
        int i3 = 0;
        super(Opcodes.ASM5);
        if (classWriter.f59D == null) {
            classWriter.f59D = this;
        } else {
            classWriter.f60E.mv = this;
        }
        classWriter.f60E = this;
        this.f195b = classWriter;
        this.f196c = i;
        if ("<init>".equals(str)) {
            this.f196c |= 524288;
        }
        this.f197d = classWriter.newUTF8(str);
        this.f198e = classWriter.newUTF8(str2);
        this.f199f = str2;
        this.f200g = str3;
        if (strArr != null && strArr.length > 0) {
            this.f203j = strArr.length;
            this.f204k = new int[this.f203j];
            for (i2 = 0; i2 < this.f203j; i2++) {
                this.f204k[i2] = classWriter.newClass(strArr[i2]);
            }
        }
        if (!z2) {
            i3 = z ? 1 : 2;
        }
        this.f181M = i3;
        if (z || z2) {
            i2 = Type.getArgumentsAndReturnSizes(this.f199f) >> 2;
            if ((i & 8) != 0) {
                i2--;
            }
            this.f213t = i2;
            this.f188T = i2;
            this.f182N = new Label();
            Label label = this.f182N;
            label.f157a |= 8;
            visitLabel(this.f182N);
        }
    }

    private int m147a(int i, int i2, int i3) {
        int i4 = (i2 + 3) + i3;
        if (this.f218z == null || this.f218z.length < i4) {
            this.f218z = new int[i4];
        }
        this.f218z[0] = i;
        this.f218z[1] = i2;
        this.f218z[2] = i3;
        return 3;
    }

    static int m148a(byte[] bArr, int i) {
        return ((((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16)) | ((bArr[i + 2] & 255) << 8)) | (bArr[i + 3] & 255);
    }

    static int m149a(int[] iArr, int[] iArr2, int i, int i2) {
        int i3 = i2 - i;
        int i4 = 0;
        while (i4 < iArr.length) {
            if (i < iArr[i4] && iArr[i4] <= i2) {
                i3 += iArr2[i4];
            } else if (i2 < iArr[i4] && iArr[i4] <= i) {
                i3 -= iArr2[i4];
            }
            i4++;
        }
        return i3;
    }

    private void m150a(int i, int i2) {
        while (i < i2) {
            int i3 = this.f218z[i];
            int i4 = -268435456 & i3;
            if (i4 == 0) {
                i4 = i3 & 1048575;
                switch (i3 & 267386880) {
                    case 24117248:
                        this.f215v.putByte(7).putShort(this.f195b.newClass(this.f195b.f62H[i4].f152g));
                        break;
                    case 25165824:
                        this.f215v.putByte(8).putShort(this.f195b.f62H[i4].f150c);
                        break;
                    default:
                        this.f215v.putByte(i4);
                        break;
                }
            }
            StringBuffer stringBuffer = new StringBuffer();
            i4 >>= 28;
            while (true) {
                int i5 = i4 - 1;
                if (i4 > 0) {
                    stringBuffer.append('[');
                    i4 = i5;
                } else {
                    if ((i3 & 267386880) != 24117248) {
                        switch (i3 & 15) {
                            case 1:
                                stringBuffer.append('I');
                                break;
                            case 2:
                                stringBuffer.append('F');
                                break;
                            case 3:
                                stringBuffer.append('D');
                                break;
                            case 9:
                                stringBuffer.append('Z');
                                break;
                            case 10:
                                stringBuffer.append('B');
                                break;
                            case 11:
                                stringBuffer.append('C');
                                break;
                            case 12:
                                stringBuffer.append('S');
                                break;
                            default:
                                stringBuffer.append('J');
                                break;
                        }
                    }
                    stringBuffer.append('L');
                    stringBuffer.append(this.f195b.f62H[i3 & 1048575].f152g);
                    stringBuffer.append(';');
                    this.f215v.putByte(7).putShort(this.f195b.newClass(stringBuffer.toString()));
                }
            }
            i++;
        }
    }

    private void m151a(int i, Label label) {
        Edge edge = new Edge();
        edge.f115a = i;
        edge.f116b = label;
        edge.f117c = this.f184P.f166j;
        this.f184P.f166j = edge;
    }

    private void m152a(Object obj) {
        if (obj instanceof String) {
            this.f215v.putByte(7).putShort(this.f195b.newClass((String) obj));
        } else if (obj instanceof Integer) {
            this.f215v.putByte(((Integer) obj).intValue());
        } else {
            this.f215v.putByte(8).putShort(((Label) obj).f159c);
        }
    }

    private void m153a(Label label, Label[] labelArr) {
        int i = 0;
        if (this.f184P != null) {
            if (this.f181M == 0) {
                this.f184P.f164h.m127a((int) Opcodes.LOOKUPSWITCH, 0, null, null);
                m151a(0, label);
                Label a = label.m140a();
                a.f157a |= 16;
                for (int i2 = 0; i2 < labelArr.length; i2++) {
                    m151a(0, labelArr[i2]);
                    Label a2 = labelArr[i2].m140a();
                    a2.f157a |= 16;
                }
            } else {
                this.f185Q--;
                m151a(this.f185Q, label);
                while (i < labelArr.length) {
                    m151a(this.f185Q, labelArr[i]);
                    i++;
                }
            }
            m162e();
        }
    }

    static void m154a(byte[] bArr, int i, int i2) {
        bArr[i] = (byte) (i2 >>> 8);
        bArr[i + 1] = (byte) i2;
    }

    static void m155a(int[] iArr, int[] iArr2, Label label) {
        if ((label.f157a & 4) == 0) {
            label.f159c = m149a(iArr, iArr2, 0, label.f159c);
            label.f157a |= 4;
        }
    }

    static short m156b(byte[] bArr, int i) {
        return (short) (((bArr[i] & 255) << 8) | (bArr[i + 1] & 255));
    }

    private void m157b() {
        if (this.f217x != null) {
            if (this.f215v == null) {
                this.f215v = new ByteVector();
            }
            m160c();
            this.f214u++;
        }
        this.f217x = this.f218z;
        this.f218z = null;
    }

    private void m158b(Frame frame) {
        int i;
        int[] iArr = frame.f131c;
        int[] iArr2 = frame.f132d;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i2 < iArr.length) {
            i = iArr[i2];
            if (i == 16777216) {
                i4++;
            } else {
                i3 += i4 + 1;
                i4 = 0;
            }
            if (i == 16777220 || i == 16777219) {
                i2++;
            }
            i2++;
        }
        i2 = 0;
        i4 = 0;
        while (i2 < iArr2.length) {
            i = iArr2[i2];
            i4++;
            if (i == 16777220 || i == 16777219) {
                i2++;
            }
            i2++;
        }
        i = i3;
        i3 = m147a(frame.f130b.f159c, i3, i4);
        i2 = 0;
        while (i > 0) {
            int i5 = iArr[i2];
            i4 = i3 + 1;
            this.f218z[i3] = i5;
            if (i5 == 16777220 || i5 == 16777219) {
                i2++;
            }
            i2++;
            i--;
            i3 = i4;
        }
        i2 = i3;
        i3 = 0;
        while (i3 < iArr2.length) {
            i4 = iArr2[i3];
            int i6 = i2 + 1;
            this.f218z[i2] = i4;
            if (i4 == 16777220 || i4 == 16777219) {
                i3++;
            }
            i3++;
            i2 = i6;
        }
        m157b();
    }

    static int m159c(byte[] bArr, int i) {
        return ((bArr[i] & 255) << 8) | (bArr[i + 1] & 255);
    }

    private void m160c() {
        int i = 64;
        int i2 = 0;
        int i3 = this.f218z[1];
        int i4 = this.f218z[2];
        if ((this.f195b.f70b & SupportMenu.USER_MASK) < 50) {
            this.f215v.putShort(this.f218z[0]).putShort(i3);
            m150a(3, i3 + 3);
            this.f215v.putShort(i4);
            m150a(i3 + 3, (i3 + 3) + i4);
            return;
        }
        int i5;
        int i6;
        int i7 = this.f217x[1];
        int i8 = this.f214u == 0 ? this.f218z[0] : (this.f218z[0] - this.f217x[0]) - 1;
        if (i4 == 0) {
            i5 = i3 - i7;
            switch (i5) {
                case ProfilePictureView.NORMAL /*-3*/:
                case -2:
                case -1:
                    i = 248;
                    i7 = i3;
                    break;
                case 0:
                    if (i8 >= 64) {
                        i = 251;
                        break;
                    } else {
                        i = 0;
                        break;
                    }
                case 1:
                case 2:
                case 3:
                    i = 252;
                    break;
                default:
                    i = 255;
                    break;
            }
            i6 = i7;
        } else if (i3 == i7 && i4 == 1) {
            if (i8 >= 63) {
                i = 247;
            }
            i5 = 0;
            i6 = i7;
        } else {
            i5 = 0;
            i = 255;
            i6 = i7;
        }
        if (i != 255) {
            i7 = 3;
            while (i2 < i6) {
                if (this.f218z[i7] != this.f217x[i7]) {
                    i = 255;
                } else {
                    i7++;
                    i2++;
                }
            }
        }
        switch (i) {
            case 0:
                this.f215v.putByte(i8);
                return;
            case 64:
                this.f215v.putByte(i8 + 64);
                m150a(i3 + 3, i3 + 4);
                return;
            case 247:
                this.f215v.putByte(247).putShort(i8);
                m150a(i3 + 3, i3 + 4);
                return;
            case 248:
                this.f215v.putByte(i5 + 251).putShort(i8);
                return;
            case 251:
                this.f215v.putByte(251).putShort(i8);
                return;
            case 252:
                this.f215v.putByte(i5 + 251).putShort(i8);
                m150a(i6 + 3, i3 + 3);
                return;
            default:
                this.f215v.putByte(255).putShort(i8).putShort(i3);
                m150a(3, i3 + 3);
                this.f215v.putShort(i4);
                m150a(i3 + 3, (i3 + 3) + i4);
                return;
        }
    }

    private void m161d() {
        byte[] bArr = this.f211r.f49a;
        int[] iArr = new int[0];
        int[] iArr2 = new int[0];
        boolean[] zArr = new boolean[this.f211r.f50b];
        int i = 3;
        while (true) {
            int c;
            if (i == 3) {
                i = 2;
            }
            int i2 = i;
            i = 0;
            while (i < bArr.length) {
                int i3 = bArr[i] & 255;
                int i4 = 0;
                switch (ClassWriter.f55a[i3]) {
                    case (byte) 0:
                    case (byte) 4:
                        i++;
                        break;
                    case (byte) 1:
                    case (byte) 3:
                    case (byte) 11:
                        i += 2;
                        break;
                    case (byte) 2:
                    case (byte) 5:
                    case (byte) 6:
                    case (byte) 12:
                    case (byte) 13:
                        i += 3;
                        break;
                    case (byte) 7:
                    case (byte) 8:
                        i += 5;
                        break;
                    case (byte) 9:
                        if (i3 > 201) {
                            i3 = i3 < 218 ? i3 - 49 : i3 - 20;
                            c = m159c(bArr, i + 1) + i;
                        } else {
                            c = m156b(bArr, i + 1) + i;
                        }
                        c = m149a(iArr, iArr2, i, c);
                        if ((c < -32768 || c > 32767) && !zArr[i]) {
                            c = (i3 == Opcodes.GOTO || i3 == Opcodes.JSR) ? 2 : 5;
                            zArr[i] = true;
                        } else {
                            c = 0;
                        }
                        i += 3;
                        i4 = c;
                        break;
                    case (byte) 10:
                        i += 5;
                        break;
                    case (byte) 14:
                        if (i2 == 1) {
                            i4 = -(m149a(iArr, iArr2, 0, i) & 3);
                        } else if (!zArr[i]) {
                            i4 = i & 3;
                            zArr[i] = true;
                        }
                        i = (i + 4) - (i & 3);
                        i += (((m148a(bArr, i + 8) - m148a(bArr, i + 4)) + 1) * 4) + 12;
                        break;
                    case (byte) 15:
                        if (i2 == 1) {
                            i4 = -(m149a(iArr, iArr2, 0, i) & 3);
                        } else if (!zArr[i]) {
                            i4 = i & 3;
                            zArr[i] = true;
                        }
                        i = (i + 4) - (i & 3);
                        i += (m148a(bArr, i + 4) * 8) + 8;
                        break;
                    case (byte) 17:
                        if ((bArr[i + 1] & 255) != Opcodes.IINC) {
                            i += 4;
                            break;
                        } else {
                            i += 6;
                            break;
                        }
                    default:
                        i += 4;
                        break;
                }
                if (i4 != 0) {
                    Object obj = new int[(iArr.length + 1)];
                    Object obj2 = new int[(iArr2.length + 1)];
                    System.arraycopy(iArr, 0, obj, 0, iArr.length);
                    System.arraycopy(iArr2, 0, obj2, 0, iArr2.length);
                    obj[iArr.length] = i;
                    obj2[iArr2.length] = i4;
                    if (i4 > 0) {
                        i2 = 3;
                        iArr2 = obj2;
                        iArr = obj;
                    } else {
                        iArr2 = obj2;
                        iArr = obj;
                    }
                }
            }
            if (i2 < 3) {
                i2--;
            }
            if (i2 == 0) {
                ByteVector byteVector = new ByteVector(this.f211r.f50b);
                i = 0;
                while (i < this.f211r.f50b) {
                    c = bArr[i] & 255;
                    int i5;
                    switch (ClassWriter.f55a[c]) {
                        case (byte) 0:
                        case (byte) 4:
                            byteVector.putByte(c);
                            i++;
                            continue;
                        case (byte) 1:
                        case (byte) 3:
                        case (byte) 11:
                            byteVector.putByteArray(bArr, i, 2);
                            i += 2;
                            continue;
                        case (byte) 2:
                        case (byte) 5:
                        case (byte) 6:
                        case (byte) 12:
                        case (byte) 13:
                            byteVector.putByteArray(bArr, i, 3);
                            i += 3;
                            continue;
                        case (byte) 7:
                        case (byte) 8:
                            byteVector.putByteArray(bArr, i, 5);
                            i += 5;
                            continue;
                        case (byte) 9:
                            if (c > 201) {
                                c = c < 218 ? c - 49 : c - 20;
                                i2 = m159c(bArr, i + 1) + i;
                            } else {
                                i2 = m156b(bArr, i + 1) + i;
                            }
                            i3 = m149a(iArr, iArr2, i, i2);
                            if (zArr[i]) {
                                if (c == Opcodes.GOTO) {
                                    byteVector.putByte(Callback.DEFAULT_DRAG_ANIMATION_DURATION);
                                    i2 = i3;
                                } else if (c == Opcodes.JSR) {
                                    byteVector.putByte(201);
                                    i2 = i3;
                                } else {
                                    byteVector.putByte(c <= Opcodes.IF_ACMPNE ? ((c + 1) ^ 1) - 1 : c ^ 1);
                                    byteVector.putShort(8);
                                    byteVector.putByte(Callback.DEFAULT_DRAG_ANIMATION_DURATION);
                                    i2 = i3 - 3;
                                }
                                byteVector.putInt(i2);
                            } else {
                                byteVector.putByte(c);
                                byteVector.putShort(i3);
                            }
                            i += 3;
                            continue;
                        case (byte) 10:
                            i2 = m149a(iArr, iArr2, i, m148a(bArr, i + 1) + i);
                            byteVector.putByte(c);
                            byteVector.putInt(i2);
                            i += 5;
                            continue;
                        case (byte) 14:
                            i2 = (i + 4) - (i & 3);
                            byteVector.putByte(Opcodes.TABLESWITCH);
                            byteVector.putByteArray(null, 0, (4 - (byteVector.f50b % 4)) % 4);
                            i2 += 4;
                            byteVector.putInt(m149a(iArr, iArr2, i, m148a(bArr, i2) + i));
                            c = m148a(bArr, i2);
                            i3 = i2 + 4;
                            byteVector.putInt(c);
                            i2 = (m148a(bArr, i3) - c) + 1;
                            c = i3 + 4;
                            byteVector.putInt(m148a(bArr, c - 4));
                            i5 = i2;
                            i2 = c;
                            c = i5;
                            while (c > 0) {
                                i3 = i2 + 4;
                                byteVector.putInt(m149a(iArr, iArr2, i, i + m148a(bArr, i2)));
                                c--;
                                i2 = i3;
                            }
                            break;
                        case (byte) 15:
                            i2 = (i + 4) - (i & 3);
                            byteVector.putByte(Opcodes.LOOKUPSWITCH);
                            byteVector.putByteArray(null, 0, (4 - (byteVector.f50b % 4)) % 4);
                            i3 = i2 + 4;
                            byteVector.putInt(m149a(iArr, iArr2, i, m148a(bArr, i2) + i));
                            i2 = m148a(bArr, i3);
                            c = i3 + 4;
                            byteVector.putInt(i2);
                            i5 = i2;
                            i2 = c;
                            c = i5;
                            while (c > 0) {
                                byteVector.putInt(m148a(bArr, i2));
                                i2 += 4;
                                i3 = i2 + 4;
                                byteVector.putInt(m149a(iArr, iArr2, i, i + m148a(bArr, i2)));
                                c--;
                                i2 = i3;
                            }
                            break;
                        case (byte) 17:
                            if ((bArr[i + 1] & 255) != Opcodes.IINC) {
                                byteVector.putByteArray(bArr, i, 4);
                                i += 4;
                                break;
                            }
                            byteVector.putByteArray(bArr, i, 6);
                            i += 6;
                            continue;
                        default:
                            byteVector.putByteArray(bArr, i, 4);
                            i += 4;
                            continue;
                    }
                    i = i2;
                }
                if (this.f181M == 0) {
                    Label label = this.f182N;
                    while (label != null) {
                        i2 = label.f159c - 3;
                        if (i2 >= 0 && zArr[i2]) {
                            label.f157a |= 16;
                        }
                        m155a(iArr, iArr2, label);
                        label = label.f165i;
                    }
                    if (this.f195b.f62H != null) {
                        for (Item item : this.f195b.f62H) {
                            if (item != null && item.f149b == 31) {
                                item.f150c = m149a(iArr, iArr2, 0, item.f150c);
                            }
                        }
                    }
                } else if (this.f214u > 0) {
                    this.f195b.f66L = true;
                }
                for (Handler handler = this.f170B; handler != null; handler = handler.f147f) {
                    m155a(iArr, iArr2, handler.f142a);
                    m155a(iArr, iArr2, handler.f143b);
                    m155a(iArr, iArr2, handler.f144c);
                }
                c = 0;
                while (c < 2) {
                    ByteVector byteVector2 = c == 0 ? this.f173E : this.f175G;
                    if (byteVector2 != null) {
                        byte[] bArr2 = byteVector2.f49a;
                        for (i = 0; i < byteVector2.f50b; i += 10) {
                            int c2 = m159c(bArr2, i);
                            int a = m149a(iArr, iArr2, 0, c2);
                            m154a(bArr2, i, a);
                            m154a(bArr2, i + 2, m149a(iArr, iArr2, 0, c2 + m159c(bArr2, i + 2)) - a);
                        }
                    }
                    c++;
                }
                if (this.f177I != null) {
                    byte[] bArr3 = this.f177I.f49a;
                    for (i = 0; i < this.f177I.f50b; i += 4) {
                        m154a(bArr3, i, m149a(iArr, iArr2, 0, m159c(bArr3, i)));
                    }
                }
                for (Attribute attribute = this.f178J; attribute != null; attribute = attribute.f47a) {
                    Label[] labels = attribute.getLabels();
                    if (labels != null) {
                        for (i = labels.length - 1; i >= 0; i--) {
                            m155a(iArr, iArr2, labels[i]);
                        }
                    }
                }
                this.f211r = byteVector;
                return;
            }
            i = i2;
        }
    }

    private void m162e() {
        if (this.f181M == 0) {
            Label label = new Label();
            label.f164h = new Frame();
            label.f164h.f130b = label;
            label.m145a(this, this.f211r.f50b, this.f211r.f49a);
            this.f183O.f165i = label;
            this.f183O = label;
        } else {
            this.f184P.f163g = this.f186R;
        }
        this.f184P = null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void m163f() {
        /*
        r11 = this;
        r10 = 59;
        r1 = 0;
        r9 = 24117248; // 0x1700000 float:4.4081038E-38 double:1.19155037E-316;
        r2 = 1;
        r0 = r11.f199f;
        r0 = r0.length();
        r0 = r0 + 1;
        r1 = r11.m147a(r1, r0, r1);
        r0 = r11.f196c;
        r0 = r0 & 8;
        if (r0 != 0) goto L_0x00d7;
    L_0x0018:
        r0 = r11.f196c;
        r3 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
        r0 = r0 & r3;
        if (r0 != 0) goto L_0x0047;
    L_0x001f:
        r3 = r11.f218z;
        r0 = r1 + 1;
        r4 = r11.f195b;
        r5 = r11.f195b;
        r5 = r5.f63I;
        r4 = r4.m112c(r5);
        r4 = r4 | r9;
        r3[r1] = r4;
    L_0x0030:
        r1 = r0;
        r0 = r2;
    L_0x0032:
        r4 = r11.f199f;
        r3 = r0 + 1;
        r4 = r4.charAt(r0);
        switch(r4) {
            case 66: goto L_0x004f;
            case 67: goto L_0x004f;
            case 68: goto L_0x006c;
            case 70: goto L_0x0058;
            case 73: goto L_0x004f;
            case 74: goto L_0x0062;
            case 76: goto L_0x00b2;
            case 83: goto L_0x004f;
            case 90: goto L_0x004f;
            case 91: goto L_0x0076;
            default: goto L_0x003d;
        };
    L_0x003d:
        r0 = r11.f218z;
        r1 = r1 + -3;
        r0[r2] = r1;
        r11.m157b();
        return;
    L_0x0047:
        r3 = r11.f218z;
        r0 = r1 + 1;
        r4 = 6;
        r3[r1] = r4;
        goto L_0x0030;
    L_0x004f:
        r4 = r11.f218z;
        r0 = r1 + 1;
        r4[r1] = r2;
        r1 = r0;
        r0 = r3;
        goto L_0x0032;
    L_0x0058:
        r4 = r11.f218z;
        r0 = r1 + 1;
        r5 = 2;
        r4[r1] = r5;
        r1 = r0;
        r0 = r3;
        goto L_0x0032;
    L_0x0062:
        r4 = r11.f218z;
        r0 = r1 + 1;
        r5 = 4;
        r4[r1] = r5;
        r1 = r0;
        r0 = r3;
        goto L_0x0032;
    L_0x006c:
        r4 = r11.f218z;
        r0 = r1 + 1;
        r5 = 3;
        r4[r1] = r5;
        r1 = r0;
        r0 = r3;
        goto L_0x0032;
    L_0x0076:
        r4 = r11.f199f;
        r4 = r4.charAt(r3);
        r5 = 91;
        if (r4 != r5) goto L_0x0083;
    L_0x0080:
        r3 = r3 + 1;
        goto L_0x0076;
    L_0x0083:
        r4 = r11.f199f;
        r4 = r4.charAt(r3);
        r5 = 76;
        if (r4 != r5) goto L_0x009a;
    L_0x008d:
        r3 = r3 + 1;
    L_0x008f:
        r4 = r11.f199f;
        r4 = r4.charAt(r3);
        if (r4 == r10) goto L_0x009a;
    L_0x0097:
        r3 = r3 + 1;
        goto L_0x008f;
    L_0x009a:
        r5 = r11.f218z;
        r4 = r1 + 1;
        r6 = r11.f195b;
        r7 = r11.f199f;
        r3 = r3 + 1;
        r0 = r7.substring(r0, r3);
        r0 = r6.m112c(r0);
        r0 = r0 | r9;
        r5[r1] = r0;
        r0 = r3;
        r1 = r4;
        goto L_0x0032;
    L_0x00b2:
        r4 = r11.f199f;
        r4 = r4.charAt(r3);
        if (r4 == r10) goto L_0x00bd;
    L_0x00ba:
        r3 = r3 + 1;
        goto L_0x00b2;
    L_0x00bd:
        r5 = r11.f218z;
        r4 = r1 + 1;
        r6 = r11.f195b;
        r7 = r11.f199f;
        r8 = r0 + 1;
        r0 = r3 + 1;
        r3 = r7.substring(r8, r3);
        r3 = r6.m112c(r3);
        r3 = r3 | r9;
        r5[r1] = r3;
        r1 = r4;
        goto L_0x0032;
    L_0x00d7:
        r0 = r1;
        goto L_0x0030;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.objectweb.asm.MethodWriter.f():void");
    }

    final int m164a() {
        if (this.f201h != 0) {
            return this.f202i + 6;
        }
        int i;
        int length;
        int i2 = 8;
        if (this.f211r.f50b > 0) {
            if (this.f211r.f50b > 65536) {
                throw new RuntimeException("Method code too large!");
            }
            this.f195b.newUTF8("Code");
            i = ((this.f211r.f50b + 18) + (this.f169A * 8)) + 8;
            if (this.f173E != null) {
                this.f195b.newUTF8("LocalVariableTable");
                i += this.f173E.f50b + 8;
            }
            if (this.f175G != null) {
                this.f195b.newUTF8("LocalVariableTypeTable");
                i += this.f175G.f50b + 8;
            }
            if (this.f177I != null) {
                this.f195b.newUTF8("LineNumberTable");
                i += this.f177I.f50b + 8;
            }
            if (this.f215v != null) {
                this.f195b.newUTF8(((this.f195b.f70b & SupportMenu.USER_MASK) >= 50 ? 1 : null) != null ? "StackMapTable" : "StackMap");
                i += this.f215v.f50b + 8;
            }
            if (this.f191W != null) {
                this.f195b.newUTF8("RuntimeVisibleTypeAnnotations");
                i += this.f191W.m67a() + 8;
            }
            if (this.f192X != null) {
                this.f195b.newUTF8("RuntimeInvisibleTypeAnnotations");
                i2 = i + (this.f192X.m67a() + 8);
            } else {
                i2 = i;
            }
            if (this.f178J != null) {
                i2 += this.f178J.m70a(this.f195b, this.f211r.f49a, this.f211r.f50b, this.f212s, this.f213t);
            }
        }
        if (this.f203j > 0) {
            this.f195b.newUTF8("Exceptions");
            i2 += (this.f203j * 2) + 8;
        }
        if ((this.f196c & 4096) != 0 && ((this.f195b.f70b & SupportMenu.USER_MASK) < 49 || (this.f196c & 262144) != 0)) {
            this.f195b.newUTF8("Synthetic");
            i2 += 6;
        }
        if ((this.f196c & 131072) != 0) {
            this.f195b.newUTF8("Deprecated");
            i2 += 6;
        }
        if (this.f200g != null) {
            this.f195b.newUTF8("Signature");
            this.f195b.newUTF8(this.f200g);
            i2 += 8;
        }
        if (this.f168$ != null) {
            this.f195b.newUTF8("MethodParameters");
            i2 += this.f168$.f50b + 7;
        }
        if (this.f205l != null) {
            this.f195b.newUTF8("AnnotationDefault");
            i2 += this.f205l.f50b + 6;
        }
        if (this.f206m != null) {
            this.f195b.newUTF8("RuntimeVisibleAnnotations");
            i2 += this.f206m.m67a() + 8;
        }
        if (this.f207n != null) {
            this.f195b.newUTF8("RuntimeInvisibleAnnotations");
            i2 += this.f207n.m67a() + 8;
        }
        if (this.f189U != null) {
            this.f195b.newUTF8("RuntimeVisibleTypeAnnotations");
            i2 += this.f189U.m67a() + 8;
        }
        if (this.f190V != null) {
            this.f195b.newUTF8("RuntimeInvisibleTypeAnnotations");
            i2 += this.f190V.m67a() + 8;
        }
        if (this.f208o != null) {
            this.f195b.newUTF8("RuntimeVisibleParameterAnnotations");
            length = i2 + (((this.f208o.length - this.f187S) * 2) + 7);
            for (i = this.f208o.length - 1; i >= this.f187S; i--) {
                length += this.f208o[i] == null ? 0 : this.f208o[i].m67a();
            }
        } else {
            length = i2;
        }
        if (this.f209p != null) {
            this.f195b.newUTF8("RuntimeInvisibleParameterAnnotations");
            length += ((this.f209p.length - this.f187S) * 2) + 7;
            for (i = this.f209p.length - 1; i >= this.f187S; i--) {
                length += this.f209p[i] == null ? 0 : this.f209p[i].m67a();
            }
        }
        i2 = length;
        return this.f210q != null ? i2 + this.f210q.m70a(this.f195b, null, 0, -1, -1) : i2;
    }

    final void m165a(ByteVector byteVector) {
        int i = 1;
        byteVector.putShort(((917504 | ((this.f196c & 262144) / 64)) ^ -1) & this.f196c).putShort(this.f197d).putShort(this.f198e);
        if (this.f201h != 0) {
            byteVector.putByteArray(this.f195b.f67M.f52b, this.f201h, this.f202i);
            return;
        }
        int i2 = this.f211r.f50b > 0 ? 1 : 0;
        if (this.f203j > 0) {
            i2++;
        }
        if ((this.f196c & 4096) != 0 && ((this.f195b.f70b & SupportMenu.USER_MASK) < 49 || (this.f196c & 262144) != 0)) {
            i2++;
        }
        if ((this.f196c & 131072) != 0) {
            i2++;
        }
        if (this.f200g != null) {
            i2++;
        }
        if (this.f168$ != null) {
            i2++;
        }
        if (this.f205l != null) {
            i2++;
        }
        if (this.f206m != null) {
            i2++;
        }
        if (this.f207n != null) {
            i2++;
        }
        if (this.f189U != null) {
            i2++;
        }
        if (this.f190V != null) {
            i2++;
        }
        if (this.f208o != null) {
            i2++;
        }
        if (this.f209p != null) {
            i2++;
        }
        if (this.f210q != null) {
            i2 += this.f210q.m69a();
        }
        byteVector.putShort(i2);
        if (this.f211r.f50b > 0) {
            i2 = (this.f211r.f50b + 12) + (this.f169A * 8);
            if (this.f173E != null) {
                i2 += this.f173E.f50b + 8;
            }
            if (this.f175G != null) {
                i2 += this.f175G.f50b + 8;
            }
            if (this.f177I != null) {
                i2 += this.f177I.f50b + 8;
            }
            if (this.f215v != null) {
                i2 += this.f215v.f50b + 8;
            }
            if (this.f191W != null) {
                i2 += this.f191W.m67a() + 8;
            }
            int a = this.f192X != null ? i2 + (this.f192X.m67a() + 8) : i2;
            if (this.f178J != null) {
                a += this.f178J.m70a(this.f195b, this.f211r.f49a, this.f211r.f50b, this.f212s, this.f213t);
            }
            byteVector.putShort(this.f195b.newUTF8("Code")).putInt(a);
            byteVector.putShort(this.f212s).putShort(this.f213t);
            byteVector.putInt(this.f211r.f50b).putByteArray(this.f211r.f49a, 0, this.f211r.f50b);
            byteVector.putShort(this.f169A);
            if (this.f169A > 0) {
                for (Handler handler = this.f170B; handler != null; handler = handler.f147f) {
                    byteVector.putShort(handler.f142a.f159c).putShort(handler.f143b.f159c).putShort(handler.f144c.f159c).putShort(handler.f146e);
                }
            }
            i2 = this.f173E != null ? 1 : 0;
            if (this.f175G != null) {
                i2++;
            }
            if (this.f177I != null) {
                i2++;
            }
            if (this.f215v != null) {
                i2++;
            }
            if (this.f191W != null) {
                i2++;
            }
            if (this.f192X != null) {
                i2++;
            }
            if (this.f178J != null) {
                i2 += this.f178J.m69a();
            }
            byteVector.putShort(i2);
            if (this.f173E != null) {
                byteVector.putShort(this.f195b.newUTF8("LocalVariableTable"));
                byteVector.putInt(this.f173E.f50b + 2).putShort(this.f172D);
                byteVector.putByteArray(this.f173E.f49a, 0, this.f173E.f50b);
            }
            if (this.f175G != null) {
                byteVector.putShort(this.f195b.newUTF8("LocalVariableTypeTable"));
                byteVector.putInt(this.f175G.f50b + 2).putShort(this.f174F);
                byteVector.putByteArray(this.f175G.f49a, 0, this.f175G.f50b);
            }
            if (this.f177I != null) {
                byteVector.putShort(this.f195b.newUTF8("LineNumberTable"));
                byteVector.putInt(this.f177I.f50b + 2).putShort(this.f176H);
                byteVector.putByteArray(this.f177I.f49a, 0, this.f177I.f50b);
            }
            if (this.f215v != null) {
                if ((this.f195b.f70b & SupportMenu.USER_MASK) < 50) {
                    i = 0;
                }
                byteVector.putShort(this.f195b.newUTF8(i != 0 ? "StackMapTable" : "StackMap"));
                byteVector.putInt(this.f215v.f50b + 2).putShort(this.f214u);
                byteVector.putByteArray(this.f215v.f49a, 0, this.f215v.f50b);
            }
            if (this.f191W != null) {
                byteVector.putShort(this.f195b.newUTF8("RuntimeVisibleTypeAnnotations"));
                this.f191W.m68a(byteVector);
            }
            if (this.f192X != null) {
                byteVector.putShort(this.f195b.newUTF8("RuntimeInvisibleTypeAnnotations"));
                this.f192X.m68a(byteVector);
            }
            if (this.f178J != null) {
                this.f178J.m71a(this.f195b, this.f211r.f49a, this.f211r.f50b, this.f213t, this.f212s, byteVector);
            }
        }
        if (this.f203j > 0) {
            byteVector.putShort(this.f195b.newUTF8("Exceptions")).putInt((this.f203j * 2) + 2);
            byteVector.putShort(this.f203j);
            for (i2 = 0; i2 < this.f203j; i2++) {
                byteVector.putShort(this.f204k[i2]);
            }
        }
        if ((this.f196c & 4096) != 0 && ((this.f195b.f70b & SupportMenu.USER_MASK) < 49 || (this.f196c & 262144) != 0)) {
            byteVector.putShort(this.f195b.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.f196c & 131072) != 0) {
            byteVector.putShort(this.f195b.newUTF8("Deprecated")).putInt(0);
        }
        if (this.f200g != null) {
            byteVector.putShort(this.f195b.newUTF8("Signature")).putInt(2).putShort(this.f195b.newUTF8(this.f200g));
        }
        if (this.f168$ != null) {
            byteVector.putShort(this.f195b.newUTF8("MethodParameters"));
            byteVector.putInt(this.f168$.f50b + 1).putByte(this.f194Z);
            byteVector.putByteArray(this.f168$.f49a, 0, this.f168$.f50b);
        }
        if (this.f205l != null) {
            byteVector.putShort(this.f195b.newUTF8("AnnotationDefault"));
            byteVector.putInt(this.f205l.f50b);
            byteVector.putByteArray(this.f205l.f49a, 0, this.f205l.f50b);
        }
        if (this.f206m != null) {
            byteVector.putShort(this.f195b.newUTF8("RuntimeVisibleAnnotations"));
            this.f206m.m68a(byteVector);
        }
        if (this.f207n != null) {
            byteVector.putShort(this.f195b.newUTF8("RuntimeInvisibleAnnotations"));
            this.f207n.m68a(byteVector);
        }
        if (this.f189U != null) {
            byteVector.putShort(this.f195b.newUTF8("RuntimeVisibleTypeAnnotations"));
            this.f189U.m68a(byteVector);
        }
        if (this.f190V != null) {
            byteVector.putShort(this.f195b.newUTF8("RuntimeInvisibleTypeAnnotations"));
            this.f190V.m68a(byteVector);
        }
        if (this.f208o != null) {
            byteVector.putShort(this.f195b.newUTF8("RuntimeVisibleParameterAnnotations"));
            AnnotationWriter.m66a(this.f208o, this.f187S, byteVector);
        }
        if (this.f209p != null) {
            byteVector.putShort(this.f195b.newUTF8("RuntimeInvisibleParameterAnnotations"));
            AnnotationWriter.m66a(this.f209p, this.f187S, byteVector);
        }
        if (this.f210q != null) {
            this.f210q.m71a(this.f195b, null, 0, -1, -1, byteVector);
        }
    }

    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.f195b.newUTF8(str)).putShort(0);
        AnnotationVisitor annotationWriter = new AnnotationWriter(this.f195b, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.f45g = this.f206m;
            this.f206m = annotationWriter;
        } else {
            annotationWriter.f45g = this.f207n;
            this.f207n = annotationWriter;
        }
        return annotationWriter;
    }

    public AnnotationVisitor visitAnnotationDefault() {
        this.f205l = new ByteVector();
        return new AnnotationWriter(this.f195b, false, this.f205l, null, 0);
    }

    public void visitAttribute(Attribute attribute) {
        if (attribute.isCodeAttribute()) {
            attribute.f47a = this.f178J;
            this.f178J = attribute;
            return;
        }
        attribute.f47a = this.f210q;
        this.f210q = attribute;
    }

    public void visitCode() {
    }

    public void visitEnd() {
    }

    public void visitFieldInsn(int i, String str, String str2, String str3) {
        int i2 = 1;
        int i3 = -2;
        this.f193Y = this.f211r.f50b;
        Item a = this.f195b.m109a(str, str2, str3);
        if (this.f184P != null) {
            if (this.f181M == 0) {
                this.f184P.f164h.m127a(i, 0, this.f195b, a);
            } else {
                char charAt = str3.charAt(0);
                switch (i) {
                    case Opcodes.GETSTATIC /*178*/:
                        i3 = this.f185Q;
                        if (charAt == 'D' || charAt == 'J') {
                            i2 = 2;
                        }
                        i2 += i3;
                        break;
                    case Opcodes.PUTSTATIC /*179*/:
                        int i4 = this.f185Q;
                        i2 = (charAt == 'D' || charAt == 'J') ? -2 : -1;
                        i2 += i4;
                        break;
                    case Opcodes.GETFIELD /*180*/:
                        i3 = this.f185Q;
                        if (!(charAt == 'D' || charAt == 'J')) {
                            i2 = 0;
                        }
                        i2 += i3;
                        break;
                    default:
                        i2 = this.f185Q;
                        if (charAt == 'D' || charAt == 'J') {
                            i3 = -3;
                        }
                        i2 += i3;
                        break;
                }
                if (i2 > this.f186R) {
                    this.f186R = i2;
                }
                this.f185Q = i2;
            }
        }
        this.f211r.m74b(i, a.f148a);
    }

    public void visitFrame(int i, int i2, Object[] objArr, int i3, Object[] objArr2) {
        int i4 = 0;
        if (this.f181M != 0) {
            int i5;
            if (i == -1) {
                int i6;
                if (this.f217x == null) {
                    m163f();
                }
                this.f188T = i2;
                int a = m147a(this.f211r.f50b, i2, i3);
                int i7 = 0;
                while (i7 < i2) {
                    if (objArr[i7] instanceof String) {
                        i6 = a + 1;
                        this.f218z[a] = this.f195b.m112c((String) objArr[i7]) | 24117248;
                        i5 = i6;
                    } else if (objArr[i7] instanceof Integer) {
                        i6 = a + 1;
                        this.f218z[a] = ((Integer) objArr[i7]).intValue();
                        i5 = i6;
                    } else {
                        i6 = a + 1;
                        this.f218z[a] = this.f195b.m100a("", ((Label) objArr[i7]).f159c) | 25165824;
                        i5 = i6;
                    }
                    i7++;
                    a = i5;
                }
                while (i4 < i3) {
                    if (objArr2[i4] instanceof String) {
                        i6 = a + 1;
                        this.f218z[a] = this.f195b.m112c((String) objArr2[i4]) | 24117248;
                        i5 = i6;
                    } else if (objArr2[i4] instanceof Integer) {
                        i6 = a + 1;
                        this.f218z[a] = ((Integer) objArr2[i4]).intValue();
                        i5 = i6;
                    } else {
                        i6 = a + 1;
                        this.f218z[a] = this.f195b.m100a("", ((Label) objArr2[i4]).f159c) | 25165824;
                        i5 = i6;
                    }
                    i4++;
                    a = i5;
                }
                m157b();
            } else {
                if (this.f215v == null) {
                    this.f215v = new ByteVector();
                    i5 = this.f211r.f50b;
                } else {
                    i5 = (this.f211r.f50b - this.f216w) - 1;
                    if (i5 < 0) {
                        if (i != 3) {
                            throw new IllegalStateException();
                        }
                        return;
                    }
                }
                switch (i) {
                    case 0:
                        this.f188T = i2;
                        this.f215v.putByte(255).putShort(i5).putShort(i2);
                        for (i5 = 0; i5 < i2; i5++) {
                            m152a(objArr[i5]);
                        }
                        this.f215v.putShort(i3);
                        while (i4 < i3) {
                            m152a(objArr2[i4]);
                            i4++;
                        }
                        break;
                    case 1:
                        this.f188T += i2;
                        this.f215v.putByte(i2 + 251).putShort(i5);
                        for (i5 = 0; i5 < i2; i5++) {
                            m152a(objArr[i5]);
                        }
                        break;
                    case 2:
                        this.f188T -= i2;
                        this.f215v.putByte(251 - i2).putShort(i5);
                        break;
                    case 3:
                        if (i5 >= 64) {
                            this.f215v.putByte(251).putShort(i5);
                            break;
                        } else {
                            this.f215v.putByte(i5);
                            break;
                        }
                    case 4:
                        if (i5 < 64) {
                            this.f215v.putByte(i5 + 64);
                        } else {
                            this.f215v.putByte(247).putShort(i5);
                        }
                        m152a(objArr2[0]);
                        break;
                }
                this.f216w = this.f211r.f50b;
                this.f214u++;
            }
            this.f212s = Math.max(this.f212s, i3);
            this.f213t = Math.max(this.f213t, this.f188T);
        }
    }

    public void visitIincInsn(int i, int i2) {
        this.f193Y = this.f211r.f50b;
        if (this.f184P != null && this.f181M == 0) {
            this.f184P.f164h.m127a((int) Opcodes.IINC, i, null, null);
        }
        if (this.f181M != 2) {
            int i3 = i + 1;
            if (i3 > this.f213t) {
                this.f213t = i3;
            }
        }
        if (i > 255 || i2 > 127 || i2 < -128) {
            this.f211r.putByte(196).m74b(Opcodes.IINC, i).putShort(i2);
        } else {
            this.f211r.putByte(Opcodes.IINC).m73a(i, i2);
        }
    }

    public void visitInsn(int i) {
        this.f193Y = this.f211r.f50b;
        this.f211r.putByte(i);
        if (this.f184P != null) {
            if (this.f181M == 0) {
                this.f184P.f164h.m127a(i, 0, null, null);
            } else {
                int i2 = this.f185Q + Frame.f129a[i];
                if (i2 > this.f186R) {
                    this.f186R = i2;
                }
                this.f185Q = i2;
            }
            if ((i >= Opcodes.IRETURN && i <= Opcodes.RETURN) || i == Opcodes.ATHROW) {
                m162e();
            }
        }
    }

    public AnnotationVisitor visitInsnAnnotation(int i, TypePath typePath, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        AnnotationWriter.m65a((-16776961 & i) | (this.f193Y << 8), typePath, byteVector);
        byteVector.putShort(this.f195b.newUTF8(str)).putShort(0);
        AnnotationVisitor annotationWriter = new AnnotationWriter(this.f195b, true, byteVector, byteVector, byteVector.f50b - 2);
        if (z) {
            annotationWriter.f45g = this.f191W;
            this.f191W = annotationWriter;
        } else {
            annotationWriter.f45g = this.f192X;
            this.f192X = annotationWriter;
        }
        return annotationWriter;
    }

    public void visitIntInsn(int i, int i2) {
        this.f193Y = this.f211r.f50b;
        if (this.f184P != null) {
            if (this.f181M == 0) {
                this.f184P.f164h.m127a(i, i2, null, null);
            } else if (i != Opcodes.NEWARRAY) {
                int i3 = this.f185Q + 1;
                if (i3 > this.f186R) {
                    this.f186R = i3;
                }
                this.f185Q = i3;
            }
        }
        if (i == 17) {
            this.f211r.m74b(i, i2);
        } else {
            this.f211r.m73a(i, i2);
        }
    }

    public void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr) {
        this.f193Y = this.f211r.f50b;
        Item a = this.f195b.m111a(str, str2, handle, objArr);
        int i = a.f150c;
        if (this.f184P != null) {
            if (this.f181M == 0) {
                this.f184P.f164h.m127a((int) Opcodes.INVOKEDYNAMIC, 0, this.f195b, a);
            } else {
                if (i == 0) {
                    i = Type.getArgumentsAndReturnSizes(str2);
                    a.f150c = i;
                }
                i = ((i & 3) + (this.f185Q - (i >> 2))) + 1;
                if (i > this.f186R) {
                    this.f186R = i;
                }
                this.f185Q = i;
            }
        }
        this.f211r.m74b(Opcodes.INVOKEDYNAMIC, a.f148a);
        this.f211r.putShort(0);
    }

    public void visitJumpInsn(int i, Label label) {
        Label label2 = null;
        this.f193Y = this.f211r.f50b;
        if (this.f184P != null) {
            if (this.f181M == 0) {
                this.f184P.f164h.m127a(i, 0, null, null);
                Label a = label.m140a();
                a.f157a |= 16;
                m151a(0, label);
                if (i != Opcodes.GOTO) {
                    label2 = new Label();
                }
            } else if (i == Opcodes.JSR) {
                if ((label.f157a & 512) == 0) {
                    label.f157a |= 512;
                    this.f180L++;
                }
                label2 = this.f184P;
                label2.f157a |= 128;
                m151a(this.f185Q + 1, label);
                label2 = new Label();
            } else {
                this.f185Q += Frame.f129a[i];
                m151a(this.f185Q, label);
            }
        }
        if ((label.f157a & 2) == 0 || label.f159c - this.f211r.f50b >= -32768) {
            this.f211r.putByte(i);
            label.m142a(this, this.f211r, this.f211r.f50b - 1, false);
        } else {
            if (i == Opcodes.GOTO) {
                this.f211r.putByte(Callback.DEFAULT_DRAG_ANIMATION_DURATION);
            } else if (i == Opcodes.JSR) {
                this.f211r.putByte(201);
            } else {
                if (label2 != null) {
                    label2.f157a |= 16;
                }
                this.f211r.putByte(i <= Opcodes.IF_ACMPNE ? ((i + 1) ^ 1) - 1 : i ^ 1);
                this.f211r.putShort(8);
                this.f211r.putByte(Callback.DEFAULT_DRAG_ANIMATION_DURATION);
            }
            label.m142a(this, this.f211r, this.f211r.f50b - 1, true);
        }
        if (this.f184P != null) {
            if (label2 != null) {
                visitLabel(label2);
            }
            if (i == Opcodes.GOTO) {
                m162e();
            }
        }
    }

    public void visitLabel(Label label) {
        this.f179K |= label.m145a(this, this.f211r.f50b, this.f211r.f49a);
        if ((label.f157a & 1) == 0) {
            if (this.f181M == 0) {
                Label label2;
                if (this.f184P != null) {
                    if (label.f159c == this.f184P.f159c) {
                        label2 = this.f184P;
                        label2.f157a |= label.f157a & 16;
                        label.f164h = this.f184P.f164h;
                        return;
                    }
                    m151a(0, label);
                }
                this.f184P = label;
                if (label.f164h == null) {
                    label.f164h = new Frame();
                    label.f164h.f130b = label;
                }
                if (this.f183O != null) {
                    if (label.f159c == this.f183O.f159c) {
                        label2 = this.f183O;
                        label2.f157a |= label.f157a & 16;
                        label.f164h = this.f183O.f164h;
                        this.f184P = this.f183O;
                        return;
                    }
                    this.f183O.f165i = label;
                }
                this.f183O = label;
            } else if (this.f181M == 1) {
                if (this.f184P != null) {
                    this.f184P.f163g = this.f186R;
                    m151a(this.f185Q, label);
                }
                this.f184P = label;
                this.f185Q = 0;
                this.f186R = 0;
                if (this.f183O != null) {
                    this.f183O.f165i = label;
                }
                this.f183O = label;
            }
        }
    }

    public void visitLdcInsn(Object obj) {
        int i;
        this.f193Y = this.f211r.f50b;
        Item a = this.f195b.m106a(obj);
        if (this.f184P != null) {
            if (this.f181M == 0) {
                this.f184P.f164h.m127a(18, 0, this.f195b, a);
            } else {
                i = (a.f149b == 5 || a.f149b == 6) ? this.f185Q + 2 : this.f185Q + 1;
                if (i > this.f186R) {
                    this.f186R = i;
                }
                this.f185Q = i;
            }
        }
        i = a.f148a;
        if (a.f149b == 5 || a.f149b == 6) {
            this.f211r.m74b(20, i);
        } else if (i >= 256) {
            this.f211r.m74b(19, i);
        } else {
            this.f211r.m73a(18, i);
        }
    }

    public void visitLineNumber(int i, Label label) {
        if (this.f177I == null) {
            this.f177I = new ByteVector();
        }
        this.f176H++;
        this.f177I.putShort(label.f159c);
        this.f177I.putShort(i);
    }

    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i) {
        int i2 = 2;
        if (str3 != null) {
            if (this.f175G == null) {
                this.f175G = new ByteVector();
            }
            this.f174F++;
            this.f175G.putShort(label.f159c).putShort(label2.f159c - label.f159c).putShort(this.f195b.newUTF8(str)).putShort(this.f195b.newUTF8(str3)).putShort(i);
        }
        if (this.f173E == null) {
            this.f173E = new ByteVector();
        }
        this.f172D++;
        this.f173E.putShort(label.f159c).putShort(label2.f159c - label.f159c).putShort(this.f195b.newUTF8(str)).putShort(this.f195b.newUTF8(str2)).putShort(i);
        if (this.f181M != 2) {
            char charAt = str2.charAt(0);
            if (!(charAt == 'J' || charAt == 'D')) {
                i2 = 1;
            }
            i2 += i;
            if (i2 > this.f213t) {
                this.f213t = i2;
            }
        }
    }

    public AnnotationVisitor visitLocalVariableAnnotation(int i, TypePath typePath, Label[] labelArr, Label[] labelArr2, int[] iArr, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putByte(i >>> 24).putShort(labelArr.length);
        for (int i2 = 0; i2 < labelArr.length; i2++) {
            byteVector.putShort(labelArr[i2].f159c).putShort(labelArr2[i2].f159c - labelArr[i2].f159c).putShort(iArr[i2]);
        }
        if (typePath == null) {
            byteVector.putByte(0);
        } else {
            byteVector.putByteArray(typePath.f223a, typePath.f224b, (typePath.f223a[typePath.f224b] * 2) + 1);
        }
        byteVector.putShort(this.f195b.newUTF8(str)).putShort(0);
        AnnotationVisitor annotationWriter = new AnnotationWriter(this.f195b, true, byteVector, byteVector, byteVector.f50b - 2);
        if (z) {
            annotationWriter.f45g = this.f191W;
            this.f191W = annotationWriter;
        } else {
            annotationWriter.f45g = this.f192X;
            this.f192X = annotationWriter;
        }
        return annotationWriter;
    }

    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        int i = 0;
        this.f193Y = this.f211r.f50b;
        int i2 = this.f211r.f50b;
        this.f211r.putByte(Opcodes.LOOKUPSWITCH);
        this.f211r.putByteArray(null, 0, (4 - (this.f211r.f50b % 4)) % 4);
        label.m142a(this, this.f211r, i2, true);
        this.f211r.putInt(labelArr.length);
        while (i < labelArr.length) {
            this.f211r.putInt(iArr[i]);
            labelArr[i].m142a(this, this.f211r, i2, true);
            i++;
        }
        m153a(label, labelArr);
    }

    public void visitMaxs(int i, int i2) {
        if (this.f179K) {
            m161d();
        }
        Label a;
        Label a2;
        Label label;
        Edge edge;
        int i3;
        Label label2;
        int length;
        Handler handler;
        if (this.f181M == 0) {
            Label a3;
            int c;
            Handler handler2 = this.f170B;
            while (handler2 != null) {
                a = handler2.f142a.m140a();
                a2 = handler2.f144c.m140a();
                a3 = handler2.f143b.m140a();
                c = 24117248 | this.f195b.m112c(handler2.f145d == null ? "java/lang/Throwable" : handler2.f145d);
                a2.f157a |= 16;
                for (label = a; label != a3; label = label.f165i) {
                    edge = new Edge();
                    edge.f115a = c;
                    edge.f116b = a2;
                    edge.f117c = label.f166j;
                    label.f166j = edge;
                }
                handler2 = handler2.f147f;
            }
            Frame frame = this.f182N.f164h;
            frame.m128a(this.f195b, this.f196c, Type.getArgumentTypes(this.f199f), this.f213t);
            m158b(frame);
            a2 = this.f182N;
            i3 = 0;
            while (a2 != null) {
                label2 = a2.f167k;
                a2.f167k = null;
                Frame frame2 = a2.f164h;
                if ((a2.f157a & 16) != 0) {
                    a2.f157a |= 32;
                }
                a2.f157a |= 64;
                length = frame2.f132d.length + a2.f163g;
                if (length <= i3) {
                    length = i3;
                }
                Edge edge2 = a2.f166j;
                while (edge2 != null) {
                    a = edge2.f116b.m140a();
                    if (frame2.m129a(this.f195b, a.f164h, edge2.f115a) && a.f167k == null) {
                        a.f167k = label2;
                    } else {
                        a = label2;
                    }
                    edge2 = edge2.f117c;
                    label2 = a;
                }
                a2 = label2;
                i3 = length;
            }
            length = i3;
            for (a2 = this.f182N; a2 != null; a2 = a2.f165i) {
                Frame frame3 = a2.f164h;
                if ((a2.f157a & 32) != 0) {
                    m158b(frame3);
                }
                if ((a2.f157a & 64) == 0) {
                    a3 = a2.f165i;
                    int i4 = a2.f159c;
                    c = (a3 == null ? this.f211r.f50b : a3.f159c) - 1;
                    if (c >= i4) {
                        length = Math.max(length, 1);
                        for (i3 = i4; i3 < c; i3++) {
                            this.f211r.f49a[i3] = (byte) 0;
                        }
                        this.f211r.f49a[c] = (byte) -65;
                        this.f218z[m147a(i4, 0, 1)] = this.f195b.m112c("java/lang/Throwable") | 24117248;
                        m157b();
                        this.f170B = Handler.m130a(this.f170B, a2, a3);
                    }
                }
            }
            this.f169A = 0;
            for (handler = this.f170B; handler != null; handler = handler.f147f) {
                this.f169A++;
            }
            this.f212s = length;
        } else if (this.f181M == 1) {
            for (handler = this.f170B; handler != null; handler = handler.f147f) {
                label2 = handler.f144c;
                a2 = handler.f143b;
                for (label = handler.f142a; label != a2; label = label.f165i) {
                    Edge edge3 = new Edge();
                    edge3.f115a = Integer.MAX_VALUE;
                    edge3.f116b = label2;
                    if ((label.f157a & 128) == 0) {
                        edge3.f117c = label.f166j;
                        label.f166j = edge3;
                    } else {
                        edge3.f117c = label.f166j.f117c.f117c;
                        label.f166j.f117c.f117c = edge3;
                    }
                }
            }
            if (this.f180L > 0) {
                this.f182N.m146b(null, 1, this.f180L);
                length = 0;
                for (a = this.f182N; a != null; a = a.f165i) {
                    if ((a.f157a & 128) != 0) {
                        label2 = a.f166j.f117c.f116b;
                        if ((label2.f157a & 1024) == 0) {
                            length++;
                            label2.m146b(null, ((((long) length) / 32) << 32) | (1 << (length % 32)), this.f180L);
                        }
                    }
                }
                for (a = this.f182N; a != null; a = a.f165i) {
                    if ((a.f157a & 128) != 0) {
                        label = this.f182N;
                        while (label != null) {
                            label.f157a &= -2049;
                            label = label.f165i;
                        }
                        a.f166j.f117c.f116b.m146b(a, 0, this.f180L);
                    }
                }
            }
            label2 = this.f182N;
            i3 = 0;
            while (label2 != null) {
                a2 = label2.f167k;
                int i5 = label2.f162f;
                length = label2.f163g + i5;
                if (length <= i3) {
                    length = i3;
                }
                edge = label2.f166j;
                Edge edge4 = (label2.f157a & 128) != 0 ? edge.f117c : edge;
                while (edge4 != null) {
                    label2 = edge4.f116b;
                    if ((label2.f157a & 8) == 0) {
                        label2.f162f = edge4.f115a == Integer.MAX_VALUE ? 1 : edge4.f115a + i5;
                        label2.f157a |= 8;
                        label2.f167k = a2;
                        a = label2;
                    } else {
                        a = a2;
                    }
                    edge4 = edge4.f117c;
                    a2 = a;
                }
                label2 = a2;
                i3 = length;
            }
            this.f212s = Math.max(i, i3);
        } else {
            this.f212s = i;
            this.f213t = i2;
        }
    }

    public void visitMethodInsn(int i, String str, String str2, String str3, boolean z) {
        this.f193Y = this.f211r.f50b;
        Item a = this.f195b.m110a(str, str2, str3, z);
        int i2 = a.f150c;
        if (this.f184P != null) {
            if (this.f181M == 0) {
                this.f184P.f164h.m127a(i, 0, this.f195b, a);
            } else {
                int argumentsAndReturnSizes;
                if (i2 == 0) {
                    argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(str3);
                    a.f150c = argumentsAndReturnSizes;
                } else {
                    argumentsAndReturnSizes = i2;
                }
                i2 = i == Opcodes.INVOKESTATIC ? ((this.f185Q - (argumentsAndReturnSizes >> 2)) + (argumentsAndReturnSizes & 3)) + 1 : (this.f185Q - (argumentsAndReturnSizes >> 2)) + (argumentsAndReturnSizes & 3);
                if (i2 > this.f186R) {
                    this.f186R = i2;
                }
                this.f185Q = i2;
                i2 = argumentsAndReturnSizes;
            }
        }
        if (i == Opcodes.INVOKEINTERFACE) {
            if (i2 == 0) {
                i2 = Type.getArgumentsAndReturnSizes(str3);
                a.f150c = i2;
            }
            this.f211r.m74b(Opcodes.INVOKEINTERFACE, a.f148a).m73a(i2 >> 2, 0);
            return;
        }
        this.f211r.m74b(i, a.f148a);
    }

    public void visitMultiANewArrayInsn(String str, int i) {
        this.f193Y = this.f211r.f50b;
        Item a = this.f195b.m107a(str);
        if (this.f184P != null) {
            if (this.f181M == 0) {
                this.f184P.f164h.m127a((int) Opcodes.MULTIANEWARRAY, i, this.f195b, a);
            } else {
                this.f185Q += 1 - i;
            }
        }
        this.f211r.m74b(Opcodes.MULTIANEWARRAY, a.f148a).putByte(i);
    }

    public void visitParameter(String str, int i) {
        if (this.f168$ == null) {
            this.f168$ = new ByteVector();
        }
        this.f194Z++;
        this.f168$.putShort(str == null ? 0 : this.f195b.newUTF8(str)).putShort(i);
    }

    public AnnotationVisitor visitParameterAnnotation(int i, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        if ("Ljava/lang/Synthetic;".equals(str)) {
            this.f187S = Math.max(this.f187S, i + 1);
            return new AnnotationWriter(this.f195b, false, byteVector, null, 0);
        }
        byteVector.putShort(this.f195b.newUTF8(str)).putShort(0);
        AnnotationVisitor annotationWriter = new AnnotationWriter(this.f195b, true, byteVector, byteVector, 2);
        if (z) {
            if (this.f208o == null) {
                this.f208o = new AnnotationWriter[Type.getArgumentTypes(this.f199f).length];
            }
            annotationWriter.f45g = this.f208o[i];
            this.f208o[i] = annotationWriter;
            return annotationWriter;
        }
        if (this.f209p == null) {
            this.f209p = new AnnotationWriter[Type.getArgumentTypes(this.f199f).length];
        }
        annotationWriter.f45g = this.f209p[i];
        this.f209p[i] = annotationWriter;
        return annotationWriter;
    }

    public void visitTableSwitchInsn(int i, int i2, Label label, Label... labelArr) {
        int i3 = 0;
        this.f193Y = this.f211r.f50b;
        int i4 = this.f211r.f50b;
        this.f211r.putByte(Opcodes.TABLESWITCH);
        this.f211r.putByteArray(null, 0, (4 - (this.f211r.f50b % 4)) % 4);
        label.m142a(this, this.f211r, i4, true);
        this.f211r.putInt(i).putInt(i2);
        while (i3 < labelArr.length) {
            labelArr[i3].m142a(this, this.f211r, i4, true);
            i3++;
        }
        m153a(label, labelArr);
    }

    public AnnotationVisitor visitTryCatchAnnotation(int i, TypePath typePath, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        AnnotationWriter.m65a(i, typePath, byteVector);
        byteVector.putShort(this.f195b.newUTF8(str)).putShort(0);
        AnnotationVisitor annotationWriter = new AnnotationWriter(this.f195b, true, byteVector, byteVector, byteVector.f50b - 2);
        if (z) {
            annotationWriter.f45g = this.f191W;
            this.f191W = annotationWriter;
        } else {
            annotationWriter.f45g = this.f192X;
            this.f192X = annotationWriter;
        }
        return annotationWriter;
    }

    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        this.f169A++;
        Handler handler = new Handler();
        handler.f142a = label;
        handler.f143b = label2;
        handler.f144c = label3;
        handler.f145d = str;
        handler.f146e = str != null ? this.f195b.newClass(str) : 0;
        if (this.f171C == null) {
            this.f170B = handler;
        } else {
            this.f171C.f147f = handler;
        }
        this.f171C = handler;
    }

    public AnnotationVisitor visitTypeAnnotation(int i, TypePath typePath, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        AnnotationWriter.m65a(i, typePath, byteVector);
        byteVector.putShort(this.f195b.newUTF8(str)).putShort(0);
        AnnotationVisitor annotationWriter = new AnnotationWriter(this.f195b, true, byteVector, byteVector, byteVector.f50b - 2);
        if (z) {
            annotationWriter.f45g = this.f189U;
            this.f189U = annotationWriter;
        } else {
            annotationWriter.f45g = this.f190V;
            this.f190V = annotationWriter;
        }
        return annotationWriter;
    }

    public void visitTypeInsn(int i, String str) {
        this.f193Y = this.f211r.f50b;
        Item a = this.f195b.m107a(str);
        if (this.f184P != null) {
            if (this.f181M == 0) {
                this.f184P.f164h.m127a(i, this.f211r.f50b, this.f195b, a);
            } else if (i == Opcodes.NEW) {
                int i2 = this.f185Q + 1;
                if (i2 > this.f186R) {
                    this.f186R = i2;
                }
                this.f185Q = i2;
            }
        }
        this.f211r.m74b(i, a.f148a);
    }

    public void visitVarInsn(int i, int i2) {
        this.f193Y = this.f211r.f50b;
        if (this.f184P != null) {
            if (this.f181M == 0) {
                this.f184P.f164h.m127a(i, i2, null, null);
            } else if (i == Opcodes.RET) {
                Label label = this.f184P;
                label.f157a |= 256;
                this.f184P.f162f = this.f185Q;
                m162e();
            } else {
                int i3;
                i3 = this.f185Q + Frame.f129a[i];
                if (i3 > this.f186R) {
                    this.f186R = i3;
                }
                this.f185Q = i3;
            }
        }
        if (this.f181M != 2) {
            i3 = (i == 22 || i == 24 || i == 55 || i == 57) ? i2 + 2 : i2 + 1;
            if (i3 > this.f213t) {
                this.f213t = i3;
            }
        }
        if (i2 < 4 && i != Opcodes.RET) {
            this.f211r.putByte(i < 54 ? (((i - 21) << 2) + 26) + i2 : (((i - 54) << 2) + 59) + i2);
        } else if (i2 >= 256) {
            this.f211r.putByte(196).m74b(i, i2);
        } else {
            this.f211r.m73a(i, i2);
        }
        if (i >= 54 && this.f181M == 0 && this.f169A > 0) {
            visitLabel(new Label());
        }
    }
}
