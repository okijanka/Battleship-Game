package com.example.statki;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

public class GameController {

    public GridPane battlefieldLeftGrid;
    public GridPane battlefieldRightGrid;
    public GridPane battlefieldLeftGridSecond;
    public GridPane battlefieldRightGridSecond;
    public Label player1Label;
    public Label player2Label;
    public Label infoLabel;
    public Player leftPlayer;
    public Player rightPlayer;
    public Game game;
    public List<Pair<Integer, Integer>> shipsCellsPairsLeft = new ArrayList<>();
    public List<Pair<Integer, Integer>> shipsCellsPairsRight = new ArrayList<>();
    public List<ArrayList> shipsCellsLeft = new ArrayList<>();
    public List<ArrayList> shipsCellsRight = new ArrayList<>();
    public Button nextButton;
    public Button bufferButton;
    public List<Pair<Integer,Integer>> listOfShotsLeft = new ArrayList<>();
    public List<Pair<Integer,Integer>> listOfShotsRight = new ArrayList<>();
    public Player player;
    public Button leftButton;
    public Button rightButton;
    public Date startGameTime = new Date();
    public static MediaPlayer mediaPlayer;

    public void initialize() {

        battlefieldLeftGridSecond.setVisible(false);
        battlefieldRightGridSecond.setDisable(true);
        battlefieldRightGridSecond.setVisible(false);

        infoLabel.setVisible(false);
        bufferButton.setDisable(true);
        nextButton.setDisable(true);


        for (int j = 1; j < 11; j++) {
            for (int i = 1; i < 11; i++) {
                Pane cell1 = new Pane();
                cell1.setStyle("-fx-border-color: black;");
                GridPane.setConstraints(cell1, j, i);
                battlefieldLeftGrid.getChildren().add(cell1);

                Pane cellSecond1 = new Pane();
                cellSecond1.setStyle("-fx-border-color: black;");
                GridPane.setConstraints(cellSecond1, j, i);
                battlefieldLeftGridSecond.getChildren().add(cellSecond1);

                Pane cell2 = new Pane();
                cell2.setStyle("-fx-border-color: black;");
                GridPane.setConstraints(cell2, j, i);
                battlefieldRightGrid.getChildren().add(cell2);

                Pane cellSecond2 = new Pane();
                cellSecond2.setStyle("-fx-border-color: black;");
                GridPane.setConstraints(cellSecond2, j,i);
                battlefieldRightGridSecond.getChildren().add(cellSecond2);

                int finalI = i;
                int finalJ = j;

                cell1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                    }
                });

                cell2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (leftPlayer.isMyTurn()) {
                            drawMap(finalI, finalJ, cell2, leftPlayer, rightPlayer, listOfShotsLeft, shipsCellsPairsRight, battlefieldRightGrid, battlefieldLeftGridSecond, shipsCellsRight);
                            if (rightPlayer.getBattlefield().checkIfShipsAlive()) {
                                try {
                                    ifPlayerWon(leftPlayer, rightPlayer, mouseEvent);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

                cellSecond1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                    }
                });

