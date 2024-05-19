package State.homeState;

import javax.swing.*;

import music.MusicPlayer;

import java.awt.*;
import java.awt.event.*;

import java.util.HashMap;
import java.util.Map;


public class HomeScreen {
    public HomeScreen(JFrame frame, boolean unmute){
        HomePanel background_panel = new HomePanel(unmute);
        frame.getContentPane().add(background_panel);
        background_panel.requestFocusInWindow();
        this.unmute = unmute;

        initalMusicPlayer();
        while(background_panel.getKeyHandler().checkEnter() == false){

            background_panel.enterLabelColorChange();
            if(background_panel.checkClickSoundIcon())
            {
                this.unmute = !this.unmute;
                initalMusicPlayer();
            }
            frame.revalidate();
            frame.repaint();
            try{Thread.sleep(25);}
            catch(InterruptedException e){e.printStackTrace();}
        }
        
        stopMusicPlayer();
        background_panel.removeAll();
        background_panel.revalidate();
        frame.getContentPane().remove(background_panel);
    }
    public boolean unmuteSetting(){
        return this.unmute;
    }
    // ----------------
    private void stopMusicPlayer(){
        // stop the player if the player isn't null
        if(music_player != null)
            music_player.stopPlaying();
        music_player = null;
    }
    
    private void initalMusicPlayer(){
        // start the player or not (based on sound mode)
        if(music_player != null) stopMusicPlayer();
        if(unmute) {
            music_player = new MusicPlayer("HomeState", true);
            music_player.start();
        }
        else music_player = null;
    }
    
    private boolean unmute;
    private MusicPlayer music_player = null;
}
class HomePanel extends JPanel{
    public static final int FRAME_WIDTH = 800, FRAME_HEIGHT = 800;
    
    public HomePanel(boolean unmute) {
        this.unmute = unmute;
        setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        setFocusable(true);
        setLayout(null);
        setBackground(Color.BLACK);

        setEnterLabel();
        setkeyboardDescription();
        setGameCover();
        setBackground_Img();
        setSoundIcon();

        key_handler = new HomeKeyHandler();
        addKeyListener(key_handler);

        mouse_handler = new HomeMouseHandler(){
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        };
        addMouseListener(mouse_handler);
    }
    public void enterLabelColorChange(){
        // this function makes the enter_game_label flashing
        int r = enter_game_label.getForeground().getRed();
        int g = enter_game_label.getForeground().getGreen();
        int b = enter_game_label.getForeground().getBlue();
        if((r == 0 && g == 0 && b == 0) || (r == 255 && g == 255 && b == 255))
            color_change *= (-1);
        r += color_change*5; g += color_change*5; b += color_change*5;
        enter_game_label.setForeground(new Color(r, g, b));
    }
    public HomeKeyHandler getKeyHandler(){
        return key_handler;
    }
    public HomeMouseHandler getMouseHandler(){
        return mouse_handler;
    }
    
