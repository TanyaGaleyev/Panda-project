package org.ivan.simple.game.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import org.ivan.simple.UserControlType;
import org.ivan.simple.game.ContainerMotion;
import org.ivan.simple.game.Motion;
import org.ivan.simple.game.MotionType;
import org.ivan.simple.game.hero.HeroModel;
import org.ivan.simple.game.monster.MonsterDirection;
import org.ivan.simple.game.monster.MonsterModel;
import org.ivan.simple.game.monster.strategies.MonsterStrategy;
import org.ivan.simple.game.monster.strategies.RandomDirection;
import org.ivan.simple.game.monster.strategies.RandomContiniousDirection;
import org.ivan.simple.game.monster.strategies.LinearRouteStrategy;

public class LevelModel {
	private LevelCell[][] levelGrid;
	private final int rows;
	private final int cols;
	public final MonsterModel monster;
	public final HeroModel hero;
	private int prizesLeft = 0;
	private boolean lose = false;
	private boolean complete = false;
	private LevelCell winCell;
	private HashMap<CellCoords, TpPeer> tpGroupMap = new HashMap<CellCoords, TpPeer>();
	private ArrayList<Platform> switchList = new ArrayList<Platform>();
	private ArrayList<Platform> unlockList = new ArrayList<Platform>();
	private HashMap<CellCoords, CellCoords> floorTPMap = new HashMap<CellCoords, CellCoords>();
	private int steps = 1;
	
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
		rows=5;
		cols=10;
		LevelStorage storage = new LevelStorage();
		// MonsterStrategy dangerousKillerMonsterStrategy = new RandomContiniousDirection(MonsterDirection.getAllDirections());
		int[][] routeArray = storage.getRouteArray(lev);
		MonsterStrategy route = new LinearRouteStrategy(routeArray);
				
