import java.awt.Color;
import java.awt.Graphics;

public class BLACKCHECKER extends GameObject{
    
    public BLACKCHECKER(int x, int y, ID id){
        super(x, y, id);
    }

    public void tick(){

    }

    public void render(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(x, y, 50, 50);
    }
}
