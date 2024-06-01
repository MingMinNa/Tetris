package State.gameState;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.nio.file.Paths;

import blocks.*;
import music.MusicPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.function.*;

import State.ScreenPanel;



public class GameScreen { 
    public static final int GAME_SPEEDUP_STATE_SCORE[] = {0, 500, 1000, -1}; // the score required into the next state
    public static final int GAME_STATE_AUTO_FALL_TICK[] = {0, 6, 4, 3}; // change required auto-fall tick number to satisfy the speedup function
    public static int GAME_CLEAR_SCORE = 1300;
    public static int time_ticks = 50;

    public GameScreen(JFrame frame, boolean unmute){
        this.unmute = unmute;
        for(int i = 0; i < game_area_cells.length; i ++){
            for(int j = 0; j < game_area_cells[0].length; j ++){
                game_area_cells[i][j] = new Cell(j, i, "black");
            }
        }
        for(int i = 0; i < 2; i++){
            Random rnd = new Random();
            preview_blocks[i] = new GameBlock(rnd.nextInt(7), rnd.nextInt(7));
        }
        for(int i = 0;i < 4;i ++)
            ghost_block_cells[i] = null;

        background_panel = new GamePanel();
        frame.getContentPane().add(background_panel);
        background_panel.paintPreviewBlock(preview_blocks);
        background_panel.revalidate();
        background_panel.requestFocusInWindow();
        background_panel.scoreDisplayUpdate(score);
        frame.revalidate();
        frame.repaint();

        // after building the component, change control to GameRunner
        boolean game_over = new GameRunner().run(frame);
        
        background_panel.getGameEndPanel().GameEndState(game_over);

        // Remove all the components in the GameScreen
        removePanel(frame);
    }
    // inner class
    class GameRunner{

        public boolean run(JFrame frame) {
            MusicPlayer music_player = null;
            boolean game_over = false;
            // auto_fall_ticks is for auto-fall; movement_time_ticks is for left, right and down
            // auto-fall_time_ticks = GAME_STATE_AUTO_FALL_TICK[ speedup_state ] * time_ticks, 
            // movement_time_ticks = 2 * time_ticks
            int auto_fall_ticks = 0, movement_ticks = 0;

            // The description explains show Why I use "left_right_move" variable
            /*  to prevent the the block from moving horizontally twice with only one click
                For example, if you press and quickly release key "D", it should be expected to move right once
                but sometimes, due to ths fast time ticks, it will move right twice with one click.
 
                To fix the above problem
                For the first movement (left or right), it move as soon as possible (two movement_ticks)
                but for the second movement, it need four movement_ticks to move.
                then for the third and so on movement, it need two movement_ticks to move(same as other operations) 
            */
            int left_right_move = 0;  

            GAME_END_LABEL:while(true){
                // If the score reach the requirement to the next state, change the game music and enhance the game level(hardness).
                if(checkNextGameState() && unmute){
                    if(music_player != null){music_player.stopPlaying(); music_player.interrupt();}
                    music_player = new MusicPlayer("GameState" + Integer.toString(current_speedup_state), true);
                    music_player.start();
                }
                long current_time = System.currentTimeMillis();
                long delta_time = current_time - last_update_time;
                GameKeyHandler key_handler = background_panel.getHandler();
                
                if(auto_fall_ticks >= GAME_STATE_AUTO_FALL_TICK[current_speedup_state]){
                    auto_fall_ticks = 0;
                    background_panel.blockPositionUpdate(game_area_cells);
        
                    if(tryMove("down") == false){
                        if(current_cells[3].getY() <= 2){
                            game_over = true;
                            break GAME_END_LABEL;
                        }
                        current_block = null;
                        
                        gameLineCheck(game_area_cells);
                        background_panel.blockPositionUpdate(game_area_cells);
                    }
                    background_panel.revalidate();
                    frame.revalidate();
                    frame.repaint();
                }
                else if(delta_time >= time_ticks){

                    auto_fall_ticks++;
                    if(movement_ticks < 2) movement_ticks++;
                    last_update_time = current_time;

                    if(current_block == null){
                        currentBlockUpdate();
                        background_panel.paintPreviewBlock(preview_blocks);

                    }
                    ghostBlockUpdate();
                    // keyboard operation
                    String pressed_key = key_handler.getCurrentKey();

                    if( pressed_key.equals("left") && movement_ticks >= 2){
                        if(left_right_move != 1)
                            tryMove("left");
                        left_right_move = (left_right_move >= 1)?(2):(1);
                    }   
                    else if(pressed_key.equals("right") && movement_ticks >= 2){
                        if(left_right_move != 1)
                            tryMove("right");
                        left_right_move = (left_right_move >= 1)?(2):(1);
                    }
                    else if(pressed_key.equals("down") && movement_ticks >= 2){
                        auto_fall_ticks = 0;
                        if(tryMove("down") == false){
                            if(current_cells[3].getY() <= 2){
                                game_over = true;
                                break GAME_END_LABEL;
                            }
                            current_block = null;
                            gameLineCheck(game_area_cells);
                        }
                    }
                    else if(pressed_key.equals("space")){
                        auto_fall_ticks = 0;key_handler.changeSpaceState();
                        while(tryMove("down") == true);
                        if(current_cells[3].getY() <= 2){
                            game_over = true;
                            break GAME_END_LABEL;
                        }
                        current_block = null;
                        gameLineCheck(game_area_cells);
                    }
                    else if(pressed_key.equals("right rotate")){
                        key_handler.changeRightRotateState();
                        tryRotate("right");
                    }
                    else if(pressed_key.equals("left rotate")){
                        key_handler.changeLeftRotateState();
                        tryRotate("left");
                    }

                    // the next left or right movement will be seen as the first one (reset)
                    boolean nothing_pressed = pressed_key.equals("null");
                    if(nothing_pressed){left_right_move = 0;}

                    if(movement_ticks >= 2 && nothing_pressed == false)
                        movement_ticks = 0;
                        

                    background_panel.stateColorChange(current_speedup_state);
                    background_panel.blockPositionUpdate(game_area_cells);
                    background_panel.revalidate();
                    frame.revalidate();
                    frame.repaint();
                    if(score >= GAME_CLEAR_SCORE)
                        break GAME_END_LABEL;
                }
                try{Thread.sleep(5);}
                catch(InterruptedException e){e.printStackTrace();}
            }
            if(music_player != null)
                {music_player.stopPlaying(); music_player.interrupt();}

            return game_over; // true: game_over, false: game_clear
        }

