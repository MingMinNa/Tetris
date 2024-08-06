package states.gameState.end;

import java.awt.event.*;

public class GameEndKeyHandler implements KeyListener{
    @Override
    public void keyTyped(KeyEvent e){}
    @Override
    public void keyPressed(KeyEvent e){
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_ESCAPE){
            pressed_esc = true;
        }
    }
    @Override
    public void keyReleased(KeyEvent e){
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_ESCAPE){
            pressed_esc = false;
        }
    }

    public boolean getEscPressed(){
        return pressed_esc;
    }
    private boolean pressed_esc = false;
}

