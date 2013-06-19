package org.ivan.simple.game.monster.strategies;

import org.ivan.simple.game.monster.MonsterDirection;


public interface MonsterStrategy {
	
	public MonsterDirection nextDirection(boolean decline);
}
