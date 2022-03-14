package com.example.statki;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController {

    public Button rightButton;
    public Button leftButton;
    public Button loginButton;
    public TextField nickTextField;
    public PasswordField passwordTextField;
    public Button homeBackLabel;
    public Label InfoMessageLabel;
    public Player player = null;

    public void cancelLabelOnAction (ActionEvent actionEvent) throws IOException {
        changeOfWindow(actionEvent);
    }

    public void rightClicked(ActionEvent actionEvent) throws IOException {
        if (player != null) {
            player = null;
            leftButton.setText("Zaloguj się");
            rightButton.setText("Rejestracja");
            System.out.println("Użytkownik został wylogowany");
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    public void leftClicked(ActionEvent actionEvent) {
        if (player != null) {
            player = null;
            leftButton.setText("Zaloguj się");
            rightButton.setText("Rejestracja");
            System.out.println("Użytkownik został wylogowany");
        }
    }

    public void loginClicked(ActionEvent actionEvent) throws NoSuchAlgorithmException {
        if (nickTextField.getText().isBlank() == false && passwordTextField.getText().isBlank() == false) {
            validateLogin(actionEvent);
            nickTextField.clear();
            passwordTextField.clear();
        } else {
            InfoMessageLabel.setText("Prosze wprowadź login i hasło");
            nickTextField.clear();
            passwordTextField.clear();
        }
    }

    public void validateLogin(ActionEvent actionEvent) throws NoSuchAlgorithmException {
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        InfoMessageLabel.setText(String.valueOf(connectDB));
        String nick = nickTextField.getText();
        String password = hashPassword(passwordTextField.getText());
        System.out.println(password);

        String verifyLogin = "SELECT count(1) FROM Users WHERE login = '" + nick + "' AND PASSWORD = '" + password + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    InfoMessageLabel.setText("Gratulacje");
                    leftButton.setText("Wyloguj się");
                    rightButton.setText("Menu");
                    player = getPlayer(nick);
                    System.out.println(player.getIdUser() + player.getNick());
                    changeOfWindow(actionEvent);
                }
                else {
                    InfoMessageLabel.setText("Niepoprawne dane");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getClass();
        }
    }

    public static Player getPlayer(String nick) {
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();

        String StringQuery = "SELECT idUser, login FROM Users WHERE login = '" + nick + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(StringQuery);
            if (queryResult.next()) {
                return new Player(queryResult.getString("login"), (queryResult.getInt("idUser")));
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            e.getClass();
            return null;
        }
    }

    public static String hashPassword (String password) throws NoSuchAlgorithmException {

            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(password.getBytes());
                byte [] resultByteArray = messageDigest.digest();
                StringBuilder sb = new StringBuilder();

                for (byte b : resultByteArray) {
                    sb.append(String.format("%02x", b));
                }
                return sb.toString();
          } catch ( NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
        return "";
    }

    public void changeOfWindow (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("start.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        StartController startController = loader.getController();
        startController.setPlayer(player);
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
