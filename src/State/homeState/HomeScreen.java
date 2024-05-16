package State.homeState;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


public class HomeScreen {

    public HomeScreen(JFrame frame){
        
        HomePanel background_panel = new HomePanel();
        frame.getContentPane().add(background_panel);
        background_panel.requestFocusInWindow();

        while(background_panel.getHandler().checkEnter() == false){
            frame.revalidate();
            frame.repaint();
            try{Thread.sleep(25);}
            catch(InterruptedException e){e.printStackTrace();}
            background_panel.enterLabelColorChange();
        }
        background_panel.removeAll();
        background_panel.revalidate();
        frame.getContentPane().remove(background_panel);
    }
}
class HomePanel extends JPanel{
    public int FRAME_WIDTH = 800, FRAME_HEIGHT = 800;
    public HomePanel() {
        setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        setFocusable(true);
        setBackground(Color.BLACK);
        setLayout(null);

        enter_game_label = labelMake(400 , 480, "Press Enter to play", 250, 200, 25);
        enter_game_label.setForeground(new Color(255, 255,255));
        // enter_game_label
        add(enter_game_label);

        keyboardDescription();
        gameCover();

        handler = new HomeKeyHandler();
        addKeyListener(handler);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });
        
    }
    public void enterLabelColorChange(){
        int r = enter_game_label.getForeground().getRed();
        int g = enter_game_label.getForeground().getGreen();
        int b = enter_game_label.getForeground().getBlue();
        if((r == 0 && g == 0 && b == 0) || (r == 255 && g == 255 && b == 255))
            color_change *= (-1);
        r += color_change*5; g += color_change*5; b += color_change*5;
        enter_game_label.setForeground(new Color(r, g, b));
    }
    public HomeKeyHandler getHandler(){
        return handler;
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
    private void keyboardDescription(){
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
    private void gameCover(){
        ImageIcon cover = new ImageIcon("img\\cover.png");
        JLabel coverLabel = labelMake(400, 300, "", 250, 250, 20);
        coverLabel.setIcon(cover);
        add(coverLabel);
    }
    
    private HomeKeyHandler handler = null;
    private JLabel enter_game_label = null;
    private int color_change = 1;
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