package org.ivan.simple.game.hero;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.R;
import org.ivan.simple.game.MotionType;
import org.ivan.simple.game.level.LevelCell;
import org.ivan.simple.game.level.PlatformType;

import android.graphics.Canvas;

public class Hero {
	/**
	 * Save prev motion after set changed. Prev motion used to get proper animation.
	 * For example, if prev motion was STEP_LEFT and next motion will be STAY,
	 * Panda schould turn 90 degrees right in air while jumping on place.
	 */
	private MotionType currentMotion = MotionType.NONE;
	private MotionType finishingMotion = MotionType.NONE;
	private LevelCell prevCell;
	private Sprite sprite8 = new Sprite(ImageProvider.getBitmap(R.drawable.panda_sprite8), 36, 8);
	private Sprite sprite16 = new Sprite(ImageProvider.getBitmap(R.drawable.panda_sprite16), 9, 16);
	private Sprite tpSprite = new Sprite(ImageProvider.getBitmap(R.drawable.panda_tp), 12, 8);
	private Sprite activeSprite;
	private Sprite shadeSprite = new Sprite(ImageProvider.getBitmap(R.drawable.panda_tp), 12, 8);
	public int heroX;
	public int heroY;
	private int prevX;
	private int prevY;
	
	public Hero() {
		sprite16.setAnimating(true);
		sprite8.setAnimating(true);
		tpSprite.setAnimating(true);
		activeSprite = sprite16;
		shadeSprite.setAnimating(true);
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
	
	public boolean tryToEndFinishMotion(MotionType prevMotion) {
		if(activeSprite.getFrame() == 0 ||
				(finishingMotion == MotionType.FLY_LEFT && activeSprite.getFrame() == 4) ||
				(finishingMotion == MotionType.FLY_RIGHT && activeSprite.getFrame() == 4)) {
			// turn motion to initial stage (stage == 0)
			finishingMotion.startMotion();
			finishingMotion = prevMotion;
			switchToCurrentMotion();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean tryToEndStartMotion() {
		if(activeSprite.getFrame() == 0) {
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
		if((finishingMotion == MotionType.FLY_LEFT  ||
				finishingMotion == MotionType.FLY_RIGHT) &&
				finishingMotion.isFinishing()) {
			return activeSprite.getFrame() == 4;
		}
		if(currentMotion == MotionType.FALL_BLANSH) {
			return activeSprite.getFrame() % 8 == 0;
		}

		return activeSprite.getFrame() == 0;
	}
	
	/**
	 * Change hero behavior (animation) depending on motion type.
	 * Used after new motion type is obtained. 
	 * Goal is to play start/end animations of motions.
	 * @param newMotion
	 */
	public void changeMotion(MotionType newMotion, MotionType prevMotion, LevelCell prevCell) {
		this.prevCell = prevCell;
		prevX = heroX;
		prevY = heroY;
		finishingMotion = prevMotion;
		currentMotion = newMotion;
		if (finishingMotion.isFinishing()) {
			switch(finishingMotion) {
			case MAGNET:
				activeSprite.changeSet(17);
				break;
			case STICK_LEFT:
				activeSprite.changeSet(30);
				break;
			case STICK_RIGHT:
				activeSprite.changeSet(33);
				break;
			case FLY_LEFT:
				// skip finishing fall down after FLY if finish because wall
				if(newMotion == MotionType.JUMP_LEFT_WALL|| 
				newMotion == MotionType.FLY_RIGHT ||
				newMotion == MotionType.STICK_LEFT) {
					finishingMotion.startMotion();
					switchToCurrentMotion();
				} else {
					activeSprite.changeSet(26);
				}
				break;
			case FLY_RIGHT:
				// skip finishing fall down after FLY if finish because wall
				if(newMotion == MotionType.JUMP_RIGHT_WALL || 
				newMotion == MotionType.FLY_LEFT ||
				newMotion == MotionType.STICK_RIGHT) {
					finishingMotion.startMotion();
					switchToCurrentMotion();
				} else {
					activeSprite.changeSet(26);
				}
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
	
	/**
	 * Begins main animation, after finish/start animations became complete
	 */
	private void switchToCurrentMotion() {
		pickActiveSprite(currentMotion);
		switch (currentMotion) {
		case STAY:
			if(prevCell.getFloor().getType() == PlatformType.GLUE){
				activeSprite.changeSet(8);
			} else if(finishingMotion == MotionType.THROW_LEFT || 
					finishingMotion == MotionType.JUMP_LEFT ||
					finishingMotion == MotionType.TP_LEFT) {
				activeSprite.changeSet(1);
			} else if(finishingMotion == MotionType.THROW_RIGHT || 
					finishingMotion == MotionType.JUMP_RIGHT ||
					finishingMotion == MotionType.TP_RIGHT) {
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
			if(currentMotion.getStage() == 0) {
				activeSprite.changeSet(3);
			}
			break;
//		case STEP_LEFT:
//			if(finishingMotion == currentMotion || finishingMotion == MotionType.JUMP_LEFT) {
//				activeSprite.changeSet(2);
//			} else {
//				activeSprite.changeSet(3);
//			}
//			break;
		case JUMP_LEFT:
			if(finishingMotion == MotionType.JUMP || finishingMotion == MotionType.TP) {
				activeSprite.changeSet(8);
			} else if(finishingMotion == currentMotion || 
					finishingMotion == MotionType.THROW_LEFT ||
					finishingMotion == MotionType.TP_LEFT) {
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
			if(finishingMotion == MotionType.JUMP || finishingMotion == MotionType.TP) {
				activeSprite.changeSet(7);
			} else if(finishingMotion == currentMotion || 
					finishingMotion == MotionType.THROW_RIGHT ||
					finishingMotion == MotionType.TP_RIGHT) {
				activeSprite.changeSet(0);
			} else {
				activeSprite.changeSet(1);
			}
			break;
//		case PRE_JUMP:
//			activeSprite.changeSet(9);
//			break;
		case JUMP:
			if(currentMotion.getStage() != 0) {
				activeSprite.changeSet(4);
			} else {
				activeSprite.changeSet(9);
			}
			break;
		case THROW_LEFT:
			if(currentMotion.getStage() == 1) {
				activeSprite.changeSet(24);
			} else if(finishingMotion == MotionType.JUMP_LEFT || finishingMotion == MotionType.THROW_LEFT) {
				activeSprite.changeSet(21);
			} else {
				activeSprite.changeSet(20);
			}
			break;
		case THROW_RIGHT:
			if(currentMotion.getStage() == 1) {
				activeSprite.changeSet(25);
			} else if(finishingMotion == MotionType.JUMP_RIGHT || finishingMotion == MotionType.THROW_RIGHT) {
				activeSprite.changeSet(19);
			} else {
				activeSprite.changeSet(18);
			}
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
			case THROW_LEFT:
			case FLY_LEFT:
			case TP:
//			case JUMP_RIGHT_WALL: 
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
			case THROW_RIGHT:
			case FLY_RIGHT:
			case TP:
//			case JUMP_LEFT_WALL:
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
//		case PRE_MAGNET:
//			activeSprite.changeSet(13);
//			break;	
		case FLY_LEFT:
			if(finishingMotion == currentMotion || finishingMotion == MotionType.TP_LEFT) {
				activeSprite.changeSet(22);
			} else if(finishingMotion == MotionType.JUMP || finishingMotion == MotionType.FLY_RIGHT ||
					finishingMotion == MotionType.TP) {
				activeSprite.changeSet(11);
			} else {
				activeSprite = sprite16;
				activeSprite.changeSet(7);
//				activeSprite.changeSet(11);
			}
			break;
		case FLY_RIGHT:
			if(finishingMotion == currentMotion || finishingMotion == MotionType.TP_RIGHT) {
				activeSprite.changeSet(23);
			} else if(finishingMotion == MotionType.JUMP || finishingMotion == MotionType.FLY_LEFT ||
					finishingMotion == MotionType.TP) {
				activeSprite.changeSet(12);
			} else {
				activeSprite = sprite16;
				activeSprite.changeSet(6);
//				activeSprite.changeSet(12);
			}
			break;
		case TP_LEFT:
			if(finishingMotion == MotionType.JUMP) {
				shadeSprite.changeSet(6);
				tpSprite.changeSet(7);
			} else if(finishingMotion == currentMotion || 
					finishingMotion == MotionType.THROW_LEFT ||
					finishingMotion == MotionType.TP_LEFT) {
				shadeSprite.changeSet(2);
				tpSprite.changeSet(3);
			} else if(finishingMotion == MotionType.FLY_LEFT) {
				shadeSprite.changeSet(10);
				tpSprite.changeSet(11);
			} else {
				shadeSprite.changeSet(2);
				tpSprite.changeSet(3);
			}
			break;
		case TP_RIGHT:
			if(finishingMotion == MotionType.JUMP) {
				shadeSprite.changeSet(4);
				tpSprite.changeSet(5);
			} else if(finishingMotion == currentMotion || 
					finishingMotion == MotionType.THROW_RIGHT ||
					finishingMotion == MotionType.TP_RIGHT) {
				shadeSprite.changeSet(0);
				tpSprite.changeSet(1);
			} else if(finishingMotion == MotionType.FLY_RIGHT) {
				shadeSprite.changeSet(8);
				tpSprite.changeSet(9);
			} else {
				shadeSprite.changeSet(0);
				tpSprite.changeSet(1);
			}
			break;
		case STICK_LEFT:
			if(currentMotion.getStage() == 0) {
				activeSprite.changeSet(28);
			} else {
				activeSprite.changeSet(29);
			}
			break;
		case STICK_RIGHT:
			if(currentMotion.getStage() == 0) {
				activeSprite.changeSet(31);
			} else {
				activeSprite.changeSet(32);
			}
			break;
		case TP:
			if(currentMotion.getStage() == 0) {
				activeSprite.changeSet(34);
			} else {
				activeSprite.changeSet(35);
			}
			break;
		default:
			activeSprite.changeSet(4);
			break;
		}
	}

	
	/**
	 * Used to get proper bitmap for motion
	 * @param mt
	 */
	private void pickActiveSprite(MotionType mt) {
		switch(mt) {
		case NONE:
		case STAY:
		case FALL_BLANSH:
//		case STEP_LEFT_WALL:
//		case STEP_RIGHT_WALL:
			activeSprite = sprite16;
			break;
		case TP_LEFT:
		case TP_RIGHT:
			activeSprite = tpSprite;
			break;
		default:
			activeSprite = sprite8;
			break;
		}
	}
	
	/**
	 * Draw proper hero animation frame by center coordinates
	 * @param canvas
	 */
	public void onDraw(Canvas canvas, boolean update) {
		activeSprite.onDraw(canvas, heroX - activeSprite.getWidth() / 2, heroY - activeSprite.getHeight() / 2, update);
		if(!finishingMotion.isFinishing() && (currentMotion == MotionType.TP_LEFT || currentMotion == MotionType.TP_RIGHT)) {
			shadeSprite.onDraw(canvas, prevX - shadeSprite.getWidth() / 2, prevY - shadeSprite.getHeight() / 2, update);
		}
	}
	
	/**
	 * Real motion type used to get proper hero speed on start/finish/main animations
	 * @return
	 */
	public MotionType getRealMotion() {
		if(currentMotion.isStarting()) {
			return MotionType.NONE;
		} else if(finishingMotion.isFinishing()) {
			return MotionType.NONE;
		} else {
			return currentMotion;
		}
	}
	
	/**
	 * Play loose animation
	 */
	public void playLoseAnimation() {
		activeSprite = sprite8;
		if(activeSprite.getSet() != 5) {
			activeSprite.changeSet(5);
		}
	}
	
	/**
	 * Play win animation
	 * @return is sprite animating?
	 */
	public boolean playWinAnimation() {
		activeSprite = sprite8;
		if(activeSprite.getSet() != 16) {
			activeSprite.changeSet(16);
		}
		activeSprite.setPlayOnce(true);
		if(!activeSprite.isAnimating()) {
			activeSprite.goToFrame(7);
			return false;
		}
		return true;
	}
}
