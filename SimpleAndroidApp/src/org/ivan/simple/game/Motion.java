package org.ivan.simple.game;

public class Motion {
	private final MotionType type;
	private int stage;
	
	public Motion(MotionType type) {
		this(type, 0);
	}
	
	public Motion(MotionType type, int stage) {
		this.type = type;
		this.stage = stage;
	}
	
	public MotionType getType() {
		return type;
	}
	
	public int getStage() {
		return stage;
	}
	
	public void startMotion() {
		stage = 0;
	}
	
	public void continueMotion() {
		switch(type) {
		case FALL_BLANSH:
		case THROW_LEFT:
		case THROW_RIGHT:
		case TP:
			stage = (stage + 1) % 2;
			break;
		default:
			stage = 1;
			break;
		}
		// TODO CHECK if we need this commented code
		//if(isFinishing()) stage = 0;
	}
	
	public void finishMotion() {
		switch(type) {
		case FLY_LEFT:
		case FLY_RIGHT:
		case MAGNET:
		case STICK_LEFT:
		case STICK_RIGHT:
			stage = 2;
			break;
		default:
			stage = 0;
			break;
		}
	}
	
	public boolean isFinishing() {
		switch(type) {
		case MAGNET:
		case STICK_LEFT:
		case STICK_RIGHT:
		case FLY_LEFT:
		case FLY_RIGHT:
			return stage == 2;
		default:
			return false;
		}
	}
	
	public int getXSpeed() {
		return getXSpeedAtStage(type, stage);
	}
	
	public int getYSpeed() {
		return getYSpeedAtStage(type, stage);
	}
	
	public boolean isUncontrolable() {
		switch(type) {
		case JUMP:
		case FALL_BLANSH:
		case FLY_LEFT:
		case FLY_RIGHT:
			return stage == 0;
		default:
			return false;
		}
	}
	
	public static int getXSpeedAtStage(MotionType type, int stage) {
		switch(type) {
		case JUMP_LEFT:
		case THROW_LEFT:
			return -1;
		case FLY_LEFT:
			return stage != 0 ? -1 : 0;
		case JUMP_RIGHT:
		case THROW_RIGHT:
			return 1;
		case FLY_RIGHT:
			return stage != 0 ? 1 : 0;
		default: 
			return 0;
		}
	}
	
	public static int getYSpeedAtStage(MotionType type, int stage) {
		if(type == MotionType.JUMP && stage != 0) return -1;
		if(type == MotionType.FALL) return 1;
		if(type == MotionType.FALL_BLANSH) return 1;
		return 0;
	}
	
	public Motion getChildMotion() {
		return this;
	}
}
