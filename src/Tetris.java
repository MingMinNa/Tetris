import java.io.File;
import java.util.Collection;

import javax.swing.*;

import states.gameState.*;
import states.homeState.*;
import musicPlayer.MusicPlayer;
import musicPlayer.SoundType;

public class Tetris {
    // windows width: FRAME_WIDTH; windows height: FRAME_HEIGHT
    public static final int FRAME_WIDTH = 800, FRAME_HEIGHT = 800;
    public static JFrame frame = null;
    public static SoundType sound_mode = SoundType.MUTE;
    public static void main(String[] args) throws Exception {

        frame = new JFrame("Tetris");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        // default sound mode (The default mode is MUTE mode)
        sound_mode = checkMP3Exist();
        while(true){
            // check the sound icon => set the mode back to unmute (unmute mode or mute mode)
            sound_mode = (new HomeScreen(frame, sound_mode)).getSoundMode();
            new GameScreen(frame, sound_mode);
        } 
    }

    private static SoundType checkMP3Exist(){
        Collection<String> mp3_paths = MusicPlayer.music_paths.values();
        for(String path: mp3_paths){
            File  mp3_file = new File(path);
            if(mp3_file.exists() == false)  return SoundType.PATH_ERROR;
        }
        return SoundType.MUTE;
    }
}
