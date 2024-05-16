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
        if(game_over == true)   game_end_panel.setTitle("Game Over", Color.RED);
        else                    game_end_panel.setTitle("Game Clear", Color.GREEN);
        game_end_panel.revalidate();
        game_end_panel.getParent().revalidate();
        game_end_panel.getParent().repaint();

        while(true){
            Point released_point = game_end_panel.getHandler().getReleased();
            Point pressed_point = game_end_panel.getHandler().getPressed();
            if(checkTouchButton(released_point))
                break;
            else if (checkTouchButton(pressed_point)){
                // System.out.println("Hello");
            }
            try {Thread.sleep(50);} 
            catch (InterruptedException e) {e.printStackTrace();}
        }
    }
    // -----------------------------------
    private boolean checkTouchButton(Point touch_point){
        int [] button_range = game_end_panel.getButtonRange();
        if(touch_point != null &&
        touch_point.getX() >= button_range[0] && touch_point.getX() <= button_range[2] &&
        touch_point.getY() >= button_range[1] && touch_point.getY() <= button_range[3])
            return true;
        return false;
    }
    private GameEndPanel game_end_panel;
}

class GameEndPanel extends JPanel{

    public static final int PANEL_WIDTH = 400, PANEL_HEIGHT = 350;
    public GameEndPanel(){
        setBounds(  (GamePanel.FRAME_WIDTH - PANEL_WIDTH) / 2, 
                    (GamePanel.FRAME_HEIGHT - PANEL_HEIGHT) / 2,
                    GameEndPanel.PANEL_WIDTH,
                    GameEndPanel.PANEL_HEIGHT);
        setFocusable(true);
        setBackground(Color.GRAY);
        setLayout(null);
        setVisible(false);

        continueButtonMake();

        game_end_handler = new GameEndHandler(){
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        };
        addMouseListener(game_end_handler);
    }
    public void setTitle(String title, Color color){
        JLabel title_label = labelMake(GameEndPanel.PANEL_WIDTH / 2, GameEndPanel.PANEL_HEIGHT / 2 - 40, title, 400, 300, 50);
        title_label.setForeground(color);
        add(title_label);
    }
    public int [] getButtonRange(){
        return continue_button_range;
    }
    public GameEndHandler getHandler(){
        return game_end_handler;
    }
    // ---------------------------------
    private JLabel labelMake(int center_x, int center_y, String words, int words_width, int words_height, int font_size){
        JLabel label = new JLabel(words);
        label.setBounds(center_x - words_width/2 , center_y - words_height/2, words_width, words_height);
        
        label.setFont(new Font("Arial", Font.BOLD, font_size));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        return label;
    }
    private void continueButtonMake(){
        int button_panel_width = 100, button_panel_height = 40;
        JLabel continue_button_text = labelMake(button_panel_width / 2, button_panel_height / 2, "Continue", 40, 40, 20);
        JPanel continue_button_panel = new JPanel();
        continue_button_panel.setBounds((PANEL_WIDTH - button_panel_width) / 2, (PANEL_HEIGHT - button_panel_height) / 2 + 50, button_panel_width, button_panel_height);
        continue_button_panel.setBackground(Color.WHITE);
        continue_button_panel.add(continue_button_text);
        add(continue_button_panel);

        continue_button_range[0] = (PANEL_WIDTH - button_panel_width) / 2;
        continue_button_range[2] = continue_button_range[0] + button_panel_width;
        continue_button_range[1] = (PANEL_HEIGHT - button_panel_height) / 2 + 50;
        continue_button_range[3] = continue_button_range[1] + button_panel_height;
    }
    
    private int continue_button_range[] = new int[4]; // [0]:x_start, [1]:y_start, [2]:x_end, [3]:y_end
    private GameEndHandler game_end_handler;
}

class GameEndHandler implements MouseListener{
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        pressed_point = e.getPoint();
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        released_point = e.getPoint();
        pressed_point = null;
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    public Point getPressed(){
        return pressed_point;
    }
    public Point getReleased(){
        return released_point;
    }
    private Point pressed_point = null, released_point = null;
}
