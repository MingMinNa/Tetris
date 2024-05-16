package State.gameState;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameEND{
    public GameEND(GamePanel background_panel){
        game_end_panel = null;
        game_end_panel = new GameEndPanel();
        background_panel.add(game_end_panel);
    }
    public void GameEndState(boolean game_over){
        game_end_panel.requestFocusInWindow();
        game_end_panel.setVisible(true);
        if(game_over == true)   game_end_panel.setTitle("Game Over");
        else                    game_end_panel.setTitle("Game Clear");
        game_end_panel.revalidate();
        game_end_panel.getParent().revalidate();
        game_end_panel.getParent().repaint();

        while(true){
            boolean button_clicked = GameEndHandler.getCurrentClick();
            if(button_clicked == true)
                break;
        }
        GameEndHandler.reset();
    }
    private GameEndPanel game_end_panel;
}

class GameEndPanel extends JPanel{

    public static final int PANEL_WIDTH = 450, PANEL_HEIGHT = 300;
    public GameEndPanel(){
        setBounds(  (GamePanel.FRAME_WIDTH - PANEL_WIDTH) / 2, 
                    (GamePanel.FRAME_HEIGHT - PANEL_HEIGHT) / 2,
                    GameEndPanel.PANEL_WIDTH,
                    GameEndPanel.PANEL_HEIGHT);
        setFocusable(true);
        setBackground(Color.GRAY);
        setLayout(null);
        setVisible(false);
        addMouseListener(new GameEndHandler(){
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }
    public void setTitle(String title){
        JLabel title_label = labelMake(GameEndPanel.PANEL_WIDTH / 2, GameEndPanel.PANEL_HEIGHT / 2 - 40, title, 400, 300, 50);
        title_label.setForeground(Color.WHITE);
        add(title_label);
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

class GameEndHandler implements MouseListener{
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        clicked = true;
        System.out.println("Mouse released at: " + e.getPoint());
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    public static boolean getCurrentClick(){
        return clicked;
    }
    public static void reset(){
        clicked = false;
    }
    private static boolean clicked = false;
}
