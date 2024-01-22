package lib;
public class View {
    int screenWidth, screenHeight;
    double xSlope, xOffset, ySlope, yOffset;

    public View(int w, int h) {
        screenWidth = w;
        screenHeight = h;
        setComplexCorners(new Complex(-2, 1), new Complex(1, -1));
    }

    public Complex translate(int x, int y) {
        double real = xSlope * x + xOffset;
        double imag = ySlope * y + yOffset;
        return new Complex(real, imag);
    }

    public void setComplexCorners(Complex topLeft, Complex botRight) {
        xSlope = (botRight.getR() - topLeft.getR()) / (screenWidth - 0);
        xOffset = topLeft.getR();
        ySlope = (botRight.getI() - topLeft.getI()) / (screenHeight - 0);
        yOffset = topLeft.getI();
    }
}