                cellSecond2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (rightPlayer.isMyTurn()) {
                            drawMap(finalI, finalJ, cellSecond2, rightPlayer, leftPlayer, listOfShotsRight, shipsCellsPairsLeft, battlefieldRightGridSecond, battlefieldLeftGrid, shipsCellsLeft);
                            if (leftPlayer.getBattlefield().checkIfShipsAlive()) {
                                try {
                                    ifPlayerWon(rightPlayer, leftPlayer, mouseEvent);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    public void drawMap(int finalI, int finalJ, Pane cell2, Player player, Player enemy, List<Pair<Integer,Integer>> listOfShots, List<Pair<Integer,Integer>> shipsCellsPairs, GridPane battlefieldGrid, GridPane otherBattlefieldGrid, List<ArrayList> shipsCells) {
        listOfShots.add(new Pair<>(finalI,finalJ));
        System.out.println(listOfShots);
        int tempRow = finalI;
        int tempColumn = finalJ;
        Pair tempCell = new Pair(tempRow, tempColumn);
        if( shipsCellsPairs.contains(tempCell) ) {
            playMusicExplosion();
            Pane cell = new Pane();
            cell.setStyle("-fx-background-color: #ff1f35; -fx-border-color: black;");
            GridPane.setConstraints(cell, tempColumn, tempRow);
            battlefieldGrid.getChildren().add(cell);

            cell = new Pane();
            cell.setStyle("-fx-background-color: #ff1f35; -fx-border-color: black;");
            GridPane.setConstraints(cell, tempColumn, tempRow);
            otherBattlefieldGrid.getChildren().add(cell);

            cell2.setDisable(true);
            boolean check = checkIfEntire(finalI, finalJ, shipsCells, enemy, shipsCellsPairs, listOfShots);
            System.out.println(check);
            enemy.getBattlefield().getMap().get(tempRow-1).get(tempColumn-1).setWasHit(true);
            if(check){
                sinkThatBastard(finalI,finalJ, shipsCells, battlefieldGrid, otherBattlefieldGrid, shipsCellsPairs);
                game.setMove(tempRow, tempColumn, player.getIdUser(), true, true);
            } else {
                game.setMove(tempRow, tempColumn, player.getIdUser(), true, false);
            }
            player.incrementNumberOfAllShots();
            player.incrementNumberOfHitShots();
        } else {
            playMusicSplash();
            player.incrementNumberOfAllShots();
            Pane cell = new Pane();
            cell.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
            GridPane.setConstraints(cell, tempColumn, tempRow);
            battlefieldGrid.getChildren().add(cell);

            cell = new Pane();
            cell.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
            GridPane.setConstraints(cell, tempColumn, tempRow);
            otherBattlefieldGrid.getChildren().add(cell);

            cell2.setDisable(true);
            enemy.getBattlefield().getMap().get(tempRow-1).get(tempColumn-1).setWasHit(true);
            player.setMyTurn(false);
            enemy.setMyTurn(true);
            bufferButton.setDisable(false);
            game.setMove(tempRow, tempColumn, player.getIdUser(), false,false);
        }
    }

    public void ifPlayerWon(Player winner, Player loser, MouseEvent mouseEvent) throws IOException {
        game.setGameOver(true);
        leftPlayer.incrementNumberOfGames();
        rightPlayer.incrementNumberOfGames();
        winner.incrementWonGames();
        loser.incrementLostGames();
        leftPlayer.setAccuracy();
        rightPlayer.setAccuracy();
        game.setWinner(winner);
        game.setLoser(loser);
        game.updateWinnerAndLoser();
        game.setStatistics(leftPlayer);
        game.setStatistics(rightPlayer);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date endGameTime = new Date();
        System.out.println("Czas zakończenia gry: " + dateFormat.format(endGameTime));
        System.out.println("To jest startGame w if won: " + startGameTime);
        String gameTime = printDifference(startGameTime, endGameTime);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("endgame.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        EndgameController endgameController = loader.getController();

        endgameController.setGame(game, player, gameTime);
        Stage stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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

    public void cancelLabelOnAction(MouseEvent mouseEvent) throws IOException, InterruptedException {
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

    public boolean checkIfEntire(int row, int column, List<ArrayList> shipsCells, Player player, List<Pair<Integer,Integer>> enemyShips, List<Pair<Integer,Integer>> myShots){
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

    public void sinkThatBastard(int row, int column, List<ArrayList> shipsCells, GridPane battlefieldGrid, GridPane otherBattlefieldGrid, List<Pair<Integer, Integer>> shipsCellsPairs){
        List<ArrayList> numberedShips = new ArrayList<>();
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

        surroundGray(tempRow, tempColumn, tempPlacement, tempLength, battlefieldGrid, otherBattlefieldGrid, shipsCellsPairs);

    }

    public void surroundGray (int row, int column, int placement, int length, GridPane battlefieldGrid, GridPane otherBattlefieldGrid, List<Pair<Integer, Integer>> shipsCellsPairs){

        if(placement==1){
            for(int i=-1; i<2; i++){
                for(int j=-1; j<(length+1); j++){
                    Pair<Integer,Integer> tempCell = new Pair<>(row+i,column+j);
                    if(shipsCellsPairs.contains(tempCell)){
                        Pane blackPane = new Pane();
                        blackPane.setStyle("-fx-background-color: black; -fx-border-color: black;");
                        GridPane.setConstraints(blackPane, (column+j), (row+i));
                        battlefieldGrid.getChildren().add(blackPane);

                        blackPane = new Pane();
                        blackPane.setStyle("-fx-background-color: black; -fx-border-color: black;");
                        GridPane.setConstraints(blackPane, (column+j), (row+i));
                        otherBattlefieldGrid.getChildren().add(blackPane);
                    }
                    else if ((column+j)!=0){
                        if ((column+j)!=11) {
                            if ((row+i)!=0) {
                                if ((row+i)!=11) {
                                    Pane grayPane = new Pane();
                                    grayPane.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
                                    GridPane.setConstraints(grayPane, (column + j), (row + i));
                                    battlefieldGrid.getChildren().add(grayPane);

                                    grayPane = new Pane();
                                    grayPane.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
                                    GridPane.setConstraints(grayPane, (column + j), (row + i));
                                    otherBattlefieldGrid.getChildren().add(grayPane);
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

                        blackPane = new Pane();
                        blackPane.setStyle("-fx-background-color: black; -fx-border-color: black;");
                        GridPane.setConstraints(blackPane, (column + j), (row + i));
                        otherBattlefieldGrid.getChildren().add(blackPane);
                    } else if ((column + j) != 0) {
                        if ((column + j) != 11) {
                            if ((row + i) != 0) {
                                if ((row + i) != 11) {
                                    Pane grayPane = new Pane();
                                    grayPane.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
                                    GridPane.setConstraints(grayPane, (column + j), (row + i));
                                    battlefieldGrid.getChildren().add(grayPane);

                                    grayPane = new Pane();
                                    grayPane.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
                                    GridPane.setConstraints(grayPane, (column + j), (row + i));
                                    otherBattlefieldGrid.getChildren().add(grayPane);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void makeListOfShipsPoints(Player tempPlayer, List<ArrayList> shipsCells) {
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

    public void makeListOfShipsPairs(Player tempPlayer, List<Pair<Integer,Integer>> shipsCells) {
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

    public void setGame(Player player, Player secondPlayer, BorderPane ap) {

        Stage stage = (Stage) ap.getScene().getWindow();
        stage.setOnCloseRequest(event -> unsetGame(stage));

        this.leftPlayer = player;
        this.rightPlayer = secondPlayer;
        this.rightPlayer.setMyTurn(false);
        this.game = new Game(leftPlayer, rightPlayer);
        game.startGame();
        player1Label.setText(leftPlayer.getNick());
        player2Label.setText(rightPlayer.getNick());
        makeListOfShipsPoints(leftPlayer, shipsCellsLeft);
        makeListOfShipsPoints(rightPlayer, shipsCellsRight);
        makeListOfShipsPairs(leftPlayer, shipsCellsPairsLeft);
        makeListOfShipsPairs(rightPlayer, shipsCellsPairsRight);
        System.out.println(shipsCellsLeft);
        System.out.println(numberShips(shipsCellsLeft));
        System.out.println(shipsCellsRight);
        colorShips(shipsCellsLeft, battlefieldLeftGrid);
        colorShips(shipsCellsRight, battlefieldLeftGridSecond);
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startGameTime = new Date();

        System.out.println("Czas początku gry: " + dateFormat.format(startGameTime)); */
    }

    public void nextClicked(ActionEvent actionEvent) {
        if (leftPlayer.isMyTurn()) {
            battlefieldLeftGrid.setVisible(true);
            battlefieldRightGrid.setVisible(true);
            battlefieldRightGrid.setDisable(false);
            nextButton.setDisable(true);
            infoLabel.setVisible(false);
            player1Label.setVisible(true);
            player2Label.setVisible(true);
            player1Label.setText(leftPlayer.getNick());
            player2Label.setText(rightPlayer.getNick());
        } else if (rightPlayer.isMyTurn()) {
            battlefieldLeftGridSecond.setVisible(true);
            battlefieldRightGridSecond.setVisible(true);
            battlefieldRightGridSecond.setDisable(false);
            nextButton.setDisable(true);
            infoLabel.setVisible(false);
            player1Label.setVisible(true);
            player2Label.setVisible(true);
            player1Label.setText(rightPlayer.getNick());
            player2Label.setText(leftPlayer.getNick());
        }
    }

    public void bufferClicked(ActionEvent actionEvent) {
        battlefieldLeftGrid.setVisible(false);
        battlefieldRightGrid.setVisible(false);
        battlefieldRightGrid.setDisable(true);
        battlefieldLeftGridSecond.setVisible(false);
        battlefieldRightGridSecond.setVisible(false);
        battlefieldRightGridSecond.setDisable(true);
        player1Label.setVisible(false);
        player2Label.setVisible(false);
        infoLabel.setVisible(true);
        infoLabel.setText("Zamiana miejsc!");
        nextButton.setDisable(false);
        bufferButton.setDisable(true);
    }


    public void setPlayer(Player player) {
        this.player = player;
    }


    public static String printDifference(Date startDate, Date endDate) {

        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String result = elapsedMinutes + " min " + elapsedSeconds + " sec ";
        System.out.printf("Czas trwania gry wyniósł: " + result);
        return result;
    }

    public static void playMusicSplash() {
        System.out.println("SPLASH!!!!");
        String name = "src\\main\\resources\\com\\example\\statki\\splash.mp3";
        Media sound = new Media(Paths.get(name).toUri().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    public static void playMusicExplosion() {
        System.out.println("EXPLOSION!!!!");
        String name = "src\\main\\resources\\com\\example\\statki\\explosion.mp3";
        Media sound = new Media(Paths.get(name).toUri().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

}

