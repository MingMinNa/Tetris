import javax.swing.*;

import gameState.*;

public class Tetris {
    // windows width: FRAME_WIDTH; windows height: FRAME_HEIGHT
    public static final int FRAME_WIDTH = 800, FRAME_HEIGHT = 800;
    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("Tetris");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        while(true){
            new HomeScreen(frame);
            new GameScreen(frame);
        } 
    }
}
