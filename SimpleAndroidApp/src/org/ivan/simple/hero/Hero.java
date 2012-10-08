package org.ivan.simple.hero;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.MotionType;
import org.ivan.simple.R;

import android.graphics.Canvas;

public class Hero {
	private MotionType prevMotion = MotionType.NONE;
	private Sprite sprite = new Sprite(ImageProvider.getBitmap(R.drawable.ic_launcher3), 7, 16);
	
	public Hero() {
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public boolean isInControlState() {
		if(sprite.currentSet == 0) {
			return sprite.currentFrame == 0;
		}
		return sprite.currentFrame % 8 == 0;
	}
	
	public void changeSet(MotionType mt) {
		switch (mt) {
		case STAY:
			sprite.changeSet(0);
			break;
		case FALL:
			sprite.changeSet(6);
			break;
		case STEP_LEFT:
		case JUMP_LEFT:
			if(prevMotion == mt) {
				sprite.changeSet(3);
			} else {
				sprite.changeSet(4);
			}
			break;
		case STEP_RIGHT:
		case JUMP_RIGHT:
			if(prevMotion == mt) {
				sprite.changeSet(1);
			} else {
				sprite.changeSet(2);
			}
			break;
		default:
			sprite.changeSet(5);
			break;
		}
		prevMotion = mt;
	}
	
	public void onDraw(Canvas canvas, int x, int y) {
		sprite.onDraw(canvas, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2);
	}
}
