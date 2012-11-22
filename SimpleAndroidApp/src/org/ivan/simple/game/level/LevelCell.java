package org.ivan.simple.game.level;

import org.ivan.simple.game.MotionType;

public class LevelCell {
	protected Platform floor = new Platform(PlatformType.NONE);
	protected Platform left_wall = new Platform(PlatformType.NONE);
	protected Platform right_wall = new Platform(PlatformType.NONE);
	protected Platform roof = new Platform(PlatformType.NONE);
	protected Prize prize = null;
	
	public LevelCell() {
	}
	
	public void createRoof(int typeid) {
		roof = new Platform(PlatformType.getTypeById(typeid, true));
	}
	
	public void createFloor(int typeid) {
		floor = new Platform(PlatformType.getTypeById(typeid, true));
	}
	
	public void createLeft(int typeid) {
		left_wall = new Platform(PlatformType.getTypeById(typeid, false));
	}
	
	public void createRight(int typeid) {
		right_wall = new Platform(PlatformType.getTypeById(typeid, false));
	}
	
	public void createRoof(PlatformType pt) {
		roof = new Platform(pt);
	}
	
	public void createFloor(PlatformType pt) {
		floor = new Platform(pt);
	}
	
	public void createLeft(PlatformType pt) {
		left_wall = new Platform(pt);
	}
	
	public void createRight(PlatformType pt) {
		right_wall = new Platform(pt);
	}
	
	public void setPrize(int prizeType) {
		if(prizeType != 0) {
			prize = new Prize();
		} else {
			prize = null;
		}
	}
	
	public int removePrize() {
		if(prize == null) return 0;
		prize = null;
		return 1;
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
	
	public void updateCell(MotionType mt, MotionType prevMt) {
		roof.updateRoof(mt);
		if(mt == MotionType.FLY_LEFT && mt.isUncontrolable()) {
			right_wall.highlightSpring(prevMt);
		}
		if(mt == MotionType.FLY_RIGHT && mt.isUncontrolable()) {
			left_wall.highlightSpring(prevMt);
		}
		right_wall.updateRightWall(mt, prevMt);
		left_wall.updateLeftWall(mt, prevMt);
		floor.changeSet(mt);
	}
}
