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
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.statki.LoginController.hashPassword;

public class MyAccountController {

    public TextField currentPasswordTextField;
    public TextField newPasswordTextField;
    public Label nrLabel;
    public Label nickLabel;
    public Label gamesLabel;
    public Label wonLabel;
    public Label lostLabel;
    public Label accuracyLabel;
    public Button confirmButton;
    public Label InfoTextLabel;
    public Player player;
    public Button rightButton;
    public Button leftButton;

    public void rightClicked(ActionEvent actionEvent) throws IOException {
        if (player == null) {
            Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void leftClicked(ActionEvent actionEvent) throws IOException {
            player = null;
            leftButton.setText("Zaloguj się");
            rightButton.setText("Rejestracja");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("start.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            System.out.println("Użytkownik został wylogowany");
    }

    public void confirmedClicked(ActionEvent actionEvent) throws NoSuchAlgorithmException {
        if (currentPasswordTextField.getText().isBlank() == false && newPasswordTextField.getText().isBlank() == false) {
            validateChangePassword(actionEvent);
            currentPasswordTextField.clear();
            newPasswordTextField.clear();
        } else {
            InfoTextLabel.setText("Prosze wprowadź dane.");
            currentPasswordTextField.clear();
            newPasswordTextField.clear();
        }
    }

    private void validateChangePassword(ActionEvent actionEvent) throws NoSuchAlgorithmException {

        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();

        String nickPlayer = this.player.nick;
        String currentPassword = hashPassword(currentPasswordTextField.getText());
        System.out.println(currentPassword);
        String newPassword = hashPassword(newPasswordTextField.getText());
        System.out.println(hashPassword(newPassword));

        String veryfiyCurrentPassword = "SELECT password FROM Users WHERE login = '" + nickPlayer + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(veryfiyCurrentPassword);

            while (queryResult.next()) {
                System.out.println("Obecne " + currentPassword);
                if (queryResult.getString("password").equals(currentPassword)  && !currentPassword.equals(newPassword))
                {
                    updatePassword();
                }
                else if (currentPassword.equals(newPassword) && queryResult.getString("password").equals(currentPassword))
                {
                    InfoTextLabel.setText("Twoje hasla są identyczne. Zmień je.");
                }
                else
                {
                    InfoTextLabel.setText("Wprowdziłeś niepoprawne aktualne hasło");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getClass();
        }
    }

    public void updatePassword () throws NoSuchAlgorithmException {
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();

        String nickPlayer = this.player.nick;
        String newPassword = hashPassword(newPasswordTextField.getText());

        String updateNewPassword = "UPDATE Users SET password = '" + newPassword + "' WHERE login = '" + nickPlayer + "'";

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(updateNewPassword);
            System.out.println("Zmienione hasło: " + newPassword);
            InfoTextLabel.setText("Hasło zostalo zmienione pomyślnie");
        } catch (SQLException e) {
            e.printStackTrace();
            e.getClass();
        }
    }



    public void setStatistics () {

        nrLabel.setText(getStatistics(this.player.nick)[0]);
        nickLabel.setText(getStatistics(this.player.nick)[1]);
        gamesLabel.setText(getStatistics(this.player.nick)[2]);
        wonLabel.setText(getStatistics(this.player.nick)[3]);
        lostLabel.setText(getStatistics(this.player.nick)[4]);
        accuracyLabel.setText(getStatistics(this.player.nick)[5]);
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
    
    public void setPlayer (Player player) {
        this.player = player;
        System.out.println("Obecny player to: " + this.player.nick);

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
}
