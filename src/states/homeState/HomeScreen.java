package states.homeState;

import javax.swing.*;

import musicPlayer.MusicPlayer;
import musicPlayer.SoundType;

import states.homeState.*;

public class HomeScreen {
    public HomeScreen(JFrame frame, SoundType sound_mode){
        background_panel = new HomePanel(sound_mode);
        frame.getContentPane().add(background_panel);
        background_panel.requestFocusInWindow();
        this.sound_mode = sound_mode;

        initalMusicPlayer();
        while(background_panel.getKeyHandler().checkEnter() == false){

            background_panel.enterLabelColorChange();
            if(this.sound_mode != SoundType.PATH_ERROR && background_panel.checkClickSoundIcon())
            {
                // change sound_mode
                if(this.sound_mode == SoundType.UNMUTE) this.sound_mode = SoundType.MUTE;  
                else this.sound_mode = SoundType.UNMUTE;
                initalMusicPlayer();
            }
            frame.revalidate();
            frame.repaint();
            try{Thread.sleep(25);}
            catch(InterruptedException e){e.printStackTrace();}
        }
        
        stopMusicPlayer();
        removePanel(frame);
    }
    public SoundType getSoundMode(){
        return this.sound_mode;
    }
    // ----------------
    private void stopMusicPlayer(){
        // stop the player if the player isn't null
        if(music_player != null)
            {music_player.stopPlaying(); music_player.interrupt();}
        music_player = null;
    }
    
    private void initalMusicPlayer(){
        // start the player or not (based on sound mode)
        if(music_player != null) stopMusicPlayer();
        if(sound_mode == SoundType.UNMUTE) {
            music_player = new MusicPlayer("HomeState", true);
            music_player.start();
        }
        else music_player = null;
    }
    
    private void removePanel(JFrame frame){
        background_panel.removeAll();
        background_panel.revalidate();
        frame.getContentPane().remove(background_panel);
    }

    private SoundType sound_mode;
    private MusicPlayer music_player = null;
    private HomePanel background_panel;
}
