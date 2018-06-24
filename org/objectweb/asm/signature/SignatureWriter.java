package org.objectweb.asm.signature;

import org.objectweb.asm.Opcodes;

public class SignatureWriter extends SignatureVisitor {
    private final StringBuffer f227a = new StringBuffer();
    private boolean f228b;
    private boolean f229c;
    private int f230d;

    public SignatureWriter() {
        super(Opcodes.ASM5);
    }

    private void m170a() {
        if (this.f228b) {
            this.f228b = false;
            this.f227a.append('>');
        }
    }

    private void m171b() {
        if (this.f230d % 2 != 0) {
            this.f227a.append('>');
        }
        this.f230d /= 2;
    }

    public String toString() {
        return this.f227a.toString();
    }

    public SignatureVisitor visitArrayType() {
        this.f227a.append('[');
        return this;
    }

    public void visitBaseType(char c) {
        this.f227a.append(c);
    }

    public SignatureVisitor visitClassBound() {
        return this;
    }

    public void visitClassType(String str) {
        this.f227a.append('L');
        this.f227a.append(str);
        this.f230d *= 2;
    }

    public void visitEnd() {
        m171b();
        this.f227a.append(';');
    }

    public SignatureVisitor visitExceptionType() {
        this.f227a.append('^');
        return this;
    }

    public void visitFormalTypeParameter(String str) {
        if (!this.f228b) {
            this.f228b = true;
            this.f227a.append('<');
        }
        this.f227a.append(str);
        this.f227a.append(':');
    }

    public void visitInnerClassType(String str) {
        m171b();
        this.f227a.append('.');
        this.f227a.append(str);
        this.f230d *= 2;
    }

    public SignatureVisitor visitInterface() {
        return this;
    }

    public SignatureVisitor visitInterfaceBound() {
        this.f227a.append(':');
        return this;
    }

    public SignatureVisitor visitParameterType() {
        m170a();
        if (!this.f229c) {
            this.f229c = true;
            this.f227a.append('(');
        }
        return this;
    }

    public SignatureVisitor visitReturnType() {
        m170a();
        if (!this.f229c) {
            this.f227a.append('(');
        }
        this.f227a.append(')');
        return this;
    }

    public SignatureVisitor visitSuperclass() {
        m170a();
        return this;
    }

    public SignatureVisitor visitTypeArgument(char c) {
        if (this.f230d % 2 == 0) {
            this.f230d++;
            this.f227a.append('<');
        }
        if (c != SignatureVisitor.INSTANCEOF) {
            this.f227a.append(c);
        }
        return this;
    }

    public void visitTypeArgument() {
        if (this.f230d % 2 == 0) {
            this.f230d++;
            this.f227a.append('<');
        }
        this.f227a.append('*');
    }

    public void visitTypeVariable(String str) {
        this.f227a.append('T');
        this.f227a.append(str);
        this.f227a.append(';');
    }
}
