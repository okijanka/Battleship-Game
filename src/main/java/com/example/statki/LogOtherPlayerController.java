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

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.statki.LoginController.hashPassword;

public class LogOtherPlayerController {
    public Button rightButton;
    public Button leftButton;
    public Button loginButton;
    public TextField nickTextField;
    public PasswordField passwordTextField;
    public Button homeBackLabel;
    public Label InfoMessageLabel;
    public Player secondPlayer = null;
    public Player player;

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
            secondPlayer = null;
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


    public void loginClicked(ActionEvent actionEvent) throws NoSuchAlgorithmException {
        this.InfoMessageLabel.setText("Zaloguj się");
        if (!this.nickTextField.getText().isBlank() && !this.passwordTextField.getText().isBlank()) {
            this.validateLogin(actionEvent);
            this.nickTextField.clear();
            this.passwordTextField.clear();
        } else {
            this.InfoMessageLabel.setText("Prosze wprowadź login i hasło");
            this.nickTextField.clear();
            this.passwordTextField.clear();
        }

    }

    public void validateLogin(ActionEvent actionEvent) throws NoSuchAlgorithmException {
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        this.InfoMessageLabel.setText("Niepoprawne dane");
        String firstPlayerNick = this.player.nick;
        String nick = nickTextField.getText();
        String password = hashPassword(passwordTextField.getText());
        String verifyLogin = "SELECT login FROM Users WHERE login = '" + nick + "' AND PASSWORD = '" + password + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()) {
                if (!queryResult.getString("login").equals(firstPlayerNick)) {
                    this.InfoMessageLabel.setText("Gratulacje");
                    this.leftButton.setText("Wyloguj się");
                    this.rightButton.setText("Moje konto");
                    this.secondPlayer = getPlayer(nick);
                    System.out.println(this.secondPlayer);
                    PrintStream var10000 = System.out;
                    int var10001 = this.secondPlayer.getIdUser();
                    var10000.println(var10001 + this.secondPlayer.getNick());
                    changeOfWindow(actionEvent);
                } else if (queryResult.getString("login").equals(firstPlayerNick))
                {
                    this.InfoMessageLabel.setText("Nie możesz zagrać z samym sobą!");
                }
                else
                {
                    this.InfoMessageLabel.setText("Niepoprawne dane");
                }
            }
        } catch (Exception var9) {
            var9.printStackTrace();
            var9.getClass();
        }

    }

    public static Player getPlayer(String nick) {
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        String StringQuery = "SELECT idUser, login FROM Users WHERE login = '" + nick + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(StringQuery);
            return queryResult.next() ? new Player(queryResult.getString("login"), queryResult.getInt("idUser")) : null;
        } catch (Exception var6) {
            var6.printStackTrace();
            var6.getClass();
            return null;
        }
    }

    public void changeOfWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("setShipsEnemy.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        SetShipsEnemyController setShipsEnemyController = loader.getController();
        setShipsEnemyController.setPlayer(player, secondPlayer);

        Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

    public void cancelLabelOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("start.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        StartController startController = loader.getController();
        startController.setPlayer(player);
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
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
