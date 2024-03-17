package gameState;


import javax.swing.*;
import javax.swing.plaf.TreeUI;

import Music.MusicPlayer;

import java.awt.*;
import java.awt.event.*;
import blocks.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import java.util.List;
import java.util.ArrayList;
import java.util.function.*;

public class GameScreen { 
    public static int time_ticks = 50;
    public static int key_press = -1;
    public static int GAME_STATE_SCORE[] = {0, 400, 1000, -1}; // the score required into the next state

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
        background_panel = new GamePanel();
        frame.getContentPane().add(background_panel);
        background_panel.placePreviewBlock(preview_blocks);
        background_panel.revalidate();
        background_panel.requestFocusInWindow();
        frame.revalidate();
        frame.repaint();
        new GameTimer().run(frame);

    }

    class GameTimer{
        static boolean game_over = false;
        public void run(JFrame frame) {
            MusicPlayer music_player = null;
            // count is for auto-fall, count2 is for left, right and down, and the 
            int count = 0, count2 = 0;
            while(true){
                if(checkNextGameState()){
                    if(music_player != null){music_player.stopPlaying();}
                    music_player = new MusicPlayer("GameState" + Integer.toString(current_state), true);
                    music_player.start();
                }
                long currentTime = System.currentTimeMillis();
                long deltaTime = currentTime - last_update_time;
                
                if(count >= 6){
                    count = 0;
                    background_panel.cellPositionUpdate(game_area_cells);
        
                    if(tryMove("down") == false){
                        if(current_cells[3].getY() <= 2){
                            game_over = true;
                            break;
                        }
                        current_block = null;
                        
                        gameLineCheck(game_area_cells);
                        background_panel.cellPositionUpdate(game_area_cells);
                    }
                    background_panel.revalidate();
                    frame.revalidate();
                    frame.repaint();
                }
                else if(deltaTime >= time_ticks){
                    count++;
                    count2++;
                    last_update_time = currentTime;
                    if(current_block == null){
                        // System.out.println("Hlleo");
                        background_panel.previewAreaRefresh();
                        currentBlockUpdate();
                        background_panel.placePreviewBlock(preview_blocks);
                    }
                    // keyboard operation
                    String pressed_key = GameKeyHandler.getCurrentKey();
                    if( pressed_key.equals("left") && count2 >= 2){
                        tryMove("left");
                    }   
                    else if(pressed_key.equals("right") && count2 >= 2){
                        tryMove("right");
                    }
                    else if(pressed_key.equals("down") && count2 >= 2){
                        count = 0;
                        if(tryMove("down") == false){
                            if(current_cells[3].getY() <= 2){
                                game_over = true;
                                break;
                            }
                            current_block = null;
                            gameLineCheck(game_area_cells);
                        }
                    }
                    else if(pressed_key.equals("space")){
                        count = 0;GameKeyHandler.changeSpaceState();
                        while(tryMove("down") == true);
                        if(current_cells[3].getY() <= 2){
                            game_over = true;
                            break;
                        }
                        current_block = null;
                        gameLineCheck(game_area_cells);
                    }
                    else if(pressed_key.equals("right rotate")){
                        GameKeyHandler.changeRightRotateState();
                        tryRotate("left");
                    }
                    else if(pressed_key.equals("left rotate")){
                        GameKeyHandler.changeLeftRotateState();
                        tryRotate("right");
                    }
                    if(count2 >= 2)
                        count2 = 0;
                    background_panel.cellPositionUpdate(game_area_cells);
                    background_panel.revalidate();
                    frame.revalidate();
                    frame.repaint();
                }

                if(game_over == true)    break;
            }
            if(music_player != null)
                music_player.stopPlaying();
            // reset the setting for next loop game;
            game_over = false;
            GameKeyHandler.resetSetting();
            // Remove all the components in the GameScreen
            removePanel(frame);
        }

        private boolean tryMove(String direction){
            if(direction.equals("down")){
                if(checkMove(current_cells, game_area_cells, direction)){
                    for(int i = 0; i < 4; i ++ ){
                        Cell cell = current_cells[i];
                        game_area_cells[cell.getY() + 1][cell.getX()].setColor(cell.getColor());
                        game_area_cells[cell.getY()][cell.getX()].setColor("black");
                        current_cells[i] = game_area_cells[cell.getY() + 1][cell.getX()];
                    }
                    current_block.setBlockCenter(current_block.getCenterX(), current_block.getCenterY() + 1);
                    return true;
                }
                return false;
            }
            else if(direction == "left"){
                if(checkMove(current_cells, game_area_cells, direction)){
                    // System.out.println("Hello");
                    for(int i = 0; i < 4; i ++ ){
                        Cell cell = current_cells[i];
                        game_area_cells[cell.getY()][cell.getX() - 1].setColor(cell.getColor());
                        game_area_cells[cell.getY()][cell.getX()].setColor("black");
                        current_cells[i] = game_area_cells[cell.getY()][cell.getX() - 1];
                    }
                    current_block.setBlockCenter(current_block.getCenterX() - 1, current_block.getCenterY());
                    return true;
                }
                return false;
            }
            else if(direction == "right"){
                if(checkMove(current_cells, game_area_cells, direction)){
                    for(int i = 3; i >= 0; i -- ){
                        Cell cell = current_cells[i];
                        game_area_cells[cell.getY()][cell.getX() + 1].setColor(cell.getColor());
                        game_area_cells[cell.getY()][cell.getX()].setColor("black");
                        current_cells[i] = game_area_cells[cell.getY()][cell.getX() + 1];
                    }
                    current_block.setBlockCenter(current_block.getCenterX() + 1, current_block.getCenterY());
                    return true;
                }
                return false;
            }
            return false;
        }

        private void tryRotate(String direction){
            if(checkRotate(current_block, current_cells, game_area_cells, direction)){
                // System.out.println("Rotatte!!!");
                int next_state;
                if(direction.equals("right rotate"))    next_state = (current_block.getBlockState() + 1) % 4;
                else                                             next_state = (current_block.getBlockState() - 1 + 4) % 4;
                int block_type = current_block.getBlockType();
                String block_color = current_block.getColor();
                int center_x = current_block.getCenterX(), center_y = current_block.getCenterY();
                
                for(int i = 0; i < 4;i++)
                    game_area_cells[current_cells[i].getY()][current_cells[i].getX()].setColor("black");
                for(int i = 0; i < 4;i++){
                    int next_x = center_x + GameBlock.BLOCK_DIST[block_type][next_state][i][1];
                    int next_y = center_y + GameBlock.BLOCK_DIST[block_type][next_state][i][0];
                    current_cells[i] = game_area_cells[next_y][next_x];
                    current_cells[i].setColor(block_color);
                }
                for(int i = 0; i < 3;i ++){
                    for(int j = 0;j < 3;j ++){
                        // y: down to up, x: left to right
                        if(current_cells[j].getY() < current_cells[j+1].getY() || (current_cells[j].getY() == current_cells[j+1].getY() && current_cells[j].getX() > current_cells[j+1].getX())){
                            Cell temp_cell = current_cells[j];
                            current_cells[j] = current_cells[j + 1];
                            current_cells[j + 1] = temp_cell;
                        }
                    }
                }
                current_block.nextState();
            }
        }
        // merge all the function "checkMoveDown", "checkMoveLeft" and "checkMoveRight"
        private boolean checkMove(Cell [] current_cells,Cell [][] game_area_cells, String direction){
            List<Integer> cells_list =  new ArrayList<>();
            for(Cell dominated_cell: current_cells){
                cells_list.add(100 * dominated_cell.getX() + dominated_cell.getY());
            }
            boolean move = true;
            Map<String,Function<Cell, Boolean>> check_touch_border = new HashMap<>();
            check_touch_border.put("down",(x) -> (x.getY() + 1 >= 23));
            check_touch_border.put("left", (x) -> (x.getX() - 1 < 0));
            check_touch_border.put("right", (x) -> (x.getX() + 1 >= 10));

            Map<String,Function<Cell,Integer>> get_next_cell_x = new HashMap<>();
            Map<String,Function<Cell,Integer>> get_next_cell_y = new HashMap<>();
            get_next_cell_x.put("down", (x)->(x.getX()));
            get_next_cell_y.put("down", (x)->(x.getY() + 1));
            
            get_next_cell_x.put("left", (x)->(x.getX() - 1));
            get_next_cell_y.put("left", (x)->(x.getY()));

            get_next_cell_x.put("right", (x)->(x.getX() + 1));
            get_next_cell_y.put("right", (x)->(x.getY()));
            for(int i = 0; i < 4;i ++){
                int next_x = get_next_cell_x.get(direction).apply(current_cells[i]);
                int next_y = get_next_cell_y.get(direction).apply(current_cells[i]);
                if(check_touch_border.get(direction).apply(current_cells[i])){
                    move = false;
                    break;
                }
                if(cells_list.contains(100 * next_x + next_y) == false && 
                    game_area_cells[next_y][next_x].getColor().equals("black") == false){
                    move = false;
                    break;
                }
            }
            return move;
        }
        private boolean checkRotate(GameBlock current_block, Cell [] current_cells, Cell[][] game_area_cells, String direction){
            int block_type = current_block.getBlockType();
            int next_state;
            if(direction.equals("right rotate"))    next_state = (current_block.getBlockState() + 1) % 4;
            else                                             next_state = (current_block.getBlockState() - 1 + 4) % 4;
            if(block_type == 1) // O block, rotate operation is useless
                return false;
            List<Integer> current_cells_pos = new ArrayList<>();
            for(int i = 0; i < 4; i++){
                // 100 * x + y
                current_cells_pos.add(100 * current_cells[i].getX() + current_cells[i].getY());
            }
            boolean rotate = true;
            int center_x = current_block.getCenterX(), center_y = current_block.getCenterY();
            for(int i = 0; i < 4; i++){
                int next_x = center_x + GameBlock.BLOCK_DIST[block_type][next_state][i][1];
                int next_y = center_y + GameBlock.BLOCK_DIST[block_type][next_state][i][0];
                if(next_x < 0 || next_x >= GamePanel.GAME_AREA_X_CNT || next_y < 0 || next_y >= GamePanel.GAME_AREA_Y_CNT){
                    rotate = false;break;
                }
                if(current_cells_pos.contains(100 * (next_x) + next_y) == false && game_area_cells[next_y][next_x].getColor().equals("black") == false){
                    rotate = false;break;
                }
            }
            return rotate;
        }
    }

    // ----------------------------------------
    private long last_update_time = System.currentTimeMillis();
    private GamePanel background_panel;

    private Cell [][] game_area_cells = new Cell[23][10]; // 3, 20 
    private GameBlock [] preview_blocks = new GameBlock[2];
    private GameBlock current_block = null;
    private Cell [] current_cells =  new Cell[4];

    private int current_state = 0; 

    private boolean checkNextGameState(){
        if(GAME_STATE_SCORE[current_state] >= 0 && background_panel.getScore() >= GAME_STATE_SCORE[current_state]){
            current_state = current_state + 1;
            return true;
        }
        return false;
    }
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
        *  x o x x * *
        * 1: O block
        *  * * * * * *
        *  o x * * * *
        *  x x * * * *
        * 2: S block
        *  * * * * * *
        *  * x x * * *
        *  x o * * * *
        * 3: Z block
        *  * * * * * *
        *  x x * * * *
        *  * o x * * *
        * 4: J block 
        *  * * * * * *
        *  x * * * * * 
        *  x o x * * * 
        * 5: L block
        *  * * * * * *
        *  * * x * * *
        *  x o x * * *
        * 6: T block
        *  * * * * * *
        *  * x * * * * 
        *  x o x * * * 
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

            this.game_area_cells[colored_pos[block_type][i][0]][colored_pos[block_type][i][1] + init_x].setColor(block_color);
            current_cells[i] = game_area_cells[colored_pos[block_type][i][0]][colored_pos[block_type][i][1] + init_x];
        }
        for(int i = 0; i < 3;i ++){
            for(int j = 0;j < 3;j ++){
                // y: down to up, x: left to right
                if(current_cells[j].getY() < current_cells[j+1].getY() || (current_cells[j].getY() == current_cells[j+1].getY() && current_cells[j].getX() > current_cells[j+1].getX())){
                    Cell temp_cell = current_cells[j];
                    current_cells[j] = current_cells[j + 1];
                    current_cells[j + 1] = temp_cell;
                }
            }
        }
        int [] center_cell_index = {1, 2, 1, 0, 1, 1, 1};    
        current_block = new GameBlock(current_cells[center_cell_index[block_type]].getX(), current_cells[center_cell_index[block_type]].getY(),block_type, block_color);
        
        return;
    }
    private void gameLineCheck(Cell [][] game_area_cells){
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
        int current_score = background_panel.getScore();
        switch(del_line){
            case 4: background_panel.setScore(current_score + 700);break;
            case 3: background_panel.setScore(current_score + 500);break;
            case 2: background_panel.setScore(current_score + 300);break;
            case 1: background_panel.setScore(current_score + 100);break;
            default:
                break;
        }
    }
    private void removePanel(JFrame frame){
        background_panel.removeAll();
        background_panel.revalidate();
        frame.remove(background_panel);
        frame.revalidate();
        frame.repaint();
    }
}

