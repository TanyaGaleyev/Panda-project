package org.ivan.simple.game.monster;

import java.util.ArrayList;

public class MonsterStrategy {
	
	private ArrayList<MonsterDirection> directionsByPriority = new ArrayList<MonsterDirection>();
	private int id = 0;

	public MonsterStrategy(MonsterDirection... directions) {
		for(MonsterDirection direction : directions) {
			directionsByPriority.add(direction);
		}
	}
	
	public MonsterDirection nextDirection() {
//		if(id == directionsByPriority.size() - 1) {
//			id = 0;
//		} else {
//			id++;
//		}
		id = (int) (Math.random() * (directionsByPriority.size()));
		return directionsByPriority.get(id);
	}
}
