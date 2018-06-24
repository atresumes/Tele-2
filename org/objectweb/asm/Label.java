package org.objectweb.asm;

public class Label {
    int f157a;
    int f158b;
    int f159c;
    private int f160d;
    private int[] f161e;
    int f162f;
    int f163g;
    Frame f164h;
    Label f165i;
    public Object info;
    Edge f166j;
    Label f167k;

    private void m139a(int i, int i2) {
        if (this.f161e == null) {
            this.f161e = new int[6];
        }
        if (this.f160d >= this.f161e.length) {
            Object obj = new int[(this.f161e.length + 6)];
            System.arraycopy(this.f161e, 0, obj, 0, this.f161e.length);
            this.f161e = obj;
        }
        int[] iArr = this.f161e;
        int i3 = this.f160d;
        this.f160d = i3 + 1;
        iArr[i3] = i;
        iArr = this.f161e;
        i3 = this.f160d;
        this.f160d = i3 + 1;
        iArr[i3] = i2;
    }

    Label m140a() {
        return this.f164h == null ? this : this.f164h.f130b;
    }

    void m141a(long j, int i) {
        if ((this.f157a & 1024) == 0) {
            this.f157a |= 1024;
            this.f161e = new int[((i / 32) + 1)];
        }
        int[] iArr = this.f161e;
        int i2 = (int) (j >>> 32);
        iArr[i2] = iArr[i2] | ((int) j);
    }

    void m142a(MethodWriter methodWriter, ByteVector byteVector, int i, boolean z) {
        if ((this.f157a & 2) == 0) {
            if (z) {
                m139a(-1 - i, byteVector.f50b);
                byteVector.putInt(-1);
                return;
            }
            m139a(i, byteVector.f50b);
            byteVector.putShort(-1);
        } else if (z) {
            byteVector.putInt(this.f159c - i);
        } else {
            byteVector.putShort(this.f159c - i);
        }
    }

    boolean m143a(long j) {
        return ((this.f157a & 1024) == 0 || (this.f161e[(int) (j >>> 32)] & ((int) j)) == 0) ? false : true;
    }

    boolean m144a(Label label) {
        if ((this.f157a & 1024) == 0 || (label.f157a & 1024) == 0) {
            return false;
        }
        for (int i = 0; i < this.f161e.length; i++) {
            if ((this.f161e[i] & label.f161e[i]) != 0) {
                return true;
            }
        }
        return false;
    }

    boolean m145a(MethodWriter methodWriter, int i, byte[] bArr) {
        int i2 = 0;
        this.f157a |= 2;
        this.f159c = i;
        boolean z = false;
        while (i2 < this.f160d) {
            int i3 = i2 + 1;
            int i4 = this.f161e[i2];
            i2 = i3 + 1;
            i3 = this.f161e[i3];
            int i5;
            if (i4 >= 0) {
                i4 = i - i4;
                if (i4 < -32768 || i4 > 32767) {
                    int i6 = bArr[i3 - 1] & 255;
                    if (i6 <= Opcodes.JSR) {
                        bArr[i3 - 1] = (byte) (i6 + 49);
                    } else {
                        bArr[i3 - 1] = (byte) (i6 + 20);
                    }
                    z = true;
                }
                i5 = i3 + 1;
                bArr[i3] = (byte) (i4 >>> 8);
                bArr[i5] = (byte) i4;
            } else {
                i4 = (i4 + i) + 1;
                i5 = i3 + 1;
                bArr[i3] = (byte) (i4 >>> 24);
                i3 = i5 + 1;
                bArr[i5] = (byte) (i4 >>> 16);
                i5 = i3 + 1;
                bArr[i3] = (byte) (i4 >>> 8);
                bArr[i5] = (byte) i4;
            }
        }
        return z;
    }

    void m146b(Label label, long j, int i) {
        while (this != null) {
            Label label2 = this.f167k;
            this.f167k = null;
            if (label != null) {
                if ((this.f157a & 2048) != 0) {
                    this = label2;
                } else {
                    this.f157a |= 2048;
                    if (!((this.f157a & 256) == 0 || m144a(label))) {
                        Edge edge = new Edge();
                        edge.f115a = this.f162f;
                        edge.f116b = label.f166j.f116b;
                        edge.f117c = this.f166j;
                        this.f166j = edge;
                    }
                }
            } else if (m143a(j)) {
                this = label2;
            } else {
                m141a(j, i);
            }
            Label label3 = label2;
            Edge edge2 = this.f166j;
            while (edge2 != null) {
                if (((this.f157a & 128) == 0 || edge2 != this.f166j.f117c) && edge2.f116b.f167k == null) {
                    edge2.f116b.f167k = label3;
                    label3 = edge2.f116b;
                }
                edge2 = edge2.f117c;
            }
            this = label3;
        }
    }

    public int getOffset() {
        if ((this.f157a & 2) != 0) {
            return this.f159c;
        }
        throw new IllegalStateException("Label offset position has not been resolved yet");
    }

    public String toString() {
        return new StringBuffer().append("L").append(System.identityHashCode(this)).toString();
    }
}
