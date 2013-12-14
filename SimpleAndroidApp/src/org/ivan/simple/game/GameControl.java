package org.ivan.simple.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.ivan.simple.PandaApplication;
import org.ivan.simple.UserControlType;
import org.ivan.simple.game.level.LevelModel;
import org.ivan.simple.game.sound.SoundManager;
import org.ivan.simple.game.tutorial.SolutionStep;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

public class GameControl {
	private GameView view;
//	private LevelModel model;
//	private Hero hero;
	private UserControlType delayedControl = UserControlType.IDLE;
	public UserControlType pressedControl = UserControlType.IDLE;
	public UserControlType obtainedControl = UserControlType.IDLE;
	private TimerTask useDelayedControl;

    private boolean robotMode = false;
    private Iterator<SolutionStep> autoControls = new ArrayList<SolutionStep>().iterator();
	
	private float[] startPressedY = new float[2];
	private float[] startPressedX = new float[2];
	private int slideSenderID;
	
	private GameManager gameLoopThread;
	private boolean paused;
	
	private SoundManager soundManager;
	
	public GameControl(GameView view) {
		this.view  = view;
		soundManager = new SoundManager(view.getContext());
	}

    protected boolean scanControl(MotionEvent event, int x, int y) {
		if(paused || robotMode) return false;
		return oneHandControl(event, x ,y);
	}
	
	private boolean oneHandControl(MotionEvent event, int x, int y) {
		int actionMask = event.getActionMasked();
		int actionIndex = event.getActionIndex();
		int pointerId = event.getPointerId(actionIndex);
		switch(actionMask) {
		case MotionEvent.ACTION_DOWN:
			startPressedY[0] = event.getY();
			startPressedX[0] = event.getX();
//			if(event.getX() > hero.heroX) {
//				delayedControl = UserControlType.RIGHT;
//			} else {
//				delayedControl = UserControlType.LEFT;
//			}
			delayedControl = getMoveType(event, x, y);
			useDelayedControl = new TimerTask() {				
				@Override
				public void run() {
					pressedControl = delayedControl;
					obtainedControl = delayedControl;
				}
			};
			new Timer().schedule(useDelayedControl, 100);
			return true;
		case MotionEvent.ACTION_POINTER_DOWN:
			if(event.getPointerCount() > 2) return true;
			startPressedY[pointerId] = event.getY(actionIndex);
			startPressedX[pointerId] = event.getX(actionIndex);
			return true;
		case MotionEvent.ACTION_POINTER_UP:
			if(event.getPointerCount() > 2) return true;
			return true;
		case MotionEvent.ACTION_UP:
			/*if(pressedControl != UserControlType.IDLE) {
				model.setControlType(pressedControl);
			} else */if(useDelayedControl != null &&
					useDelayedControl.cancel() &&
					obtainedControl == UserControlType.IDLE) {
				obtainedControl = delayedControl;
			}
			pressedControl = UserControlType.IDLE;
			return true;
		case MotionEvent.ACTION_MOVE:
			if(event.getPointerCount() > 2) return true;
			for(int ai = 0; ai < event.getPointerCount(); ai++) {
				pointerId = event.getPointerId(ai);
				if(pointerId > 1) continue;
				float ex = event.getX(ai);
				float ey = event.getY(ai);
				if(event.getX(ai) - startPressedX[pointerId] > 20) {
					receiveSlideControl(UserControlType.RIGHT, pointerId, ex, ey);
					break;
				} else if(event.getX(ai) - startPressedX[pointerId] < -20) {
					receiveSlideControl(UserControlType.LEFT, pointerId, ex, ey);
					break;
				} else if(event.getY(ai) - startPressedY[pointerId] > 20) {
					receiveSlideControl(UserControlType.DOWN, pointerId, ex, ey);
					break;
				} else if(event.getY(ai) - startPressedY[pointerId] < -20) {
					receiveSlideControl(UserControlType.UP, pointerId, ex, ey);
					break;
				}
			}
			return true;
		}
		return false;
	}
	
	private void receiveSlideControl(UserControlType control, int pointerId, float x, float y) {
		if(useDelayedControl != null) {
			useDelayedControl.cancel();
		}
		pressedControl = control;
		obtainedControl = control;
		startPressedY[pointerId] = y;
		startPressedX[pointerId] = x;
		slideSenderID = pointerId;
	}
	
