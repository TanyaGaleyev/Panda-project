package org.ivan.simple.hero;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.MotionType;
import org.ivan.simple.R;

import android.graphics.Canvas;

public class Hero {
	/**
	 * Save prev motion after set changed. Prev motion used to get proper animation.
	 * For example, if prev motion was STEP_LEFT and next motion will be STAY,
	 * Panda schould turn 90 degrees right in air while jumping on place.
	 */
	private MotionType prevMotion = MotionType.NONE;
	private Sprite sprite8 = new Sprite(ImageProvider.getBitmap(R.drawable.panda_sprite8), 15, 8);
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
	
	/**
	 * Check if hero is in control state: ready for begin new motion type.
	 * Used each game loop iteration to know is it time to process user controls
	 * and achieve new motion type.
	 * More often there is control state when next frame is first frame of animation.   
	 * @return
	 */
	public boolean isInControlState() {
		switch(prevMotion) {
		case NONE:
		case STAY:
		case FALL_BLANSH:
		case STEP_LEFT_WALL:
		case STEP_RIGHT_WALL:
			return activeSprite.currentFrame == 0;
		default:
			return activeSprite.currentFrame % 8 == 0;
		}
	}
	
	/**
	 * Change hero behavior (animation) depending on motion type.
	 * Used after new motion type is obtained. 
	 * @param mt
	 */
	public void changeSet(MotionType mt) {
		pickActiveSprite(mt);
		switch (mt) {
		case STAY:
			if(prevMotion == MotionType.STEP_LEFT || prevMotion == MotionType.JUMP_LEFT) {
				activeSprite.changeSet(1);
			} else if(prevMotion == MotionType.STEP_RIGHT || prevMotion == MotionType.JUMP_RIGHT) {
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
		case STEP_LEFT:
			if(prevMotion == mt) {
				activeSprite.changeSet(2);
			} else {
				activeSprite.changeSet(3);
			}
			break;
		case JUMP_LEFT:
			activeSprite.changeSet(8);
			break;
		case STEP_RIGHT:
			if(prevMotion == mt) {
				activeSprite.changeSet(0);
			} else {
				activeSprite.changeSet(1);
			}
			break;
		case JUMP_RIGHT:
			activeSprite.changeSet(7);
			break;
		case PRE_JUMP:
			activeSprite.changeSet(9);
			break;
		case JUMP:
			activeSprite.changeSet(4);
			break;
		case STEP_LEFT_WALL:
			activeSprite.changeSet(5);
			break;
		case STEP_RIGHT_WALL:
			activeSprite.changeSet(4);
			break;
		case JUMP_LEFT_WALL:
			activeSprite.changeSet(12);
			break;
		case JUMP_RIGHT_WALL:
			activeSprite.changeSet(11);
			break;	
		case BEAT_ROOF:
			activeSprite.changeSet(10);
			break;
		case MAGNET:
			activeSprite.changeSet(14);
			break;
		case PRE_MAGNET:
			activeSprite.changeSet(13);
			break;	
		default:
			activeSprite.changeSet(4);
			break;
		}
		prevMotion = mt;
	}
	
	private void pickActiveSprite(MotionType mt) {
		switch(mt) {
		case NONE:
		case STAY:
		case FALL_BLANSH:
		case STEP_LEFT_WALL:
		case STEP_RIGHT_WALL:
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
}
