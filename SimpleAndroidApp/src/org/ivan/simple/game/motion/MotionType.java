package org.ivan.simple.game.motion;

/**
 * This enumeration represents type of motion that hero can do.
 * Motion type is based on current state, user control, position in level grid
 * For example:
 * Jump left/right
 * Slide left/right
 * Jump up
 * Fall
 * Fly left/right
 * Attach to wall
 * @author Ivan
 *
 */
public enum MotionType {
	JUMP,
	BEAT_ROOF,
	FALL,
	FALL_BLANSH,
	JUMP_LEFT,
	JUMP_RIGHT,
	STAY,
	JUMP_RIGHT_WALL,
	JUMP_LEFT_WALL,
	MAGNET,
	THROW_LEFT,
	THROW_RIGHT,
	FLY_RIGHT,
	FLY_LEFT,
	TP_RIGHT,
	TP_LEFT,
	TP_RL,
	TP_LR,
	STICK_LEFT,
	STICK_RIGHT,
    TRY_JUMP_GLUE,
	TP,
	CLOUD_IDLE,
	CLOUD_LEFT,
	CLOUD_RIGHT,
	CLOUD_UP,
	CLOUD_DOWN,
	NONE;
	
	public boolean isCLOUD() {
		switch (this) {
		case CLOUD_IDLE:
		case CLOUD_LEFT:
		case CLOUD_RIGHT:
		case CLOUD_UP:
		case CLOUD_DOWN:
			return true;
		default:
			return false;
		}
	}

    public boolean isHorizontalTP() {
        return this == TP_LEFT || this == TP_RIGHT || this == TP_LR || this == TP_RL;
    }

    public MotionType opposite() {
        switch (this) {
            case JUMP_LEFT: return JUMP_RIGHT;
            case JUMP_RIGHT: return JUMP_LEFT;
            case THROW_LEFT: return THROW_RIGHT;
            case THROW_RIGHT: return THROW_LEFT;
            case FLY_LEFT: return FLY_RIGHT;
            case FLY_RIGHT: return FLY_LEFT;
            default: return this;
        }
    }
}
