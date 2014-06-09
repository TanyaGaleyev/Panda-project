package org.ivan.simple.game.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ivan.simple.UserControlType;
import org.ivan.simple.game.motion.ContainerMotion;
import org.ivan.simple.game.motion.Motion;
import org.ivan.simple.game.motion.MotionType;
import org.ivan.simple.game.hero.HeroModel;
import org.ivan.simple.game.monster.MonsterDirection;
import org.ivan.simple.game.monster.MonsterModel;
import org.ivan.simple.game.monster.strategies.MonsterStrategy;
import org.ivan.simple.game.monster.strategies.RouteDirectionStrategyFactory;
import org.ivan.simple.game.scores.ScoreProvider;
import org.ivan.simple.utils.Multimap;

public class LevelModel {
	private LevelGrid levelGrid;
	private final int rows;
	private final int cols;
	public final MonsterModel monster;
	public final HeroModel hero;
	private int totalPrizes;
    private int prizesCollected = 0;
    private ScoreProvider scoreProvider;
	private boolean lose = false;
	private boolean complete = false;
	private LevelCell winCell;
	private Map<Platform, PlatformCellPair> leftRightTpDestMap;
	private Map<Platform, PlatformCellPair> floorTpDestMap;
	private List<Platform> switchList = new ArrayList<Platform>();
	private List<Platform> unlockList = new ArrayList<Platform>();
	private int steps = 0;

    private class PlatformCellPair {
        Platform p;
        CellCoords c;

        private PlatformCellPair(Platform p, CellCoords c) {
            this.p = p;
            this.c = c;
        }
    }

