package org.objectweb.asm;

import android.support.v4.internal.view.SupportMenu;

public class ClassWriter extends ClassVisitor {
    public static final int COMPUTE_FRAMES = 2;
    public static final int COMPUTE_MAXS = 1;
    static final byte[] f55a;
    ByteVector f56A;
    FieldWriter f57B;
    FieldWriter f58C;
    MethodWriter f59D;
    MethodWriter f60E;
    private short f61G;
    Item[] f62H;
    String f63I;
    private boolean f64J;
    private boolean f65K;
    boolean f66L;
    ClassReader f67M;
    private AnnotationWriter f68N;
    private AnnotationWriter f69O;
    int f70b;
    int f71c;
    final ByteVector f72d;
    Item[] f73e;
    int f74f;
    final Item f75g;
    final Item f76h;
    final Item f77i;
    final Item f78j;
    private int f79k;
    private int f80l;
    private int f81m;
    private int f82n;
    private int f83o;
    private int[] f84p;
    private int f85q;
    private ByteVector f86r;
    private int f87s;
    private int f88t;
    private AnnotationWriter f89u;
    private AnnotationWriter f90v;
    private Attribute f91w;
    private int f92x;
    private ByteVector f93y;
    int f94z;

    static {
        _clinit_();
        byte[] bArr = new byte[220];
        String str = "AAAAAAAAAAAAAAAABCLMMDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJDOPAAAAAAGGGGGGGHIFBFAAFFAARQJJKKJJJJJJJJJJJJJJJJJJ";
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (str.charAt(i) - 65);
        }
        f55a = bArr;
    }

    public ClassWriter(int i) {
        boolean z = true;
        super(Opcodes.ASM5);
        this.f71c = 1;
        this.f72d = new ByteVector();
        this.f73e = new Item[256];
        this.f74f = (int) (0.75d * ((double) this.f73e.length));
        this.f75g = new Item();
        this.f76h = new Item();
        this.f77i = new Item();
        this.f78j = new Item();
        this.f65K = (i & 1) != 0;
        if ((i & 2) == 0) {
            z = false;
        }
        this.f64J = z;
    }

    public ClassWriter(ClassReader classReader, int i) {
        this(i);
        classReader.m92a(this);
        this.f67M = classReader;
    }

    static void _clinit_() {
    }

    private Item m93a(Item item) {
        Item item2 = this.f73e[item.f155j % this.f73e.length];
        while (item2 != null && (item2.f149b != item.f149b || !item.m138a(item2))) {
            item2 = item2.f156k;
        }
        return item2;
    }

    private void m94a(int i, int i2, int i3) {
        this.f72d.m74b(i, i2).putShort(i3);
    }

    private Item m95b(String str) {
        this.f76h.m135a(8, str, null, null);
        Item a = m93a(this.f76h);
        if (a != null) {
            return a;
        }
        this.f72d.m74b(8, newUTF8(str));
        int i = this.f71c;
        this.f71c = i + 1;
        a = new Item(i, this.f76h);
        m97b(a);
        return a;
    }

    private void m96b(int i, int i2, int i3) {
        this.f72d.m73a(i, i2).putShort(i3);
    }

    private void m97b(Item item) {
        int length;
        if (this.f71c + this.f61G > this.f74f) {
            length = this.f73e.length;
            int i = (length * 2) + 1;
            Item[] itemArr = new Item[i];
            for (int i2 = length - 1; i2 >= 0; i2--) {
                Item item2 = this.f73e[i2];
                while (item2 != null) {
                    int length2 = item2.f155j % itemArr.length;
                    Item item3 = item2.f156k;
                    item2.f156k = itemArr[length2];
                    itemArr[length2] = item2;
                    item2 = item3;
                }
            }
            this.f73e = itemArr;
            this.f74f = (int) (((double) i) * 0.75d);
        }
        length = item.f155j % this.f73e.length;
        item.f156k = this.f73e[length];
        this.f73e[length] = item;
    }

    private Item m98c(Item item) {
        this.f61G = (short) (this.f61G + 1);
        Item item2 = new Item(this.f61G, this.f75g);
        m97b(item2);
        if (this.f62H == null) {
            this.f62H = new Item[16];
        }
        if (this.f61G == this.f62H.length) {
            Object obj = new Item[(this.f62H.length * 2)];
            System.arraycopy(this.f62H, 0, obj, 0, this.f62H.length);
            this.f62H = obj;
        }
        this.f62H[this.f61G] = item2;
        return item2;
    }

    int m99a(int i, int i2) {
        this.f76h.f149b = 32;
        this.f76h.f151d = ((long) i) | (((long) i2) << 32);
        this.f76h.f155j = Integer.MAX_VALUE & ((i + 32) + i2);
        Item a = m93a(this.f76h);
        if (a == null) {
            String str = this.f62H[i].f152g;
            String str2 = this.f62H[i2].f152g;
            this.f76h.f150c = m112c(getCommonSuperClass(str, str2));
            a = new Item(0, this.f76h);
            m97b(a);
        }
        return a.f150c;
    }

    int m100a(String str, int i) {
        this.f75g.f149b = 31;
        this.f75g.f150c = i;
        this.f75g.f152g = str;
        this.f75g.f155j = Integer.MAX_VALUE & ((str.hashCode() + 31) + i);
        Item a = m93a(this.f75g);
        if (a == null) {
            a = m98c(this.f75g);
        }
        return a.f148a;
    }

    Item m101a(double d) {
        this.f75g.m131a(d);
        Item a = m93a(this.f75g);
        if (a != null) {
            return a;
        }
        this.f72d.putByte(6).putLong(this.f75g.f151d);
        a = new Item(this.f71c, this.f75g);
        this.f71c += 2;
        m97b(a);
        return a;
    }

    Item m102a(float f) {
        this.f75g.m132a(f);
        Item a = m93a(this.f75g);
        if (a != null) {
            return a;
        }
        this.f72d.putByte(4).putInt(this.f75g.f150c);
        int i = this.f71c;
        this.f71c = i + 1;
        a = new Item(i, this.f75g);
        m97b(a);
        return a;
    }

    Item m103a(int i) {
        this.f75g.m133a(i);
        Item a = m93a(this.f75g);
        if (a != null) {
            return a;
        }
        this.f72d.putByte(3).putInt(i);
        int i2 = this.f71c;
        this.f71c = i2 + 1;
        a = new Item(i2, this.f75g);
        m97b(a);
        return a;
    }

    Item m104a(int i, String str, String str2, String str3) {
        this.f78j.m135a(i + 20, str, str2, str3);
        Item a = m93a(this.f78j);
        if (a != null) {
            return a;
        }
        if (i <= 4) {
            m96b(15, i, newField(str, str2, str3));
        } else {
            m96b(15, i, newMethod(str, str2, str3, i == 9));
        }
        int i2 = this.f71c;
        this.f71c = i2 + 1;
        a = new Item(i2, this.f78j);
        m97b(a);
        return a;
    }

    Item m105a(long j) {
        this.f75g.m136a(j);
        Item a = m93a(this.f75g);
        if (a != null) {
            return a;
        }
        this.f72d.putByte(5).putLong(j);
        a = new Item(this.f71c, this.f75g);
        this.f71c += 2;
        m97b(a);
        return a;
    }

    Item m106a(Object obj) {
        if (obj instanceof Integer) {
            return m103a(((Integer) obj).intValue());
        }
        if (obj instanceof Byte) {
            return m103a(((Byte) obj).intValue());
        }
        if (obj instanceof Character) {
            return m103a(((Character) obj).charValue());
        }
        if (obj instanceof Short) {
            return m103a(((Short) obj).intValue());
        }
        if (obj instanceof Boolean) {
            return m103a(((Boolean) obj).booleanValue() ? 1 : 0);
        } else if (obj instanceof Float) {
            return m102a(((Float) obj).floatValue());
        } else {
            if (obj instanceof Long) {
                return m105a(((Long) obj).longValue());
            }
            if (obj instanceof Double) {
                return m101a(((Double) obj).doubleValue());
            }
            if (obj instanceof String) {
                return m95b((String) obj);
            }
            if (obj instanceof Type) {
                Type type = (Type) obj;
                int sort = type.getSort();
                return sort == 10 ? m107a(type.getInternalName()) : sort == 11 ? m112c(type.getDescriptor()) : m107a(type.getDescriptor());
            } else if (obj instanceof Handle) {
                Handle handle = (Handle) obj;
                return m104a(handle.f138a, handle.f139b, handle.f140c, handle.f141d);
            } else {
                throw new IllegalArgumentException(new StringBuffer().append("value ").append(obj).toString());
            }
        }
    }

    Item m107a(String str) {
        this.f76h.m135a(7, str, null, null);
        Item a = m93a(this.f76h);
        if (a != null) {
            return a;
        }
        this.f72d.m74b(7, newUTF8(str));
        int i = this.f71c;
        this.f71c = i + 1;
        a = new Item(i, this.f76h);
        m97b(a);
        return a;
    }

    Item m108a(String str, String str2) {
        this.f76h.m135a(12, str, str2, null);
        Item a = m93a(this.f76h);
        if (a != null) {
            return a;
        }
        m94a(12, newUTF8(str), newUTF8(str2));
        int i = this.f71c;
        this.f71c = i + 1;
        a = new Item(i, this.f76h);
        m97b(a);
        return a;
    }

    Item m109a(String str, String str2, String str3) {
        this.f77i.m135a(9, str, str2, str3);
        Item a = m93a(this.f77i);
        if (a != null) {
            return a;
        }
        m94a(9, newClass(str), newNameType(str2, str3));
        int i = this.f71c;
        this.f71c = i + 1;
        a = new Item(i, this.f77i);
        m97b(a);
        return a;
    }

    Item m110a(String str, String str2, String str3, boolean z) {
        int i = z ? 11 : 10;
        this.f77i.m135a(i, str, str2, str3);
        Item a = m93a(this.f77i);
        if (a != null) {
            return a;
        }
        m94a(i, newClass(str), newNameType(str2, str3));
        int i2 = this.f71c;
        this.f71c = i2 + 1;
        Item item = new Item(i2, this.f77i);
        m97b(item);
        return item;
    }

    Item m111a(String str, String str2, Handle handle, Object... objArr) {
        int i;
        Item item;
        ByteVector byteVector = this.f56A;
        if (byteVector == null) {
            byteVector = new ByteVector();
            this.f56A = byteVector;
        }
        int i2 = byteVector.f50b;
        int hashCode = handle.hashCode();
        byteVector.putShort(newHandle(handle.f138a, handle.f139b, handle.f140c, handle.f141d));
        byteVector.putShort(r5);
        int i3 = hashCode;
        for (Object obj : objArr) {
            i3 ^= obj.hashCode();
            byteVector.putShort(newConst(obj));
        }
        byte[] bArr = byteVector.f49a;
        int i4 = (i4 + 2) << 1;
        int i5 = i3 & Integer.MAX_VALUE;
        Item item2 = this.f73e[i5 % this.f73e.length];
        loop1:
        while (item2 != null) {
            if (item2.f149b == 33 && item2.f155j == i5) {
                int i6 = item2.f150c;
                hashCode = 0;
                while (hashCode < i4) {
                    if (bArr[i2 + hashCode] != bArr[i6 + hashCode]) {
                        item2 = item2.f156k;
                    } else {
                        hashCode++;
                    }
                }
                break loop1;
            }
            item2 = item2.f156k;
        }
        if (item2 != null) {
            hashCode = item2.f148a;
            byteVector.f50b = i2;
            i = hashCode;
        } else {
            i = this.f94z;
            this.f94z = i + 1;
            item = new Item(i);
            item.m134a(i2, i5);
            m97b(item);
        }
        this.f77i.m137a(str, str2, i);
        item = m93a(this.f77i);
        if (item != null) {
            return item;
        }
        m94a(18, i, newNameType(str, str2));
        hashCode = this.f71c;
        this.f71c = hashCode + 1;
        Item item3 = new Item(hashCode, this.f77i);
        m97b(item3);
        return item3;
    }

    int m112c(String str) {
        this.f75g.m135a(30, str, null, null);
        Item a = m93a(this.f75g);
        if (a == null) {
            a = m98c(this.f75g);
        }
        return a.f148a;
    }

    Item m113c(String str) {
        this.f76h.m135a(16, str, null, null);
        Item a = m93a(this.f76h);
        if (a != null) {
            return a;
        }
        this.f72d.m74b(16, newUTF8(str));
        int i = this.f71c;
        this.f71c = i + 1;
        a = new Item(i, this.f76h);
        m97b(a);
        return a;
    }

    protected String getCommonSuperClass(String str, String str2) {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            Class cls = Class.forName(str.replace('/', '.'), false, classLoader);
            Class cls2 = Class.forName(str2.replace('/', '.'), false, classLoader);
            if (cls.isAssignableFrom(cls2)) {
                return str;
            }
            if (cls2.isAssignableFrom(cls)) {
                return str2;
            }
            if (cls.isInterface() || cls2.isInterface()) {
                return "java/lang/Object";
            }
            do {
                cls = cls.getSuperclass();
            } while (!cls.isAssignableFrom(cls2));
            return cls.getName().replace('.', '/');
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public int newClass(String str) {
        return m107a(str).f148a;
    }

    public int newConst(Object obj) {
        return m106a(obj).f148a;
    }

    public int newField(String str, String str2, String str3) {
        return m109a(str, str2, str3).f148a;
    }

    public int newHandle(int i, String str, String str2, String str3) {
        return m104a(i, str, str2, str3).f148a;
    }

    public int newInvokeDynamic(String str, String str2, Handle handle, Object... objArr) {
        return m111a(str, str2, handle, objArr).f148a;
    }

    public int newMethod(String str, String str2, String str3, boolean z) {
        return m110a(str, str2, str3, z).f148a;
    }

    public int newMethodType(String str) {
        return m112c(str).f148a;
    }

    public int newNameType(String str, String str2) {
        return m108a(str, str2).f148a;
    }

    public int newUTF8(String str) {
        this.f75g.m135a(1, str, null, null);
        Item a = m93a(this.f75g);
        if (a == null) {
            this.f72d.putByte(1).putUTF8(str);
            int i = this.f71c;
            this.f71c = i + 1;
            a = new Item(i, this.f75g);
            m97b(a);
        }
        return a.f148a;
    }

    public byte[] toByteArray() {
        if (this.f71c > SupportMenu.USER_MASK) {
            throw new RuntimeException("Class file too large!");
        }
        int i;
        int i2 = (this.f83o * 2) + 24;
        FieldWriter fieldWriter = this.f57B;
        int i3 = 0;
        while (fieldWriter != null) {
            i2 += fieldWriter.m114a();
            fieldWriter = (FieldWriter) fieldWriter.fv;
            i3++;
        }
        MethodWriter methodWriter = this.f59D;
        int i4 = 0;
        while (methodWriter != null) {
            i2 += methodWriter.m164a();
            methodWriter = (MethodWriter) methodWriter.mv;
            i4++;
        }
        if (this.f56A != null) {
            i = 1;
            i2 += this.f56A.f50b + 8;
            newUTF8("BootstrapMethods");
        } else {
            i = 0;
        }
        if (this.f81m != 0) {
            i++;
            i2 += 8;
            newUTF8("Signature");
        }
        if (this.f85q != 0) {
            i++;
            i2 += 8;
            newUTF8("SourceFile");
        }
        if (this.f86r != null) {
            i++;
            i2 += this.f86r.f50b + 6;
            newUTF8("SourceDebugExtension");
        }
        if (this.f87s != 0) {
            i++;
            i2 += 10;
            newUTF8("EnclosingMethod");
        }
        if ((this.f79k & 131072) != 0) {
            i++;
            i2 += 6;
            newUTF8("Deprecated");
        }
        if ((this.f79k & 4096) != 0 && ((this.f70b & SupportMenu.USER_MASK) < 49 || (this.f79k & 262144) != 0)) {
            i++;
            i2 += 6;
            newUTF8("Synthetic");
        }
        if (this.f93y != null) {
            i++;
            i2 += this.f93y.f50b + 8;
            newUTF8("InnerClasses");
        }
        if (this.f89u != null) {
            i++;
            i2 += this.f89u.m67a() + 8;
            newUTF8("RuntimeVisibleAnnotations");
        }
        if (this.f90v != null) {
            i++;
            i2 += this.f90v.m67a() + 8;
            newUTF8("RuntimeInvisibleAnnotations");
        }
        if (this.f68N != null) {
            i++;
            i2 += this.f68N.m67a() + 8;
            newUTF8("RuntimeVisibleTypeAnnotations");
        }
        if (this.f69O != null) {
            i++;
            i2 += this.f69O.m67a() + 8;
            newUTF8("RuntimeInvisibleTypeAnnotations");
        }
        int i5 = i2;
        if (this.f91w != null) {
            i5 += this.f91w.m70a(this, null, 0, -1, -1);
            i2 = i + this.f91w.m69a();
        } else {
            i2 = i;
        }
        ByteVector byteVector = new ByteVector(this.f72d.f50b + i5);
        byteVector.putInt(-889275714).putInt(this.f70b);
        byteVector.putShort(this.f71c).putByteArray(this.f72d.f49a, 0, this.f72d.f50b);
        byteVector.putShort(((393216 | ((this.f79k & 262144) / 64)) ^ -1) & this.f79k).putShort(this.f80l).putShort(this.f82n);
        byteVector.putShort(this.f83o);
        for (i = 0; i < this.f83o; i++) {
            byteVector.putShort(this.f84p[i]);
        }
        byteVector.putShort(i3);
        for (fieldWriter = this.f57B; fieldWriter != null; fieldWriter = (FieldWriter) fieldWriter.fv) {
            fieldWriter.m115a(byteVector);
        }
        byteVector.putShort(i4);
        for (methodWriter = this.f59D; methodWriter != null; methodWriter = (MethodWriter) methodWriter.mv) {
            methodWriter.m165a(byteVector);
        }
        byteVector.putShort(i2);
        if (this.f56A != null) {
            byteVector.putShort(newUTF8("BootstrapMethods"));
            byteVector.putInt(this.f56A.f50b + 2).putShort(this.f94z);
            byteVector.putByteArray(this.f56A.f49a, 0, this.f56A.f50b);
        }
        if (this.f81m != 0) {
            byteVector.putShort(newUTF8("Signature")).putInt(2).putShort(this.f81m);
        }
        if (this.f85q != 0) {
            byteVector.putShort(newUTF8("SourceFile")).putInt(2).putShort(this.f85q);
        }
        if (this.f86r != null) {
            i = this.f86r.f50b;
            byteVector.putShort(newUTF8("SourceDebugExtension")).putInt(i);
            byteVector.putByteArray(this.f86r.f49a, 0, i);
        }
        if (this.f87s != 0) {
            byteVector.putShort(newUTF8("EnclosingMethod")).putInt(4);
            byteVector.putShort(this.f87s).putShort(this.f88t);
        }
        if ((this.f79k & 131072) != 0) {
            byteVector.putShort(newUTF8("Deprecated")).putInt(0);
        }
        if ((this.f79k & 4096) != 0 && ((this.f70b & SupportMenu.USER_MASK) < 49 || (this.f79k & 262144) != 0)) {
            byteVector.putShort(newUTF8("Synthetic")).putInt(0);
        }
        if (this.f93y != null) {
            byteVector.putShort(newUTF8("InnerClasses"));
            byteVector.putInt(this.f93y.f50b + 2).putShort(this.f92x);
            byteVector.putByteArray(this.f93y.f49a, 0, this.f93y.f50b);
        }
        if (this.f89u != null) {
            byteVector.putShort(newUTF8("RuntimeVisibleAnnotations"));
            this.f89u.m68a(byteVector);
        }
        if (this.f90v != null) {
            byteVector.putShort(newUTF8("RuntimeInvisibleAnnotations"));
            this.f90v.m68a(byteVector);
        }
        if (this.f68N != null) {
            byteVector.putShort(newUTF8("RuntimeVisibleTypeAnnotations"));
            this.f68N.m68a(byteVector);
        }
        if (this.f69O != null) {
            byteVector.putShort(newUTF8("RuntimeInvisibleTypeAnnotations"));
            this.f69O.m68a(byteVector);
        }
        if (this.f91w != null) {
            this.f91w.m71a(this, null, 0, -1, -1, byteVector);
        }
        if (!this.f66L) {
            return byteVector.f49a;
        }
        this.f89u = null;
        this.f90v = null;
        this.f91w = null;
        this.f92x = 0;
        this.f93y = null;
        this.f94z = 0;
        this.f56A = null;
        this.f57B = null;
        this.f58C = null;
        this.f59D = null;
        this.f60E = null;
        this.f65K = false;
        this.f64J = true;
        this.f66L = false;
        new ClassReader(byteVector.f49a).accept(this, 4);
        return toByteArray();
    }

    public final void visit(int i, int i2, String str, String str2, String str3, String[] strArr) {
        int i3 = 0;
        this.f70b = i;
        this.f79k = i2;
        this.f80l = newClass(str);
        this.f63I = str;
        if (str2 != null) {
            this.f81m = newUTF8(str2);
        }
        this.f82n = str3 == null ? 0 : newClass(str3);
        if (strArr != null && strArr.length > 0) {
            this.f83o = strArr.length;
            this.f84p = new int[this.f83o];
            while (i3 < this.f83o) {
                this.f84p[i3] = newClass(strArr[i3]);
                i3++;
            }
        }
    }

    public final AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(newUTF8(str)).putShort(0);
        AnnotationVisitor annotationWriter = new AnnotationWriter(this, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.f45g = this.f89u;
            this.f89u = annotationWriter;
        } else {
            annotationWriter.f45g = this.f90v;
            this.f90v = annotationWriter;
        }
        return annotationWriter;
    }

    public final void visitAttribute(Attribute attribute) {
        attribute.f47a = this.f91w;
        this.f91w = attribute;
    }

    public final void visitEnd() {
    }

    public final FieldVisitor visitField(int i, String str, String str2, String str3, Object obj) {
        return new FieldWriter(this, i, str, str2, str3, obj);
    }

    public final void visitInnerClass(String str, String str2, String str3, int i) {
        int i2 = 0;
        if (this.f93y == null) {
            this.f93y = new ByteVector();
        }
        Item a = m107a(str);
        if (a.f150c == 0) {
            this.f92x++;
            this.f93y.putShort(a.f148a);
            this.f93y.putShort(str2 == null ? 0 : newClass(str2));
            ByteVector byteVector = this.f93y;
            if (str3 != null) {
                i2 = newUTF8(str3);
            }
            byteVector.putShort(i2);
            this.f93y.putShort(i);
            a.f150c = this.f92x;
        }
    }

    public final MethodVisitor visitMethod(int i, String str, String str2, String str3, String[] strArr) {
        return new MethodWriter(this, i, str, str2, str3, strArr, this.f65K, this.f64J);
    }

    public final void visitOuterClass(String str, String str2, String str3) {
        this.f87s = newClass(str);
        if (str2 != null && str3 != null) {
            this.f88t = newNameType(str2, str3);
        }
    }

    public final void visitSource(String str, String str2) {
        if (str != null) {
            this.f85q = newUTF8(str);
        }
        if (str2 != null) {
            this.f86r = new ByteVector().m75c(str2, 0, Integer.MAX_VALUE);
        }
    }

    public final AnnotationVisitor visitTypeAnnotation(int i, TypePath typePath, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        AnnotationWriter.m65a(i, typePath, byteVector);
        byteVector.putShort(newUTF8(str)).putShort(0);
        AnnotationVisitor annotationWriter = new AnnotationWriter(this, true, byteVector, byteVector, byteVector.f50b - 2);
        if (z) {
            annotationWriter.f45g = this.f68N;
            this.f68N = annotationWriter;
        } else {
            annotationWriter.f45g = this.f69O;
            this.f69O = annotationWriter;
        }
        return annotationWriter;
    }
}
