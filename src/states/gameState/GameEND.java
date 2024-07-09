package states.gameState;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import states.ScreenPanel;

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
            // [0] is released, [1] is pressed;
            Point mouse_touch[] = {game_end_panel.getMouseHandler().getReleased(), game_end_panel.getMouseHandler().getPressed()};
            boolean esc_touch[] = {game_end_panel.getKeyHandler().getReleased(), game_end_panel.getKeyHandler().getPressed()};
            if((checkTouchButton(mouse_touch[0]) || esc_touch[0]) && pressed_button)
                break;
            pressed_button = setButtonState(mouse_touch[1], esc_touch[1]);
            // add little delay to prevent error
            try {Thread.sleep(20);} 
            catch (InterruptedException e) {e.printStackTrace();}
        }
        setButtonState(null, false);
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
    private boolean setButtonState(Point pressed_point, boolean pressed_esc){
        Object [] button_panel_objects = game_end_panel.getButtonObjects();
        Color [] object_color = {Color.WHITE, Color.BLACK};
        boolean pressed = false;
        if(checkTouchButton(pressed_point) || pressed_esc){
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

        game_end_mouse_handler = new GameEndMouseHandler(){
            public void mouseClicked(MouseEvent e) {requestFocusInWindow();}
        };
        game_end_key_handler = new GameEndKeyHandler();

        addMouseListener(game_end_mouse_handler);
        addKeyListener(game_end_key_handler);
    }

    public void setTitle(String title, Color color){
        JLabel title_label = labelMake(GameEndPanel.PANEL_WIDTH / 2, GameEndPanel.PANEL_HEIGHT / 2 - 40, title, 400, 300, 50);
        title_label.setForeground(color);
        add(title_label);
        this.requestFocusInWindow();
    }
    
    public int [] getButtonRange(){
        return continue_button_range;
    }
    public GameEndMouseHandler getMouseHandler(){
        return game_end_mouse_handler;
    }
    public GameEndKeyHandler getKeyHandler(){
        return game_end_key_handler;
    }
    public Object [] getButtonObjects(){
        return button_panel_object;
    }
    
    // ---------------------------------
    private void buildContinueButton(){
        int button_panel_width = 100, button_panel_height = 40;
        JLabel continue_button_text = labelMake(button_panel_width / 2, button_panel_height / 2, "Continue", 40, 40, 20);
        JPanel continue_button_panel = new JPanel();
        continue_button_panel.setBounds((PANEL_WIDTH - button_panel_width) / 2, (PANEL_HEIGHT - button_panel_height) / 2 + 80, button_panel_width, button_panel_height);
        continue_button_panel.setBackground(Color.WHITE);
        continue_button_panel.add(continue_button_text);

        add(continue_button_panel);
        add(labelMake(PANEL_WIDTH / 2, PANEL_HEIGHT / 2 + 20, "Pressed Esc to continue", 250, 100, 20));


        button_panel_object[0] = continue_button_panel;
        button_panel_object[1] = continue_button_text;

        continue_button_range[0] = (PANEL_WIDTH - button_panel_width) / 2;
        continue_button_range[2] = continue_button_range[0] + button_panel_width;
        continue_button_range[1] = (PANEL_HEIGHT - button_panel_height) / 2 + 80;
        continue_button_range[3] = continue_button_range[1] + button_panel_height;
    }
    
    private int continue_button_range[] = new int[4]; // [0]:x_start, [1]:y_start, [2]:x_end, [3]:y_end
    private GameEndMouseHandler game_end_mouse_handler;
    private GameEndKeyHandler game_end_key_handler;
    private Object button_panel_object[] = new Object[2]; // [0]:panel, [1]:text
}

class GameEndMouseHandler implements MouseListener{
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


class GameEndKeyHandler implements KeyListener{
    @Override
    public void keyTyped(KeyEvent e){}
    @Override
    public void keyPressed(KeyEvent e){
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_ESCAPE)
            pressed_esc = true;
    }
    @Override
    public void keyReleased(KeyEvent e){
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_ESCAPE)
            released_esc = true;
    }

    public boolean getPressed(){
        return pressed_esc;
    }
    public boolean getReleased(){
        return released_esc;
    }
    private boolean released_esc, pressed_esc;
}
