import java.awt.Color;
import java.awt.Graphics;

public class BLACKKING extends GameObject{
    
    public BLACKKING(int x, int y, ID id){
        super(x, y, id);
    }

    public void tick(){

    }

    public void render(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect(x, y, 50, 50);
    }
}
