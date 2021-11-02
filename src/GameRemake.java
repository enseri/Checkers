import static java.lang.System.out;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferStrategy;

import java.lang.Math;

public class GameRemake extends Canvas implements Runnable {
    private int line = 0;
    private int WIDTH = 0;
    private int HEIGHT = 0;
    private int size = 64;
    private int checkers[] = new int[100];
    private int possibleMoves[] = new int[100];
    private Thread thread;
    private String[] tiles = new String[100];
    private boolean running = false;
    private Handler handler;
    private Mouse mouse = new Mouse();

    public GameRemake() {
        for (int i = 0; i != 100; i++) {
            checkers[i] = 0;
            possibleMoves[i] = 0;
            tiles[i] = null;
        }
        while (line != Math.sqrt(size)) {
            line++;
        }
        WIDTH = 52 * line;
        HEIGHT = 54 * line;
        new Window(WIDTH, HEIGHT, "TICTACTOE", null, this, true);

        handler = new Handler();

        this.addMouseListener(mouse);

        int f = 0;
        int d = 0;
        int g = 1;
        int h = 0;
        int v = 0;
        String lastTile = "GRAYTILE";
        int x = 0;
        while (d != size) {
            x = 0;
            if (f % 2 == 0 && h <= 2) {
                if (h == 1)
                    checkers[(f + (h * 8)) + 8] = 3;
                if (h != 1)
                    checkers[((f + (h * 8)) + 1) + 8] = 3;
            }
            if (f % 2 == 0 && h >= 5) {
                if (h == 6)
                    checkers[((f + (h * 8)) + 1) + 8] = 2;
                if (h != 6)
                    checkers[(f + (h * 8)) + 8] = 2;
            }
            if (lastTile.equals("WHITETILE") && x == 0 && checkers[(f + (h * 8)) + 8] == 0) {
                handler.addObject(new GRAYTILE(0 + (f * 50), 0 + (h * 50), ID.GRAYTILE));
                lastTile = "GRAYTILE";
                x++;
            }
            if (lastTile.equals("GRAYTILE") && x == 0 && checkers[(f + (h * 8)) + 8] == 0) {
                handler.addObject(new WHITETILE(0 + (f * 50), 0 + (h * 50), ID.WHITETILE));
                lastTile = "WHITETILE";
                x++;
            }
            if (checkers[(f + (h * 8)) + 8] == 3)
                handler.addObject(new BLACKCHECKER(0 + (f * 50), 0 + (h * 50), ID.BLACKCHECKER));
            if (checkers[(f + (h * 8)) + 8] == 2)
                handler.addObject(new REDCHECKER(0 + (f * 50), 0 + (h * 50), ID.REDCHECKER));
            if (checkers[(f + (h * 8)) + 8] != 0) {
                if (lastTile.equals("GRAYTILE") && x == 0) {
                    lastTile = "WHITETILE";
                    x++;
                }
                if (lastTile.equals("WHITETILE") && x == 0) {
                    lastTile = "GRAYTILE";
                    x++;
                }
            }
            if (lastTile.equals("GRAYTILE")) {
                tiles[v] = "GRAYTILE";
                v++;
            }
            if (lastTile.equals("WHITETILE")) {
                tiles[v] = "WHITETILE";
                v++;
            }
            handler.addObject(new COLUMN(0 + (f * 50), 0 + (h * 50), ID.COLUMN));
            handler.addObject(new ROW(0 + (f * 50), 0 + (h * 50), ID.ROW));
            f++;
            d++;
            g++;
            if (g == line + 1) {
                g = 1;
                f = 0;
                h++;
                if (lastTile.equals("GRAYTILE") && x == 1) {
                    lastTile = "WHITETILE";
                    x++;
                }
                if (lastTile.equals("WHITETILE") && x == 1) {
                    lastTile = "GRAYTILE";
                    x++;
                }
            }
        }
        begin();
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            if (running)
                render();
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }
        stop();
    }

    public void tick() {
        handler.tick();
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.yellow);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        handler.render(g);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) throws Exception {
        new GameRemake();
    }

    public void begin() {
        int clicks = 0;
        int objectLocation = 0;
        boolean clickedOnProperChecker = false;
        while(clicks == mouse.getClicks()){
            out.print("");
        }
        clicks = mouse.getClicks();
        objectLocation = mouse.getObjectLoc();
        if(checkers[objectLocation + 8] != 0){
            clickedOnProperChecker = true;
        }
        if(clickedOnProperChecker){
            getPossibleMoves(objectLocation);
        }
    }
    // checker == 3 || 6 == black
    // checker == 2 || 4 == red
    public void getPossibleMoves(int objectLocation){
        int tempChecker = 0;
        int checkerType = checkers[objectLocation + 8];
        int checkerLevel = 0;
        if(checkerType > 3)
            checkerLevel = 1;
        // Top Left
        if(checkerType == 2 || checkerLevel == 3){
            if(checkerType % 2 == 0 && checkers[objectLocation - 1] % 3 == 0){
                
            }
        }
    }
}
