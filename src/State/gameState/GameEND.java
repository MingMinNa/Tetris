package State.gameState;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameEND {

    public GameEND(JFrame frame, boolean game_over, GamePanel game_panel){
        JPanel game_end_panel = null;
        if(game_over)   game_end_panel = new GameOverPanel();
        game_end_panel.requestFocusInWindow();
        game_panel.add(game_end_panel);
        frame.getContentPane().add(game_panel);
        
        game_panel.revalidate();
        frame.revalidate();
        frame.repaint();
        while(true);
    }
    // ---------------------------------
}

class GameOverPanel extends JPanel{

    public static final int PANEL_WIDTH = 450, PANEL_HEIGHT = 300;
    public GameOverPanel(){
        setBounds(  (GamePanel.FRAME_WIDTH - PANEL_WIDTH) / 2, 
                    (GamePanel.FRAME_HEIGHT - PANEL_HEIGHT) / 2,
                    GameOverPanel.PANEL_WIDTH,
                    GameOverPanel.PANEL_HEIGHT);
        // setBounds(0, 0, 600, 600);
        setFocusable(true);
        setBackground(Color.WHITE);
        setLayout(null);

        addMouseListener(new GameOverHandler(){
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });

    }
    // ---------------------------------
    private JLabel labelMake(int center_x, int center_y, String words, int words_width, int words_height){
        return labelMake(center_x, center_y, words, words_width, words_height, 20);
    }
    private JLabel labelMake(int center_x, int center_y, String words, int words_width, int words_height, int font_size){
        JLabel label = new JLabel(words);
        label.setBounds(center_x - words_width/2 , center_y - words_height/2, words_width, words_height);
        
        label.setFont(new Font("Arial", Font.BOLD, font_size));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        return label;
    }
}
class GameOverHandler implements MouseListener{
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse released at: " + e.getPoint());
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
