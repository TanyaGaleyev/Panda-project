package org.ivan.simple.game.monster;

import java.util.ArrayList;

public class MonsterStrategy {
	
	private ArrayList<MonsterDirection> directionsByPriority = new ArrayList<MonsterDirection>(); 

	public MonsterStrategy(MonsterDirection... directions) {
		for(MonsterDirection direction : directions) {
			directionsByPriority.add(direction);
		}
	}
	
	public ArrayList<MonsterDirection> getDirectionsByPriority() {
		return directionsByPriority;
	}
}
