package gameState;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


public class HomeScreen {
    public static boolean game_play = false; 
    public static int time_ticks = 60; // current is useless 

    public HomeScreen(JFrame frame){
        
        game_play = false;
        HomePanel background_panel = new HomePanel();
        HomeKeyHandler.setPanel(background_panel);
        frame.getContentPane().add(background_panel);
        background_panel.requestFocusInWindow();

        while(game_play == false){
            frame.revalidate();
            frame.repaint();
        }

        frame.getContentPane().remove(background_panel);
    }
}
class HomePanel extends JPanel{
    public int FRAME_WIDTH = 800, FRAME_HEIGHT = 800;
    public HomePanel() {
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setFocusable(true);
        setBackground(Color.BLACK);
        setLayout(null);

        keyboard_description();
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
        return labelMake(center_x, center_y, words, words_width, words_height);
    }
    private JLabel labelMake(int center_x, int center_y, String words, int words_width, int words_height){
        JLabel label = new JLabel(words);
        label.setBounds(center_x - words_width/2 , center_y - words_height/2, words_width, words_height);
        
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        return label;
    }
    private void keyboard_description(){
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
        
        add(space);add(down);
        add(left);add(right);
        add(left_rotate);add(right_rotate);

        add(space_descrip);
        add(down_descrip);
        add(move_descrip);
        add(rotate_descrip);
    }
    private void game_cover(){
        ImageIcon cover = new ImageIcon("img/cover.png");
        JLabel coverLabel = labelMake(400, 300, "", 250, 250 );
        coverLabel.setIcon(cover);
        add(coverLabel);
    }

}

class HomeKeyHandler implements KeyListener{
    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {
        // Press any to play game, remove the panel in the frame.
        HomeScreen.game_play = true;
        control_panel.removeAll();
        control_panel.revalidate();
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void setPanel(HomePanel background_panel){control_panel = background_panel;}
    // ---------------------------
    private static HomePanel control_panel;
}