	public LevelModel(int lev, LevelParser parser) {
        // MonsterStrategy dangerousKillerMonsterStrategy = new RandomContiniousDirection(MonsterDirection.getAllDirections());
        LevelInfo levelInfo;
        try {
            levelInfo = parser.readLevelInfo(lev);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int[][] routeArray = levelInfo.monsterRoute;

        if(routeArray != null && routeArray.length != 0)  {
            MonsterStrategy route = RouteDirectionStrategyFactory.createRouteDirectionStrategy(routeArray);
            monster = new MonsterModel(routeArray[0][1], routeArray[0][0], route, levelInfo.monsterType);
        } else {
            monster = null;
        }
        int[][][][] mylevel = levelInfo.levelGrid;
        rows=mylevel.length;
        cols=mylevel[0].length;
        int[][] prizes = levelInfo.prizesMap;
        totalPrizes = 0;
        scoreProvider = new ScoreProvider(levelInfo.scoreStruct);
        CellCoords startCellCoord = levelInfo.startCell;
        CellCoords winCellCoord = levelInfo.winCell;
        hero = new HeroModel(startCellCoord.i, startCellCoord.j);
        levelGrid = new LevelGrid(rows, cols);
        Map<Platform, int[]> platformMetaMap = new HashMap<Platform, int[]>();
        for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
                LevelCell newCell = new LevelCell();
                levelGrid.put(i, j, newCell);
				newCell.setPrize(prizes[i][j]);
                if(prizes[i][j] != 0) totalPrizes++;
                if(j == 0) {
                    // create left wall only for first column's cells
                    int[] leftWallDescr = mylevel[i][j][0];
                    newCell.createLeft(type(leftWallDescr));
                    platformMetaMap.put(newCell.getLeft(), leftWallDescr);
				} else {
                    // else set left wall as right wall of nearest left cell
                    newCell.left_wall = levelGrid.get(i, j - 1).getRight();
                }

                if(i == 0) {
                    // create roof only for first row's cells
                    int[] roofDescr = mylevel[i][j][1];
                    newCell.createRoof(type(roofDescr));
                    platformMetaMap.put(newCell.getRoof(), roofDescr);
				} else {
                    // else set roof as floor of nearest upper cell
                    newCell.roof = levelGrid.get(i - 1, j).getFloor();
				}

				int[] rightWallDescr = mylevel[i][j][2];
                newCell.createRight(type(rightWallDescr));
                platformMetaMap.put(newCell.getRight(), rightWallDescr);

				int[] floorDescr = mylevel[i][j][3];
                newCell.createFloor(type(floorDescr));
                platformMetaMap.put(newCell.getFloor(), floorDescr);

				if(i == winCellCoord.i && j == winCellCoord.j) {
					winCell = newCell;
				}
			}
		}
        initFloorTps(platformMetaMap);
        initLeftRightTps(platformMetaMap);
        initStatePlatforms(platformMetaMap);
        if(winCell == null) winCell = levelGrid.get(0, 0);
    }

    private void initStatePlatforms(Map<Platform, int[]> platformMetaMap) {
        for (LevelGrid.VerticalPlatformCoords vp : levelGrid.verticalPlatforms()) {
            initStatus(vp.getPlatform(), platformMetaMap);
        }
        for (LevelGrid.HorizontalPlatformCoords hp : levelGrid.horizontalPlatforms()) {
            initStatus(hp.getPlatform(), platformMetaMap);
        }
    }

    private void initStatus(Platform wall, Map<Platform, int[]> platformMetaMap) {
        int[] meta;
        if((meta = platformMetaMap.get(wall)).length > 1 &&
                (wall.getType() == PlatformType.LIMIT || wall.getType() == PlatformType.REDUCE ||
                wall.getType() == PlatformType.BRICK || wall.getType() == PlatformType.BRICK_V)) {
            wall.setStatus(meta[1]);
        }
    }

    private void initLeftRightTps(Map<Platform, int[]> platformMetaMap) {
        Multimap<Integer, PlatformCellPair> inGroupsLR = new Multimap<Integer, PlatformCellPair>();
        HashMap<Integer, PlatformCellPair> outGroupsLR = new HashMap<Integer, PlatformCellPair>();
        for (LevelGrid.VerticalPlatformCoords vp : levelGrid.verticalPlatforms()) {
            Platform rightWall = vp.getPlatform();
            int[] meta = platformMetaMap.get(rightWall);
            if (rightWall.getType() == PlatformType.TELEPORT_L_V) {
                CellCoords coords = new CellCoords(vp.getRow(), vp.getColAtRight());
                inGroupsLR.put(inKey(meta), new PlatformCellPair(rightWall, coords));
                outGroupsLR.put(outKey(meta), new PlatformCellPair(rightWall, coords));
            }
            if (rightWall.getType() == PlatformType.TELEPORT_R_V) {
                CellCoords coords = new CellCoords(vp.getRow(), vp.getColAtLeft());
                inGroupsLR.put(inKey(meta), new PlatformCellPair(rightWall, coords));
                outGroupsLR.put(outKey(meta), new PlatformCellPair(rightWall, coords));
            }
            if (rightWall.getType() == PlatformType.SWITCH) {
                switchList.add(rightWall);
            }
            if (rightWall.getType() == PlatformType.UNLOCK) {
                unlockList.add(rightWall);
            }
        }
        leftRightTpDestMap = tpDestMap(inGroupsLR, outGroupsLR);
    }

    private void initFloorTps(Map<Platform, int[]> platformMetaMap) {
        Multimap<Integer, PlatformCellPair> inGroupsFloor = new Multimap<Integer, PlatformCellPair>();
        HashMap<Integer, PlatformCellPair> outGroupsFloor = new HashMap<Integer, PlatformCellPair>();
        for(LevelGrid.HorizontalPlatformCoords hp : levelGrid.horizontalPlatforms()) {
            Platform floor = hp.getPlatform();
            int[] meta = platformMetaMap.get(floor);
            if(floor.getType() == PlatformType.TELEPORT) {
                CellCoords coords = new CellCoords(hp.getRowAbove(), hp.getCol());
                inGroupsFloor.put(inKey(meta), new PlatformCellPair(floor, coords));
                outGroupsFloor.put(outKey(meta), new PlatformCellPair(floor, coords));
            }
        }
        floorTpDestMap = tpDestMap(inGroupsFloor, outGroupsFloor);
    }

    private Map<Platform, PlatformCellPair> tpDestMap(
            Multimap<Integer, PlatformCellPair> inGroups,
            HashMap<Integer, PlatformCellPair> outGroups) {
        Map<Platform, PlatformCellPair> retMap = new HashMap<Platform, PlatformCellPair>();
        for (int key : inGroups.keySet()) {
            for (PlatformCellPair platformCellPair : inGroups.get(key)) {
                retMap.put(platformCellPair.p, outGroups.get(key));
            }
        }
        return retMap;
    }

    private int type(int[] platformDescr) {
        return platformDescr[0];
    }

    private int inKey(int[] platformDescr) {
        return platformDescr[1];
    }

	private int outKey(int[] platformDescr) {
        return platformDescr[2];
    }

	public LevelCell getCell(int i, int j) {
		return levelGrid.get(i, j);
	}

	public LevelCell getHeroCell() {
        LevelCell ret;
        try {
            ret = levelGrid.get(hero.getY(), hero.getX());
        } catch (ArrayIndexOutOfBoundsException e) {
            ret = new LevelCell();
        }
        return ret;
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
	
	private MotionType platformsCheck(UserControlType controlType, MotionType prevMt) {
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
				(prevMt == MotionType.JUMP_LEFT || prevMt == MotionType.JUMP_RIGHT_WALL ||
                prevMt == MotionType.THROW_LEFT)) {
				motionType = moveLeft();
			} else if (controlType == UserControlType.IDLE &&
					(prevMt == MotionType.JUMP_RIGHT || prevMt == MotionType.JUMP_LEFT_WALL ||
                    prevMt == MotionType.THROW_RIGHT)) {
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
			if(controlType == UserControlType.DOWN && prevMt != MotionType.JUMP) {
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
			if(prevMt == MotionType.STAY && controlType == UserControlType.UP) {
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
		case CLOUD:
			motionType = MotionType.CLOUD_IDLE;
			break;
        case SPIKE_UP:
            lose = true;
            motionType = stayCheck(controlType);
            break;
		default:
			motionType = stayCheck(controlType);
			break;	
		}
		return motionType;
	}

    private void checkTeleport(MotionType mt, MotionType prevMt) {
        switch(mt) {
            case JUMP_LEFT:
            case THROW_LEFT:
            case FLY_LEFT:
                if(!(mt == MotionType.FLY_LEFT && prevMt != MotionType.FLY_LEFT) && getHeroCell().getLeft().getType() == PlatformType.TELEPORT_L_V) {
                    Platform peer = leftRightTpDestMap.get(getHeroCell().getLeft()).p;
                    if(peer.getType() == PlatformType.TELEPORT_R_V) {
                        hero.currentMotion = new ContainerMotion(MotionType.TP_LEFT, hero.currentMotion);
                    } else {
                        hero.currentMotion = new ContainerMotion(MotionType.TP_LR, hero.currentMotion);
                    }
                }
                break;
            case JUMP_RIGHT:
            case THROW_RIGHT:
            case FLY_RIGHT:
                if(!(mt == MotionType.FLY_RIGHT && prevMt != MotionType.FLY_RIGHT) && getHeroCell().getRight().getType() == PlatformType.TELEPORT_R_V) {
                    Platform peer = leftRightTpDestMap.get(getHeroCell().getRight()).p;
                    if(peer.getType() == PlatformType.TELEPORT_L_V) {
                        hero.currentMotion = new ContainerMotion(MotionType.TP_RIGHT, hero.currentMotion);
                    } else {
                        hero.currentMotion = new ContainerMotion(MotionType.TP_RL, hero.currentMotion);
                    }
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
        MotionType tpAwareFinishingMt = hero.currentMotion.getTpAwareChildMotionType();
		hero.currentMotion = hero.currentMotion.getChildMotion();
		hero.finishingMotion = hero.currentMotion;
		hero.finishingMotion.continueMotion();
        MotionType motionType = obtainMotionType(controlType, tpAwareFinishingMt);
		if(motionType != tpAwareFinishingMt) {
            // finish prev motion if motion of another type obtained
            hero.finishingMotion.finishMotion();
			/*
			 * If new Motion has not created yet (e.g. stage 1 jump on trampoline)
			 * we create it
			 */
			if(hero.currentMotion == hero.finishingMotion) {
				hero.currentMotion = new Motion(motionType);
			}
		} else if(hero.currentMotion.getType() != tpAwareFinishingMt) {
            hero.finishingMotion = hero.currentMotion = new Motion(tpAwareFinishingMt, hero.currentMotion.getStage());
        }
		// check if we gonna teleport
		checkTeleport(hero.currentMotion.getType(), tpAwareFinishingMt);
		// collect prize 
		prizesCollected += getHeroCell().removePrize();
        checkWin();
        updatePosition(hero.currentMotion);
	}

    private MotionType obtainMotionType(UserControlType controlType, MotionType tpAwareFinishingMt) {
        MotionType motionType;
        switch(tpAwareFinishingMt){
        case JUMP:
            switch(controlType) {
            case DOWN:
                motionType = platformsCheck(controlType, tpAwareFinishingMt);
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
                motionType = platformsCheck(controlType, tpAwareFinishingMt);
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
                motionType = platformsCheck(controlType, tpAwareFinishingMt);
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
                motionType = platformsCheck(controlType, tpAwareFinishingMt);
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
                motionType = platformsCheck(controlType, tpAwareFinishingMt);
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
                motionType = platformsCheck(controlType, tpAwareFinishingMt);
            }
            break;
        case JUMP_LEFT_WALL:
            motionType = platformsCheck(controlType, tpAwareFinishingMt);
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
                motionType = platformsCheck(controlType, tpAwareFinishingMt);
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
            motionType = platformsCheck(controlType, tpAwareFinishingMt);
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
                motionType = platformsCheck(controlType, tpAwareFinishingMt);
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
                    motionType = platformsCheck(controlType, tpAwareFinishingMt);
                }
            } else {
                motionType = MotionType.TP;
            }
            break;
        case FALL_BLANSH:
            if(hero.finishingMotion.getStage() == 1) {
                motionType = MotionType.FALL_BLANSH;
            } else {
                motionType = platformsCheck(controlType, tpAwareFinishingMt);
            }
            break;
        case CLOUD_IDLE:
        case CLOUD_LEFT:
        case CLOUD_RIGHT:
        case CLOUD_UP:
        case CLOUD_DOWN:
            switch(controlType) {
            case LEFT:
                if(motionAvaible(MotionType.CLOUD_LEFT)) {
                    motionType = MotionType.CLOUD_LEFT;
                } else {
                    motionType = moveLeft();
                }
                break;
            case RIGHT:
                if(motionAvaible(MotionType.CLOUD_RIGHT)) {
                    motionType = MotionType.CLOUD_RIGHT;
                } else {
                    motionType = moveRight();
                }
                break;
            case UP:
                if(motionAvaible(MotionType.CLOUD_UP)) {
                    motionType = MotionType.CLOUD_UP;
                } else {
                    motionType = MotionType.CLOUD_IDLE;
                }
                break;
            case DOWN:
                if(motionAvaible(MotionType.CLOUD_DOWN)) {
                    motionType = MotionType.CLOUD_DOWN;
                } else {
                    motionType = MotionType.CLOUD_IDLE;
                }
                break;
            default:
                motionType = MotionType.CLOUD_IDLE;
                break;
            }
            break;
        default:
            motionType = platformsCheck(controlType, tpAwareFinishingMt);
            break;
        }
        return motionType;
    }

    private void checkWin() {
        /*
		 * if all prizes are collected show win platform
		 * level will be complete after hero reaches win cell (with win floor platform now)
		 */
        if(scoreProvider.hasScore(prizesCollected)/* || prizesCollected >= totalPrizes*/) {
            if(winCell.getFloor().getType() != PlatformType.WIN) {
                // TODO careful with roof (of underlying cell)
                winCell.createFloor(PlatformType.WIN);
            }
            if(getHeroCell() == winCell && !skipWinPlatform(hero.finishingMotion, hero.currentMotion)) {
                complete = true;
            }
        }
    }

    private void updatePosition(Motion motion) {
        MotionType mt = motion.getType();
        if(mt == MotionType.TP_LEFT || mt == MotionType.TP_LR) {
			CellCoords nextCell = leftRightTpDestMap.get(getHeroCell().getLeft()).c;
			hero.setX(nextCell.j);
			hero.setY(nextCell.i);
		} else if(mt == MotionType.TP_RIGHT || mt == MotionType.TP_RL) {
			CellCoords nextCell = leftRightTpDestMap.get(getHeroCell().getRight()).c;
			hero.setX(nextCell.j);
			hero.setY(nextCell.i);
		} else if(mt == MotionType.TP && motion.getStage() == 1) {
			CellCoords nextCell = floorTpDestMap.get(getHeroCell().getFloor()).c;
			hero.setX(nextCell.j);
			hero.setY(nextCell.i);
		} else {
			hero.setX(hero.getX() + motion.getXSpeed());
			hero.setY(hero.getY() + motion.getYSpeed());
		}
		if(hero.hasMoved()) steps++;
	}

    public boolean outOfBounds() {
        return hero.getX() < 0 || hero.getX() >= cols || hero.getY() < 0 || hero.getY() >= rows;
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
		case CLOUD_LEFT:
			if(hero.getX() - 1 < 0) return false;
			if(getHeroCell().left_wall.getType() != PlatformType.NONE) {
				return false;
			}
			if(getCell(hero.getY(), hero.getX() - 1).floor.getType() != PlatformType.NONE) {
				return false;
			}
			return true;
		case CLOUD_RIGHT:
			if(hero.getX() + 1 > cols - 1) return false;
			if(getHeroCell().right_wall.getType() != PlatformType.NONE) {
				return false;
			}
			if(getCell(hero.getY(), hero.getX() + 1).floor.getType() != PlatformType.NONE) {
				return false;
			}
			return true;
		case CLOUD_UP:
			if(hero.getY() - 1 < 0) return false;
			if(getHeroCell().roof.getType() != PlatformType.NONE) {
				return false;
			}
			return true;
		case CLOUD_DOWN:
			if(hero.getY() + 1 > rows - 1) return false;
			if(getCell(hero.getY() + 1, hero.getX()).floor.getType() != PlatformType.NONE) {
				return false;
			}
			return true;
		default:
			return false;
		}
	}

	public boolean isComplete() {
		return complete;
	}
	
	public int getScore() {
		return scoreProvider.getScoreConst(prizesCollected);
	}
	
	public boolean isLost() {
		return lose;
	}

    public void setLost(boolean lost) {
        this.lose = lost;
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
	
	private void checkMonsterColision() {
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
	
	public boolean skipWinPlatform(Motion finishingMotion,
                                   Motion currentMotion) {
		switch(finishingMotion.getType()) {
		case FLY_LEFT:
		case FLY_RIGHT:
			if(!finishingMotion.isFinishing()) return true;
            if(currentMotion.getType() == MotionType.STICK_LEFT) return true;
            if(currentMotion.getType() == MotionType.STICK_RIGHT) return true;
            return false;
		case THROW_LEFT:
		case THROW_RIGHT:
			return finishingMotion.getStage() == 1;
		case STICK_LEFT:
		case STICK_RIGHT:
            if(finishingMotion.getType() == currentMotion.getType()) return true;
			return false;
		default:
			return false;
		}
	}
	
	
	
}
