package org.objectweb.asm;

public class Attribute {
    Attribute f47a;
    byte[] f48b;
    public final String type;

    protected Attribute(String str) {
        this.type = str;
    }

    final int m69a() {
        int i = 0;
        while (this != null) {
            i++;
            this = this.f47a;
        }
        return i;
    }

    final int m70a(ClassWriter classWriter, byte[] bArr, int i, int i2, int i3) {
        int i4 = 0;
        Attribute attribute = this;
        while (attribute != null) {
            classWriter.newUTF8(attribute.type);
            int i5 = (attribute.write(classWriter, bArr, i, i2, i3).f50b + 6) + i4;
            attribute = attribute.f47a;
            i4 = i5;
        }
        return i4;
    }

    final void m71a(ClassWriter classWriter, byte[] bArr, int i, int i2, int i3, ByteVector byteVector) {
        for (Attribute attribute = this; attribute != null; attribute = attribute.f47a) {
            ByteVector write = attribute.write(classWriter, bArr, i, i2, i3);
            byteVector.putShort(classWriter.newUTF8(attribute.type)).putInt(write.f50b);
            byteVector.putByteArray(write.f49a, 0, write.f50b);
        }
    }

    protected Label[] getLabels() {
        return null;
    }

    public boolean isCodeAttribute() {
        return false;
    }

    public boolean isUnknown() {
        return true;
    }

    protected Attribute read(ClassReader classReader, int i, int i2, char[] cArr, int i3, Label[] labelArr) {
        Attribute attribute = new Attribute(this.type);
        attribute.f48b = new byte[i2];
        System.arraycopy(classReader.f52b, i, attribute.f48b, 0, i2);
        return attribute;
    }

    protected ByteVector write(ClassWriter classWriter, byte[] bArr, int i, int i2, int i3) {
        ByteVector byteVector = new ByteVector();
        byteVector.f49a = this.f48b;
        byteVector.f50b = this.f48b.length;
        return byteVector;
    }
}
