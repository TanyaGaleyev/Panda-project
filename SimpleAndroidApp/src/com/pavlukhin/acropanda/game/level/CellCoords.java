package com.pavlukhin.acropanda.game.level;

/**
 * Created by ivan on 26.05.2014.
 */
public class CellCoords {
    public int i;
    public int j;

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

