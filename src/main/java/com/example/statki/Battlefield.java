package com.example.statki;

import java.util.ArrayList;
import java.util.List;

public class Battlefield {

    protected List<List<Point>> map;
    protected List<Ship> ships;

    public Battlefield() {
        setMap();
    }

    public List<List<Point>> getMap() {
        return map;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setMap() {
        List<List<Point>> arrayOfPoints = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            List<Point> listOfPoints = new ArrayList<>();
            for (int j = 1; j < 11; j++) {
                Point point = new Point(false,j,i);
                listOfPoints.add(point);
            }
            arrayOfPoints.add(listOfPoints);
        }
        this.map = arrayOfPoints;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public boolean checkIfShipsAlive() {
        int numberOfSunkenShips = 0;
        for (int i = 0; i < ships.size(); i++) {
            if (ships.get(i).checkIfAlive()) {
                numberOfSunkenShips++;
            }
        }
        if (numberOfSunkenShips == ships.size()) {
            return true;
        } else {
            return false;
        }
    }
}
