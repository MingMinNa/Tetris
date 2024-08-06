package states.gameState;

import java.awt.event.*;

public class GameKeyHandler implements KeyListener{
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        // key press priority
        if(code == KeyEvent.VK_SPACE && space_done == false){ // space
            space = true;
        }
        else if(code == KeyEvent.VK_A && left_rotate_done == false){ // A is left rotate
            left_rotate = true;
        }
        else if(code == KeyEvent.VK_D && right_rotate_done == false){ // D is right rotate 
            right_rotate = true;
            right_rotate_done = false;
        }
        else if(code == KeyEvent.VK_LEFT) // left
            left = true;
        else if(code == KeyEvent.VK_RIGHT) // right
            right = true;
        else if(code == KeyEvent.VK_DOWN) // down
            down = true;
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_LEFT)  left = false;
        if(code == KeyEvent.VK_RIGHT)  right = false;
        if(code == KeyEvent.VK_DOWN)  down = false;
        if(code == KeyEvent.VK_SPACE){  space = false; space_done = false;}
        if(code == KeyEvent.VK_A){  left_rotate = false; left_rotate_done = false;}
        if(code == KeyEvent.VK_D){  right_rotate = false; right_rotate_done = false;}
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    public String getCurrentKey(){
        if(space == true && space_done == false)        return "space";
        else if(right_rotate == true && right_rotate_done == false)   return "right rotate";
        else if(left_rotate == true && left_rotate_done == false)   return "left rotate";
        else if(left == true)    return "left";
        else if(right == true) return "right";
        else if(down == true)  return "down";
        else                   return "null";
    }
    public void changeSpaceState(){
        space_done = true;
    }
    public void changeLeftRotateState(){
        left_rotate_done = true;
    }
    public void changeRightRotateState(){
        right_rotate_done = true;
    }

    // ---------------------------
    private boolean left = false, right = false, down = false, space = false, left_rotate = false, right_rotate = false;
    private boolean space_done = false, left_rotate_done = false, right_rotate_done = false;
}