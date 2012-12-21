package org.ivan.simple.game.monster.strategies;

import java.util.ArrayList;

import org.ivan.simple.game.monster.MonsterDirection;
import org.ivan.simple.game.monster.MonsterStrategy;

public class PriorityDirection implements MonsterStrategy {
	private ArrayList<MonsterDirection> directionsByPriority = new ArrayList<MonsterDirection>();
	private int id = 0;

	public PriorityDirection(MonsterDirection... directions) {
		for(MonsterDirection direction : directions) {
			directionsByPriority.add(direction);
		}
	}
	
	public MonsterDirection nextDirection(boolean decline) {
		if(decline) {
			if(id == directionsByPriority.size() - 1) {
				id = 0;
			} else {
				id++;
			}
		}
		return directionsByPriority.get(id);
	}
}
