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

public class TopTenController {
    public Button rightButton;
    public Button leftButton;
    public Player player;

    public Label nrLabel1;
    public Label nrLabel2;
    public Label nrLabel3;
    public Label nrLabel4;
    public Label nrLabel5;
    public Label nrLabel6;
    public Label nrLabel7;
    public Label nrLabel8;
    public Label nrLabel9;
    public Label nrLabel10;

    public Label nickLabel1;
    public Label nickLabel2;
    public Label nickLabel3;
    public Label nickLabel4;
    public Label nickLabel5;
    public Label nickLabel6;
    public Label nickLabel7;
    public Label nickLabel8;
    public Label nickLabel9;
    public Label nickLabel10;

    public Label gamesLabel1;
    public Label gamesLabel2;
    public Label gamesLabel3;
    public Label gamesLabel4;
    public Label gamesLabel5;
    public Label gamesLabel6;
    public Label gamesLabel7;
    public Label gamesLabel8;
    public Label gamesLabel9;
    public Label gamesLabel10;

    public Label wonLabel1;
    public Label wonLabel2;
    public Label wonLabel3;
    public Label wonLabel4;
    public Label wonLabel5;
    public Label wonLabel6;
    public Label wonLabel7;
    public Label wonLabel8;
    public Label wonLabel9;
    public Label wonLabel10;

    public Label lostLabel1;
    public Label lostLabel2;
    public Label lostLabel3;
    public Label lostLabel4;
    public Label lostLabel5;
    public Label lostLabel6;
    public Label lostLabel7;
    public Label lostLabel8;
    public Label lostLabel9;
    public Label lostLabel10;

    public Label accuracyLabel1;
    public Label accuracyLabel2;
    public Label accuracyLabel3;
    public Label accuracyLabel4;
    public Label accuracyLabel5;
    public Label accuracyLabel6;
    public Label accuracyLabel7;
    public Label accuracyLabel8;
    public Label accuracyLabel9;
    public Label accuracyLabel10;

    public void setStatistics () {
        if(getStatistics()[0] != null) {
            nrLabel1.setText("1");
            nickLabel1.setText(getStatistics()[0].getLogin());
            gamesLabel1.setText(getStatistics()[0].getNumberOfGames());
            wonLabel1.setText(getStatistics()[0].getWonGames());
            lostLabel1.setText(getStatistics()[0].getLostGames());
            accuracyLabel1.setText(getStatistics()[0].getAccuracy());
        }

        if(getStatistics()[1] != null) {
            nrLabel2.setText("2");
            nickLabel2.setText(getStatistics()[1].getLogin());
            gamesLabel2.setText(getStatistics()[1].getNumberOfGames());
            wonLabel2.setText(getStatistics()[1].getWonGames());
            lostLabel2.setText(getStatistics()[1].getLostGames());
            accuracyLabel2.setText(getStatistics()[1].getAccuracy());
        }

        if(getStatistics()[2] != null) {
            nrLabel3.setText("3");
            nickLabel3.setText(getStatistics()[2].getLogin());
            gamesLabel3.setText(getStatistics()[2].getNumberOfGames());
            wonLabel3.setText(getStatistics()[2].getWonGames());
            lostLabel3.setText(getStatistics()[2].getLostGames());
            accuracyLabel3.setText(getStatistics()[2].getAccuracy());
        }

        if(getStatistics()[3] != null) {
            nrLabel4.setText("4");
            nickLabel4.setText(getStatistics()[3].getLogin());
            gamesLabel4.setText(getStatistics()[3].getNumberOfGames());
            wonLabel4.setText(getStatistics()[3].getWonGames());
            lostLabel4.setText(getStatistics()[3].getLostGames());
            accuracyLabel4.setText(getStatistics()[3].getAccuracy());
        }

        if(getStatistics()[4] != null) {
            nrLabel5.setText("5");
            nickLabel5.setText(getStatistics()[4].getLogin());
            gamesLabel5.setText(getStatistics()[4].getNumberOfGames());
            wonLabel5.setText(getStatistics()[4].getWonGames());
            lostLabel5.setText(getStatistics()[4].getLostGames());
            accuracyLabel5.setText(getStatistics()[4].getAccuracy());
        }

        if(getStatistics()[5] != null) {
            nrLabel6.setText("6");
            nickLabel6.setText(getStatistics()[5].getLogin());
            gamesLabel6.setText(getStatistics()[5].getNumberOfGames());
            wonLabel6.setText(getStatistics()[5].getWonGames());
            lostLabel6.setText(getStatistics()[5].getLostGames());
            accuracyLabel6.setText(getStatistics()[5].getAccuracy());
        }

        if(getStatistics()[6] != null) {
            nrLabel7.setText("7");
            nickLabel7.setText(getStatistics()[6].getLogin());
            gamesLabel7.setText(getStatistics()[6].getNumberOfGames());
            wonLabel7.setText(getStatistics()[6].getWonGames());
            lostLabel7.setText(getStatistics()[6].getLostGames());
            accuracyLabel7.setText(getStatistics()[6].getAccuracy());
        }

        if(getStatistics()[7] != null) {
            nrLabel8.setText("8");
            nickLabel8.setText(getStatistics()[7].getLogin());
            gamesLabel8.setText(getStatistics()[7].getNumberOfGames());
            wonLabel8.setText(getStatistics()[7].getWonGames());
            lostLabel8.setText(getStatistics()[7].getLostGames());
            accuracyLabel8.setText(getStatistics()[7].getAccuracy());
        }

        if(getStatistics()[8] != null) {
            nrLabel9.setText("9");
            nickLabel9.setText(getStatistics()[8].getLogin());
            gamesLabel9.setText(getStatistics()[8].getNumberOfGames());
            wonLabel9.setText(getStatistics()[8].getWonGames());
            lostLabel9.setText(getStatistics()[8].getLostGames());
            accuracyLabel9.setText(getStatistics()[8].getAccuracy());
        }

        if(getStatistics()[9] != null) {
            nrLabel10.setText("10");
            nickLabel10.setText(getStatistics()[9].getLogin());
            gamesLabel10.setText(getStatistics()[9].getNumberOfGames());
            wonLabel10.setText(getStatistics()[9].getWonGames());
            lostLabel10.setText(getStatistics()[9].getLostGames());
            accuracyLabel10.setText(getStatistics()[9].getAccuracy());
        }

    }

   public Results[] getStatistics() {

        Results [] resultsTab = new Results[10];
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        String SQLQuery = "SELECT s.idUser, login, numberOfGames, wonGames, lostGames, lostGames, accuracy FROM Users u, Statistics s WHERE s.idUser = u.idUser ORDER BY wonGames DESC LIMIT 10";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(SQLQuery);
            for (int i = 0; i < 10; i++)
            {
                if (queryResult.next()) {
                    resultsTab[i] = new Results(
                                    queryResult.getString("login"),
                                    queryResult.getString("numberOfGames"),
                                    queryResult.getString("wonGames"),
                                    queryResult.getString("lostGames"),
                                    queryResult.getString("accuracy"));
                }
            }
                return  resultsTab;
            } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
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

    public void setPlayer (Player player) {
        this.player = player;
        System.out.println(this.player);
        if (this.player != null) {
            leftButton.setText("Wyloguj się");
            rightButton.setText("Moje konto");
        }
    }
}