        private boolean tryMove(String direction){
            if (direction.equals("down") &&
                checkMove(current_cells, game_area_cells, direction)){
                for(int i = 0; i < 4; i ++ ){
                    Cell cell = current_cells[i];
                    game_area_cells[cell.getY() + 1][cell.getX()].setColor(cell.getColor());
                    game_area_cells[cell.getY()][cell.getX()].setColor("black");
                    current_cells[i] = game_area_cells[cell.getY() + 1][cell.getX()];
                }
                current_block.setBlockCenter(current_block.getCenterX(), current_block.getCenterY() + 1);
                return true;
            }
            else if(direction.equals("left") && 
                    checkMove(current_cells, game_area_cells, direction)){
                for(int i = 0; i < 4; i ++ ){
                    Cell cell = current_cells[i];
                    game_area_cells[cell.getY()][cell.getX() - 1].setColor(cell.getColor());
                    game_area_cells[cell.getY()][cell.getX()].setColor("black");
                    current_cells[i] = game_area_cells[cell.getY()][cell.getX() - 1];
                }
                current_block.setBlockCenter(current_block.getCenterX() - 1, current_block.getCenterY());
                return true;
            }
            else if(direction.equals("right") && 
                    checkMove(current_cells, game_area_cells, direction)){
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
        private void tryRotate(String direction){
            if(checkRotate(current_block, current_cells, game_area_cells, direction)){

                int next_dir;
                if(direction.equals("right"))    next_dir = (current_block.getBlockDir() + 1) % 4;
                else                                             next_dir = (current_block.getBlockDir() - 1 + 4) % 4;
                int block_type = current_block.getBlockType();
                String block_color = current_block.getColor();
                int center_x = current_block.getCenterX(), center_y = current_block.getCenterY();
                
                for(int i = 0; i < 4;i++)
                    game_area_cells[current_cells[i].getY()][current_cells[i].getX()].setColor("black");
                for(int i = 0; i < 4;i++){
                    int next_x = center_x + GameBlock.BLOCK_DIST[block_type][next_dir][i][1];
                    int next_y = center_y + GameBlock.BLOCK_DIST[block_type][next_dir][i][0];
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
                current_block.setDirection(next_dir);
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
            int next_dir;
            if(direction.equals("right"))    next_dir = (current_block.getBlockDir() + 1) % 4;
            else                                             next_dir = (current_block.getBlockDir() - 1 + 4) % 4;
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
                int next_x = center_x + GameBlock.BLOCK_DIST[block_type][next_dir][i][1];
                int next_y = center_y + GameBlock.BLOCK_DIST[block_type][next_dir][i][0];
                if(next_y < 0 || next_y >= GamePanel.GAME_AREA_Y_CNT + 3){
                    rotate = false;break;
                }
                if(next_x < 0){
                    if(tryMove("right") == false)   return false;
                    return checkRotate(current_block,current_cells,game_area_cells,direction);
                }
                else if(next_x >= GamePanel.GAME_AREA_X_CNT){
                    if(tryMove("left") == false) return false;
                    return checkRotate(current_block,current_cells,game_area_cells,direction);
                }
                if(current_cells_pos.contains(100 * (next_x) + next_y) == false && game_area_cells[next_y][next_x].getColor().equals("black") == false){
                    rotate = false;break;
                }
            }
            return rotate;
        }
        private void ghostBlockUpdate(){
            JLabel [][][] cell_borders = background_panel.getCellBorder();
            // [0]: vertical, [1]: horizontal
            for(int i = 0; i < 4; i ++){
                if(ghost_block_cells[i] == null){
                    ghost_block_cells[i] = new Cell(current_cells[i]);
                    continue;
                }
                int last_x = ghost_block_cells[i].getX(), last_y = ghost_block_cells[i].getY() - 3;
                if(last_y >= 0){
                    cell_borders[0][last_y][last_x].setIcon(GamePanel.cell_img.get("gray"));
                    cell_borders[0][last_y][last_x + 1].setIcon(GamePanel.cell_img.get("gray"));
                    cell_borders[1][last_y][last_x].setIcon(GamePanel.cell_img.get("gray"));
                    cell_borders[1][last_y + 1][last_x].setIcon(GamePanel.cell_img.get("gray"));    
                }
                ghost_block_cells[i].setX(current_cells[i].getX());
                ghost_block_cells[i].setY(current_cells[i].getY());
            }
            while(checkMove(ghost_block_cells, game_area_cells, "down")){
                for(int i = 0; i < 4;i ++)
                    ghost_block_cells[i].setY(ghost_block_cells[i].getY() + 1);
            }
            for(int i = 0; i < 4; i++){
                int last_x = ghost_block_cells[i].getX(), last_y = ghost_block_cells[i].getY() - 3;
                if(last_y < 0 ) continue;
                cell_borders[0][last_y][last_x].setIcon(GamePanel.cell_img.get(current_cells[0].getColor()));
                cell_borders[0][last_y][last_x + 1].setIcon(GamePanel.cell_img.get(current_cells[0].getColor()));
                cell_borders[1][last_y][last_x].setIcon(GamePanel.cell_img.get(current_cells[0].getColor()));
                cell_borders[1][last_y + 1][last_x].setIcon(GamePanel.cell_img.get(current_cells[0].getColor()));
            }
    
        }
        private void currentBlockUpdate(){
            // the preview_blocks[0] will become the current_block we can operate
            int block_type = preview_blocks[0].getBlockType();
            String block_color = preview_blocks[0].getColor();
    
            // preview_blocks[1] -> preview_blocks[0], random_block -> preview_blocks[1]
            Random rnd = new Random();
            preview_blocks[0].setBlockType(preview_blocks[1].getBlockType());
            preview_blocks[0].setColor(preview_blocks[1].getColor());
            preview_blocks[1].setBlockType(rnd.nextInt(7));
            preview_blocks[1].setColor(rnd.nextInt(7));
    
            // initialize falling x_axis
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
    
                game_area_cells[colored_pos[block_type][i][0]][colored_pos[block_type][i][1] + init_x].setColor(block_color);
                current_cells[i] = game_area_cells[colored_pos[block_type][i][0]][colored_pos[block_type][i][1] + init_x];
            }
            // sort the current_cells based on x and y. (to simplify the current_block movement code)
            // for example, assume current_cells has been sorted, then 
            // current_cells[0].getY() > current_cells[1].getY() or 
            // if current_cells[0].getY() == current_cells[1].getY(), current_cells[0].getX() < current_cells[1].getX()
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
        private boolean checkNextGameState(){
            boolean next_state = false; 
            while(GAME_SPEEDUP_STATE_SCORE[current_speedup_state] >= 0 && score >= GAME_SPEEDUP_STATE_SCORE[current_speedup_state]){
                current_speedup_state = current_speedup_state + 1;
                next_state = true;
                background_panel.stateDisplayUpdate(current_speedup_state);
            }
            return next_state;
        }
        private void gameLineCheck(Cell [][] game_area_cells){
            int del_line = 0;
            for(int i = game_area_cells.length - 1; i > 1; i--){
                boolean all_filled = true;
                // check whether the line is filled with blocks_cells
                for(int j = 0; j < game_area_cells[0].length; j++){
                    if(game_area_cells[i][j].getColor().equals("black")){all_filled = false; break;}
                }
                // there exist at least one grid(cell) empty(color:black).
                if(all_filled == false){
                    for(int j = 0; j < game_area_cells[0].length; j++){
                        game_area_cells[i + del_line][j].setColor(game_area_cells[i][j].getColor());
                        if(del_line != 0)
                            game_area_cells[i][j].setColor("black");
                    }
                }
                else{
                    for(int j = 0; j < game_area_cells[0].length; j++){
                        game_area_cells[i][j].setColor("black");
                        background_panel.blockPositionUpdate(game_area_cells);
                        try {Thread.sleep(50);}
                        catch(InterruptedException e) {e.printStackTrace();}
                    }
                    del_line++;
                }
            }
            if(del_line == 0)   return;
    
            if(unmute){
                MusicPlayer delete_player = new MusicPlayer("DeleteLine", false);
                delete_player.start();
            }
            switch(del_line){
                case 4: score += 400;break;
                case 3: score += 250;break;
                case 2: score += 150;break;
                case 1: score += 50;break;
                default:
                    break;
            }
            background_panel.scoreDisplayUpdate(score);
        }
    }

    // ----------------------------------------
    private void removePanel(JFrame frame){
        background_panel.removeAll();
        background_panel.revalidate();
        frame.remove(background_panel);
        frame.revalidate();
        frame.repaint();
    }

    private long last_update_time = System.currentTimeMillis();
    private GamePanel background_panel;

    private Cell [][] game_area_cells = new Cell[23][10];
    private GameBlock [] preview_blocks = new GameBlock[2];
    private GameBlock current_block = null;
    private Cell [] ghost_block_cells = new Cell[4];
    private Cell [] current_cells =  new Cell[4];

    // Game_state_data
    private int current_speedup_state = 0; 
    private int score = 0;
    private boolean unmute;
}

class GamePanel extends JPanel implements ScreenPanel{
    public static final int GAME_AREA_X_CNT = 10, GAME_AREA_Y_CNT = 20;
    public static final int PREVIEW_AREA_X_CNT = 8, PREVIEW_AREA_Y_CNT = 12;
    public static final int X_START = 130, Y_START = 100;
    public static final int PREVIEW_AREA_X = X_START + 1 + 400, PREVIEW_AREA_Y = Y_START + 1 + 200; 
    public static final int GAME_AREA_X = X_START + 1, GAME_AREA_Y = Y_START + 1;
    public static final int FRAME_WIDTH = 800, FRAME_HEIGHT = 800;

    public static Map<String,ImageIcon> cell_img = new HashMap<>();

    public GamePanel() {
        // load all color cell into the cell_img map
        for(int i = 0;i < GameBlock.COLOR_LIST.size();i++){
            String color = GameBlock.COLOR_LIST.get(i);
            cell_img.put(color, new ImageIcon(Paths.get("img", "game_img", color + ".jpg").toString()));
        }
        setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        setFocusable(true);
        setBackground(Color.BLACK);
        setLayout(null);

        game_end_panel = new GameEND(this);
        
        buildBoard();
        handler = new GameKeyHandler();
        addKeyListener(handler);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }
    public void stateDisplayUpdate(int state){
        if(state_display != null)   this.remove(state_display);
        int display_x = X_START + 6 * Cell.BLOCK_WIDTH, display_y = 40; 
        state_display = labelMake(display_x, display_y, "--- State " + state + " ---", 250, 60, 40);
        state_display.setForeground(Color.RED);
        add(state_display);
    }
    public void stateColorChange(int current_state){
        int new_color[] = {state_display.getForeground().getRed(),
                        state_display.getForeground().getGreen(),
                        state_display.getForeground().getBlue()}; // color[0]:red, color[1]: green, color[2]:blue

        int delta_value = 5*current_state;
        int increment_index = 0, decrement_index = 0;
        // if(new_color[0] > 0 && new_color[2] == 0)   { decrement_index = 0; increment_index = 1;}
        // else if( new_color[0] == 0 && new_color[1] > 0) { decrement_index = 1; increment_index = 2;}
        // else { decrement_index = 2; increment_index = 0;}
        for(int i = 0; i < 3;i ++){
            // for i = 0, 1, 2, one of the conditions must be true
            if(new_color[i] > 0 && new_color[(i + 2) % 3] == 0 ){
                decrement_index = i; increment_index = (i + 1) % 3;
                break;
            }
        }
        new_color[increment_index] += delta_value;
        new_color[decrement_index] -= delta_value;
        if(new_color[increment_index] > 255) new_color[increment_index] = 255;
        if(new_color[decrement_index] < 0) new_color[decrement_index] = 0;
        state_display.setForeground(new Color(new_color[0], new_color[1], new_color[2]));
    }
    public void scoreDisplayUpdate(int score){
        // refresh
        int score_height_cnt = 5, score_witdh_cnt = 15;
        for(int i = 0; i < score_height_cnt;i ++){
            for(int j = 0; j < score_witdh_cnt;j ++){
                label_score_display[i][j].setVisible(false);
            }
        }
        /*
         * (0) 
         *  x x x 
         *  x o x
         *  x o x
         *  x o x
         *  x x x
         * (1)
         *  o x o 
         *  x x o
         *  o x o
         *  o x o
         *  x x x
         * (2)
         *  x x x 
         *  o o x
         *  x x x
         *  x o o
         *  x x x
         * (3)
         *  x x x 
         *  o o x
         *  x x x
         *  o o x
         *  x x x
         * (4)
         *  x o x 
         *  x o x
         *  x x x
         *  o o x
         *  o o x
         * (5)
         *  x x x 
         *  x o o
         *  x x x
         *  o o x
         *  x x x
         * (6)
         *  x x x 
         *  x o o
         *  x x x
         *  x o x
         *  x x x
         * (7)
         *  x x x 
         *  x o x
         *  x o x
         *  o o x
         *  o o x
         * (8)
         *  x x x 
         *  x o x
         *  x x x
         *  x o x
         *  x x x
         * (9)
         *  x x x 
         *  x o x
         *  x x x
         *  o o x
         *  x x x
         */
        String [] relative_position = {
            "xxxxoxxoxxoxxxx",
            "oxoxxooxooxoxxx",
            "xxxooxxxxxooxxx",
            "xxxooxxxxooxxxx",
            "xoxxoxxxxooxoox",
            "xxxxooxxxooxxxx",
            "xxxxooxxxxoxxxx",
            "xxxxoxooxooxoox",
            "xxxxoxxxxxoxxxx",
            "xxxxoxxxxooxxxx"
        };
        // current digit have 5 , and the number from 0 to 99999 can be represented.
        for(int i = 0;i < 5;i ++){
            int score_digit_x = i * 3, score_digit_y = 0;
            int digit = (score / (int)Math.pow(10, 5 - i - 1)) % 10;
            for(int j = 0; j < 15; j++){
                if(relative_position[digit].charAt(j) == 'o')   continue;
                int score_cell_x = score_digit_x + j % 3, score_cell_y = score_digit_y + j / 3;
                label_score_display[score_cell_y][score_cell_x].setVisible(true);
            }
        }
        return;
    }
    public void blockPositionUpdate(Cell [][] game_area_cells){

        for(int i = 0; i < GAME_AREA_Y_CNT; i++){
            for(int j = 0; j < GAME_AREA_X_CNT; j++){
                if (game_area_cells[i + 3][j].getColor().equals("black")){
                    label_cells[i][j].setVisible(false);
                }
                else{
                    label_cells[i][j].setVisible(true);
                    label_cells[i][j].setIcon(cell_img.get(game_area_cells[i + 3][j].getColor()));
                }
            }
        }
    }
    /* for paintPreviewBlock function 
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
    public void paintPreviewBlock(GameBlock [] blocks){
        int [][][] colored_pos = {{{3, 2}, {3, 3}, {3, 4}, {3, 5}}, { {2, 3}, {2, 4}, {3, 3}, {3, 4}},
        { {2, 3}, {2, 4}, {3, 2}, {3, 3}}, { {2, 3}, {2, 4}, {3, 4}, {3, 5}}, { {2, 2}, {3, 2}, {3, 3}, {3, 4}}, { {2, 5}, {3, 3}, {3, 4}, {3, 5}}, { {2, 3}, {3, 2}, {3, 3}, {3, 4}}};

        for(int i = 0; i < label_preview.length; i++){
            for(int j = 0; j < label_preview[0].length; j++){
                label_preview[i][j].setVisible(false);
            }
        }
        int type = blocks[0].getBlockType();
        for(int i = 0; i < 4;i ++){
            label_preview[colored_pos[type][i][0] - 1][colored_pos[type][i][1] - 1].setVisible(true);
            label_preview[colored_pos[type][i][0] - 1][colored_pos[type][i][1] - 1].setIcon(cell_img.get(blocks[0].getColor()));
        }
        type = blocks[1].getBlockType();
        for(int i = 0; i < 4;i ++){
            label_preview[colored_pos[type][i][0] - 1 + 6][colored_pos[type][i][1] - 1].setVisible(true);
            label_preview[colored_pos[type][i][0] - 1 + 6][colored_pos[type][i][1] - 1].setIcon(cell_img.get(blocks[1].getColor()));
        }
        this.revalidate();
    }
    public JLabel[][][] getCellBorder(){
        return this.label_cell_border;
    }
    public GameEND getGameEndPanel(){
        return this.game_end_panel;
    }
    public GameKeyHandler getHandler(){
        return this.handler;
    }
    // --------------------------------------
    private void buildCellBorderLine(){
        // This feature is currently turned off because the border line may cause eye discomfort
        
        // vertical border line
        int x_init = X_START + Cell.BLOCK_WIDTH / 2, y_init = Y_START;
        for(int i = 1; i <= GAME_AREA_Y_CNT; i ++){
            for(int j = 0; j <= GAME_AREA_X_CNT; j++){
                JLabel cell_border = labelMake(x_init + Cell.BLOCK_WIDTH * j + 1, y_init + Cell.BLOCK_HEIGHT * i , "",  3, Cell.BLOCK_HEIGHT - 3);
                cell_border.setIcon(cell_img.get("gray"));
                label_cell_border[0][i-1][j] = cell_border;
                this.add(cell_border);
            }
        }
        // horizontal border line
        x_init = X_START; y_init = Y_START +  + Cell.BLOCK_HEIGHT / 2;
        for(int i = 0; i <= GAME_AREA_Y_CNT; i ++){
            for(int j = 1; j <= GAME_AREA_X_CNT; j++){
                JLabel cell_border = labelMake(x_init + Cell.BLOCK_WIDTH * j, y_init + Cell.BLOCK_HEIGHT * i + 1 , "",  Cell.BLOCK_WIDTH - 3, 3);
                cell_border.setIcon(cell_img.get("gray"));
                label_cell_border[1][i][j-1] = cell_border; 
                this.add(cell_border);
            }
        }
    }
    private void buildBorder(int x_cnt, int y_cnt, int x_init, int y_init, boolean preview_or_game){
        // load the gray_cell image
        for(int i = 0; i < y_cnt; i++){
            for(int j = 0;j < x_cnt; j++){
                if(i == 0 || i == y_cnt - 1 || j == 0 || j == x_cnt - 1){
                    
                    JLabel border_label = labelMake(x_init + Cell.BLOCK_WIDTH * j , y_init + Cell.BLOCK_HEIGHT * i , "", Cell.BLOCK_WIDTH - 4, Cell.BLOCK_HEIGHT - 4);
                    border_label.setIcon(cell_img.get("gray"));
                    this.add(border_label);
                }
                else if(preview_or_game == true){
                    label_cells[i - 1][j - 1] = labelMake(X_START + Cell.BLOCK_WIDTH * j + 1,Y_START + Cell.BLOCK_HEIGHT * i + 1,"", Cell.BLOCK_WIDTH - 4, Cell.BLOCK_HEIGHT - 4);
                    label_cells[i - 1][j - 1].setVisible(false);
                    this.add(label_cells[i - 1][j - 1]);
                }
                else{
                    JLabel innerLabel = labelMake(x_init + Cell.BLOCK_WIDTH * j , y_init + Cell.BLOCK_HEIGHT * i , "", Cell.BLOCK_WIDTH - 4, Cell.BLOCK_HEIGHT - 4);
                    label_preview[i - 1][j - 1] = innerLabel;
                    innerLabel.setVisible(false);
                    this.add(label_preview[i - 1][j - 1]);
                }
            }
        }
    }
    private void buildScoreTitle(int score_title_x, int score_title_y){
        JLabel score_title = labelMake(score_title_x, score_title_y, "Score", 120, 60, 40);
        score_title.setForeground(Color.WHITE); 
        this.add(score_title);

    }
    private void buildScoreDisplay(int score_x, int score_y){
        int score_witdh = Cell.BLOCK_WIDTH - 15, score_height = Cell.BLOCK_HEIGHT - 15;
        int score_height_cnt = 5, score_witdh_cnt = 15;
        for(int i = 0; i < score_height_cnt; i++){
            for(int j = 0; j < score_witdh_cnt; j++){
                label_score_display[i][j] = labelMake(score_x + (j - 7) * score_witdh + 5 + (j / 3 - 2) * 10, score_y + score_height * i, "", score_witdh - 3, score_height - 3);
                label_score_display[i][j].setIcon(cell_img.get("gray"));
                label_score_display[i][j].setVisible(false);
                this.add(label_score_display[i][j]);
            }
        }
    }
    private void buildBoard(){
        // build the game area border, Note: GAME_AREA_X_CNT and GAME_AREA_Y_CNT is public static variable
        // true: game_border, false: preview_border
        buildBorder(GAME_AREA_X_CNT + 2, GAME_AREA_Y_CNT + 2, GAME_AREA_X, GAME_AREA_Y, true);
        // build the preview border
        buildBorder(PREVIEW_AREA_X_CNT, PREVIEW_AREA_Y_CNT, PREVIEW_AREA_X, PREVIEW_AREA_Y, false);
        int score_title_x = PREVIEW_AREA_X + (PREVIEW_AREA_X_CNT / 2 - 1) * Cell.BLOCK_WIDTH + PREVIEW_AREA_X_CNT, 
            score_title_y = 60;
        buildScoreTitle(score_title_x, score_title_y);
        int score_x = PREVIEW_AREA_X + (PREVIEW_AREA_X_CNT / 2 - 1) * Cell.BLOCK_WIDTH + PREVIEW_AREA_X_CNT, 
            score_y = 120;
        buildScoreDisplay(score_x, score_y);

        // To have border line, just remove the comments below. 
        buildCellBorderLine(); 
    }
    private JLabel [][] label_score_display = new JLabel[5][15]; // int score_height_cnt = 5, score_witdh_cnt = 15;
    private JLabel [][] label_cells = new JLabel[GAME_AREA_Y_CNT][GAME_AREA_X_CNT];
    private JLabel [][] label_preview = new JLabel[PREVIEW_AREA_Y_CNT - 2][PREVIEW_AREA_X_CNT - 2];
    
    private JLabel [][][] label_cell_border = new JLabel[2][GAME_AREA_Y_CNT + 1][GAME_AREA_X_CNT + 1];
    private GameEND game_end_panel = null;
    private GameKeyHandler handler = null;
    private JLabel state_display = null;
}


class GameKeyHandler implements KeyListener{
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