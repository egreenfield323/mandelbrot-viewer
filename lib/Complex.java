package lib;

public class Complex {

    private double r;
    private double i;

    public Complex() {
        this.r = 0;
        this.i = 0;
    }

    public Complex(double real, double imag) {
        this.r = real;
        this.i = imag;
    }

    public double getR() {
        return this.r;
    }

    public double getI() {
        return this.i;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void setI(double i) {
        this.i = i;
    }

    public Complex add(Complex that) {
        return new Complex(this.r + that.r, this.i + that.i);
    }

    public Complex add(double real) {
        return new Complex(this.r + real, this.i);
    }

    public Complex subtract(Complex that) {
        return new Complex(this.r - that.r, this.i - that.i);
    }

    public Complex subtract(double real) {
        return new Complex(this.r - real, this.i);
    }

    public Complex multiply(Complex that) {
        double rs = this.r * that.r;
        double is = this.i * that.i;
        double cross1 = this.r * this.i;
        double cross2 = this.i * that.r;
        double imag = 0;
        double real = cross1 + cross2;
        if (rs < 0 && is < 0) {
            imag = rs + is;
        } else {

            imag = rs + -is;
            return new Complex(imag, real);
        }
        return new Complex(real, imag);
    }

    public Complex multiply(double real) {
        return new Complex(this.r * real, this.i * real);
    }

    public Complex divide(Complex that) {
        double that2 = that.r * that.r + that.i * that.i;
        double real = (this.r * that.r + this.i * that.i) / that2;
        double imag = (this.i * that.r - this.r * that.i) / that2;
        return new Complex(real, imag);
    }

    public Complex divide(double real) {
        return this.divide(new Complex(real, 0));
    }

    public double abs() {
        return Math.sqrt(this.r * this.r + this.i * this.i);
    }

    public Complex square() {
        return this.multiply(this);
    }

    public boolean equals(Complex that) {
        if (this.r == that.r && this.i == that.i) {
            return true;
        }
        return false;
    }

    public String toString() {
        return String.format("%.2f + %.2fi", this.r, this.i);
    }
}