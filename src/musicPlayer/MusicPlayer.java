package musicPlayer;


import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import java.nio.file.Paths;

public class MusicPlayer extends Thread {
    // mp3 file path
    public final static Map<String, String> music_paths = new HashMap<>(){{
        put("GameState1", Paths.get("music", "GameState1.mp3").toString());
        put("GameState2", Paths.get("music", "GameState2.mp3").toString());
        put("GameState3", Paths.get("music", "GameState3.mp3").toString());
        put("HomeState", Paths.get("music", "HomeState.mp3").toString());
        put("DeleteLine", Paths.get("music", "DeleteLine.mp3").toString());
    }};
    public MusicPlayer(String current_state, boolean repeat) {
        this.mp3_file_path = music_paths.get(current_state);
        this.repeat = repeat;
        this.ready = false;
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
            ready = true;
            player.play();
            
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }
    public void stopPlaying(){
        is_playing = false;
        // wait until the player is ready
        // or it will close prematurely before the "player = new Player()", this will raise error
        while(!ready){
            try{Thread.sleep(100);}
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        // player is ready, then close it
        player.close();
    }
    // -----------------------------
    private String mp3_file_path;
    private Player player;
    private boolean is_playing, repeat, ready;
    
    
}