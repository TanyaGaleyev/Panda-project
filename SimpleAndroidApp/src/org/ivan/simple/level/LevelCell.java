package org.ivan.simple.level;

import org.ivan.simple.MotionType;

public class LevelCell {
	protected Platform floor = new Platform(PlatformType.NONE);
	protected Platform left_wall = new Platform(PlatformType.NONE);
	protected Platform right_wall = new Platform(PlatformType.NONE);
	protected Platform roof = new Platform(PlatformType.NONE);
	private int ObjectStatus=0; // LevelCell object create or not create? 
	
	public LevelCell() {
	}
	
	public void createNone() {
	}
	public void createSimple(int side) {
		if(side==0){
		floor = new Platform(PlatformType.SIMPLE);
		}else{
		roof = new Platform(PlatformType.SIMPLE);
		}
	}

	public void createSimple_V(int side) {
		if(side==0){
		left_wall = new Platform(PlatformType.SIMPLE_V);
		}else{
		right_wall = new Platform(PlatformType.SIMPLE_V);
		}
	}
	
	public void createReduce(int side) {
		if(side==0){
		floor = new Platform(PlatformType.REDUCE);
		}else{
		roof = new Platform(PlatformType.REDUCE);
		}
	}
	
	public void createAngleRight(int side) {
		if(side==0){
		floor = new Platform(PlatformType.ANGLE_RIGHT);
		}else{
		roof = new Platform(PlatformType.ANGLE_RIGHT);
		}
	}
	
	public void createAngleLeft(int side) {
		if(side==0){
		floor = new Platform(PlatformType.ANGLE_LEFT);
		}else{
		roof = new Platform(PlatformType.ANGLE_LEFT);
		}
	}
	
	
	public Platform getFloor() {
		return floor;
	}
	public Platform getRoof() {
		return roof;
	}
	public Platform getLeft() {
		return left_wall;
	}
	public Platform getRight() {
		return right_wall;
	}
	
	public void updateCell(MotionType mt) {
		floor.changePlatform();
		floor.changeSet(mt);
	}
}
