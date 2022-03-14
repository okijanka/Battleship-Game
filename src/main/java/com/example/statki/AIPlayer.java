package com.example.statki;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer {

    public enum DifficultyLevel {EASY, MEDIUM, HARD}

    protected DifficultyLevel difficultyLevel;
    protected String nick;
    protected int idUser;
    protected boolean isMyTurn;
    protected Battlefield battlefield =new Battlefield();
    protected List<Ship> listOfShips = new ArrayList<>();
    public List<Pair<Integer, Integer>> occupiedCells = new ArrayList<>();

    public AIPlayer(String nick, int idUser) {
        this.nick = nick;
        this.idUser = idUser;
        this.isMyTurn = true;
        setShipsRandomly();
        battlefield.setShips(listOfShips);
    }

    public String getNick() {
        return nick;
    }

    public int getIdUser() {
        return idUser;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public Battlefield getBattlefield() {
        return battlefield;
    }

    public void setBattlefield(Battlefield battlefield) {
        this.battlefield = battlefield;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public void setShipsRandomly() {

        List<Ship.PlacementOfShips> placementCases = new ArrayList<>();
        placementCases.add(Ship.PlacementOfShips.VERTICAL);
        placementCases.add(Ship.PlacementOfShips.HORIZONTAL);

        // ship with 4
        Random rand = new Random();
        Ship.PlacementOfShips randPlacement = placementCases.get(rand.nextInt(placementCases.size()));

        int indexOfColumn;
        int indexOfRow;

        if (randPlacement == Ship.PlacementOfShips.HORIZONTAL) {
            indexOfColumn = rand.nextInt(7) + 1;
            indexOfRow = rand.nextInt(10) + 1;
        } else {
            indexOfColumn = rand.nextInt(10) + 1;
            indexOfRow = rand.nextInt(7) + 1;
        }

        Ship ship = new Ship(battlefield.getMap().get(indexOfRow - 1).get(indexOfColumn - 1), randPlacement, 4, battlefield);
        listOfShips.add(ship);
        takenPoints(ship);

        // ship with 3
        randPlacement = placementCases.get(rand.nextInt(placementCases.size()));
        setShipWithLength3(randPlacement, indexOfRow, indexOfColumn, rand);
        randPlacement = placementCases.get(rand.nextInt(placementCases.size()));
        setShipWithLength3(randPlacement, indexOfRow, indexOfColumn, rand);

        // ship with 2
        randPlacement = placementCases.get(rand.nextInt(placementCases.size()));
        setShipWithLength2(randPlacement, indexOfRow, indexOfColumn, rand);
        randPlacement = placementCases.get(rand.nextInt(placementCases.size()));
        setShipWithLength2(randPlacement, indexOfRow, indexOfColumn, rand);
        randPlacement = placementCases.get(rand.nextInt(placementCases.size()));
        setShipWithLength2(randPlacement, indexOfRow, indexOfColumn, rand);

        // ship with 1
        setShipWithLength1(indexOfRow, indexOfColumn, rand);
        setShipWithLength1(indexOfRow, indexOfColumn, rand);
        setShipWithLength1(indexOfRow, indexOfColumn, rand);
        setShipWithLength1(indexOfRow, indexOfColumn, rand);
    }

    public void setShipWithLength3(Ship.PlacementOfShips randPlacement, int indexOfRow, int indexOfColumn, Random rand) {
        if (randPlacement == Ship.PlacementOfShips.HORIZONTAL) {
            while (occupiedCells.contains(new Pair<>(indexOfRow, indexOfColumn)) || occupiedCells.contains(new Pair<>(indexOfRow, indexOfColumn+1)) || occupiedCells.contains(new Pair<>(indexOfRow, indexOfColumn+2))) {
                indexOfColumn = rand.nextInt(8)+1;
                indexOfRow = rand.nextInt(10)+1;
            }
        } else {
            while (occupiedCells.contains(new Pair<>(indexOfRow, indexOfColumn)) || occupiedCells.contains(new Pair<>(indexOfRow+1, indexOfColumn)) || occupiedCells.contains(new Pair<>(indexOfRow+2, indexOfColumn))) {
                indexOfColumn = rand.nextInt(10)+1;
                indexOfRow = rand.nextInt(8)+1;
            }
        }
        Ship ship = new Ship(battlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1), randPlacement, 3, battlefield);
        listOfShips.add(ship);
        takenPoints(ship);
    }

    public void setShipWithLength2(Ship.PlacementOfShips randPlacement, int indexOfRow, int indexOfColumn, Random rand) {
        if (randPlacement == Ship.PlacementOfShips.HORIZONTAL) {
            while (occupiedCells.contains(new Pair<>(indexOfRow, indexOfColumn)) || occupiedCells.contains(new Pair<>(indexOfRow, indexOfColumn+1))) {
                indexOfColumn = rand.nextInt(9)+1;
                indexOfRow = rand.nextInt(10)+1;
            }
        } else {
            while (occupiedCells.contains(new Pair<>(indexOfRow, indexOfColumn)) || occupiedCells.contains(new Pair<>(indexOfRow+1, indexOfColumn))) {
                indexOfColumn = rand.nextInt(10)+1;
                indexOfRow = rand.nextInt(9)+1;
            }
        }
        Ship ship = new Ship(battlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1), randPlacement, 2, battlefield);
        listOfShips.add(ship);
        takenPoints(ship);
    }

    public void setShipWithLength1(int indexOfRow, int indexOfColumn, Random rand) {
        while (occupiedCells.contains(new Pair<>(indexOfRow, indexOfColumn))) {
            indexOfColumn = rand.nextInt(10)+1;
            indexOfRow = rand.nextInt(10)+1;
        }
        Ship ship = new Ship(battlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1), Ship.PlacementOfShips.NONE, 1, battlefield);
        listOfShips.add(ship);
        takenPoints(ship);
    }

    public void takenPoints(Ship ship) {
        int iStartingPoint = ship.startingPoint.getIndexOfRow();
        int jStartingPoint = ship.startingPoint.getIndexOfColumn();

        if (ship.placement == Ship.PlacementOfShips.VERTICAL) {
            for (int i = (iStartingPoint-1); i < (iStartingPoint+ship.length+1); i++) {
                for (int j = (jStartingPoint-1); j < (jStartingPoint+2); j++) {
                    if(!occupiedCells.contains(new Pair<>(i,j))) {
                        occupiedCells.add(new Pair<>(i, j));
                    }
                }
            }
        } else if (ship.placement == Ship.PlacementOfShips.HORIZONTAL) {
            for (int i = (iStartingPoint-1); i < (iStartingPoint+2); i++) {
                for (int j = (jStartingPoint-1); j < (jStartingPoint+ship.length+1); j++) {
                    if(!occupiedCells.contains(new Pair<>(i,j))) {
                        occupiedCells.add(new Pair<>(i, j));
                    }
                }
            }
        } else {
            for (int i = (iStartingPoint-1); i < (iStartingPoint+2); i++) {
                for (int j = (jStartingPoint-1); j < (jStartingPoint+2); j++) {
                    if(!occupiedCells.contains(new Pair<>(i,j))) {
                        occupiedCells.add(new Pair<>(i, j));
                    }
                }
            }
        }
        System.out.println(occupiedCells);
    }

    public Pair<Integer,Integer> move(Battlefield enemyBattlefield, List<Pair<Integer, Integer>> listOfShip, int indexOfRow, int indexOfColumn, boolean ifCheck) {

        if (difficultyLevel == DifficultyLevel.EASY){

            Random rand = new Random();
            while (enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1).isWasHit()) {
                indexOfRow = rand.nextInt(10)+1;
                indexOfColumn = rand.nextInt(10)+1;
            }

        } else if (difficultyLevel == DifficultyLevel.MEDIUM) {

            if (enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1).isShip()) {
                if (indexOfRow == 1) {
                    if (indexOfColumn == 1) {
                        if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn+1-1).isWasHit()) {
                            return new Pair<>(indexOfRow, indexOfColumn+1);
                        } else if (!enemyBattlefield.getMap().get(indexOfRow+1-1).get(indexOfColumn-1).isWasHit()) {
                            return new Pair<>(indexOfRow+1, indexOfColumn);
                        }
                    } else if (indexOfColumn == 10) {
                        if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1-1).isWasHit()) {
                            return new Pair<>(indexOfRow, indexOfColumn-1);
                        } else if (!enemyBattlefield.getMap().get(indexOfRow+1-1).get(indexOfColumn-1).isWasHit()) {
                            return new Pair<>(indexOfRow+1, indexOfColumn);
                        }
                    } else {
                        if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1-1).isWasHit()) {
                            return new Pair<>(indexOfRow, indexOfColumn-1);
                        } else if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn+1-1).isWasHit()) {
                            return new Pair<>(indexOfRow, indexOfColumn+1);
                        } else if (!enemyBattlefield.getMap().get(indexOfRow+1-1).get(indexOfColumn-1).isWasHit()) {
                            return new Pair<>(indexOfRow+1, indexOfColumn);
                        }
                    }
                } else if (indexOfRow == 10) {
                    if (indexOfColumn == 1) {
                        if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn+1-1).isWasHit()) {
                            return new Pair<>(indexOfRow, indexOfColumn+1);
                        } else if (!enemyBattlefield.getMap().get(indexOfRow-1-1).get(indexOfColumn-1).isWasHit()) {
                            return new Pair<>(indexOfRow-1, indexOfColumn);
                        }
                    } else if (indexOfColumn == 10) {
                        if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1-1).isWasHit()) {
                            return new Pair<>(indexOfRow, indexOfColumn-1);
                        } else if (!enemyBattlefield.getMap().get(indexOfRow-1-1).get(indexOfColumn-1).isWasHit()) {
                            return new Pair<>(indexOfRow-1, indexOfColumn);
                        }
                    } else {
                        if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1-1).isWasHit()) {
                            return new Pair<>(indexOfRow, indexOfColumn-1);
                        } else if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn+1-1).isWasHit()) {
                            return new Pair<>(indexOfRow, indexOfColumn+1);
                        } else if (!enemyBattlefield.getMap().get(indexOfRow-1-1).get(indexOfColumn-1).isWasHit()) {
                            return new Pair<>(indexOfRow-1, indexOfColumn);
                        }
                    }
                } else {
                    if (indexOfColumn == 1) {
                        if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn+1-1).isWasHit()) {
                            return new Pair<>(indexOfRow, indexOfColumn+1);
                        } else if (!enemyBattlefield.getMap().get(indexOfRow+1-1).get(indexOfColumn-1).isWasHit()) {
                            return new Pair<>(indexOfRow+1, indexOfColumn);
                        } else if (!enemyBattlefield.getMap().get(indexOfRow-1-1).get(indexOfColumn-1).isWasHit()) {
                            return new Pair<>(indexOfRow-1, indexOfColumn);
                        }
                    } else if (indexOfColumn == 10) {
                        if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1-1).isWasHit()) {
                            return new Pair<>(indexOfRow, indexOfColumn-1);
                        } else if (!enemyBattlefield.getMap().get(indexOfRow+1-1).get(indexOfColumn-1).isWasHit()) {
                            return new Pair<>(indexOfRow+1, indexOfColumn);
                        } else if (!enemyBattlefield.getMap().get(indexOfRow-1-1).get(indexOfColumn-1).isWasHit()) {
                            return new Pair<>(indexOfRow-1, indexOfColumn);
                        }
                    } else {
                        if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1-1).isWasHit()) {
                            return new Pair<>(indexOfRow, indexOfColumn-1);
                        } else if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn+1-1).isWasHit()) {
                            return new Pair<>(indexOfRow, indexOfColumn+1);
                        } else if (!enemyBattlefield.getMap().get(indexOfRow+1-1).get(indexOfColumn-1).isWasHit()) {
                            return new Pair<>(indexOfRow+1, indexOfColumn);
                        } else if (!enemyBattlefield.getMap().get(indexOfRow-1-1).get(indexOfColumn-1).isWasHit()) {
                            return new Pair<>(indexOfRow-1, indexOfColumn);
                        }
                    }
                }
                Random rand = new Random();
                while (enemyBattlefield.getMap().get(indexOfRow - 1).get(indexOfColumn - 1).isWasHit()) {
                    indexOfRow = rand.nextInt(10) + 1;
                    indexOfColumn = rand.nextInt(10) + 1;
                }
            } else {
                Random rand = new Random();
                while (enemyBattlefield.getMap().get(indexOfRow - 1).get(indexOfColumn - 1).isWasHit()) {
                    indexOfRow = rand.nextInt(10) + 1;
                    indexOfColumn = rand.nextInt(10) + 1;
                }
            }

        } else if (difficultyLevel == DifficultyLevel.HARD) {
            if (!ifCheck) {
                for (int i = 0; i < listOfShip.size(); i++) {
                    indexOfRow = listOfShip.get(i).getKey();
                    indexOfColumn = listOfShip.get(i).getValue();
                    if (indexOfRow == 1) {
                        if (indexOfColumn == 1) {
                            if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn+1-1).isWasHit()) {
                                return new Pair<>(indexOfRow, indexOfColumn+1);
                            } else if (!enemyBattlefield.getMap().get(indexOfRow+1-1).get(indexOfColumn-1).isWasHit()) {
                                return new Pair<>(indexOfRow+1, indexOfColumn);
                            }
                        } else if (indexOfColumn == 10) {
                            if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1-1).isWasHit()) {
                                return new Pair<>(indexOfRow, indexOfColumn-1);
                            } else if (!enemyBattlefield.getMap().get(indexOfRow+1-1).get(indexOfColumn-1).isWasHit()) {
                                return new Pair<>(indexOfRow+1, indexOfColumn);
                            }
                        } else {
                            if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1-1).isWasHit()) {
                                return new Pair<>(indexOfRow, indexOfColumn-1);
                            } else if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn+1-1).isWasHit()) {
                                return new Pair<>(indexOfRow, indexOfColumn+1);
                            } else if (!enemyBattlefield.getMap().get(indexOfRow+1-1).get(indexOfColumn-1).isWasHit()) {
                                return new Pair<>(indexOfRow+1, indexOfColumn);
                            }
                        }
                    } else if (indexOfRow == 10) {
                        if (indexOfColumn == 1) {
                            if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn+1-1).isWasHit()) {
                                return new Pair<>(indexOfRow, indexOfColumn+1);
                            } else if (!enemyBattlefield.getMap().get(indexOfRow-1-1).get(indexOfColumn-1).isWasHit()) {
                                return new Pair<>(indexOfRow-1, indexOfColumn);
                            }
                        } else if (indexOfColumn == 10) {
                            if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1-1).isWasHit()) {
                                return new Pair<>(indexOfRow, indexOfColumn-1);
                            } else if (!enemyBattlefield.getMap().get(indexOfRow-1-1).get(indexOfColumn-1).isWasHit()) {
                                return new Pair<>(indexOfRow-1, indexOfColumn);
                            }
                        } else {
                            if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1-1).isWasHit()) {
                                return new Pair<>(indexOfRow, indexOfColumn-1);
                            } else if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn+1-1).isWasHit()) {
                                return new Pair<>(indexOfRow, indexOfColumn+1);
                            } else if (!enemyBattlefield.getMap().get(indexOfRow-1-1).get(indexOfColumn-1).isWasHit()) {
                                return new Pair<>(indexOfRow-1, indexOfColumn);
                            }
                        }
                    } else {
                        if (indexOfColumn == 1) {
                            if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn+1-1).isWasHit()) {
                                return new Pair<>(indexOfRow, indexOfColumn+1);
                            } else if (!enemyBattlefield.getMap().get(indexOfRow+1-1).get(indexOfColumn-1).isWasHit()) {
                                return new Pair<>(indexOfRow+1, indexOfColumn);
                            } else if (!enemyBattlefield.getMap().get(indexOfRow-1-1).get(indexOfColumn-1).isWasHit()) {
                                return new Pair<>(indexOfRow-1, indexOfColumn);
                            }
                        } else if (indexOfColumn == 10) {
                            if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1-1).isWasHit()) {
                                return new Pair<>(indexOfRow, indexOfColumn-1);
                            } else if (!enemyBattlefield.getMap().get(indexOfRow+1-1).get(indexOfColumn-1).isWasHit()) {
                                return new Pair<>(indexOfRow+1, indexOfColumn);
                            } else if (!enemyBattlefield.getMap().get(indexOfRow-1-1).get(indexOfColumn-1).isWasHit()) {
                                return new Pair<>(indexOfRow-1, indexOfColumn);
                            }
                        } else {
                            if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn-1-1).isWasHit()) {
                                return new Pair<>(indexOfRow, indexOfColumn-1);
                            } else if (!enemyBattlefield.getMap().get(indexOfRow-1).get(indexOfColumn+1-1).isWasHit()) {
                                return new Pair<>(indexOfRow, indexOfColumn+1);
                            } else if (!enemyBattlefield.getMap().get(indexOfRow+1-1).get(indexOfColumn-1).isWasHit()) {
                                return new Pair<>(indexOfRow+1, indexOfColumn);
                            } else if (!enemyBattlefield.getMap().get(indexOfRow-1-1).get(indexOfColumn-1).isWasHit()) {
                                return new Pair<>(indexOfRow-1, indexOfColumn);
                            }
                        }
                    }
                }
                Random rand = new Random();
                while (enemyBattlefield.getMap().get(indexOfRow - 1).get(indexOfColumn - 1).isWasHit()) {
                    indexOfRow = rand.nextInt(10) + 1;
                    indexOfColumn = rand.nextInt(10) + 1;
                }
            } else {
                Random rand = new Random();
                while (enemyBattlefield.getMap().get(indexOfRow - 1).get(indexOfColumn - 1).isWasHit()) {
                    indexOfRow = rand.nextInt(10) + 1;
                    indexOfColumn = rand.nextInt(10) + 1;
                }
            }
        }
        return new Pair<>(indexOfRow, indexOfColumn);
    }

}
