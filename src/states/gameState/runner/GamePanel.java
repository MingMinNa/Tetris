package states.gameState.runner;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.nio.file.Paths;

import blocks.*;

import java.util.HashMap;
import java.util.Map;

import states.ScreenPanel;


public class GamePanel extends JPanel implements ScreenPanel{
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
        
        
        buildBoard();
        handler = new GameKeyHandler();
        addKeyListener(handler);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });

    }
    public void updateStateDisplay(int state){
        if(state_display != null)   this.remove(state_display);
        int display_x = X_START + 6 * Cell.BLOCK_WIDTH, display_y = 40; 
        state_display = labelMake(display_x, display_y, "--- State " + state + " ---", 250, 60, 40);
        state_display.setForeground(Color.RED);
        add(state_display);
    }
    public void changeStateColor(int current_state){
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
    public void updateScoreDisplay(int score){
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
    public void updateBlockPosition(Cell [][] game_area_cells){

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
    public GameKeyHandler getHandler(){
        return this.handler;
    }
    // --------------------------------------
    private void buildCellBorderLine(){
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
    private GameKeyHandler handler = null;
    private JLabel state_display = null;
}


