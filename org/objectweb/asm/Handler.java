package org.objectweb.asm;

class Handler {
    Label f142a;
    Label f143b;
    Label f144c;
    String f145d;
    int f146e;
    Handler f147f;

    Handler() {
    }

    static Handler m130a(Handler handler, Label label, Label label2) {
        if (handler == null) {
            return null;
        }
        handler.f147f = m130a(handler.f147f, label, label2);
        int i = handler.f142a.f159c;
        int i2 = handler.f143b.f159c;
        int i3 = label.f159c;
        int i4 = label2 == null ? Integer.MAX_VALUE : label2.f159c;
        if (i3 >= i2 || i4 <= i) {
            return handler;
        }
        if (i3 <= i) {
            if (i4 >= i2) {
                return handler.f147f;
            }
            handler.f142a = label2;
            return handler;
        } else if (i4 >= i2) {
            handler.f143b = label;
            return handler;
        } else {
            Handler handler2 = new Handler();
            handler2.f142a = label2;
            handler2.f143b = handler.f143b;
            handler2.f144c = handler.f144c;
            handler2.f145d = handler.f145d;
            handler2.f146e = handler.f146e;
            handler2.f147f = handler.f147f;
            handler.f143b = label;
            handler.f147f = handler2;
            return handler;
        }
    }
}
