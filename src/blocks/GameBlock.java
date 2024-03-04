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
    public GameBlock(Cell [] dominated_cells, int type, int color_idx)
    {
        this(type,color_idx);
        for_preview = false;
        setCurrentCells(dominated_cells);
    }
    // for preview type 
    public GameBlock(int type, int color_idx){
        for_preview = true;
        setColor(color_idx);
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
    public void setBlockType(int idx){block_type = idx;}

    public Cell[] getCurrentCells(){return current_cells;}
    public void setCurrentCells(Cell [] next_cells){
        for(int i = 0;i < 4;i ++){
            current_cells[i] = next_cells[i]; 
        }
    }
    
    public boolean getForPreview(){return for_preview;}
    
    // -----------------------------
    private Cell [] current_cells = new Cell[4];
    private int block_type = -1;
    private String color = "red";
    private boolean for_preview = false;
}
