package org.ivan.simple.level;

import android.graphics.Canvas;

public class LevelCell {
	private PlatformType floor = PlatformType.NONE;
	private PlatformType left_wall = PlatformType.NONE;
	private PlatformType right_wall = PlatformType.NONE;
	private PlatformType roof = PlatformType.NONE;
	private int ObjectStatus=0; // LevelCell object create or not create? 
	
	public LevelCell() {
	}
	
	public void createNone() {
		
	}
	public void createSimple(int side) {
		if(side==0){
		floor = PlatformType.SIMPLE;}
		else{
		roof = PlatformType.SIMPLE;
			
		}
	}

	public void createSimple_V(int side) {
		if(side==0){
		left_wall = PlatformType.SIMPLE_V;
		}else{
		right_wall = PlatformType.SIMPLE_V;
		}
	}
	
	public PlatformType getFloor() {
		return floor;
	}
	public PlatformType getRoof() {
		return roof;
	}
	public PlatformType getLeft() {
		return left_wall;
	}
	public PlatformType getRight() {
		return right_wall;
	}
}
