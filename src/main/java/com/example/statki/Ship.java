package com.example.statki;

import java.util.ArrayList;
import java.util.List;

public class Ship {

    public enum PlacementOfShips {VERTICAL, HORIZONTAL, NONE}

    protected Point startingPoint;
    protected PlacementOfShips placement;
    protected Integer length;
    protected Battlefield battlefield;

    public Ship(Point startingPoint, PlacementOfShips placement, Integer length, Battlefield battlefield) {
        this.startingPoint = startingPoint;
        this.placement = placement;
        this.length = length;
        this.battlefield = battlefield;
        setPointToShip();
    }

    public void setPointToShip() {
        if (placement == PlacementOfShips.NONE) {
            startingPoint.setShip(true);
        } else {
            List<Point> listOfPointsOfShip = searchForPointsOfShip();
            for (int i = 0; i < length; i++) {
                listOfPointsOfShip.get(i).setShip(true);
            }
        }
    }

    public List<Point> searchForPointsOfShip() {
        List<Point> listOfPointsOfShip = new ArrayList<>();
        if (placement == PlacementOfShips.VERTICAL) {
            for (int i = 0; i < length; i++) {
                listOfPointsOfShip.add(battlefield.getMap().get(startingPoint.getIndexOfRow()+i-1).get(startingPoint.getIndexOfColumn()-1));
            }
        } else if (placement == PlacementOfShips.HORIZONTAL) {
            for (int i = 0; i < length; i++) {
                listOfPointsOfShip.add(battlefield.getMap().get(startingPoint.getIndexOfRow()-1).get(startingPoint.getIndexOfColumn()+i-1));
            }
        } else {
            listOfPointsOfShip.add(battlefield.getMap().get(startingPoint.getIndexOfRow()-1).get(startingPoint.getIndexOfColumn()-1));
        }
        return listOfPointsOfShip;
    }

    public boolean checkIfAlive() {
        int numberOfSunkenPoints = 0;
        List<Point> listOfPointsOfShip = searchForPointsOfShip();
        for (int i = 0; i < length; i++) {
            if (listOfPointsOfShip.get(i).isWasHit()) {
                numberOfSunkenPoints++;
            }
        }
        if (numberOfSunkenPoints == length) {
            return true;
        } else {
            return false;
        }
    }

    public Point getStartingPoint() {
        return startingPoint;
    }

    public PlacementOfShips getPlacement() {
        return placement;
    }

    public Integer getLength() {
        return length;
    }
}
