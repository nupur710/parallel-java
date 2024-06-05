package org.section.one;

import java.applet.Applet;

public class ParticleApplet extends Applet {

    protected Thread[] threads= null;
    protected final ParticleCanvas canvas= new ParticleCanvas(100);

    public void init() {
        add(canvas);
    }

    public void check() {
        Thread t= new Thread();
        t.start();
        t.run();
    }

    protected Thread makeThread(final Particle p) { // utility
        Runnable runloop = new Runnable() {
            public void run() {
                try {
                    for(;;) { //forever loop-- broken only when current thread is interrupted
                        p.move(); //particle moves
                        canvas.repaint(); //canvas repaints-- paricle will be visible
                        Thread.sleep(100); // slow down to view
                    }
                }
                catch (InterruptedException e) { return; }
            }
        };
        return new Thread(runloop);
    }
    public synchronized void start() {
        int n = 10; // just for demo
        if (threads == null) { // bypass if already started
            Particle[] particles = new Particle[n];
            for (int i = 0; i < n; ++i)
                particles[i] = new Particle(50, 50);
            canvas.setParticles(particles);
            threads = new Thread[n];
            for (int i = 0; i < n; ++i) {
                threads[i] = makeThread(particles[i]);
                threads[i].start();
            }
        }
    }
    public synchronized void stop() {
        if (threads != null) { // bypass if already stopped
            for (int i = 0; i < threads.length; ++i)
                threads[i].interrupt();
            threads = null;
        }
    }

}
