package org.ivan.simple.game.hero;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.R;
import org.ivan.simple.game.Motion;
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
	private boolean finishingState = false;
	private LevelCell prevCell;
	private Sprite sprite8 = new Sprite(ImageProvider.getBitmap(R.drawable.panda_sprite8), 38, 8);
	private Sprite sprite16 = new Sprite(ImageProvider.getBitmap(R.drawable.panda_sprite16), 9, 16);
	private Sprite tpSprite = new Sprite(ImageProvider.getBitmap(R.drawable.panda_tp), 28, 8);
	private Sprite activeSprite;
	private Sprite shadeSprite = new Sprite(ImageProvider.getBitmap(R.drawable.panda_tp), 28, 8);
	public int x;
	public int y;
	private int prevX;
	private int prevY;
	public final HeroModel model;
	
	public Hero(HeroModel model) {
		this.model = model;
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
		return finishingState;
	}
	
	/* properly 'was', 'has been'? */
	public boolean isFinishingMotionEnded(/*Motion prevMotion*/) {
		if(activeSprite.getFrame() == 0 ||
				(model.finishingMotion.getType() == MotionType.FLY_LEFT && activeSprite.getFrame() == 4) ||
				(model.finishingMotion.getType() == MotionType.FLY_RIGHT && activeSprite.getFrame() == 4)) {
			finishingState = false;
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
		if((model.finishingMotion.getType() == MotionType.FLY_LEFT  ||
				model.finishingMotion.getType() == MotionType.FLY_RIGHT) &&
				finishingState) {
			return activeSprite.getFrame() == 4;
		}
		if(model.currentMotion.getType() == MotionType.FALL_BLANSH) {
			return activeSprite.getFrame() % 8 == 0;
		}
		if(prevCell != null && prevCell.getFloor().getType() == PlatformType.GLUE) {
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
	public void finishPrevMotion(LevelCell prevCell) {
		this.prevCell = prevCell;
		prevX = x;
		prevY = y;
//		model.finishingMotion = prevMotion;
//		model.currentMotion = newMotion;
		if (model.finishingMotion.getChildMotion().isFinishing()) {
			finishingState = true;
			switch(model.finishingMotion.getChildMotion().getType()) {
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
				if(model.currentMotion.getType() == MotionType.JUMP_LEFT_WALL || 
					model.currentMotion.getType() == MotionType.STICK_LEFT ||
					model.currentMotion.getType() == MotionType.FLY_RIGHT) {
					finishingState = false;
				} else {
					activeSprite = sprite8;
					activeSprite.changeSet(26);
				}
				break;
			case FLY_RIGHT:
				// skip finishing fall down after FLY if finish because wall
				if(model.currentMotion.getType() == MotionType.JUMP_RIGHT_WALL || 
					model.currentMotion.getType() == MotionType.STICK_RIGHT ||
					model.currentMotion.getType() == MotionType.FLY_LEFT) {
					finishingState = false;
				} else {
					activeSprite = sprite8;
					activeSprite.changeSet(26);
				}
				break;
			default:
				break;
			}
		} else {
			finishingState = false;
		}
	}
	
	/**
	 * Begins main animation, after finish/start animations became complete
	 */
	public void switchToCurrentMotion() {
		pickActiveSprite(model.currentMotion.getType());
		MotionType mt = model.currentMotion.getType();
		int curStage = model.currentMotion.getStage();
		MotionType prevMt = model.finishingMotion.getChildMotion().getType();
//		if(prevMt == MotionType.TP_LEFT || prevMt == MotionType.TP_RIGHT) {
//			prevMt = finishingMotion.getChildMotion().getType();
//			prevStage = finishingMotion.getChildMotion().getStage();
//		}
//		if(mt == MotionType.TP_LEFT || mt == MotionType.TP_RIGHT) {
//			prevMt = currentMotion.getChildMotion().getType();
//			prevStage = currentMotion.getChildMotion().getStage();
//		}
		switch (mt) {
		case STAY:
			if(prevCell.getFloor().getType() == PlatformType.GLUE){
				activeSprite.changeSet(8);
			} else if(prevMt == MotionType.THROW_LEFT || 
					prevMt == MotionType.JUMP_LEFT ||
					prevMt == MotionType.TP_LEFT) {
				activeSprite.changeSet(1);
			} else if(prevMt == MotionType.THROW_RIGHT || 
					prevMt == MotionType.JUMP_RIGHT ||
					prevMt == MotionType.TP_RIGHT) {
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
			if(curStage == 0) {
				activeSprite.changeSet(3);
			}
			break;
		case JUMP_LEFT:
			if(prevMt == MotionType.JUMP || prevMt == MotionType.TP) {
				activeSprite.changeSet(8);
			} else if(prevMt == mt || 
					prevMt == MotionType.THROW_LEFT ||
					prevMt == MotionType.TP_LEFT) {
				activeSprite.changeSet(2);
			} else {
				activeSprite.changeSet(3);
			}
			break;
		case JUMP_RIGHT:
			if(prevMt == MotionType.JUMP || prevMt == MotionType.TP) {
				activeSprite.changeSet(7);
			} else if(prevMt == mt || 
					prevMt == MotionType.THROW_RIGHT ||
					prevMt == MotionType.TP_RIGHT) {
				activeSprite.changeSet(0);
			} else {
				activeSprite.changeSet(1);
			}
			break;
		case JUMP:
			if(curStage != 0) {
				activeSprite.changeSet(4);
			} else {
				activeSprite.changeSet(9);
			}
			break;
		case THROW_LEFT:
			if(curStage == 1) {
				activeSprite.changeSet(24);
			} else if(prevMt == MotionType.JUMP_LEFT || prevMt == MotionType.THROW_LEFT) {
				activeSprite.changeSet(21);
			} else {
				activeSprite.changeSet(20);
			}
			break;
		case THROW_RIGHT:
			if(curStage == 1) {
				activeSprite.changeSet(25);
			} else if(prevMt == MotionType.JUMP_RIGHT || prevMt == MotionType.THROW_RIGHT) {
				activeSprite.changeSet(19);
			} else {
				activeSprite.changeSet(18);
			}
			break;
		case JUMP_LEFT_WALL:
			switch(prevMt) {
			case JUMP:
			case THROW_LEFT:
			case FLY_LEFT:
			case TP:
//			case JUMP_RIGHT_WALL: 
				activeSprite.changeSet(12);
				break;
			default:
				if(prevCell.getFloor().getType() == PlatformType.THROW_OUT_LEFT) {
					activeSprite.changeSet(12);
				} else {
					activeSprite = sprite16;
					activeSprite.changeSet(5);
				}
				break;
			}
			break;
		case JUMP_RIGHT_WALL:
			switch(prevMt) {
			case JUMP:
			case THROW_RIGHT:
			case FLY_RIGHT:
			case TP:
//			case JUMP_LEFT_WALL:
				activeSprite.changeSet(11);
				break;
			default:
				if(prevCell.getFloor().getType() == PlatformType.THROW_OUT_RIGHT) {
					activeSprite.changeSet(11);
				} else {
					activeSprite = sprite16;
					activeSprite.changeSet(4);
				}
				break;
			}
			break;	
		case BEAT_ROOF:
			activeSprite.changeSet(10);
			break;
		case MAGNET:
			if(curStage == 0) {
				activeSprite.changeSet(13);
			} else {
				activeSprite.changeSet(14);
			}
			break;
		case FLY_LEFT:
			if(prevMt == mt || prevMt == MotionType.TP_LEFT) {
				activeSprite.changeSet(22);
			} else if(prevMt == MotionType.JUMP || prevMt == MotionType.FLY_RIGHT ||
					prevMt == MotionType.TP || prevMt == MotionType.THROW_RIGHT || 
					prevCell.getFloor().getType() == PlatformType.THROW_OUT_RIGHT) {
				activeSprite.changeSet(11);
			} else {
				activeSprite = sprite16;
				activeSprite.changeSet(7);
			}
			break;
		case FLY_RIGHT:
			if(prevMt == mt || prevMt == MotionType.TP_RIGHT) {
				activeSprite.changeSet(23);
			} else if(prevMt == MotionType.JUMP || prevMt == MotionType.FLY_LEFT ||
					prevMt == MotionType.TP || prevMt == MotionType.THROW_LEFT ||
					prevCell.getFloor().getType() == PlatformType.THROW_OUT_LEFT) {
				activeSprite.changeSet(12);
			} else {
				activeSprite = sprite16;
				activeSprite.changeSet(6);
			}
			break;
		case TP_LEFT:
			MotionType childMt = model.currentMotion.getChildMotion().getType();
			int childStage = model.currentMotion.getChildMotion().getStage();
			if(childMt == MotionType.JUMP_LEFT) {
				if(prevMt == MotionType.JUMP) {
					shadeSprite.changeSet(10);
					tpSprite.changeSet(11);
				} else if(prevMt == MotionType.JUMP_LEFT || prevMt == MotionType.THROW_LEFT) {
					shadeSprite.changeSet(2);
					tpSprite.changeSet(3);
				} else {
					shadeSprite.changeSet(6);
					tpSprite.changeSet(7);
				}
			} else if(childMt == MotionType.THROW_LEFT && childStage == 0) {
				if(prevMt == MotionType.THROW_LEFT || prevMt == MotionType.JUMP_LEFT) {
					shadeSprite.changeSet(22);
					tpSprite.changeSet(23);
				} else {
					shadeSprite.changeSet(18);
					tpSprite.changeSet(19);
				}
			} else if(childMt == MotionType.THROW_LEFT) {
				shadeSprite.changeSet(26);
				tpSprite.changeSet(27);
			} else if(childMt == MotionType.FLY_LEFT) {
				shadeSprite.changeSet(14);
				tpSprite.changeSet(15);
			} else {
				shadeSprite.changeSet(6);
				tpSprite.changeSet(7);
			}
			break;
		case TP_RIGHT:
			MotionType childMt1 = model.currentMotion.getChildMotion().getType();
			int childStage1 = model.currentMotion.getChildMotion().getStage();
			if(childMt1 == MotionType.JUMP_RIGHT) {
				if(prevMt == MotionType.JUMP) {
					shadeSprite.changeSet(8);
					tpSprite.changeSet(9);
				} else if(prevMt == MotionType.JUMP_RIGHT || prevMt == MotionType.THROW_RIGHT) {
					shadeSprite.changeSet(0);
					tpSprite.changeSet(1);
				} else {
					shadeSprite.changeSet(4);
					tpSprite.changeSet(5);
				}
			} else if(childMt1 == MotionType.THROW_RIGHT && childStage1 == 0) {
				if(prevMt == MotionType.THROW_RIGHT || prevMt == MotionType.JUMP_RIGHT) {
					shadeSprite.changeSet(20);
					tpSprite.changeSet(21); 
				} else {
					shadeSprite.changeSet(16);
					tpSprite.changeSet(17); 
				}
			} else if(childMt1 == MotionType.THROW_RIGHT) {
				shadeSprite.changeSet(24);
				tpSprite.changeSet(25);
			} else if(childMt1 == MotionType.FLY_RIGHT) {
				shadeSprite.changeSet(12);
				tpSprite.changeSet(13);
			} else {
				shadeSprite.changeSet(4);
				tpSprite.changeSet(5);
			}
			break;
		case STICK_LEFT:
			if(curStage == 0) {
				activeSprite.changeSet(28);
			} else {
				activeSprite.changeSet(29);
			}
			break;
		case STICK_RIGHT:
			if(curStage == 0) {
				activeSprite.changeSet(31);
			} else {
				activeSprite.changeSet(32);
			}
			break;
		case TP:
			if(curStage == 0) {
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
		activeSprite.onDraw(canvas, x - activeSprite.getWidth() / 2, y - activeSprite.getHeight() / 2, update);
		if(!finishingState && (model.currentMotion.getType() == MotionType.TP_LEFT || model.currentMotion.getType() == MotionType.TP_RIGHT)) {
			shadeSprite.onDraw(canvas, prevX - shadeSprite.getWidth() / 2, prevY - shadeSprite.getHeight() / 2, update);
		}
	}
	
	/**
	 * Real motion type used to get proper hero speed on start/finish/main animations
	 * @return
	 */
	public Motion getRealMotion() {
		if(finishingState) {
			return new Motion(MotionType.NONE);
		} else {
			return model.currentMotion;
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
