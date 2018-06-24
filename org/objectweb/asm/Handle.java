package org.objectweb.asm;

public final class Handle {
    final int f138a;
    final String f139b;
    final String f140c;
    final String f141d;

    public Handle(int i, String str, String str2, String str3) {
        this.f138a = i;
        this.f139b = str;
        this.f140c = str2;
        this.f141d = str3;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Handle)) {
            return false;
        }
        Handle handle = (Handle) obj;
        return this.f138a == handle.f138a && this.f139b.equals(handle.f139b) && this.f140c.equals(handle.f140c) && this.f141d.equals(handle.f141d);
    }

    public String getDesc() {
        return this.f141d;
    }

    public String getName() {
        return this.f140c;
    }

    public String getOwner() {
        return this.f139b;
    }

    public int getTag() {
        return this.f138a;
    }

    public int hashCode() {
        return this.f138a + ((this.f139b.hashCode() * this.f140c.hashCode()) * this.f141d.hashCode());
    }

    public String toString() {
        return new StringBuffer().append(this.f139b).append('.').append(this.f140c).append(this.f141d).append(" (").append(this.f138a).append(')').toString();
    }
}
