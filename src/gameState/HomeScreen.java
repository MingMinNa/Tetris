// package gameState;

// import javax.swing.*;

// import java.awt.*;
// import java.awt.event.*;


// public class HomeScreen {
//     public static boolean game_play = false; 
//     public static int time_ticks = 60; // current is useless 

//     public HomeScreen(JFrame frame){
        
//         game_play = false;
//         HomePanel background_panel = new HomePanel();
//         HomeKeyHandler.setPanel(background_panel);
//         frame.getContentPane().add(background_panel);
//         background_panel.requestFocusInWindow();

//         while(game_play == false){
//             frame.revalidate();
//             frame.repaint();
//         }

//         frame.getContentPane().remove(background_panel);
//     }
// }
// class HomePanel extends JPanel{
//     public int FRAME_WIDTH = 800, FRAME_HEIGHT = 800;
//     public HomePanel() {
//         setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
//         setFocusable(true);
//         setBackground(Color.BLACK);
//         setLayout(null);

//         keyboard_description();
//         game_cover();

//         addKeyListener(new HomeKeyHandler());
//         addMouseListener(new MouseAdapter() {
//             public void mouseClicked(MouseEvent e) {
//                 requestFocusInWindow();
//             }
//         });
        
//     }
    
//     private JLabel labelMake(int center_x, int center_y, String words){
//         int words_width = 100, words_height = 30;
//         return labelMake(center_x, center_y, words, words_width, words_height);
//     }
//     private JLabel labelMake(int center_x, int center_y, String words, int words_width, int words_height){
//         JLabel label = new JLabel(words);
//         label.setBounds(center_x - words_width/2 , center_y - words_height/2, words_width, words_height);
        
//         label.setFont(new Font("Arial", Font.BOLD, 20));
//         label.setHorizontalAlignment(JLabel.CENTER);
//         label.setVerticalAlignment(JLabel.CENTER);
//         return label;
//     }
//     private void keyboard_description(){
//         // Describe "How to play"
//         int x_start = 350;
//         int y_start = 500;
//         JLabel space = labelMake(x_start, y_start, "Space");
//         space.setForeground(Color.BLACK);
//         space.setOpaque(true);

//         JLabel down = labelMake(x_start, y_start + 40, "↓", 30, 30);
//         down.setForeground(Color.BLACK);
//         down.setOpaque(true);

//         JLabel left = labelMake(x_start - 20, y_start + 40 * 2, "←", 30, 30);
//         left.setForeground(Color.BLACK);
//         left.setOpaque(true);

//         JLabel right = labelMake(x_start + 20, y_start + 40 * 2, "→", 30, 30);
//         right.setForeground(Color.BLACK);
//         right.setOpaque(true);

//         JLabel left_rotate = labelMake(x_start - 20, y_start + 40 * 3, "A", 30, 30);
//         left_rotate.setForeground(Color.BLACK);
//         left_rotate.setOpaque(true);

//         JLabel right_rotate = labelMake(x_start + 20, y_start + 40 * 3, "D", 30, 30);
//         right_rotate.setForeground(Color.BLACK);
//         right_rotate.setOpaque(true);

//         JLabel space_descrip = labelMake(x_start + 150, y_start, "Hard drop");
//         space_descrip.setForeground(Color.WHITE);

//         JLabel down_descrip = labelMake(x_start + 150, y_start + 40 * 1, "Soft drop");
//         down_descrip.setForeground(Color.WHITE);

//         JLabel move_descrip = labelMake(x_start + 150, y_start + 40 * 2, "Move");
//         move_descrip.setForeground(Color.WHITE);

//         JLabel rotate_descrip = labelMake(x_start + 150, y_start + 40 * 3, "Rotate");
//         rotate_descrip.setForeground(Color.WHITE);
        
//         add(space);add(down);
//         add(left);add(right);
//         add(left_rotate);add(right_rotate);

//         add(space_descrip);
//         add(down_descrip);
//         add(move_descrip);
//         add(rotate_descrip);
//     }
//     private void game_cover(){
//         ImageIcon cover = new ImageIcon("img\\cover.png");
//         JLabel coverLabel = labelMake(400, 300, "", 250, 250 );
//         coverLabel.setIcon(cover);
//         add(coverLabel);
//     }

