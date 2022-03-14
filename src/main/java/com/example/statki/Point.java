package com.example.statki;

public class Point {

    protected boolean isShip;
    protected boolean wasHit;
    protected int indexOfColumn; // 1 - 10
    protected int indexOfRow; // 1 - 10

    public Point(boolean isShip, int indexOfColumn, int indexOfRow) {
        this.isShip = isShip;
        this.wasHit = false;
        this.indexOfColumn = indexOfColumn;
        this.indexOfRow = indexOfRow;
    }

    public int getIndexOfColumn() {
        return indexOfColumn;
    }

    public int getIndexOfRow() {
        return indexOfRow;
    }

    public boolean isShip() {
        return isShip;
    }

    public boolean isWasHit() {
        return wasHit;
    }

    public void setShip(boolean ship) {
        isShip = ship;
    }

    public void setWasHit(boolean wasHit) {
        this.wasHit = wasHit;
    }
}
