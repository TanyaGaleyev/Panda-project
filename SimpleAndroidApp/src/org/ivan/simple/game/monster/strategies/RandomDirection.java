package org.ivan.simple.game.monster.strategies;

import java.util.ArrayList;

import org.ivan.simple.game.monster.MonsterDirection;
import org.ivan.simple.game.monster.MonsterStrategy;

public class RandomDirection implements MonsterStrategy {
	
	private ArrayList<MonsterDirection> avaibleDirections = new ArrayList<MonsterDirection>();
	private int id = 0;

	public RandomDirection(MonsterDirection... directions) {
		for(MonsterDirection direction : directions) {
			avaibleDirections.add(direction);
		}
	}

	public MonsterDirection nextDirection(boolean decline) {
		id = (int) (Math.random() * (avaibleDirections.size()));
		return avaibleDirections.get(id);
	}

}
