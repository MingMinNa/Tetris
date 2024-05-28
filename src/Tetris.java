import javax.swing.*;

import State.gameState.*;
import State.homeState.*;

public class Tetris {
    // windows width: FRAME_WIDTH; windows height: FRAME_HEIGHT
    public static final int FRAME_WIDTH = 800, FRAME_HEIGHT = 800;
    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("Tetris");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        // default sound mode (The default mode is unmute mode)
        boolean unmute = true;
        while(true){
            // check the sound icon => set the mode back to unmute (unmute mode or mute mode)
            unmute = (new HomeScreen(frame, unmute)).unmuteSetting();
            new GameScreen(frame, unmute);
        } 
    }
}
