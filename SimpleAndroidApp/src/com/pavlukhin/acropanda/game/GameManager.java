package com.pavlukhin.acropanda.game;


import android.graphics.Canvas;
import android.util.Log;

import com.pavlukhin.acropanda.PandaApplication;
import com.pavlukhin.acropanda.UserControlType;
import com.pavlukhin.acropanda.game.controls.UserControl;

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

    public static long getOneFrameDuration() {
        return ticksPS;
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
//    private long rememberTime = 0;

	@Override
	public void run() {
        long startTime;
        long sleepTime;
        try {
            Thread.sleep(100);
            while(running) {
                startTime = System.currentTimeMillis();
                UserControlType controlType = receiveUserControlType();
                if(view.readyForUpdate(controlType)) {
                    view.updateGame(controlType);
                    rememberedControl = UserControlType.IDLE;
                }
                view.updatePositions();
                doDraw(true);
                if(view.getControl().finished) {
                    if(view.isComplete())
                        view.getGameContext().switchBackToChooseActivity(true, view.getScore());
                    else
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                view.getGameContext().showLoseDialog();
                            }
                        });
                }
                // calculate sleep time to reach needed fps
                sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
                if (sleepTime > 0) sleep(sleepTime);
            }
        } catch (InterruptedException e) {
            Log.d(PandaApplication.LOG_TAG, "Game loop thread interrupted");
        }
	}

    private UserControlType receiveUserControlType() {
//        if(System.currentTimeMillis() - rememberTime > ticksPS * 8) {
//            rememberedControl = UserControlType.IDLE;
//        }
        UserControl userControl = view.getControl().getUserControl();
        UserControlType controlType = userControl.getControlType();
        if(controlType == UserControlType.IDLE) {
            controlType = rememberedControl;
        } else if(userControl.getRememberControlType() != UserControlType.IDLE) {
//            rememberTime = startTime;
            rememberedControl = userControl.getRememberControlType();
        }
        return controlType;
    }

    protected void doDraw(boolean update) {
		Canvas c = null;
		try {
			c = view.getHolder().lockCanvas();
            if(c != null) {
                view.draw(c, update);
            }
		} catch(Exception ex) {
			Log.e(PandaApplication.LOG_TAG, ex.getMessage(), ex);
		} finally {
			if(c != null) {
				view.getHolder().unlockCanvasAndPost(c);
			}
		}
	}
	
}
