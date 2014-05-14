package org.ivan.simple.game.level;

/**
 * Created by Ivan on 14.05.2014.
 */
public class LevelInfo {
    public final int[][][][] levelGrid;
    public final int[][] prizesMap;
    public final int[] winCell;
    public final int[][] monsterRoute;

    public LevelInfo(int[][][][] levelGrid, int[][] prizesMap, int[] winCell, int[][] monsterRoute) {
        this.levelGrid = levelGrid;
        this.prizesMap = prizesMap;
        this.winCell = winCell;
        this.monsterRoute = monsterRoute;
    }
}
