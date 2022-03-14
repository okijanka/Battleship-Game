package com.example.statki;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;
import java.io.IOException;

public class SetShipsController {

    public GridPane boardGrid;
    public RadioButton verticalRadio;
    public RadioButton horizontalRadio;
    public Label tempShipLabel;
    public Button nextButton;
    public Button leftButton;
    public Button rightButton;
    public Label nickNameLabel;
    public BorderPane mainPane;


    //Mamy 4 sytuacje: ustawiamy czteromasztowiec - 4, trójm - 3, dwum - 2 albo jednom - 1
    public int tempShipToSet = 4;
    //Mamy dwie opcje ustawienia statku: wertykalnie - 1, horyzontalnie - 2
    public int tempAlignment;
    public int countThree = 0; // licznik trójmasztowców
    public int countTwo = 0; // licznik dwumasztowców
    public int countOne = 0; // licznik jednomasztowców
    public static Battlefield battlefieldUser = new Battlefield();
    public List<Ship> listOfShips = new ArrayList<>();
    public Player player;
    public AIPlayer bot;
    public List<Pair<Integer, Integer>> occupiedCells = new ArrayList<>();

    public boolean humanHumanGame;

    public void initGameMode(boolean gameMode){
        humanHumanGame = gameMode;
    }

    public void initialize(){

        battlefieldUser = new Battlefield();
        listOfShips = new ArrayList<>();

        nextButton.setDisable(true);

        for(int j=1; j<11; j++) {
            for(int i=1; i<11; i++) {
                Pane cell = new Pane();
                cell.setStyle("-fx-border-color: black;");
                GridPane.setConstraints(cell, j, i);
                boardGrid.getChildren().add(cell);

                int finalI = i;
                int finalJ = j;
                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(verticalRadio.isSelected() || horizontalRadio.isSelected()) {
                            switch (tempShipToSet) {
                                case 4:
                                    Pane nextCell41 = new Pane();
                                    nextCell41.setStyle("-fx-background-color: #7e63eb; -fx-border-color: black;");
                                    Pane nextCell42 = new Pane();
                                    nextCell42.setStyle("-fx-background-color: #7e63eb; -fx-border-color: black;");
                                    Pane nextCell43 = new Pane();
                                    nextCell43.setStyle("-fx-background-color: #7e63eb; -fx-border-color: black;");

                                    if (verticalRadio.isSelected()) {
                                        tempAlignment = 1;
                                    }
                                    if (horizontalRadio.isSelected()) {
                                        tempAlignment = 2;
                                    }
                                    if (tempAlignment == 1 && finalI <= 7) {
                                        if (possibleToSet(finalI, finalI, 4, true)) {
                                            cell.setStyle("-fx-background-color: #7e63eb; -fx-border-color: black;");
                                            GridPane.setConstraints(nextCell41, finalJ, finalI + 1);
                                            GridPane.setConstraints(nextCell42, finalJ, finalI + 2);
                                            GridPane.setConstraints(nextCell43, finalJ, finalI + 3);
                                            boardGrid.getChildren().addAll(nextCell41, nextCell42, nextCell43);
                                            Ship ship4 = new Ship(battlefieldUser.getMap().get(finalI - 1).get(finalJ - 1), Ship.PlacementOfShips.VERTICAL, 4, battlefieldUser);
                                            listOfShips.add(ship4);
                                            takenPoints(ship4);
                                            tempShipToSet = 3;
                                        }
                                    }
                                    if (tempAlignment == 2 && finalJ <= 7) {
                                        if (possibleToSet(finalI, finalJ, 4, false)) {
                                            cell.setStyle("-fx-background-color: #7e63eb; -fx-border-color: black;");
                                            GridPane.setConstraints(nextCell41, finalJ + 1, finalI);
                                            GridPane.setConstraints(nextCell42, finalJ + 2, finalI);
                                            GridPane.setConstraints(nextCell43, finalJ + 3, finalI);
                                            boardGrid.getChildren().addAll(nextCell41, nextCell42, nextCell43);
                                            Ship ship4 = new Ship(battlefieldUser.getMap().get(finalI - 1).get(finalJ - 1), Ship.PlacementOfShips.HORIZONTAL, 4, battlefieldUser);
                                            listOfShips.add(ship4);
                                            takenPoints(ship4);
                                            tempShipToSet = 3;
                                        }
                                    }
                                    break;
                                case 3:
                                    Pane nextCell31 = new Pane();
                                    nextCell31.setStyle("-fx-background-color: #7e63eb; -fx-border-color: black;");
                                    Pane nextCell32 = new Pane();
                                    nextCell32.setStyle("-fx-background-color: #7e63eb; -fx-border-color: black;");

                                    if (verticalRadio.isSelected()) {
                                        tempAlignment = 1;
                                    }
                                    if (horizontalRadio.isSelected()) {
                                        tempAlignment = 2;
                                    }
                                    if (tempAlignment == 1 && finalI <= 8) {
                                        if (possibleToSet(finalI,finalJ, 3, true)) {
                                            cell.setStyle("-fx-background-color: #7e63eb; -fx-border-color: black;");
                                            GridPane.setConstraints(nextCell31, finalJ, finalI + 1);
                                            GridPane.setConstraints(nextCell32, finalJ, finalI + 2);
                                            boardGrid.getChildren().addAll(nextCell31, nextCell32);
                                            Ship ship3 = new Ship(battlefieldUser.getMap().get(finalI - 1).get(finalJ - 1), Ship.PlacementOfShips.VERTICAL, 3, battlefieldUser);
                                            listOfShips.add(ship3);
                                            takenPoints(ship3);
                                            countThree++;
                                            if (countThree == 1) {
                                                tempShipToSet = 3;
                                            }
                                            if (countThree == 2) {
                                                tempShipToSet = 2;
                                            }
                                        }
                                    }
                                    if (tempAlignment == 2 && finalJ <= 8) {
                                        if (possibleToSet(finalI, finalJ, 3, false)) {
                                            cell.setStyle("-fx-background-color: #7e63eb; -fx-border-color: black;");
                                            GridPane.setConstraints(nextCell31, finalJ + 1, finalI);
                                            GridPane.setConstraints(nextCell32, finalJ + 2, finalI);
                                            boardGrid.getChildren().addAll(nextCell31, nextCell32);
                                            Ship ship3 = new Ship(battlefieldUser.getMap().get(finalI - 1).get(finalJ - 1), Ship.PlacementOfShips.HORIZONTAL, 3, battlefieldUser);
                                            listOfShips.add(ship3);
                                            takenPoints(ship3);
                                            countThree++;
                                            if (countThree == 1) {
                                                tempShipToSet = 3;
                                            }
                                            if (countThree == 2) {
                                                tempShipToSet = 2;
                                            }
                                        }
                                    }
                                    break;
                                case 2:
                                    Pane nextCell21 = new Pane();
                                    nextCell21.setStyle("-fx-background-color: #7e63eb; -fx-border-color: black;");

                                    if (verticalRadio.isSelected()) {
                                        tempAlignment = 1;
                                    }
                                    if (horizontalRadio.isSelected()) {
                                        tempAlignment = 2;
                                    }
                                    if (tempAlignment == 1 && finalI <= 9) {
                                        if (possibleToSet(finalI, finalJ, 2, true)) {
                                            cell.setStyle("-fx-background-color: #7e63eb; -fx-border-color: black;");
                                            GridPane.setConstraints(nextCell21, finalJ, finalI + 1);
                                            boardGrid.getChildren().addAll(nextCell21);
                                            Ship ship2 = new Ship(battlefieldUser.getMap().get(finalI - 1).get(finalJ - 1), Ship.PlacementOfShips.VERTICAL, 2, battlefieldUser);
                                            listOfShips.add(ship2);
                                            takenPoints(ship2);
                                            countTwo++;
                                            if (countTwo == 1 || countTwo == 2) {
                                                tempShipToSet = 2;
                                            }
                                            if (countTwo == 3) {
                                                tempShipToSet = 1;
                                            }
                                        }
                                    }
                                    if (tempAlignment == 2 && finalJ <= 9) {
                                        if (possibleToSet(finalI, finalJ, 2, false)) {
                                            cell.setStyle("-fx-background-color: #7e63eb; -fx-border-color: black;");
                                            GridPane.setConstraints(nextCell21, finalJ + 1, finalI);
                                            boardGrid.getChildren().addAll(nextCell21);
                                            Ship ship2 = new Ship(battlefieldUser.getMap().get(finalI - 1).get(finalJ - 1), Ship.PlacementOfShips.HORIZONTAL, 2, battlefieldUser);
                                            listOfShips.add(ship2);
                                            takenPoints(ship2);
                                            countTwo++;
                                            if (countTwo == 1 || countTwo == 2) {
                                                tempShipToSet = 2;
                                            }
                                            if (countTwo == 3) {
                                                tempShipToSet = 1;
                                            }
                                        }
                                    }
                                    break;
                                case 1:
                                    if (possibleToSet(finalI, finalJ, 1, false)) {
                                        cell.setStyle("-fx-background-color: #7e63eb; -fx-border-color: black;");
                                        Ship ship1 = new Ship(battlefieldUser.getMap().get(finalI - 1).get(finalJ - 1), Ship.PlacementOfShips.NONE, 1, battlefieldUser);
                                        listOfShips.add(ship1);
                                        takenPoints(ship1);
                                        countOne++;
                                        if (countOne == 1 || countOne == 2 || countOne == 3) {
                                            tempShipToSet = 1;
                                        }
                                        if (countOne == 4) {
                                            tempShipToSet = 0;
                                            nextButton.setDisable(false);
                                        }
                                    }
                                    break;
                                case 0:
                            }
                        }
                    }
                });
            }
        }
    }
    public void cancelLabelOnAction(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("start.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        StartController startController = loader.getController();
        startController.setPlayer(player);
        Stage stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void rightClicked(ActionEvent actionEvent) throws IOException {
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

    public void confirmShip(ActionEvent actionEvent) {
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
    }

    //row - wiersz pierwszego punktu,
    //column - kolumna pierwszego punktu,
    //length = długość statku,
    //vert - true dla wertykalnie, false dla horyzontalnie, lub none
    public boolean possibleToSet(int row, int column, int length, boolean vert){
        if(vert){
            for(int i=row; i<row+length; i++){
                Pair temp = new Pair(i, column);
                if(occupiedCells.contains(temp)){
                    return false;
                }
            }
        }
        if(!vert){
            for(int j=column; j<column+length; j++){
                Pair temp = new Pair(row, j);
                if(occupiedCells.contains(temp)){
                    return false;
                }
            }
        }
        return true;
    }

    public void nextClicked(ActionEvent actionEvent) throws IOException {
        if (humanHumanGame) {
            battlefieldUser.setShips(listOfShips);
            player.setBattlefield(battlefieldUser);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("logOtherPlayer.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            LogOtherPlayerController logOtherPlayerController = loader.getController();
            logOtherPlayerController.setPlayer(player);

            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else {
            battlefieldUser.setShips(listOfShips);
            player.setBattlefield(battlefieldUser);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gamePlayerAI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            GamePlayerAIController gamePlayerAIController = loader.getController();
            gamePlayerAIController.setPlayerAndBot(player, bot, mainPane);

            Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }

    }

    public void setPlayer(Player player) {
        this.player = player;
        nickNameLabel.setText(this.player.getNick());
        System.out.println(this.player);
    }

    public void setPlayerAndBot(Player player, AIPlayer bot) {
        this.player = player;
        nickNameLabel.setText(this.player.getNick());
        this.bot = bot;
    }

}