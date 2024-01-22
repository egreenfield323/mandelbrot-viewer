package lib;

public class Mandelbrot {

    public static int ITERATION_LIMIT = 500;

    public static int testPoint(Complex c) {
        Complex z = new Complex(0, 0);
        for (int i = 1; i < ITERATION_LIMIT; i++) {
            z = z.square();
            z = z.add(c);
            if (z.abs() > 2) {
                return i;
            }
        }
        return -1;
    }
}