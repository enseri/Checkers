import java.awt.Color;
import java.awt.Graphics;

public class GRAYTILE extends GameObject{
    
    public GRAYTILE(int x, int y, ID id){
        super(x, y, id);
    }

    public void tick(){

    }

    public void render(Graphics g) {
        g.setColor(Color.lightGray);
        g.fillRect(x, y, 50, 50);
    }
}
