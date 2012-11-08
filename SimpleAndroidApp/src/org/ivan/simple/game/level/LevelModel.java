package org.ivan.simple.game.level;

import java.util.ArrayList;
import java.util.HashMap;

import org.ivan.simple.UserControlType;
import org.ivan.simple.game.MotionType;

public class LevelModel {
	private LevelCell[][] levelGrid;
	protected int row;
	protected int col;
	public int xSpeed = 0;
	public int ySpeed = 0;
	public int heroX;
	public int heroY;
	private UserControlType controlType = UserControlType.IDLE;
	private UserControlType bufferedControlType = UserControlType.IDLE;
	private MotionType motionType = MotionType.STAY;
	private int prizesLeft = 0;
	private boolean lose = false;
	private boolean complete = false;
	private LevelCell winCell;
	private tpCoords tpStart;
	private tpCoords tpEnd;
	
	private class tpCoords {
		public tpCoords(int i, int j) {
			this.i = i;
			this.j = j;
		}
		int i;
		int j;
	}

	public LevelModel(int lev) {
		row=5;
		col=10;
		heroX = 0;
		heroY = row - 1;
		LevelStorage storage = new LevelStorage();
		int[][][] mylevel = storage.getLevel(lev);
		int[][] prizes = storage.getPrizesMap(lev);
		int[] winCellCoord = storage.getWinCell(lev);
		levelGrid = new LevelCell[row][col];
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				levelGrid[i][j] = new LevelCell();
				levelGrid[i][j].setPrize(prizes[i][j]);
				prizesLeft += prizes[i][j];
				int leftWallType = mylevel[i][j][0];
				// create left wall only for first column's cells
				if(j == 0) {
					if(leftWallType==0){
						levelGrid[i][j].createNone();
					}
					if(leftWallType==1){
						levelGrid[i][j].createSimple_V(0);
					}
					if(leftWallType==9) {
						levelGrid[i][j].createSpring(0);
					}
					if(leftWallType==10) {
						levelGrid[i][j].createSpike_V(0);
					}
					if(leftWallType==11) {
						levelGrid[i][j].createTeleport_l_V(0);
						tpStart = new tpCoords(i, j);
					}
					if(leftWallType==12) {
						levelGrid[i][j].createTeleport_r_V(0);
						tpEnd = new tpCoords(i, j);
					}
					// else set left wall as right wall of nearest left cell
				} else {
					levelGrid[i][j].left_wall = levelGrid[i][j - 1].right_wall;
				}
				
				int roofType = mylevel[i][j][1];
				// create roof only for first row's cells
				if(i == 0) {
					if(roofType==0){
						levelGrid[i][j].createNone();
					}
					if(roofType==1){
						levelGrid[i][j] .createSimple(1);
					}
					if(roofType==2){
						levelGrid[i][j] .createReduce(1);
					}
					if(roofType==3){
						levelGrid[i][j] .createAngleRight(1);
					}
					if(roofType==4){
						levelGrid[i][j] .createAngleLeft(1);
					}
					if(roofType==5){
						levelGrid[i][j] .createTrampoline(1);
					}
					if(roofType==6){
						levelGrid[i][j] .createElecrto(1);
					}
					if(roofType==7){
						levelGrid[i][j] .createTrowOutRight(1);
					}
					if(roofType==8){
						levelGrid[i][j] .createTrowOutLeft(1);
					}
					if(roofType==10){
						levelGrid[i][j] .createSpike(1);
					}
					// else set roof as floor of nearest upper cell
				} else {
					levelGrid[i][j].roof = levelGrid[i - 1][j].floor;
				}

				int rightWallType = mylevel[i][j][2];
				if(rightWallType==0){
					levelGrid[i][j] .createNone();
				}
				if(rightWallType==1){
					levelGrid[i][j].createSimple_V(1);
				}			
				if(rightWallType==9) {
					levelGrid[i][j].createSpring(1);
				}
				if(rightWallType==10) {
					levelGrid[i][j].createSpike_V(1);
				}
				if(rightWallType==11) {
					levelGrid[i][j].createTeleport_l_V(1);
					tpStart = new tpCoords(i, j);
				}
				if(rightWallType==12) {
					levelGrid[i][j].createTeleport_r_V(1);
					tpEnd = new tpCoords(i, j);
				}
				
				
				int floorType = mylevel[i][j][3];
				if(floorType==0){
					levelGrid[i][j].createNone();
				}
				if(floorType==1){
					levelGrid[i][j] .createSimple(0);
				}
				if(floorType==2){
					levelGrid[i][j] .createReduce(0);
				}
				if(floorType==3){
					levelGrid[i][j] .createAngleRight(0);
				}
				if(floorType==4){
					levelGrid[i][j] .createAngleLeft(0);
				}
				if(floorType==5){
					levelGrid[i][j] .createTrampoline(0);
				}
				if(floorType==6){
					levelGrid[i][j] .createElecrto(0);
				}
				if(floorType==7){
					levelGrid[i][j] .createTrowOutRight(0);
				}
				if(floorType==8){
					levelGrid[i][j] .createTrowOutLeft(0);
				}
				if(floorType==10){
					levelGrid[i][j] .createSpike(0);
				}
				
				if(i == winCellCoord[0] && j == winCellCoord[1]) {
					winCell = levelGrid[i][j];
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

	private void stayCheck() {
		if(controlType == UserControlType.DOWN && motionAvaible(MotionType.FALL_BLANSH)) {
			motionType=MotionType.FALL_BLANSH;
		} else if(motionAvaible(MotionType.FALL)) {
			motionType=MotionType.FALL;
		} else {
			switch(controlType){
			case UP:
				jump();
				break;
			case LEFT:
				moveLeft();
				break;
			case RIGHT:
				moveRight();
				break;
			case DOWN:
			case IDLE:
			default:
				motionType=MotionType.STAY;
				break;

			}
		}
	}
	
	private void jump() {    	
		if(motionAvaible(MotionType.JUMP)) {
			motionType=MotionType.JUMP;
		} else if(motionAvaible(MotionType.MAGNET)) {
			motionType = MotionType.MAGNET;
		} else {
			motionType=MotionType.BEAT_ROOF;	
		}
	}
	
	private void moveLeft() {
		if(motionAvaible(MotionType.JUMP_LEFT) ) {
			motionType=MotionType.JUMP_LEFT;
		} else if(getHeroCell().getLeft().getType() == PlatformType.SPRING) {
			motionType=MotionType.FLY_RIGHT;
		} else if(getHeroCell().getLeft().getType() == PlatformType.TELEPORT_L_V) {
			motionType=MotionType.TP_LEFT;
		} else {
			motionType=MotionType.JUMP_LEFT_WALL;
		}
	}
	
	private void moveRight() {
		if(motionAvaible(MotionType.JUMP_RIGHT) ) {
			motionType=MotionType.JUMP_RIGHT;
		} else if(getHeroCell().getRight().getType() == PlatformType.SPRING) {
			motionType=MotionType.FLY_LEFT;
		} else if(getHeroCell().getRight().getType() == PlatformType.TELEPORT_R_V) {
			motionType=MotionType.TP_RIGHT;
		} else {
			motionType=MotionType.JUMP_RIGHT_WALL;
		}
	}
	
	private void platformsCheck() {
		switch (getHeroCell().getFloor().getType()){

		case ANGLE_RIGHT:
			moveRight();			
			break;	
		case  ANGLE_LEFT:
			moveLeft();
			break;
		case  TRAMPOLINE:
			jump();
			break;
		case  THROW_OUT_LEFT:
			if(motionAvaible(MotionType.JUMP_LEFT)){
				motionType=MotionType.THROW_LEFT;
			} else {
				moveLeft();
			}	
			break;
		case  THROW_OUT_RIGHT:
			if(motionAvaible(MotionType.JUMP_RIGHT)){
				motionType=MotionType.THROW_RIGHT;
			} else {
				moveRight();
			}	
			break;
		default:
			stayCheck();
			break;	
		}
	}

	
	public void updateGame() {
		/* 
		 * buffering needed to prevent control type changing 
		 * before update finished
		 */
		synchronized(bufferedControlType) {
			controlType = bufferedControlType;
			bufferedControlType = UserControlType.IDLE;
		}
		
		// collect prize
		prizesLeft -= getHeroCell().removePrize();
		/*
		 * if all prizes are collected show win platform
		 * level will be complete after hero reaches win cell (with win floor platform now)
		 */
		if(prizesLeft == 0) {
			if(winCell.getFloor().getType() != PlatformType.WIN) {
				winCell.createWin(0);
			}
			if(getHeroCell() == winCell) {
				complete = true;
			}
		}
		if(motionType.isUncontrolable()) {
			controlType = UserControlType.IDLE;
		}
		motionType.continueMotion();
		switch(motionType){
		case JUMP:
			switch(controlType) {
			case DOWN:
				platformsCheck();
				break;
			case LEFT:
				moveLeft();
				break;
			case RIGHT:
				moveRight();
				break;
			case IDLE:
			case UP:	
				jump();
				break;
			}
			break;
		case  MAGNET:
			switch(controlType){
			case DOWN:
				platformsCheck();
				break;
			default:
				motionType = MotionType.MAGNET;
				break;
			}
			break;	
		case THROW_LEFT:
			if(motionType.getStage() == 1) {
				if(!motionAvaible(MotionType.JUMP_LEFT) ) {
					moveLeft();
				}
			} else {				
				platformsCheck();
			}
			break;
		case THROW_RIGHT:
			if(motionType.getStage() == 1) {
				if(!motionAvaible(MotionType.JUMP_RIGHT) ) {
					moveRight();
				}
			} else {
				platformsCheck();
			}
			break;
		case JUMP_LEFT_WALL:
			platformsCheck();
			break;
		case FLY_LEFT:
			switch(controlType) {
			case UP:
			case DOWN:
			case RIGHT:
				platformsCheck();
				break;
			default:
				if(motionAvaible(MotionType.JUMP_LEFT)) {
					motionType = MotionType.FLY_LEFT;
				} else {
					moveLeft();
				}
				break;
			}
			break;
		case JUMP_RIGHT_WALL:
			platformsCheck();
			break;
		case FLY_RIGHT:
			switch(controlType) {
			case UP:
			case DOWN:
			case LEFT:
				platformsCheck();
				break;
			default:
				if(motionAvaible(MotionType.JUMP_RIGHT)) {
					motionType = MotionType.FLY_RIGHT;
				} else {
					moveRight();
				}
				break;
			}
			break;
		default:
			platformsCheck();
			break;
		}
		updatePosition();
	}
	
	private void updatePosition() {
		if(motionType == MotionType.TP_LEFT) {
			heroX = tpEnd.j;
			heroY = tpEnd.i;
			return;
		}
		if(motionType == MotionType.TP_RIGHT) {
			heroX = tpStart.j;
			heroY = tpStart.i;
			return;
		}
		if(motionType == MotionType.FALL_BLANSH) heroY++;
		heroX += motionType.getXSpeed();
		heroY += motionType.getYSpeed();
	}
	
	private boolean motionAvaible(MotionType mt) {
		switch (mt) {
		case JUMP:
			if(mt.getYSpeed() != 0 && getHeroCell().getRoof().getType() == PlatformType.SPIKE) lose = true;
			if(mt.getYSpeed() != 0 && getHeroCell().getRoof().getType() != PlatformType.NONE) return false;
			if(heroY + mt.getYSpeed() < 0) return false;
			return true;
//		case STEP_LEFT:
		case JUMP_LEFT:
		case THROW_LEFT:
			if(getHeroCell().getLeft().getType() == PlatformType.SPIKE_V) lose = true;
			if(getHeroCell().getLeft().getType() != PlatformType.NONE) return false;
			if(heroX + mt.getXSpeed() < 0) return false;
			return true;
//		case STEP_RIGHT:
		case JUMP_RIGHT:
		case THROW_RIGHT:	
			if(getHeroCell().getRight().getType() == PlatformType.SPIKE_V) lose = true;
			if(getHeroCell().getRight().getType() != PlatformType.NONE) return false;
			if(heroX + mt.getXSpeed() > col - 1) return false;
			return true;
		case FALL:
			if(getHeroCell().getFloor().getType() != PlatformType.NONE) return false;
			if(heroY + 1 > row - 1) lose = true;
			return true;
		case FALL_BLANSH:
			if(getHeroCell().getFloor().getType() != PlatformType.NONE) return false;
			if(heroY + 2 > row - 1) return false;
			if(getCell(heroY + 1, heroX).getFloor().getType() != PlatformType.NONE) return false;
			return true;
//		case STEP_LEFT_WALL:
//			return true;
//		case STEP_RIGHT_WALL:
//			return true;
		case BEAT_ROOF:
			if(getHeroCell().getRoof().getType() != PlatformType.NONE) return true;
			if(heroY - 1 < 0) return true;
			return false;
		case MAGNET:
			if(getHeroCell().getRoof().getType() == PlatformType.ELECTRO) return true;
			return false;	
		default:
			return false;
		}
	}
	
	public MotionType getMotionType() {
		return motionType;
	}

	public boolean isComplete() {
		return complete;
	}
	
	public boolean isLost() {
		return lose;
	}
	
	public void setControlType(UserControlType control) {
		synchronized(bufferedControlType) {
			bufferedControlType = control;
		}
	}
	
	public UserControlType getControlType() {
		return bufferedControlType;
	}
	
}
