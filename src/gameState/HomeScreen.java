package gameState;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


public class HomeScreen {
    public static boolean game_play = false; 
    public static int time_ticks = 60;
    public HomeScreen(JFrame frame){

        // MusicPlayer musicPlayer = new MusicPlayer("HomeScreen", true);
        // musicPlayer.start();
        
        HomePanel background_panel = new HomePanel();
        HomeKeyHandler.setPanel(background_panel);
        frame.getContentPane().add(background_panel);
        
        background_panel.requestFocusInWindow();
        while(true){
            
            frame.revalidate();
            frame.repaint();
            if(game_play == true){
                frame.getContentPane().remove(background_panel);
                break;
            }
        }
        // reset the setting for next loop
        game_play = false;
        // musicPlayer.stopPlaying();
    }
}
class HomePanel extends JPanel{
    public HomePanel() {
        setPreferredSize(new Dimension(300, 200));
        setFocusable(true);
        setBackground(Color.BLACK);
        setBounds(0, 0, 800, 800);
        setLayout(null);

        keyboard_description(this);
        game_cover();
        addKeyListener(new HomeKeyHandler());
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });
        
    }

    private JLabel labelMake(int center_x, int center_y, String words){
        int words_width = 100, words_height = 30;
        JLabel label = new JLabel(words);
        label.setBounds(center_x - words_width/2 , center_y - words_height/2, words_width, words_height);
        
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        return label;
    }
    private JLabel labelMake(int center_x, int center_y, String words, int words_width, int words_height){
        JLabel label = new JLabel(words);
        label.setBounds(center_x - words_width/2 , center_y - words_height/2, words_width, words_height);
        
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        return label;
    }
    private void keyboard_description(HomePanel background_panel){
        // Describe "How to play"
        int x_start = 350;
        int y_start = 500;
        JLabel space = labelMake(x_start, y_start, "Space");
        space.setForeground(Color.BLACK);
        space.setOpaque(true);

        JLabel down = labelMake(x_start, y_start + 40, "↓", 30, 30);
        down.setForeground(Color.BLACK);
        down.setOpaque(true);

        JLabel left = labelMake(x_start - 20, y_start + 40 * 2, "←", 30, 30);
        left.setForeground(Color.BLACK);
        left.setOpaque(true);

        JLabel right = labelMake(x_start + 20, y_start + 40 * 2, "→", 30, 30);
        right.setForeground(Color.BLACK);
        right.setOpaque(true);

        JLabel left_rotate = labelMake(x_start - 20, y_start + 40 * 3, "A", 30, 30);
        left_rotate.setForeground(Color.BLACK);
        left_rotate.setOpaque(true);

        JLabel right_rotate = labelMake(x_start + 20, y_start + 40 * 3, "D", 30, 30);
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
        
        background_panel.add(space);background_panel.add(down);
        background_panel.add(left);background_panel.add(right);background_panel.add(left_rotate);background_panel.add(right_rotate);

        background_panel.add(space_descrip);
        background_panel.add(down_descrip);
        background_panel.add(move_descrip);
        background_panel.add(rotate_descrip);
    }
    private void game_cover(){
        ImageIcon cover = new ImageIcon("img\\cover.png");
        JLabel coverLabel = labelMake(400, 300, "", 250, 250 );
        coverLabel.setIcon(cover);
        this.add(coverLabel);
    }

}

class HomeKeyHandler implements KeyListener{
    
    @Override
    public void keyPressed(KeyEvent e) {
        HomeScreen.game_play = true;
        control_panel.removeAll();
        control_panel.revalidate();
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void setPanel(HomePanel background_panel){control_panel = background_panel;}
    // ---------------------------
    private static HomePanel control_panel;
}