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
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

import static com.example.statki.LoginController.hashPassword;

public class RegisterController {
    public Button leftButton;
    public Button rightButton;
    public Button signingButton;
    public TextField nickTextField;
    public TextField emailTextField;
    public PasswordField passwordTextField;
    public Button homeBackLabel;
    public Label registrationMessageLabel;
    public Player player = null;


    public void leftClicked(ActionEvent actionEvent) throws IOException{
        if (player != null) {
            player = null;
            leftButton.setText("Zaloguj się");
            rightButton.setText("Rejestracja");
            System.out.println("Użytkownik został wylogowany");
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    public void rightClicked(ActionEvent actionEvent) {

    }

    public void signingClicked(ActionEvent actionEvent) throws NoSuchAlgorithmException {

        if (nickTextField.getText().isBlank() == true || passwordTextField.getText().isBlank() == true  || emailTextField.getText().isBlank() == true) {
            registrationMessageLabel.setText("Dane nie zostały wprowadzone poprawnie.");
            nickTextField.clear();
            passwordTextField.clear();
            emailTextField.clear();
        } else {
            registerUser(actionEvent);
            emailTextField.clear();
            nickTextField.clear();
            passwordTextField.clear();
        }

    }


    public void closeBackOnAction (ActionEvent actionEvent) throws IOException {
        changeOfWindow(actionEvent);
    }

    public void registerUser(ActionEvent actionEvent) throws NoSuchAlgorithmException {

        Pattern pattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

        String nick = nickTextField.getText();
        String password = hashPassword(passwordTextField.getText());
        String email = emailTextField.getText();

        System.out.println(password);

        if (!pattern.matcher(emailTextField.getText()).matches()) {
            registrationMessageLabel.setText("Niepoprawny e-mail");
        }
        else if (isNickTaken(nick,email,password) == true)
        {
            registrationMessageLabel.setText("Isnieje użytkownik o danym loginie.");
        }
        else if (isEmailTaken(nick,email,password) == true)
        {
            registrationMessageLabel.setText("Istnieje użytkownik z adresem e-mail.");
        }
        else
        {
            successfulLogin(actionEvent, nick,email,password);
        }

    }

    public void successfulLogin (ActionEvent actionEvent, String nick, String email, String password)  {
        DatabaseConnector conectNow = new DatabaseConnector();
        Connection conn = conectNow.getConnection();


        String insertFields = "INSERT INTO Users(login,email,password) VALUES ('";
        String insertValues = nick + "', '" + email + "', '" + password + "')";
        String insertToRegister = insertFields + insertValues;

        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(insertToRegister);
            registrationMessageLabel.setText("Sukces");
            leftButton.setText("Wyloguj się");
            rightButton.setText("Menu");
            player = LoginController.getPlayer(nick);
            updateStatistics();
            System.out.println(player.getIdUser() + player.getNick());
            changeOfWindow(actionEvent);
        } catch (SQLException e) {
            e.printStackTrace();
            e.getClass();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateStatistics () {
        DatabaseConnector conectNow = new DatabaseConnector();
        Connection conn = conectNow.getConnection();
        String insertFields = "INSERT INTO Statistics(idUser, numberOfGames, wonGames, lostGames,accuracy,numberOfAllShots, numberOfHitShots) VALUES ('" +
                player.getIdUser() + "', 0,0,0,0,0,0); ";
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(insertFields);
        } catch (SQLException e) {
            e.printStackTrace();
            e.getClass();
        }

    }

    public boolean isNickTaken (String nick, String email, String password)  {
        DatabaseConnector conectNow = new DatabaseConnector();
        Connection conn = conectNow.getConnection();

        String insertToRegister =  "SELECT * FROM Users WHERE login = '" + nick + "' ";

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(insertToRegister);
            if (resultSet.next()) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            e.getClass();
            return false;
        }
    }

    public boolean isEmailTaken (String nick, String email, String password)  {
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection conn = connectNow.getConnection();

        String insertToRegister =  "SELECT * FROM Users WHERE email = '" + email + "' ";

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(insertToRegister);
            if (resultSet.next()) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            e.getClass();
            return false;
        }
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
