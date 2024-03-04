package gameState;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import blocks.Cell;

import java.util.HashMap;
import java.util.Map;

public class GameScreen {
    public static boolean game_end = false; 
    public static int time_ticks = 60;
    public static int key_press = -1;
    public GameScreen(JFrame frame){
        for(int i = 0; i < game_area_cells.length; i ++){
            for(int j = 0; j < game_area_cells[0].length; j ++){
                game_area_cells[i][j] = new Cell(i, j, "black");
            }
        }
        GamePanel background_panel = new GamePanel();
        frame.getContentPane().add(background_panel);
        background_panel.revalidate();
        frame.revalidate();
        frame.repaint();
        
        while(true){
            Timer timer = new Timer(time_ticks, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    long currentTime = System.currentTimeMillis();
                    long deltaTime = currentTime - lastUpdateTime;
                    if(deltaTime >= time_ticks){
                        // System.out.println("DeltaTime2: " + deltaTime);
                        lastUpdateTime = currentTime;
    
                        background_panel.cell_position_update(game_area_cells);
                        background_panel.revalidate();
                    }
                }
            });
            timer.start();


            frame.revalidate();
            frame.repaint();
            if(game_end == true){
                timer.stop();
                break;
            }
        }

    }
    // ----------------------------------------
    private long lastUpdateTime = System.currentTimeMillis();
    private Cell [][] game_area_cells = new Cell[25][10];
}

class GamePanel extends JPanel implements KeyListener {
    public static int game_area_x = 10, game_area_y = 20;
    public GamePanel() {
        // initialize the label_cells -> label_cells is to fill color in the cells
        for(int i = 0; i < game_area_y; i ++){
            for(int j = 0 ;j < game_area_x;j ++){
                label_cells[i][j] = labelMake(x_start + Cell.block_width * (j + 1) + 1,y_start + Cell.block_height * (i + 1) + 1,"", Cell.block_width - 3, Cell.block_height - 3);
                label_cells[i][j].setVisible(false);
                this.add(label_cells[i][j]);
            }
        }

        // load all color cell into the cell_img map
        String [] colors_arr = {"red", "blue", "gray", "green", "lightblue", "orange", "purple", "yellow"};
        for(String color: colors_arr)
            cell_img.put(color, new ImageIcon("img\\" + color + ".jpg"));

        setPreferredSize(new Dimension(300, 200));
        setFocusable(true);
        setBackground(Color.BLACK);
        setBounds(0, 0, 800, 800);
        setLayout(null);
        buildBoard();
        
        addKeyListener(this);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }
    public void keyPressed(KeyEvent e) {
        
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    

    public void cell_position_update(Cell [][] game_area_cells){
        for(int i = 0; i < game_area_y; i++){
            for(int j = 0; j < game_area_x; j++){
                // System.out.println(game_area_cells[i][j].getColor());
                if (game_area_cells[i][j].getColor().equals("black")){
                    label_cells[i][j].setVisible(false);
                }
                else{
                    label_cells[i][j].setVisible(true);
                    // System.out.println("img\\" + game_area_cells[i][j].getColor() + ".jpg");
                    label_cells[i][j].setIcon(cell_img.get(game_area_cells[i][j].getColor()));
                }
            }
        }
    }
    // --------------------------------------
    private JLabel labelMake(int center_x, int center_y, String words, int words_width, int words_height){
        JLabel label = new JLabel(words);
        label.setBounds(center_x - words_width/2 , center_y - words_height/2, words_width, words_height);
        
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        return label;
    }
    private void buildBorder(int x_cnt, int y_cnt, int x_init, int y_init){
        // load the gray_cell image
        for(int i = 0; i < y_cnt; i++){
            for(int j = 0;j < x_cnt; j++){
                if(i == 0 || i == y_cnt - 1 || j == 0 || j == x_cnt - 1){
                    JLabel coverLabel = labelMake(x_init + Cell.block_width * j , y_init + Cell.block_height * i , "", Cell.block_width - 3, Cell.block_height - 3);
                    coverLabel.setIcon(cell_img.get("gray"));
                    this.add(coverLabel);
                }
            }
        }
    }
    private void buildBoard(){
        // build the game area border, Note: game_area_x and game_area_y is public static variable
        buildBorder(game_area_x + 2, game_area_y + 2, x_start + 1, y_start + 1);
        // build the preview border
        int preview_area_x = 7, preview_area_y = 12;
        buildBorder(preview_area_x, preview_area_y, x_start + 1 + 400, y_start + 1 + 200);
    }
    

    private static Map<String,ImageIcon> cell_img = new HashMap<>();
    private static JLabel [][] label_cells = new JLabel[game_area_y][game_area_x];
    private static int x_start = 130, y_start = 60;
}
