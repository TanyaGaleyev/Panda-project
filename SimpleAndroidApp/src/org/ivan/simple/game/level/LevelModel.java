package org.ivan.simple.game.level;

import java.util.ArrayList;
import java.util.HashMap;

import org.ivan.simple.UserControlType;
import org.ivan.simple.game.ContainerMotion;
import org.ivan.simple.game.Motion;
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
	private Motion prevMotion = new Motion(MotionType.STAY);
	private Motion motion = new Motion(MotionType.STAY); 
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

	private MotionType stayCheck() {
		MotionType motionType;
		if(controlType == UserControlType.DOWN && motionAvaible(MotionType.FALL_BLANSH)) {
			motionType=MotionType.FALL_BLANSH;
		} else if(motionAvaible(MotionType.FALL)) {
			motionType=MotionType.FALL;
		} else {
			switch(controlType){
			case UP:
				motionType = jump(0);
				break;
			case LEFT:
				motionType = moveLeft();
				break;
			case RIGHT:
				motionType = moveRight();
				break;
			case DOWN:
			case IDLE:
			default:
				motionType=MotionType.STAY;
				break;

			}
		}
		return motionType;
	}
	
	private MotionType jump(int stage) {
		MotionType motionType;
		if(motionAvaible(MotionType.JUMP, stage)) {
			motionType=MotionType.JUMP;
			motion = new Motion(motionType, stage);
		} else if(motionAvaible(MotionType.MAGNET)) {
			motionType = MotionType.MAGNET;
		} else {
			motionType=MotionType.BEAT_ROOF;	
		}
		return motionType;
	}
	
	private MotionType moveLeft() {
		MotionType motionType;
		if(motionAvaible(MotionType.JUMP_LEFT) ) {
			motionType=MotionType.JUMP_LEFT;
		} else if(getHeroCell().getLeft().getType() == PlatformType.SPRING) {
			motionType=MotionType.FLY_RIGHT;
		} else if(getHeroCell().getLeft().getType() == PlatformType.GLUE_V) {
			motionType=MotionType.STICK_LEFT;
		} else {
			motionType=MotionType.JUMP_LEFT_WALL;
		}
		return motionType;
	}
	
	private MotionType moveRight() {
		MotionType motionType;
		if(motionAvaible(MotionType.JUMP_RIGHT) ) {
			motionType=MotionType.JUMP_RIGHT;
		} else if(getHeroCell().getRight().getType() == PlatformType.SPRING) {
			motionType=MotionType.FLY_LEFT;
		} else if(getHeroCell().getRight().getType() == PlatformType.GLUE_V) {
			motionType=MotionType.STICK_RIGHT;
		} else {
			motionType=MotionType.JUMP_RIGHT_WALL;
		}
		return motionType;
	}
	
	private MotionType platformsCheck() {
		MotionType motionType;
		switch (getHeroCell().getFloor().getType()){

		case ANGLE_RIGHT:
			motionType = moveRight();			
			break;	
		case  ANGLE_LEFT:
			motionType = moveLeft();
			break;
		case  TRAMPOLINE:
			motionType = jump(1);
			break;
		case  THROW_OUT_LEFT:
			if(motionAvaible(MotionType.JUMP_LEFT)){
				motionType=MotionType.THROW_LEFT;
			} else {
				motionType = moveLeft();
			}	
			break;
		case  THROW_OUT_RIGHT:
			if(motionAvaible(MotionType.JUMP_RIGHT)){
				motionType=MotionType.THROW_RIGHT;
			} else {
				motionType = moveRight();
			}	
			break;
		case SLICK:
			if(controlType == UserControlType.IDLE &&
				(motion.getType() == MotionType.JUMP_LEFT || motion.getType() == MotionType.JUMP_RIGHT_WALL)) {
				motionType = moveLeft();
			} else if (controlType == UserControlType.IDLE &&
					(motion.getType() == MotionType.JUMP_RIGHT || motion.getType() == MotionType.JUMP_LEFT_WALL)) {
				motionType = moveRight();
			} else {
				motionType = stayCheck();
			}
			break;
		case SLOPE:
			if(getHeroCell().getFloor().getStatus() == 1) {
				motionType = moveLeft();
			} else if(getHeroCell().getFloor().getStatus() == 2) {
				motionType = moveRight();
			} else {
				motionType = stayCheck();
			}
			break;
		case ONE_WAY_DOWN:
			if(controlType == UserControlType.DOWN) {
				if(heroY + 1 > row - 1) lose = true;
				motionType = MotionType.FALL;
			} else {
				motionType = stayCheck();
			}
			break;
		case WAY_UP_DOWN:
			if(controlType == UserControlType.DOWN && motion.getType() != MotionType.JUMP) {
				if(heroY + 1 > row - 1) lose = true;
				motionType = MotionType.FALL;
			} else {
				motionType = stayCheck();
			}
			break;
		case BRICK:
			if(controlType == UserControlType.UP) {
				controlType = UserControlType.IDLE;
			}
			motionType = stayCheck();
			break;
		case GLUE:
			if(prevMotion.getType() == MotionType.STAY && controlType == UserControlType.UP) {
				controlType = UserControlType.IDLE;
			}
			motionType = stayCheck();
			break;
		case TELEPORT:
			if(controlType != UserControlType.LEFT && controlType != UserControlType.RIGHT) {
				motionType = MotionType.TP;
			} else {
				motionType = stayCheck();
			}
			break;
		default:
			motionType = stayCheck();
			break;	
		}
		return motionType;
	}
	
	private void checkTeleport() {
		MotionType mt = motion.getType();
		MotionType prevMt = prevMotion.getType();
		switch(mt) {
		case JUMP_LEFT:
		case THROW_LEFT:
		case FLY_LEFT:
			if(!(mt == MotionType.FLY_LEFT && prevMt != MotionType.FLY_LEFT) && getHeroCell().getLeft().getType() == PlatformType.TELEPORT_L_V) {
				motion = new ContainerMotion(MotionType.TP_LEFT, motion);
			}
			break;
		case JUMP_RIGHT:
		case THROW_RIGHT:
		case FLY_RIGHT:
			if(!(mt == MotionType.FLY_RIGHT && prevMt != MotionType.FLY_RIGHT) && getHeroCell().getRight().getType() == PlatformType.TELEPORT_R_V) {
				motion = new ContainerMotion(MotionType.TP_RIGHT, motion);
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
		if(motion.isUncontrolable()) {
			controlType = UserControlType.IDLE;
		}
		if(motion.getType() == MotionType.TP_LEFT || motion.getType() == MotionType.TP_RIGHT) {
			motion = motion.getChildMotion();
		}
		prevMotion = motion;
		motion.continueMotion();
		MotionType motionType;
		switch(motion.getType()){
		case JUMP:
			switch(controlType) {
			case DOWN:
				motionType = platformsCheck();
				break;
			case LEFT:
				motionType = moveLeft();
				break;
			case RIGHT:
				motionType = moveRight();
				break;
			case IDLE:
			case UP:	
			default:
				motionType = jump(motion.getStage());
				break;
			}
			break;
		case  MAGNET:
			switch(controlType){
			case DOWN:
				motionType = platformsCheck();
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
				motionType = platformsCheck();
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
				motionType = platformsCheck();
				break;
			default:
				motionType = MotionType.STICK_RIGHT;
				break;
			}
			break;
		case THROW_LEFT:
			if(motion.getStage() == 1) {
				if(!motionAvaible(MotionType.JUMP_LEFT) ) {
					motionType = moveLeft();
				} else {
					motionType = MotionType.THROW_LEFT;
				}
			} else {				
				motionType = platformsCheck();
			}
			break;
		case THROW_RIGHT:
			if(motion.getStage() == 1) {
				if(!motionAvaible(MotionType.JUMP_RIGHT) ) {
					motionType = moveRight();
				} else {
					motionType = MotionType.THROW_RIGHT;
				}
			} else {
				motionType = platformsCheck();
			}
			break;
		case JUMP_LEFT_WALL:
			motionType = platformsCheck();
			break;
//		case TP_LEFT:
//			if(!MotionType.FLY_LEFT.isUncontrolable() && motionAvaible(MotionType.JUMP_LEFT)) {
//				motionType = MotionType.FLY_LEFT;
//			} else if(MotionType.THROW_LEFT.getStage() == 1 && motionAvaible(MotionType.JUMP_LEFT)) {
//				motionType = MotionType.THROW_LEFT;
//			} else {
//			motionType = platformsCheck();
//			}
//			break;
		case FLY_LEFT:
			switch(controlType) {
			case UP:
			case DOWN:
			case RIGHT:
//				motion.finishMotion();
				motionType = platformsCheck();
				break;
			default:
				if(motionAvaible(MotionType.JUMP_LEFT)) {
					motionType = MotionType.FLY_LEFT;
				} else {
					motionType = moveLeft();
				}
				break;
			}
			break;
		case JUMP_RIGHT_WALL:
			motionType = platformsCheck();
			break;
//		case TP_RIGHT:
//			if(!MotionType.FLY_RIGHT.isUncontrolable() && motionAvaible(MotionType.JUMP_RIGHT)) {
//				motionType = MotionType.FLY_RIGHT;
//			} else {
//			motionType = platformsCheck();
//			}
//			break;
		case FLY_RIGHT:
			switch(controlType) {
			case UP:
			case DOWN:
			case LEFT:
//				motion.finishMotion();
				motionType = platformsCheck();
				break;
			default:
				if(motionAvaible(MotionType.JUMP_RIGHT)) {
					motionType = MotionType.FLY_RIGHT;
				} else {
					motionType = moveRight();
				}
				break;
			}
			break;
		case TP:
			if(motion.getStage() == 0) {
				if(controlType == UserControlType.UP || controlType == UserControlType.IDLE) {
					// to start without pre jump
					motionType = jump(1);
				} else {
					motionType = platformsCheck();
				}
			} else {
				motionType = MotionType.TP;
			}
			break;
		case FALL_BLANSH:
			if(motion.getStage() == 1) {
				motionType = MotionType.FALL_BLANSH;
			} else {
				motionType = platformsCheck();
			}
			break;
		default:
			motionType = platformsCheck();
			break;
		}
		if(motionType != motion.getType()) {
			motion = new Motion(motionType);
		}
		checkTeleport();
		startFinishMotions();
		updatePosition();
	}
	
	private void updatePosition() {
		if(motion.getType() == MotionType.TP_LEFT) {
			TpPeer toCoords = tpGroupMap.get(new CellCoords(heroY, heroX));
			heroX = toCoords.endCol;
			heroY = toCoords.endRow;
			return;
		}
		if(motion.getType() == MotionType.TP_RIGHT) {
			TpPeer toCoords = tpGroupMap.get(new CellCoords(heroY, heroX));
			heroX = toCoords.startCol;
			heroY = toCoords.startRow;
			return;
		}
		if(motion.getType() == MotionType.TP && motion.getStage() == 1) {
			CellCoords nextCell = tpSequence.get(new CellCoords(heroY, heroX)); 
			heroX = nextCell.j;
			heroY = nextCell.i;
		}
		heroX += motion.getXSpeed();
		heroY += motion.getYSpeed();
	}
	
	private boolean motionAvaible(MotionType mt) {
		if(mt == motion.getType()) {
			return motionAvaible(mt, motion.getStage());
		} else {
			return motionAvaible(mt, 0);
		}
	}
	
	private boolean motionAvaible(MotionType mt, int stage) {
		int xSpeed = Motion.getXSpeedAtStage(mt, stage);
		int ySpeed = Motion.getYSpeedAtStage(mt, stage);
		switch (mt) {
		case JUMP:
			if(ySpeed < 0 && getHeroCell().getRoof().getType() == PlatformType.SPIKE) lose = true;
			if(heroY + ySpeed < 0) return false;
			if(getHeroCell().getRoof().getType() == PlatformType.ONE_WAY_UP) return true;
			if(getHeroCell().getRoof().getType() == PlatformType.WAY_UP_DOWN) return true;
			if(getHeroCell().getRoof().getType() == PlatformType.TRANSPARENT) return true;
			if(ySpeed < 0 && getHeroCell().getRoof().getType() != PlatformType.NONE) return false;
			return true;
		case FLY_LEFT:
		case JUMP_LEFT:
		case THROW_LEFT:
			if(getHeroCell().getLeft().getType() == PlatformType.SPIKE_V) lose = true;
			if(getHeroCell().getLeft().getType() == PlatformType.TELEPORT_L_V) return true;
			if(heroX + xSpeed < 0) return false;
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
			if(heroX + xSpeed > col - 1) return false;
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
	
	public Motion getMotion() {
		return motion;
	}
	
	public Motion getPrevMotion() {
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
		MotionType motionType = motion.getChildMotion().getType();
		MotionType prevMotionType = prevMotion.getType();
		if(prevMotionType != motionType) {
			prevMotion.finishMotion();
			// case to start jump without pre-jump after TP (via horizontal platform)
			if(!(prevMotionType == MotionType.TP && motionType == MotionType.JUMP) &&
					motionType != MotionType.JUMP) {
				motion.startMotion();
			}
		}
	}
	
}
