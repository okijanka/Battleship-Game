package com.example.statki;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class ReplayController {

    private Player player;
    public Button leftButton;
    public Button rightButton;
    public Button nextButton;
    public int idGame;
    public GridPane battlefieldLeftGrid;
    public GridPane battlefieldRightGrid;
    public Label player1Label;
    public Label player2Label;
    public int idPlayer1;
    public int idPlayer2;
    public ArrayList<Integer> yColumn = new ArrayList<>();
    public ArrayList<Integer> xRow = new ArrayList<>();
    public ArrayList<Integer> idWhoMoved = new ArrayList<>();
    public ArrayList<Boolean> hit = new ArrayList<>();
    public ArrayList<Boolean> shipDestroyed = new ArrayList<>();
    public ArrayList<Pair<Integer,Integer>> hitNotDestroyedLeft = new ArrayList<>();
    public ArrayList<Pair<Integer,Integer>> hitNotDestroyedRight = new ArrayList<>();
    public ArrayList<Pair<Integer,Integer>> destroyedLeft = new ArrayList<>();
    public ArrayList<Pair<Integer,Integer>> destroyedRight = new ArrayList<>();
    public boolean end;

    public void initialize() {

        end = false;

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

    public void cancelLabelOnAction(MouseEvent mouseEvent) throws IOException, InterruptedException {
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

    public void setResultSetOfMoves() {
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection conn = connectNow.getConnection();
        try {
            ArrayList<Integer> yColumn = new ArrayList<>();
            ArrayList<Integer> xRow = new ArrayList<>();
            ArrayList<Integer> idWhoMoved = new ArrayList<>();
            ArrayList<Boolean> hit = new ArrayList<>();
            ArrayList<Boolean> shipDestroyed = new ArrayList<>();
            String sql = "SELECT xRow, yColumn, idWhoMoved, hit, shipDestroyed FROM moves WHERE idGame = " + idGame + "";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                xRow.add(rs.getInt("xRow"));
                yColumn.add(rs.getInt("yColumn"));
                idWhoMoved.add(rs.getInt("idWhoMoved"));
                hit.add(rs.getBoolean("hit"));
                shipDestroyed.add(rs.getBoolean("shipDestroyed"));
            }
            this.xRow = xRow;
            this.yColumn = yColumn;
            this.idWhoMoved = idWhoMoved;
            this.hit = hit;
            this.shipDestroyed = shipDestroyed;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getPlayersID(){
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        String StringQuery = "SELECT idWhoWon, idWhoLost FROM Games WHERE idGame = '" + idGame + "'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(StringQuery);
            if (queryResult.next()) {
                idPlayer1 = queryResult.getInt("idWhoWon");
                idPlayer2 = queryResult.getInt("idWhoLost");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String whosePlayerID(int id) {
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        String StringQuery = "SELECT login FROM Users WHERE idUser = '" + id + "'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(StringQuery);
            if (queryResult.next()) {
                return queryResult.getString("login");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void colorCells(int id, GridPane grid, ArrayList<Pair<Integer, Integer>> hitNotDestroyed) {
        Pane cellPlus = new Pane();
        cellPlus.setStyle("-fx-background-color: black; -fx-border-color: black;");
        GridPane.setConstraints(cellPlus, hitNotDestroyed.get(id).getValue(), hitNotDestroyed.get(id).getKey());
        grid.getChildren().add(cellPlus);
    }

    public boolean ifContainsPair(int x, int y, ArrayList<Pair<Integer, Integer>> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getKey() == x && list.get(i).getValue() == y) {
                return true;
            }
        }
        return false;
    }

    public void greyCells(int id, GridPane grid, ArrayList<Pair<Integer, Integer>> hitNotDestroyed, ArrayList<Pair<Integer, Integer>> destroyed) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (!(ifContainsPair(hitNotDestroyed.get(id).getKey()+i, hitNotDestroyed.get(id).getValue() + j, hitNotDestroyed) || ifContainsPair(hitNotDestroyed.get(id).getKey()+i, hitNotDestroyed.get(id).getValue() + j, destroyed))) {
                    if (hitNotDestroyed.get(id).getValue()+j != 0 && hitNotDestroyed.get(id).getValue()+j != 11 && hitNotDestroyed.get(id).getKey()+i != 0 && hitNotDestroyed.get(id).getKey()+i != 11) {
                        Pane cell = new Pane();
                        cell.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
                        GridPane.setConstraints(cell, hitNotDestroyed.get(id).getValue()+j, hitNotDestroyed.get(id).getKey()+i);
                        grid.getChildren().add(cell);
                    }
                }
            }
        }
    }

    public void nextButtonClicked(ActionEvent actionEvent) throws IOException {
        if (end) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("start.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            StartController startController = loader.getController();
            startController.setPlayer(player);
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
        if (idWhoMoved.size() != 0) {
            Pane cell = new Pane();
            if (idWhoMoved.get(0) == idPlayer1) {
                if (hit.get(0)) {
                    if (shipDestroyed.get(0)) {
                        cell.setStyle("-fx-background-color: black; -fx-border-color: black;");
                        int indexOfRow = xRow.get(0);
                        int indexOfColumn = yColumn.get(0);
                        destroyedLeft.add(new Pair<>(indexOfRow, indexOfColumn));
                        for (int i = -1; i < 2; i++) {
                            for (int j = -1; j < 2; j++) {
                                if (!(ifContainsPair(indexOfRow+i, indexOfColumn + j, hitNotDestroyedLeft) || ifContainsPair(indexOfRow+i, indexOfColumn + j, destroyedLeft))) {
                                    if (indexOfColumn + j != 0 && indexOfColumn + j != 11 && indexOfRow+i != 0 && indexOfRow+i != 11) {
                                        Pane cellPlus = new Pane();
                                        cellPlus.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
                                        GridPane.setConstraints(cellPlus, indexOfColumn+j, indexOfRow+i);
                                        battlefieldLeftGrid.getChildren().add(cellPlus);
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < hitNotDestroyedLeft.size(); i++) {
                            if (hitNotDestroyedLeft.get(i).getKey() == indexOfRow && hitNotDestroyedLeft.get(i).getValue() == indexOfColumn - 1) {
                                colorCells(i, battlefieldLeftGrid, hitNotDestroyedLeft);
                                destroyedLeft.add(new Pair<>(hitNotDestroyedLeft.get(i).getKey(), hitNotDestroyedLeft.get(i).getValue()));
                                greyCells(i, battlefieldLeftGrid, hitNotDestroyedLeft, destroyedLeft);
                                hitNotDestroyedLeft.remove(i);
                                for (int m = 0; m < hitNotDestroyedLeft.size(); m++) {
                                    if (hitNotDestroyedLeft.get(m).getKey() == indexOfRow && hitNotDestroyedLeft.get(m).getValue() == indexOfColumn - 2) {
                                        colorCells(m, battlefieldLeftGrid, hitNotDestroyedLeft);
                                        destroyedLeft.add(new Pair<>(hitNotDestroyedLeft.get(m).getKey(), hitNotDestroyedLeft.get(m).getValue()));
                                        greyCells(m, battlefieldLeftGrid, hitNotDestroyedLeft, destroyedLeft);
                                        hitNotDestroyedLeft.remove(m);
                                        for (int n = 0; n < hitNotDestroyedLeft.size(); n++) {
                                            if (hitNotDestroyedLeft.get(n).getKey() == indexOfRow && hitNotDestroyedLeft.get(n).getValue() == indexOfColumn - 3) {
                                                colorCells(n, battlefieldLeftGrid, hitNotDestroyedLeft);
                                                destroyedLeft.add(new Pair<>(hitNotDestroyedLeft.get(n).getKey(), hitNotDestroyedLeft.get(n).getValue()));
                                                greyCells(n, battlefieldLeftGrid, hitNotDestroyedLeft, destroyedLeft);
                                                hitNotDestroyedLeft.remove(n);
                                            }
                                        }
                                    }
                                }
                            } else if (hitNotDestroyedLeft.get(i).getKey() == indexOfRow && hitNotDestroyedLeft.get(i).getValue() == indexOfColumn + 1) {
                                colorCells(i, battlefieldLeftGrid, hitNotDestroyedLeft);
                                destroyedLeft.add(new Pair<>(hitNotDestroyedLeft.get(i).getKey(), hitNotDestroyedLeft.get(i).getValue()));
                                greyCells(i, battlefieldLeftGrid, hitNotDestroyedLeft, destroyedLeft);
                                hitNotDestroyedLeft.remove(i);
                                for (int m = 0; m < hitNotDestroyedLeft.size(); m++) {
                                    if (hitNotDestroyedLeft.get(m).getKey() == indexOfRow && hitNotDestroyedLeft.get(m).getValue() == indexOfColumn + 2) {
                                        colorCells(m, battlefieldLeftGrid, hitNotDestroyedLeft);
                                        destroyedLeft.add(new Pair<>(hitNotDestroyedLeft.get(m).getKey(), hitNotDestroyedLeft.get(m).getValue()));
                                        greyCells(m, battlefieldLeftGrid, hitNotDestroyedLeft, destroyedLeft);
                                        hitNotDestroyedLeft.remove(m);
                                        for (int n = 0; n < hitNotDestroyedLeft.size(); n++) {
                                            if (hitNotDestroyedLeft.get(n).getKey() == indexOfRow && hitNotDestroyedLeft.get(n).getValue() == indexOfColumn + 3) {
                                                colorCells(n, battlefieldLeftGrid, hitNotDestroyedLeft);
                                                destroyedLeft.add(new Pair<>(hitNotDestroyedLeft.get(n).getKey(), hitNotDestroyedLeft.get(n).getValue()));
                                                greyCells(n, battlefieldLeftGrid, hitNotDestroyedLeft, destroyedLeft);
                                                hitNotDestroyedLeft.remove(n);
                                            }
                                        }
                                    }
                                }
                            } else if (hitNotDestroyedLeft.get(i).getKey() == indexOfRow - 1 && hitNotDestroyedLeft.get(i).getValue() == indexOfColumn) {
                                colorCells(i, battlefieldLeftGrid, hitNotDestroyedLeft);
                                destroyedLeft.add(new Pair<>(hitNotDestroyedLeft.get(i).getKey(), hitNotDestroyedLeft.get(i).getValue()));
                                greyCells(i, battlefieldLeftGrid, hitNotDestroyedLeft, destroyedLeft);
                                hitNotDestroyedLeft.remove(i);
                                for (int m = 0; m < hitNotDestroyedLeft.size(); m++) {
                                    if (hitNotDestroyedLeft.get(m).getKey() == indexOfRow - 2 && hitNotDestroyedLeft.get(m).getValue() == indexOfColumn) {
                                        colorCells(m, battlefieldLeftGrid, hitNotDestroyedLeft);
                                        destroyedLeft.add(new Pair<>(hitNotDestroyedLeft.get(m).getKey(), hitNotDestroyedLeft.get(m).getValue()));
                                        greyCells(m, battlefieldLeftGrid, hitNotDestroyedLeft, destroyedLeft);
                                        hitNotDestroyedLeft.remove(m);
                                        for (int n = 0; n < hitNotDestroyedLeft.size(); n++) {
                                            if (hitNotDestroyedLeft.get(n).getKey() == indexOfRow - 3 && hitNotDestroyedLeft.get(n).getValue() == indexOfColumn) {
                                                colorCells(n, battlefieldLeftGrid, hitNotDestroyedLeft);
                                                destroyedLeft.add(new Pair<>(hitNotDestroyedLeft.get(n).getKey(), hitNotDestroyedLeft.get(n).getValue()));
                                                greyCells(n, battlefieldLeftGrid, hitNotDestroyedLeft, destroyedLeft);
                                                hitNotDestroyedLeft.remove(n);
                                            }
                                        }
                                    }
                                }
                            } else if (hitNotDestroyedLeft.get(i).getKey() == indexOfRow + 1 && hitNotDestroyedLeft.get(i).getValue() == indexOfColumn) {
                                colorCells(i, battlefieldLeftGrid, hitNotDestroyedLeft);
                                destroyedLeft.add(new Pair<>(hitNotDestroyedLeft.get(i).getKey(), hitNotDestroyedLeft.get(i).getValue()));
                                greyCells(i, battlefieldLeftGrid, hitNotDestroyedLeft, destroyedLeft);
                                hitNotDestroyedLeft.remove(i);
                                for (int m = 0; m < hitNotDestroyedLeft.size(); m++) {
                                    if (hitNotDestroyedLeft.get(m).getKey() == indexOfRow + 2 && hitNotDestroyedLeft.get(m).getValue() == indexOfColumn) {
                                        colorCells(m, battlefieldLeftGrid, hitNotDestroyedLeft);
                                        destroyedLeft.add(new Pair<>(hitNotDestroyedLeft.get(m).getKey(), hitNotDestroyedLeft.get(m).getValue()));
                                        greyCells(m, battlefieldLeftGrid, hitNotDestroyedLeft, destroyedLeft);
                                        hitNotDestroyedLeft.remove(m);
                                        for (int n = 0; n < hitNotDestroyedLeft.size(); n++) {
                                            if (hitNotDestroyedLeft.get(n).getKey() == indexOfRow + 3 && hitNotDestroyedLeft.get(n).getValue() == indexOfColumn) {
                                                colorCells(n, battlefieldLeftGrid, hitNotDestroyedLeft);
                                                destroyedLeft.add(new Pair<>(hitNotDestroyedLeft.get(n).getKey(), hitNotDestroyedLeft.get(n).getValue()));
                                                greyCells(n, battlefieldLeftGrid, hitNotDestroyedLeft, destroyedLeft);
                                                hitNotDestroyedLeft.remove(n);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        hitNotDestroyedLeft.add(new Pair<>(xRow.get(0), yColumn.get(0)));
                        cell.setStyle("-fx-background-color: #ff1f35; -fx-border-color: black;");
                    }
                } else {
                    cell.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
                }
                GridPane.setConstraints(cell, yColumn.get(0), xRow.get(0));
                battlefieldLeftGrid.getChildren().add(cell);
            } else {
                if (hit.get(0)) {
                    if (shipDestroyed.get(0)) {
                        cell.setStyle("-fx-background-color: black; -fx-border-color: black;");
                        int indexOfRow = xRow.get(0);
                        int indexOfColumn = yColumn.get(0);
                        destroyedRight.add(new Pair<>(indexOfRow, indexOfColumn));
                        for (int i = -1; i < 2; i++) {
                            for (int j = -1; j < 2; j++) {
                                if (!(ifContainsPair(indexOfRow+i, indexOfColumn + j, hitNotDestroyedRight) || ifContainsPair(indexOfRow+i, indexOfColumn + j, destroyedRight))) {
                                    if (indexOfColumn + j != 0 && indexOfColumn + j != 11 && indexOfRow+i != 0 && indexOfRow+i != 11) {
                                        Pane cellPlus = new Pane();
                                        cellPlus.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
                                        GridPane.setConstraints(cellPlus, indexOfColumn+j, indexOfRow+i);
                                        battlefieldRightGrid.getChildren().add(cellPlus);
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < hitNotDestroyedRight.size(); i++) {
                            if (hitNotDestroyedRight.get(i).getKey() == indexOfRow && hitNotDestroyedRight.get(i).getValue() == indexOfColumn - 1) {
                                colorCells(i, battlefieldRightGrid, hitNotDestroyedRight);
                                destroyedRight.add(new Pair<>(hitNotDestroyedRight.get(i).getKey(), hitNotDestroyedRight.get(i).getValue()));
                                greyCells(i, battlefieldRightGrid, hitNotDestroyedRight, destroyedRight);
                                hitNotDestroyedRight.remove(i);
                                for (int m = 0; m < hitNotDestroyedRight.size(); m++) {
                                    if (hitNotDestroyedRight.get(m).getKey() == indexOfRow && hitNotDestroyedRight.get(m).getValue() == indexOfColumn - 2) {
                                        colorCells(m, battlefieldRightGrid, hitNotDestroyedRight);
                                        destroyedRight.add(new Pair<>(hitNotDestroyedRight.get(m).getKey(), hitNotDestroyedRight.get(m).getValue()));
                                        greyCells(m, battlefieldRightGrid, hitNotDestroyedRight, destroyedRight);
                                        hitNotDestroyedRight.remove(m);
                                        for (int n = 0; n < hitNotDestroyedRight.size(); n++) {
                                            if (hitNotDestroyedRight.get(n).getKey() == indexOfRow && hitNotDestroyedRight.get(n).getValue() == indexOfColumn - 3) {
                                                colorCells(n, battlefieldRightGrid, hitNotDestroyedRight);
                                                destroyedRight.add(new Pair<>(hitNotDestroyedRight.get(n).getKey(), hitNotDestroyedRight.get(n).getValue()));
                                                greyCells(n, battlefieldRightGrid, hitNotDestroyedRight, destroyedRight);
                                                hitNotDestroyedRight.remove(n);
                                            }
                                        }
                                    }
                                }
                            } else if (hitNotDestroyedRight.get(i).getKey() == indexOfRow && hitNotDestroyedRight.get(i).getValue() == indexOfColumn + 1) {
                                colorCells(i, battlefieldRightGrid, hitNotDestroyedRight);
                                destroyedRight.add(new Pair<>(hitNotDestroyedRight.get(i).getKey(), hitNotDestroyedRight.get(i).getValue()));
                                greyCells(i, battlefieldRightGrid, hitNotDestroyedRight, destroyedRight);
                                hitNotDestroyedRight.remove(i);
                                for (int m = 0; m < hitNotDestroyedRight.size(); m++) {
                                    if (hitNotDestroyedRight.get(m).getKey() == indexOfRow && hitNotDestroyedRight.get(m).getValue() == indexOfColumn + 2) {
                                        colorCells(m, battlefieldRightGrid, hitNotDestroyedRight);
                                        destroyedRight.add(new Pair<>(hitNotDestroyedRight.get(m).getKey(), hitNotDestroyedRight.get(m).getValue()));
                                        greyCells(m, battlefieldRightGrid, hitNotDestroyedRight, destroyedRight);
                                        hitNotDestroyedRight.remove(m);
                                        for (int n = 0; n < hitNotDestroyedRight.size(); n++) {
                                            if (hitNotDestroyedRight.get(n).getKey() == indexOfRow && hitNotDestroyedRight.get(n).getValue() == indexOfColumn + 3) {
                                                colorCells(n, battlefieldRightGrid, hitNotDestroyedRight);
                                                destroyedRight.add(new Pair<>(hitNotDestroyedRight.get(n).getKey(), hitNotDestroyedRight.get(n).getValue()));
                                                greyCells(n, battlefieldRightGrid, hitNotDestroyedRight, destroyedRight);
                                                hitNotDestroyedRight.remove(n);
                                            }
                                        }
                                    }
                                }
                            } else if (hitNotDestroyedRight.get(i).getKey() == indexOfRow - 1 && hitNotDestroyedRight.get(i).getValue() == indexOfColumn) {
                                colorCells(i, battlefieldRightGrid, hitNotDestroyedRight);
                                destroyedRight.add(new Pair<>(hitNotDestroyedRight.get(i).getKey(), hitNotDestroyedRight.get(i).getValue()));
                                greyCells(i, battlefieldRightGrid, hitNotDestroyedRight, destroyedRight);
                                hitNotDestroyedRight.remove(i);
                                for (int m = 0; m < hitNotDestroyedRight.size(); m++) {
                                    if (hitNotDestroyedRight.get(m).getKey() == indexOfRow - 2 && hitNotDestroyedRight.get(m).getValue() == indexOfColumn) {
                                        colorCells(m, battlefieldRightGrid, hitNotDestroyedRight);
                                        destroyedRight.add(new Pair<>(hitNotDestroyedRight.get(m).getKey(), hitNotDestroyedRight.get(m).getValue()));
                                        greyCells(m, battlefieldRightGrid, hitNotDestroyedRight, destroyedRight);
                                        hitNotDestroyedRight.remove(m);
                                        for (int n = 0; n < hitNotDestroyedRight.size(); n++) {
                                            if (hitNotDestroyedRight.get(n).getKey() == indexOfRow - 3 && hitNotDestroyedRight.get(n).getValue() == indexOfColumn) {
                                                colorCells(n, battlefieldRightGrid, hitNotDestroyedRight);
                                                destroyedRight.add(new Pair<>(hitNotDestroyedRight.get(n).getKey(), hitNotDestroyedRight.get(n).getValue()));
                                                greyCells(n, battlefieldRightGrid, hitNotDestroyedRight, destroyedRight);
                                                hitNotDestroyedRight.remove(n);
                                            }
                                        }
                                    }
                                }
                            } else if (hitNotDestroyedRight.get(i).getKey() == indexOfRow + 1 && hitNotDestroyedRight.get(i).getValue() == indexOfColumn) {
                                colorCells(i, battlefieldRightGrid, hitNotDestroyedRight);
                                destroyedRight.add(new Pair<>(hitNotDestroyedRight.get(i).getKey(), hitNotDestroyedRight.get(i).getValue()));
                                greyCells(i, battlefieldRightGrid, hitNotDestroyedRight, destroyedRight);
                                hitNotDestroyedRight.remove(i);
                                for (int m = 0; m < hitNotDestroyedRight.size(); m++) {
                                    if (hitNotDestroyedRight.get(m).getKey() == indexOfRow + 2 && hitNotDestroyedRight.get(m).getValue() == indexOfColumn) {
                                        colorCells(m, battlefieldRightGrid, hitNotDestroyedRight);
                                        destroyedRight.add(new Pair<>(hitNotDestroyedRight.get(m).getKey(), hitNotDestroyedRight.get(m).getValue()));
                                        greyCells(m, battlefieldRightGrid, hitNotDestroyedRight, destroyedRight);
                                        hitNotDestroyedRight.remove(m);
                                        for (int n = 0; n < hitNotDestroyedRight.size(); n++) {
                                            if (hitNotDestroyedRight.get(n).getKey() == indexOfRow + 3 && hitNotDestroyedRight.get(n).getValue() == indexOfColumn) {
                                                colorCells(n, battlefieldRightGrid, hitNotDestroyedRight);
                                                destroyedRight.add(new Pair<>(hitNotDestroyedRight.get(n).getKey(), hitNotDestroyedRight.get(n).getValue()));
                                                greyCells(n, battlefieldRightGrid, hitNotDestroyedRight, destroyedRight);
                                                hitNotDestroyedRight.remove(n);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        hitNotDestroyedRight.add(new Pair<>(xRow.get(0), yColumn.get(0)));
                        cell.setStyle("-fx-background-color: #ff1f35; -fx-border-color: black;");
                    }
                } else {
                    cell.setStyle("-fx-background-color: #b8b8b8; -fx-border-color: black;");
                }
                GridPane.setConstraints(cell, yColumn.get(0), xRow.get(0));
                battlefieldRightGrid.getChildren().add(cell);
            }
            yColumn.remove(0);
            xRow.remove(0);
            idWhoMoved.remove(0);
            hit.remove(0);
            shipDestroyed.remove(0);
        } else {
            nextButton.setText("koniec");
            end = true;
        }
    }

    public void setPlayer (Player player) {
        this.player = player;
        if (this.player != null) {
            leftButton.setText("Wyloguj się");
            rightButton.setText("Moje konto");
        }
    }

    public void setIdGame (int idGame) {
        this.idGame = idGame;
        setResultSetOfMoves();
        getPlayersID();
        player2Label.setText(whosePlayerID(idPlayer1));
        player1Label.setText(whosePlayerID(idPlayer2));
    }
}
