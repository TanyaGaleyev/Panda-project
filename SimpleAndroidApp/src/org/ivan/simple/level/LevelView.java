package org.ivan.simple.level;

import org.ivan.simple.GameView;

import android.graphics.Canvas;
import org.ivan.simple.level.LevelStorage;

public class LevelView {

	private LevelModel model;
	public LevelView(int lev) {
		model = new LevelModel(lev);
	}
	
	public void onDraw(Canvas canvas) {
		
		for(int i = 0; i <model.row; i++) {
			for(int j = 0; j <model.col; j++) {
				drawCell(canvas, i, j);
			}
		}
	}
	
	private int getXByIndex(int j) {
		return GameView.GRID_STEP * (j) + GameView.GRID_STEP / 2;
	}
	
	private int getYByIndex(int i) {
		return (GameView.GRID_STEP * (i + 1) + GameView.GRID_STEP / 2)-15;
	}
	
	private void drawCell(Canvas canvas, int i, int j) {
		LevelCell cell = model.getCell(i, j);
		if(cell.getFloor().getSprite() != null) {
			cell.getFloor().getSprite().onDraw(canvas, getXByIndex(j), getYByIndex(i));
		}
	}

}