class GamePanel extends JPanel{
    public static final int GAME_AREA_X_CNT = 10, GAME_AREA_Y_CNT = 20;
    public static final int PREVIEW_AREA_X_CNT = 8, PREVIEW_AREA_Y_CNT = 12;
    public static final int X_START = 130, Y_START = 60;
    public static final int PREVIEW_AREA_X = X_START + 1 + 400, PREVIEW_AREA_Y = Y_START + 1 + 200; 
    public static final int GAME_AREA_X = X_START + 1, GAME_AREA_Y = Y_START + 1;

    public static Map<String,ImageIcon> cell_img = new HashMap<>();


    
    public static JLabel [][] getLabelPreview(){return label_preview;}
    public GamePanel() {
        // load all color cell into the cell_img map
        String [] colors_arr = {"red", "blue", "gray", "green", "lightblue", "orange", "purple", "yellow"};
        for(String color: colors_arr)
            cell_img.put(color, new ImageIcon("img\\" + color + ".jpg"));

        score = 0;
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
    public void cellPositionUpdate(Cell [][] game_area_cells){
        for(int i = 0; i < GAME_AREA_Y_CNT; i++){
            for(int j = 0; j < GAME_AREA_X_CNT; j++){
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


    public int getScore(){return score;}
    public void setScore(int new_score){
        score = new_score;
        if(label_score != null)
            this.remove(label_score);
        int score_x = PREVIEW_AREA_X + (PREVIEW_AREA_X_CNT / 2 - 1) * Cell.BLOCK_WIDTH + PREVIEW_AREA_X_CNT, 
            score_y = 120;
        label_score = labelMake(score_x, score_y, Integer.toString(score),120, 60, 40);
        label_score.setForeground(Color.WHITE);
        this.add(label_score);
    }
    // --------------------------------------
    private JLabel labelMake(int center_x, int center_y, String words, int words_width, int words_height){
        return labelMake(center_x, center_y, words, words_width, words_height, 20);
    }
    private JLabel labelMake(int center_x, int center_y, String words, int words_width, int words_height, int font_size){
        JLabel label = new JLabel(words);
        label.setBounds(center_x - words_width/2 , center_y - words_height/2, words_width, words_height);
        
        label.setFont(new Font("Arial", Font.BOLD, font_size));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        return label;
    }
    private void buildBorder(int x_cnt, int y_cnt, int x_init, int y_init, boolean preview_or_game){
        // load the gray_cell image
        for(int i = 0; i < y_cnt; i++){
            for(int j = 0;j < x_cnt; j++){
                if(i == 0 || i == y_cnt - 1 || j == 0 || j == x_cnt - 1){
                    
                    JLabel borderLabel = labelMake(x_init + Cell.BLOCK_WIDTH * j , y_init + Cell.BLOCK_HEIGHT * i , "", Cell.BLOCK_WIDTH - 3, Cell.BLOCK_HEIGHT - 3);
                    borderLabel.setIcon(cell_img.get("gray"));
                    this.add(borderLabel);
                }
                else if(preview_or_game == true){
                    label_cells[i - 1][j - 1] = labelMake(X_START + Cell.BLOCK_WIDTH * j + 1,Y_START + Cell.BLOCK_HEIGHT * i + 1,"", Cell.BLOCK_WIDTH - 3, Cell.BLOCK_HEIGHT - 3);
                    label_cells[i - 1][j - 1].setVisible(false);
                    this.add(label_cells[i - 1][j - 1]);
                }
                else{
                    JLabel innerLabel = labelMake(x_init + Cell.BLOCK_WIDTH * j , y_init + Cell.BLOCK_HEIGHT * i , "", Cell.BLOCK_WIDTH - 3, Cell.BLOCK_HEIGHT - 3);
                    label_preview[i - 1][j - 1] = innerLabel;
                    innerLabel.setVisible(false);
                    this.add(label_preview[i - 1][j - 1]);
                }
            }
        }
    }
    private void buildScore(int score_title_x, int score_title_y){
        JLabel score_title = labelMake(score_title_x, score_title_y, "Score", 120, 60, 40);
        score_title.setForeground(Color.WHITE); 
        this.add(score_title);

    }
    
    private void buildBoard(){
        // build the game area border, Note: GAME_AREA_X_CNT and GAME_AREA_Y_CNT is public static variable
        // true: game, false: preview
        buildBorder(GAME_AREA_X_CNT + 2, GAME_AREA_Y_CNT + 2, GAME_AREA_X, GAME_AREA_Y, true);
        // build the preview border
        buildBorder(PREVIEW_AREA_X_CNT, PREVIEW_AREA_Y_CNT, PREVIEW_AREA_X, PREVIEW_AREA_Y, false);
        int score_title_x = PREVIEW_AREA_X + (PREVIEW_AREA_X_CNT / 2 - 1) * Cell.BLOCK_WIDTH + PREVIEW_AREA_X_CNT, 
            score_title_y = 60;
        buildScore(score_title_x, score_title_y);
        setScore(0);
    }
    private int score;
    private static JLabel label_score = null;
    private static JLabel [][] label_cells = new JLabel[GAME_AREA_Y_CNT][GAME_AREA_X_CNT];
    private static JLabel [][] label_preview = new JLabel[PREVIEW_AREA_Y_CNT - 2][PREVIEW_AREA_X_CNT - 2];
}


class GameKeyHandler implements KeyListener{
    private static boolean left = false, right = false, down = false, space = false, left_rotate = false, right_rotate = false;
    private static boolean space_done = false, left_rotate_done = false, right_rotate_done;
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == 32 && space_done == false){ // space
            space = true;
            space_done = false;
        }
        else if(code == 65 && left_rotate_done == false){ // A is left rotate
            left_rotate = true;
            left_rotate_done = false;
        }
        else if(code == 68 && right_rotate_done == false){ // D is right rotate 
            right_rotate = true;
            right_rotate_done = false;
        }
        else if(code == 37) // left
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
        if(code == 32){  space = false; space_done = false;}
        if(code == 65){  left_rotate = false; left_rotate_done = false;}
        if(code == 68){  right_rotate = false; right_rotate_done = false;}
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    public static String getCurrentKey(){
        if(space == true && space_done == false)        return "space";
        else if(right_rotate == true && right_rotate_done == false)   return "right rotate";
        else if(left_rotate == true && left_rotate_done == false)   return "left rotate";
        else if(left == true)    return "left";
        else if(right == true) return "right";
        else if(down == true)  return "down";
        else                   return "null";
    }
    public static void changeSpaceState(){
        space_done = true;
    }
    public static void changeLeftRotateState(){
        left_rotate_done = true;
    }
    public static void changeRightRotateState(){
        right_rotate_done = true;
    }

    public static void resetSetting(){
        left = right = down = space = left_rotate = right_rotate = false;
        space_done = left_rotate_done = right_rotate_done = false;
    }
}