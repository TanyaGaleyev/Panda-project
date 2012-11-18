package org.ivan.simple.game.level;

import org.ivan.simple.game.GameView;

import android.graphics.Canvas;

public class LevelView {

	public LevelModel model;
	public LevelView(int lev) {
		model = new LevelModel(lev);
	}
	
	public void onDraw(Canvas canvas, boolean update) {
		
		for(int i = 0; i <model.row; i++) {
			for(int j = 0; j <model.col; j++) {
				drawCell(canvas, i, j, update);
			}
		}
	}
	
	private int getXByIndex(int j) {
		return GameView.GRID_STEP * (j) + GameView.GRID_STEP / 2;
	}
	
	private int getYByIndex(int i) {
		return (GameView.GRID_STEP * (i + 1) + GameView.GRID_STEP / 2);
	}
	
	private void drawCell(Canvas canvas, int i, int j, boolean update) {
		LevelCell cell = model.getCell(i, j);
		// Careful with duplicated walls
		if(cell.getFloor().getSprite() != null) {
			cell.getFloor().getSprite().onDraw(canvas, getXByIndex(j), getYByIndex(i)-15, update);
		}
		// Draw left wall ONLY for most left cells 
		if(cell.getLeft().getSprite() != null && j == 0) {
			cell.getLeft().getSprite().onDraw(canvas, getXByIndex(j)-15, getYByIndex(i)-GameView.GRID_STEP, update);
		}
		// Draw roof ONLY for highest cells
		if(cell.getRoof().getSprite() != null && i == 0) {
			cell.getRoof().getSprite().onDraw(canvas, getXByIndex(j), getYByIndex(i)-15-GameView.GRID_STEP, update);
		}	
		if(cell.getRight().getSprite() != null) {
			cell.getRight().getSprite().onDraw(canvas, getXByIndex(j)+GameView.GRID_STEP-15, getYByIndex(i)-GameView.GRID_STEP, update);
		}
		if(cell.prize != null) {
			cell.prize.onDraw(canvas, getXByIndex(j), getYByIndex(i), update);
		}
	}

}
