package states.gameState.end;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import states.ScreenPanel;
import states.gameState.runner.GamePanel;

public class GameEndPanel extends JPanel implements ScreenPanel{

    public static final int PANEL_WIDTH = 400, PANEL_HEIGHT = 300;
    public GameEndPanel(boolean game_over){
        setBounds(  (GamePanel.FRAME_WIDTH - PANEL_WIDTH) / 2, 
                    (GamePanel.FRAME_HEIGHT - PANEL_HEIGHT) / 2,
                    GameEndPanel.PANEL_WIDTH,
                    GameEndPanel.PANEL_HEIGHT);
        setFocusable(true);
        setBackground(new Color(139, 121, 94));
        setLayout(null);
        setFocusTraversalKeysEnabled(false);

        buildContinueButton();
        buildTexts(game_over);

        game_end_mouse_handler = new GameEndMouseHandler(){
            public void mouseClicked(MouseEvent e) {requestFocusInWindow();}
        };
        game_end_key_handler = new GameEndKeyHandler();

        addMouseListener(game_end_mouse_handler);
        addKeyListener(game_end_key_handler);
        
    }
    public void setTransparent(boolean transparent){
        int transparency = 255; boolean visible = true;
        if(transparent){ 
            transparency = 0; visible = false;
        }
        this.setBackground(new Color(139, 121, 94, transparency));
        for(int i = 0; i < labels.length; i++)
            labels[i].setVisible(visible);
            
        if(button_panel_object[0] instanceof JPanel)
            ((JPanel)button_panel_object[0]).setVisible(visible);
            
        if(button_panel_object[1] instanceof JLabel)
            ((JLabel)button_panel_object[1]).setVisible(visible);
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

        button_panel_object[0] = continue_button_panel;
        button_panel_object[1] = continue_button_text;

        continue_button_range[0] = (PANEL_WIDTH - button_panel_width) / 2;
        continue_button_range[2] = continue_button_range[0] + button_panel_width;
        continue_button_range[1] = (PANEL_HEIGHT - button_panel_height) / 2 + 80;
        continue_button_range[3] = continue_button_range[1] + button_panel_height;
    }
    private void buildTexts(boolean game_over){
        // build Title
        String title = "Game Clear"; Color color = new Color(192, 255, 62);
        if(game_over){
            title = "Game Over";  color = new Color(139, 69, 19);
        }
        // labels[0] == title_labal
        labels[0] = labelMake(GameEndPanel.PANEL_WIDTH / 2, GameEndPanel.PANEL_HEIGHT / 2 - 40, title, 400, 300, 50);
        labels[0].setForeground(color);
        add(labels[0]);


        labels[1] = labelMake(PANEL_WIDTH / 2, PANEL_HEIGHT / 2 + 20, "Press Esc to hide panel", 250, 100, 20);
        add(labels[1]);
    }
    
    private int continue_button_range[] = new int[4]; // [0]:x_start, [1]:y_start, [2]:x_end, [3]:y_end
    private GameEndMouseHandler game_end_mouse_handler;
    private GameEndKeyHandler game_end_key_handler;
    private JLabel labels[] = new JLabel[2]; // [0]: Title
    private Object button_panel_object[] = new Object[2]; // [0]:panel, [1]:text
}

