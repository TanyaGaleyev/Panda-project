package org.ivan.simple.game.level;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.R;
import org.ivan.simple.game.Motion;
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
			sprite = new Sprite(R.drawable.simple_platform, 4, 8);
			break;
		case SIMPLE_V:
			sprite  = new Sprite(R.drawable.simple_platform_v, 1, 1);
			break;
		case REDUCE:
			sprite = new Sprite(R.drawable.reduce_platform, 4, 8);
			break;
		case ANGLE_RIGHT:
			sprite = new Sprite(R.drawable.angle_platform_right, 1, 8);
			break;
		case ANGLE_LEFT:
			sprite = new Sprite(R.drawable.angle_platform_left, 1, 8);
			break;
			
		case THROW_OUT_RIGHT:
			sprite = new Sprite(R.drawable.throw_out_platform_right, 1, 8);
			break;
		case THROW_OUT_LEFT:
			sprite = new Sprite(R.drawable.throw_out_platform_left, 1, 8);
			break;	
		case TRAMPOLINE:
			sprite = new Sprite(R.drawable.trampoline_platform,1,6);
			break;
		case ELECTRO:
			sprite = new Sprite(R.drawable.electro_platform,1,4);
			sprite.setAnimating(true);
			break;
		case SPRING:
			sprite = new Sprite(R.drawable.spring_platform,2,16);
			break;
		case SPIKE:
			sprite = new Sprite(R.drawable.spike,1,8);
			sprite.setAnimating(true);
			break;
		case SPIKE_V:
			sprite = new Sprite(R.drawable.spike_v, 1, 8);
			sprite.setAnimating(true);
			break;
		case WIN:
			sprite = new Sprite(R.drawable.win_platform, 1, 8);
			sprite.setAnimating(true);
			break;
		case TELEPORT_L_V:
			sprite = new Sprite(R.drawable.teleport_l_v, 1, 16);
			sprite.setAnimating(true);
			break;
		case TELEPORT_R_V:
			sprite = new Sprite(R.drawable.teleport_r_v, 1, 16);
			sprite.setAnimating(true);
			break;
		case SLICK:
			sprite = new Sprite(R.drawable.slick, 1, 1);
			break;
		case SLOPE:
			sprite = new Sprite(R.drawable.slope, 3, 1);
			break;
		case ONE_WAY_LEFT:
			sprite = new Sprite(R.drawable.one_way_right, 1, 16);
			break;
		case ONE_WAY_RIGHT:
			sprite = new Sprite(R.drawable.one_way_left, 1, 16);
			break;
		case ONE_WAY_DOWN:
			sprite = new Sprite(R.drawable.one_way_down, 1, 16);
			break;
		case ONE_WAY_UP:
			sprite = new Sprite(R.drawable.one_way_up, 1, 16);
			break;
		case SWITCH:
			sprite = new Sprite(R.drawable.switch_platform, 4, 8, switchHelper);
			currentStatus = switchHelper;
			switchHelper = (switchHelper + 1) % 4;
			break;
		case UNLOCK:
			sprite = new Sprite(R.drawable.unlock_platform, 1, 1);
			break;
		case STRING:
			sprite = new Sprite(R.drawable.string_platform, 1, 8);
			break;
		case LIMIT:
			sprite = new Sprite(R.drawable.limit_way, 4, 8);
			break;
		case BRICK:
			sprite = new Sprite(R.drawable.brick, 4, 1);
			break;
		case BRICK_V:
			sprite = new Sprite(R.drawable.brick_v, 4, 1);
			break;
		case GLUE:
			sprite = new Sprite(R.drawable.glue, 1, 1);
			break;
		case GLUE_V:
			sprite = new Sprite(R.drawable.glue_v, 1, 1);
			break;
		case TELEPORT:
			sprite = new Sprite(R.drawable.teleport, 1, 1);
			break;
		case INVISIBLE:
			sprite = new Sprite(R.drawable.invisible_platform, 2, 8);
			break;
		case TRANSPARENT:
			sprite = new Sprite(R.drawable.transparent_platform, 1, 8);
			break;
		case TRANSPARENT_V:
			sprite = new Sprite(R.drawable.transparent_platform_v, 1, 8);
			break;
		case WAY_UP_DOWN:
			sprite = new Sprite(R.drawable.way_up_down, 2, 16);
			break;
		case NONE:
			break;
		}
	}
	
	public PlatformType getType() {
		return type;
	}
	
	public void changeSet(Motion motion, Motion prevMotion) {
		MotionType mt = motion.getType();
		MotionType prevMt = prevMotion.getType();
		if(sprite == null || 
				mt == MotionType.MAGNET || 
				mt == MotionType.BEAT_ROOF || 
				mt == MotionType.THROW_LEFT && motion.getStage() != 0 || 
				mt == MotionType.THROW_RIGHT && motion.getStage() != 0 || 
				mt == MotionType.JUMP && motion.getStage() != 0 && type != PlatformType.TRAMPOLINE ||
				mt == MotionType.FLY_LEFT && motion.getStage() != 0 ||
				mt == MotionType.FLY_RIGHT && motion.getStage() != 0 ||
				mt == MotionType.TP_LEFT && prevMt == MotionType.FLY_LEFT ||
				mt == MotionType.TP_RIGHT && prevMt == MotionType.FLY_RIGHT ||
				mt == MotionType.JUMP_LEFT_WALL && prevMt == MotionType.FLY_LEFT ||
				mt == MotionType.JUMP_RIGHT_WALL && prevMt == MotionType.FLY_RIGHT ||
				mt == MotionType.FLY_RIGHT && prevMt == MotionType.FLY_LEFT && prevMotion.getStage() != 0 ||
				mt == MotionType.FLY_LEFT && prevMt == MotionType.FLY_RIGHT && prevMotion.getStage() != 0) {
			return;
		}
		if(type == PlatformType.REDUCE) {
			if(currentStatus<3) {
				currentStatus++;
				sprite.changeSet(currentStatus);
				sprite.playOnce();
			} else if(currentStatus<4){
				currentStatus++;
				sprite = null;
				type = PlatformType.NONE;
			}
			return;
		}
		if(type == PlatformType.ANGLE_LEFT) {
			sprite.playOnce();
			return;
		}
		if(type == PlatformType.ANGLE_RIGHT) {
			sprite.playOnce();
			return;
		}
		if(type == PlatformType.THROW_OUT_LEFT) {
			sprite.playOnce();
			return;
		}
		if(type == PlatformType.THROW_OUT_RIGHT) {
			sprite.playOnce();
			return;
		}
		if(type == PlatformType.TRAMPOLINE) {
			sprite.playOnce();
			return;
		}
		if(type == PlatformType.SLOPE) {
			switch(mt) {
			case JUMP_LEFT:
			case JUMP_LEFT_WALL:
				currentStatus = 1;
				sprite.changeSet(1);
				break;
			case JUMP_RIGHT:
			case JUMP_RIGHT_WALL:
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
				sprite.changeSet(1);
				sprite.playOnce();
				break;
			case JUMP_RIGHT:
				sprite.changeSet(2);
				sprite.playOnce();
				break;
			case JUMP:
			default:
				sprite.changeSet(0);
				sprite.playOnce();
				break;
			}
			return;
		}
		if(type == PlatformType.ONE_WAY_DOWN && mt == MotionType.FALL) {
			sprite.playOnce();
			return;
		}
		if(type == PlatformType.WAY_UP_DOWN && mt == MotionType.FALL) {
			sprite.changeSet(1);
			sprite.playOnce();
			return;
		}
		if(type == PlatformType.STRING && mt == MotionType.STAY) {
			sprite.playOnce();
			type = PlatformType.NONE;
			return;
		}
		if(type == PlatformType.INVISIBLE) {
			sprite.changeSet(1);
			sprite.playOnce();
			return;
		}
		if(type == PlatformType.TRANSPARENT) {
			sprite.playOnce();
			type = PlatformType.NONE;
			return;
		}
	}
	
	public void updateRoof(Motion motion) {
		MotionType mt = motion.getType();
		int stage = motion.getStage();
		if(mt == MotionType.BEAT_ROOF) {
			if(type == PlatformType.SIMPLE) {
				sprite.changeSet(3);
				sprite.playOnce();
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
				sprite.playOnce();
			}
		}
		if(mt == MotionType.JUMP && stage != 0 && type == PlatformType.ONE_WAY_UP) {
			sprite.playOnce();
		}
		if(mt == MotionType.JUMP && stage != 0 && type == PlatformType.WAY_UP_DOWN) {
			sprite.changeSet(0);
			sprite.playOnce();
		}
		
		if(mt == MotionType.JUMP && stage != 0 && type == PlatformType.TRANSPARENT) {
			sprite.playOnce();
			type = PlatformType.NONE;
		}
	}
	
	public void highlightSpring(Motion prevMotion) {
		MotionType prevMt = prevMotion.getType();
		if(type == PlatformType.SPRING) {
			switch(prevMt) {
			case JUMP:
			case FLY_LEFT:
			case FLY_RIGHT:
			case THROW_LEFT:
			case THROW_RIGHT:
			case TP:
				sprite.playOnce(0);
				break;
			default:
				sprite.playOnce(10);
				break;
			}
		}
	}
	
	public void updateLeftWall(Motion motion, Motion prevMotion) {
		MotionType mt = motion.getType();
		int stage = motion.getStage();
		MotionType prevMt = prevMotion.getType();
		if(mt == MotionType.JUMP_LEFT ||
				mt == MotionType.FLY_LEFT && stage != 0 ||
				mt == MotionType.THROW_LEFT ||
				mt == MotionType.JUMP_LEFT_WALL) {
			if(type == PlatformType.ONE_WAY_LEFT) {
				sprite.playOnce();
			}
			if(type == PlatformType.LIMIT && currentStatus < 3) {
				currentStatus = currentStatus + 1;
				sprite.changeSet(currentStatus);
				sprite.goToFrame(1);
				sprite.playOnce();
			}
			if(type == PlatformType.TRANSPARENT_V) {
				sprite.playOnce();
				type = PlatformType.NONE;
			}
			if(type == PlatformType.SWITCH) {
				currentStatus = (currentStatus + 1) % 4;
				sprite.changeSet(currentStatus);
				sprite.goToFrame(1);
				switch(prevMt) {
				case JUMP:
				case FLY_LEFT:
				case THROW_LEFT:
				case TP:
					sprite.playOnce(0);
					break;
				default:
					sprite.playOnce(10);
					break;
				}
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
		}
	}
	
	public void updateRightWall(Motion motion, Motion prevMotion) {
		MotionType mt = motion.getType();
		int stage = motion.getStage();
		MotionType prevMt = prevMotion.getType();
		if(mt == MotionType.JUMP_RIGHT ||
				mt == MotionType.FLY_RIGHT && stage != 0 ||
				mt == MotionType.THROW_RIGHT ||
				mt == MotionType.JUMP_RIGHT_WALL) {
			if(type == PlatformType.ONE_WAY_RIGHT) {
				sprite.playOnce();
			}
			if(type == PlatformType.LIMIT && currentStatus < 3) {
				currentStatus = currentStatus + 1;
				sprite.changeSet(currentStatus);
				sprite.goToFrame(1);
				sprite.playOnce();
			}
			if(type == PlatformType.TRANSPARENT_V) {
				sprite.playOnce();
				type = PlatformType.NONE;
			}
			if(type == PlatformType.SWITCH) {
				currentStatus = (currentStatus + 1) % 4;
				sprite.changeSet(currentStatus);
				sprite.goToFrame(1);
				switch(prevMt) {
				case JUMP:
				case FLY_RIGHT:
				case THROW_RIGHT:
				case TP:
					sprite.playOnce(0);
					break;
				default:
					sprite.playOnce(10);
					break;
				}
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
			if(type == PlatformType.NONE && !sprite.isAnimating()) {
				sprite = null;
			} else {
				sprite.onDraw(canvas, x - sprite.getWidth() / 2, y - sprite.getHeight() / 2, update);
			}
		}
	}
}