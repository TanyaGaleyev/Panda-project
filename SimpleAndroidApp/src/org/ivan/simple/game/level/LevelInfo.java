package org.ivan.simple.game.level;

import org.ivan.simple.game.scores.ScoreStruct;

/**
 * Created by Ivan on 14.05.2014.
 */
public class LevelInfo {
    public final int[][][][] levelGrid;
    public final int[][] prizesMap;
    public final CellCoords startCell;
    public final CellCoords winCell;
    public final int[][] monsterRoute;
    public final ScoreStruct scoreStruct;
    public final int monsterType;

    public LevelInfo(
            int[][][][] levelGrid,
            int[][] prizesMap,
            CellCoords startCell,
            CellCoords winCell,
            int[][] monsterRoute,
            int monsterType, ScoreStruct scoreStruct) {
        this.levelGrid = levelGrid;
        this.prizesMap = prizesMap;
        this.startCell = startCell;
        this.winCell = winCell;
        this.monsterRoute = monsterRoute;
        this.monsterType = monsterType;
        this.scoreStruct = scoreStruct;
    }
}
