package org.ivan.simple.game;


import android.graphics.Canvas;

public class GameManager extends Thread {
	private GameView view;

	private boolean running = false;
	
	private static long FPS;
	private static long ticksPS;
	
	static {
		FPS = 25;
		ticksPS = 1000 / FPS;;
	}
	
	public static void changeFPS(int dFPS) {
		if(FPS + dFPS > 0) {
			FPS += dFPS;
			ticksPS = 1000 / FPS;
		}
	}
	
	public static long getFPS() {
		return FPS;
	}
	
	public GameManager(GameView view) {
		this.view = view;
	}
	
	public void setRunning(boolean run) {
		running = run;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	@Override
	public void run() {
		
        long startTime;
        long sleepTime;
		while(running) {
			startTime = System.currentTimeMillis();
			Canvas c = null;
			try {
				c = view.getHolder().lockCanvas();
				synchronized (view.getHolder()) {
					if(view.readyForUpdate()) {
						view.updateGame();
					}
					view.updateHeroScreenPosition();
					view.onDraw(c);
					if(view.finished) {
						((GameActivity) view.getContext()).switchBackToChooseActivity(view.isComplete());
					}
				}
			} catch(Exception ex) {
				// TODO process exception!
			} finally {
				if(c != null) {
					view.getHolder().unlockCanvasAndPost(c);
				}
			}
			
			// calculate sleep time to reach needed fps
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
