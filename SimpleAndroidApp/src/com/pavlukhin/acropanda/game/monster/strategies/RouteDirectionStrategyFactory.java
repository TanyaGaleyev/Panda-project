package com.pavlukhin.acropanda.game.monster.strategies;

import java.util.ArrayList;
import java.util.Arrays;

import com.pavlukhin.acropanda.game.monster.MonsterDirection;

public class RouteDirectionStrategyFactory {
	public static MonsterStrategy createRouteDirectionStrategy(int[][] routeCells) {
		ArrayList<MonsterDirection> dirs = new ArrayList<MonsterDirection>();
		int ncells = routeCells.length;
		if(ncells == 0) {
			return new RandomDirection(MonsterDirection.getAllDirections());
		}
		for(int i = 1; i < ncells; i++) {
			addDirections(routeCells[i - 1], routeCells[i], dirs);
		}
		if(Arrays.equals(routeCells[0], routeCells[ncells - 1])) {
			return new CyclicRouteStrategy(dirs);
		} else {
			return new LinearRouteStrategy(dirs);
		}
	}

	private static void addDirections(int[] src, int[] dst, ArrayList<MonsterDirection> dirs) {
		if(src[0] == dst[0]) {
			if(dst[1] > src[1]) {
				for(int j = 0; j < dst[1] - src[1]; j++) {
					dirs.add(MonsterDirection.DOWN);
				}
			} else {
				for(int j = 0; j < src[1] - dst[1]; j++) {
					dirs.add(MonsterDirection.UP);
				}
			}
		} else if(src[1] == dst[1]) {
			if(dst[0] > src[0]) {
				for(int j = 0; j < dst[0] - src[0]; j++) {
					dirs.add(MonsterDirection.RIGHT);
				}
			} else {
				for(int j = 0; j < src[0] - dst[0]; j++) {
					dirs.add(MonsterDirection.LEFT);
				}
			}
		} else {
			System.out.println("Wrong route, carefull " + src + dst);
		}
	}
}
