package org.ivan.simple.game.monster;


public interface MonsterStrategy {
	
	public MonsterDirection nextDirection(boolean decline);
}
