package states.gameState;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.nio.file.Paths;

import blocks.*;
import musicPlayer.MusicPlayer;
import musicPlayer.SoundType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.function.*;

import states.ScreenPanel.*;

public class GameScreen { 
    public static final int GAME_SPEEDUP_STATE_SCORE[] = {0, 500, 1000, -1}; // the score required into the next state
    public static final int GAME_STATE_AUTO_FALL_TICK[] = {0, 6, 4, 3}; // change required auto-fall tick number to satisfy the speedup function
    public static int GAME_CLEAR_SCORE = 1300;
    public static int time_ticks = 50;

    public GameScreen(JFrame frame, SoundType sound_mode){
        this.sound_mode = sound_mode;
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
        background_panel.updateScoreDisplay(score);
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
                if(checkNextGameState() && sound_mode == SoundType.UNMUTE){
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
                        
                        checkGameLine(game_area_cells);
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
                        updateCurrentBlock();
                        background_panel.paintPreviewBlock(preview_blocks);

                    }
                    updateGhostBlock();
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
                            checkGameLine(game_area_cells);
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
                        checkGameLine(game_area_cells);
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
                        

                    background_panel.changeStateColor(current_speedup_state);
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
        private void updateGhostBlock(){
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
        private void updateCurrentBlock(){
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
                background_panel.updateStateDisplay(current_speedup_state);
            }
            return next_state;
        }
        private void checkGameLine(Cell [][] game_area_cells){
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
    
            if(sound_mode == SoundType.UNMUTE){
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
            background_panel.updateScoreDisplay(score);
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
    private SoundType sound_mode;
}

