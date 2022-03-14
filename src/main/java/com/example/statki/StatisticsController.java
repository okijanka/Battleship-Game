package com.example.statki;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class StatisticsController {

    public Player player;
    public Button rightButton;
    public Button leftButton;

    public void setPlayer (Player player) {
        this.player = player;
        System.out.println(this.player);
        if (this.player != null) {
            leftButton.setText("Wyloguj się");
            rightButton.setText("Moje konto");
        }
    }

    public void topTenClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("topTen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        TopTenController topTenController = loader.getController();
        topTenController.setPlayer(player);
        topTenController.getStatistics();
        topTenController.setStatistics();
        Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void replaysClicked(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gamesReplay.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        GamesReplayController gamesReplayController = loader.getController();
        gamesReplayController.setPlayer(player);
        gamesReplayController.getResultGames();
        gamesReplayController.setResultGames();
        Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void searchPlayerClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("searchPlayer.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        SearchPlayerController searchPlayerController = loader.getController();
        searchPlayerController.setPlayer(player);
        Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
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
}
