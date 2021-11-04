import java.awt.event.*;
import javax.swing.event.MouseInputListener;

public class Mouse implements MouseInputListener {
    private int clicks;
    private int objectLocation;
    private int x;
    private int y;

    public int getClicks() {
        return clicks;
    }

    public int getObjectLoc() {
        return objectLocation;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void mouseClicked(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        objectLocation = ((y / 50) * 8) + (x / 50);
        clicks++;
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent arg0) {

    }

    public void mouseMoved(MouseEvent arg0) {

    }

}
