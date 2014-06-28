package com.pavlukhin.acropanda.game.monster.strategies;

import com.pavlukhin.acropanda.game.monster.MonsterDirection;


public interface MonsterStrategy {
	
	public MonsterDirection nextDirection(boolean decline);
}
