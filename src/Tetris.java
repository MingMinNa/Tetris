import javax.swing.*;
import gameState.*;

// 800, 800
public class Tetris {
    public static int frame_width = 800, frame_height = 800;
    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("Tetris");
        frame.setSize(frame_width, frame_height);
        
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        new HomeScreen(frame);
        // System.out.println("Hello");
        new GameScreen(frame);
        // System.out.println("Game End");
        frame.revalidate();
        frame.repaint();
    }
}