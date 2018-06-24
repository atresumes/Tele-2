package org.objectweb.asm;

import android.support.v4.internal.view.SupportMenu;

public class ByteVector {
    byte[] f49a;
    int f50b;

    public ByteVector() {
        this.f49a = new byte[64];
    }

    public ByteVector(int i) {
        this.f49a = new byte[i];
    }

    private void m72a(int i) {
        int length = this.f49a.length * 2;
        int i2 = this.f50b + i;
        if (length <= i2) {
            length = i2;
        }
        Object obj = new byte[length];
        System.arraycopy(this.f49a, 0, obj, 0, this.f50b);
        this.f49a = obj;
    }

    ByteVector m73a(int i, int i2) {
        int i3 = this.f50b;
        if (i3 + 2 > this.f49a.length) {
            m72a(2);
        }
        byte[] bArr = this.f49a;
        int i4 = i3 + 1;
        bArr[i3] = (byte) i;
        i3 = i4 + 1;
        bArr[i4] = (byte) i2;
        this.f50b = i3;
        return this;
    }

    ByteVector m74b(int i, int i2) {
        int i3 = this.f50b;
        if (i3 + 3 > this.f49a.length) {
            m72a(3);
        }
        byte[] bArr = this.f49a;
        int i4 = i3 + 1;
        bArr[i3] = (byte) i;
        i3 = i4 + 1;
        bArr[i4] = (byte) (i2 >>> 8);
        i4 = i3 + 1;
        bArr[i3] = (byte) i2;
        this.f50b = i4;
        return this;
    }

    ByteVector m75c(String str, int i, int i2) {
        int i3;
        int length = str.length();
        int i4 = i;
        for (i3 = i; i3 < length; i3++) {
            char charAt = str.charAt(i3);
            i4 = (charAt < '\u0001' || charAt > '') ? charAt > '߿' ? i4 + 3 : i4 + 2 : i4 + 1;
        }
        if (i4 > i2) {
            throw new IllegalArgumentException();
        }
        i3 = (this.f50b - i) - 2;
        if (i3 >= 0) {
            this.f49a[i3] = (byte) (i4 >>> 8);
            this.f49a[i3 + 1] = (byte) i4;
        }
        if ((this.f50b + i4) - i > this.f49a.length) {
            m72a(i4 - i);
        }
        i3 = this.f50b;
        while (i < length) {
            charAt = str.charAt(i);
            if (charAt >= '\u0001' && charAt <= '') {
                i4 = i3 + 1;
                this.f49a[i3] = (byte) charAt;
            } else if (charAt > '߿') {
                r4 = i3 + 1;
                this.f49a[i3] = (byte) (((charAt >> 12) & 15) | 224);
                i3 = r4 + 1;
                this.f49a[r4] = (byte) (((charAt >> 6) & 63) | 128);
                i4 = i3 + 1;
                this.f49a[i3] = (byte) ((charAt & 63) | 128);
            } else {
                r4 = i3 + 1;
                this.f49a[i3] = (byte) (((charAt >> 6) & 31) | Opcodes.CHECKCAST);
                i4 = r4 + 1;
                this.f49a[r4] = (byte) ((charAt & 63) | 128);
            }
            i++;
            i3 = i4;
        }
        this.f50b = i3;
        return this;
    }

    public ByteVector putByte(int i) {
        int i2 = this.f50b;
        if (i2 + 1 > this.f49a.length) {
            m72a(1);
        }
        int i3 = i2 + 1;
        this.f49a[i2] = (byte) i;
        this.f50b = i3;
        return this;
    }

    public ByteVector putByteArray(byte[] bArr, int i, int i2) {
        if (this.f50b + i2 > this.f49a.length) {
            m72a(i2);
        }
        if (bArr != null) {
            System.arraycopy(bArr, i, this.f49a, this.f50b, i2);
        }
        this.f50b += i2;
        return this;
    }

    public ByteVector putInt(int i) {
        int i2 = this.f50b;
        if (i2 + 4 > this.f49a.length) {
            m72a(4);
        }
        byte[] bArr = this.f49a;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 24);
        i2 = i3 + 1;
        bArr[i3] = (byte) (i >>> 16);
        i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 8);
        i2 = i3 + 1;
        bArr[i3] = (byte) i;
        this.f50b = i2;
        return this;
    }

    public ByteVector putLong(long j) {
        int i = this.f50b;
        if (i + 8 > this.f49a.length) {
            m72a(8);
        }
        byte[] bArr = this.f49a;
        int i2 = (int) (j >>> 32);
        int i3 = i + 1;
        bArr[i] = (byte) (i2 >>> 24);
        i = i3 + 1;
        bArr[i3] = (byte) (i2 >>> 16);
        i3 = i + 1;
        bArr[i] = (byte) (i2 >>> 8);
        i = i3 + 1;
        bArr[i3] = (byte) i2;
        i2 = (int) j;
        i3 = i + 1;
        bArr[i] = (byte) (i2 >>> 24);
        i = i3 + 1;
        bArr[i3] = (byte) (i2 >>> 16);
        i3 = i + 1;
        bArr[i] = (byte) (i2 >>> 8);
        i = i3 + 1;
        bArr[i3] = (byte) i2;
        this.f50b = i;
        return this;
    }

    public ByteVector putShort(int i) {
        int i2 = this.f50b;
        if (i2 + 2 > this.f49a.length) {
            m72a(2);
        }
        byte[] bArr = this.f49a;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 8);
        i2 = i3 + 1;
        bArr[i3] = (byte) i;
        this.f50b = i2;
        return this;
    }

    public ByteVector putUTF8(String str) {
        int length = str.length();
        if (length > SupportMenu.USER_MASK) {
            throw new IllegalArgumentException();
        }
        int i = this.f50b;
        if ((i + 2) + length > this.f49a.length) {
            m72a(length + 2);
        }
        byte[] bArr = this.f49a;
        int i2 = i + 1;
        bArr[i] = (byte) (length >>> 8);
        int i3 = i2 + 1;
        bArr[i2] = (byte) length;
        i = 0;
        while (i < length) {
            char charAt = str.charAt(i);
            if (charAt < '\u0001' || charAt > '') {
                this.f50b = i3;
                return m75c(str, i, SupportMenu.USER_MASK);
            }
            i2 = i3 + 1;
            bArr[i3] = (byte) charAt;
            i++;
            i3 = i2;
        }
        this.f50b = i3;
        return this;
    }
}
