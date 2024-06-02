package org.section.one;

import java.awt.*;
import java.util.Random;

public class Particle {

    protected int x;
    protected int y;
    protected final Random rng = new Random(); //marked final so will not be impacted by locking rules

    public Particle(int initialX, int initalY) {
        x = initialX;
        y = initalY;
    }

    public synchronized void move() {
        x += rng.nextInt(10) - 5;
        y += rng.nextInt(20) - 5;
    }

    public void draw(Graphics g) {
        int lx, ly;
        synchronized (this) {
            lx = x;
            ly = y;
        }
        g.drawRect(lx, ly, 10, 10);
    }

}