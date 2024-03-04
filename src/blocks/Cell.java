package blocks;

public class Cell {
    public static final int block_width = 30; 
    public static final int block_height = 30;
    public Cell(){}
    public Cell(int index_x, int index_y,String color){
        block_x_index = index_x;
        block_y_index = index_y;
        this.color = color;
    }

    public String getColor(){ return this.color;}
    // ------------------------------------
    private int block_x_index;
    private int block_y_index;
    private String color;
}
