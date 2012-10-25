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
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.simple_platform), 3, 8);
			break;
		case SIMPLE_V:
			sprite  = new Sprite(ImageProvider.getBitmap(R.drawable.simple_platform_v), 1, 1);
			break;
		case REDUCE:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.reduce_platform), 1, 4);
			break;
		case ANGLE_RIGHT:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.angle_platform_right), 1, 8);
			break;
		case ANGLE_LEFT:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.angle_platform_left), 1, 8);
			break;
			
		case TROW_OUT_RIGHT:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.throw_out_platform_right), 1, 8);
			break;
		case TROW_OUT_LEFT:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.throw_out_platform_left), 1, 8);
			break;	
		case TRAMPOLINE:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.trampoline_platform),1,18);
			break;
		case ELECTRO:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.electro_platform),1,4);
			sprite.setAnimating(true);
			sprite.playOnce = false;
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
	
	public void changeSet(MotionType mt) {
		if(sprite == null || mt == MotionType.MAGNET || mt == MotionType.BEAT_ROOF || mt == MotionType.TROW_LEFT &&  this.getType()!=PlatformType.TROW_OUT_LEFT|| mt == MotionType.TROW_RIGHT && this.getType()!=PlatformType.TROW_OUT_RIGHT || (mt == MotionType.JUMP && mt.getStage() != 0)) return;
		if(type == PlatformType.REDUCE) {
			if(currentStatus<3) {
				currentStatus++;
				sprite.gotoAndStop(currentStatus);
			} else if(currentStatus<4){
				currentStatus++;
				sprite = null;
				type = PlatformType.NONE;
			}
			return;
		}
		if(type == PlatformType.ANGLE_LEFT) {
			sprite.setAnimating(true);
			sprite.playOnce = true;
			return;
		}
		if(type == PlatformType.ANGLE_RIGHT) {
			sprite.setAnimating(true);
			sprite.playOnce = true;
			return;
		}
		if(type == PlatformType.TRAMPOLINE) {
			sprite.setAnimating(true);
			sprite.playOnce = true;
			return;
		}
		switch(mt) {
		case STEP_LEFT:
			sprite.setAnimating(sprite.changeSet(1));
			sprite.playOnce = true;
			break;
		case STEP_RIGHT:
			sprite.setAnimating(sprite.changeSet(2));
			sprite.playOnce = true;
			break;
		case JUMP:
//			sprite.setAnimating(false);
//			break;
		default:
			sprite.setAnimating(sprite.changeSet(0));
			sprite.playOnce = true;;
			break;
		}
	}
}