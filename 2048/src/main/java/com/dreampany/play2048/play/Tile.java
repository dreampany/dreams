package com.dreampany.play2048.play;

/**
 * Created by Hawladar Roman on 12/26/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class Tile extends Cell {

    private int value;
    private Tile[] mergedFrom = null;

    public Tile(int x, int y, int value) {
        super(x, y);
        this.value = value;
    }

    public Tile(Cell cell, int value) {
        super(cell.getX(), cell.getY());
        this.value = value;
    }

    public void updatePosition(Cell cell) {
        this.setX(cell.getX());
        this.setY(cell.getY());
    }

    public int getValue() {
        return this.value;
    }

    public Tile[] getMergedFrom() {
        return mergedFrom;
    }

    public void setMergedFrom(Tile[] tile) {
        mergedFrom = tile;
    }
}
