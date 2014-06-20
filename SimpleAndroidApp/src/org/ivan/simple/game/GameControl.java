package org.ivan.simple.game;

import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import org.ivan.simple.PandaApplication;
import org.ivan.simple.UserControlType;
import org.ivan.simple.game.controls.ControlChangeObserver;
import org.ivan.simple.game.controls.ObtainedControl;
import org.ivan.simple.game.controls.UserControl;
import org.ivan.simple.game.controls.UserControlProvider;
import org.ivan.simple.game.level.LevelModel;
import org.ivan.simple.game.sound.SoundManager;
import org.ivan.simple.game.tutorial.SolutionStep;
import org.ivan.simple.utils.OneShotAction;

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
    protected int levId = 0;
    protected boolean finished = false;
    protected boolean monsterLose = false;
    protected int loseDelay = 3;
    private OneShotAction playWinSound;
    private OneShotAction playLoseSound;

    protected void setLevId(int levId) {
        this.levId = levId;
    }

    public GameControl(final GameView view) {
		this.view  = view;
        gameLoopThread = new GameManager(view);
        controlsFactory = new ControlsFactory(this);
        initControlProvider();
        soundManager = view.getGameContext().app().getSoundManager();
	}

    protected boolean scanControl(MotionEvent event) {
		if(paused || robotMode) return false;
		return controlProvider.obtainControl(event);
	}

    protected void restartGame() {
        stopManager();
        initGame();
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

    protected void initGame() {
        finished = false;
        monsterLose = false;
        loseDelay = 3;
        playWinSound = new OneShotAction() {
            @Override
            protected void doAction() {
                if (view.getGameContext().app().getSound()) {
                    soundManager.playWin();
                }
            }
        };
        playLoseSound = new OneShotAction() {
            @Override
            protected void doAction() {
                if (view.getGameContext().app().getSound()) {
                    soundManager.playDetonate();
                }
            }
        };
        LevelModel model =
                new LevelModel(levId, PandaApplication.getPandaApplication().getLevelParser());
        view.initView(model);
    }
	
	protected GameManager getGameLoopThread() {
		return gameLoopThread;
	}
	
	protected void playSound() {
		if(view.getGameContext().app().getSound()) {
			soundManager.playSound(
                    view.level.model.hero.currentMotion,
                    view.level.model.hero.finishingMotion,
                    view.level.model.getHeroCell(),
                    view.prevCell
            );
		}
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
            if(autoControls.hasNext()) {
                SolutionStep step = autoControls.next();
                control = new ObtainedControl(step.getControl());
                view.guideAnimation.init(step);
                toastMessage(step.getMessage());
            } else {
                control = new ObtainedControl(UserControlType.IDLE);
                view.getGameContext().stopTutorial();
            }
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

    protected void playDetonateSound() {
        playLoseSound.act();
    }

    protected void playWinSound() {
        playWinSound.act();
    }

    private OneShotAction firstStartGame = new OneShotAction() {
        @Override
        protected void doAction() {
            startManager();
        }
    };

    protected void firstStartGame() {
        firstStartGame.act();
    }

    public void releaseResources() {
        if(view.background != null) view.background.recycle();
        view.getGameContext().app().getImageProvider().recycleLruCache();
    }
}
