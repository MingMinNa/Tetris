package gameState;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomeScreen {
    public static boolean game_play = false; 
    public static int time_ticks = 60;
    public HomeScreen(JFrame frame){
        HomePanel background_panel = new HomePanel();
        frame.getContentPane().add(background_panel);
        
        while(true){

            frame.revalidate();
            frame.repaint();
            if(game_play == true){
                frame.getContentPane().remove(background_panel);
                break;
            }
        }
    }
}
class HomePanel extends JPanel implements KeyListener {
    public HomePanel() {
        // 設置 JPanel 的屬性
        setPreferredSize(new Dimension(300, 200));
        setFocusable(true);
        setBackground(Color.BLACK);
        setBounds(0, 0, 800, 800);
        setLayout(null);
        requestFocusInWindow();

        keyboard_description(this);
        game_cover();
        addKeyListener(this);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }
    public void keyPressed(KeyEvent e) {
        HomeScreen.game_play = true;
        this.removeAll();
        this.revalidate();
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    

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
        int right_shift_pixel = 350;
        JLabel space = labelMake(right_shift_pixel, 500, "Space");
        space.setForeground(Color.BLACK);
        space.setOpaque(true);

        JLabel down = labelMake(right_shift_pixel, 540, "↓", 30, 30);
        down.setForeground(Color.BLACK);
        down.setOpaque(true);

        JLabel left = labelMake(right_shift_pixel - 20, 580, "←", 30, 30);
        left.setForeground(Color.BLACK);
        left.setOpaque(true);

        JLabel right = labelMake(right_shift_pixel + 20, 580, "→", 30, 30);
        right.setForeground(Color.BLACK);
        right.setOpaque(true);

        JLabel shift = labelMake(right_shift_pixel, 620, "Shift");
        shift.setForeground(Color.BLACK);
        shift.setOpaque(true);

        JLabel space_descrip = labelMake(right_shift_pixel + 150, 500, "Hard drop");
        space_descrip.setForeground(Color.WHITE);

        JLabel down_descrip = labelMake(right_shift_pixel + 150, 540, "Soft drop");
        down_descrip.setForeground(Color.WHITE);

        JLabel move_descrip = labelMake(right_shift_pixel + 150, 580, "Move");
        move_descrip.setForeground(Color.WHITE);

        JLabel shift_descrip = labelMake(right_shift_pixel + 150, 620, "Rotate");
        shift_descrip.setForeground(Color.WHITE);
        
        background_panel.add(space);background_panel.add(down);
        background_panel.add(left);background_panel.add(right);background_panel.add(shift);

        background_panel.add(space_descrip);
        background_panel.add(down_descrip);
        background_panel.add(move_descrip);
        background_panel.add(shift_descrip);
    }
    private void game_cover(){
        ImageIcon cover = new ImageIcon("img\\cover.png");
        JLabel coverLabel = labelMake(400, 300, "", 250, 250 );
        coverLabel.setIcon(cover);
        this.add(coverLabel);
    }

}
