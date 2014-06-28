package com.pavlukhin.acropanda.game.hero;

import com.pavlukhin.acropanda.game.motion.Motion;
import com.pavlukhin.acropanda.game.motion.MotionType;

public class HeroModel {
	private int x;
	private int y;
	private int prevX;
	private int prevY;
	
	public Motion currentMotion = new Motion(MotionType.NONE);
	public Motion finishingMotion = new Motion(MotionType.NONE); 
	
	public HeroModel(int row, int col) {
		x = col;
		y = row;
		prevX = x;
		prevY = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		prevX = this.x;
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		prevY = this.y;
		this.y = y;
	}

	public int getPrevX() {
		return prevX;
	}

	public int getPrevY() {
		return prevY;
	}
	
	public boolean hasMoved() {
		return prevX != x || prevY != y;
	}
	
}
