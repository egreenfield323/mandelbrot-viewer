import javax.swing.JFrame;

public class Runner {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Mandelbrot Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Explorer explorer = new Explorer();
        frame.getContentPane().add(explorer);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

        explorer.requestFocusInWindow();
    }
}