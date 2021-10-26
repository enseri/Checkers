import java.awt.Color;
import java.awt.Graphics;

public class POSSIBLEMOVE extends GameObject {

    public POSSIBLEMOVE(int x, int y, ID id) {
        super(x, y, id);
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.setColor(Color.green);
        g.fillRect(x, y, 50, 50);
    }
}
