package org.objectweb.asm;

final class Item {
    int f148a;
    int f149b;
    int f150c;
    long f151d;
    String f152g;
    String f153h;
    String f154i;
    int f155j;
    Item f156k;

    Item() {
    }

    Item(int i) {
        this.f148a = i;
    }

    Item(int i, Item item) {
        this.f148a = i;
        this.f149b = item.f149b;
        this.f150c = item.f150c;
        this.f151d = item.f151d;
        this.f152g = item.f152g;
        this.f153h = item.f153h;
        this.f154i = item.f154i;
        this.f155j = item.f155j;
    }

    void m131a(double d) {
        this.f149b = 6;
        this.f151d = Double.doubleToRawLongBits(d);
        this.f155j = Integer.MAX_VALUE & (this.f149b + ((int) d));
    }

    void m132a(float f) {
        this.f149b = 4;
        this.f150c = Float.floatToRawIntBits(f);
        this.f155j = Integer.MAX_VALUE & (this.f149b + ((int) f));
    }

    void m133a(int i) {
        this.f149b = 3;
        this.f150c = i;
        this.f155j = Integer.MAX_VALUE & (this.f149b + i);
    }

    void m134a(int i, int i2) {
        this.f149b = 33;
        this.f150c = i;
        this.f155j = i2;
    }

    void m135a(int i, String str, String str2, String str3) {
        this.f149b = i;
        this.f152g = str;
        this.f153h = str2;
        this.f154i = str3;
        switch (i) {
            case 1:
            case 8:
            case 16:
            case 30:
                break;
            case 7:
                this.f150c = 0;
                break;
            case 12:
                this.f155j = ((str.hashCode() * str2.hashCode()) + i) & Integer.MAX_VALUE;
                return;
            default:
                this.f155j = (((str.hashCode() * str2.hashCode()) * str3.hashCode()) + i) & Integer.MAX_VALUE;
                return;
        }
        this.f155j = (str.hashCode() + i) & Integer.MAX_VALUE;
    }

    void m136a(long j) {
        this.f149b = 5;
        this.f151d = j;
        this.f155j = Integer.MAX_VALUE & (this.f149b + ((int) j));
    }

    void m137a(String str, String str2, int i) {
        this.f149b = 18;
        this.f151d = (long) i;
        this.f152g = str;
        this.f153h = str2;
        this.f155j = Integer.MAX_VALUE & (((this.f152g.hashCode() * i) * this.f153h.hashCode()) + 18);
    }

    boolean m138a(Item item) {
        switch (this.f149b) {
            case 1:
            case 7:
            case 8:
            case 16:
            case 30:
                return item.f152g.equals(this.f152g);
            case 3:
            case 4:
                return item.f150c == this.f150c;
            case 5:
            case 6:
            case 32:
                return item.f151d == this.f151d;
            case 12:
                return item.f152g.equals(this.f152g) && item.f153h.equals(this.f153h);
            case 18:
                return item.f151d == this.f151d && item.f152g.equals(this.f152g) && item.f153h.equals(this.f153h);
            case 31:
                return item.f150c == this.f150c && item.f152g.equals(this.f152g);
            default:
                return item.f152g.equals(this.f152g) && item.f153h.equals(this.f153h) && item.f154i.equals(this.f154i);
        }
    }
}
