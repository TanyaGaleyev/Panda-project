package org.ivan.simple;

import java.util.Timer;
import java.util.TimerTask;

import org.ivan.simple.hero.Hero;
import org.ivan.simple.level.LevelModel;

import android.view.MotionEvent;

public class GameControl {
	private LevelModel model;
	private Hero hero;
	public UserControlType pressedControl = UserControlType.IDLE;
	public UserControlType delayedControl = UserControlType.IDLE;
	private TimerTask useDelayedControl;
	
	private float[] startPressedY = new float[2];
	private float[] startPressedX = new float[2];
	private int slideSenderID;
	
	public GameControl(LevelModel model, Hero hero) {
		this.model = model;
		this.hero = hero;
	}
	
	protected boolean oneHandControl(MotionEvent event) {
		int actionMask = event.getActionMasked();
		int actionIndex = event.getActionIndex();
		int pointerId = event.getPointerId(actionIndex);
		switch(actionMask) {
		case MotionEvent.ACTION_DOWN:
			startPressedY[0] = event.getY();
			startPressedX[0] = event.getX();
			if(event.getX() > hero.heroX) {
				delayedControl = UserControlType.RIGHT;
			} else {
				delayedControl = UserControlType.LEFT;
			}
			useDelayedControl = new TimerTask() {				
				@Override
				public void run() {
					pressedControl = delayedControl;
					model.controlType = delayedControl;
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
			if(useDelayedControl.cancel() &&
					model.controlType == UserControlType.IDLE) {
				model.controlType = delayedControl;
			}
			pressedControl = UserControlType.IDLE;
			return true;
		case MotionEvent.ACTION_MOVE:
			if(event.getPointerCount() > 2) return true;
			for(int ai = 0; ai < event.getPointerCount(); ai++) {
				pointerId = event.getPointerId(ai); 
				float x = event.getX(ai);
				float y = event.getY(ai);
				if(event.getX(ai) - startPressedX[pointerId] > 20) {
					receiveSlideControl(UserControlType.RIGHT, pointerId, x, y);
					break;
				} else if(event.getX(ai) - startPressedX[pointerId] < -20) {
					receiveSlideControl(UserControlType.LEFT, pointerId, x, y);
					break;
				} else if(event.getY(ai) - startPressedY[pointerId] > 20) {
					receiveSlideControl(UserControlType.DOWN, pointerId, x, y);
					break;
				} else if(event.getY(ai) - startPressedY[pointerId] < -20) {
					receiveSlideControl(UserControlType.UP, pointerId, x, y);
					break;
				}
			}
			return true;
		}
		return false;
	}
	
	private void receiveSlideControl(UserControlType control, int pointerId, float x, float y) {
		useDelayedControl.cancel();
		pressedControl = control;
		model.controlType = control;
		startPressedY[pointerId] = y;
		startPressedX[pointerId] = x;
		slideSenderID = pointerId;
	}
	
	protected boolean simpleControl(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN ||
			event.getAction() == MotionEvent.ACTION_MOVE) {
			pressedControl = getMoveType(event); 
			model.controlType = pressedControl;
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
	
	protected UserControlType getMoveType(MotionEvent event) {
		float dX = hero.heroX - event.getX(); // positive dx move left
		float dY = hero.heroY - event.getY(); // positive dy move up
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
}