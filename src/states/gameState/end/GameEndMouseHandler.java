package states.gameState.end;

import java.awt.*;
import java.awt.event.*;

public class GameEndMouseHandler implements MouseListener{
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