// }

// class HomeKeyHandler implements KeyListener{
//     @Override
//     public void keyPressed(KeyEvent e) {}
//     @Override
//     public void keyReleased(KeyEvent e) {
//         // Press any to play game, remove the panel in the frame.
//         HomeScreen.game_play = true;
//         control_panel.removeAll();
//         control_panel.revalidate();
//     }
//     @Override
//     public void keyTyped(KeyEvent e) {}

//     public static void setPanel(HomePanel background_panel){control_panel = background_panel;}
//     // ---------------------------
//     private static HomePanel control_panel;
// }
package frontPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomeScreen extends JFrame{

    public HomeScreen(){
        setTitle("Tetris");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new HomePanel());
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                enterGame();
            }
        });
        setVisible(true);
    }

    private class HomePanel extends JPanel{
        public int FRAME_WIDTH = 600, FRAME_HEIGHT = 800;
        public HomePanel(){
            setPreferredSize();
        }
        // private Image TetrisLogo;

        // public StartPanel(){
        //     try{
        //         TetrisLogo = ImageIO.read(new File("img/cover.png"));
        //     }
        //     catch(IOException e){
        //         e.printStackTrace();
        //     }
        // }

        // @Override
        // protected void paintComponent(Graphics g){
        //     super.paintComponent(g);
        //     g.setColor(Color.BLACK);
        //     g.fillRect(0, 0, getWidth(), getHeight());

        //     // paint Tetris logo
        //     if(TetrisLogo != null){
        //         int x = (getWidth() - TetrisLogo.getWidth(null)) / 2;
        //         int y = 100;
        //         g.drawImage(TetrisLogo, x, y, this);
        //     }

        //     // paint control keys and directions
        //     // g.setColor(Color.WHITE);
        //     // g.setFont(new Font("Arial", Font.PLAIN, 20));

        //     int baseX = 150;
        //     int baseY = getHeight() - 80;

        //     // paint "Space" frame and text
        //     g.fillRect(baseX, baseY - 30, 50, 50);
        //     g.setColor(Color.BLACK);
        //     g.drawString("Space", baseX + 10, baseY + 10);
        //     g.setColor(Color.WHITE);
        //     g.drawString("Hard drop", baseX + 60, baseY + 5);

        //     baseX += 300;

        //     // paint "down arrow" frame and text
        //     g.fillRect(baseX, baseY - 30, 50, 50);
        //     g.setColor(Color.BLACK);
        //     g.drawString("\u2193", baseX + 10, baseY + 10);
        //     g.setColor(Color.WHITE);
        //     g.drawString("Soft drop", baseX + 60, baseY + 5);

        //     baseX += 200;

        //     // paint "left&right arrow" frame and text
        //     g.fillRect(baseX, baseY - 30, 100, 50);
        //     g.setColor(Color.BLACK);
        //     g.drawString("\u2190 \u2192", baseX + 10, baseY + 10);
        //     g.setColor(Color.WHITE);
        //     g.drawString("Move", baseX + 110, baseY + 5);

        //     baseX += 250;

        //     // paint "A&D arrow" frame and text
        //     g.fillRect(baseX, baseY - 30, 100, 50);
        //     g.setColor(Color.BLACK);
        //     g.drawString("A D", baseX + 10, baseY + 10);
        //     g.setColor(Color.WHITE);
        //     g.drawString("Rotate", baseX + 110, baseY + 5);
    }
}

private void enterGame(){
    JOptionPane.showMessageDialog(this, "Entering the game...");
    dispose();  // close current window
}

public static void main(String[] args){
    SwingUtilities.invokeLater(() -> new HomeScreen());
}

// class JFrame from package javax.swing.*
// class JPanel from package javax.swing.*
// interface KeyListener from package java.awt.event.*
// class Dimension from package java.awt.*
// class MouseAdapter from package java.awt.event.*
// class Font from package java.awt.*
// class ImageIcon from package javax.swing.*