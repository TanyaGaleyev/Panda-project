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
	private int trowOutCounter=0;
	private int prizesLeft = 0;

	public LevelModel(int lev) {
		row=5;
		col=10;
		heroX = 0;
		heroY = row - 1;
		LevelStorage storage = new LevelStorage();
		int[][][] mylevel = storage.getLevel(lev);
		int[][] prizes = storage.getPrizesMap(lev);
		levelGrid = new LevelCell[row][col];
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				levelGrid[i][j] = new LevelCell();
				levelGrid[i][j].setPrize(prizes[i][j]);
				prizesLeft += prizes[i][j];
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
							if(mylevel[i][j][k]==6){
								levelGrid[i][j] .createElecrto(1);
							}
							if(mylevel[i][j][k]==7){
								levelGrid[i][j] .createTrowOutRight(1);
							}
							if(mylevel[i][j][k]==8){
								levelGrid[i][j] .createTrowOutLeft(1);
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
						if(mylevel[i][j][k]==6){
							levelGrid[i][j] .createElecrto(0);
						}
						if(mylevel[i][j][k]==7){
							levelGrid[i][j] .createTrowOutRight(0);
						}
						if(mylevel[i][j][k]==8){
							levelGrid[i][j] .createTrowOutLeft(0);
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

	private void stayCheck(){
		if(motionAvaible(MotionType.FALL) ){
			motionType=MotionType.FALL;
		}else{
			switch(controlType){
			case UP:
				motionType=MotionType.JUMP;
				break;
			case LEFT:
				if(motionAvaible(MotionType.STEP_LEFT) ){
					motionType=MotionType.STEP_LEFT;
				}
				else{
					motionType=MotionType.STEP_LEFT_WALL;
				}
				break;
			case RIGHT:
				if(motionAvaible(MotionType.STEP_RIGHT) ){
					motionType=MotionType.STEP_RIGHT;
				}
				else{
					motionType=MotionType.STEP_RIGHT_WALL;
				}
				break;
			case IDLE:
			case DOWN:
				motionType=MotionType.STAY;
				break;

			}}}
	
	private void jump(){    	
		if(motionAvaible(MotionType.JUMP) ){
			motionType=MotionType.JUMP;
		}
		else{
			if(motionAvaible(MotionType.MAGNET)){
				motionType = MotionType.MAGNET;
			}else{
				motionType=MotionType.BEAT_ROOF;	}
		}

	}
	public void updateGame() {
		prizesLeft -= getHeroCell().removePrize();
		switch(motionType){
		case  PRE_JUMP:
			jump();
			break;
		case JUMP:
			switch(controlType){
			case DOWN:
				if(motionAvaible(MotionType.FALL_BLANSH) )
				{
					motionType=MotionType.FALL_BLANSH;
				}else{stayCheck();}
				break;
			case LEFT:
				if(motionAvaible(MotionType.JUMP_LEFT) ){
					motionType=MotionType.JUMP_LEFT;
				}
				else{
					motionType=MotionType.JUMP_LEFT_WALL;
				}
				break;
			case RIGHT:
				if(motionAvaible(MotionType.JUMP_RIGHT) ){
					motionType=MotionType.JUMP_RIGHT;
				}
				else{
					motionType=MotionType.JUMP_RIGHT_WALL;
				}
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
				stayCheck();
				break;
			default:
				motionType = MotionType.MAGNET;
				break;
			}

			break;	
		case TROW_LEFT:
			if(trowOutCounter==0){
				if(motionAvaible(MotionType.TROW_LEFT) ){
					trowOutCounter++;
				}
				else{
					motionType=MotionType.JUMP_LEFT_WALL;
				}
			}else{
				trowOutCounter=0;
				motionType=MotionType.STAY;
				updateGame();
				return;
			}
			break;
		case TROW_RIGHT:
			if(trowOutCounter==0){
				if(motionAvaible(MotionType.TROW_RIGHT) ){
					trowOutCounter++;
				}
				else{
					motionType=MotionType.JUMP_RIGHT_WALL;
				}
			}else{
				trowOutCounter=0;
				motionType=MotionType.STAY;
				updateGame();
				return;
			}
			break;
		default:
			switch (getHeroCell().getFloor().getType()){

			case ANGLE_RIGHT:
				if(motionAvaible(MotionType.STEP_RIGHT)){
					motionType=MotionType.STEP_RIGHT;

				}   
				else{
					motionType=MotionType.STEP_RIGHT_WALL;
				}				
				break;	
			case  ANGLE_LEFT:
				if(motionAvaible(MotionType.STEP_LEFT)){
					motionType=MotionType.STEP_LEFT;

				}   
				else{
					motionType=MotionType.STEP_LEFT_WALL;
				}		

				break;

			case  TRAMPOLINE:
				motionType=MotionType.PRE_JUMP;
				break;

			case  TROW_OUT_LEFT:
				if(motionAvaible(MotionType.STEP_LEFT)){
					motionType=MotionType.TROW_LEFT;

				}   else{
					motionType=MotionType.STEP_LEFT_WALL;
				}	
				break;

			case  TROW_OUT_RIGHT:
				if(motionAvaible(MotionType.STEP_RIGHT)){
					motionType=MotionType.TROW_RIGHT;

				}   else{
					motionType=MotionType.STEP_RIGHT_WALL;
				}	
				break;
			default:
				stayCheck();
				break;	
			}



			break;
		}
		updatePosition();
	}
	
	private void updatePosition() {
		controlType = UserControlType.IDLE;
		if(motionType == MotionType.FALL_BLANSH) heroY++;
		heroX += motionType.getXSpeed();
		heroY += motionType.getYSpeed();
		
	}
	private boolean motionAvaible(MotionType mt) {
		switch (mt) {
		case JUMP:
			if(getHeroCell().getRoof().getType() != PlatformType.NONE) return false;
			if(heroY - 1 < 0) return false;
			return true;
		case STEP_LEFT:
		case JUMP_LEFT:
		case TROW_LEFT:
			if(getHeroCell().getLeft().getType() != PlatformType.NONE) return false;
			if(heroX - 1 < 0) return false;
			return true;
		case STEP_RIGHT:
		case JUMP_RIGHT:
		case TROW_RIGHT:	
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
		return prizesLeft == 0;
	}
}
