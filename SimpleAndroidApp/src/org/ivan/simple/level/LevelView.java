package org.ivan.simple.level;

import org.ivan.simple.GameView;

import android.graphics.Canvas;
import org.ivan.simple.level.LevelStorage;

public class LevelView {

	private LevelStorage level;
	private LevelCell[][] levelGrid;
	private int row;
	private int col;
	public LevelView(int lev) {
		row=5;
		col=10;
		level=new LevelStorage();
		int[][][] mylevel=level.getLevel(lev);
		levelGrid = new LevelCell[row][col];
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				for(int k=0;k<4;k++){	
				if(k==0){
					
					
					
				}
				if(k==1){
					
					
				}
				if(k==2){
					
					
				}
				if(k==3){
					if(mylevel[i][j][k]==0){
						levelGrid[i][j] = LevelCell.createNone();
					}
            	if(mylevel[i][j][k]==1){
            		levelGrid[i][j] = LevelCell.createSimple();
						
					}	
					
				}
				}
			}
			
			
			
			
		}
		

	}
	
	public void onDraw(Canvas canvas) {
		
		
		for(int i = 0; i <row; i++) {
			for(int j = 0; j <col; j++) {
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
		return (GameView.GRID_STEP * (i + 1) + GameView.GRID_STEP / 2)-15;
	}

}
