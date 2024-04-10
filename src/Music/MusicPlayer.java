package music;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class MusicPlayer extends Thread {
    
    public MusicPlayer(String current_state, boolean repeat) {
        this.mp3_file_path = state_music.get(current_state);
        this.repeat = repeat;
    }
    @Override
    public void run() {
        is_playing = true;
        if(repeat == true){
            // After the music playback completed, replay it.
            while(is_playing){
                startSong();
            }
        }
        else{
            // Once the music playback is complete, close the player.
            startSong();
            player.close();
        }
    }
    public void startSong(){
        try {
            FileInputStream file_input_stream = new FileInputStream(mp3_file_path);
            player = new Player(file_input_stream);
            player.play();
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }
    public void stopPlaying(){
        is_playing = false;
        player.close();
    }
    // -----------------------------
    private String mp3_file_path;
    private Player player;
    private boolean is_playing, repeat;
    private final Map<String, String> state_music = new HashMap<>(){{
        put("GameState1", "sound\\GameState1.mp3");
        put("GameState2", "sound\\GameState2.mp3");
        put("GameState3", "sound\\GameState3.mp3");
    }};
}