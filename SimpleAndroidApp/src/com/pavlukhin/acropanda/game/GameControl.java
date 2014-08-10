package com.pavlukhin.acropanda.game;

import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.pavlukhin.acropanda.PandaApplication;
import com.pavlukhin.acropanda.UserControlType;
import com.pavlukhin.acropanda.bitmaputils.cache.Recycler;
import com.pavlukhin.acropanda.game.controls.ControlChangeObserver;
import com.pavlukhin.acropanda.game.controls.ObtainedControl;
import com.pavlukhin.acropanda.game.controls.UserControl;
import com.pavlukhin.acropanda.game.controls.UserControlProvider;
import com.pavlukhin.acropanda.game.level.LevelModel;
import com.pavlukhin.acropanda.game.level.actions.Action;
import com.pavlukhin.acropanda.game.level.actions.SoundAction;
import com.pavlukhin.acropanda.game.sound.SoundControl;
import com.pavlukhin.acropanda.game.tutorial.SolutionStep;
import com.pavlukhin.acropanda.utils.OneShotAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
    protected int packId = 0;
    protected int inPackPos = 0;
    protected boolean finished = false;
    protected boolean monsterLose = false;
    protected int loseDelay = 3;

    protected void setLevId(int levId, int packId, int inPackPos) {
        this.levId = levId;
        this.packId = packId;
        this.inPackPos = inPackPos;
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
        Log.d(PandaApplication.LOG_TAG, "Start game loop");
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
               Log.d(PandaApplication.LOG_TAG, "Interrupted exception: " + e);
           }
        }
	}

    public void postStopManager() {
        if(gameLoopThread == null) return;
        Log.d(PandaApplication.LOG_TAG, "Stop game loop");
        paused = true;
        gameLoopThread.setRunning(false);
    }

    private OneShotAction updateAttempts = new UpdateAttempts();

    private class UpdateAttempts extends OneShotAction {
        @Override
        protected void doAction() {
            view.getGameContext().updateAttempts();
        }
    }

    public void updateAttempts() {
        updateAttempts.act();
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
                updateAttempts = new UpdateAttempts();
                LevelModel levelModel =
                        new LevelModel(levId, PandaApplication.getPandaApplication().getLevelParser());
                view.initView(levelModel);
                retry = false;
                initControlProvider();
            } catch (OutOfMemoryError oom) {
                recycler.recycle();
                Log.w(PandaApplication.LOG_TAG, "Retry GameControl.initGame()");
            }
        }
    }
	
	protected GameManager getGameLoopThread() {
		return gameLoopThread;
	}
	
	protected void playSound(Collection<Action> actions) {
        soundControl.playSound(
                view.level.model.hero.currentMotion,
                view.level.model.hero.finishingMotion,
                view.level.model.getHeroCell(),
                view.prevCell
        );
        for(Action action : actions)
            if (action instanceof SoundAction)
                soundControl.playSound((SoundAction) action);
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
