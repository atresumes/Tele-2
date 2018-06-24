package org.objectweb.asm;

final class Frame {
    static final int[] f129a;
    Label f130b;
    int[] f131c;
    int[] f132d;
    private int[] f133e;
    private int[] f134f;
    private int f135g;
    private int f136h;
    private int[] f137i;

    static {
        _clinit_();
        int[] iArr = new int[202];
        String str = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = str.charAt(i) - 69;
        }
        f129a = iArr;
    }

    Frame() {
    }

    static void _clinit_() {
    }

    private int m116a() {
        if (this.f135g > 0) {
            int[] iArr = this.f134f;
            int i = this.f135g - 1;
            this.f135g = i;
            return iArr[i];
        }
        Label label = this.f130b;
        int i2 = label.f162f - 1;
        label.f162f = i2;
        return 50331648 | (-i2);
    }

    private int m117a(int i) {
        if (this.f133e == null || i >= this.f133e.length) {
            return 33554432 | i;
        }
        int i2 = this.f133e[i];
        if (i2 != 0) {
            return i2;
        }
        i2 = 33554432 | i;
        this.f133e[i] = i2;
        return i2;
    }

    private int m118a(ClassWriter classWriter, int i) {
        int c;
        if (i == 16777222) {
            c = classWriter.m112c(classWriter.f63I) | 24117248;
        } else if ((-1048576 & i) != 25165824) {
            return i;
        } else {
            c = classWriter.m112c(classWriter.f62H[1048575 & i].f152g) | 24117248;
        }
        for (int i2 = 0; i2 < this.f136h; i2++) {
            int i3 = this.f137i[i2];
            int i4 = -268435456 & i3;
            int i5 = 251658240 & i3;
            if (i5 == 33554432) {
                i3 = this.f131c[i3 & 8388607] + i4;
            } else if (i5 == 50331648) {
                i3 = this.f132d[this.f132d.length - (i3 & 8388607)] + i4;
            }
            if (i == i3) {
                return c;
            }
        }
        return i;
    }

    private void m119a(int i, int i2) {
        if (this.f133e == null) {
            this.f133e = new int[10];
        }
        int length = this.f133e.length;
        if (i >= length) {
            Object obj = new int[Math.max(i + 1, length * 2)];
            System.arraycopy(this.f133e, 0, obj, 0, length);
            this.f133e = obj;
        }
        this.f133e[i] = i2;
    }

    private void m120a(String str) {
        char charAt = str.charAt(0);
        if (charAt == '(') {
            m125c((Type.getArgumentsAndReturnSizes(str) >> 2) - 1);
        } else if (charAt == 'J' || charAt == 'D') {
            m125c(2);
        } else {
            m125c(1);
        }
    }

    private void m121a(ClassWriter classWriter, String str) {
        int b = m123b(classWriter, str);
        if (b != 0) {
            m124b(b);
            if (b == 16777220 || b == 16777219) {
                m124b(16777216);
            }
        }
    }

    private static boolean m122a(ClassWriter classWriter, int i, int[] iArr, int i2) {
        int i3 = iArr[i2];
        if (i3 == i) {
            return false;
        }
        int i4;
        if ((268435455 & i) != 16777221) {
            i4 = i;
        } else if (i3 == 16777221) {
            return false;
        } else {
            i4 = 16777221;
        }
        if (i3 == 0) {
            iArr[i2] = i4;
            return true;
        }
        if ((i3 & 267386880) == 24117248 || (i3 & -268435456) != 0) {
            if (i4 == 16777221) {
                return false;
            }
            if ((-1048576 & i4) == (-1048576 & i3)) {
                i4 = (i3 & 267386880) == 24117248 ? classWriter.m99a(i4 & 1048575, 1048575 & i3) | ((-268435456 & i4) | 24117248) : (((i3 & -268435456) - 268435456) | 24117248) | classWriter.m112c("java/lang/Object");
            } else if ((i4 & 267386880) == 24117248 || (i4 & -268435456) != 0) {
                int i5 = ((i4 & -268435456) == 0 || (i4 & 267386880) == 24117248) ? 0 : -268435456;
                i5 += i4 & -268435456;
                i4 = ((i3 & -268435456) == 0 || (i3 & 267386880) == 24117248) ? 0 : -268435456;
                i4 = (Math.min(i5, i4 + (-268435456 & i3)) | 24117248) | classWriter.m112c("java/lang/Object");
            } else {
                i4 = 16777216;
            }
        } else if (i3 != 16777221) {
            i4 = 16777216;
        } else if ((i4 & 267386880) != 24117248 && (-268435456 & i4) == 0) {
            i4 = 16777216;
        }
        if (i3 == i4) {
            return false;
        }
        iArr[i2] = i4;
        return true;
    }

    private static int m123b(ClassWriter classWriter, String str) {
        int i = 16777217;
        int indexOf = str.charAt(0) == '(' ? str.indexOf(41) + 1 : 0;
        switch (str.charAt(indexOf)) {
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z':
                return 16777217;
            case 'D':
                return 16777219;
            case 'F':
                return 16777218;
            case 'J':
                return 16777220;
            case 'L':
                return 24117248 | classWriter.m112c(str.substring(indexOf + 1, str.length() - 1));
            case 'V':
                return 0;
            default:
                int i2 = indexOf + 1;
                while (str.charAt(i2) == '[') {
                    i2++;
                }
                switch (str.charAt(i2)) {
                    case 'B':
                        i = 16777226;
                        break;
                    case 'C':
                        i = 16777227;
                        break;
                    case 'D':
                        i = 16777219;
                        break;
                    case 'F':
                        i = 16777218;
                        break;
                    case 'I':
                        break;
                    case 'J':
                        i = 16777220;
                        break;
                    case 'S':
                        i = 16777228;
                        break;
                    case 'Z':
                        i = 16777225;
                        break;
                    default:
                        i = classWriter.m112c(str.substring(i2 + 1, str.length() - 1)) | 24117248;
                        break;
                }
                return ((i2 - indexOf) << 28) | i;
        }
    }

    private void m124b(int i) {
        if (this.f134f == null) {
            this.f134f = new int[10];
        }
        int length = this.f134f.length;
        if (this.f135g >= length) {
            Object obj = new int[Math.max(this.f135g + 1, length * 2)];
            System.arraycopy(this.f134f, 0, obj, 0, length);
            this.f134f = obj;
        }
        int[] iArr = this.f134f;
        int i2 = this.f135g;
        this.f135g = i2 + 1;
        iArr[i2] = i;
        length = this.f130b.f162f + this.f135g;
        if (length > this.f130b.f163g) {
            this.f130b.f163g = length;
        }
    }

    private void m125c(int i) {
        if (this.f135g >= i) {
            this.f135g -= i;
            return;
        }
        Label label = this.f130b;
        label.f162f -= i - this.f135g;
        this.f135g = 0;
    }

    private void m126d(int i) {
        if (this.f137i == null) {
            this.f137i = new int[2];
        }
        int length = this.f137i.length;
        if (this.f136h >= length) {
            Object obj = new int[Math.max(this.f136h + 1, length * 2)];
            System.arraycopy(this.f137i, 0, obj, 0, length);
            this.f137i = obj;
        }
        int[] iArr = this.f137i;
        int i2 = this.f136h;
        this.f136h = i2 + 1;
        iArr[i2] = i;
    }

    void m127a(int i, int i2, ClassWriter classWriter, Item item) {
        int a;
        int a2;
        int a3;
        String str;
        switch (i) {
            case 0:
            case Opcodes.INEG /*116*/:
            case Opcodes.LNEG /*117*/:
            case Opcodes.FNEG /*118*/:
            case Opcodes.DNEG /*119*/:
            case Opcodes.I2B /*145*/:
            case Opcodes.I2C /*146*/:
            case Opcodes.I2S /*147*/:
            case Opcodes.GOTO /*167*/:
            case Opcodes.RETURN /*177*/:
                return;
            case 1:
                m124b(16777221);
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 16:
            case 17:
            case 21:
                m124b(16777217);
                return;
            case 9:
            case 10:
            case 22:
                m124b(16777220);
                m124b(16777216);
                return;
            case 11:
            case 12:
            case 13:
            case 23:
                m124b(16777218);
                return;
            case 14:
            case 15:
            case 24:
                m124b(16777219);
                m124b(16777216);
                return;
            case 18:
                switch (item.f149b) {
                    case 3:
                        m124b(16777217);
                        return;
                    case 4:
                        m124b(16777218);
                        return;
                    case 5:
                        m124b(16777220);
                        m124b(16777216);
                        return;
                    case 6:
                        m124b(16777219);
                        m124b(16777216);
                        return;
                    case 7:
                        m124b(24117248 | classWriter.m112c("java/lang/Class"));
                        return;
                    case 8:
                        m124b(24117248 | classWriter.m112c("java/lang/String"));
                        return;
                    case 16:
                        m124b(24117248 | classWriter.m112c("java/lang/invoke/MethodType"));
                        return;
                    default:
                        m124b(24117248 | classWriter.m112c("java/lang/invoke/MethodHandle"));
                        return;
                }
            case 25:
                m124b(m117a(i2));
                return;
            case 46:
            case 51:
            case 52:
            case 53:
                m125c(2);
                m124b(16777217);
                return;
            case 47:
            case Opcodes.D2L /*143*/:
                m125c(2);
                m124b(16777220);
                m124b(16777216);
                return;
            case 48:
                m125c(2);
                m124b(16777218);
                return;
            case 49:
            case Opcodes.L2D /*138*/:
                m125c(2);
                m124b(16777219);
                m124b(16777216);
                return;
            case 50:
                m125c(1);
                m124b(m116a() - 268435456);
                return;
            case 54:
            case 56:
            case 58:
                m119a(i2, m116a());
                if (i2 > 0) {
                    a = m117a(i2 - 1);
                    if (a == 16777220 || a == 16777219) {
                        m119a(i2 - 1, 16777216);
                        return;
                    } else if ((251658240 & a) != 16777216) {
                        m119a(i2 - 1, a | 8388608);
                        return;
                    } else {
                        return;
                    }
                }
                return;
            case 55:
            case 57:
                m125c(1);
                m119a(i2, m116a());
                m119a(i2 + 1, 16777216);
                if (i2 > 0) {
                    a = m117a(i2 - 1);
                    if (a == 16777220 || a == 16777219) {
                        m119a(i2 - 1, 16777216);
                        return;
                    } else if ((251658240 & a) != 16777216) {
                        m119a(i2 - 1, a | 8388608);
                        return;
                    } else {
                        return;
                    }
                }
                return;
            case 79:
            case 81:
            case 83:
            case 84:
            case 85:
            case 86:
                m125c(3);
                return;
            case 80:
            case 82:
                m125c(4);
                return;
            case 87:
            case Opcodes.IFEQ /*153*/:
            case Opcodes.IFNE /*154*/:
            case Opcodes.IFLT /*155*/:
            case Opcodes.IFGE /*156*/:
            case Opcodes.IFGT /*157*/:
            case Opcodes.IFLE /*158*/:
            case Opcodes.TABLESWITCH /*170*/:
            case Opcodes.LOOKUPSWITCH /*171*/:
            case Opcodes.IRETURN /*172*/:
            case Opcodes.FRETURN /*174*/:
            case Opcodes.ARETURN /*176*/:
            case Opcodes.ATHROW /*191*/:
            case Opcodes.MONITORENTER /*194*/:
            case Opcodes.MONITOREXIT /*195*/:
            case Opcodes.IFNULL /*198*/:
            case Opcodes.IFNONNULL /*199*/:
                m125c(1);
                return;
            case 88:
            case Opcodes.IF_ICMPEQ /*159*/:
            case Opcodes.IF_ICMPNE /*160*/:
            case Opcodes.IF_ICMPLT /*161*/:
            case Opcodes.IF_ICMPGE /*162*/:
            case Opcodes.IF_ICMPGT /*163*/:
            case Opcodes.IF_ICMPLE /*164*/:
            case Opcodes.IF_ACMPEQ /*165*/:
            case Opcodes.IF_ACMPNE /*166*/:
            case Opcodes.LRETURN /*173*/:
            case Opcodes.DRETURN /*175*/:
                m125c(2);
                return;
            case 89:
                a = m116a();
                m124b(a);
                m124b(a);
                return;
            case 90:
                a = m116a();
                a2 = m116a();
                m124b(a);
                m124b(a2);
                m124b(a);
                return;
            case 91:
                a = m116a();
                a2 = m116a();
                a3 = m116a();
                m124b(a);
                m124b(a3);
                m124b(a2);
                m124b(a);
                return;
            case 92:
                a = m116a();
                a2 = m116a();
                m124b(a2);
                m124b(a);
                m124b(a2);
                m124b(a);
                return;
            case 93:
                a = m116a();
                a2 = m116a();
                a3 = m116a();
                m124b(a2);
                m124b(a);
                m124b(a3);
                m124b(a2);
                m124b(a);
                return;
            case 94:
                a = m116a();
                a2 = m116a();
                a3 = m116a();
                int a4 = m116a();
                m124b(a2);
                m124b(a);
                m124b(a4);
                m124b(a3);
                m124b(a2);
                m124b(a);
                return;
            case 95:
                a = m116a();
                a2 = m116a();
                m124b(a);
                m124b(a2);
                return;
            case 96:
            case 100:
            case 104:
            case 108:
            case 112:
            case Opcodes.ISHL /*120*/:
            case Opcodes.ISHR /*122*/:
            case Opcodes.IUSHR /*124*/:
            case 126:
            case 128:
            case 130:
            case Opcodes.L2I /*136*/:
            case Opcodes.D2I /*142*/:
            case Opcodes.FCMPL /*149*/:
            case Opcodes.FCMPG /*150*/:
                m125c(2);
                m124b(16777217);
                return;
            case 97:
            case 101:
            case 105:
            case 109:
            case 113:
            case 127:
            case Opcodes.LOR /*129*/:
            case Opcodes.LXOR /*131*/:
                m125c(4);
                m124b(16777220);
                m124b(16777216);
                return;
            case 98:
            case 102:
            case 106:
            case 110:
            case 114:
            case Opcodes.L2F /*137*/:
            case Opcodes.D2F /*144*/:
                m125c(2);
                m124b(16777218);
                return;
            case 99:
            case 103:
            case 107:
            case 111:
            case Opcodes.DREM /*115*/:
                m125c(4);
                m124b(16777219);
                m124b(16777216);
                return;
            case Opcodes.LSHL /*121*/:
            case Opcodes.LSHR /*123*/:
            case Opcodes.LUSHR /*125*/:
                m125c(3);
                m124b(16777220);
                m124b(16777216);
                return;
            case Opcodes.IINC /*132*/:
                m119a(i2, 16777217);
                return;
            case Opcodes.I2L /*133*/:
            case Opcodes.F2L /*140*/:
                m125c(1);
                m124b(16777220);
                m124b(16777216);
                return;
            case Opcodes.I2F /*134*/:
                m125c(1);
                m124b(16777218);
                return;
            case Opcodes.I2D /*135*/:
            case Opcodes.F2D /*141*/:
                m125c(1);
                m124b(16777219);
                m124b(16777216);
                return;
            case Opcodes.F2I /*139*/:
            case 190:
            case Opcodes.INSTANCEOF /*193*/:
                m125c(1);
                m124b(16777217);
                return;
            case Opcodes.LCMP /*148*/:
            case Opcodes.DCMPL /*151*/:
            case Opcodes.DCMPG /*152*/:
                m125c(4);
                m124b(16777217);
                return;
            case Opcodes.JSR /*168*/:
            case Opcodes.RET /*169*/:
                throw new RuntimeException("JSR/RET are not supported with computeFrames option");
            case Opcodes.GETSTATIC /*178*/:
                m121a(classWriter, item.f154i);
                return;
            case Opcodes.PUTSTATIC /*179*/:
                m120a(item.f154i);
                return;
            case Opcodes.GETFIELD /*180*/:
                m125c(1);
                m121a(classWriter, item.f154i);
                return;
            case Opcodes.PUTFIELD /*181*/:
                m120a(item.f154i);
                m116a();
                return;
            case Opcodes.INVOKEVIRTUAL /*182*/:
            case Opcodes.INVOKESPECIAL /*183*/:
            case Opcodes.INVOKESTATIC /*184*/:
            case Opcodes.INVOKEINTERFACE /*185*/:
                m120a(item.f154i);
                if (i != Opcodes.INVOKESTATIC) {
                    a = m116a();
                    if (i == Opcodes.INVOKESPECIAL && item.f153h.charAt(0) == '<') {
                        m126d(a);
                    }
                }
                m121a(classWriter, item.f154i);
                return;
            case Opcodes.INVOKEDYNAMIC /*186*/:
                m120a(item.f153h);
                m121a(classWriter, item.f153h);
                return;
            case Opcodes.NEW /*187*/:
                m124b(25165824 | classWriter.m100a(item.f152g, i2));
                return;
            case Opcodes.NEWARRAY /*188*/:
                m116a();
                switch (i2) {
                    case 4:
                        m124b(285212681);
                        return;
                    case 5:
                        m124b(285212683);
                        return;
                    case 6:
                        m124b(285212674);
                        return;
                    case 7:
                        m124b(285212675);
                        return;
                    case 8:
                        m124b(285212682);
                        return;
                    case 9:
                        m124b(285212684);
                        return;
                    case 10:
                        m124b(285212673);
                        return;
                    default:
                        m124b(285212676);
                        return;
                }
            case Opcodes.ANEWARRAY /*189*/:
                str = item.f152g;
                m116a();
                if (str.charAt(0) == '[') {
                    m121a(classWriter, new StringBuffer().append('[').append(str).toString());
                    return;
                } else {
                    m124b(classWriter.m112c(str) | 292552704);
                    return;
                }
            case Opcodes.CHECKCAST /*192*/:
                str = item.f152g;
                m116a();
                if (str.charAt(0) == '[') {
                    m121a(classWriter, str);
                    return;
                } else {
                    m124b(classWriter.m112c(str) | 24117248);
                    return;
                }
            default:
                m125c(i2);
                m121a(classWriter, item.f152g);
                return;
        }
    }

    void m128a(ClassWriter classWriter, int i, Type[] typeArr, int i2) {
        int i3 = 1;
        int i4 = 0;
        this.f131c = new int[i2];
        this.f132d = new int[0];
        if ((i & 8) != 0) {
            i3 = 0;
        } else if ((524288 & i) == 0) {
            this.f131c[0] = 24117248 | classWriter.m112c(classWriter.f63I);
        } else {
            this.f131c[0] = 16777222;
        }
        while (i4 < typeArr.length) {
            int b = m123b(classWriter, typeArr[i4].getDescriptor());
            int i5 = i3 + 1;
            this.f131c[i3] = b;
            if (b == 16777220 || b == 16777219) {
                i3 = i5 + 1;
                this.f131c[i5] = 16777216;
            } else {
                i3 = i5;
            }
            i4++;
        }
        while (i3 < i2) {
            i4 = i3 + 1;
            this.f131c[i3] = 16777216;
            i3 = i4;
        }
    }

    boolean m129a(ClassWriter classWriter, Frame frame, int i) {
        int i2;
        int i3;
        boolean z = false;
        int length = this.f131c.length;
        int length2 = this.f132d.length;
        if (frame.f131c == null) {
            frame.f131c = new int[length];
            z = true;
        }
        int i4 = 0;
        boolean z2 = z;
        while (i4 < length) {
            int i5;
            if (this.f133e == null || i4 >= this.f133e.length) {
                i2 = this.f131c[i4];
            } else {
                i2 = this.f133e[i4];
                if (i2 == 0) {
                    i2 = this.f131c[i4];
                } else {
                    i5 = -268435456 & i2;
                    i3 = 251658240 & i2;
                    if (i3 != 16777216) {
                        i5 = i3 == 33554432 ? i5 + this.f131c[8388607 & i2] : i5 + this.f132d[length2 - (8388607 & i2)];
                        i2 = ((i2 & 8388608) == 0 || !(i5 == 16777220 || i5 == 16777219)) ? i5 : 16777216;
                    }
                }
            }
            if (this.f137i != null) {
                i2 = m118a(classWriter, i2);
            }
            z2 |= m122a(classWriter, i2, frame.f131c, i4);
            i4++;
        }
        if (i > 0) {
            i5 = 0;
            i2 = z2;
            while (i5 < length) {
                int a = m122a(classWriter, this.f131c[i5], frame.f131c, i5) | i2;
                i5++;
                i2 = a;
            }
            if (frame.f132d == null) {
                frame.f132d = new int[1];
                i2 = 1;
            }
            return m122a(classWriter, i, frame.f132d, 0) | i2;
        }
        length = this.f132d.length + this.f130b.f162f;
        if (frame.f132d == null) {
            frame.f132d = new int[(this.f135g + length)];
            z = true;
        } else {
            z = z2;
        }
        boolean z3 = z;
        for (a = 0; a < length; a++) {
            i2 = this.f132d[a];
            if (this.f137i != null) {
                i2 = m118a(classWriter, i2);
            }
            z3 |= m122a(classWriter, i2, frame.f132d, a);
        }
        for (i2 = 0; i2 < this.f135g; i2++) {
            a = this.f134f[i2];
            i4 = -268435456 & a;
            i3 = 251658240 & a;
            if (i3 != 16777216) {
                i4 = i3 == 33554432 ? i4 + this.f131c[8388607 & a] : i4 + this.f132d[length2 - (8388607 & a)];
                a = ((a & 8388608) == 0 || !(i4 == 16777220 || i4 == 16777219)) ? i4 : 16777216;
            }
            if (this.f137i != null) {
                a = m118a(classWriter, a);
            }
            z3 |= m122a(classWriter, a, frame.f132d, length + i2);
        }
        return z3;
    }
}
