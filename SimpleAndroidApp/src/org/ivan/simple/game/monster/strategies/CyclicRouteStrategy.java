package org.ivan.simple.game.monster.strategies;

import java.util.ArrayList;
import java.util.List;

import org.ivan.simple.game.monster.MonsterDirection;

public class CyclicRouteStrategy implements MonsterStrategy {
	private List<MonsterDirection> availableDirections;
	
	public CyclicRouteStrategy(List<MonsterDirection> availableDirections) {
		this.availableDirections = availableDirections;
	}
	
	private int adder = 1;
	private int id = 0;
	
	public MonsterDirection nextDirection(boolean decline) {
		if(decline) {
			adder = -adder;
			id += adder;
			id += adder;
		}
		if(id < 0) {
			id = availableDirections.size() - 1;
		}
		if(id >= availableDirections.size()) {
			id = 0;
		}
		MonsterDirection nd = backOrForward();
		id += adder;
		return nd;
	}
	
	private MonsterDirection backOrForward() {
		if(adder > 0) {
			return availableDirections.get(id);
		} else {
			return availableDirections.get(id).opposite();
		}
	}
}
