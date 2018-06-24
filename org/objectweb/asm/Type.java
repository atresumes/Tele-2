package org.objectweb.asm;

import android.support.v4.view.MotionEventCompat;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Type {
    public static final int ARRAY = 9;
    public static final int BOOLEAN = 1;
    public static final Type BOOLEAN_TYPE = new Type(1, null, 1509950721, 1);
    public static final int BYTE = 3;
    public static final Type BYTE_TYPE = new Type(3, null, 1107297537, 1);
    public static final int CHAR = 2;
    public static final Type CHAR_TYPE = new Type(2, null, 1124075009, 1);
    public static final int DOUBLE = 8;
    public static final Type DOUBLE_TYPE = new Type(8, null, 1141048066, 1);
    public static final int FLOAT = 6;
    public static final Type FLOAT_TYPE = new Type(6, null, 1174536705, 1);
    public static final int INT = 5;
    public static final Type INT_TYPE = new Type(5, null, 1224736769, 1);
    public static final int LONG = 7;
    public static final Type LONG_TYPE = new Type(7, null, 1241579778, 1);
    public static final int METHOD = 11;
    public static final int OBJECT = 10;
    public static final int SHORT = 4;
    public static final Type SHORT_TYPE = new Type(4, null, 1392510721, 1);
    public static final int VOID = 0;
    public static final Type VOID_TYPE = new Type(0, null, 1443168256, 1);
    private final int f219a;
    private final char[] f220b;
    private final int f221c;
    private final int f222d;

    static {
        _clinit_();
    }

    private Type(int i, char[] cArr, int i2, int i3) {
        this.f219a = i;
        this.f220b = cArr;
        this.f221c = i2;
        this.f222d = i3;
    }

    static void _clinit_() {
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static org.objectweb.asm.Type m166a(char[] r4, int r5) {
        /*
        r3 = 59;
        r0 = 1;
        r1 = r4[r5];
        switch(r1) {
            case 66: goto L_0x001b;
            case 67: goto L_0x0018;
            case 68: goto L_0x002a;
            case 69: goto L_0x0008;
            case 70: goto L_0x0024;
            case 71: goto L_0x0008;
            case 72: goto L_0x0008;
            case 73: goto L_0x0021;
            case 74: goto L_0x0027;
            case 75: goto L_0x0008;
            case 76: goto L_0x0056;
            case 77: goto L_0x0008;
            case 78: goto L_0x0008;
            case 79: goto L_0x0008;
            case 80: goto L_0x0008;
            case 81: goto L_0x0008;
            case 82: goto L_0x0008;
            case 83: goto L_0x001e;
            case 84: goto L_0x0008;
            case 85: goto L_0x0008;
            case 86: goto L_0x0012;
            case 87: goto L_0x0008;
            case 88: goto L_0x0008;
            case 89: goto L_0x0008;
            case 90: goto L_0x0015;
            case 91: goto L_0x002d;
            default: goto L_0x0008;
        };
    L_0x0008:
        r0 = new org.objectweb.asm.Type;
        r1 = 11;
        r2 = r4.length;
        r2 = r2 - r5;
        r0.<init>(r1, r4, r5, r2);
    L_0x0011:
        return r0;
    L_0x0012:
        r0 = VOID_TYPE;
        goto L_0x0011;
    L_0x0015:
        r0 = BOOLEAN_TYPE;
        goto L_0x0011;
    L_0x0018:
        r0 = CHAR_TYPE;
        goto L_0x0011;
    L_0x001b:
        r0 = BYTE_TYPE;
        goto L_0x0011;
    L_0x001e:
        r0 = SHORT_TYPE;
        goto L_0x0011;
    L_0x0021:
        r0 = INT_TYPE;
        goto L_0x0011;
    L_0x0024:
        r0 = FLOAT_TYPE;
        goto L_0x0011;
    L_0x0027:
        r0 = LONG_TYPE;
        goto L_0x0011;
    L_0x002a:
        r0 = DOUBLE_TYPE;
        goto L_0x0011;
    L_0x002d:
        r1 = r5 + r0;
        r1 = r4[r1];
        r2 = 91;
        if (r1 != r2) goto L_0x0038;
    L_0x0035:
        r0 = r0 + 1;
        goto L_0x002d;
    L_0x0038:
        r1 = r5 + r0;
        r1 = r4[r1];
        r2 = 76;
        if (r1 != r2) goto L_0x004b;
    L_0x0040:
        r0 = r0 + 1;
    L_0x0042:
        r1 = r5 + r0;
        r1 = r4[r1];
        if (r1 == r3) goto L_0x004b;
    L_0x0048:
        r0 = r0 + 1;
        goto L_0x0042;
    L_0x004b:
        r1 = new org.objectweb.asm.Type;
        r2 = 9;
        r0 = r0 + 1;
        r1.<init>(r2, r4, r5, r0);
        r0 = r1;
        goto L_0x0011;
    L_0x0056:
        r1 = r5 + r0;
        r1 = r4[r1];
        if (r1 == r3) goto L_0x005f;
    L_0x005c:
        r0 = r0 + 1;
        goto L_0x0056;
    L_0x005f:
        r1 = new org.objectweb.asm.Type;
        r2 = 10;
        r3 = r5 + 1;
        r0 = r0 + -1;
        r1.<init>(r2, r4, r3, r0);
        r0 = r1;
        goto L_0x0011;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.objectweb.asm.Type.a(char[], int):org.objectweb.asm.Type");
    }

    private void m167a(StringBuffer stringBuffer) {
        if (this.f220b == null) {
            stringBuffer.append((char) ((this.f221c & -16777216) >>> 24));
        } else if (this.f219a == 10) {
            stringBuffer.append('L');
            stringBuffer.append(this.f220b, this.f221c, this.f222d);
            stringBuffer.append(';');
        } else {
            stringBuffer.append(this.f220b, this.f221c, this.f222d);
        }
    }

    private static void m168a(StringBuffer stringBuffer, Class cls) {
        char charAt;
        while (!cls.isPrimitive()) {
            if (cls.isArray()) {
                stringBuffer.append('[');
                cls = cls.getComponentType();
            } else {
                stringBuffer.append('L');
                String name = cls.getName();
                int length = name.length();
                for (int i = 0; i < length; i++) {
                    charAt = name.charAt(i);
                    if (charAt == '.') {
                        charAt = '/';
                    }
                    stringBuffer.append(charAt);
                }
                stringBuffer.append(';');
                return;
            }
        }
        charAt = cls == Integer.TYPE ? 'I' : cls == Void.TYPE ? 'V' : cls == Boolean.TYPE ? 'Z' : cls == Byte.TYPE ? 'B' : cls == Character.TYPE ? 'C' : cls == Short.TYPE ? 'S' : cls == Double.TYPE ? 'D' : cls == Float.TYPE ? 'F' : 'J';
        stringBuffer.append(charAt);
    }

    public static Type[] getArgumentTypes(String str) {
        int i = 1;
        char[] toCharArray = str.toCharArray();
        int i2 = 0;
        int i3 = 1;
        while (true) {
            int i4 = i3 + 1;
            char c = toCharArray[i3];
            if (c == ')') {
                break;
            } else if (c == 'L') {
                i3 = i4;
                while (true) {
                    i4 = i3 + 1;
                    if (toCharArray[i3] == ';') {
                        break;
                    }
                    i3 = i4;
                }
                i2++;
                i3 = i4;
            } else if (c != '[') {
                i2++;
                i3 = i4;
            } else {
                i3 = i4;
            }
        }
        Type[] typeArr = new Type[i2];
        i2 = 0;
        while (toCharArray[i] != ')') {
            typeArr[i2] = m166a(toCharArray, i);
            i += (typeArr[i2].f219a == 10 ? 2 : 0) + typeArr[i2].f222d;
            i2++;
        }
        return typeArr;
    }

    public static Type[] getArgumentTypes(Method method) {
        Class[] parameterTypes = method.getParameterTypes();
        Type[] typeArr = new Type[parameterTypes.length];
        for (int length = parameterTypes.length - 1; length >= 0; length--) {
            typeArr[length] = getType(parameterTypes[length]);
        }
        return typeArr;
    }

    public static int getArgumentsAndReturnSizes(String str) {
        int i;
        char charAt;
        int i2 = 1;
        int i3 = 1;
        int i4 = 1;
        while (true) {
            i = i3 + 1;
            charAt = str.charAt(i3);
            if (charAt == ')') {
                break;
            } else if (charAt == 'L') {
                i3 = i;
                while (true) {
                    i = i3 + 1;
                    if (str.charAt(i3) == ';') {
                        break;
                    }
                    i3 = i;
                }
                i4++;
                i3 = i;
            } else if (charAt == '[') {
                while (true) {
                    charAt = str.charAt(i);
                    if (charAt != '[') {
                        break;
                    }
                    i++;
                }
                if (charAt == 'D' || charAt == 'J') {
                    i4--;
                    i3 = i;
                } else {
                    i3 = i;
                }
            } else if (charAt == 'D' || charAt == 'J') {
                i4 += 2;
                i3 = i;
            } else {
                i4++;
                i3 = i;
            }
        }
        charAt = str.charAt(i);
        i4 <<= 2;
        if (charAt == 'V') {
            i2 = 0;
        } else if (charAt == 'D' || charAt == 'J') {
            i2 = 2;
        }
        return i4 | i2;
    }

    public static String getConstructorDescriptor(Constructor constructor) {
        Class[] parameterTypes = constructor.getParameterTypes();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (Class a : parameterTypes) {
            m168a(stringBuffer, a);
        }
        return stringBuffer.append(")V").toString();
    }

    public static String getDescriptor(Class cls) {
        StringBuffer stringBuffer = new StringBuffer();
        m168a(stringBuffer, cls);
        return stringBuffer.toString();
    }

    public static String getInternalName(Class cls) {
        return cls.getName().replace('.', '/');
    }

    public static String getMethodDescriptor(Method method) {
        Class[] parameterTypes = method.getParameterTypes();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (Class a : parameterTypes) {
            m168a(stringBuffer, a);
        }
        stringBuffer.append(')');
        m168a(stringBuffer, method.getReturnType());
        return stringBuffer.toString();
    }

    public static String getMethodDescriptor(Type type, Type... typeArr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (Type a : typeArr) {
            a.m167a(stringBuffer);
        }
        stringBuffer.append(')');
        type.m167a(stringBuffer);
        return stringBuffer.toString();
    }

    public static Type getMethodType(String str) {
        return m166a(str.toCharArray(), 0);
    }

    public static Type getMethodType(Type type, Type... typeArr) {
        return getType(getMethodDescriptor(type, typeArr));
    }

    public static Type getObjectType(String str) {
        char[] toCharArray = str.toCharArray();
        return new Type(toCharArray[0] == '[' ? 9 : 10, toCharArray, 0, toCharArray.length);
    }

    public static Type getReturnType(String str) {
        return m166a(str.toCharArray(), str.indexOf(41) + 1);
    }

    public static Type getReturnType(Method method) {
        return getType(method.getReturnType());
    }

    public static Type getType(Class cls) {
        return cls.isPrimitive() ? cls == Integer.TYPE ? INT_TYPE : cls == Void.TYPE ? VOID_TYPE : cls == Boolean.TYPE ? BOOLEAN_TYPE : cls == Byte.TYPE ? BYTE_TYPE : cls == Character.TYPE ? CHAR_TYPE : cls == Short.TYPE ? SHORT_TYPE : cls == Double.TYPE ? DOUBLE_TYPE : cls == Float.TYPE ? FLOAT_TYPE : LONG_TYPE : getType(getDescriptor(cls));
    }

    public static Type getType(String str) {
        return m166a(str.toCharArray(), 0);
    }

    public static Type getType(Constructor constructor) {
        return getType(getConstructorDescriptor(constructor));
    }

    public static Type getType(Method method) {
        return getType(getMethodDescriptor(method));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Type)) {
            return false;
        }
        Type type = (Type) obj;
        if (this.f219a != type.f219a) {
            return false;
        }
        if (this.f219a < 9) {
            return true;
        }
        if (this.f222d != type.f222d) {
            return false;
        }
        int i = this.f221c;
        int i2 = type.f221c;
        int i3 = this.f222d + i;
        while (i < i3) {
            if (this.f220b[i] != type.f220b[i2]) {
                return false;
            }
            i++;
            i2++;
        }
        return true;
    }

    public Type[] getArgumentTypes() {
        return getArgumentTypes(getDescriptor());
    }

    public int getArgumentsAndReturnSizes() {
        return getArgumentsAndReturnSizes(getDescriptor());
    }

    public String getClassName() {
        switch (this.f219a) {
            case 0:
                return "void";
            case 1:
                return "boolean";
            case 2:
                return "char";
            case 3:
                return "byte";
            case 4:
                return "short";
            case 5:
                return "int";
            case 6:
                return "float";
            case 7:
                return "long";
            case 8:
                return "double";
            case 9:
                StringBuffer stringBuffer = new StringBuffer(getElementType().getClassName());
                for (int dimensions = getDimensions(); dimensions > 0; dimensions--) {
                    stringBuffer.append("[]");
                }
                return stringBuffer.toString();
            case 10:
                return new String(this.f220b, this.f221c, this.f222d).replace('/', '.');
            default:
                return null;
        }
    }

    public String getDescriptor() {
        StringBuffer stringBuffer = new StringBuffer();
        m167a(stringBuffer);
        return stringBuffer.toString();
    }

    public int getDimensions() {
        int i = 1;
        while (this.f220b[this.f221c + i] == '[') {
            i++;
        }
        return i;
    }

    public Type getElementType() {
        return m166a(this.f220b, this.f221c + getDimensions());
    }

    public String getInternalName() {
        return new String(this.f220b, this.f221c, this.f222d);
    }

    public int getOpcode(int i) {
        int i2 = 4;
        if (i == 46 || i == 79) {
            if (this.f220b == null) {
                i2 = (this.f221c & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
            }
            return i2 + i;
        }
        if (this.f220b == null) {
            i2 = (this.f221c & 16711680) >> 16;
        }
        return i2 + i;
    }

    public Type getReturnType() {
        return getReturnType(getDescriptor());
    }

    public int getSize() {
        return this.f220b == null ? this.f221c & 255 : 1;
    }

    public int getSort() {
        return this.f219a;
    }

    public int hashCode() {
        int i = this.f219a * 13;
        if (this.f219a >= 9) {
            int i2 = this.f221c;
            int i3 = i2 + this.f222d;
            while (i2 < i3) {
                int i4 = (i + this.f220b[i2]) * 17;
                i2++;
                i = i4;
            }
        }
        return i;
    }

    public String toString() {
        return getDescriptor();
    }
}
