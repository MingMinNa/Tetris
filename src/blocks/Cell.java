package blocks;

public class Cell {
    public static final int BLOCK_WIDTH = 30; 
    public static final int BLOCK_HEIGHT = 30;
    public Cell(){}
    public Cell(int index_x, int index_y,String color){
        block_x_index = index_x;
        block_y_index = index_y;
        this.color = color;
    }

    public String getColor(){ return this.color;}
    public void setColor(String color){   this.color = color;}
    public int getX(){return block_x_index;}
    public int getY(){return block_y_index;}
    // ------------------------------------
    private int block_x_index;
    private int block_y_index;
    private String color;
}
