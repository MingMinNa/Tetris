package State;
import javax.swing.JLabel;
import java.awt.*;

public interface ScreenPanel {
    default JLabel labelMake(int center_x, int center_y, String words, int words_width, int words_height){
        return labelMake(center_x, center_y, words, words_width, words_height, 20);
    }
    default JLabel labelMake(int center_x, int center_y, String words, int words_width, int words_height, int font_size){
        JLabel label = new JLabel(words);
        label.setBounds(center_x - words_width/2 , center_y - words_height/2, words_width, words_height);
        
        label.setFont(new Font("Arial", Font.BOLD, font_size));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        return label;
    }
} 
