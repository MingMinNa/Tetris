package blocks;

public class Cell {
    public static final int BLOCK_WIDTH = 30, BLOCK_HEIGHT = 30;
    
    public Cell(int index_x, int index_y,String color){
        block_x_index = index_x;
        block_y_index = index_y;
        this.color = color;
    }

    public String getColor(){ return this.color;}
    public int getX(){return block_x_index;}
    public int getY(){return block_y_index;}

    public void setColor(String color){this.color = color;}
    // ------------------------------------
    private int block_x_index; // 0 ~ 9 (10)
    private int block_y_index; // 0 ~ 22(23)
    private String color;
}
