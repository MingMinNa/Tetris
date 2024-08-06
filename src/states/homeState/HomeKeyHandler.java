package states.homeState;

import java.awt.event.*;

public class HomeKeyHandler implements KeyListener{
    @Override
    public void keyPressed(KeyEvent e) {
        int key_code = e.getKeyCode();
        if(key_code == KeyEvent.VK_ENTER)
            pressed_enter = true;
    }
    @Override
    public void keyReleased(KeyEvent e) {
        // Press Enter to play game
        int key_code = e.getKeyCode();
        if(key_code == KeyEvent.VK_ENTER && pressed_enter)
            released_enter = true;
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    
    public boolean checkEnter(){
        return released_enter;
    }
    // ---------------------------
    private boolean pressed_enter = false, released_enter = false;
}
