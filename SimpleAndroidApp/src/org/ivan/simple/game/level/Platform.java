package org.ivan.simple.game.level;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.R;
import org.ivan.simple.game.MotionType;
import org.ivan.simple.game.hero.Sprite;

import android.graphics.Canvas;

public class Platform {
	private PlatformType type = PlatformType.NONE;
	private Sprite sprite = null;
	private int currentStatus = 0;
	private static int switchHelper = 0;

	public Platform(PlatformType type) {
		if(type == null) return;
		this.type = type;
		switch(type) {
		case SIMPLE:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.simple_platform), 4, 8);
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
			
		case THROW_OUT_RIGHT:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.throw_out_platform_right), 1, 8);
			break;
		case THROW_OUT_LEFT:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.throw_out_platform_left), 1, 8);
			break;	
		case TRAMPOLINE:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.trampoline_platform),1,12);
			break;
		case ELECTRO:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.electro_platform),1,4);
			sprite.setAnimating(true);
			sprite.playOnce = false;
			break;
		case SPRING:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.spring_platform),2,16);
			break;
		case SPIKE:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.spike),1,8);
			sprite.setAnimating(true);
			sprite.playOnce = false;
			break;
		case SPIKE_V:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.spike_v), 1, 8);
			sprite.setAnimating(true);
			sprite.playOnce = false;
			break;
		case WIN:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.win_platform), 1, 1);
			break;
		case TELEPORT_L_V:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.teleport_l_v), 1, 16);
			sprite.setAnimating(true);
			sprite.playOnce = false;
			break;
		case TELEPORT_R_V:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.teleport_r_v), 1, 16);
			sprite.setAnimating(true);
			sprite.playOnce = false;
			break;
		case SLICK:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.slick), 1, 1);
			break;
		case SLOPE:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.slope), 3, 1);
			break;
		case ONE_WAY_LEFT:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.one_way_right), 1, 16);
			break;
		case ONE_WAY_RIGHT:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.one_way_left), 1, 16);
			break;
		case ONE_WAY_DOWN:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.one_way_down), 1, 16);
			break;
		case ONE_WAY_UP:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.one_way_up), 1, 16);
			break;
		case SWITCH:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.switch_platform), 4, 8, switchHelper);
			currentStatus = switchHelper;
			switchHelper = (switchHelper + 1) % 4;
			break;
		case UNLOCK:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.unlock_platform), 1, 1);
			break;
		case STRING:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.string_platform), 1, 16);
			break;
		case LIMIT:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.limit_way), 4, 8);
			break;
		case BRICK:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.brick), 4, 1);
			break;
		case BRICK_V:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.brick_v), 4, 1);
			break;
		case GLUE:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.glue), 1, 1);
			break;
		case GLUE_V:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.glue_v), 1, 1);
			break;
		case TELEPORT:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.teleport), 1, 1);
			break;
		case INVISIBLE:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.invisible_platform), 2, 8);
			break;
		case TRANSPARENT:
			sprite = new Sprite(ImageProvider.getBitmap(R.drawable.transparent_platform), 1, 8);
			break;
		case NONE:
			break;
		}
	}
	
	public PlatformType getType() {
		return type;
	}
	
	public void changeSet(MotionType mt) {
		if(sprite == null || 
				mt == MotionType.MAGNET || 
				mt == MotionType.BEAT_ROOF || 
				mt == MotionType.THROW_LEFT &&  this.getType()!=PlatformType.THROW_OUT_LEFT|| 
				mt == MotionType.THROW_RIGHT && this.getType()!=PlatformType.THROW_OUT_RIGHT || 
				mt == MotionType.JUMP && mt.getStage() != 0 ||
				mt == MotionType.FLY_LEFT ||
				mt == MotionType.FLY_RIGHT ||
				mt == MotionType.JUMP_LEFT_WALL ||
				mt == MotionType.JUMP_RIGHT_WALL) return;
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
		if(type == PlatformType.SLOPE) {
			switch(mt) {
			case JUMP_LEFT:
				currentStatus = 1;
				sprite.changeSet(1);
				break;
			case JUMP_RIGHT:
				currentStatus = 2;
				sprite.changeSet(2);
				break;
			default:
				break;
			}
			return;
		}
		if(type == PlatformType.SIMPLE) {
			switch(mt) {
	//		case STEP_LEFT:
	//			sprite.setAnimating(sprite.changeSet(1));
	//			sprite.playOnce = true;
	//			break;
	//		case STEP_RIGHT:
	//			sprite.setAnimating(sprite.changeSet(2));
	//			sprite.playOnce = true;
	//			break;
			case JUMP_LEFT:
				sprite.setAnimating(sprite.changeSet(1));
				sprite.playOnce = true;
				break;
			case JUMP_RIGHT:
				sprite.setAnimating(sprite.changeSet(2));
				sprite.playOnce = true;
				break;
			case JUMP:
			default:
				sprite.setAnimating(sprite.changeSet(0));
				sprite.playOnce = true;;
				break;
			}
			return;
		}
		if(type == PlatformType.ONE_WAY_DOWN && mt == MotionType.FALL) {
			sprite.setAnimating(true);
			sprite.playOnce = true;
			return;
		}
		if(type == PlatformType.STRING && mt == MotionType.STAY) {
			sprite.setAnimating(true);
			sprite.playOnce = true;
			type = PlatformType.NONE;
			return;
		}
		if(type == PlatformType.NONE) {
			sprite = null;
			return;
		}
		if(type == PlatformType.INVISIBLE) {
			sprite.changeSet(1);
			sprite.setAnimating(true);
			sprite.playOnce = true;
			return;
		}
	}
	
	public void updateRoof(MotionType mt) {
		if(mt == MotionType.BEAT_ROOF) {
			if(type == PlatformType.SIMPLE) {
				sprite.setAnimating(sprite.changeSet(3));
				sprite.playOnce = true;
			}
			if(type == PlatformType.BRICK) {
				if(currentStatus < 3) {
					currentStatus++;
					sprite.changeSet(currentStatus);
				} else {
					sprite = null;
					type = PlatformType.NONE;
				}
			}
			if(type == PlatformType.INVISIBLE) {
				sprite.changeSet(1);
				sprite.setAnimating(true);
				sprite.playOnce = true;
			}
		}
		if(mt == MotionType.JUMP && mt.getStage() != 0 && type == PlatformType.ONE_WAY_UP) {
			sprite.setAnimating(true);
			sprite.playOnce = true;
		}
	}
	
	public void highlightSpring(MotionType prevMt) {
		if(type == PlatformType.SPRING) {
			switch(prevMt) {
			case JUMP:
			case FLY_LEFT:
			case FLY_RIGHT:
			case TP:
				sprite.changeSet(0);
				break;
			default:
				sprite.changeSet(1);
				break;
			}
			sprite.setAnimating(true);
			sprite.playOnce = true;
		}
	}
	
	public void updateLeftWall(MotionType mt, MotionType prevMt) {
		if(type == PlatformType.NONE) {
			sprite = null;
		}
		switch(mt) {
		case JUMP_LEFT:
		case FLY_LEFT:
		case THROW_LEFT:
		case JUMP_LEFT_WALL:
			if(type == PlatformType.ONE_WAY_LEFT) {
				sprite.setAnimating(true);
				sprite.playOnce = true;
			}
			if(type == PlatformType.LIMIT && currentStatus < 3) {
				currentStatus = currentStatus + 1;
				sprite.changeSet(currentStatus);
				sprite.gotoAndStop(1);
				sprite.setAnimating(true);
				sprite.playOnce = true;
			}
			if(type == PlatformType.TRANSPARENT) {
				sprite.setAnimating(true);
				sprite.playOnce = true;
				type = PlatformType.NONE;
			}
			if(type == PlatformType.SWITCH) {
				currentStatus = (currentStatus + 1) % 4;
				sprite.changeSet(currentStatus);
				sprite.gotoAndStop(1);
				sprite.setAnimating(true);
				sprite.playOnce = true;
			}
			if(type == PlatformType.BRICK_V) {
				if(currentStatus < 3) {
					currentStatus++;
					sprite.changeSet(currentStatus);
				} else {
					sprite = null;
					type = PlatformType.NONE;
				}
			}
			break;
		default:
			break;
		}
	}
	
	public void updateRightWall(MotionType mt, MotionType prevMt) {
		if(type == PlatformType.NONE) {
			sprite = null;
		}
		switch(mt) {
		case JUMP_RIGHT:
		case FLY_RIGHT:
		case THROW_RIGHT:
		case JUMP_RIGHT_WALL:
			if(type == PlatformType.ONE_WAY_RIGHT) {
				sprite.setAnimating(true);
				sprite.playOnce = true;
			}
			if(type == PlatformType.LIMIT && currentStatus < 3) {
				currentStatus = currentStatus + 1;
				sprite.changeSet(currentStatus);
				sprite.gotoAndStop(1);
				sprite.setAnimating(true);
				sprite.playOnce = true;
			}
			if(type == PlatformType.TRANSPARENT) {
				sprite.setAnimating(true);
				sprite.playOnce = true;
				type = PlatformType.NONE;
			}
			if(type == PlatformType.SWITCH) {
				currentStatus = (currentStatus + 1) % 4;
				sprite.changeSet(currentStatus);
				sprite.gotoAndStop(1);
				sprite.setAnimating(true);
				sprite.playOnce = true;
			}
			if(type == PlatformType.BRICK_V) {
				if(currentStatus < 3) {
					currentStatus++;
					sprite.changeSet(currentStatus);
				} else {
					sprite = null;
					type = PlatformType.NONE;
				}
			}
			break;
		default:
			break;
		}
	}
	
	public void unlock() {
		sprite = null;
		type = PlatformType.NONE;
	}
	
	public int getStatus() {
		return currentStatus;
	}
	
	public void onDraw(Canvas canvas, int x, int y, boolean update) {
		if(sprite != null) {
			sprite.onDraw(canvas, x, y, update);
		}
	}
}