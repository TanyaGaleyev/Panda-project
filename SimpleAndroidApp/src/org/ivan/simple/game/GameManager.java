package org.ivan.simple.game;


import android.graphics.Canvas;

import org.ivan.simple.UserControlType;

public class GameManager extends Thread {
	private GameView view;

	private boolean running = false;
	
	private static long FPS;
	private static long ticksPS;
	
	static {
		FPS = 25;
		ticksPS = 1000 / FPS;
	}

    public static void changeFPS(int dFPS) {
		if(FPS == 1) {
			FPS = 5;
			ticksPS = 1000 / FPS;
		} else if(FPS + dFPS > 0) {
			FPS += dFPS;
			ticksPS = 1000 / FPS;
		} else {
			FPS = 1;
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

    private UserControlType rememberedControl = UserControlType.IDLE;
    private long rememberTime = 0;

	@Override
	public void run() {

        long startTime;
        long sleepTime;
		while(running) {
			startTime = System.currentTimeMillis();

            if(System.currentTimeMillis() - rememberTime > ticksPS / 2) {
                rememberedControl = UserControlType.IDLE;
            }
            UserControlType userControl = view.getControl().getUserControl();
            if(userControl == UserControlType.IDLE) {
                userControl = rememberedControl;
            } else {
                rememberTime = startTime;
                rememberedControl = userControl;
            }
            if(view.readyForUpdate(userControl)) {
				view.updateGame(userControl);
                rememberedControl = UserControlType.IDLE;
			}
            view.updatePositions();
			doDraw(true);
			if(view.finished) {
				((GameActivity) view.getContext()).switchBackToChooseActivity(
                        view.isComplete(),
                        view.getScore());
			}

			// calculate sleep time to reach needed fps
			sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0) sleep(sleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
		}
	}

    protected void doDraw(boolean update) {
		Canvas c = null;
		try {
			c = view.getHolder().lockCanvas();
			synchronized (view.getHolder()) {
				view.onDraw(c, update);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(c != null) {
				view.getHolder().unlockCanvasAndPost(c);
			}
		}
	}
	
}
