package lib;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Explorer extends JPanel implements MouseListener, KeyListener {

    int screenWidth = 600;
    int screenHeight = 400;

    Complex topLeft = new Complex(-2, 1);
    Complex botRight = new Complex(1, -1);
    View view = new View(screenWidth, screenHeight);
    View[] views = new View[500];
    int i = 0;
    Complex[] botR = new Complex[500];
    Complex[] topL = new Complex[500];

    public Explorer() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.WHITE);
        addKeyListener(this);
        addMouseListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                view = new View(screenWidth, screenHeight);
                view.setComplexCorners(topLeft, botRight);
                Complex c = view.translate(x, y);
                int colorVal = Mandelbrot.testPoint(c);
                Color color = mapColor(colorVal);
                g.setColor(color);
                g.fillRect(x, y, 1, 1);
            }
        }
    }

    private Color mapColor(int n) {
        double percentDone = (double) n / Mandelbrot.ITERATION_LIMIT;
        double redD, greenD;
        greenD = 0;
        int blue = (int) (Math.pow(percentDone, 4) * 255);
        if (percentDone > 0.2) {
            redD = 150 + (60 * Math.sin(percentDone * 60));
            blue = 255 - blue;
            greenD = 150 + (80 * Math.sin(percentDone * 50));
            if (percentDone > 0.8) {
                greenD = percentDone * 255;
            }
        } else {

            greenD = 500 * (percentDone);
            double blueD = 1250 * (percentDone);
            blue = (int) blueD;
            redD = 650 * (percentDone);
        }
        int green = (int) greenD;
        int red = (int) redD;

        if (green < 0)
            green = 0;
        if (blue < 0)
            blue = 0;
        if (red < 0)
            red = 0;
        return new Color(red, green, blue);
    }

    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        topLeft = view.translate(mouseX, mouseY);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        botRight = view.translate(mouseX, mouseY);
        view.setComplexCorners(topLeft, botRight);
        views[i] = view;
        i++;
        repaint();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C) {
            Mandelbrot.ITERATION_LIMIT += 1000;
            System.out.printf("Iterations: %d\n", Mandelbrot.ITERATION_LIMIT);
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_X) {
            if (Mandelbrot.ITERATION_LIMIT != 1000) {
                Mandelbrot.ITERATION_LIMIT -= 1000;
                repaint();
            }
            System.out.printf("Iterations: %d\n", Mandelbrot.ITERATION_LIMIT);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}