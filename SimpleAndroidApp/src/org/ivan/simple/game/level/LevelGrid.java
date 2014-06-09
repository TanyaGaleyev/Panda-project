package org.ivan.simple.game.level;

import java.util.Iterator;

/**
 * Created by ivan on 28.05.2014.
 */
public class LevelGrid {
    private final LevelCell[][] grid;
    private final int rows;
    private final int cols;

    public LevelGrid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new LevelCell[rows][cols];
    }

    public void put(int i, int j, LevelCell cell) {
        grid[i][j] = cell;
    }

    public LevelCell get(int i, int j) {
        return grid[i][j];
    }

    public LevelCell get(CellCoords coords) {
        return get(coords.i, coords.j);
    }

    public Platform getRight(int i, int j) {
        if(j == -1) return get(i, 0).getLeft();
        else        return get(i, j).getRight();
    }

    public Platform getRight(CellCoords coords) {
        return getRight(coords.i, coords.j);
    }

    public VerticalPlatformCoords getRightCoords(CellCoords coords) {
        return new VerticalPlatformCoords(getRight(coords.i, coords.j), coords.i, coords.j);
    }

    public Platform getFloor(int i, int j) {
        if(i == -1) return get(0, j).getRoof();
        else       return get(i, j).getFloor();
    }

    public Platform getFloor(CellCoords coords) {
        return getFloor(coords.i, coords.j);
    }

    public HorizontalPlatformCoords getFloorCoords(CellCoords coords) {
        return new HorizontalPlatformCoords(getFloor(coords.i, coords.j), coords.i, coords.j);
    }

    public static class HorizontalPlatformCoords {
        private Platform platform;
        private int rowAbove;
        private int col;

        public HorizontalPlatformCoords(Platform platform, int rowAbove, int col) {
            this.platform = platform;
            this.rowAbove = rowAbove;
            this.col = col;
        }

        public int getCol() {
            return col;
        }

        public int getRowAbove() {
            return rowAbove;
        }

        public int getRowBehind() {
            return rowAbove + 1;
        }

        public Platform getPlatform() {
            return platform;
        }
    }

    public static class VerticalPlatformCoords {
        private Platform platform;
        private int row;
        private int colAtLeft;

        public VerticalPlatformCoords(Platform platform, int row, int colAtLeft) {
            this.platform = platform;
            this.row = row;
            this.colAtLeft = colAtLeft;
        }

        public int getRow() {
            return row;
        }

        public int getColAtLeft() {
            return colAtLeft;
        }

        public int getColAtRight() {
            return colAtLeft + 1;
        }

        public Platform getPlatform() {
            return platform;
        }
    }

    public Iterator<HorizontalPlatformCoords> horizontalIt() {
        return new HorizontalIterator();
    }

    public Iterable<HorizontalPlatformCoords> horizontalPlatforms() {
        return new Iterable<HorizontalPlatformCoords>() {
            @Override
            public Iterator<HorizontalPlatformCoords> iterator() {
                return horizontalIt();
            }
        };
    }

    public Iterable<VerticalPlatformCoords> verticalPlatforms() {
        return new Iterable<VerticalPlatformCoords>() {
            @Override
            public Iterator<VerticalPlatformCoords> iterator() {
                return verticalIt();
            }
        };
    }

    public Iterator<VerticalPlatformCoords> verticalIt() {
        return new VerticalIterator();
    }

    private class HorizontalIterator implements Iterator<HorizontalPlatformCoords> {
        private CoordsIterator coordsIt = new CoordsIterator(-1, 0);

        @Override
        public boolean hasNext() {
            return coordsIt.hasNext();
        }

        @Override
        public HorizontalPlatformCoords next() {
            return getFloorCoords(coordsIt.next());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class VerticalIterator implements Iterator<VerticalPlatformCoords> {
        private CoordsIterator coordsIt = new CoordsIterator(0, -1);

        @Override
        public boolean hasNext() {
            return coordsIt.hasNext();
        }

        @Override
        public VerticalPlatformCoords next() {
            return getRightCoords(coordsIt.next());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class CoordsIterator implements Iterator<CellCoords> {
        private int i;
        private int j;
        private int startJ;

        private CoordsIterator(int startI, int startJ) {
            this.startJ = startJ;
            this.i = startI;
            this.j = startJ;
        }

        @Override
        public boolean hasNext() {
            return i < rows && j < cols;
        }

        @Override
        public CellCoords next() {
            CellCoords ret = new CellCoords(i, j);
            if(j + 1 < cols) {
                j++;
            } else {
                i++;
                j = startJ;
            }
            return ret;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
