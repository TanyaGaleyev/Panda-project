package org.ivan.simple;

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
	PRE_JUMP,
	FALL,
	JUMP_LEFT,
	JUMP_RIGHT,
	STEP_LEFT,
	STEP_RIGHT,
	STAY,
	NONE
}
