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
        WIDTH = 50 * line;
        HEIGHT = 50 * line;
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
        while (clicks != -1) {
            while (clicks == mouse.getClicks()) {
                out.print("");
            }
            clicks = mouse.getClicks();
            objectLocation = mouse.getObjectLoc();
            if(checkers[objectLocation + 8] != 0)
                getPossibleMoves(objectLocation);
            if(possibleMoves[objectLocation + 8] != 0){
                switch(possibleMoves[objectLocation + 8]){
                    case 1:
                    
                    break;
                    case 2:
                    break;
                    case 3:
                    break;
                    case 4:
                    break;
                    case 5:
                    break;
                    case 6:
                    break;
                    case 7:
                    break;
                    case 8:
                    break;
                }
            }
        }
    }

    // checker == 3 || 9 == black
    // checker == 2 || 4 == red
    public void getPossibleMoves(int objectLocation) {
        for (int i = 0; i != 100; i++) {
            if (possibleMoves[i] != 0) {
                int x = (i - 8) % 8;
                x *= 50;
                int y = (i - 8) / 8;
                y *= 50; 
                handler.replaceObject((i - 8) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                possibleMoves[i] = 0;
            }
        }
        int checkerType = checkers[objectLocation + 8];
        int checkerLevel = 0;
        if (checkerType > 3)
            checkerLevel = 1;
        // Top Left || Possible Move: 1
        if (objectLocation % 8 != 0 && objectLocation - 9 > -1 && (checkerType == 2 || checkerLevel == 1)) {
            if ((checkerType % 2 == 0 || checkerLevel == 1) && checkers[objectLocation - 1] == 0) {
                possibleMoves[objectLocation - 1] = 1;
            }
        }
        // Top Right || Possible Move: 2
        if ((objectLocation + 1) % 8 != 0 && objectLocation - 7 > -1 && (checkerType == 2 || checkerLevel == 1)) {
            if ((checkerType % 2 == 0 || checkerLevel == 1) && checkers[objectLocation + 1] == 0) {
                possibleMoves[objectLocation + 1] = 2;
            }
        }
        // Bottom Left || Possible Move: 3
        if (objectLocation % 8 != 0 && objectLocation + 7 < 64 && (checkerType == 3 || checkerLevel == 1)) {
            if ((checkerType % 3 == 0 || checkerLevel == 1) && checkers[objectLocation + 15] == 0) {
                possibleMoves[objectLocation + 15] = 3;
            }
        }
        // Bottom Right || Possible Move: 4
        if ((objectLocation + 1) % 8 != 0 && objectLocation + 9 < 64 && (checkerType == 3 || checkerLevel == 1)) {
            if ((checkerType % 3 == 0 || checkerLevel == 1) && checkers[objectLocation + 17] == 0) {
                possibleMoves[objectLocation + 17] = 4;
            }
        }
        int[] tempCheckers = new int[100];
        int[] tempCheckers2 = new int[400];
        int a = 1, b = 1;
        tempCheckers[a] = objectLocation + 8;
        tempCheckers2[b] = objectLocation + 8;
        while (a != 100) {
            // Top Left || Possible Move: 5
            if (tempCheckers[a] % 8 != 0 && (tempCheckers[a] - 9) % 8 != 0 && tempCheckers[a] - 26 > -1
                    && (checkerType == 2 || checkerLevel == 1)) {
                if (checkerType % 2 == 0 && checkers[tempCheckers[a] - 9] % 3 == 0
                        && checkers[tempCheckers[a] - 18] == 0 && checkers[tempCheckers[a] - 9] != 0) {
                    possibleMoves[tempCheckers[a] - 18] = 5;
                    b++;
                    tempCheckers2[b] = tempCheckers[a] - 18;
                }
                if (checkerType % 9 == 0 && checkers[tempCheckers[a] - 1] % 2 == 0
                        && checkers[tempCheckers[a] - 18] == 0 && checkers[tempCheckers[a] - 9] != 0) {
                    possibleMoves[tempCheckers[a] - 18] = 5;
                    b++;
                    tempCheckers2[b] = tempCheckers[a] - 18;
                }
            }
            // Top Right || Possible Move: 6
            if ((tempCheckers[a] + 1) % 8 != 0 && (tempCheckers[a] - 6) % 8 != 0 && tempCheckers[a] - 22 > -1
                    && (checkerType == 2 || checkerLevel == 1)) {
                if (checkerType % 2 == 0 && checkers[tempCheckers[a] - 7] % 3 == 0
                        && checkers[tempCheckers[a] - 14] == 0 && checkers[tempCheckers[a] - 7] != 0) {
                    possibleMoves[tempCheckers[a] - 14] = 6;
                    b++;
                    tempCheckers2[b] = tempCheckers[a] - 14;
                }
                if (checkerType % 9 == 0 && checkers[tempCheckers[a] - 7] % 2 == 0
                        && checkers[tempCheckers[a] - 14] == 0 && checkers[tempCheckers[a] - 7] != 0) {
                    possibleMoves[tempCheckers[a] - 14] = 6;
                    b++;
                    tempCheckers2[b] = tempCheckers[a] - 14;
                }
            }
            // Bottom Left || Possible Move: 7
            if (tempCheckers[a] % 8 != 0 && (tempCheckers[a] + 7) % 8 != 0 && tempCheckers[a] + 6 < 64
                    && (checkerType == 3 || checkerLevel == 1)) {
                if (checkerType % 3 == 0 && checkers[tempCheckers[a] + 7] % 2 == 0
                        && checkers[tempCheckers[a] + 14] == 0 && checkers[tempCheckers[a] + 7] != 0) {
                    possibleMoves[tempCheckers[a] + 14] = 7;
                    b++;
                    tempCheckers2[b] = tempCheckers[a] + 14;
                }
                if (checkerType % 4 == 0 && checkers[tempCheckers[a] + 7] % 3 == 0
                        && checkers[tempCheckers[a] + 14] == 0 && checkers[tempCheckers[a] + 7] != 0) {
                    possibleMoves[tempCheckers[a] + 14] = 7;
                    b++;
                    tempCheckers2[b] = tempCheckers[a] + 14;
                }
            }
            // Bottom Right || Possible Move: 8
            if ((tempCheckers[a] + 1) % 8 != 0 && (tempCheckers[a] + 10) % 8 != 0 && tempCheckers[a] + 10 < 64
                    && (checkerType == 2 || checkerLevel == 1)) {
                if (checkerType % 3 == 0 && checkers[tempCheckers[a] + 9] % 2 == 0
                        && checkers[tempCheckers[a] + 18] == 0 && checkers[tempCheckers[a] + 9] != 0) {
                    possibleMoves[tempCheckers[a] + 18] = 8;
                    b++;
                    tempCheckers2[b] = tempCheckers[a] + 18;
                }
                if (checkerType % 4 == 0 && checkers[tempCheckers[a] + 9] % 3 == 0
                        && checkers[tempCheckers[a] + 18] == 0 && checkers[tempCheckers[a] + 9] != 0) {
                    possibleMoves[tempCheckers[a] + 18] = 8;
                    b++;
                    tempCheckers2[b] = tempCheckers[a] + 18;
                }
            }
            a++;
            if (a != 100)
                tempCheckers[a] = tempCheckers2[a];
        }
        for (int i = 0; i != 100; i++) {
            if (possibleMoves[i] != 0) {
                int x = (i - 8) % 8;
                x *= 50;
                int y = (i - 8) / 8;
                y *= 50; 
                handler.replaceObject((i - 8) * 3, new POSSIBLEMOVE(x, y, ID.POSSIBLEMOVE));
            }
        }
    }
}
