package gameState;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import blocks.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
        for(int i = 0; i < 2; i++){
            Random rnd = new Random();
            preview_blocks[i] = new GameBlock(rnd.nextInt(7), rnd.nextInt(7));
        }
        GamePanel background_panel = new GamePanel();
        frame.getContentPane().add(background_panel);
        background_panel.place_preview_block(preview_blocks);
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

                        if(currnetBlock == null){
                            int init_x = new Random().nextInt(1, 7), init_y = 2;
                            
                        }
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
    private Cell [][] game_area_cells = new Cell[23][10]; // 3, 20 
    private GameBlock [] preview_blocks = new GameBlock[2];
    private GameBlock [] currnetBlock = null;
    private Cell [] currentCells =  new Cell[4];
}

class GamePanel extends JPanel implements KeyListener {
    public static final int game_area_x_cnt = 10, game_area_y_cnt = 20;
    public static final int preview_area_x_cnt = 8, preview_area_y_cnt = 12;
    public static final int x_start = 130, y_start = 60;
    public static final int preview_area_x = x_start + 1 + 400, preview_area_y = y_start + 1 + 200; 
    public static final int game_area_x = x_start + 1, game_area_y = y_start + 1;
    public GamePanel() {

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
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    
    public void cell_position_update(Cell [][] game_area_cells){
        for(int i = 0; i < game_area_y_cnt; i++){
            for(int j = 0; j < game_area_x_cnt; j++){
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

    public static JLabel [][] getLabelPreview(){return label_preview;}

    
    /*
     * block_type
     * 0: I block
     *  * * * * * *
     *  * * * * * *
     *  * x x x x *
     *  * * * * * *
     * 1: O block
     *  * * * * * *
     *  * * x x * *
     *  * * x x * *
     *  * * * * * *
     * 2: S block
     *  * * * * * *
     *  * * x x * *
     *  * x x * * *
     *  * * * * * *
     * 3: Z block
     *  * * * * * *
     *  * * x x * *
     *  * * * x x *
     *  * * * * * *
     * 4: J block 
     *  * * * * * *
     *  * x * * * *
     *  * x x x * *
     *  * * * * * *
     * 5: L block
     *  * * * * * *
     *  * * * * x *
     *  * * x x x *
     *  * * * * * *
     * 6: T block
     *  * * * * * *
     *  * * x * * *
     *  * x x x * *
     *  * * * * * *
     */
    public void place_preview_block(GameBlock [] block){
        int [][][] colored_pos = {{{3, 2}, {3, 3}, {3, 4}, {3, 5}}, { {2, 3}, {2, 4}, {3, 3}, {3, 4}},
        { {2, 3}, {2, 4}, {3, 2}, {3, 3}}, { {2, 3}, {2, 4}, {3, 4}, {3, 5}}, { {2, 2}, {3, 2}, {3, 3}, {3, 4}}, { {2, 5}, {3, 3}, {3, 4}, {3, 5}}, { {2, 3}, {3, 2}, {3, 3}, {3, 4}}};

        int type = block[0].getBlockType();
        for(int i = 0; i < 4;i ++){
            label_preview[colored_pos[type][i][0] - 1][colored_pos[type][i][1] - 1].setVisible(true);
            label_preview[colored_pos[type][i][0] - 1][colored_pos[type][i][1] - 1].setIcon(new ImageIcon("img\\" + block[0].getColor() + ".jpg"));
        }
        type = block[1].getBlockType();
        for(int i = 0; i < 4;i ++){
            label_preview[colored_pos[type][i][0] - 1 + 6][colored_pos[type][i][1] - 1].setVisible(true);
            label_preview[colored_pos[type][i][0] - 1 + 6][colored_pos[type][i][1] - 1].setIcon(new ImageIcon("img\\" + block[1].getColor() + ".jpg"));
        }
        this.revalidate();
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
    private void buildBorder(int x_cnt, int y_cnt, int x_init, int y_init, boolean preview_or_game){
        // load the gray_cell image
        for(int i = 0; i < y_cnt; i++){
            for(int j = 0;j < x_cnt; j++){
                if(i == 0 || i == y_cnt - 1 || j == 0 || j == x_cnt - 1){
                    
                    JLabel borderLabel = labelMake(x_init + Cell.block_width * j , y_init + Cell.block_height * i , "", Cell.block_width - 3, Cell.block_height - 3);
                    borderLabel.setIcon(cell_img.get("gray"));
                    this.add(borderLabel);
                }
                else if(preview_or_game == true){
                    label_cells[i - 1][j - 1] = labelMake(x_start + Cell.block_width * j + 1,y_start + Cell.block_height * i + 1,"", Cell.block_width - 3, Cell.block_height - 3);
                    label_cells[i - 1][j - 1].setVisible(false);
                    this.add(label_cells[i - 1][j - 1]);
                }
                else{
                    JLabel innerLabel = labelMake(x_init + Cell.block_width * j , y_init + Cell.block_height * i , "", Cell.block_width - 3, Cell.block_height - 3);
                    label_preview[i - 1][j - 1] = innerLabel;
                    innerLabel.setVisible(false);
                    this.add(label_preview[i - 1][j - 1]);
                }
            }
        }
    }
    private void buildBoard(){
        // build the game area border, Note: game_area_x_cnt and game_area_y_cnt is public static variable
        // true: game, false: preview
        buildBorder(game_area_x_cnt + 2, game_area_y_cnt + 2, game_area_x, game_area_y, true);
        // build the preview border
        buildBorder(preview_area_x_cnt, preview_area_y_cnt, preview_area_x, preview_area_y, false);
    }
    
    private static Map<String,ImageIcon> cell_img = new HashMap<>();
    private static JLabel [][] label_cells = new JLabel[game_area_y_cnt][game_area_x_cnt];
    private static JLabel [][] label_preview = new JLabel[preview_area_y_cnt - 2][preview_area_x_cnt - 2];
}
