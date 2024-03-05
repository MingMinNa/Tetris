package blocks;
import blocks.Cell;
import gameState.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.swing.*;
import gameState.*;

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
    
    // -----------------------------
    private int block_type = -1;
    private String color = "red";
    private boolean for_preview = false;
}
