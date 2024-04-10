package blocks;

import java.util.ArrayList;
import java.util.List;

public class GameBlock {
    // color_list is for randomly selecting a color 
    public static final List<String> COLOR_LIST =  new ArrayList<>(){{
        add("blue");
        add( "green");
        add("lightblue");
        add("orange");
        add("purple");
        add("red");
        add("yellow");
        add("gray");
    }};
    // BLOCK_DIST is for four directions based on the the center cell (detail in the below)
    // block_type(0 ~ 6), block_directions(or state)(0 ~ 3), block_cell_from_center_cell(0 ~ 3), cell_y and cell_x(0 - 1)
    public static final int [][][][] BLOCK_DIST = {
        {{{0, -1}, {0, 0}, {0, 1}, {0, 2}}, {{-1, 0}, {0, 0}, {1, 0}, {2, 0}}, {{0, -2}, {0, -1}, {0, 0}, {0, 1}}, {{-2, 0}, {-1, 0}, {0, 0}, {1, 0}}},
        {{{0, 0}, {1, 0}, {0, 1}, {1, 1}}, {{0, 0}, {1, 0}, {0, 1}, {1, 1}}, {{0, 0}, {1, 0}, {0, 1}, {1, 1}}, {{0, 0}, {1, 0}, {0, 1}, {1, 1}}},
        {{{0, -1}, {0, 0}, {-1, 0}, {-1, 1}}, {{0, 0}, {-1, 0}, {0, 1}, {1, 1}}, {{1, -1}, {1, 0}, {0, 0}, {0, 1}}, {{-1, -1}, {0, -1}, {0, 0}, {1, 0}}},
        {{{0, 0}, {-1, -1}, {-1, 0}, {0, 1}}, {{0, 0}, {-1, 1}, {0, 1}, {1, 0}}, {{0, -1}, {0, 0}, {1, 0}, {1, 1}}, {{0, 0}, {1, -1}, {0, -1}, {-1, 0}}},
        {{{0, 0}, {0, -1}, {0, 1}, {-1, -1}}, {{0, 0}, {-1, 0}, {1, 0}, {-1, 1}}, {{0, 0}, {0, -1}, {0, 1}, {1, 1}}, {{0, 0}, {-1, 0}, {1, 0}, {1, -1}}},
        {{{0, 0}, {0, -1}, {0, 1}, {-1, 1}}, {{0, 0}, {-1, 0}, {1, 0}, {1, 1}}, {{0, 0}, {0, -1}, {0, 1}, {1, -1}}, {{0, 0}, {-1, 0}, {1, 0}, {-1, -1}}},
        {{{0, 0}, {0, -1}, {0, 1}, {-1, 0}}, {{0, 0}, {-1, 0}, {1, 0}, {0, 1}}, {{0, 0}, {0, -1}, {0, 1}, {1, 0}}, {{0, 0}, {-1, 0}, {1, 0}, {0, -1}}}
    };
    // Constructor overloading
    public GameBlock(int center_x_idx, int center_y_idx, int type, String color_str){
        for_preview = true;
        this.block_center_x = center_x_idx;
        this.block_center_y = center_y_idx;
        setColor(color_str);
        setBlockType(type);
    }

    public GameBlock(int type, int color_idx){
        for_preview = true;
        setColor(color_idx);
        setBlockType(type);
    }
    public GameBlock(int type, String color_str){
        for_preview = true;
        setColor(color_str);
        setBlockType(type);
    }
    
    public String getColor(){return color;}
    public int getBlockType(){return block_type;}
    public boolean getForPreview(){return for_preview;}
    public int getCenterX(){return block_center_x;}
    public int getCenterY(){return block_center_y;}
    public int getBlockState(){return block_state;}

    public void setColor(int color_idx){
        if(color_idx < COLOR_LIST.size())
            color = COLOR_LIST.get(color_idx);
        return;
    }
    public void setColor(String colorStr){
        if(COLOR_LIST.contains(colorStr))
            color = colorStr;
        return;
    }
    public void setBlockType(int idx){
        if(idx <= 6 && idx >= 0)
            block_type = idx;
        return;
    }
    public void setBlockCenter(int center_x, int center_y){
        this.block_center_x = center_x;
        this.block_center_y = center_y;
    }
    public void setState(int next_state){
        if(next_state >= 0 && next_state <= 3)
            block_state = next_state;
    }
    
    // -----------------------------
    private int block_center_x, block_center_y;
    private int block_type;
    private int block_state;
    private String color;
    private boolean for_preview;
}

/*
* center_cell(o), other_cells(x)
* block_type
* I block
*  0-0              0-1             0-2             0-3
*  * * * * * *      * * * * * *     * * * * * *     * * x * * *
*  * * * * * *      * * x * * *     * * * * * *     * * x * * *
*  * x o x x *      * * o * * *     x x o x * *     * * o * * *
*  * * * * * *      * * x * * *     * * * * * *     * * x * * *
*  * * * * * *      * * x * * *     * * * * * *     * * * * * *

*  O block (rotate is useless)
*  1-0              1-1             1-2             1-3
*  * * * * * *      * * * * * *     * * * * * *     * * * * * *
*  o x * * * *      * * * * * *     * * * * * *     * * * * * *
*  x x * * * *      * * * * * *     * * * * * *     * * * * * *

*  S block
*  2-0              2-1             2-2             2-3
*  * * x x * *      * * x * * *     * * * * * *     * x * * * *
*  * x o * * *      * * o x * *     * * o x * *     * x o * * *
*  * * * * * *      * * * x * *     * x x * * *     * * x * * *

*  Z block
*  3-0              3-1             3-2             3-3
*  * x x * * *      * * * x * *     * * * * * *     * * x * * *
*  * * o x * *      * * o x * *     * x o * * *     * x o * * *
*  * * * * * *      * * x * * *     * * x x * *     * x * * * *

*  J block
*  4-0              4-1             4-2             4-3 
*  * x * * * *      * * x x * *     * * * * * *     * * x * * *
*  * x o x * *      * * o * * *     * x o x * *     * * o * * * 
*  * * * * * *      * * x * * *     * * * x * *     * x x * * * 

*  J block
*  5-0              5-1             5-2             5-3 
*  * * * x * *      * * x * * *     * * * * * *     * x x * * *
*  * x o x * *      * * o * * *     * x o x * *     * * o * * * 
*  * * * * * *      * * x x * *     * x * * * *     * * x * * * 
*  T block
*  6-0              6-1             6-2             6-3 
*  * * x * * *      * * x * * *     * * * * * *     * * x * * *
*  * x o x * *      * * o x * *     * x o x * *     * x o * * * 
*  * * * * * *      * * x * * *     * * x * * *     * * x * * * 
*/