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

    public static interface PlatformLayerChecker {
        boolean belongsToLayer(Platform p);
    }

    public LevelViewLayer getLayer(PlatformLayerChecker checker) {
        LevelViewLayer ret = new LevelViewLayer();
        for (int i = 0; i < model.getRows(); i++) {
            for (int j = 0; j < model.getCols(); j++) {
                LevelCell cell = model.getCell(i, j);
                if(checker.belongsToLayer(cell.getFloor()))
                    ret.addSprite(cell.getFloor(), getXByIndex(j), getYByIndex(i) + GRID_STEP / 2);
                // Draw left wall ONLY for most left cells
                if(j == 0 && checker.belongsToLayer(cell.getLeft()))
                    ret.addSprite(cell.getLeft(), getXByIndex(j) - GRID_STEP / 2, getYByIndex(i));
                // Draw roof ONLY for highest cells
                if(i == 0 && checker.belongsToLayer(cell.getRoof()))
                    ret.addSprite(cell.getRoof(), getXByIndex(j), getYByIndex(i) - GRID_STEP / 2);
                if(checker.belongsToLayer(cell.getRight()))
                    ret.addSprite(cell.getRight(), getXByIndex(j) + GRID_STEP / 2, getYByIndex(i));
            }
        }
        return ret;
    }

    public LevelViewLayer getBackLayer() {
        LevelViewLayer ret = getLayer(new PlatformLayerChecker() {
            @Override
            public boolean belongsToLayer(Platform p) {
                return p.getType() != PlatformType.CLOUD;
            }
        });
        for (int i = 0; i < model.getRows(); i++) {
            for (int j = 0; j < model.getCols(); j++) {
                LevelCell cell = model.getCell(i, j);
                if (cell.getPrize() != null)
                    ret.addSprite(cell.getPrize(), getXByIndex(j), getYByIndex(i));
            }
        }
        return ret;
    }

    public LevelViewLayer getFrontLayer() {
        return getLayer(new PlatformLayerChecker() {
            @Override
            public boolean belongsToLayer(Platform p) {
                return p.getType() == PlatformType.CLOUD;
            }
        });
    }
	
	public void onDraw(Canvas canvas, boolean update) {
		for (int i = 0; i < model.getRows(); i++) {
			for (int j = 0; j < model.getCols(); j++) {
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
		cell.getFloor().draw(canvas, getXByIndex(j), getYByIndex(i) + GRID_STEP / 2, update);
		// Draw left wall ONLY for most left cells 
		if(j == 0) {
			cell.getLeft().draw(canvas, getXByIndex(j) - GRID_STEP / 2, getYByIndex(i), update);
		}
		// Draw roof ONLY for highest cells
		if(i == 0) {
			cell.getRoof().draw(canvas, getXByIndex(j), getYByIndex(i) - GRID_STEP / 2, update);
		}	
		cell.getRight().draw(canvas, getXByIndex(j) + GRID_STEP / 2, getYByIndex(i), update);
		if(cell.prize != null) {
			cell.prize.draw(canvas, getXByIndex(j), getYByIndex(i), update);
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
