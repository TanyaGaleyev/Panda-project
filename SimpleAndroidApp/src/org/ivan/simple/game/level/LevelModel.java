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
	private MotionType prevMotion = MotionType.STAY;
	private MotionType motionType = MotionType.STAY;
	private int prizesLeft = 0;
	private boolean lose = false;
	private boolean complete = false;
	private LevelCell winCell;
	private HashMap<CellCoords, TpPeer> tpGroupMap = new HashMap<CellCoords, TpPeer>();
	private ArrayList<Platform> switchList = new ArrayList<Platform>();
	private ArrayList<Platform> unlockList = new ArrayList<Platform>();
	private HashMap<CellCoords, CellCoords> tpSequence = new HashMap<CellCoords, CellCoords>();
	
	private class CellCoords {
		int i;
		int j;
		
		CellCoords(int i, int j) {
			this.i = i;
			this.j = j;
		}
		
		
		// hash and equals methods are needed to properly get right mapping for
		// cells with equal coordinates
		@Override
		public boolean equals(Object o) {
			if(o == null) return false;
			if(o == this) return true;
			if(!(o instanceof CellCoords)) return false;
			CellCoords oCell = (CellCoords) o;
			if(this.i != oCell.i) return false;
			if(this.j != oCell.j) return false;
			return true;
		}
		
		@Override
		public int hashCode() {
			return i * 100 + j;
		}
	}
	
	private class TpPeer {
		int startRow;
		int startCol;
		int endRow;
		int endCol;
		
		void setStartCoords(CellCoords coords) {
			startRow = coords.i;
			startCol = coords.j;
		}
		
		void setEndCoords(CellCoords coords) {
			endRow = coords.i;
			endCol = coords.j;
		}
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
		ArrayList<TpPeer> groups = new ArrayList<TpPeer>();
		CellCoords prevTpCell = null;
		CellCoords firstTpCell = null;
		int tpStartGroupId = 0;
		int tpEndGroupId = 0;
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				levelGrid[i][j] = new LevelCell();
				levelGrid[i][j].setPrize(prizes[i][j]);
				prizesLeft += prizes[i][j];
				int leftWallType = mylevel[i][j][0];
				// create left wall only for first column's cells
				if(j == 0) {
					levelGrid[i][j].createLeft(leftWallType);
					if(leftWallType==11) {
						CellCoords coords = new CellCoords(i, j);
						if(tpStartGroupId >=
								tpEndGroupId) {
							TpPeer newPeer = new TpPeer();
							newPeer.setStartCoords(coords);
							groups.add(newPeer);
							tpGroupMap.put(coords, newPeer);
						} else {
							TpPeer existedPeer = groups.get(tpStartGroupId);
							existedPeer.setStartCoords(coords);
							tpGroupMap.put(coords, existedPeer);
						}
						tpStartGroupId++;
					}
					if(leftWallType==12) {
						CellCoords coords = new CellCoords(i, j - 1);
						if(tpEndGroupId >= tpStartGroupId) {
							TpPeer newPeer = new TpPeer();
							newPeer.setEndCoords(coords);
							groups.add(newPeer);
							tpGroupMap.put(coords, newPeer);
						} else {
							TpPeer existedPeer = groups.get(tpEndGroupId);
							existedPeer.setEndCoords(coords);
							tpGroupMap.put(coords, existedPeer);
						}
						tpEndGroupId++;
					}
					if(leftWallType==17) {
						switchList.add(levelGrid[i][j].getLeft());
					}
					if(leftWallType==18) {
						unlockList.add(levelGrid[i][j].getLeft());
					}
					// else set left wall as right wall of nearest left cell
				} else {
					levelGrid[i][j].left_wall = levelGrid[i][j - 1].right_wall;
				}
				
				int roofType = mylevel[i][j][1];
				// create roof only for first row's cells
				if(i == 0) {
					levelGrid[i][j].createRoof(roofType);
					if(roofType == 23) {
						CellCoords cell = new CellCoords(i, j);
						if(prevTpCell != null) {
							tpSequence.put(prevTpCell, cell);
						} else {
							firstTpCell = cell;
						}
						prevTpCell = cell;
					}
					// else set roof as floor of nearest upper cell
				} else {
					levelGrid[i][j].roof = levelGrid[i - 1][j].floor;
				}

				int rightWallType = mylevel[i][j][2];
				levelGrid[i][j] .createRight(rightWallType);
				if(rightWallType==11) {
					CellCoords coords = new CellCoords(i, j + 1);
					if(tpStartGroupId >= tpEndGroupId) {
						TpPeer newPeer = new TpPeer();
						newPeer.setStartCoords(coords);
						groups.add(newPeer);
						tpGroupMap.put(coords, newPeer);
					} else {
						TpPeer existedPeer = groups.get(tpStartGroupId);
						existedPeer.setStartCoords(coords);
						tpGroupMap.put(coords, existedPeer);
					}
					tpStartGroupId++;
				}
				if(rightWallType==12) {
					CellCoords coords = new CellCoords(i, j);
					if(tpEndGroupId >= tpStartGroupId) {
						TpPeer newPeer = new TpPeer();
						newPeer.setEndCoords(coords);
						groups.add(newPeer);
						tpGroupMap.put(coords, newPeer);
					} else {
						TpPeer existedPeer = groups.get(tpEndGroupId);
						existedPeer.endRow = i;
						existedPeer.endCol = j;
						tpGroupMap.put(coords, existedPeer);
					}
					tpEndGroupId++;
				}
				if(rightWallType==17) {
					switchList.add(levelGrid[i][j].getRight());
				}
				if(rightWallType==18) {
					unlockList.add(levelGrid[i][j].getRight());
				}
				
				
				int floorType = mylevel[i][j][3];
				levelGrid[i][j].createFloor(floorType);
				if(floorType == 23) {
					CellCoords cell = new CellCoords(i, j);
					if(prevTpCell != null) {
						tpSequence.put(prevTpCell, cell);
					} else {
						firstTpCell = cell;
					}
					prevTpCell = cell;
				}
				
				if(i == winCellCoord[0] && j == winCellCoord[1]) {
					winCell = levelGrid[i][j];
				}
				
				if(prevTpCell != null && firstTpCell != null) {
					tpSequence.put(prevTpCell, firstTpCell);
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
		} else if(getHeroCell().getLeft().getType() == PlatformType.GLUE_V) {
			motionType=MotionType.STICK_LEFT;
		} else {
			motionType=MotionType.JUMP_LEFT_WALL;
		}
	}
	
	private void moveRight() {
		if(motionAvaible(MotionType.JUMP_RIGHT) ) {
			motionType=MotionType.JUMP_RIGHT;
		} else if(getHeroCell().getRight().getType() == PlatformType.SPRING) {
			motionType=MotionType.FLY_LEFT;
		} else if(getHeroCell().getRight().getType() == PlatformType.GLUE_V) {
			motionType=MotionType.STICK_RIGHT;
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
		case SLICK:
			if(controlType == UserControlType.IDLE &&
				(motionType == MotionType.JUMP_LEFT || motionType == MotionType.JUMP_RIGHT_WALL)) {
				moveLeft();
			} else if (controlType == UserControlType.IDLE &&
					(motionType == MotionType.JUMP_RIGHT || motionType == MotionType.JUMP_LEFT_WALL)) {
				moveRight();
			} else {
				stayCheck();
			}
			break;
		case SLOPE:
			if(getHeroCell().getFloor().getStatus() == 1) {
				moveLeft();
			} else if(getHeroCell().getFloor().getStatus() == 2) {
				moveRight();
			} else {
				stayCheck();
			}
			break;
		case ONE_WAY_DOWN:
			if(controlType == UserControlType.DOWN) {
				if(heroY + 1 > row - 1) lose = true;
				motionType = MotionType.FALL;
			} else {
				stayCheck();
			}
			break;
		case WAY_UP_DOWN:
			if(controlType == UserControlType.DOWN && motionType != MotionType.JUMP) {
				if(heroY + 1 > row - 1) lose = true;
				motionType = MotionType.FALL;
			} else {
				stayCheck();
			}
			break;
		case BRICK:
			if(controlType == UserControlType.UP) {
				controlType = UserControlType.IDLE;
			}
			stayCheck();
			break;
		case GLUE:
			if(prevMotion ==  MotionType.STAY && controlType == UserControlType.UP) {
				controlType = UserControlType.IDLE;
			}
			stayCheck();
			break;
		case TELEPORT:
			if(controlType != UserControlType.LEFT && controlType != UserControlType.RIGHT) {
				motionType = MotionType.TP;
			} else {
				stayCheck();
			}
			break;
		default:
			stayCheck();
			break;	
		}
	}
	
	private void checkTeleport() {
		switch(motionType) {
		case JUMP_LEFT:
		case THROW_LEFT:
		case FLY_LEFT:
			if(!(motionType == MotionType.FLY_LEFT && prevMotion != MotionType.FLY_LEFT) && getHeroCell().getLeft().getType() == PlatformType.TELEPORT_L_V) {
				prevMotion = motionType;
				motionType = MotionType.TP_LEFT;
			}
			break;
		case JUMP_RIGHT:
		case THROW_RIGHT:
		case FLY_RIGHT:
			if(!(motionType == MotionType.FLY_RIGHT && prevMotion != MotionType.FLY_RIGHT) && getHeroCell().getRight().getType() == PlatformType.TELEPORT_R_V) {
				prevMotion = motionType;
				motionType = MotionType.TP_RIGHT;
			}
			break;
		default:
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
				// TODO careful with roof (of underlying cell)
				winCell.createFloor(PlatformType.WIN);
			}
			if(getHeroCell() == winCell) {
				complete = true;
			}
		}
		tryToUnlock();
		if(motionType.isUncontrolable()) {
			controlType = UserControlType.IDLE;
		}
		if(motionType == MotionType.TP_LEFT || motionType == MotionType.TP_RIGHT) {
//			switch(prevMotion) {
//			case JUMP_LEFT:
//			case THROW_LEFT:
//			case FLY_LEFT:
//			case JUMP_RIGHT:
//			case THROW_RIGHT:
//			case FLY_RIGHT:
				motionType = prevMotion;
//				break;
//			default: 
//				break;
//			}
		}
		prevMotion = motionType;
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
			default:
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
		case STICK_LEFT:
			switch(controlType) {
			case DOWN:
			case RIGHT:
				platformsCheck();
				break;
			default:
				motionType = MotionType.STICK_LEFT;
				break;
			}
			break;
		case STICK_RIGHT:
			switch(controlType) {
			case DOWN:
			case LEFT:
				platformsCheck();
				break;
			default:
				motionType = MotionType.STICK_RIGHT;
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
		case TP_LEFT:
//			if(!MotionType.FLY_LEFT.isUncontrolable() && motionAvaible(MotionType.JUMP_LEFT)) {
//				motionType = MotionType.FLY_LEFT;
//			} else if(MotionType.THROW_LEFT.getStage() == 1 && motionAvaible(MotionType.JUMP_LEFT)) {
//				motionType = MotionType.THROW_LEFT;
//			} else {
				platformsCheck();
//			}
			break;
		case FLY_LEFT:
			switch(controlType) {
			case UP:
			case DOWN:
			case RIGHT:
				motionType.finishMotion();
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
		case TP_RIGHT:
//			if(!MotionType.FLY_RIGHT.isUncontrolable() && motionAvaible(MotionType.JUMP_RIGHT)) {
//				motionType = MotionType.FLY_RIGHT;
//			} else {
				platformsCheck();
//			}
			break;
		case FLY_RIGHT:
			switch(controlType) {
			case UP:
			case DOWN:
			case LEFT:
				motionType.finishMotion();
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
		case TP:
			if(motionType.getStage() == 0) {
				if(controlType == UserControlType.UP || controlType == UserControlType.IDLE) {
					// to start without pre jump
					MotionType.JUMP.continueMotion();
					jump();
				} else {
					platformsCheck();
				}
			}
			break;
		case FALL_BLANSH:
			if(motionType.getStage() == 1) {
				motionType = MotionType.FALL_BLANSH;
			} else {
				platformsCheck();
			}
			break;
		default:
			platformsCheck();
			break;
		}
		checkTeleport();
		startFinishMotions();
		updatePosition();
	}
	
	private void updatePosition() {
		if(motionType == MotionType.TP_LEFT) {
			TpPeer toCoords = tpGroupMap.get(new CellCoords(heroY, heroX));
			heroX = toCoords.endCol;
			heroY = toCoords.endRow;
			return;
		}
		if(motionType == MotionType.TP_RIGHT) {
			TpPeer toCoords = tpGroupMap.get(new CellCoords(heroY, heroX));
			heroX = toCoords.startCol;
			heroY = toCoords.startRow;
			return;
		}
		if(motionType == MotionType.TP && motionType.getStage() == 1) {
			CellCoords nextCell = tpSequence.get(new CellCoords(heroY, heroX)); 
			heroX = nextCell.j;
			heroY = nextCell.i;
		}
		heroX += motionType.getXSpeed();
		heroY += motionType.getYSpeed();
	}
	
	private boolean motionAvaible(MotionType mt) {
		switch (mt) {
		case JUMP:
			if(mt.getYSpeed() != 0 && getHeroCell().getRoof().getType() == PlatformType.SPIKE) lose = true;
			if(heroY + mt.getYSpeed() < 0) return false;
			if(getHeroCell().getRoof().getType() == PlatformType.ONE_WAY_UP) return true;
			if(getHeroCell().getRoof().getType() == PlatformType.WAY_UP_DOWN) return true;
			if(getHeroCell().getRoof().getType() == PlatformType.TRANSPARENT) return true;
			if(mt.getYSpeed() != 0 && getHeroCell().getRoof().getType() != PlatformType.NONE) return false;
			return true;
		case FLY_LEFT:
		case JUMP_LEFT:
		case THROW_LEFT:
			if(getHeroCell().getLeft().getType() == PlatformType.SPIKE_V) lose = true;
			if(getHeroCell().getLeft().getType() == PlatformType.TELEPORT_L_V) return true;
			if(heroX + mt.getXSpeed() < 0) return false;
			if(getHeroCell().getLeft().getType() == PlatformType.ONE_WAY_LEFT) return true;
			if(getHeroCell().getLeft().getType() == PlatformType.LIMIT &&
					getHeroCell().getLeft().getStatus() < 3) return true;
			if(getHeroCell().getLeft().getType() == PlatformType.TRANSPARENT_V) return true;
			if(getHeroCell().getLeft().getType() != PlatformType.NONE) return false;
			return true;
		case FLY_RIGHT:
		case JUMP_RIGHT:
		case THROW_RIGHT:	
			if(getHeroCell().getRight().getType() == PlatformType.SPIKE_V) lose = true;
			if(getHeroCell().getRight().getType() == PlatformType.TELEPORT_R_V) return true;
			if(heroX + mt.getXSpeed() > col - 1) return false;
			if(getHeroCell().getRight().getType() == PlatformType.ONE_WAY_RIGHT) return true;
			if(getHeroCell().getRight().getType() == PlatformType.LIMIT &&
					getHeroCell().getRight().getStatus() < 3) return true;
			if(getHeroCell().getRight().getType() == PlatformType.TRANSPARENT_V) return true;
			if(getHeroCell().getRight().getType() != PlatformType.NONE) return false;
			return true;
		case FALL:
			if(getHeroCell().getFloor().getType() != PlatformType.NONE &&
				getHeroCell().getFloor().getType() != PlatformType.TRANSPARENT) return false;
			if(heroY + 1 > row - 1) lose = true;
			return true;
		case FALL_BLANSH:
			if(getHeroCell().getFloor().getType() != PlatformType.NONE &&
					getHeroCell().getFloor().getType() != PlatformType.TRANSPARENT) return false;
			if(heroY + 2 > row - 1) return false;
			if(getCell(heroY + 1, heroX).getFloor().getType() != PlatformType.NONE &&
					getCell(heroY + 1, heroX).getFloor().getType() != PlatformType.TRANSPARENT) return false;
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
	
	public MotionType getPrevMotion() {
		return prevMotion;
	}

	public boolean isComplete() {
		return complete;
	}
	
	public boolean isLost() {
		return lose;
	}
	
	public boolean tryToUnlock() {
		if(unlockList.isEmpty()) return true;
		if(switchList.isEmpty()) return false;
		int status = switchList.get(0).getStatus();
		for(Platform wall : switchList) {
			if(wall.getStatus() != status) return false;
		}
		unlockList.get(0).unlock();
		unlockList.remove(0);
		return true;
	}
	
	public void setControlType(UserControlType control) {
		synchronized(bufferedControlType) {
			bufferedControlType = control;
		}
	}
	
	public UserControlType getControlType() {
		return bufferedControlType;
	}
	
	private void startFinishMotions() {
		if(prevMotion != motionType) {
			if(!(prevMotion == MotionType.FLY_LEFT && motionType == MotionType.TP_LEFT) &&
					!(prevMotion == MotionType.FLY_RIGHT && motionType == MotionType.TP_RIGHT) &&
					!(prevMotion == MotionType.THROW_LEFT && motionType == MotionType.TP_LEFT) &&
					!(prevMotion == MotionType.THROW_RIGHT && motionType == MotionType.TP_RIGHT) &&
					!(prevMotion == MotionType.FLY_LEFT && motionType == MotionType.FLY_RIGHT) &&
					!(prevMotion == MotionType.FLY_RIGHT && motionType == MotionType.FLY_LEFT)) {
				prevMotion.finishMotion();
			}
			if(
//					!(prevMotion == MotionType.TP_LEFT && motionType == MotionType.FLY_LEFT) &&
//					!(prevMotion == MotionType.TP_RIGHT && motionType == MotionType.FLY_RIGHT) &&
//					!(prevMotion == MotionType.TP_LEFT && motionType == MotionType.THROW_LEFT) &&
//					!(prevMotion == MotionType.TP_RIGHT && motionType == MotionType.THROW_RIGHT) &&
					!(prevMotion == MotionType.TP && motionType == MotionType.JUMP)
					) {
				motionType.startMotion();
			}
		}
	}
	
}
