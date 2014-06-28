package com.pavlukhin.acropanda.game.level;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class LevelView {

	public LevelModel model;
	private final int LEFT_BOUND;
	private final int TOP_BOUND;
	private final int GRID_STEP;
	public LevelView(LevelModel model, int gridStep, int leftBound, int topBound) {
		this.model = model;
		GRID_STEP = gridStep;
		LEFT_BOUND = leftBound;
		TOP_BOUND = topBound;
	}
	
	public void onDraw(Canvas canvas, boolean update) {
		
		for(int i = 0; i <model.getRows(); i++) {
			for(int j = 0; j <model.getCols(); j++) {
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
		int xstart = LEFT_BOUND - GRID_STEP / 2;
		int xend = xstart + model.getCols() * GRID_STEP;
		int ystart = TOP_BOUND - GRID_STEP / 2;
		int yend = ystart + model.getRows() * GRID_STEP;
		for(int x = xstart; x <= xend; x += GRID_STEP) {
			canvas.drawLine(x, ystart, x, yend, paint);
		}
		for(int y = ystart; y <= yend; y += GRID_STEP) {
			canvas.drawLine(xstart, y, xend, y, paint);
		}
	}

}
