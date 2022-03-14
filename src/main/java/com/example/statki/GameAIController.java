package com.example.statki;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GameAIController {

    public GridPane battlefieldLeftGrid;
    public GridPane battlefieldRightGrid;
    public Label player1Label;
    public Label player2Label;
    public Player player;
    public AIPlayer leftPlayer;
    public AIPlayer rightPlayer;
    public Game game;
    public List<Pair<Integer, Integer>> shipsCellsPairsLeft = new ArrayList<>();
    public List<Pair<Integer, Integer>> shipsCellsPairsRight = new ArrayList<>();
    public List<ArrayList> shipsCellsLeft = new ArrayList<>();
    public List<ArrayList> shipsCellsRight = new ArrayList<>();
    public Button nextButton;
    public Button leftButton;
    public Button rightButton;
    public List<Pair<Integer,Integer>> listOfShotsLeft = new ArrayList<>();
    public List<Pair<Integer,Integer>> listOfShotsRight = new ArrayList<>();
    public int indexOfColumnLeft;
    public int indexOfRowLeft;
    public int indexOfColumnRight;
    public int indexOfRowRight;
    public boolean firstTimeLeftPlayer = true;
    public boolean firstTimeRightPlayer = true;
    public boolean ifCheckLeft = true;
    public List<Pair<Integer, Integer>> listOfShipLeft = new ArrayList<>();
    public boolean ifCheckRight = true;
    public List<Pair<Integer, Integer>> listOfShipRight = new ArrayList<>();
    public Date startGameTime = new Date();
    public AnchorPane ap;

    public void initialize() {

        for (int j = 1; j < 11; j++) {
            for (int i = 1; i < 11; i++) {
                Pane cell1 = new Pane();
                cell1.setStyle("-fx-border-color: black;");
                GridPane.setConstraints(cell1, j, i);
                battlefieldLeftGrid.getChildren().add(cell1);

                Pane cell2 = new Pane();
                cell2.setStyle("-fx-border-color: black;");
                GridPane.setConstraints(cell2, j, i);
                battlefieldRightGrid.getChildren().add(cell2);

                cell1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                    }
                });

                cell2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                    }
                });
            }
        }
    }

    public void rightClicked(ActionEvent actionEvent) throws IOException {
        game.deleteGame();
        if (player != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("myAccount.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            MyAccountController myAccountController = loader.getController();
            myAccountController.setPlayer(player);
            myAccountController.setStatistics();
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
        else {
            Parent root =  FXMLLoader.load(getClass().getResource("register.fxml"));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void leftClicked(ActionEvent actionEvent) throws IOException {
        game.deleteGame();
        if (player != null) {
            player = null;
            leftButton.setText("Zaloguj się");
            rightButton.setText("Rejestracja");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("start.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    public void makeListOfShipsPoints(AIPlayer tempPlayer, List<ArrayList> shipsCells) {
        for (int i = 0; i < 10; i++) {
            int column = tempPlayer.getBattlefield().getShips().get(i).getStartingPoint().getIndexOfColumn();
            int row = tempPlayer.getBattlefield().getShips().get(i).getStartingPoint().getIndexOfRow();
            int length = tempPlayer.getBattlefield().getShips().get(i).getLength();
            if (tempPlayer.getBattlefield().getShips().get(i).getPlacement() == Ship.PlacementOfShips.HORIZONTAL) {
                for (int j = 0; j < length; j++) {
                    ArrayList<Integer> tempList = new ArrayList();
                    tempList.add(row);
                    tempList.add(column + j);
                    tempList.add(length);
                    tempList.add(1);
                    shipsCells.add(tempList);
                }
            } else {
                for (int j = 0; j < length; j++) {
                    ArrayList<Integer> tempList = new ArrayList();
                    tempList.add(row + j);
                    tempList.add(column);
                    tempList.add(length);
                    tempList.add(2);
                    shipsCells.add(tempList);
                }
            }
        }
    }

    public void makeListOfShipsPairs(AIPlayer tempPlayer, List<Pair<Integer,Integer>> shipsCells) {
        for (int i = 0; i < 10; i++) {
            int column = tempPlayer.getBattlefield().getShips().get(i).getStartingPoint().getIndexOfColumn();
            int row = tempPlayer.getBattlefield().getShips().get(i).getStartingPoint().getIndexOfRow();
            int length = tempPlayer.getBattlefield().getShips().get(i).getLength();
            if (tempPlayer.getBattlefield().getShips().get(i).getPlacement() == Ship.PlacementOfShips.HORIZONTAL) {
                for (int j = 0; j < length; j++) {
                    shipsCells.add(new Pair<>(row, column + j));
                }
            } else {
                for (int j = 0; j < length; j++) {
                    shipsCells.add(new Pair<>(row + j, column));
                }
            }
        }
    }

    public void colorShips(List<ArrayList> shipsCells, GridPane battlefieldGrid) {
        int length = shipsCells.size();
        int x; //row
        int y; //column
        for (int i = 0; i < length; i++) {
            x = (int) shipsCells.get(i).get(0);
            y = (int) shipsCells.get(i).get(1);
            Pane cell = new Pane();
            cell.setStyle("-fx-background-color: #7e63eb; -fx-border-color: black;");
            GridPane.setConstraints(cell, y, x);
            battlefieldGrid.getChildren().add(cell);
        }
    }

    public void unsetGame(Stage stage) {
        if (!game.isGameOver()) {
            game.deleteGame();
        }
    }

    public void setBots(AIPlayer playerBot1, AIPlayer playerBot2, Player player, BorderPane ap) {

        Stage stage = (Stage) ap.getScene().getWindow();
        stage.setOnCloseRequest(event -> unsetGame(stage));

        this.leftPlayer = playerBot1;
        this.rightPlayer = playerBot2;
        this.player = player;
        this.game = new Game(leftPlayer, rightPlayer);
        game.startGame();
        rightPlayer.setMyTurn(false);
        makeListOfShipsPoints(leftPlayer, shipsCellsLeft);
        makeListOfShipsPairs(leftPlayer, shipsCellsPairsLeft);
        colorShips(shipsCellsLeft, battlefieldLeftGrid);
        makeListOfShipsPoints(rightPlayer, shipsCellsRight);
        makeListOfShipsPairs(rightPlayer, shipsCellsPairsRight);
        colorShips(shipsCellsRight, battlefieldRightGrid);
    }

    public void nextButtonClicked(ActionEvent actionEvent) throws IOException {
        if (rightPlayer.isMyTurn()) {
            if (firstTimeRightPlayer) {
                Random rand = new Random();
                indexOfColumnLeft = rand.nextInt(10)+1;
                indexOfRowLeft = rand.nextInt(10)+1;
                listOfShipRight.add(new Pair<>(indexOfRowLeft, indexOfColumnLeft));
                Pane cell = new Pane();
                listOfShotsRight.add(new Pair<>(indexOfRowLeft, indexOfColumnLeft));
                if (leftPlayer.getBattlefield().getMap().get(indexOfRowLeft-1).get(indexOfColumnLeft-1).isShip()) {
                    GameController.playMusicExplosion();
                    ifCheckRight = false;
                    cell.setStyle("-fx-background-color: #ff1f35; -fx-border-color: black;");
                    GridPane.setConstraints(cell, indexOfColumnLeft, indexOfRowLeft);
                    battlefieldLeftGrid.getChildren().add(cell);
                    boolean check = checkIfEntire(indexOfRowLeft, indexOfColumnLeft, shipsCellsLeft, shipsCellsPairsLeft, listOfShotsRight);
                    if (check) {
                        ifCheckRight = true;
                        sinkThatBastard(indexOfRowLeft, indexOfColumnLeft, rightPlayer, shipsCellsLeft, battlefieldLeftGrid, shipsCellsPairsLeft);
                        game.setMove(indexOfRowLeft, indexOfColumnLeft, rightPlayer.getIdUser(), true, true);
                    } else {
                        game.setMove(indexOfRowLeft, indexOfColumnLeft, rightPlayer.getIdUser(), true, false);
                    }
                } else {
                    GameController.playMusicSplash();
                    cell.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
                    GridPane.setConstraints(cell, indexOfColumnLeft, indexOfRowLeft);
                    battlefieldLeftGrid.getChildren().add(cell);
                    rightPlayer.setMyTurn(false);
                    leftPlayer.setMyTurn(true);
                    game.setMove(indexOfRowLeft, indexOfColumnLeft, rightPlayer.getIdUser(), false,false);
                }
                firstTimeRightPlayer = false;
            } else {
                Pair<Integer, Integer> indexes = rightPlayer.move(leftPlayer.getBattlefield(), listOfShipRight, indexOfRowLeft, indexOfColumnLeft,ifCheckRight);
                indexOfRowLeft = indexes.getKey();
                indexOfColumnLeft = indexes.getValue();
                Pane cell = new Pane();
                listOfShotsRight.add(new Pair<>(indexOfRowLeft, indexOfColumnLeft));
                if (leftPlayer.getBattlefield().getMap().get(indexOfRowLeft-1).get(indexOfColumnLeft-1).isShip()) {
                    GameController.playMusicExplosion();
                    if (ifCheckRight) {
                        listOfShipRight = new ArrayList<>();
                    }
                    listOfShipRight.add(new Pair<>(indexOfRowLeft, indexOfColumnLeft));
                    ifCheckRight = false;
                    cell.setStyle("-fx-background-color: #ff1f35; -fx-border-color: black;");
                    GridPane.setConstraints(cell, indexOfColumnLeft, indexOfRowLeft);
                    battlefieldLeftGrid.getChildren().add(cell);
                    boolean check = checkIfEntire(indexOfRowLeft, indexOfColumnLeft, shipsCellsLeft, shipsCellsPairsLeft, listOfShotsRight);
                    if (check) {
                        ifCheckRight = true;
                        sinkThatBastard(indexOfRowLeft, indexOfColumnLeft, leftPlayer, shipsCellsLeft, battlefieldLeftGrid, shipsCellsPairsLeft);
                        game.setMove(indexOfRowLeft, indexOfColumnLeft, rightPlayer.getIdUser(), true, true);
                    } else {
                        game.setMove(indexOfRowLeft, indexOfColumnLeft, rightPlayer.getIdUser(), true, false);
                    }
                } else {
                    GameController.playMusicSplash();
                    cell.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
                    GridPane.setConstraints(cell, indexOfColumnLeft, indexOfRowLeft);
                    battlefieldLeftGrid.getChildren().add(cell);
                    rightPlayer.setMyTurn(false);
                    leftPlayer.setMyTurn(true);
                    game.setMove(indexOfRowLeft, indexOfColumnLeft, rightPlayer.getIdUser(), false,false);
                }
            }
            leftPlayer.getBattlefield().getMap().get(indexOfRowLeft-1).get(indexOfColumnLeft-1).setWasHit(true);
            if (leftPlayer.getBattlefield().checkIfShipsAlive()) {
                game.setGameOver(true);
                System.out.println("bot2 wygral!");
                game.setWinner(rightPlayer);
                game.setLoser(leftPlayer);
                game.updateWinnerAndLoser();

                Date endGameTime = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                System.out.println("Czas zakończenia gry: " + dateFormat.format(endGameTime));
                System.out.println("To jest startGame w if won: " + startGameTime);
                String gameTime = GameController.printDifference(startGameTime, endGameTime);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("endgame.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                EndgameController endgameController = loader.getController();
                endgameController.setGame(game, player, gameTime);
                Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }

        } else if (leftPlayer.isMyTurn()) {
            if (firstTimeLeftPlayer) {
                Random rand = new Random();
                indexOfColumnRight = rand.nextInt(10)+1;
                indexOfRowRight = rand.nextInt(10)+1;
                listOfShipLeft.add(new Pair<>(indexOfRowRight, indexOfColumnRight));
                Pane cell = new Pane();
                listOfShotsLeft.add(new Pair<>(indexOfRowRight, indexOfColumnRight));
                if (rightPlayer.getBattlefield().getMap().get(indexOfRowRight-1).get(indexOfColumnRight-1).isShip()) {
                    GameController.playMusicExplosion();
                    ifCheckLeft = false;
                    cell.setStyle("-fx-background-color: #ff1f35; -fx-border-color: black;");
                    GridPane.setConstraints(cell, indexOfColumnRight, indexOfRowRight);
                    battlefieldRightGrid.getChildren().add(cell);
                    boolean check = checkIfEntire(indexOfRowRight, indexOfColumnRight, shipsCellsRight, shipsCellsPairsRight, listOfShotsLeft);
                    if (check) {
                        ifCheckLeft = true;
                        sinkThatBastard(indexOfRowRight, indexOfColumnRight, leftPlayer, shipsCellsRight, battlefieldRightGrid, shipsCellsPairsRight);
                        game.setMove(indexOfRowRight, indexOfColumnRight, leftPlayer.getIdUser(), true, true);
                    } else {
                        game.setMove(indexOfRowRight, indexOfColumnRight, leftPlayer.getIdUser(), true, false);
                    }
                } else {
                    GameController.playMusicSplash();
                    cell.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
                    GridPane.setConstraints(cell, indexOfColumnRight, indexOfRowRight);
                    battlefieldRightGrid.getChildren().add(cell);
                    leftPlayer.setMyTurn(false);
                    rightPlayer.setMyTurn(true);
                    game.setMove(indexOfRowRight, indexOfColumnRight, leftPlayer.getIdUser(), false,false);
                }
                firstTimeLeftPlayer = false;
            } else {
                Pair<Integer, Integer> indexes = leftPlayer.move(rightPlayer.getBattlefield(), listOfShipLeft, indexOfRowRight, indexOfColumnRight, ifCheckLeft); //listOfShipLeft.get(0).getKey()
                indexOfRowRight = indexes.getKey();
                indexOfColumnRight = indexes.getValue();
                Pane cell = new Pane();
                listOfShotsLeft.add(new Pair<>(indexOfRowRight, indexOfColumnRight));
                if (rightPlayer.getBattlefield().getMap().get(indexOfRowRight-1).get(indexOfColumnRight-1).isShip()) {
                    GameController.playMusicExplosion();
                    if (ifCheckLeft) {
                        listOfShipLeft = new ArrayList<>();
                    }
                    listOfShipLeft.add(new Pair<>(indexOfRowRight, indexOfColumnRight));
                    ifCheckLeft = false;
                    cell.setStyle("-fx-background-color: #ff1f35; -fx-border-color: black;");
                    GridPane.setConstraints(cell, indexOfColumnRight, indexOfRowRight);
                    battlefieldRightGrid.getChildren().add(cell);
                    boolean check = checkIfEntire(indexOfRowRight, indexOfColumnRight, shipsCellsRight, shipsCellsPairsRight, listOfShotsLeft);
                    if (check) {
                        ifCheckLeft = true;
                        sinkThatBastard(indexOfRowRight, indexOfColumnRight, rightPlayer, shipsCellsRight, battlefieldRightGrid, shipsCellsPairsRight);
                        game.setMove(indexOfRowRight, indexOfColumnRight, leftPlayer.getIdUser(), true, true);
                    } else {
                        game.setMove(indexOfRowRight, indexOfColumnRight, leftPlayer.getIdUser(), true, false);
                    }

                } else {
                    GameController.playMusicSplash();
                    cell.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
                    GridPane.setConstraints(cell, indexOfColumnRight, indexOfRowRight);
                    battlefieldRightGrid.getChildren().add(cell);
                    leftPlayer.setMyTurn(false);
                    rightPlayer.setMyTurn(true);
                    game.setMove(indexOfRowRight, indexOfColumnRight, leftPlayer.getIdUser(), false,false);
                }
            }
            rightPlayer.getBattlefield().getMap().get(indexOfRowRight-1).get(indexOfColumnRight-1).setWasHit(true);
            if (rightPlayer.getBattlefield().checkIfShipsAlive()) {
                game.setGameOver(true);
                System.out.println("bot1 wygral!");
                game.setWinner(leftPlayer);
                game.setLoser(rightPlayer);
                game.updateWinnerAndLoser();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                Date endGameTime = new Date();
                System.out.println("Czas zakończenia gry: " + dateFormat.format(endGameTime));
                System.out.println("To jest startGame w if won: " + startGameTime);
                String gameTime = GameController.printDifference(startGameTime, endGameTime);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("endgame.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                EndgameController endgameController = loader.getController();
                endgameController.setGame(game, player, gameTime);

                Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    public boolean checkIfEntire(int row, int column, List<ArrayList> shipsCells, List<Pair<Integer,Integer>> enemyShips, List<Pair<Integer,Integer>> myShots){
        int tempLength = 0;
        int tempPlacement = 0;
        for(int i=0; i<shipsCells.size(); i++){
            if(((int)shipsCells.get(i).get(0))==row && ((int)shipsCells.get(i).get(1))==column){
                tempLength = (int) shipsCells.get(i).get(2);
                tempPlacement = (int) shipsCells.get(i).get(3);
            }
        }
        System.out.println(tempLength);
        System.out.println(tempPlacement);
        switch (tempLength){
            case 1:
                return true;
            case 2:
                if(tempPlacement==1){
                    Pair<Integer,Integer> leftCell = new Pair<>(row,column-1);
                    Pair<Integer,Integer> rightCell = new Pair<>(row,column+1);
                    if (myShots.contains(leftCell) && enemyShips.contains(leftCell)){
                        System.out.println("OK");
                        return true;
                    }
                    else if (myShots.contains(rightCell) && enemyShips.contains(rightCell)){
                        System.out.println("OK");
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                if(tempPlacement==2){
                    Pair<Integer,Integer> upperCell = new Pair<>(row-1,column);
                    Pair<Integer,Integer> lowerCell = new Pair<>(row+1,column);
                    if (myShots.contains(upperCell) && enemyShips.contains(upperCell)){
                        System.out.println("OK");
                        return true;
                    }
                    else if (myShots.contains(lowerCell) && enemyShips.contains(lowerCell)){
                        System.out.println("OK");
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            case 3:
                if(tempPlacement==1){
                    Pair<Integer,Integer> leftCell1 = new Pair<>(row,column-1);
                    Pair<Integer,Integer> leftCell2 = new Pair<>(row,column-2);
                    Pair<Integer,Integer> rightCell1 = new Pair<>(row,column+1);
                    Pair<Integer,Integer> rightCell2 = new Pair<>(row,column+2);
                    if(enemyShips.contains(leftCell1) && enemyShips.contains(leftCell2)){
                        if(myShots.contains(leftCell1) && myShots.contains(leftCell2)){
                            System.out.println("OK");
                            return true;
                        }
                    }
                    if(enemyShips.contains(rightCell1) && enemyShips.contains(rightCell2)){
                        if(myShots.contains(rightCell1) && myShots.contains(rightCell2)){
                            System.out.println("OK");
                            return true;
                        }
                    }
                    if(enemyShips.contains(rightCell1) && enemyShips.contains(leftCell1)){
                        if(myShots.contains(rightCell1) && myShots.contains(leftCell1)){
                            System.out.println("OK");
                            return true;
                        }
                    }
                    else {
                        return false;
                    }
                }
                if(tempPlacement==2){
                    Pair<Integer,Integer> upperCell1 = new Pair<>(row-1,column);
                    Pair<Integer,Integer> upperCell2 = new Pair<>(row-2,column);
                    Pair<Integer,Integer> lowerCell1 = new Pair<>(row+1,column);
                    Pair<Integer,Integer> lowerCell2 = new Pair<>(row+2,column);
                    if(enemyShips.contains(upperCell1) && enemyShips.contains(upperCell2)){
                        if(myShots.contains(upperCell1) && myShots.contains(upperCell2)){
                            System.out.println("OK");
                            return true;
                        }
                    }
                    if(enemyShips.contains(lowerCell1) && enemyShips.contains(lowerCell2)){
                        if(myShots.contains(lowerCell1) && myShots.contains(lowerCell2)){
                            System.out.println("OK");
                            return true;
                        }
                    }
                    if(enemyShips.contains(lowerCell1) && enemyShips.contains(upperCell1)){
                        if(myShots.contains(lowerCell1) && myShots.contains(upperCell1)){
                            System.out.println("OK");
                            return true;
                        }
                    }
                    else {
                        return false;
                    }
                }
            case 4:
                if(tempPlacement==1){
                    Pair<Integer,Integer> leftCell1 = new Pair<>(row,column-1);
                    Pair<Integer,Integer> leftCell2 = new Pair<>(row,column-2);
                    Pair<Integer,Integer> leftCell3 = new Pair<>(row,column-3);
                    Pair<Integer,Integer> rightCell1 = new Pair<>(row,column+1);
                    Pair<Integer,Integer> rightCell2 = new Pair<>(row,column+2);
                    Pair<Integer,Integer> rightCell3 = new Pair<>(row,column+3);
                    if(enemyShips.contains(leftCell1) && enemyShips.contains(leftCell2) && enemyShips.contains(leftCell3)){
                        if(myShots.contains(leftCell1) && myShots.contains(leftCell2) && myShots.contains(leftCell3)){
                            System.out.println("OK");
                            return true;
                        }
                    }
                    if(enemyShips.contains(rightCell1) && enemyShips.contains(rightCell2) && enemyShips.contains(rightCell3)){
                        if(myShots.contains(rightCell1) && myShots.contains(rightCell2) && myShots.contains(rightCell3)){
                            System.out.println("OK");
                            return true;
                        }
                    }

                    if(enemyShips.contains(leftCell1) && enemyShips.contains(leftCell2) && enemyShips.contains(rightCell1)){
                        if(myShots.contains(leftCell1) && myShots.contains(leftCell2) && myShots.contains(rightCell1)){
                            System.out.println("OK");
                            return true;
                        }
                    }
                    if(enemyShips.contains(leftCell1) && enemyShips.contains(rightCell1) && enemyShips.contains(rightCell2)){
                        if(myShots.contains(leftCell1) && myShots.contains(rightCell1) && myShots.contains(rightCell2)){
                            System.out.println("OK");
                            return true;
                        }
                    }
                    else {
                        return false;
                    }
                }
                if(tempPlacement==2){
                    Pair<Integer,Integer> upperCell1 = new Pair<>(row-1,column);
                    Pair<Integer,Integer> upperCell2 = new Pair<>(row-2,column);
                    Pair<Integer,Integer> upperCell3 = new Pair<>(row-3,column);
                    Pair<Integer,Integer> lowerCell1 = new Pair<>(row+1,column);
                    Pair<Integer,Integer> lowerCell2 = new Pair<>(row+2,column);
                    Pair<Integer,Integer> lowerCell3 = new Pair<>(row+3,column);
                    if(enemyShips.contains(upperCell1) && enemyShips.contains(upperCell2) && enemyShips.contains(upperCell3)){
                        if(myShots.contains(upperCell1) && myShots.contains(upperCell2) && myShots.contains(upperCell3)){
                            System.out.println("OK");
                            return true;
                        }
                    }
                    if(enemyShips.contains(lowerCell1) && enemyShips.contains(lowerCell2) && enemyShips.contains(lowerCell3)){
                        if(myShots.contains(lowerCell1) && myShots.contains(lowerCell2) && myShots.contains(lowerCell3)){
                            System.out.println("OK");
                            return true;
                        }
                    }

                    if(enemyShips.contains(upperCell1) && enemyShips.contains(lowerCell1) && enemyShips.contains(lowerCell2)){
                        if(myShots.contains(upperCell1) && myShots.contains(lowerCell1) && myShots.contains(lowerCell2)){
                            System.out.println("OK");
                            return true;
                        }
                    }
                    if(enemyShips.contains(upperCell1) && enemyShips.contains(upperCell2) && enemyShips.contains(lowerCell1)){
                        if(myShots.contains(upperCell1) && myShots.contains(upperCell2) && myShots.contains(lowerCell1)){
                            System.out.println("OK");
                            return true;
                        }
                    }
                    else {
                        return false;
                    }
                }
        }
        return false;
    }

    public List<ArrayList> numberShips(List<ArrayList> ships){
        List<ArrayList> numbered = new ArrayList<>();
        numbered = ships;
        int n = 1;
        int k = 0; // k < 20
        for(int i=0; i<4; i++){
            numbered.get(k).add(n);
            k++;
        }
        n = n + 1;

        for(int j=0; j<2; j++){
            for(int i=0; i<3; i++){
                numbered.get(k).add(n);
                k++;
            }
            n++;
        }

        for(int j=0; j<3; j++){
            for(int i=0; i<2; i++){
                numbered.get(k).add(n);
                k++;
            }
            n++;
        }

        for(int j=0; j<4; j++){
            numbered.get(k).add(n);
            k++;
            n++;
        }

        return numbered;
    }

    public void sinkThatBastard(int row, int column, AIPlayer enemy, List<ArrayList> shipsCells, GridPane battlefieldGrid, List<Pair<Integer, Integer>> shipsCellsPairs){
        List<ArrayList> numberedShips;
        numberedShips = numberShips(shipsCells);
        int tempLength=0;
        int tempPlacement=0;
        int shipIndex=0;
        for(int i=0; i< shipsCells.size(); i++){
            if(((int)shipsCells.get(i).get(0))==row && ((int)shipsCells.get(i).get(1))==column){
                tempLength=(int)shipsCells.get(i).get(2);
                tempPlacement=(int)shipsCells.get(i).get(3);
                shipIndex=(int)numberedShips.get(i).get(4);
            }
        }
        System.out.println(tempLength);
        System.out.println(tempPlacement);
        System.out.println(shipIndex);

        int tempRow = 0;
        int tempColumn = 0;

        if(shipIndex==1){
            tempRow = (int)numberedShips.get(0).get(0);
            tempColumn = (int)numberedShips.get(0).get(1);
            System.out.println(tempRow);
            System.out.println(tempColumn);
        }

        if(shipIndex==2){
            tempRow = (int)numberedShips.get(4).get(0);
            tempColumn = (int)numberedShips.get(4).get(1);
            System.out.println(tempRow);
            System.out.println(tempColumn);
        }

        if(shipIndex==3){
            tempRow = (int)numberedShips.get(7).get(0);
            tempColumn = (int)numberedShips.get(7).get(1);
            System.out.println(tempRow);
            System.out.println(tempColumn);
        }

        if(shipIndex==4){
            tempRow = (int)numberedShips.get(10).get(0);
            tempColumn = (int)numberedShips.get(10).get(1);
            System.out.println(tempRow);
            System.out.println(tempColumn);
        }

        if(shipIndex==5){
            tempRow = (int)numberedShips.get(12).get(0);
            tempColumn = (int)numberedShips.get(12).get(1);
            System.out.println(tempRow);
            System.out.println(tempColumn);
        }

        if(shipIndex==6){
            tempRow = (int)numberedShips.get(14).get(0);
            tempColumn = (int)numberedShips.get(14).get(1);
            System.out.println(tempRow);
            System.out.println(tempColumn);
        }

        if(shipIndex==7){
            tempRow = (int)numberedShips.get(16).get(0);
            tempColumn = (int)numberedShips.get(16).get(1);
            System.out.println(tempRow);
            System.out.println(tempColumn);
        }

        if(shipIndex==8){
            tempRow = (int)numberedShips.get(17).get(0);
            tempColumn = (int)numberedShips.get(17).get(1);
            System.out.println(tempRow);
            System.out.println(tempColumn);
        }

        if(shipIndex==9){
            tempRow = (int)numberedShips.get(18).get(0);
            tempColumn = (int)numberedShips.get(18).get(1);
            System.out.println(tempRow);
            System.out.println(tempColumn);
        }

        if(shipIndex==10){
            tempRow = (int)numberedShips.get(19).get(0);
            tempColumn = (int)numberedShips.get(19).get(1);
            System.out.println(tempRow);
            System.out.println(tempColumn);
        }

        surroundGray(tempRow, tempColumn, tempPlacement, enemy, tempLength, battlefieldGrid, shipsCellsPairs);

    }

    public void surroundGray (int row, int column, int placement, AIPlayer enemy, int length, GridPane battlefieldGrid, List<Pair<Integer, Integer>> shipsCellsPairs){

        if(placement==1){
            for(int i=-1; i<2; i++){
                for(int j=-1; j<(length+1); j++){
                    Pair<Integer,Integer> tempCell = new Pair<>(row+i,column+j);
                    if(shipsCellsPairs.contains(tempCell)){
                        Pane blackPane = new Pane();
                        blackPane.setStyle("-fx-background-color: black; -fx-border-color: black;");
                        GridPane.setConstraints(blackPane, (column+j), (row+i));
                        battlefieldGrid.getChildren().add(blackPane);
                    }
                    else if ((column+j)!=0){
                        if ((column+j)!=11) {
                            if ((row+i)!=0) {
                                if ((row+i)!=11) {
                                    Pane grayPane = new Pane();
                                    grayPane.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
                                    GridPane.setConstraints(grayPane, (column + j), (row + i));
                                    battlefieldGrid.getChildren().add(grayPane);
                                    enemy.getBattlefield().getMap().get(row+i-1).get(column+j-1).setWasHit(true);
                                }
                            }
                        }
                    }
                }
            }
        }
        if(placement==2){
            for(int i=-1; i<(length+1); i++){
                for(int j=-1; j<2; j++) {
                    Pair<Integer, Integer> tempCell = new Pair<>(row + i, column + j);
                    if (shipsCellsPairs.contains(tempCell)) {
                        Pane blackPane = new Pane();
                        blackPane.setStyle("-fx-background-color: black; -fx-border-color: black;");
                        GridPane.setConstraints(blackPane, (column + j), (row + i));
                        battlefieldGrid.getChildren().add(blackPane);
                    } else if ((column + j) != 0) {
                        if ((column + j) != 11) {
                            if ((row + i) != 0) {
                                if ((row + i) != 11) {
                                    Pane grayPane = new Pane();
                                    grayPane.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
                                    GridPane.setConstraints(grayPane, (column + j), (row + i));
                                    battlefieldGrid.getChildren().add(grayPane);
                                    enemy.getBattlefield().getMap().get(row+i-1).get(column+j-1).setWasHit(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void cancelLabelOnAction(MouseEvent mouseEvent) throws IOException {
        game.deleteGame();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("start.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        StartController startController = loader.getController();
        startController.setPlayer(player);
        Stage stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}