package com.example.statki;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class GamesReplayController {

    private Player player;

    public Button rightButton;
    public Button leftButton;

    public Label dateLabel1;
    public Label dateLabel2;
    public Label dateLabel3;
    public Label dateLabel4;
    public Label dateLabel5;

    public Label nickFirstPlayerLabel1;
    public Label nickFirstPlayerLabel2;
    public Label nickFirstPlayerLabel3;
    public Label nickFirstPlayerLabel4;
    public Label nickFirstPlayerLabel5;

    public Label nickSecondPlayerLabel1;
    public Label nickSecondPlayerLabel2;
    public Label nickSecondPlayerLabel3;
    public Label nickSecondPlayerLabel4;
    public Label nickSecondPlayerLabel5;

    public Label winnerLabel1;
    public Label winnerLabel2;
    public Label winnerLabel3;
    public Label winnerLabel4;
    public Label winnerLabel5;

    public Button replayButton1;
    public Button replayButton2;
    public Button replayButton3;
    public Button replayButton4;
    public Button replayButton5;

    public void setResultGames () throws SQLException {
        if (getResultGames()[0] != null) {
            dateLabel1.setText(String.valueOf(getResultGames()[0].time));
            nickFirstPlayerLabel1.setText(String.valueOf(getResultGames()[0].leftPlayerNick));
            nickSecondPlayerLabel1.setText(String.valueOf(getResultGames()[0].rightPlayerNick));
            winnerLabel1.setText(String.valueOf(getResultGames()[0].leftPlayerNick));
            replayButton1.setVisible(true);
        }

        if (getResultGames()[1] != null) {
            dateLabel2.setText(String.valueOf(getResultGames()[1].time));
            nickFirstPlayerLabel2.setText(String.valueOf(getResultGames()[1].leftPlayerNick));
            nickSecondPlayerLabel2.setText(String.valueOf(getResultGames()[1].rightPlayerNick));
            winnerLabel2.setText(String.valueOf(getResultGames()[1].leftPlayerNick));
            replayButton2.setVisible(true);
        }

       if (getResultGames()[2] != null) {
            dateLabel3.setText(getResultGames()[2].time);
            nickFirstPlayerLabel3.setText(String.valueOf(getResultGames()[2].leftPlayerNick));
            nickSecondPlayerLabel3.setText(String.valueOf(getResultGames()[2].rightPlayerNick));
            winnerLabel3.setText(String.valueOf(getResultGames()[2].leftPlayerNick));
            replayButton3.setVisible(true);
        }

        if (getResultGames()[3] != null) {
            dateLabel4.setText(getResultGames()[3].time);
            nickFirstPlayerLabel4.setText(String.valueOf(getResultGames()[3].leftPlayerNick));
            nickSecondPlayerLabel4.setText(String.valueOf(getResultGames()[3].rightPlayerNick));
            winnerLabel4.setText(String.valueOf(getResultGames()[3].leftPlayerNick));
            replayButton4.setVisible(true);
        }

        if (getResultGames()[4] != null) {
            dateLabel5.setText(getResultGames()[4].time);
            nickFirstPlayerLabel5.setText(String.valueOf(getResultGames()[4].leftPlayerNick));
            nickSecondPlayerLabel5.setText(String.valueOf(getResultGames()[4].rightPlayerNick));
            winnerLabel5.setText(String.valueOf(getResultGames()[4].leftPlayerNick));
            replayButton5.setVisible(true);
        }
    }

    public Game[] getResultGames() {

        Game[] resultsTab = new Game[5];
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        String SQLQuery = "SELECT idGame, idWhoWon, idWhoLost, gameTime, u1.login AS 'winnerLogin', u2.login AS 'loserLogin' " +
                "FROM games g, users u1, users u2 WHERE u1.idUser = idWhoWon AND u2.idUser = idWhoLost ORDER BY gameTime DESC LIMIT 5;";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(SQLQuery);
            for (int i = 0; i < 5; i++)
            {
                if (queryResult.next()) {
                    resultsTab[i] = new Game(
                            queryResult.getInt("idGame"),
                            queryResult.getString("winnerLogin"),
                            queryResult.getString("loserLogin"),
                            queryResult.getString("gameTime"));
                }
            }
            return  resultsTab;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
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

    public void setPlayer(Player player) {
        this.player = player;
        System.out.println(this.player);
        if (this.player != null) {
            leftButton.setText("Wyloguj się");
            rightButton.setText("Moje konto");
        }
    }

    public void clickedFirstGame(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("replay.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ReplayController replayGame = loader.getController();
        replayGame.setPlayer(player);
        replayGame.setIdGame(getResultGames()[0].idGame);
        System.out.println("1"+ getResultGames()[0].idGame);
        Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void clickedSecondGame(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("replay.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ReplayController replayGame = loader.getController();
        replayGame.setPlayer(player);
        replayGame.setIdGame(getResultGames()[1].idGame);
        System.out.println("2"+ getResultGames()[1].idGame);
        Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void clickedThirdGame(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("replay.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ReplayController replayGame = loader.getController();
        replayGame.setPlayer(player);
        replayGame.setIdGame(getResultGames()[2].idGame);
        Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void clickedFourthGame(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("replay.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ReplayController replayGame = loader.getController();
        replayGame.setPlayer(player);
        replayGame.setIdGame(getResultGames()[3].idGame);
        Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void clickedFifthGame(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("replay.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ReplayController replayGame = loader.getController();
        replayGame.setPlayer(player);
        replayGame.setIdGame(getResultGames()[4].idGame);
        Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
