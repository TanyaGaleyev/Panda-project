package org.ivan.simple.hero;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.MotionType;
import org.ivan.simple.R;
import org.ivan.simple.UserControlType;

import android.graphics.Canvas;

public class Hero {
	/**
	 * Save prev motion after set changed. Prev motion used to get proper animation.
	 * For example, if prev motion was STEP_LEFT and next motion will be STAY,
	 * Panda schould turn 90 degrees right in air while jumping on place.
	 */
	private MotionType currentMotion = MotionType.NONE;
	private MotionType finishingMotion = MotionType.NONE;
	private Sprite sprite8 = new Sprite(ImageProvider.getBitmap(R.drawable.panda_sprite8), 16, 8);
	private Sprite sprite16 = new Sprite(ImageProvider.getBitmap(R.drawable.panda_sprite16), 6, 16);
	private Sprite activeSprite;
	
	public Hero() {
		sprite16.setAnimating(true);
		sprite8.setAnimating(true);
		activeSprite = sprite16;
	}
	
	public Sprite getSprite() {
		return activeSprite;
	}
	
	public boolean isFinishing() {
		return finishingMotion.isFinishing();
	}
	
	public boolean isStarting() {
		return currentMotion.isStarting();
	}
	
	public boolean tryToEndFinishMotion() {
		if(activeSprite.currentFrame == 0) {
			// convert motion to initial stage (stage == 0)
			finishingMotion.startMotion();
			switchToCurrentMotion();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean tryToEndStartMotion() {
		if(activeSprite.currentFrame == 0) {
			currentMotion.continueMotion();
			switchToCurrentMotion();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Check if hero is in control state: ready for begin new motion type.
	 * Used each game loop iteration to know is it time to process user controls
	 * and achieve new motion type.
	 * More often there is control state when next frame is first frame of animation.   
	 * @return
	 */
	public boolean isInControlState() {
//		if(!finishingMotion.isFinishing()) {
//			switch(currentMotion) {
//			case NONE:
//			case STAY:
//			case FALL_BLANSH:
//			case STEP_LEFT_WALL:
//			case STEP_RIGHT_WALL:
				return activeSprite.currentFrame == 0;
//			default:
//				return activeSprite.currentFrame % 8 == 0;
//			}
//		} else if(activeSprite.currentFrame == 0) {
//			finishingMotion = MotionType.NONE;
//			switchToCurrentMotion();
//			return false;
//		}
//		return false;
	}
	
	/**
	 * Change hero behavior (animation) depending on motion type.
	 * Used after new motion type is obtained. 
	 * @param newMotion
	 */
	public void changeMotion(MotionType newMotion) {
		startFinishMotions(newMotion);
		if (finishingMotion.isFinishing()) {
			switch(finishingMotion) {
			case MAGNET:
				activeSprite.changeSet(15);
				break;
			}
		} else if(currentMotion.isStarting()) {
			pickActiveSprite(currentMotion);
			switch(currentMotion) {
			case JUMP:
				activeSprite.changeSet(9);
				break;
			}
		} else {
			switchToCurrentMotion();
		}
	}
	
	public void switchToCurrentMotion() {
		pickActiveSprite(currentMotion);
		switch (currentMotion) {
		case STAY:
			if(/*finishingMotion == MotionType.STEP_LEFT || */finishingMotion == MotionType.JUMP_LEFT) {
				activeSprite.changeSet(1);
			} else if(/*finishingMotion == MotionType.STEP_RIGHT || */finishingMotion == MotionType.JUMP_RIGHT) {
				activeSprite.changeSet(2);
			} else {
				activeSprite.changeSet(0);
			}
			break;
		case FALL:
			if(Math.random() > 0.5) {
				activeSprite.changeSet(5);
			} else {
				activeSprite.changeSet(6);
			}
			break;
		case FALL_BLANSH:
			activeSprite.changeSet(3);
			break;
//		case STEP_LEFT:
//			if(finishingMotion == currentMotion || finishingMotion == MotionType.JUMP_LEFT) {
//				activeSprite.changeSet(2);
//			} else {
//				activeSprite.changeSet(3);
//			}
//			break;
		case JUMP_LEFT:
			if(finishingMotion == MotionType.JUMP) {
				activeSprite.changeSet(8);
			} else if(finishingMotion == currentMotion || finishingMotion == MotionType.JUMP_LEFT) {
				activeSprite.changeSet(2);
			} else {
				activeSprite.changeSet(3);
			}
			break;
//		case STEP_RIGHT:
//			if(finishingMotion == currentMotion || finishingMotion == MotionType.JUMP_RIGHT) {
//				activeSprite.changeSet(0);
//			} else {
//				activeSprite.changeSet(1);
//			}
//			break;
		case JUMP_RIGHT:
			if(finishingMotion == MotionType.JUMP) {
				activeSprite.changeSet(7);
			} else if(finishingMotion == currentMotion || finishingMotion == MotionType.JUMP_RIGHT) {
				activeSprite.changeSet(0);
			} else {
				activeSprite.changeSet(1);
			}
			break;
		case PRE_JUMP:
			activeSprite.changeSet(9);
			break;
		case JUMP:
			if(finishingMotion == currentMotion) {
				activeSprite.changeSet(4);
			} else {
				activeSprite.changeSet(9);
			}
			break;
		case TROW_LEFT:
		case TROW_RIGHT:
			activeSprite.changeSet(15);
			break;
//		case STEP_LEFT_WALL:
//			activeSprite.changeSet(5);
//			break;
//		case STEP_RIGHT_WALL:
//			activeSprite.changeSet(4);
//			break;
		case JUMP_LEFT_WALL:
			switch(finishingMotion) {
			case JUMP:
			case TROW_LEFT:
			case TROW_RIGHT:
			case FLY_LEFT:
			case FLY_RIGHT:
				activeSprite.changeSet(12);
				break;
			default:
				activeSprite = sprite16;
				activeSprite.changeSet(5);
				break;
			}
			break;
		case JUMP_RIGHT_WALL:
			switch(finishingMotion) {
			case JUMP:
			case TROW_LEFT:
			case TROW_RIGHT:
			case FLY_LEFT:
			case FLY_RIGHT:
				activeSprite.changeSet(11);
				break;
			default:
				activeSprite = sprite16;
				activeSprite.changeSet(4);
				break;
			}
			break;	
		case BEAT_ROOF:
			activeSprite.changeSet(10);
			break;
		case MAGNET:
			if(currentMotion.getStage() == 0) {
				activeSprite.changeSet(13);
			} else {
				activeSprite.changeSet(14);
			}
			break;
		case PRE_MAGNET:
			activeSprite.changeSet(13);
			break;	
		case FLY_RIGHT:
			activeSprite.changeSet(15);
			break;
		default:
			activeSprite.changeSet(4);
			break;
		}
	}
	
	
	public void startFinishMotions(MotionType newMotion) {
		finishingMotion = currentMotion;
		currentMotion = newMotion;
		if(finishingMotion != currentMotion) {
			finishingMotion.finishMotion();
			currentMotion.startMotion();
		}
	}

	
	private void pickActiveSprite(MotionType mt) {
		switch(mt) {
		case NONE:
		case STAY:
		case FALL_BLANSH:
//		case STEP_LEFT_WALL:
//		case STEP_RIGHT_WALL:
			activeSprite = sprite16;
			break;
		default:
			activeSprite = sprite8;
			break;
		}
	}
	
	public void onDraw(Canvas canvas, int x, int y) {
		activeSprite.onDraw(canvas, x - activeSprite.getWidth() / 2, y - activeSprite.getHeight() / 2);
	}
	
	public MotionType getRealMotion() {
		if(currentMotion.isStarting()) {
			return MotionType.NONE;
		} else if(finishingMotion.isFinishing()) {
			return finishingMotion;
		} else {
			return currentMotion;
		}
	}
}
