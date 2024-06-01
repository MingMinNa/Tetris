package State.gameState;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import State.ScreenPanel;

public class GameEND{
    public GameEND(GamePanel background_panel){
        game_end_panel = null;
        game_end_panel = new GameEndPanel();
        background_panel.add(game_end_panel);
    }
    public void GameEndState(boolean game_over){
        // game_over = false;
        game_end_panel.requestFocusInWindow();
        game_end_panel.setVisible(true);
        if(game_over == true)   game_end_panel.setTitle("Game Over", new Color(139, 69, 19));
        else                    game_end_panel.setTitle("Game Clear", new Color(192, 255, 62));
        game_end_panel.revalidate();
        game_end_panel.getParent().revalidate();
        game_end_panel.getParent().repaint();

        boolean pressed_button = false;
        while(true){
            Point released_point = game_end_panel.getHandler().getReleased();
            Point pressed_point = game_end_panel.getHandler().getPressed();
            if(checkTouchButton(released_point) && pressed_button)
                break;
            pressed_button = setButtonState(pressed_point);
            // add little delay to prevent error
            try {Thread.sleep(20);} 
            catch (InterruptedException e) {e.printStackTrace();}
        }
        setButtonState(null);
        try {Thread.sleep(100);} 
        catch (InterruptedException e) {e.printStackTrace();}
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
    private boolean setButtonState(Point pressed_point){
        Object [] button_panel_objects = game_end_panel.getButtonObjects();
        Color [] object_color = {Color.WHITE, Color.BLACK};
        boolean pressed = false;
        if(checkTouchButton(pressed_point)){
            object_color[0] = Color.BLACK;
            object_color[1] = Color.WHITE;
            pressed = true;
        }
        
        if(button_panel_objects[0] instanceof JPanel)
            ((JPanel)button_panel_objects[0]).setBackground(object_color[0]);
        if(button_panel_objects[1] instanceof JLabel)
            ((JLabel)button_panel_objects[1]).setForeground(object_color[1]);
        return pressed;
    }
    private GameEndPanel game_end_panel;
}

class GameEndPanel extends JPanel implements ScreenPanel{

    public static final int PANEL_WIDTH = 400, PANEL_HEIGHT = 300;
    public GameEndPanel(){
        setBounds(  (GamePanel.FRAME_WIDTH - PANEL_WIDTH) / 2, 
                    (GamePanel.FRAME_HEIGHT - PANEL_HEIGHT) / 2,
                    GameEndPanel.PANEL_WIDTH,
                    GameEndPanel.PANEL_HEIGHT);
        setFocusable(true);
        setBackground(new Color(139, 121, 94));
        setLayout(null);
        setVisible(false);

        buildContinueButton();

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
    public Object [] getButtonObjects(){
        return button_panel_object;
    }
    // ---------------------------------
    private void buildContinueButton(){
        int button_panel_width = 100, button_panel_height = 40;
        JLabel continue_button_text = labelMake(button_panel_width / 2, button_panel_height / 2, "Continue", 40, 40, 20);
        JPanel continue_button_panel = new JPanel();
        continue_button_panel.setBounds((PANEL_WIDTH - button_panel_width) / 2, (PANEL_HEIGHT - button_panel_height) / 2 + 50, button_panel_width, button_panel_height);
        continue_button_panel.setBackground(Color.WHITE);
        continue_button_panel.add(continue_button_text);
        add(continue_button_panel);

        button_panel_object[0] = continue_button_panel;
        button_panel_object[1] = continue_button_text;

        continue_button_range[0] = (PANEL_WIDTH - button_panel_width) / 2;
        continue_button_range[2] = continue_button_range[0] + button_panel_width;
        continue_button_range[1] = (PANEL_HEIGHT - button_panel_height) / 2 + 50;
        continue_button_range[3] = continue_button_range[1] + button_panel_height;
    }
    
    private int continue_button_range[] = new int[4]; // [0]:x_start, [1]:y_start, [2]:x_end, [3]:y_end
    private GameEndHandler game_end_handler;
    private Object button_panel_object[] = new Object[2]; // [0]:panel, [1]:text
}

class GameEndHandler implements MouseListener{
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        pressed_point = e.getPoint();
        released_point = null;
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
