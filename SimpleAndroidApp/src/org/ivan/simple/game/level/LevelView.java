package org.ivan.simple.game.level;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class LevelView {

	public LevelModel model;
	private final int LEFT_BOUND;
	private final int TOP_BOUND;
	private final int GRID_STEP;
	public LevelView(int lev, int gridStep, int leftBound, int topBound) {
		model = new LevelModel(lev);
		GRID_STEP = gridStep;
		LEFT_BOUND = leftBound;
		TOP_BOUND = topBound;
	}
	
	public void onDraw(Canvas canvas, boolean update) {
		
		for(int i = 0; i <model.row; i++) {
			for(int j = 0; j <model.col; j++) {
				drawCell(canvas, i, j, update);
			}
		}
	}
	
	private int getXByIndex(int j) {
		return LEFT_BOUND + GRID_STEP * j;
	}
	
	private int getYByIndex(int i) {
		return TOP_BOUND + GRID_STEP * i;
	}
	
	private void drawCell(Canvas canvas, int i, int j, boolean update) {
		LevelCell cell = model.getCell(i, j);
		// Careful with duplicated walls
		cell.getFloor().onDraw(canvas, getXByIndex(j), getYByIndex(i) + GRID_STEP / 2, update);
		// Draw left wall ONLY for most left cells 
		if(j == 0) {
			cell.getLeft().onDraw(canvas, getXByIndex(j) - GRID_STEP / 2, getYByIndex(i), update);
		}
		// Draw roof ONLY for highest cells
		if(i == 0) {
			cell.getRoof().onDraw(canvas, getXByIndex(j), getYByIndex(i) - GRID_STEP / 2, update);
		}	
		cell.getRight().onDraw(canvas, getXByIndex(j) + GRID_STEP / 2, getYByIndex(i), update);
		if(cell.prize != null) {
			cell.prize.onDraw(canvas, getXByIndex(j), getYByIndex(i), update);
		}
	}
	
	public void drawGrid(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		for(int x = LEFT_BOUND - GRID_STEP / 2; x <= model.col * GRID_STEP + GRID_STEP / 2; x += GRID_STEP) {
			canvas.drawLine(x, TOP_BOUND - GRID_STEP / 2, x, model.row * GRID_STEP + GRID_STEP / 2, paint);
		}
		for(int y = TOP_BOUND - GRID_STEP / 2; y <= model.col * GRID_STEP + GRID_STEP / 2; y += GRID_STEP) {
			canvas.drawLine(LEFT_BOUND - GRID_STEP / 2, y, model.col * GRID_STEP + GRID_STEP / 2, y, paint);
		}
	}

}
