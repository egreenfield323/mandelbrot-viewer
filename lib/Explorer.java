package lib;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Explorer extends JPanel implements MouseListener, KeyListener {

    int screenWidth = 1280;
    int screenHeight = 720;

    Complex topLeft = new Complex(-2, 1);
    Complex botRight = new Complex(1, -1);
    View view = new View(screenWidth, screenHeight);

    // Calculate the number of threads based on available processors
    int numThreads = (int) (Runtime.getRuntime().availableProcessors() * 0.75);
    ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

    // Array holding images for each chunk
    private BufferedImage[] chunkImages;

    public Explorer() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.WHITE);
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(isFocusable());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        chunkImages = new BufferedImage[numThreads];
        int chunkHeight = screenHeight / numThreads; // Divide the screen height into chunks

        // Initialize the images for each chunk
        for (int i = 0; i < numThreads; i++) {
            chunkImages[i] = new BufferedImage(screenWidth, chunkHeight, BufferedImage.TYPE_INT_ARGB);
        }

        // Track when all tasks are complete
        CountDownLatch latch = new CountDownLatch(numThreads);

        // Submit rendering tasks for each chunk
        for (int i = 0; i < numThreads; i++) {
            final int startY = i * chunkHeight;
            final int endY = (i == numThreads - 1) ? screenHeight : (i + 1) * chunkHeight;
            final int chunkIndex = i;

            // Submit a task for each chunk of the screen
            executorService.submit(() -> {
                renderChunk(chunkImages[chunkIndex], startY, endY);
                latch.countDown(); // Decrease the latch count once task is complete
            });
        }

        try {
            latch.await(); // Wait until all chunks are rendered
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < numThreads; i++) {
            g.drawImage(chunkImages[i], 0, i * (screenHeight / numThreads), null);
        }
    }

    private void renderChunk(BufferedImage image, int startY, int endY) {
        Graphics2D g = image.createGraphics();
        for (int x = 0; x < screenWidth; x++) {
            for (int y = startY; y < endY; y++) {
                view.setComplexCorners(topLeft, botRight);
                Complex c = view.translate(x, y);
                int colorVal = Mandelbrot.testPoint(c);
                Color color = mapColor(colorVal);
                g.setColor(color);
                g.fillRect(x, y - startY, 1, 1);
            }
        }
        g.dispose();
    }

    private Color mapColor(int n) {
        double percentDone = (double) n / Mandelbrot.ITERATION_LIMIT;
        double redD, greenD;

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

    @Override
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

        Complex dragEnd = view.translate(mouseX, mouseY);

        double selectedWidth = dragEnd.getR() - topLeft.getR();
        double selectedHeight = dragEnd.getI() - topLeft.getI();

        double aspectRatio = (double) screenWidth / screenHeight;

        if (Math.abs(selectedWidth / selectedHeight) > aspectRatio) {
            selectedHeight = selectedWidth / aspectRatio;
        } else {
            selectedWidth = selectedHeight * aspectRatio;
        }
        botRight = new Complex(topLeft.getR() + selectedWidth, topLeft.getI() + selectedHeight);
        view.setComplexCorners(topLeft, botRight);
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
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
            if (Mandelbrot.ITERATION_LIMIT != 500) {
                Mandelbrot.ITERATION_LIMIT -= 1000;
                repaint();
            }
            System.out.printf("Iterations: %d\n", Mandelbrot.ITERATION_LIMIT);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void shutdown() {
        executorService.shutdown();
    }
}