package org.ivan.simple.level;

import org.ivan.simple.GameView;

import android.graphics.Canvas;

public class LevelView {
	private int rows;
	private int cols;
	private LevelCell[][] levelGrid;
	
	public LevelView(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		levelGrid = new LevelCell[rows][cols];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				if((i + 2) > rows) {
					levelGrid[i][j] = LevelCell.createSimple();
				} else {
					levelGrid[i][j] = LevelCell.createNone();
				}
			}
		}
	}
	
	public void onDraw(Canvas canvas) {
		
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				if(levelGrid[i][j].getFloor().getSprite() != null) {
					levelGrid[i][j].getFloor().getSprite().onDraw(canvas, getXByIndex(j), getYByIndex(i));
				}
			}
		}
	}
	
	private int getXByIndex(int j) {
		return GameView.GRID_STEP * (j) + GameView.GRID_STEP / 2;
	}
	
	private int getYByIndex(int i) {
		return GameView.GRID_STEP * (i + 1) + GameView.GRID_STEP / 2;
	}

}
