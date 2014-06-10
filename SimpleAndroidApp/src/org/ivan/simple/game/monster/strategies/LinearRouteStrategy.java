package org.ivan.simple.game.monster.strategies;

import java.util.ArrayList;
import java.util.List;

import org.ivan.simple.game.monster.MonsterDirection;

public class LinearRouteStrategy implements MonsterStrategy {
	
	private List<MonsterDirection> availableDirections = new ArrayList<MonsterDirection>();
	
	LinearRouteStrategy(List<MonsterDirection> availableDirections) {
		this.availableDirections = availableDirections;
	}
	
	private int adder = 1;
	private int id = 0;
	
	public MonsterDirection nextDirection(boolean decline) {
		if(decline || id < 0 || id >= availableDirections.size()) {
			adder = -adder;
			id += adder;
		}
		if(decline) {
			id += adder;
		}
        if(id < 0) {
            id = 0;
        }
        if(id >= availableDirections.size()) {
            id = availableDirections.size() - 1;
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
