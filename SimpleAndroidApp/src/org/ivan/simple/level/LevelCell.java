package org.ivan.simple.level;

import org.ivan.simple.game.MotionType;

public class LevelCell {
	protected Platform floor = new Platform(PlatformType.NONE);
	protected Platform left_wall = new Platform(PlatformType.NONE);
	protected Platform right_wall = new Platform(PlatformType.NONE);
	protected Platform roof = new Platform(PlatformType.NONE);
	protected Prize prize = null;
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
	
	public void createElecrto(int side) {
		if(side==0){
		floor = new Platform(PlatformType.ELECTRO);
		}else{
		roof = new Platform(PlatformType.ELECTRO);
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
	
	public void createTrowOutLeft(int side) {
		if(side==0){
		floor = new Platform(PlatformType.THROW_OUT_LEFT);
		}else{
		roof = new Platform(PlatformType.THROW_OUT_LEFT);
		}
	}
	public void createTrowOutRight(int side) {
		if(side==0){
		floor = new Platform(PlatformType.THROW_OUT_RIGHT);
		}else{
		roof = new Platform(PlatformType.THROW_OUT_RIGHT);
		}
	}
	
	public void createTrampoline(int side) {
		if (side==0){
			floor = new Platform(PlatformType.TRAMPOLINE);
		}else{
			roof = new Platform(PlatformType.TRAMPOLINE);
		}
	}
	
	public void createSpring(int side) {
		if(side==0){
		left_wall = new Platform(PlatformType.SPRING);
		}else{
		right_wall = new Platform(PlatformType.SPRING);
		}
	}
	
	public void createSpike(int side) {
		if(side==0){
		floor = new Platform(PlatformType.SPIKE);
		}else{
		roof = new Platform(PlatformType.SPIKE);
		}
	}
	
	public void createSpike_V(int side) {
		if(side==0){
		left_wall = new Platform(PlatformType.SPIKE_V);
		}else{
		right_wall = new Platform(PlatformType.SPIKE_V);
		}
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
	
	public void updateCell(MotionType mt) {
		floor.changeSet(mt);
	}
}
