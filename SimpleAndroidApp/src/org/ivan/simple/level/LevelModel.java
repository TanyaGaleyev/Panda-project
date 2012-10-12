package org.ivan.simple.level;

import org.ivan.simple.MotionType;
import org.ivan.simple.UserControlType;

public class LevelModel {
	private LevelCell[][] levelGrid;
	protected int row;
	protected int col;
	public int xSpeed = 0;
	public int ySpeed = 0;
	public int heroX;
	public int heroY;
	public UserControlType controlType = UserControlType.IDLE;
	private MotionType motionType = MotionType.STAY;
	
	public LevelModel(int lev) {
		row=5;
		col=10;
		heroX = 0;
		heroY = row - 1;
		int[][][] mylevel = new LevelStorage().getLevel(lev);
		levelGrid = new LevelCell[row][col];
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				levelGrid[i][j] =new LevelCell();
				for(int k=0;k<4;k++){	
				if(k==0){
					// create left wall only for first column's cells
					if(j == 0) {
						if(mylevel[i][j][k]==0){
							levelGrid[i][j].createNone();
						}
						if(mylevel[i][j][k]==1){
							levelGrid[i][j].createSimple_V(0);
						}
					// else set left wall as right wall of nearest left cell
					} else {
						levelGrid[i][j].left_wall = levelGrid[i][j - 1].right_wall;
					}
					
				}
				if(k==1){
					// create roof only for first row's cells
					if(i == 0) {
						if(mylevel[i][j][k]==0){
							levelGrid[i][j].createNone();
						}
	            	    if(mylevel[i][j][k]==1){
	            	    	levelGrid[i][j] .createSimple(1);
						}
	            	    if(mylevel[i][j][k]==2){
	            	    	levelGrid[i][j] .createReduce(1);
						}
	            	    if(mylevel[i][j][k]==3){
	            	    	levelGrid[i][j] .createAngleRight(1);
						}
	            	    if(mylevel[i][j][k]==4){
	            	    	levelGrid[i][j] .createAngleLeft(1);
						}
	            	    if(mylevel[i][j][k]==5){
	            	    	levelGrid[i][j] .createTrampoline(1);
	            	    }
            	    // else set roof as floor of nearest upper cell
					} else {
						levelGrid[i][j].roof = levelGrid[i - 1][j].floor;
					}
					
				}
				if(k==2){
					if(mylevel[i][j][k]==0){
						levelGrid[i][j] .createNone();
					}
            	    if(mylevel[i][j][k]==1){
            	    	levelGrid[i][j].createSimple_V(1);
					}			
					
				}
				if(k==3){
					if(mylevel[i][j][k]==0){
						levelGrid[i][j].createNone();
					}
            	    if(mylevel[i][j][k]==1){
            	    	levelGrid[i][j] .createSimple(0);
					}
            	    if(mylevel[i][j][k]==2){
            	    	levelGrid[i][j] .createReduce(0);
					}
            	    if(mylevel[i][j][k]==3){
            	    	levelGrid[i][j] .createAngleRight(0);
					}
            	    if(mylevel[i][j][k]==4){
            	    	levelGrid[i][j] .createAngleLeft(0);
					}
					if(mylevel[i][j][k]==5){
						levelGrid[i][j] .createTrampoline(0);
					}
				}
				}
			}
			
			
			
			
		}
	}
	
	public LevelCell getCell(int i, int j) {
		return levelGrid[i][j];
	}
	
	public LevelCell getHeroCell() {
		return levelGrid[heroY][heroX];
	}
	
	public void updateGame() {
		switch(getHeroCell().getFloor().getType()) {
		case TRAMPOLINE:
			if(motionAvaible(MotionType.JUMP) && motionType!=MotionType.PRE_JUMP ){
				motionType = MotionType.PRE_JUMP;}
			
			else {
				if(motionAvaible(MotionType.JUMP)){
					motionType = MotionType.JUMP;}
			}
			break;
		case ANGLE_RIGHT:
			if(motionAvaible(MotionType.STEP_RIGHT)) {
				motionType = MotionType.STEP_RIGHT;
			} else if(motionAvaible(MotionType.STEP_RIGHT_WALL)) {
				motionType = MotionType.STEP_RIGHT_WALL;
			}
			break;
		case ANGLE_LEFT:
			if(motionAvaible(MotionType.STEP_LEFT)) {
				motionType = MotionType.STEP_LEFT;	
			} else if(motionAvaible(MotionType.STEP_LEFT_WALL)) {
				motionType = MotionType.STEP_LEFT_WALL;
			}
			break;
		default:
		switch (motionType) {
		case STEP_LEFT_WALL:
		case STEP_RIGHT_WALL:
		case FALL:
		case FALL_BLANSH:
		case STAY:
			switch (controlType) {
			case UP:
				if(motionAvaible(MotionType.FALL_BLANSH)) {
					motionType = MotionType.FALL_BLANSH;
				} else if(motionAvaible(MotionType.FALL)) {
					motionType = MotionType.FALL;
				} else if(motionAvaible(MotionType.JUMP)) {
					motionType = MotionType.PRE_JUMP;
				} else {
					motionType = MotionType.STAY;
				}
				break;
			case LEFT:
				if(motionAvaible(MotionType.FALL_BLANSH)) {
					motionType = MotionType.FALL_BLANSH;
				} else if(motionAvaible(MotionType.FALL)) {
					motionType = MotionType.FALL;
				} else if(motionAvaible(MotionType.STEP_LEFT)) {
					motionType = MotionType.STEP_LEFT;
				} else if(motionAvaible(MotionType.STEP_LEFT_WALL)) {
					motionType = MotionType.STEP_LEFT_WALL;
				} else {
					motionType = MotionType.STAY;
				}
				break;
			case RIGHT:
				if(motionAvaible(MotionType.FALL_BLANSH)) {
					motionType = MotionType.FALL_BLANSH;
				} else if(motionAvaible(MotionType.FALL)) {
					motionType = MotionType.FALL;
				} else if(motionAvaible(MotionType.STEP_RIGHT)) {
					motionType = MotionType.STEP_RIGHT;
				} else if(motionAvaible(MotionType.STEP_RIGHT_WALL)) {
					motionType = MotionType.STEP_RIGHT_WALL;
				} else {
					motionType = MotionType.STAY;
				}
				break;
			default:
				if(motionAvaible(MotionType.FALL_BLANSH)) {
					motionType = MotionType.FALL_BLANSH;
				} else if(motionAvaible(MotionType.FALL)) {
					motionType = MotionType.FALL;
				} else {
					motionType = MotionType.STAY;
				}
				break;
			}
			break;
		case JUMP:
			switch (controlType) {
			case DOWN:
				if(motionAvaible(MotionType.FALL_BLANSH)) {
					motionType = MotionType.FALL_BLANSH;
				} else {
					motionType = MotionType.FALL;
				}
				break;
			case LEFT:
				if(motionAvaible(MotionType.JUMP_LEFT)) {
					motionType = MotionType.JUMP_LEFT;
				} else if(motionAvaible(MotionType.JUMP)) {
					motionType = MotionType.JUMP;
				} else if(motionAvaible(MotionType.FALL_BLANSH)) {
					motionType = MotionType.FALL_BLANSH;
				} else {
					motionType = MotionType.FALL;
				}
				break;
			case RIGHT:
				if(motionAvaible(MotionType.JUMP_RIGHT)) {
					motionType = MotionType.JUMP_RIGHT;
				} else if(motionAvaible(MotionType.JUMP)) {
					motionType = MotionType.JUMP;
				} else if(motionAvaible(MotionType.FALL_BLANSH)) {
					motionType = MotionType.FALL_BLANSH;
				} else {
					motionType = MotionType.FALL;
				}
				break;
			default:
				if(motionAvaible(MotionType.JUMP)) {
					motionType = MotionType.JUMP;
				} else if(motionAvaible(MotionType.FALL_BLANSH)) {
					motionType = MotionType.FALL_BLANSH;
				} else if (motionAvaible(MotionType.FALL)){
					motionType = MotionType.FALL;
				} else {
					motionType = MotionType.STAY;
				}
				break;
			}
			break;
		case STEP_LEFT:
		case STEP_RIGHT:
		case JUMP_LEFT:
		case JUMP_RIGHT:
			switch(controlType) {
			case UP:
				if(motionAvaible(MotionType.FALL_BLANSH)) {
					motionType = MotionType.FALL_BLANSH;
				} else if(motionAvaible(MotionType.FALL)) {
					motionType = MotionType.FALL;
				} else if(motionAvaible(MotionType.JUMP)) {
					motionType = MotionType.JUMP;
				} else {
					motionType = MotionType.STAY;
				}
				break;
			case LEFT:
				if(motionAvaible(MotionType.FALL_BLANSH)) {
					motionType = MotionType.FALL_BLANSH;
				} else if(motionAvaible(MotionType.FALL)) {
					motionType = MotionType.FALL;
				} else if(motionAvaible(MotionType.STEP_LEFT)) {
					motionType = MotionType.STEP_LEFT;
				} else if(motionAvaible(MotionType.STEP_LEFT_WALL)) {
					motionType = MotionType.STEP_LEFT_WALL;
				} else {
					motionType = MotionType.STAY;
				}
				break;
			case RIGHT:
				if(motionAvaible(MotionType.FALL_BLANSH)) {
					motionType = MotionType.FALL_BLANSH;
				} else if(motionAvaible(MotionType.FALL)) {
					motionType = MotionType.FALL;				
				} else if(motionAvaible(MotionType.STEP_RIGHT)) {
					motionType = MotionType.STEP_RIGHT;
				} else if(motionAvaible(MotionType.STEP_RIGHT_WALL)) {
					motionType = MotionType.STEP_RIGHT_WALL;
				} else {
					motionType = MotionType.STAY;
				}
				break;
			default:
				if(motionAvaible(MotionType.FALL_BLANSH)) {
					motionType = MotionType.FALL_BLANSH;
				} else if(motionAvaible(MotionType.FALL)) {
					motionType = MotionType.FALL;
				} else {
					motionType = MotionType.STAY;
				}	
				break;
			}
			break;
		case PRE_JUMP:
			motionType = MotionType.JUMP;
			break;
		default:
			break;
		}}
		controlType = UserControlType.IDLE;
		
		switch (motionType) {
		case JUMP:
			xSpeed = 0;
			ySpeed = -1;
			break;
		case FALL_BLANSH:
			heroY += 1;
		case FALL:
			xSpeed = 0;
			ySpeed = 1;
			break;
		case STEP_LEFT:
			xSpeed = -1;
			ySpeed = 0;
			break;
		case STEP_RIGHT:
			xSpeed = 1;
			ySpeed = 0;
			break;
		case JUMP_LEFT:
			xSpeed = -1;
			ySpeed = 0;
			break;
		case JUMP_RIGHT:
			xSpeed = 1;
			ySpeed = 0;
			break;
		default:
			xSpeed = 0;
			ySpeed = 0;
			break;
		}
		heroX += xSpeed;
		heroY += ySpeed;
	}
	
	private boolean motionAvaible(MotionType mt) {
		switch (mt) {
		case JUMP:
			if(getHeroCell().getRoof().getType() != PlatformType.NONE) return false;
			if(heroY - 1 < 0) return false;
			return true;
		case STEP_LEFT:
		case JUMP_LEFT:
			if(getHeroCell().getLeft().getType() != PlatformType.NONE) return false;
			if(heroX - 1 < 0) return false;
			return true;
		case STEP_RIGHT:
		case JUMP_RIGHT:
			if(getHeroCell().getRight().getType() != PlatformType.NONE) return false;
			if(heroX + 1 > col - 1) return false;
			return true;
		case FALL:
			if(getHeroCell().getFloor().getType() != PlatformType.NONE) return false;
			if(heroY + 1 > row - 1) return false;
			return true;
		case FALL_BLANSH:
			if(getHeroCell().getFloor().getType() != PlatformType.NONE) return false;
			if(heroY + 2 > row - 1) return false;
			if(getCell(heroY + 1, heroX).getFloor().getType() != PlatformType.NONE) return false;
			return true;
		case STEP_LEFT_WALL:
			return true;
		case STEP_RIGHT_WALL:
			return true;
		default:
			return false;
		}
	}
	
	public MotionType getMotionType() {
		return motionType;
	}
}
