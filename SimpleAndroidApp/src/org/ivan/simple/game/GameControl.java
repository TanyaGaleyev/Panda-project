package org.ivan.simple.game;

import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import org.ivan.simple.PandaApplication;
import org.ivan.simple.UserControlType;
import org.ivan.simple.bitmaputils.cache.Recycler;
import org.ivan.simple.game.controls.ControlChangeObserver;
import org.ivan.simple.game.controls.ObtainedControl;
import org.ivan.simple.game.controls.UserControl;
import org.ivan.simple.game.controls.UserControlProvider;
import org.ivan.simple.game.level.LevelModel;
import org.ivan.simple.game.sound.SoundControl;
import org.ivan.simple.game.tutorial.SolutionStep;
import org.ivan.simple.utils.OneShotAction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class GameControl implements ControlChangeObserver {
	GameView view;
    private ControlsFactory controlsFactory;
    private UserControlProvider controlProvider;

    private boolean robotMode = false;
    private Iterator<SolutionStep> autoControls = new ArrayList<SolutionStep>().iterator();
	
	private GameManager gameLoopThread;
	private boolean paused;

	private SoundControl soundControl;
    protected int levId = 0;
    protected boolean finished = false;
    protected boolean monsterLose = false;
    protected int loseDelay = 3;

    protected void setLevId(int levId) {
        this.levId = levId;
    }

    public GameControl(final GameView view) {
		this.view  = view;
        gameLoopThread = new GameManager(view);
        controlsFactory = new ControlsFactory(this);
        initControlProvider();
        soundControl = new SoundControl(view.getGameContext());
	}

    protected boolean scanControl(MotionEvent event) {
		if(paused || robotMode) return false;
		return controlProvider.obtainControl(event);
	}

    protected void restartGame() {
        stopManager();
        initGame();
        gameLoopThread.doDraw(false);
    }
	
	protected boolean isRunning() {
		return gameLoopThread.isRunning();
	}
	
	public void startManager() {
        if(gameLoopThread != null && gameLoopThread.isRunning()) return;
        if(gameLoopThread != null) postStopManager();
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

    protected void initGame() {
        Recycler recycler = view.getGameContext().app().getImageProvider().getCacheRecycler();
        boolean retry = true;
        while (retry) {
            try {
                finished = false;
                monsterLose = false;
                loseDelay = 3;
                firstStartGame = new FirstStartGame();
                soundControl.init();
                LevelModel levelModel =
                        new LevelModel(levId, PandaApplication.getPandaApplication().getLevelParser());
                view.initView(levelModel);
                retry = false;
                initControlProvider();
            } catch (OutOfMemoryError oom) {
                recycler.recycle();
                System.err.println("Retry GameControl.initGame()");
            }
        }
    }
	
	protected GameManager getGameLoopThread() {
		return gameLoopThread;
	}
	
	protected void playSound() {
        soundControl.playSound(
                view.level.model.hero.currentMotion,
                view.level.model.hero.finishingMotion,
                view.level.model.getHeroCell(),
                view.prevCell
        );
	}

    protected void playDetonateSound() {
        soundControl.playDetonateSound();
    }

    protected void playWinSound() {
        soundControl.playWinSound();
    }

    public void setAutoControls(Iterator<SolutionStep> autoControls) {
        robotMode = true;
        this.autoControls = autoControls;
    }

    protected UserControl getUserControl() {
        UserControl control;
        if(!robotMode) {
            control = controlProvider.getUserControl();
        } else {
            control = getAutoControl();
        }
        return control;
    }

    private UserControl getAutoControl() {
        UserControl control;
        if(autoControls.hasNext()) {
            SolutionStep step = autoControls.next();
            control = new ObtainedControl(step.getControl());
            view.guideAnimation.init(step);
            toastMessage(step.getMessage());
        } else {
            control = new ObtainedControl(UserControlType.IDLE);
            view.getGameContext().stopTutorial();
        }
        return control;
    }

    private Toast lastToast;
    public void toastMessage(final String message) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if(message.length() > 0) {
                    if(lastToast != null) lastToast.cancel();
                    lastToast = Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT);
                    lastToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    lastToast.show();
                }
            }
        });
    }

    @Override
    public void notifyObserver() {
        initControlProvider();
    }

    private void initControlProvider() {
        controlProvider = controlsFactory.createControlProvider(
                view.getGameContext().app().getSettingsModel().getControlsType());
    }

    private OneShotAction firstStartGame = new FirstStartGame();
    private class FirstStartGame extends OneShotAction {
        @Override
        protected void doAction() {
            startManager();
        }
    }

    protected void firstStartGame() {
        firstStartGame.act();
    }

    public void releaseResources() {
        if(view.background != null)
            view.getGameContext().app().getImageProvider().free(view.background);
        view.getGameContext().app().getImageProvider().recycleLruCache();
    }
}
