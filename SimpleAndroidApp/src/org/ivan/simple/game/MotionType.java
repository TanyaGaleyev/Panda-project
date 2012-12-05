package org.ivan.simple.game;

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
	STICK_LEFT,
	STICK_RIGHT,
	TP,
	NONE;
	
}
