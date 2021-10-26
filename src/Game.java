import static java.lang.System.out;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferStrategy;

import java.lang.Math;

public class Game extends Canvas implements Runnable {
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

    public Game() {
        while (line != Math.sqrt(size)) {
            line++;
        }
        WIDTH = 50 * line;
        HEIGHT = 50 * line;
        new Window(WIDTH, HEIGHT, "TICTACTOE", this);

        handler = new Handler();

        this.addMouseListener(mouse);

        int f = 0;
        int d = 0;
        int g = 1;
        int h = 0;
        int v = 0;
        String lastTile = "GRAYTILE";
        int x = 0;
        while (d != (size + 1)) {
            x = 0;
            if (f % 2 == 0 && h <= 2) {
                if (h == 1)
                    checkers[(f + (h * 8)) + 8] = 1;
                if (h != 1)
                    checkers[((f + (h * 8)) + 1) + 8] = 1;
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
            if (checkers[(f + (h * 8)) + 8] == 1)
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
        new Game();
    }

    public void begin() {
        int clicks = 0;
        int x = 0;
        int y = 0;
        int x1 = 0;
        GameObject[] removedObjectType = new GameObject[100];
        int objectLocation = 0;
        boolean validMove = false;
        boolean clickedOnProperChecker = false;
        int[] removedObjectsLocations = new int[100];
        boolean runningGame = true;
        int player = 2;
        while (runningGame) {
            validMove = false;
            clickedOnProperChecker = false;
            while (clicks == mouse.getClicks()) {
                out.print("");
            }
            if (player == 3) {
                player = 1;
            }
            x1 = 0;
            for (int i = 0; i != 100; i++) {
                if (removedObjectsLocations[i] != 0) {
                    handler.replaceObject(removedObjectsLocations[i], removedObjectType[i]);
                    removedObjectType[i] = null;
                    removedObjectsLocations[i] = 0;
                }
            }
            clicks = mouse.getClicks();
            objectLocation = mouse.getObjectLoc();
            if (checkers[objectLocation + 8] != 0
                    && (checkers[objectLocation + 8] == 1 || checkers[objectLocation + 8] == 3) && player == 1)
                clickedOnProperChecker = true;
            if (checkers[objectLocation + 8] != 0
                    && (checkers[objectLocation + 8] == 2 || checkers[objectLocation + 8] == 4) && player == 2)
                clickedOnProperChecker = true;
            if (((checkers[objectLocation + 8] == 1 || checkers[objectLocation + 8] == 3) && player == 1)
                    || possibleMoves[objectLocation + 8] != 0 && player == 1)
                validMove = true;
            if (((checkers[objectLocation + 8] == 2 || checkers[objectLocation + 8] == 4) && player == 2)
                    || possibleMoves[objectLocation + 8] != 0 && player == 2)
                validMove = true;
            if (possibleMoves[objectLocation + 8] != 0 && validMove) {
                x = (mouse.getX() / 50) * 50;
                y = (mouse.getY() / 50) * 50;
                //
                if (possibleMoves[objectLocation + 8] == 4) {
                    // if (objectLocation > 9 && checkers[objectLocation + 8] == 0)
                    // checkers[objectLocation + 8] = 2;
                    if (objectLocation < 10 && checkers[(objectLocation + 8) + 9] == 2) {
                        checkers[objectLocation + 8] = 4;
                    } else {
                        checkers[objectLocation + 8] = checkers[(objectLocation + 8) + 9];
                    }
                    checkers[(objectLocation + 8) + 9] = 0;
                    x += 50;
                    y += 50;
                    if (tiles[objectLocation + 9].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation + 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                //
                if (possibleMoves[objectLocation + 8] == 3) {
                    // if (objectLocation > 9 && checkers[objectLocation + 8] == 2)
                    // checkers[objectLocation + 8] = 2;
                    if (objectLocation < 10 && checkers[(objectLocation + 8) + 7] == 2) {
                        checkers[objectLocation + 8] = 4;
                    } else {
                        checkers[objectLocation + 8] = checkers[(objectLocation + 8) + 7];
                    }
                    checkers[(objectLocation + 8) + 7] = 0;
                    x -= 50;
                    y += 50;
                    if (tiles[objectLocation + 7].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation + 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                //
                if (possibleMoves[objectLocation + 8] == 1) {
                    // if (objectLocation < 56)
                    // checkers[objectLocation + 8] = 1;
                    if (objectLocation > 55 && checkers[(objectLocation + 8) - 9] == 1) {
                        checkers[objectLocation + 8] = 3;
                    } else {
                        checkers[objectLocation + 8] = checkers[(objectLocation + 8) - 9];
                    }

                    checkers[(objectLocation + 8) - 9] = 0;
                    x -= 50;
                    y -= 50;
                    if (tiles[objectLocation - 9].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation - 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                //
                if (possibleMoves[objectLocation + 8] == 2) {
                    // if (objectLocation < 56)
                    // checkers[objectLocation + 8] = 1;
                    if (objectLocation > 55 && checkers[(objectLocation + 8) - 7] == 1) {
                        checkers[objectLocation + 8] = 3;
                    } else {
                        checkers[objectLocation + 8] = checkers[(objectLocation + 8) - 7];
                    }

                    checkers[(objectLocation + 8) - 7] = 0;
                    x += 50;
                    y -= 50;
                    if (tiles[objectLocation - 7].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation - 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                //
                if (possibleMoves[objectLocation + 8] == 8) {
                    // if (objectLocation > 9)
                    // checkers[objectLocation + 8] = 2;
                    if (objectLocation < 10 && checkers[((objectLocation + 8) + 9) + 9] == 2) {
                        checkers[objectLocation + 8] = 4;
                    } else {
                        checkers[objectLocation + 8] = checkers[((objectLocation + 8) + 9) + 9];
                    }

                    checkers[((objectLocation + 8) + 9) + 9] = 0;
                    checkers[(objectLocation + 8) + 9] = 0;
                    x += 50;
                    y += 50;
                    if (tiles[objectLocation + 9].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation + 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y += 50;
                    if (tiles[(objectLocation + 9) + 9].equals("GRAYTILE")) {
                        handler.replaceObject(((objectLocation + 9) + 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                ////
                if (possibleMoves[objectLocation + 8] == 18) {
                    // if (objectLocation > 9)
                    // checkers[objectLocation + 8] = 2;
                    if (objectLocation < 10 && checkers[((((objectLocation + 8) + 9) + 9) + 9) + 9] == 2) {
                        checkers[((objectLocation + 9) + 9) + 8] = 4;
                    } else {
                        checkers[((objectLocation + 9) + 9) + 8] = checkers[((((objectLocation + 8) + 9) + 9) + 9) + 9];
                    }

                    checkers[((((objectLocation + 8) + 9) + 9) + 9) + 9] = 0;
                    checkers[(((objectLocation + 8) + 9) + 9) + 9] = 0;
                    //
                    if (objectLocation < 10 && checkers[((objectLocation + 8) + 9) + 9] == 2) {
                        checkers[objectLocation + 8] = 4;
                    } else {
                        checkers[objectLocation + 8] = checkers[((objectLocation + 8) + 9) + 9];
                    }

                    checkers[((objectLocation + 8) + 9) + 9] = 0;
                    checkers[(objectLocation + 8) + 9] = 0;
                    x += 50;
                    y += 50;
                    if (tiles[objectLocation + 9].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation + 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y += 50;
                    if (tiles[(objectLocation + 9) + 9].equals("GRAYTILE")) {
                        handler.replaceObject(((objectLocation + 9) + 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y += 50;
                    if (tiles[((objectLocation + 9) + 9) + 9].equals("GRAYTILE")) {
                        handler.replaceObject((((objectLocation + 9) + 9) + 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y += 50;
                    if (tiles[(((objectLocation + 9) + 9) + 9) + 9].equals("GRAYTILE")) {
                        handler.replaceObject(((((objectLocation + 9) + 9) + 9) + 9) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                //////
                if (possibleMoves[objectLocation + 8] == 28) {
                    // if (objectLocation > 9)
                    // checkers[objectLocation + 8] = 2;
                    if (objectLocation < 10 && checkers[((((((objectLocation + 8) + 9) + 9) + 9) + 9) + 9) + 9] == 2) {
                        checkers[((((objectLocation + 9) + 9) + 9) + 9) + 8] = 4;
                    } else {
                        checkers[((((objectLocation + 9) + 9) + 9) + 9)
                                + 8] = checkers[((((((objectLocation + 8) + 9) + 9) + 9) + 9) + 9) + 9];
                    }

                    checkers[((((((objectLocation + 8) + 9) + 9) + 9) + 9) + 9) + 9] = 0;
                    checkers[(((((objectLocation + 8) + 9) + 9) + 9) + 9) + 9] = 0;

                    //
                    if (objectLocation < 10 && checkers[((((objectLocation + 8) + 9) + 9) + 9) + 9] == 2) {
                        checkers[((objectLocation + 9) + 9) + 8] = 4;
                    } else {
                        checkers[((objectLocation + 9) + 9) + 8] = checkers[((((objectLocation + 8) + 9) + 9) + 9) + 9];
                    }

                    checkers[((((objectLocation + 8) + 9) + 9) + 9) + 9] = 0;
                    checkers[(((objectLocation + 8) + 9) + 9) + 9] = 0;
                    //
                    if (objectLocation < 10 && checkers[((objectLocation + 8) + 9) + 9] == 2) {
                        checkers[objectLocation + 8] = 4;
                    } else {
                        checkers[objectLocation + 8] = checkers[((objectLocation + 8) + 9) + 9];
                    }

                    checkers[((objectLocation + 8) + 9) + 9] = 0;
                    checkers[(objectLocation + 8) + 9] = 0;
                    x += 50;
                    y += 50;
                    if (tiles[objectLocation + 9].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation + 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y += 50;
                    if (tiles[(objectLocation + 9) + 9].equals("GRAYTILE")) {
                        handler.replaceObject(((objectLocation + 9) + 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y += 50;
                    if (tiles[((objectLocation + 9) + 9) + 9].equals("GRAYTILE")) {
                        handler.replaceObject((((objectLocation + 9) + 9) + 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y += 50;
                    if (tiles[(((objectLocation + 9) + 9) + 9) + 9].equals("GRAYTILE")) {
                        handler.replaceObject(((((objectLocation + 9) + 9) + 9) + 9) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y += 50;
                    if (tiles[((((objectLocation + 9) + 9) + 9) + 9) + 9].equals("GRAYTILE")) {
                        handler.replaceObject((((((objectLocation + 9) + 9) + 9) + 9) + 9) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y += 50;
                    if (tiles[(((((objectLocation + 9) + 9) + 9) + 9) + 9) + 9].equals("GRAYTILE")) {
                        handler.replaceObject(((((((objectLocation + 9) + 9) + 9) + 9) + 9) + 9) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                //
                if (possibleMoves[objectLocation + 8] == 7) {
                    // if (objectLocation > 9)
                    // checkers[objectLocation + 8] = 2;
                    if (objectLocation < 10 && checkers[((objectLocation + 8) + 7) + 7] == 2) {
                        checkers[objectLocation + 8] = 4;
                    } else {
                        checkers[objectLocation + 8] = checkers[((objectLocation + 8) + 7) + 7];
                    }

                    checkers[((objectLocation + 8) + 7) + 7] = 0;
                    checkers[(objectLocation + 8) + 7] = 0;
                    x -= 50;
                    y += 50;
                    if (tiles[objectLocation + 7].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation + 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y += 50;
                    if (tiles[(objectLocation + 7) + 7].equals("GRAYTILE")) {
                        handler.replaceObject(((objectLocation + 7) + 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                ////
                if (possibleMoves[objectLocation + 8] == 17) {
                    // if (objectLocation > 9)
                    // checkers[objectLocation + 8] = 2;
                    if (objectLocation < 10 && checkers[((((objectLocation + 8) + 7) + 7) + 7) + 7] == 2) {
                        checkers[((objectLocation + 7) + 7) + 8] = 4;
                    } else {
                        checkers[((objectLocation + 7) + 7) + 8] = checkers[((((objectLocation + 8) + 7) + 7) + 7) + 7];
                    }

                    checkers[((((objectLocation + 8) + 7) + 7) + 7) + 7] = 0;
                    checkers[(((objectLocation + 8) + 7) + 7) + 7] = 0;
                    if (objectLocation < 10 && checkers[((objectLocation + 8) + 7) + 7] == 2) {
                        checkers[objectLocation + 8] = 4;
                    } else {
                        checkers[objectLocation + 8] = checkers[((objectLocation + 8) + 7) + 7];
                    }

                    checkers[((objectLocation + 8) + 7) + 7] = 0;
                    checkers[(objectLocation + 8) + 7] = 0;
                    x -= 50;
                    y += 50;
                    if (tiles[objectLocation + 7].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation + 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y += 50;
                    if (tiles[(objectLocation + 7) + 7].equals("GRAYTILE")) {
                        handler.replaceObject(((objectLocation + 7) + 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y += 50;
                    if (tiles[((objectLocation + 7) + 7) + 7].equals("GRAYTILE")) {
                        handler.replaceObject((((objectLocation + 7) + 7) + 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y += 50;
                    if (tiles[(((objectLocation + 7) + 7) + 7) + 7].equals("GRAYTILE")) {
                        handler.replaceObject(((((objectLocation + 7) + 7) + 7) + 7) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                //////
                if (possibleMoves[objectLocation + 8] == 27) {
                    // if (objectLocation > 9)
                    // checkers[objectLocation + 8] = 2;
                    if (objectLocation < 10 && checkers[((((((objectLocation + 8) + 7) + 7) + 7) + 7) + 7) + 7] == 2) {
                        checkers[((((objectLocation + 7) + 7) + 7) + 7) + 8] = 4;
                    } else {
                        checkers[((((objectLocation + 7) + 7) + 7) + 7)
                                + 8] = checkers[((((((objectLocation + 8) + 7) + 7) + 7) + 7) + 7) + 7];
                    }

                    checkers[((((((objectLocation + 8) + 7) + 7) + 7) + 7) + 7) + 7] = 0;
                    checkers[(((((objectLocation + 8) + 7) + 7) + 7) + 7) + 7] = 0;
                    if (objectLocation < 10 && checkers[((((objectLocation + 8) + 7) + 7) + 7) + 7] == 2) {
                        checkers[((objectLocation + 7) + 7) + 8] = 4;
                    } else {
                        checkers[((objectLocation + 7) + 7) + 8] = checkers[((((objectLocation + 8) + 7) + 7) + 7) + 7];
                    }

                    checkers[((((objectLocation + 8) + 7) + 7) + 7) + 7] = 0;
                    checkers[(((objectLocation + 8) + 7) + 7) + 7] = 0;
                    if (objectLocation < 10 && checkers[((objectLocation + 8) + 7) + 7] == 2) {
                        checkers[objectLocation + 8] = 4;
                    } else {
                        checkers[objectLocation + 8] = checkers[((objectLocation + 8) + 7) + 7];
                    }

                    checkers[((objectLocation + 8) + 7) + 7] = 0;
                    checkers[(objectLocation + 8) + 7] = 0;
                    x -= 50;
                    y += 50;
                    if (tiles[objectLocation + 7].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation + 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y += 50;
                    if (tiles[(objectLocation + 7) + 7].equals("GRAYTILE")) {
                        handler.replaceObject(((objectLocation + 7) + 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y += 50;
                    if (tiles[((objectLocation + 7) + 7) + 7].equals("GRAYTILE")) {
                        handler.replaceObject((((objectLocation + 7) + 7) + 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y += 50;
                    if (tiles[(((objectLocation + 7) + 7) + 7) + 7].equals("GRAYTILE")) {
                        handler.replaceObject(((((objectLocation + 7) + 7) + 7) + 7) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y += 50;
                    if (tiles[((((objectLocation + 7) + 7) + 7) + 7) + 7].equals("GRAYTILE")) {
                        handler.replaceObject((((((objectLocation + 7) + 7) + 7) + 7) + 7) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y += 50;
                    if (tiles[(((((objectLocation + 7) + 7) + 7) + 7) + 7) + 7].equals("GRAYTILE")) {
                        handler.replaceObject(((((((objectLocation + 7) + 7) + 7) + 7) + 7) + 7) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                //
                if (possibleMoves[objectLocation + 8] == 5) {
                    // if (objectLocation < 56)
                    // checkers[objectLocation + 8] = 1;
                    if (objectLocation > 55 && checkers[((objectLocation + 8) - 9) - 9] == 1) {
                        checkers[objectLocation + 8] = 3;
                    } else {
                        checkers[objectLocation + 8] = checkers[((objectLocation + 8) - 9) - 9];
                    }

                    checkers[((objectLocation + 8) - 9) - 9] = 0;
                    checkers[(objectLocation + 8) - 9] = 0;
                    x -= 50;
                    y -= 50;
                    if (tiles[objectLocation - 9].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation - 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y -= 50;
                    if (tiles[(objectLocation - 9) - 9].equals("GRAYTILE")) {
                        handler.replaceObject(((objectLocation - 9) - 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                ////
                if (possibleMoves[objectLocation + 8] == 15) {
                    // if (objectLocation < 56)
                    // checkers[objectLocation + 8] = 1;
                    if (objectLocation > 55 && checkers[((((objectLocation + 8) - 9) - 9) - 9) - 9] == 1) {
                        checkers[((objectLocation - 9) - 9) + 8] = 3;
                    } else {
                        checkers[((objectLocation - 9) - 9) + 8] = checkers[((((objectLocation + 8) - 9) - 9) - 9) - 9];
                    }

                    checkers[((((objectLocation + 8) - 9) - 9) - 9) - 9] = 0;
                    checkers[(((objectLocation + 8) - 9) - 9) - 9] = 0;
                    if (objectLocation > 55 && checkers[((objectLocation + 8) - 9) - 9] == 1) {
                        checkers[objectLocation + 8] = 3;
                    } else {
                        checkers[objectLocation + 8] = checkers[((objectLocation + 8) - 9) - 9];
                    }

                    checkers[((objectLocation + 8) - 9) - 9] = 0;
                    checkers[(objectLocation + 8) - 9] = 0;
                    x -= 50;
                    y -= 50;
                    if (tiles[objectLocation - 9].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation - 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y -= 50;
                    if (tiles[(objectLocation - 9) - 9].equals("GRAYTILE")) {
                        handler.replaceObject(((objectLocation - 9) - 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y -= 50;
                    if (tiles[((objectLocation - 9) - 9) - 9].equals("GRAYTILE")) {
                        handler.replaceObject((((objectLocation - 9) - 9) - 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y -= 50;
                    if (tiles[(((objectLocation - 9) - 9) - 9) - 9].equals("GRAYTILE")) {
                        handler.replaceObject(((((objectLocation - 9) - 9) - 9) - 9) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                //////
                if (possibleMoves[objectLocation + 8] == 25) {
                    // if (objectLocation < 56)
                    // checkers[objectLocation + 8] = 1;
                    if (objectLocation > 55 && checkers[((((((objectLocation + 8) - 9) - 9) - 9) - 9) - 9) - 9] == 1) {
                        checkers[((((objectLocation - 9) - 9) - 9) - 9) + 8] = 3;
                    } else {
                        checkers[((((objectLocation - 9) - 9) - 9) - 9)
                                + 8] = checkers[((((((objectLocation + 8) - 9) - 9) - 9) - 9) - 9) - 9];
                    }

                    checkers[((((((objectLocation + 8) - 9) - 9) - 9) - 9) - 9) - 9] = 0;
                    checkers[(((((objectLocation + 8) - 9) - 9) - 9) - 9) - 9] = 0;
                    if (objectLocation > 55 && checkers[((((objectLocation + 8) - 9) - 9) - 9) - 9] == 1) {
                        checkers[((objectLocation - 9) - 9) + 8] = 3;
                    } else {
                        checkers[((objectLocation - 9) - 9) + 8] = checkers[((((objectLocation + 8) - 9) - 9) - 9) - 9];
                    }

                    checkers[((((objectLocation + 8) - 9) - 9) - 9) - 9] = 0;
                    checkers[(((objectLocation + 8) - 9) - 9) - 9] = 0;
                    if (objectLocation > 55 && checkers[((objectLocation + 8) - 9) - 9] == 1) {
                        checkers[objectLocation + 8] = 3;
                    } else {
                        checkers[objectLocation + 8] = checkers[((objectLocation + 8) - 9) - 9];
                    }

                    checkers[((objectLocation + 8) - 9) - 9] = 0;
                    checkers[(objectLocation + 8) - 9] = 0;
                    x -= 50;
                    y -= 50;
                    if (tiles[objectLocation - 9].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation - 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y -= 50;
                    if (tiles[(objectLocation - 9) - 9].equals("GRAYTILE")) {
                        handler.replaceObject(((objectLocation - 9) - 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y -= 50;
                    if (tiles[((objectLocation - 9) - 9) - 9].equals("GRAYTILE")) {
                        handler.replaceObject((((objectLocation - 9) - 9) - 9) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y -= 50;
                    if (tiles[(((objectLocation - 9) - 9) - 9) - 9].equals("GRAYTILE")) {
                        handler.replaceObject(((((objectLocation - 9) - 9) - 9) - 9) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y -= 50;
                    if (tiles[((((objectLocation - 9) - 9) - 9) - 9) - 9].equals("GRAYTILE")) {
                        handler.replaceObject((((((objectLocation - 9) - 9) - 9) - 9) - 9) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x -= 50;
                    y -= 50;
                    if (tiles[(((((objectLocation - 9) - 9) - 9) - 9) - 9) - 9].equals("GRAYTILE")) {
                        handler.replaceObject(((((((objectLocation - 9) - 9) - 9) - 9) - 9) - 9) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                //
                if (possibleMoves[objectLocation + 8] == 6) {
                    // if (objectLocation < 56)
                    // checkers[objectLocation + 8] = 1;
                    if (objectLocation > 55 && checkers[((objectLocation + 8) - 7) - 7] == 1) {
                        checkers[objectLocation + 8] = 3;
                    } else {
                        checkers[objectLocation + 8] = checkers[((objectLocation + 8) - 7) - 7];
                    }

                    checkers[((objectLocation + 8) - 7) - 7] = 0;
                    checkers[(objectLocation + 8) - 7] = 0;
                    x += 50;
                    y -= 50;
                    if (tiles[objectLocation - 7].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation - 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y -= 50;
                    if (tiles[(objectLocation - 7) - 7].equals("GRAYTILE")) {
                        handler.replaceObject(((objectLocation - 7) - 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                ////
                if (possibleMoves[objectLocation + 8] == 16) {
                    // if (objectLocation < 56)
                    // checkers[objectLocation + 8] = 1;
                    if (objectLocation > 55 && checkers[((((objectLocation + 8) - 7) - 7) - 7) - 7] == 1) {
                        checkers[((objectLocation - 7) - 7) + 8] = 3;
                    } else {
                        checkers[((objectLocation - 7) - 7) + 8] = checkers[((((objectLocation + 8) - 7) - 7) - 7) - 7];
                    }

                    checkers[(((((objectLocation + 8) - 7) - 7) - 7) - 7)] = 0;
                    checkers[((((objectLocation + 8) - 7) - 7) - 7)] = 0;
                    if (objectLocation > 55 && checkers[(objectLocation + 8) - 7] == 1) {
                        checkers[objectLocation + 8] = 3;
                    } else {
                        checkers[objectLocation + 8] = checkers[((objectLocation + 8) - 7) - 7];
                    }

                    checkers[((objectLocation + 8) - 7) - 7] = 0;
                    checkers[(objectLocation + 8) - 7] = 0;
                    x += 50;
                    y -= 50;
                    if (tiles[objectLocation - 7].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation - 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y -= 50;
                    if (tiles[(objectLocation - 7) - 7].equals("GRAYTILE")) {
                        handler.replaceObject(((objectLocation - 7) - 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y -= 50;
                    if (tiles[(((objectLocation - 7) - 7) - 7)].equals("GRAYTILE")) {
                        handler.replaceObject((((objectLocation - 7) - 7) - 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y -= 50;
                    if (tiles[((((objectLocation - 7) - 7) - 7) - 7)].equals("GRAYTILE")) {
                        handler.replaceObject(((((objectLocation - 7) - 7) - 7) - 7) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                //////
                if (possibleMoves[objectLocation + 8] == 26) {
                    // if (objectLocation < 56)
                    // checkers[objectLocation + 8] = 1;
                    if (objectLocation > 55 && checkers[((((((objectLocation + 8) - 7) - 7) - 7) - 7) - 7) - 7] == 1) {
                        checkers[((((objectLocation - 7) - 7) - 7) - 7) + 8] = 3;
                    } else {
                        checkers[((((objectLocation - 7) - 7) - 7) - 7)
                                + 8] = checkers[((((((objectLocation + 8) - 7) - 7) - 7) - 7) - 7) - 7];
                    }

                    checkers[((((((objectLocation + 8) - 7) - 7) - 7) - 7) - 7) - 7] = 0;
                    checkers[(((((objectLocation + 8) - 7) - 7) - 7) - 7) - 7] = 0;
                    if ((objectLocation - 7) - 7 > 55 && checkers[(((objectLocation + 8) - 7) - 7) - 7] == 1) {
                        checkers[((objectLocation - 7) - 7) + 8] = 3;
                    } else {
                        checkers[((objectLocation - 7) - 7) + 8] = checkers[((((objectLocation + 8) - 7) - 7) - 7) - 7];
                    }

                    checkers[(((((objectLocation + 8) - 7) - 7) - 7) - 7)] = 0;
                    checkers[((((objectLocation + 8) - 7) - 7) - 7)] = 0;
                    if (objectLocation > 55 && checkers[(objectLocation + 8) - 7] == 1) {
                        checkers[objectLocation + 8] = 3;
                    } else {
                        checkers[objectLocation + 8] = checkers[((objectLocation + 8) - 7) - 7];
                    }

                    checkers[((objectLocation + 8) - 7) - 7] = 0;
                    checkers[(objectLocation + 8) - 7] = 0;
                    x += 50;
                    y -= 50;
                    if (tiles[objectLocation - 7].equals("GRAYTILE")) {
                        handler.replaceObject((objectLocation - 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y -= 50;
                    if (tiles[(objectLocation - 7) - 7].equals("GRAYTILE")) {
                        handler.replaceObject(((objectLocation - 7) - 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y -= 50;
                    if (tiles[(((objectLocation - 7) - 7) - 7)].equals("GRAYTILE")) {
                        handler.replaceObject((((objectLocation - 7) - 7) - 7) * 3, new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y -= 50;
                    if (tiles[((((objectLocation - 7) - 7) - 7) - 7)].equals("GRAYTILE")) {
                        handler.replaceObject(((((objectLocation - 7) - 7) - 7) - 7) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y -= 50;
                    if (tiles[((((objectLocation - 7) - 7) - 7) - 7) - 7].equals("GRAYTILE")) {
                        handler.replaceObject((((((objectLocation - 7) - 7) - 7) - 7) - 7) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                    x += 50;
                    y -= 50;
                    if (tiles[(((((objectLocation - 7) - 7) - 7) - 7) - 7) - 7].equals("GRAYTILE")) {
                        handler.replaceObject(((((((objectLocation - 7) - 7) - 7) - 7) - 7) - 7) * 3,
                                new GRAYTILE(x, y, ID.GRAYTILE));
                    }
                }
                x = (mouse.getX() / 50) * 50;
                y = (mouse.getY() / 50) * 50;
                if (checkers[objectLocation + 8] == 2)
                    handler.replaceObject(objectLocation * 3, new REDCHECKER(x, y, ID.REDCHECKER));
                if (checkers[objectLocation + 8] == 1)
                    handler.replaceObject(objectLocation * 3, new BLACKCHECKER(x, y, ID.BLACKCHECKER));
                if (checkers[objectLocation + 8] == 4)
                    handler.replaceObject(objectLocation * 3, new REDKING(x, y, ID.REDKING));
                if (checkers[objectLocation + 8] == 3)
                    handler.replaceObject(objectLocation * 3, new BLACKKING(x, y, ID.BLACKKING));
                player++;
            }

            for (int i = 0; i != 70; i++) {
                possibleMoves[i + 8] = 0;
            }
            if (checkers[objectLocation + 8] != 0 && validMove && clickedOnProperChecker) {
                if (checkers[objectLocation + 8] == 2 || checkers[objectLocation + 8] == 4
                        || checkers[objectLocation + 8] == 3) {
                    // Top Left
                    if (checkers[(objectLocation + 8) - 9] == 0 && (objectLocation + 8) % 8 != 0
                            && objectLocation - 9 > -1) {
                        possibleMoves[(objectLocation - 9) + 8] = 4;
                    }
                    // Top Right
                    if (checkers[(objectLocation + 8) - 7] == 0 && (objectLocation + 1) % 8 != 0
                            && objectLocation - 7 > -1) {
                        possibleMoves[(objectLocation - 7) + 8] = 3;
                    }
                    // Top Left of Top Left
                    if (((checkers[objectLocation + 8] == 2 || checkers[objectLocation + 8] == 4)
                            && checkers[(objectLocation + 8) - 9] % 2 != 0)
                            || (checkers[objectLocation + 8] == 3 && (checkers[(objectLocation + 8) - 9] == 2
                                    || checkers[(objectLocation + 8) - 9] == 4)) && (objectLocation + 8) % 8 != 0
                                    && ((objectLocation - 9) + 8) % 8 != 0 && (objectLocation - 9) - 9 > -1) {
                        if (((checkers[objectLocation + 8] == 2 || checkers[objectLocation + 8] == 4)
                                && checkers[(objectLocation + 8) - 9] % 2 != 0)
                                || (checkers[objectLocation + 8] == 3 && (checkers[(objectLocation + 8) - 9] == 2
                                        || checkers[(objectLocation + 8) - 9] == 4)) && (objectLocation - 9) - 9 > -1
                                        && checkers[((objectLocation - 9) - 9) + 8] == 0
                                        && (objectLocation + 8) % 8 != 0 && ((objectLocation - 9) + 8) % 8 != 0) {
                            possibleMoves[((objectLocation - 9) - 9) + 8] = 8;
                            if (((checkers[objectLocation + 8] == 2 || checkers[objectLocation + 8] == 4)
                                    && checkers[(((objectLocation + 8) - 9) - 9) - 9] % 2 != 0)
                                    || (checkers[objectLocation + 8] == 3
                                            && (checkers[(((objectLocation + 8) - 9) - 9) - 9] == 2
                                                    || checkers[(((objectLocation + 8) - 9) - 9) - 9] == 4))
                                            && (((objectLocation - 9) - 9) - 9) - 9 > -1
                                            && checkers[((((objectLocation - 9) - 9) - 9) - 9) + 8] == 0
                                            && (((objectLocation - 9) - 9) + 8) % 8 != 0
                                            && ((((objectLocation - 9) - 9) - 9) + 8) % 8 != 0) {
                                possibleMoves[((((objectLocation - 9) - 9) - 9) - 9) + 8] = 18;
                                if (((checkers[objectLocation + 8] == 2 || checkers[objectLocation + 8] == 4)
                                        && checkers[(((((objectLocation + 8) - 9) - 9) - 9) - 9) - 9] % 2 != 0)
                                        || (checkers[objectLocation + 8] == 3
                                                && (checkers[(((((objectLocation + 8) - 9) - 9) - 9) - 9) - 9] == 2
                                                        || checkers[(((((objectLocation + 8) - 9) - 9) - 9) - 9)
                                                                - 9] == 4))
                                                && (((((objectLocation - 9) - 9) - 9) - 9) - 9) - 9 > -1
                                                && checkers[((((((objectLocation - 9) - 9) - 9) - 9) - 9) - 9) + 8] == 0
                                                && (((((objectLocation - 9) - 9) - 9) - 9) + 8) % 8 != 0
                                                && ((((((objectLocation - 9) - 9) - 9) - 9) - 9) + 8) % 8 != 0) {
                                    possibleMoves[((((((objectLocation - 9) - 9) - 9) - 9) - 9) - 9) + 8] = 28;
                                }
                            }
                        }
                    }
                    // Top Right of Top Right
                    if (((checkers[objectLocation + 8] == 2 || checkers[objectLocation + 8] == 4)
                            && checkers[(objectLocation + 8) - 7] % 2 != 0)
                            || (checkers[objectLocation + 8] == 3 && (checkers[(objectLocation + 8) - 7] == 2
                                    || checkers[(objectLocation + 8) - 7] == 4)) && (objectLocation + 1) % 8 != 0
                                    && ((objectLocation - 7) + 1) % 8 != 0 && (objectLocation - 7) - 7 > -1) {
                        if (((checkers[objectLocation + 8] == 2 || checkers[objectLocation + 8] == 4)
                                && checkers[(objectLocation + 8) - 7] % 2 != 0)
                                || (checkers[objectLocation + 8] == 3 && (checkers[(objectLocation + 8) - 7] == 2
                                        || checkers[(objectLocation + 8) - 7] == 4)) && (objectLocation - 7) - 7 > -1
                                        && checkers[((objectLocation - 7) - 7) + 8] == 0
                                        && (objectLocation + 1) % 8 != 0 && ((objectLocation - 7) + 1) % 8 != 0) {
                            possibleMoves[((objectLocation - 7) - 7) + 8] = 7;
                            if (((checkers[objectLocation + 8] == 2 || checkers[objectLocation + 8] == 4)
                                    && checkers[(((objectLocation + 8) - 7) - 7) - 7] % 2 != 0)
                                    || (checkers[objectLocation + 8] == 3
                                            && (checkers[(((objectLocation + 8) - 7) - 7) - 7] == 2
                                                    || checkers[(((objectLocation + 8) - 7) - 7) - 7] == 4))
                                            && (((objectLocation - 7) - 7) - 7) - 7 > -1
                                            && checkers[((((objectLocation - 7) - 7) - 7) - 7) + 8] == 0
                                            && (((objectLocation - 7) - 7) + 1) % 8 != 0
                                            && ((((objectLocation - 7) - 7) - 7) + 1) % 8 != 0) {
                                possibleMoves[((((objectLocation - 7) - 7) - 7) - 7) + 8] = 17;
                                if (((checkers[objectLocation + 8] == 2 || checkers[objectLocation + 8] == 4)
                                        && checkers[(((((objectLocation + 8) - 7) - 7) - 7) - 7) - 7] % 2 != 0)
                                        || (checkers[objectLocation + 8] == 3
                                                && (checkers[(((((objectLocation + 8) - 7) - 7) - 7) - 7) - 7] == 2
                                                        || checkers[(((((objectLocation + 8) - 7) - 7) - 7) - 7)
                                                                - 7] == 4))
                                                && (((((objectLocation - 7) - 7) - 7) - 7) - 7) - 7 > -1
                                                && checkers[((((((objectLocation - 7) - 7) - 7) - 7) - 7) - 7) + 8] == 0
                                                && (((((objectLocation - 7) - 7) - 7) - 7) + 1) % 8 != 0
                                                && ((((((objectLocation - 7) - 7) - 7) - 7) - 7) + 1) % 8 != 0) {
                                    possibleMoves[((((((objectLocation - 7) - 7) - 7) - 7) - 7) - 7) + 8] = 27;
                                }
                            }
                        }
                    }
                }
                if (checkers[objectLocation + 8] == 1 || checkers[objectLocation + 8] == 3
                        || checkers[objectLocation + 8] == 4) {
                    // Bottom Right
                    if (checkers[(objectLocation + 8) + 9] == 0 && (objectLocation + 1) % 8 != 0
                            && objectLocation + 9 < 64) {
                        possibleMoves[(objectLocation + 9) + 8] = 1;
                    }
                    // Bottom Left
                    if (checkers[(objectLocation + 8) + 7] == 0 && (objectLocation + 8) % 8 != 0
                            && objectLocation + 7 < 64) {
                        possibleMoves[(objectLocation + 7) + 8] = 2;
                    }
                    // Bottom Right of Bottom Right
                    if ((((checkers[objectLocation + 8] == 1 || checkers[objectLocation + 8] == 3)
                            && ((checkers[(objectLocation + 8) + 9] == 2 || checkers[(objectLocation + 8) + 9] == 4))
                            || (checkers[(objectLocation + 8)] == 4 && (checkers[(objectLocation + 9) + 8] == 1
                                    || checkers[((objectLocation + 9) + 8)] == 3))))
                            && (objectLocation + 1) % 8 != 0 && ((objectLocation + 9) + 1) % 8 != 0) {
                        if ((((checkers[objectLocation + 8] == 1 || checkers[objectLocation + 8] == 3)
                                && ((checkers[(objectLocation + 8) + 9] == 2
                                        || checkers[(objectLocation + 8) + 9] == 4))
                                || (checkers[(objectLocation + 8)] == 4 && (checkers[(objectLocation + 9) + 8] == 1
                                        || checkers[((objectLocation + 9) + 8)] == 3))))
                                && checkers[((objectLocation + 9) + 9) + 8] == 0 && (objectLocation + 9) + 9 < 64
                                && (objectLocation + 1) % 8 != 0 && ((objectLocation + 9) + 1) % 8 != 0) {
                            possibleMoves[((objectLocation + 9) + 9) + 8] = 5;
                            if ((((checkers[objectLocation + 8] == 1 || checkers[objectLocation + 8] == 3)
                                    && ((checkers[(((objectLocation + 8) + 9) + 9) + 9] == 2
                                            || checkers[(((objectLocation + 8) + 9) + 9) + 9] == 4))
                                    || (checkers[(objectLocation + 8)] == 4
                                            && (checkers[(((objectLocation + 9) + 9) + 9) + 8] == 1
                                                    || checkers[((((objectLocation + 9) + 9) + 9) + 8)] == 3))))
                                    && checkers[((((objectLocation + 9) + 9) + 9) + 9) + 8] == 0
                                    && (((objectLocation + 9) + 9) + 9) + 9 < 64
                                    && (((objectLocation + 9) + 9) + 1) % 8 != 0
                                    && ((((objectLocation + 9) + 9) + 9) + 1) % 8 != 0) {
                                possibleMoves[((((objectLocation + 9) + 9) + 9) + 9) + 8] = 15;
                                if ((((checkers[objectLocation + 8] == 1 || checkers[objectLocation + 8] == 3)
                                        && ((checkers[(((((objectLocation + 8) + 9) + 9) + 9) + 9) + 9] == 2
                                                || checkers[(((((objectLocation + 8) + 9) + 9) + 9) + 9) + 9] == 4))
                                        || (checkers[(objectLocation + 8)] == 4
                                                && (checkers[(((((objectLocation + 9) + 9) + 9) + 9) + 9) + 8] == 1
                                                        || checkers[((((((objectLocation + 9) + 9) + 9) + 9) + 9)
                                                                + 8)] == 3))))
                                        && checkers[((((((objectLocation + 9) + 9) + 9) + 9) + 9) + 9) + 8] == 0
                                        && (((((objectLocation + 9) + 9) + 9) + 9) + 9) + 9 < 64
                                        && (((((objectLocation + 9) + 9) + 9) + 9) + 1) % 8 != 0
                                        && ((((((objectLocation + 9) + 9) + 9) + 9) + 9) + 1) % 8 != 0) {
                                    possibleMoves[((((((objectLocation + 9) + 9) + 9) + 9) + 9) + 9) + 8] = 25;
                                }
                            }
                        }
                    }
                    // Bottom Left of Bottom Left
                    if (((checkers[objectLocation + 8] == 1 || checkers[objectLocation + 8] == 3)
                            && (checkers[(objectLocation + 8) + 7] == 2 || checkers[(objectLocation + 8) + 7] == 4))
                            || (checkers[objectLocation + 8] == 4 && checkers[(objectLocation + 8) + 7] % 2 != 0)
                                    && (objectLocation + 8) % 8 != 0 && ((objectLocation + 7) + 8) % 8 != 0) {
                        if (((checkers[objectLocation + 8] == 1 || checkers[objectLocation + 8] == 3)
                                && (checkers[(objectLocation + 8) + 7] == 2 || checkers[(objectLocation + 8) + 7] == 4))
                                || (checkers[objectLocation + 8] == 4 && checkers[(objectLocation + 8) + 7] % 2 != 0)
                                        && checkers[((objectLocation + 7) + 7) + 8] == 0
                                        && (objectLocation + 7) + 7 < 64 && (objectLocation + 8) % 8 != 0
                                        && ((objectLocation + 7) + 8) % 8 != 0) {
                            possibleMoves[((objectLocation + 7) + 7) + 8] = 6;
                            if (((checkers[objectLocation + 8] == 1 || checkers[objectLocation + 8] == 3)
                                    && (checkers[(((objectLocation + 8) + 7) + 7) + 7] == 2
                                            || checkers[(((objectLocation + 8) + 7) + 7) + 7] == 4))
                                    || (checkers[objectLocation + 8] == 4
                                            && checkers[(((objectLocation + 8) + 7) + 7) + 7] % 2 != 0)
                                            && checkers[((((objectLocation + 7) + 7) + 7) + 7) + 8] == 0
                                            && ((((objectLocation + 7) + 7) + 7) + 7) < 64
                                            && (((objectLocation + 7) + 7) + 8) % 8 != 0
                                            && ((((objectLocation + 7) + 7) + 7) + 8) % 8 != 0) {
                                possibleMoves[((((objectLocation + 7) + 7) + 7) + 7) + 8] = 16;
                                if (((checkers[objectLocation + 8] == 1 || checkers[objectLocation + 8] == 3)
                                        && (checkers[(((((objectLocation + 8) + 7) + 7) + 7) + 7) + 7] == 2
                                                || checkers[(((((objectLocation + 8) + 7) + 7) + 7) + 7) + 7] == 4))
                                        || (checkers[objectLocation + 8] == 4
                                                && checkers[(((((objectLocation + 8) + 7) + 7) + 7) + 7) + 7] % 2 != 0)
                                                && checkers[((((((objectLocation + 7) + 7) + 7) + 7) + 7) + 7) + 8] == 0
                                                && (((((objectLocation + 7) + 7) + 7) + 7) + 7) + 7 < 64
                                                && (((((objectLocation + 7) + 7) + 7) + 7) + 8) % 8 != 0
                                                && ((((((objectLocation + 7) + 7) + 7) + 7) + 7) + 8) % 8 != 0) {
                                    possibleMoves[((((((objectLocation + 7) + 7) + 7) + 7) + 7) + 7) + 8] = 26;
                                }
                            }
                        }
                    }
                }
                for (int i = 0; i != 70; i++) {
                    if (possibleMoves[i + 8] != 0) {
                        x = mouse.getX();
                        y = mouse.getY();
                        if (i == objectLocation + 9) {
                            x = ((x / 50) * 50) + 50;
                            y = ((y / 50) * 50) + 50;
                        }
                        if (i == objectLocation + 7) {
                            x = ((x / 50) * 50) - 50;
                            y = ((y / 50) * 50) + 50;
                        }
                        if (i == objectLocation - 9) {
                            x = ((x / 50) * 50) - 50;
                            y = ((y / 50) * 50) - 50;
                        }
                        if (i == objectLocation - 7) {
                            x = ((x / 50) * 50) + 50;
                            y = ((y / 50) * 50) - 50;
                        }
                        //
                        if (i == (objectLocation + 9) + 9) {
                            x = ((x / 50) * 50) + 100;
                            y = ((y / 50) * 50) + 100;
                        }
                        if (i == (objectLocation + 7) + 7) {
                            x = ((x / 50) * 50) - 100;
                            y = ((y / 50) * 50) + 100;
                        }
                        if (i == (objectLocation - 9) - 9) {
                            x = ((x / 50) * 50) - 100;
                            y = ((y / 50) * 50) - 100;
                        }
                        if (i == (objectLocation - 7) - 7) {
                            x = ((x / 50) * 50) + 100;
                            y = ((y / 50) * 50) - 100;
                        }
                        //
                        if (i == (((objectLocation + 9) + 9) + 9) + 9) {
                            x = ((x / 50) * 50) + 200;
                            y = ((y / 50) * 50) + 200;
                        }
                        if (i == (((objectLocation + 7) + 7) + 7) + 7) {
                            x = ((x / 50) * 50) - 200;
                            y = ((y / 50) * 50) + 200;
                        }
                        if (i == (((objectLocation - 9) - 9) - 9) - 9) {
                            x = ((x / 50) * 50) - 200;
                            y = ((y / 50) * 50) - 200;
                        }
                        if (i == (((objectLocation - 7) - 7) - 7) - 7) {
                            x = ((x / 50) * 50) + 200;
                            y = ((y / 50) * 50) - 200;
                        }
                        //
                        if (i == (((((objectLocation + 9) + 9) + 9) + 9) + 9) + 9) {
                            x = ((x / 50) * 50) + 300;
                            y = ((y / 50) * 50) + 300;
                        }
                        if (i == (((((objectLocation + 7) + 7) + 7) + 7) + 7) + 7) {
                            x = ((x / 50) * 50) - 300;
                            y = ((y / 50) * 50) + 300;
                        }
                        if (i == (((((objectLocation - 9) - 9) - 9) - 9) - 9) - 9) {
                            x = ((x / 50) * 50) - 300;
                            y = ((y / 50) * 50) - 300;
                        }
                        if (i == (((((objectLocation - 7) - 7) - 7) - 7) - 7) - 7) {
                            x = ((x / 50) * 50) + 300;
                            y = ((y / 50) * 50) - 300;
                        }
                        handler.replaceObject(i * 3, new POSSIBLEMOVE(x, y, ID.POSSIBLEMOVE));
                        removedObjectType[x1] = handler.getRemovedObject();
                        removedObjectsLocations[x1] = i * 3;
                        x1++;
                    }
                }
            }
            int q = 0;
            int w = 0;
            for (int i = 0; i != 100; i++) {
                if (checkers[i] == 1)
                    q++;
                if (checkers[i] == 2)
                    w++;
            }
            if (q == 0 || w == 0) {
                if (w == 0)
                    out.println("Game End, Player 1 Winner!!!");
                if (q == 0)
                    out.println("Game End, Player 2 Winner!!!");
                runningGame = false;
            }
        }
    }
}