	protected boolean simpleControl(MotionEvent event, int x, int y) {
		if(event.getAction() == MotionEvent.ACTION_DOWN ||
			event.getAction() == MotionEvent.ACTION_MOVE) {
			pressedControl = getMoveType(event, x, y); 
			obtainedControl = pressedControl;
			return true;
		}
		if(event.getAction() == MotionEvent.ACTION_UP) {
			pressedControl = UserControlType.IDLE;
			return true;
		}
		return false;
	}
	
//	protected boolean twoHandedControl(MotionEvent event) {
//		int actionMask = event.getActionMasked();
//		int actionIndex = event.getActionIndex();
//		int pointerId = event.getPointerId(actionIndex);
//		switch(actionMask) {
//		case MotionEvent.ACTION_DOWN:
//			startPressedY[0] = event.getY();
//			if(event.getX() > getWidth() / 2) {
//				pressedControl = UserControlType.RIGHT;
//				model.controlType = pressedControl;
//			} else {
//				pressedControl = UserControlType.LEFT;
//				model.controlType = pressedControl;
//			}
//			return true;
//		case MotionEvent.ACTION_POINTER_DOWN:
//			if(event.getPointerCount() > 2) return true;
//			startPressedY[pointerId] = event.getY(actionIndex);
//			if(event.getX(actionIndex) > getWidth() / 2) {
//				pressedControl = UserControlType.RIGHT;
//				model.controlType = pressedControl;
//			} else {
//				pressedControl = UserControlType.LEFT;
//				model.controlType = pressedControl;
//			}
//			return true;
//		case MotionEvent.ACTION_POINTER_UP:
//			if(event.getPointerCount() > 2) return true;
//			int anotherPointer = event.getActionIndex() == 0 ? 1 : 0;
//			if((pressedControl != UserControlType.UP &&
//					pressedControl !=UserControlType.DOWN) ||
//					slideSenderID == pointerId) {
//				if(event.getX(anotherPointer) > getWidth() / 2) {
//					pressedControl = UserControlType.RIGHT;
//				} else {
//					pressedControl = UserControlType.LEFT;
//				}
//			}
//			return true;
//		case MotionEvent.ACTION_UP:
//			pressedControl = UserControlType.IDLE;
//			return true;
//		case MotionEvent.ACTION_MOVE:
//			if(event.getPointerCount() > 2) return true;
//			for(int ai = 0; ai < event.getPointerCount(); ai++) {
//				pointerId = event.getPointerId(ai); 
//				if(event.getY(ai) - startPressedY[pointerId] > 20) {
//					pressedControl = UserControlType.DOWN;
//					model.controlType = pressedControl;
//					startPressedY[pointerId] = event.getY(ai);
//					slideSenderID = pointerId;
//					break;
//				} else if(event.getY(ai) - startPressedY[pointerId] < -20) {
//					pressedControl = UserControlType.UP;
//					model.controlType = pressedControl;
//					startPressedY[pointerId] = event.getY(ai);
//					slideSenderID = pointerId;
//					break;
//				}
//			}
//			return true;
//		}
//		return false;
//	}
	
	protected UserControlType getMoveType(MotionEvent event, int x, int y) {
		float dX = x - event.getX(); // positive dx move left
		float dY = y - event.getY(); // positive dy move up
		// use for get control max by absolute value dx, dy
		// max(|dx|, |dy|)
		if(Math.abs(dY) > Math.abs(dX)) {
			// up or down
			if(dY < 0) {
				return UserControlType.DOWN;
			} else {
				return UserControlType.UP;
			}
		} else {
			// left or right
			if(dX < 0) {
				return UserControlType.RIGHT;
			} else {
				return UserControlType.LEFT;
			}
		}
	}

//	protected boolean processServiceButton(MotionEvent event) {
//        ServiceButtons.Controls serviceAction;
//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            serviceAction = view.serviceButtons.getServiceAction(event);
//            switch (serviceAction) {
//                case PAUSE_RESUME:
//                    if(isRunning()) {
//                        stopManager();
//                    } else {
//                        startManager();
//                    }
//                    return true;
//                case RESTART:
//                    stopManager();
//                    view.initSurface();
//                    startManager();
//                    return true;
//                case BACK:
//                    ((GameActivity) view.getContext()).switchBackToChooseActivity(view.isComplete(), view.getScore());
//                    return true;
//            }
//        }
//		return false;
//	}

    protected void restartGame() {
        stopManager();
        view.initSurface();
        startManager();
    }
	
	protected boolean isRunning() {
		return gameLoopThread.isRunning();
	}
	
	protected void startManager() {
        if(gameLoopThread != null && gameLoopThread.isRunning()) return;
		System.out.println("Start game loop");
		gameLoopThread = new GameManager(view);
		gameLoopThread.setRunning(true);
		gameLoopThread.start();
		paused = false;
	}
	
	protected void stopManager() {
		if(gameLoopThread == null) return;
		System.out.println("Stop game loop");
		paused = true;
		boolean retry = true;
        gameLoopThread.setRunning(false);
        while (retry) {
           try {
               gameLoopThread.join();
               retry = false;
           } catch (InterruptedException e) {
               System.out.println("Interrupted exception: " + e);
           }
        }
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
            if(obtainedControl == UserControlType.IDLE) {
                controlType = pressedControl;
            } else {
                controlType = obtainedControl;
                obtainedControl = UserControlType.IDLE;
            }
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
                ((GameActivity) view.getContext()).stopTutorial();
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
}
