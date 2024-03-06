package blocks;

import java.util.ArrayList;
import java.util.List;

public class GameBlock {
    public static List<String> color_list =  new ArrayList<>(){{
        add("blue");
        add( "green");
        add("lightblue");
        add("orange");
        add("purple");
        add("red");
        add("yellow");
        add("gray");
    }};
    // block_type(0 ~ 6), block_state(0 ~ 3), block_cell_from_center_cell(0 ~ 3), cell_y and cell_x(0 - 1)
    public static int [][][][] block_dist = {
        {{{0, -1}, {0, 0}, {0, 1}, {0, 2}}, {{-1, 0}, {0, 0}, {1, 0}, {2, 0}}, {{0, -2}, {0, -1}, {0, 0}, {0, 1}}, {{-2, 0}, {-1, 0}, {0, 0}, {1, 0}}},
        {{{0, 0}, {1, 0}, {0, 1}, {1, 1}}, {{0, 0}, {1, 0}, {0, 1}, {1, 1}}, {{0, 0}, {1, 0}, {0, 1}, {1, 1}}, {{0, 0}, {1, 0}, {0, 1}, {1, 1}}},
        {{{0, -1}, {0, 0}, {-1, 0}, {-1, 1}}, {{0, 0}, {-1, 0}, {0, 1}, {1, 1}}, {{1, -1}, {1, 0}, {0, 0}, {0, 1}}, {{-1, -1}, {0, -1}, {0, 0}, {1, 0}}},
        {{{0, 0}, {-1, -1}, {-1, 0}, {0, 1}}, {{0, 0}, {-1, 1}, {0, 1}, {1, 0}}, {{0, -1}, {0, 0}, {1, 0}, {1, 1}}, {{0, 0}, {1, -1}, {0, -1}, {-1, 0}}},
        {{{0, 0}, {0, -1}, {0, 1}, {-1, -1}}, {{0, 0}, {-1, 0}, {1, 0}, {-1, 1}}, {{0, 0}, {0, -1}, {0, 1}, {1, 1}}, {{0, 0}, {-1, 0}, {1, 0}, {1, -1}}},
        {{{0, 0}, {0, -1}, {0, 1}, {-1, 1}}, {{0, 0}, {-1, 0}, {1, 0}, {1, 1}}, {{0, 0}, {0, -1}, {0, 1}, {1, -1}}, {{0, 0}, {-1, 0}, {1, 0}, {-1, -1}}},
        {{{0, 0}, {0, -1}, {0, 1}, {-1, 0}}, {{0, 0}, {-1, 0}, {1, 0}, {0, 1}}, {{0, 0}, {0, -1}, {0, 1}, {1, 0}}, {{0, 0}, {-1, 0}, {1, 0}, {0, -1}}}
    };
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
    public void setColor(int color_idx){
        if(color_idx < color_list.size())
            color = color_list.get(color_idx);
        return;
    }
    public void setColor(String colorStr){
        if(color_list.contains(colorStr)){
            color = colorStr;
        }
        return;
    }

    public int getBlockType(){return block_type;}
    public void setBlockType(int idx){
        if(idx <= 6 && idx >= 0)
            block_type = idx;
    }

    
    public boolean getForPreview(){return for_preview;}
    
    public void setBlockCenter(int center_x, int center_y){
        this.block_center_x = center_x;
        this.block_center_y = center_y;
    }
    public int getCenterX(){return block_center_x;}
    public int getCenterY(){return block_center_y;}
    public int getBlockState(){return block_state;}
    public void nextState(){block_state = (block_state + 1) % 4;}
    // -----------------------------
    private int block_center_x, block_center_y;
    private int block_type = -1;
    private int block_state = 0;
    private String color = "red";
    private boolean for_preview = false;
}

/*
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