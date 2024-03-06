package gameState;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import blocks.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import java.util.List;
import java.util.ArrayList;

public class GameScreen { 
    public static int time_ticks = 100;
    public static int key_press = -1;
    public GameScreen(JFrame frame){
        for(int i = 0; i < game_area_cells.length; i ++){
            for(int j = 0; j < game_area_cells[0].length; j ++){
                game_area_cells[i][j] = new Cell(j, i, "black");
            }
        }
        for(int i = 0; i < 2; i++){
            Random rnd = new Random();
            preview_blocks[i] = new GameBlock(rnd.nextInt(7), rnd.nextInt(7));
        }
        frame_temp = frame;
        background_panel = new GamePanel();
        frame.getContentPane().add(background_panel);
        background_panel.placePreviewBlock(preview_blocks);
        background_panel.revalidate();
        frame.revalidate();
        frame.repaint();
        new GameTimer().run();

        // Game Over
    }

    class GameTimer{
        static boolean game_over = false;
        public void run() {
            int count = 0;
            while(true){
                long currentTime = System.currentTimeMillis();
                long deltaTime = currentTime - last_update_time;
                
                if(count >= 3){
                    // System.out.println("DeltaTime2: " + deltaTime);
                    count = 0;
                    background_panel.cellPositionUpdate(game_area_cells);
        
                    // System.out.println("Hllefewfweo");
                    
                    if(tryMove("down") == false){
                        if(current_cells[3].getY() <= 2){
                            game_over = true;
                            break;
                        }
                        current_block = null;
                        
                        background_panel.gameLineCheck(game_area_cells);
                        background_panel.cellPositionUpdate(game_area_cells);
                        background_panel.revalidate();
                        frame_temp.revalidate();
                        frame_temp.repaint();
                    }
                    background_panel.revalidate();
                    frame_temp.revalidate();
                    frame_temp.repaint();
                }
                else if(deltaTime >= time_ticks){
                    count++;
                    last_update_time = currentTime;
                    if(current_block == null){
                        // System.out.println("Hlleo");
                        background_panel.previewAreaRefresh();
                        currentBlockUpdate();
                        background_panel.placePreviewBlock(preview_blocks);
                    }
                    // keyboard operation
                    String pressed_key = GameKeyHandler.getCurrentKey();
                    if( pressed_key.equals("left")){
                        tryMove("left");
                    }   
                    else if(pressed_key.equals("right")){
                        tryMove("right");
                    }
                    else if(pressed_key.equals("down")){
                        count = 0;
                        if(tryMove("down") == false){
                            if(current_cells[3].getY() <= 2){
                                game_over = true;
                                break;
                            }
                            current_block = null;
                            background_panel.gameLineCheck(game_area_cells);
                        }
                    }
                    background_panel.cellPositionUpdate(game_area_cells);
                    background_panel.revalidate();
                    frame_temp.revalidate();
                    frame_temp.repaint();
                }

                if(game_over == true)    break;
            }
        }

