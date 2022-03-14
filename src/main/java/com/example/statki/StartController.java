package com.example.statki;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartController {
    public Button playButton;
    public Button statisticsButton;
    public Button rightButton;
    public Button leftButton;
    public Label titleLabel;
    public Player player;

    public void initialize() {
        if (player == null) {
            playButton.setDisable(true);
        }
    }

    public void rightClicked(ActionEvent actionEvent) throws IOException{
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

    public void leftClicked(ActionEvent actionEvent) throws IOException{
        if (player == null) {
            Parent root =  FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            playButton.setDisable(true);
            player = null;
            leftButton.setText("Zaloguj się");
            rightButton.setText("Rejestracja");
            System.out.println("Użytkownik został wylogowany");
        }
    }

    public void playEvent(ActionEvent actionEvent) throws IOException{
        if (player != null) {
            player.setMyTurn(true);
            Player tempPlayer = new Player(player.getNick(), player.getIdUser());
            if (tempPlayer.getBattlefield() != null) {
                for (int i = 0 ; i <10; i++) {
                    for (int j = 0; j < 10; j++){
                        tempPlayer.getBattlefield().getMap().get(i).get(j).isShip();
                    }
                }
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modes.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            ModesController modesController = loader.getController();
            modesController.setPlayer(tempPlayer);
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    public void setPlayer (Player player) {
        this.player = player;
        System.out.println(this.player);
        if (this.player != null) {
            playButton.setDisable(false);
            leftButton.setText("Wyloguj się");
            rightButton.setText("Moje konto");
        }
    }

    public void statisticsEvent(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("statistics.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        StatisticsController statisticsController = loader.getController();
        statisticsController.setPlayer(player);
        Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
