package org.ivan.simple.level;

import android.graphics.Canvas;

public class LevelCell {
	private PlatformType floor = PlatformType.NONE;

	private LevelCell() {
	}
	
	public static LevelCell createNone() {
		return new LevelCell();
	}
	
	public static LevelCell createSimple() {
		LevelCell ret = new LevelCell();
		ret.floor = PlatformType.SIMPLE;
		return ret;
	}

	public PlatformType getFloor() {
		return floor;
	}
	
}
