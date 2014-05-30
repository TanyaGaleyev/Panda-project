package org.ivan.simple.game;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import org.ivan.simple.PandaApplication;
import org.ivan.simple.UserControlType;
import org.ivan.simple.game.controls.ControlChangeObserver;
import org.ivan.simple.game.controls.ControlsType;
import org.ivan.simple.game.controls.UserControlProvider;
import org.ivan.simple.game.level.LevelModel;
import org.ivan.simple.game.sound.SoundManager;
import org.ivan.simple.game.tutorial.SolutionStep;

import java.util.ArrayList;
import java.util.Iterator;

public class GameControl implements ControlChangeObserver {
	GameView view;
    private ControlsFactory controlsFactory;
    private UserControlProvider controlProvider;

    private boolean robotMode = false;
    private Iterator<SolutionStep> autoControls = new ArrayList<SolutionStep>().iterator();
	
	private GameManager gameLoopThread;
	private boolean paused;
	
	private SoundManager soundManager;
	
	public GameControl(final GameView view) {
		this.view  = view;
        controlsFactory = new ControlsFactory(this);
        initControlProvider();
        soundManager = new SoundManager(view.getContext());
	}

    protected boolean scanControl(MotionEvent event) {
		if(paused || robotMode) return false;
		return controlProvider.obtainControl(event);
	}

    protected void restartGame() {
        stopManager();
        view.initSurface();
        startManager();
    }
	
	protected boolean isRunning() {
		return gameLoopThread.isRunning();
	}
	
	public void startManager() {
        if(gameLoopThread != null && gameLoopThread.isRunning()) return;
		System.out.println("Start game loop");
		gameLoopThread = new GameManager(view);
		gameLoopThread.setRunning(true);
		gameLoopThread.start();
		paused = false;
	}
	
	public void stopManager() {
		if(gameLoopThread == null) return;
        postStopManager();
		boolean retry = true;
        while (retry) {
           try {
               gameLoopThread.join();
               retry = false;
           } catch (InterruptedException e) {
               System.out.println("Interrupted exception: " + e);
           }
        }
	}

    public void postStopManager() {
        if(gameLoopThread == null) return;
        System.out.println("Stop game loop");
        paused = true;
        gameLoopThread.setRunning(false);
    }
	
	protected GameManager getGameLoopThread() {
		return gameLoopThread;
	}
	
	protected void playSound(LevelModel model) {
		if(PandaApplication.getPandaApplication().getSound()) {
			soundManager.playSound(model.hero.currentMotion, model.hero.finishingMotion);
		}
	}

    public void setAutoControls(Iterator<SolutionStep> autoControls) {
        robotMode = true;
        this.autoControls = autoControls;
    }


    protected UserControlType getUserControl() {
        UserControlType controlType;
        if(!robotMode) {
            controlType = controlProvider.getUserControl();
        } else {
            if(autoControls.hasNext()) {
                final SolutionStep step = autoControls.next();
                controlType = step.getControl();
                view.guideAnimation.init(step);
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage(view.getContext(), step.getMessage());
                    }
                });
            } else {
                controlType = UserControlType.IDLE;
                view.getGameContext().stopTutorial();
            }
        }
        return controlType;
    }

    public void toastMessage(Context context, String message) {
        if(message.length() > 0) {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }

    @Override
    public void notifyObserver() {
        initControlProvider();
    }

    private void initControlProvider() {
        controlProvider = controlsFactory.createControlProvider(
                view.getGameContext().app().getSettingsModel().getControlsType());
    }
}
