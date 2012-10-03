package org.ivan.simple.level;

import android.graphics.Canvas;

public class LevelCell {
	protected PlatformType floor = PlatformType.NONE;
	protected PlatformType left_wall = PlatformType.NONE;
	protected PlatformType right_wall = PlatformType.NONE;
	protected PlatformType roof = PlatformType.NONE;
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
	
	public void createReduce(int side) {
		if(side==0){
		floor = PlatformType.REDUCE;
		}else{
		roof = PlatformType.REDUCE;
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
