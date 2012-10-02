package org.ivan.simple.level;

public class LevelModel {
	private LevelCell[][] levelGrid;
	protected int row;
	protected int col;
	
	public LevelModel(int lev) {
		row=5;
		col=10;
		int[][][] mylevel = new LevelStorage().getLevel(lev);
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
	
	public LevelCell getCell(int i, int j) {
		return levelGrid[i][j];
	}
}
