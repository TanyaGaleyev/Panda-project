package com.pavlukhin.acropanda.game.monster;

public enum MonsterDirection {
	UP,
	LEFT,
	DOWN,
	RIGHT,
	IDLE;
	
	public static MonsterDirection[] getAllDirections() {
		return new MonsterDirection[]{UP, LEFT, DOWN, RIGHT};
	}
	
	public MonsterDirection opposite() {
		switch (this) {
		case UP:
			return DOWN;
		case DOWN:
			return UP;
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		default:
			return null;
		}
	}
}
