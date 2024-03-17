package Music;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class MusicPlayer extends Thread {
    
    public MusicPlayer(String current_state, boolean repeat) {
        this.mp3FilePath = state_music.get(current_state);
        this.repeat = repeat;
    }
    @Override
    public void run() {
        isPlaying = true;
        if(repeat == true){
            while(isPlaying){
                startSong();
            }
        }
        else{
            startSong();
            player.close();
        }
    }
    public void startSong(){
        try {
            FileInputStream fileInputStream = new FileInputStream(mp3FilePath);
            player = new Player(fileInputStream);
            player.play();
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }
    public void stopPlaying(){
        isPlaying = false;
        player.close();
    }
    // -----------------------------
    private String mp3FilePath;
    private Player player;
    private boolean isPlaying, repeat;
    private final Map<String, String> state_music = new HashMap<>(){{
        put("GameState1", "sound\\拳皇八神.mp3");
        put("GameState2", "sound\\拳皇京.mp3");
        put("GameState3", "sound\\拳皇八神.mp3");
    }};
}