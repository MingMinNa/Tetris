package states.homeState;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.Map;

import states.ScreenPanel;
import musicPlayer.SoundType;

public class HomePanel extends JPanel implements ScreenPanel{
    public static final int FRAME_WIDTH = 800, FRAME_HEIGHT = 800;
    
    public HomePanel(SoundType sound_mode) {
        this.sound_mode = sound_mode;
        setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        setFocusable(true);
        setLayout(null);
        setBackground(Color.BLACK);

        buildEnterLabel();
        buildkeyboardDescription();
        buildGameCover();
        buildBackground_Img();
        buildSoundIcon();

        key_handler = new HomeKeyHandler();
        addKeyListener(key_handler);

        mouse_handler = new HomeMouseHandler(){
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        };
        addMouseListener(mouse_handler);
    }
    public void changeEnterLabelColor(){
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
            if(sound_mode == SoundType.UNMUTE) sound_mode = SoundType.MUTE;  
            else sound_mode = SoundType.UNMUTE;
            changeSoundIcon();
            return true;
        }
        return false;
    }
    // -----------------------
    // this function with different signature
    private JLabel labelMake(int center_x, int center_y, String words){
        int words_width = 100, words_height = 30;
        return labelMake(center_x, center_y, words, words_width, words_height, 20);
    }
    private void buildEnterLabel(){
        enter_game_label = labelMake(400 , 450, "Press Enter to play", 250, 50, 25);
        enter_game_label.setForeground(new Color(255, 255,255));
        // enter_game_label
        add(enter_game_label);
    }
    private void buildkeyboardDescription(){
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
    private void buildGameCover(){
        
        ImageIcon cover = new ImageIcon(Paths.get("img","game_img", "cover.png").toString());
        JLabel coverLabel = labelMake(400, 270, "", 250, 250, 20);
        coverLabel.setIcon(cover);
        add(coverLabel);
    }
    private void buildBackground_Img(){
        ImageIcon background_img1 = new ImageIcon(Paths.get("img","game_img", "background_img1.png").toString());
        JLabel background_Label1 = labelMake(FRAME_WIDTH / 4 + 10, (FRAME_HEIGHT / 3) * 2 + 50, "", FRAME_WIDTH / 2, FRAME_HEIGHT / 2, 20);
        background_Label1.setIcon(background_img1);
        add(background_Label1);
        
        ImageIcon background_img2 = new ImageIcon(Paths.get("img","game_img", "background_img2.png").toString());
        JLabel background_Label2 = labelMake((FRAME_WIDTH / 4) * 3 - 14, (FRAME_HEIGHT / 3) * 2 + 75, "", FRAME_WIDTH / 2, FRAME_HEIGHT / 2, 20);
        background_Label2.setIcon(background_img2);
        add(background_Label2);
    }
    private void buildSoundIcon(){
        sound_icon = labelMake(40, 40, "", 70, 70, 10);
        sound_icon.setForeground(Color.WHITE);
        changeSoundIcon();
        add(sound_icon);
    }
    
    private void changeSoundIcon(){
        final Map<SoundType, ImageIcon> SOUND_ICONS = new HashMap<>(){{
            put(SoundType.UNMUTE, new ImageIcon(Paths.get("img", "game_img", "unmute.png").toString()));
            put(SoundType.MUTE, new ImageIcon(Paths.get("img", "game_img", "mute.png").toString()));
        }};
        sound_icon.setIcon(SOUND_ICONS.get(this.sound_mode));
    }


    private HomeKeyHandler key_handler = null;
    private HomeMouseHandler mouse_handler = null;

    private JLabel enter_game_label = null;
    private int color_change = 1;

    private JLabel sound_icon = null;
    private SoundType sound_mode = SoundType.UNMUTE;
}
