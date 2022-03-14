package com.example.statki;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class SearchPlayerController {
    public Label nrLabel;
    public Label nickLabel;
    public Label gamesLabel;
    public Label wonLabel;
    public Label lostLabel;
    public Label accuracyLabel;
    public Button searchButton;
    public Button leftButton;
    public Button rightButton;
    public Label InfoTextMessage;
    public TextField nickPlayerTextField;
    Player player;

    public void searchClicked(ActionEvent actionEvent) {
        if (nickPlayerTextField.getText().isBlank() == false) {
            InfoTextMessage.setText("Wprowadzono login");
            validateSearch(actionEvent);
            nickPlayerTextField.clear();
        } else {
            InfoTextMessage.setText("Prosze wprowadź login");
        }
    }

    private void validateSearch(ActionEvent actionEvent) {
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        //InfoTextMessage.setText(String.valueOf(connectDB));
        String nick = nickPlayerTextField.getText();

        String verifyNick = "SELECT count(1) FROM Users WHERE login != 'bot' AND login != 'bot1' AND login != 'bot2' AND login = '" + nick + "'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyNick);

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    nrLabel.setText(getStatistics(nick)[0]);
                    nickLabel.setText(getStatistics(nick)[1]);
                    gamesLabel.setText(getStatistics(nick)[2]);
                    wonLabel.setText(getStatistics(nick)[3]);
                    lostLabel.setText(getStatistics(nick)[4]);
                    accuracyLabel.setText(getStatistics(nick)[5]);
                }
                else {
                    InfoTextMessage.setText("Użytkownik nie istnieje");
                    nrLabel.setText("");
                    nickLabel.setText("");
                    gamesLabel.setText("");
                    wonLabel.setText("");
                    lostLabel.setText("");
                    accuracyLabel.setText("");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getClass();
        }

    }

    private String [] getStatistics (String nick) {

        String [] resultsTab = new String[6];
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        String SQLQuery = "SELECT s.idUser, login, numberOfGames, wonGames, lostGames, lostGames, accuracy FROM Users u, Statistics s WHERE s.idUser = u.idUser AND login = '" + nick + "'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(SQLQuery);
            if (queryResult.next()) {
                resultsTab[0] = queryResult.getString("s.idUser");
                resultsTab[1] = queryResult.getString("login");
                resultsTab[2] = queryResult.getString("numberOfGames");
                resultsTab[3] = queryResult.getString("wonGames");
                resultsTab[4] = queryResult.getString("lostGames");
                resultsTab[5] = queryResult.getString("accuracy");

                return  resultsTab;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            e.getClass();
            return null;
        }
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
