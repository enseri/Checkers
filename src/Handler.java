
import java.util.LinkedList;
import java.awt.Graphics;

public class Handler {
    LinkedList<GameObject> object = new LinkedList<GameObject>();
    private GameObject removedObject;

    public void tick() {
        int i = 0;
        boolean error = false;
        while (i < object.size()) {
            do {
                try {
                    GameObject tempObject = object.get(i);
                    tempObject.tick();
                    error = false;
                } catch (NullPointerException | IndexOutOfBoundsException e) {
                    error = true;
                }
            } while (error);
            i++;
        }
    }

    public void render(Graphics g) {
        int i = 0;
        boolean error = false;
        while (i < object.size()) {
            do {
                try {
                    error = false;
                    GameObject tempObject = object.get(i);
                    tempObject.render(g);
                } catch (NullPointerException | IndexOutOfBoundsException e) {
                    error = true;
                }
            } while (error);
            i++;
        }
    }

    public void addObject(GameObject object) {
        this.object.add(object);
    }

    public void replaceObject(int objectLocation, GameObject object) {
        boolean error = false;
        do {
            try {
                error = false;
                removedObject = this.object.get(objectLocation);
            } catch (IndexOutOfBoundsException e) {
                error = true;
            }
        } while (error);
        this.object.remove(objectLocation);
        this.object.add(objectLocation, object);
    }

    public GameObject getRemovedObject() {
        return removedObject;
    }

    public void reset() {
        for (int i = object.size(); i != 0; i--) {
            object.remove();
        }
    }
}