		if(lev > 2) {
			monster = new MonsterModel(routeArray[0][1], routeArray[0][0], route);
		} else {
			monster = null;
		}
		hero = new HeroModel(rows - 1, 0);
		int[][][][] mylevel = storage.getLevel(lev);
		int[][] prizes = storage.getPrizesMap(lev);
		int[] winCellCoord = storage.getWinCell(lev);
		levelGrid = new LevelCell[rows][cols];
		HashMap<Integer, TpPeer> groups = new HashMap<Integer, TpPeer>();
		TreeMap<Integer, CellCoords> tpSequence = new TreeMap<Integer, CellCoords>();
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				levelGrid[i][j] = new LevelCell();
				levelGrid[i][j].setPrize(prizes[i][j]);
				prizesLeft += prizes[i][j];
				int leftWallType = mylevel[i][j][0][0];
				// create left wall only for first column's cells
				if(j == 0) {
					levelGrid[i][j].createLeft(leftWallType);
					if(leftWallType==11) {
						int key = mylevel[i][j][0][1];
						CellCoords coords = new CellCoords(i, j);
						if(!groups.containsKey(key)) {
							TpPeer newPeer = new TpPeer();
							newPeer.setStartCoords(coords);
							groups.put(key, newPeer);
							tpGroupMap.put(coords, newPeer);
						} else {
							TpPeer existedPeer = groups.get(key);
							existedPeer.setStartCoords(coords);
							tpGroupMap.put(coords, existedPeer);
						}
					}
					if(leftWallType==12 && j != 0) {
						int key = mylevel[i][j][0][1];
						CellCoords coords = new CellCoords(i, j - 1);
						if(!groups.containsKey(key)) {
							TpPeer newPeer = new TpPeer();
							newPeer.setEndCoords(coords);
							groups.put(key, newPeer);
							tpGroupMap.put(coords, newPeer);
						} else {
							TpPeer existedPeer = groups.get(key);
							existedPeer.setEndCoords(coords);
							tpGroupMap.put(coords, existedPeer);
						}
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
				
				int roofType = mylevel[i][j][1][0];
				// create roof only for first row's cells
				if(i == 0) {
					levelGrid[i][j].createRoof(roofType);
					// keep i!=0 do nothing check if level building model will change
					if(roofType == 23 && i != 0) {
						int key = mylevel[i][j][1][1];
						CellCoords cell = new CellCoords(i, j);
						tpSequence.put(key, cell);
					}
					// else set roof as floor of nearest upper cell
				} else {
					levelGrid[i][j].roof = levelGrid[i - 1][j].floor;
				}

				int rightWallType = mylevel[i][j][2][0];
				levelGrid[i][j] .createRight(rightWallType);
				if(rightWallType==11 && j != cols - 1) {
					int key = mylevel[i][j][2][1];
					CellCoords coords = new CellCoords(i, j + 1);
					if(!groups.containsKey(key)) {
						TpPeer newPeer = new TpPeer();
						newPeer.setStartCoords(coords);
						groups.put(key, newPeer);
						tpGroupMap.put(coords, newPeer);
					} else {
						TpPeer existedPeer = groups.get(key);
						existedPeer.setStartCoords(coords);
						tpGroupMap.put(coords, existedPeer);
					}
				}
				if(rightWallType==12) {
					int key = mylevel[i][j][2][1];
					CellCoords coords = new CellCoords(i, j);
					if(!groups.containsKey(key)) {
						TpPeer newPeer = new TpPeer();
						newPeer.setEndCoords(coords);
						groups.put(key, newPeer);
						tpGroupMap.put(coords, newPeer);
					} else {
						TpPeer existedPeer = groups.get(key);
						existedPeer.setEndCoords(coords);
						tpGroupMap.put(coords, existedPeer);
					}
				}
				if(rightWallType==17) {
					switchList.add(levelGrid[i][j].getRight());
				}
				if(rightWallType==18) {
					unlockList.add(levelGrid[i][j].getRight());
				}
				
				
				int floorType = mylevel[i][j][3][0];
				levelGrid[i][j].createFloor(floorType);
				if(floorType == 23) {
					int key = mylevel[i][j][3][1];
					CellCoords cell = new CellCoords(i, j);
					tpSequence.put(key, cell);
				}
				
				if(i == winCellCoord[0] && j == winCellCoord[1]) {
					winCell = levelGrid[i][j];
				}
				
				
				if(!tpSequence.isEmpty()) {
					Iterator<CellCoords> it = tpSequence.values().iterator();
					CellCoords firstTpCell = it.next();
					CellCoords prevTpCell = firstTpCell;
					while(it.hasNext()) {
						CellCoords cell = it.next();
						floorTPMap.put(prevTpCell, cell);
						prevTpCell = cell;
					}
					floorTPMap.put(prevTpCell, firstTpCell);
				}
			}
		}
	}

	public LevelCell getCell(int i, int j) {
		return levelGrid[i][j];
	}

	public LevelCell getHeroCell() {
		return levelGrid[hero.getY()][hero.getX()];
	}

	private MotionType stayCheck(UserControlType controlType) {
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
			hero.currentMotion = new Motion(motionType, stage);
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
	
	private MotionType platformsCheck(UserControlType controlType) {
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
				(hero.finishingMotion.getType() == MotionType.JUMP_LEFT || hero.finishingMotion.getType() == MotionType.JUMP_RIGHT_WALL)) {
				motionType = moveLeft();
			} else if (controlType == UserControlType.IDLE &&
					(hero.finishingMotion.getType() == MotionType.JUMP_RIGHT || hero.finishingMotion.getType() == MotionType.JUMP_LEFT_WALL)) {
				motionType = moveRight();
			} else {
				motionType = stayCheck(controlType);
			}
			break;
		case SLOPE:
			if(getHeroCell().getFloor().getStatus() == 1) {
				motionType = moveLeft();
			} else if(getHeroCell().getFloor().getStatus() == 2) {
				motionType = moveRight();
			} else {
				motionType = stayCheck(controlType);
			}
			break;
		case ONE_WAY_DOWN:
			if(controlType == UserControlType.DOWN) {
				if(hero.getY() + 1 > rows - 1) lose = true;
				motionType = MotionType.FALL;
			} else {
				motionType = stayCheck(controlType);
			}
			break;
		case WAY_UP_DOWN:
			if(controlType == UserControlType.DOWN && hero.finishingMotion.getType() != MotionType.JUMP) {
				if(hero.getY() + 1 > rows - 1) lose = true;
				motionType = MotionType.FALL;
			} else {
				motionType = stayCheck(controlType);
			}
			break;
		case BRICK:
			if(controlType == UserControlType.UP) {
				controlType = UserControlType.IDLE;
			}
			motionType = stayCheck(controlType);
			break;
		case GLUE:
			if(hero.finishingMotion.getType() == MotionType.STAY && controlType == UserControlType.UP) {
				controlType = UserControlType.IDLE;
			}
			motionType = stayCheck(controlType);
			break;
		case TELEPORT:
			if(controlType != UserControlType.LEFT && controlType != UserControlType.RIGHT) {
				motionType = MotionType.TP;
			} else {
				motionType = stayCheck(controlType);
			}
			break;
		default:
			motionType = stayCheck(controlType);
			break;	
		}
		return motionType;
	}
	
	private void checkTeleport() {
		MotionType mt = hero.currentMotion.getType();
		MotionType prevMt = hero.finishingMotion.getType();
		switch(mt) {
		case JUMP_LEFT:
		case THROW_LEFT:
		case FLY_LEFT:
			if(!(mt == MotionType.FLY_LEFT && prevMt != MotionType.FLY_LEFT) && getHeroCell().getLeft().getType() == PlatformType.TELEPORT_L_V) {
				hero.currentMotion = new ContainerMotion(MotionType.TP_LEFT, hero.currentMotion);
			}
			break;
		case JUMP_RIGHT:
		case THROW_RIGHT:
		case FLY_RIGHT:
			if(!(mt == MotionType.FLY_RIGHT && prevMt != MotionType.FLY_RIGHT) && getHeroCell().getRight().getType() == PlatformType.TELEPORT_R_V) {
				hero.currentMotion = new ContainerMotion(MotionType.TP_RIGHT, hero.currentMotion);
			}
			break;
		default:
			break;
		}
	}

	
	public void updateGame(UserControlType controlType) {
		
		tryToUnlock();
		if(hero.currentMotion.isUncontrolable()) {
			controlType = UserControlType.IDLE;
		}
		hero.currentMotion = hero.currentMotion.getChildMotion();
		hero.finishingMotion = hero.currentMotion;
		hero.finishingMotion.continueMotion();
		MotionType motionType;
		switch(hero.finishingMotion.getType()){
		case JUMP:
			switch(controlType) {
			case DOWN:
				motionType = platformsCheck(controlType);
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
				motionType = jump(hero.finishingMotion.getStage());
				break;
			}
			break;
		case  MAGNET:
			switch(controlType){
			case DOWN:
				motionType = platformsCheck(controlType);
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
				motionType = platformsCheck(controlType);
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
				motionType = platformsCheck(controlType);
				break;
			default:
				motionType = MotionType.STICK_RIGHT;
				break;
			}
			break;
		case THROW_LEFT:
			if(hero.finishingMotion.getStage() == 1) {
				if(!motionAvaible(MotionType.JUMP_LEFT) ) {
					motionType = moveLeft();
				} else {
					motionType = MotionType.THROW_LEFT;
				}
			} else {				
				motionType = platformsCheck(controlType);
			}
			break;
		case THROW_RIGHT:
			if(hero.finishingMotion.getStage() == 1) {
				if(!motionAvaible(MotionType.JUMP_RIGHT) ) {
					motionType = moveRight();
				} else {
					motionType = MotionType.THROW_RIGHT;
				}
			} else {
				motionType = platformsCheck(controlType);
			}
			break;
		case JUMP_LEFT_WALL:
			motionType = platformsCheck(controlType);
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
				motionType = platformsCheck(controlType);
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
			motionType = platformsCheck(controlType);
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
				motionType = platformsCheck(controlType);
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
			if(hero.finishingMotion.getStage() == 0) {
				if(controlType == UserControlType.UP || controlType == UserControlType.IDLE) {
					// to start without pre jump
					motionType = jump(1);
				} else {
					motionType = platformsCheck(controlType);
				}
			} else {
				motionType = MotionType.TP;
			}
			break;
		case FALL_BLANSH:
			if(hero.finishingMotion.getStage() == 1) {
				motionType = MotionType.FALL_BLANSH;
			} else {
				motionType = platformsCheck(controlType);
			}
			break;
		default:
			motionType = platformsCheck(controlType);
			break;
		}
		// finish prev motion if motion of another type obtained
		if(motionType != hero.finishingMotion.getType()) {
			hero.finishingMotion.finishMotion();
			/* 
			 * If new Motion has not created yet (e.g. stage 1 jump on trampoline)
			 * we create it
			 */
			if(hero.currentMotion == hero.finishingMotion) {
				hero.currentMotion = new Motion(motionType);
			}
		}
		// check if we gonna teleport
		checkTeleport();
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
			if(getHeroCell() == winCell && !skipWinPlatform(hero.finishingMotion)) {
				complete = true;
			}
		}
		updatePosition();
	}
	
	private void updatePosition() {
		if(hero.currentMotion.getType() == MotionType.TP_LEFT) {
			TpPeer toCoords = tpGroupMap.get(new CellCoords(hero.getY(), hero.getX()));
			hero.setX(toCoords.endCol);
			hero.setY(toCoords.endRow);
		} else if(hero.currentMotion.getType() == MotionType.TP_RIGHT) {
			TpPeer toCoords = tpGroupMap.get(new CellCoords(hero.getY(), hero.getX()));
			hero.setX(toCoords.startCol);
			hero.setY(toCoords.startRow);
		} else if(hero.currentMotion.getType() == MotionType.TP && hero.currentMotion.getStage() == 1) {
			CellCoords nextCell = floorTPMap.get(new CellCoords(hero.getY(), hero.getX())); 
			hero.setX(nextCell.j);
			hero.setY(nextCell.i);
		} else {
			hero.setX(hero.getX() + hero.currentMotion.getXSpeed());
			hero.setY(hero.getY() + hero.currentMotion.getYSpeed());
		}
		if(hero.hasMoved()) steps++;
	}
	
	private boolean motionAvaible(MotionType mt) {
		if(mt == hero.finishingMotion.getType()) {
			return motionAvaible(mt, hero.finishingMotion.getStage());
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
			if(hero.getY() + ySpeed < 0) return false;
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
			if(hero.getX() + xSpeed < 0) return false;
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
			if(hero.getX() + xSpeed > cols - 1) return false;
			if(getHeroCell().getRight().getType() == PlatformType.ONE_WAY_RIGHT) return true;
			if(getHeroCell().getRight().getType() == PlatformType.LIMIT &&
					getHeroCell().getRight().getStatus() < 3) return true;
			if(getHeroCell().getRight().getType() == PlatformType.TRANSPARENT_V) return true;
			if(getHeroCell().getRight().getType() != PlatformType.NONE) return false;
			return true;
		case FALL:
			if(getHeroCell().getFloor().getType() != PlatformType.NONE &&
				getHeroCell().getFloor().getType() != PlatformType.TRANSPARENT) return false;
			if(hero.getY() + 1 > rows - 1) lose = true;
			return true;
		case FALL_BLANSH:
			if(getHeroCell().getFloor().getType() != PlatformType.NONE &&
					getHeroCell().getFloor().getType() != PlatformType.TRANSPARENT) return false;
			if(hero.getY() + 2 > rows - 1) return false;
			if(getCell(hero.getY() + 1, hero.getX()).getFloor().getType() != PlatformType.NONE &&
					getCell(hero.getY() + 1, hero.getX()).getFloor().getType() != PlatformType.TRANSPARENT) return false;
			return true;
		case BEAT_ROOF:
			if(getHeroCell().getRoof().getType() != PlatformType.NONE) return true;
			if(hero.getY() - 1 < 0) return true;
			return false;
		case MAGNET:
			if(getHeroCell().getRoof().getType() == PlatformType.ELECTRO) return true;
			return false;	
		default:
			return false;
		}
	}

	public boolean isComplete() {
		return complete;
	}
	
	public int getScore() {
		return 1000 / steps + 1;
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

	
	public void nextDirection() {
		if(monster == null) return;
		LevelCell prevCell = getCell(monster.getRow(), monster.getCol());
		boolean decline = false;
		for(int i = 0; i < 10; i++) {
			MonsterDirection direction = monster.getStrategy().nextDirection(decline);
			if(monsterDirectionAvaible(direction, prevCell)) {
				monster.setDirection(direction);
				monster.moveInDirection();
				return;
			}
			decline = true;
		}
		monster.setDirection(MonsterDirection.IDLE);
	}
	
	private boolean monsterDirectionAvaible(MonsterDirection direction, LevelCell prevCell) {
		switch(direction) {
		case UP:
			return monster.getRow() > 0 && prevCell.getRoof().getType() == PlatformType.NONE;
		case LEFT:
			return monster.getCol() > 0 && prevCell.getLeft().getType() == PlatformType.NONE;
		case DOWN:
			return monster.getRow() < rows - 1 && prevCell.getFloor().getType() == PlatformType.NONE;
		case RIGHT:
			return monster.getCol() < cols - 1 && prevCell.getRight().getType() == PlatformType.NONE;
		case IDLE:
			return true;
		default:
			return false;
		}
	}
	
	public void checkMonsterColision() {
		if(monster == null) return;
		if(monster.getRow() == hero.getY() && monster.getCol() == hero.getX()) {
			lose = true;
		}
		if(monster.getRow() == hero.getPrevY() && monster.getCol() == hero.getPrevX() &&
				monster.getPrevRow() == hero.getY() && monster.getPrevCol() == hero.getX()) {
			lose = true;
		}
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}
	
	public boolean skipWinPlatform(Motion motion) {
		switch(motion.getType()) {
		case FLY_LEFT:
		case FLY_RIGHT:
			return !motion.isFinishing();
		case THROW_LEFT:
		case THROW_RIGHT:
			return motion.getStage() == 1;
		case STICK_LEFT:
		case STICK_RIGHT:
			return true;
		default:
			return false;
		}
	}
	
	
	
}
