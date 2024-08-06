package states.gameState.end;
import java.awt.*;
import javax.swing.*;

import states.gameState.runner.GamePanel;

public class GameEND{
    public GameEND(GamePanel background_panel, boolean game_over){
        game_end_panel = new GameEndPanel(game_over);
        background_panel.add(game_end_panel);
        background_panel.setComponentZOrder(game_end_panel, 0);
        parent_panel = background_panel;
        game_end_panel.requestFocusInWindow();
        game_end_panel.revalidate();
        parent_panel.revalidate();
        parent_panel.repaint();

        boolean pressed_button = false;
        while(true){
            
            boolean pressed_esc = game_end_panel.getKeyHandler().getEscPressed();
            if(pressed_esc == true) game_end_panel.setTransparent(true);
            else                    game_end_panel.setTransparent(false);
            
            // [0] is released, [1] is pressed;
            Point mouse_touch[] = {game_end_panel.getMouseHandler().getReleased(), game_end_panel.getMouseHandler().getPressed()};
            if((checkTouchButton(mouse_touch[0])) && pressed_button && pressed_esc == false)
                break;
            pressed_button = setButtonState(mouse_touch[1]);
            game_end_panel.requestFocusInWindow();
            game_end_panel.revalidate();
            parent_panel.revalidate();
            parent_panel.repaint();
            // add little delay to prevent error
            try {Thread.sleep(50);} 
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
    

    private GamePanel parent_panel; // background_panel
    private GameEndPanel game_end_panel = null;
}


