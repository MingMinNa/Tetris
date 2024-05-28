package blocks;

public class Cell {
    public static final int BLOCK_WIDTH = 30, BLOCK_HEIGHT = 30;
    
    public Cell(int index_x, int index_y,String color){
        block_x_index = index_x;
        block_y_index = index_y;
        this.color = color;
    }
    public Cell(Cell copied_cell){
        block_x_index = copied_cell.getX();
        block_y_index = copied_cell.getY();
        this.color = copied_cell.getColor();
    }

    public String getColor(){ return this.color;}
    public int getX(){return block_x_index;}
    public int getY(){return block_y_index;}

    public void setX(int next_x){if(next_x >= 0 && next_x <= 9) block_x_index = next_x;}
    public void setY(int next_y){if(next_y >= 0 && next_y <= 22) block_y_index = next_y;}
    public void setColor(String color){this.color = color;}

    // ------------------------------------
    private int block_x_index; // 0 ~ 9 (10)
    private int block_y_index; // 0 ~ 22(23)
    private String color;
}
