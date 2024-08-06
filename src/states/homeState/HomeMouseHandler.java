package states.homeState;

import java.awt.*;
import java.awt.event.*;

class HomeMouseHandler implements MouseListener{
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {
        released_point = e.getPoint();
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    public Point getReleased(){
        return released_point;
    }
    public void resetReleased(){
        released_point = null;
    }
    // ---------------
    private Point released_point = null;
}