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

    public Platform getRight(int i, int j) {
        if(j == -1) return get(i, 0).getLeft();
        else        return get(i, j).getRight();
    }

    public Platform getRight(CellCoords coords) {
        return getRight(coords.i, coords.j);
    }

    public Platform getFloor(int i, int j) {
        if(i == -1) return get(0, j).getRoof();
        else       return get(i, j).getFloor();
    }

    public Platform getFloor(CellCoords coords) {
        return getFloor(coords.i, coords.j);
    }

    public Iterator<Platform> horizontalIt() {
        return new HorizontalIterator();
    }

    public Iterable<Platform> horizontalPlatforms() {
        return new Iterable<Platform>() {
            @Override
            public Iterator<Platform> iterator() {
                return horizontalIt();
            }
        };
    }

    public Iterable<Platform> verticalPlatforms() {
        return new Iterable<Platform>() {
            @Override
            public Iterator<Platform> iterator() {
                return verticalIt();
            }
        };
    }

    public Iterator<Platform> verticalIt() {
        return new VerticalIterator();
    }

    private class HorizontalIterator implements Iterator<Platform> {
        private CoordsIterator coordsIt = new CoordsIterator(-1, 0);

        @Override
        public boolean hasNext() {
            return coordsIt.hasNext();
        }

        @Override
        public Platform next() {
            return getFloor(coordsIt.next());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class VerticalIterator implements Iterator<Platform> {
        private CoordsIterator coordsIt = new CoordsIterator(0, -1);

        @Override
        public boolean hasNext() {
            return coordsIt.hasNext();
        }

        @Override
        public Platform next() {
            return getRight(coordsIt.next());
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
            this.i = startI - 1;
            this.j = startJ - 1;
        }

        @Override
        public boolean hasNext() {
            return i < rows && j < cols;
        }

        @Override
        public CellCoords next() {
            if(j < cols) {
                j++;
            } else {
                i++;
                j = startJ;
            }
            return new CellCoords(i, j);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