    public boolean checkClickSoundIcon(){
        int [] sound_icon_range = {5, 5, 75, 75}; // [0]:x_start, [1]:y_start, [2]:x_end, [3]:y_end
        Point released_point = this.getMouseHandler().getReleased();
        if(released_point != null &&
            released_point.getX() >= sound_icon_range[0] && released_point.getX() <= sound_icon_range[2] &&
            released_point.getY() >= sound_icon_range[1] && released_point.getY() <= sound_icon_range[3])
        {
            this.getMouseHandler().resetReleased();
            unmute = !unmute;
            changeSoundIcon();
            return true;
        }
        return false;
    }
    // -----------------------
    private JLabel labelMake(int center_x, int center_y, String words){
        int words_width = 100, words_height = 30;
        return labelMake(center_x, center_y, words, words_width, words_height, 20);
    }
    private JLabel labelMake(int center_x, int center_y, String words, int words_width, int words_height, int words_size){
        JLabel label = new JLabel(words);
        label.setBounds(center_x - words_width/2 , center_y - words_height/2, words_width, words_height);
        
        label.setFont(new Font("Arial", Font.BOLD, words_size));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        return label;
    }
    private void setEnterLabel(){
        enter_game_label = labelMake(400 , 450, "Press Enter to play", 250, 50, 25);
        enter_game_label.setForeground(new Color(255, 255,255));
        // enter_game_label
        add(enter_game_label);
    }
    private void setkeyboardDescription(){
        // Describe "How to play"
        int x_start = 350;
        int y_start = 550;
        JLabel space = labelMake(x_start, y_start, "Space");
        space.setForeground(Color.BLACK);
        space.setOpaque(true);

        JLabel down = labelMake(x_start, y_start + 40, "↓", 30, 30, 20);
        down.setForeground(Color.BLACK);
        down.setOpaque(true);

        JLabel left = labelMake(x_start - 20, y_start + 40 * 2, "←", 30, 30, 20);
        left.setForeground(Color.BLACK);
        left.setOpaque(true);

        JLabel right = labelMake(x_start + 20, y_start + 40 * 2, "→", 30, 30, 20);
        right.setForeground(Color.BLACK);
        right.setOpaque(true);

        JLabel left_rotate = labelMake(x_start - 20, y_start + 40 * 3, "A", 30, 30, 20);
        left_rotate.setForeground(Color.BLACK);
        left_rotate.setOpaque(true);

        JLabel right_rotate = labelMake(x_start + 20, y_start + 40 * 3, "D", 30, 30, 20);
        right_rotate.setForeground(Color.BLACK);
        right_rotate.setOpaque(true);

        JLabel space_descrip = labelMake(x_start + 150, y_start, "Hard drop");
        space_descrip.setForeground(Color.WHITE);

        JLabel down_descrip = labelMake(x_start + 150, y_start + 40 * 1, "Soft drop");
        down_descrip.setForeground(Color.WHITE);

        JLabel move_descrip = labelMake(x_start + 150, y_start + 40 * 2, "Move");
        move_descrip.setForeground(Color.WHITE);

        JLabel rotate_descrip = labelMake(x_start + 150, y_start + 40 * 3, "Rotate");
        rotate_descrip.setForeground(Color.WHITE);
        
        add(space);add(down);
        add(left);add(right);
        add(left_rotate);add(right_rotate);

        add(space_descrip);
        add(down_descrip);
        add(move_descrip);
        add(rotate_descrip);
    }
    private void setGameCover(){
        ImageIcon cover = new ImageIcon("img\\cover.png");
        JLabel coverLabel = labelMake(400, 270, "", 250, 250, 20);
        coverLabel.setIcon(cover);
        add(coverLabel);
    }
    private void setBackground_Img(){
        ImageIcon background_img1 = new ImageIcon("img\\background_img1.png");
        JLabel background_Label1 = labelMake(FRAME_WIDTH / 4 + 10, (FRAME_HEIGHT / 3) * 2 + 50, "", FRAME_WIDTH / 2, FRAME_HEIGHT / 2, 20);
        background_Label1.setIcon(background_img1);
        add(background_Label1);
        
        ImageIcon background_img2 = new ImageIcon("img\\background_img2.png");
        JLabel background_Label2 = labelMake((FRAME_WIDTH / 4) * 3 - 14, (FRAME_HEIGHT / 3) * 2 + 75, "", FRAME_WIDTH / 2, FRAME_HEIGHT / 2, 20);
        background_Label2.setIcon(background_img2);
        add(background_Label2);
    }
    private void setSoundIcon(){
        sound_icon = labelMake(40, 40, "", 70, 70, 10);
        sound_icon.setForeground(Color.WHITE);
        changeSoundIcon();
        add(sound_icon);
    }
    
    private void changeSoundIcon(){
        final Map<Boolean, ImageIcon> SOUND_ICONS = new HashMap<>(){{
            put(true, new ImageIcon("img\\unmute.png"));
            put(false, new ImageIcon("img\\mute.png"));
        }};
        sound_icon.setIcon(SOUND_ICONS.get(unmute));
    }


    private HomeKeyHandler key_handler = null;
    private HomeMouseHandler mouse_handler = null;

    private JLabel enter_game_label = null;
    private int color_change = 1;

    private JLabel sound_icon = null;
    private boolean unmute = true;
}

class HomeKeyHandler implements KeyListener{
    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {
        // Press Enter to play game
        int key_code = e.getKeyCode();
        if(key_code == KeyEvent.VK_ENTER)
            pressed_enter = true;
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    
    public boolean checkEnter(){
        return pressed_enter;
    }
    // ---------------------------
    private boolean pressed_enter = false;
}

class HomeMouseHandler implements MouseListener{
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {
        released_point = e.getPoint();
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    public Point getReleased(){
        return released_point;
    }
    public void resetReleased(){
        released_point = null;
    }
    // ---------------
    private Point released_point = null;
}