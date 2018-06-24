package org.objectweb.asm;

public class TypePath {
    public static final int ARRAY_ELEMENT = 0;
    public static final int INNER_TYPE = 1;
    public static final int TYPE_ARGUMENT = 3;
    public static final int WILDCARD_BOUND = 2;
    byte[] f223a;
    int f224b;

    TypePath(byte[] bArr, int i) {
        this.f223a = bArr;
        this.f224b = i;
    }

    public static TypePath fromString(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        int length = str.length();
        ByteVector byteVector = new ByteVector(length);
        byteVector.putByte(0);
        int i = 0;
        while (i < length) {
            int i2 = i + 1;
            char charAt = str.charAt(i);
            if (charAt == '[') {
                byteVector.m73a(0, 0);
            } else if (charAt == '.') {
                byteVector.m73a(1, 0);
            } else if (charAt == '*') {
                byteVector.m73a(2, 0);
            } else if (charAt >= '0' && charAt <= '9') {
                i = charAt - 48;
                while (i2 < length) {
                    char charAt2 = str.charAt(i2);
                    if (charAt2 < '0' || charAt2 > '9') {
                        break;
                    }
                    i = ((i * 10) + charAt2) - 48;
                    i2++;
                }
                if (i2 < length && str.charAt(i2) == ';') {
                    i2++;
                }
                byteVector.m73a(3, i);
            }
            i = i2;
        }
        byteVector.f49a[0] = (byte) (byteVector.f50b / 2);
        return new TypePath(byteVector.f49a, 0);
    }

    public int getLength() {
        return this.f223a[this.f224b];
    }

    public int getStep(int i) {
        return this.f223a[(this.f224b + (i * 2)) + 1];
    }

    public int getStepArgument(int i) {
        return this.f223a[(this.f224b + (i * 2)) + 2];
    }

    public String toString() {
        int length = getLength();
        StringBuffer stringBuffer = new StringBuffer(length * 2);
        for (int i = 0; i < length; i++) {
            switch (getStep(i)) {
                case 0:
                    stringBuffer.append('[');
                    break;
                case 1:
                    stringBuffer.append('.');
                    break;
                case 2:
                    stringBuffer.append('*');
                    break;
                case 3:
                    stringBuffer.append(getStepArgument(i)).append(';');
                    break;
                default:
                    stringBuffer.append('_');
                    break;
            }
        }
        return stringBuffer.toString();
    }
}
