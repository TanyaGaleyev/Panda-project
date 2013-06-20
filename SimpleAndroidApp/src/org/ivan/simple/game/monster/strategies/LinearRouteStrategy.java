package org.ivan.simple.game.monster.strategies;

import java.util.ArrayList;

import org.ivan.simple.game.level.LevelCell;
import org.ivan.simple.game.monster.MonsterDirection;

public class LinearRouteStrategy implements MonsterStrategy {
	
	private ArrayList<MonsterDirection> avaibleDirections = new ArrayList<MonsterDirection>();
	
	public LinearRouteStrategy(int[][] route) {
		for(int i = 1; i < route.length; i++) {
			addDirections(route[i - 1], route[i]);
		}
	}
	
	private void addDirections(int[] src, int[] dst) {
		if(src[0] == dst[0]) {
			if(dst[1] > src[1]) {
				for(int j = 0; j < dst[1] - src[1]; j++) {
					avaibleDirections.add(MonsterDirection.DOWN);
				}
			} else {
				for(int j = 0; j < src[1] - dst[1]; j++) {
					avaibleDirections.add(MonsterDirection.UP);
				}
			}
		} else if(src[1] == dst[1]) {
			if(dst[0] > src[0]) {
				for(int j = 0; j < dst[0] - src[0]; j++) {
					avaibleDirections.add(MonsterDirection.RIGHT);
				}
			} else {
				for(int j = 0; j < src[0] - dst[0]; j++) {
					avaibleDirections.add(MonsterDirection.LEFT);
				}
			}
		} else {
			System.out.println("Wrong route carefull " + src + dst);
		}
	}
	
	private int adder = 1;
	private int id = 0;
	
	public MonsterDirection nextDirection(boolean decline) {
		if(decline || id < 0 || id >= avaibleDirections.size()) {
			adder = -adder;
			id += adder;
		}
		if(decline) {
			id += adder;
		}
		MonsterDirection nd = backOrForward();
		id += adder;
		return nd;
	}
	
	private MonsterDirection backOrForward() {
		if(adder > 0) {
			return avaibleDirections.get(id);
		} else {
			return avaibleDirections.get(id).opposite();
		}
	}
}
