package org.ivan.simple;

import android.graphics.Canvas;

public class GameManager extends Thread {
	private GameView view;

	private boolean running = false;
	
	static final long FPS = 5;
	
	public GameManager(GameView view) {
		this.view = view;
	}
	
	public void setRunning(boolean run) {
		running = run;
	}
	
	@Override
	public void run() {
		long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
		while(running) {
			startTime = System.currentTimeMillis();
			Canvas c = null;
			try {
				c = view.getHolder().lockCanvas();
				synchronized (view.getHolder()) {
					view.onDraw(c);
				}
			} catch(Exception ex) {
				// TODO process exception!
			} finally {
				if(c != null) {
					view.getHolder().unlockCanvasAndPost(c);
				}
			}
			
			sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            try {
                   if (sleepTime > 10)
                          sleep(sleepTime);
                   else
                          sleep(10);
            } catch (Exception e) {}
		}
	}
}
