package org.ivan.simple.game.monster;

public enum MonsterDirection {
	UP,
	LEFT,
	DOWN,
	RIGHT,
	IDLE;
	
	public static MonsterDirection[] getAllDirections() {
		return new MonsterDirection[]{UP, LEFT, DOWN, RIGHT};
	}
}
