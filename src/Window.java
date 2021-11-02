import java.awt.Dimension;
import javax.swing.*;

public class Window extends JFrame {
    public Window(int width, int height, String title, Game game, GameRemake gameRemake, boolean x) {
        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        if(!x)
        frame.add(game);
        if(x)
        frame.add(gameRemake);
        frame.setVisible(true);
        if(!x)
        game.start();
        if(x)
        gameRemake.start();
    }   
}