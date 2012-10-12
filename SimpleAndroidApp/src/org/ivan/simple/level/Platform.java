package org.ivan.simple.level;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.MotionType;
import org.ivan.simple.R;
import org.ivan.simple.hero.Sprite;

public class Platform {
	private PlatformType type = PlatformType.NONE;
	private Sprite sprite = null;
	private int currentStatus = 0;

	public Platform(PlatformType type) {
		if(type == null) return;
		this.type = type;
		switch(type) {
		case SIMPLE:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.simple_platform), 2, 8);
			break;
		case SIMPLE_V:
			sprite  = new Sprite(ImageProvider.getBitmap(R.drawable.simple_platform_v), 1, 1);
			break;
		case REDUCE:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.reduce_platform), 1, 4);
			break;
		case ANGLE_RIGHT:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.angle_platform_right), 1, 1);
			break;
		case ANGLE_LEFT:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.angle_platform_left), 1, 1);
			break;	
		case NONE:
			break;
		}
	}
	
	public PlatformType getType() {
		return type;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void changePlatform() {
		if(type != PlatformType.REDUCE) return;
		if(currentStatus<3) {
			currentStatus++;
			sprite.gotoAndStop(currentStatus);
		} else if(currentStatus<4){
			currentStatus++;
			sprite = null;
			type = PlatformType.NONE;
		} else {
			
		}
	}
	
	public void changeSet(MotionType mt) {
		if(sprite == null || type == PlatformType.REDUCE) return;
		switch(mt) {
		case STEP_LEFT:
			sprite.setAnimating(sprite.changeSet(0));
			sprite.playOnce = true;
			break;
		case STEP_RIGHT:
			sprite.setAnimating(sprite.changeSet(1));
			sprite.playOnce = true;
			break;
		default:
			sprite.changeSet(0);
			sprite.setAnimating(false);
			break;
		}
	}
}