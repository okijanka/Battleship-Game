package com.example.statki;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class DifficultyController {

    public AIPlayer playerBot = null;
    public AIPlayer playerBot1 = null;
    public AIPlayer playerBot2 = null;
    public Button rightButton;
    public Button leftButton;
    public Player player;
    public BorderPane ap;

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
    public void easySelected(ActionEvent actionEvent) throws IOException {
        if (playerBot == null) {
            playerBot1.setDifficultyLevel(AIPlayer.DifficultyLevel.EASY);
            playerBot2.setDifficultyLevel(AIPlayer.DifficultyLevel.EASY);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gameAI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            GameAIController gameAIController = loader.getController();
            gameAIController.setBots(playerBot1, playerBot2, player, ap);
            Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            playerBot.setDifficultyLevel(AIPlayer.DifficultyLevel.EASY);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("setShips.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            SetShipsController setShipsController = loader.getController();
            setShipsController.setPlayerAndBot(player, playerBot);
            Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }

    public void mediumSelected(ActionEvent actionEvent) throws IOException {
        if (playerBot == null) {
            playerBot1.setDifficultyLevel(AIPlayer.DifficultyLevel.MEDIUM);
            playerBot2.setDifficultyLevel(AIPlayer.DifficultyLevel.MEDIUM);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gameAI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            GameAIController gameAIController = loader.getController();
            gameAIController.setBots(playerBot1, playerBot2, player, ap);
            Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            playerBot.setDifficultyLevel(AIPlayer.DifficultyLevel.MEDIUM);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("setShips.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            SetShipsController setShipsController = loader.getController();
            setShipsController.setPlayerAndBot(player, playerBot);
            Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }

    public void difficultSelected(ActionEvent actionEvent) throws IOException {
        if (playerBot == null) {
            playerBot1.setDifficultyLevel(AIPlayer.DifficultyLevel.HARD);
            playerBot2.setDifficultyLevel(AIPlayer.DifficultyLevel.HARD);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gameAI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            GameAIController gameAIController = loader.getController();
            gameAIController.setBots(playerBot1, playerBot2, player, ap);
            Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            playerBot.setDifficultyLevel(AIPlayer.DifficultyLevel.HARD);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("setShips.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            SetShipsController setShipsController = loader.getController();
            setShipsController.setPlayerAndBot(player, playerBot);
            Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }

    public void setPlayerAndBot(Player player, AIPlayer playerBot) {
        this.player = player;
        this.playerBot = playerBot;
    }

    public void setBots(AIPlayer playerBot1, AIPlayer playerBot2) {
        this.playerBot1 = playerBot1;
        this.playerBot2 = playerBot2;
    }

    public void setPlayer (Player player) {
        this.player = player;
        System.out.println(this.player);
    }
}
