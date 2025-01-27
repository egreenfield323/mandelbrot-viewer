package lib;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class ColorMapper extends JPanel {

    private static final long serialVersionUID = 1L;
    public final int ITERATION_LIMIT = 500;
    public final int GRAPH_WIDTH = 1000;
    public final int LEFT_MARGIN = 50;

    public ColorMapper() {
        setPreferredSize(new Dimension(GRAPH_WIDTH + 100, 400));
        setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.white);
        g.drawString("0", 48, 365);
        g.drawString("" + ITERATION_LIMIT, GRAPH_WIDTH + LEFT_MARGIN, 365);
        g.drawString("0", 32, 305);
        g.drawString("255", 32, 45);

        double stepSize = (double) ITERATION_LIMIT / GRAPH_WIDTH;
        double testVal = 0;
        for (int x = 0; x < GRAPH_WIDTH; x++) {
            testVal += stepSize;
            Color c = mapColor((int) testVal);
            g.setColor(c);
            g.fillRect(x + LEFT_MARGIN, 300, 1, 50);
            g.setColor(Color.red);
            g.fillRect(x + LEFT_MARGIN, 300 - c.getRed(), 1, 1);
            g.setColor(Color.blue);
            g.fillRect(x + LEFT_MARGIN, 300 - c.getBlue(), 1, 1);
            g.setColor(Color.green);
            g.fillRect(x + LEFT_MARGIN, 300 - c.getGreen(), 1, 1);
        }
    }

    private Color mapColor(int n) {

        double percentDone = (double) n / ITERATION_LIMIT;
        double redD;
        if (percentDone > 0.2) {
            redD = 150 + (30 * Math.sin(n * 0.1));
        } else {
            redD = n * 1.3;
        }
        int blue = (int) (Math.pow(percentDone, 4) * 255);
        blue = 255 - blue;
        int green = n / 2;
        int red = (int) redD;
        return new Color(red, green, blue);
    }
}