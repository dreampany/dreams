package com.dreampany.play2048.play;

/**
 * Created by Hawladar Roman on 12/26/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class Cell {

    private int x;
    private int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