        private boolean tryMove(String direction){
            if(direction.equals("down")){
                if(background_panel.checkMoveDown(current_cells, game_area_cells)){
                    for(int i = 0; i < 4; i ++ ){
                        Cell cell = current_cells[i];
                        game_area_cells[cell.getY() + 1][cell.getX()].setColor(cell.getColor());
                        game_area_cells[cell.getY()][cell.getX()].setColor("black");
                        current_cells[i] = game_area_cells[cell.getY() + 1][cell.getX()];
                    }
                    return true;
                }
                return false;
            }
            else if(direction == "left"){
                if(background_panel.checkMoveLeft(current_cells, game_area_cells)){
                    // System.out.println("Hello");
                    for(int i = 0; i < 4; i ++ ){
                        Cell cell = current_cells[i];
                        game_area_cells[cell.getY()][cell.getX() - 1].setColor(cell.getColor());
                        game_area_cells[cell.getY()][cell.getX()].setColor("black");
                        current_cells[i] = game_area_cells[cell.getY()][cell.getX() - 1];
                    }
                    return true;
                }
                return false;
            }
            else if(direction == "right"){
                if(background_panel.checkMoveRight(current_cells, game_area_cells)){
                    for(int i = 3; i >= 0; i -- ){
                        Cell cell = current_cells[i];
                        game_area_cells[cell.getY()][cell.getX() + 1].setColor(cell.getColor());
                        game_area_cells[cell.getY()][cell.getX()].setColor("black");
                        current_cells[i] = game_area_cells[cell.getY()][cell.getX() + 1];
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    // ----------------------------------------
    private long last_update_time = System.currentTimeMillis();
    private GamePanel background_panel;
    private JFrame frame_temp;

    private Cell [][] game_area_cells = new Cell[23][10]; // 3, 20 
    private GameBlock [] preview_blocks = new GameBlock[2];
    private GameBlock current_block = null;
    private Cell [] current_cells =  new Cell[4];

    private void currentBlockUpdate(){
        int block_type = preview_blocks[0].getBlockType();
        String block_color = preview_blocks[0].getColor();
        Random rnd = new Random();
        preview_blocks[0].setBlockType(preview_blocks[1].getBlockType());
        preview_blocks[0].setColor(preview_blocks[1].getColor());
        preview_blocks[1].setBlockType(rnd.nextInt(7));
        // preview_blocks[1].setBlockType(1);
        preview_blocks[1].setColor(rnd.nextInt(7));
        int init_x = rnd.nextInt(0, 7);
        /*
        * block_type
        * 0: I block
        *  * * * * * *
        *  * * * * * *
        *  x x x x * *
        * 1: O block
        *  * * * * * *
        *  x x * * * *
        *  x x * * * *
        * 2: S block
        *  * * * * * *
        *  * x x * * *
        *  x x * * * *
        * 3: Z block
        *  * * * * * *
        *  x x * * * *
        *  * x x * * *
        * 4: J block 
        *  * * * * * *
        *  x * * * * * 
        *  x x x * * * 
        * 5: L block
        *  * * * * * *
        *  * * x * * *
        *  x x x * * *
        * 6: T block
        *  * * * * * *
        *  * x * * * * 
        *  x x x * * * 
        */
        int [][][] colored_pos = {
            {{2, 0}, {2, 1}, {2, 2}, {2, 3}}, 
            {{1, 0}, {1, 1}, {2, 0}, {2, 1}}, 
            {{1, 1}, {1, 2}, {2, 0}, {2, 1}}, 
            {{1, 0}, {1, 1}, {2, 1}, {2, 2}}, 
            {{1, 0}, {2, 2}, {2, 0}, {2, 1}}, 
            {{1, 2}, {2, 2}, {2, 0}, {2, 1}}, 
            {{1, 1}, {2, 2}, {2, 0}, {2, 1}}};
        for(int i = 0; i < 4;i ++){
            // System.out.println(colored_pos[block_type][i][0] + 1);
            // System.out.println(colored_pos[block_type][i][1] + init_x);
            this.game_area_cells[colored_pos[block_type][i][0]][colored_pos[block_type][i][1] + init_x].setColor(block_color);
            current_cells[i] = game_area_cells[colored_pos[block_type][i][0]][colored_pos[block_type][i][1] + init_x];
        }
        for(int i = 0; i < 3;i ++){
            for(int j = 0;j < 3;j ++){
                if(current_cells[j].getY() < current_cells[j+1].getY() || (current_cells[j].getY() == current_cells[j+1].getY() && current_cells[j].getX() > current_cells[j+1].getX())){
                    Cell temp_cell = current_cells[j];
                    current_cells[j] = current_cells[j + 1];
                    current_cells[j + 1] = temp_cell;
                }
            }
        }
        current_block = new GameBlock(block_type, block_color);
        return;
    }
}

class GamePanel extends JPanel{
    public static final int game_area_x_cnt = 10, game_area_y_cnt = 20;
    public static final int preview_area_x_cnt = 8, preview_area_y_cnt = 12;
    public static final int x_start = 130, y_start = 60;
    public static final int preview_area_x = x_start + 1 + 400, preview_area_y = y_start + 1 + 200; 
    public static final int game_area_x = x_start + 1, game_area_y = y_start + 1;
    public static Map<String,ImageIcon> cell_img = new HashMap<>();


    
    public static JLabel [][] getLabelPreview(){return label_preview;}
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
        
        addKeyListener(new GameKeyHandler());
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }
    

    public void gameLineCheck(Cell [][] game_area_cells){
        int del_line = 0;
        for(int i = game_area_cells.length - 1; i > 1; i--){
            boolean all_filled = true;
            for(int j = 0; j < game_area_cells[0].length; j++){
                if(game_area_cells[i][j].getColor().equals("black")){all_filled = false; break;}
            }
            if(all_filled == false){
                for(int j = 0; j < game_area_cells[0].length; j++)
                    game_area_cells[i + del_line][j].setColor(game_area_cells[i][j].getColor());
            }
            else
                del_line++;
        }
    }
    public void cellPositionUpdate(Cell [][] game_area_cells){
        for(int i = 0; i < game_area_y_cnt; i++){
            for(int j = 0; j < game_area_x_cnt; j++){
                // System.out.println(game_area_cells[i][j].getColor());
                if (game_area_cells[i + 3][j].getColor().equals("black")){
                    label_cells[i][j].setVisible(false);
                }
                else{
                    label_cells[i][j].setVisible(true);
                    // System.out.println("img\\" + game_area_cells[i][j].getColor() + ".jpg");
                    label_cells[i][j].setIcon(cell_img.get(game_area_cells[i + 3][j].getColor()));
                }
            }
        }
    }
    
    public boolean checkMoveDown(Cell [] current_cells,Cell [][] game_area_cells){
        List<Integer> cells_list =  new ArrayList<>();
        for(Cell dominated_cell: current_cells){
            cells_list.add(100 * dominated_cell.getX() + dominated_cell.getY());
            // System.out.println(100 * dominated_cell.getX() + dominated_cell.getY());
        }
        boolean moveDown = true;
        for(int i = 0; i < 4;i ++){
            if(current_cells[i].getY() + 1 >= 23){
                moveDown = false;
                break;
            }
            if(cells_list.contains(100 * (current_cells[i].getX()) + current_cells[i].getY() + 1) == false && 
                game_area_cells[current_cells[i].getY() + 1][current_cells[i].getX()].getColor().equals("black") == false){
                moveDown = false;
                break;
            }
        }
        return moveDown;
    }

    public boolean checkMoveLeft(Cell [] current_cells,Cell [][] game_area_cells){
        List<Integer> cells_list =  new ArrayList<>();
        for(Cell dominated_cell: current_cells){
            cells_list.add(100 * dominated_cell.getX() + dominated_cell.getY());
            // System.out.println(100 * dominated_cell.getX() + dominated_cell.getY());
        }
        boolean move = true;
        for(int i = 0; i < 4;i ++){
            if(current_cells[i].getX() - 1 < 0){
                move = false;
                break;
            }
            if(cells_list.contains(100 * (current_cells[i].getX() - 1) + current_cells[i].getY()) == false && 
                game_area_cells[current_cells[i].getY()][current_cells[i].getX() - 1].getColor().equals("black") == false){
                move = false;
                break;
            }
        }
        return move;
    }

    public boolean checkMoveRight(Cell [] current_cells,Cell [][] game_area_cells){
        List<Integer> cells_list =  new ArrayList<>();
        for(Cell dominated_cell: current_cells){
            cells_list.add(100 * dominated_cell.getX() + dominated_cell.getY());
            // System.out.println(100 * dominated_cell.getX() + dominated_cell.getY());
        }
        boolean move = true;
        for(int i = 0; i < 4;i ++){
            if(current_cells[i].getX() + 1 >= 10){
                move = false;
                break;
            }
            if(cells_list.contains(100 * (current_cells[i].getX() + 1) + current_cells[i].getY()) == false && 
                game_area_cells[current_cells[i].getY()][current_cells[i].getX() + 1].getColor().equals("black") == false){
                move = false;
                break;
            }
        }
        return move;
    }
    
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
    public void previewAreaRefresh(){
        for(int i = 0; i < label_preview.length; i++){
            for(int j = 0; j < label_preview[0].length; j++){
                label_preview[i][j].setVisible(false);
            }
        }
    }
    public void placePreviewBlock(GameBlock [] blocks){
        int [][][] colored_pos = {{{3, 2}, {3, 3}, {3, 4}, {3, 5}}, { {2, 3}, {2, 4}, {3, 3}, {3, 4}},
        { {2, 3}, {2, 4}, {3, 2}, {3, 3}}, { {2, 3}, {2, 4}, {3, 4}, {3, 5}}, { {2, 2}, {3, 2}, {3, 3}, {3, 4}}, { {2, 5}, {3, 3}, {3, 4}, {3, 5}}, { {2, 3}, {3, 2}, {3, 3}, {3, 4}}};

        int type = blocks[0].getBlockType();
        for(int i = 0; i < 4;i ++){
            label_preview[colored_pos[type][i][0] - 1][colored_pos[type][i][1] - 1].setVisible(true);
            label_preview[colored_pos[type][i][0] - 1][colored_pos[type][i][1] - 1].setIcon(new ImageIcon("img\\" + blocks[0].getColor() + ".jpg"));
        }
        type = blocks[1].getBlockType();
        for(int i = 0; i < 4;i ++){
            label_preview[colored_pos[type][i][0] - 1 + 6][colored_pos[type][i][1] - 1].setVisible(true);
            label_preview[colored_pos[type][i][0] - 1 + 6][colored_pos[type][i][1] - 1].setIcon(new ImageIcon("img\\" + blocks[1].getColor() + ".jpg"));
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
    
    private static JLabel [][] label_cells = new JLabel[game_area_y_cnt][game_area_x_cnt];
    private static JLabel [][] label_preview = new JLabel[preview_area_y_cnt - 2][preview_area_x_cnt - 2];
}


class GameKeyHandler implements KeyListener{
    private static boolean left = false, right = false, down = false;
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == 37) // left
            left = true;
        else if(code == 39) // right
            right = true;
        else if(code == 40) // down
            down = true;
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == 37)  left = false;
        if(code == 39)  right = false;
        if(code == 40)  down = false;
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    public static String getCurrentKey(){
        if(left == true)    return "left";
        else if(right == true) return "right";
        else if(down == true)  return "down";
        else                   return "null";
    }
}