package com.example.statki;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ModesController {
    public Button humanVShumanBtn;
    public Button humansVSbotBtn;
    public Button botVSbotBtn;
    public Button rightButton;
    public Button leftButton;
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

    public void hhSelected(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("setShips.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        SetShipsController setShipsController = loader.getController();
        setShipsController.setPlayer(player);
        setShipsController.initGameMode(true);
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    public void hbSelected(ActionEvent actionEvent) throws IOException {
        AIPlayer playerBot = new AIPlayer("bot", 1);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("difficulty.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        DifficultyController difficultyController = loader.getController();
        difficultyController.setPlayerAndBot(player, playerBot);
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    public void bbSelected(ActionEvent actionEvent) throws IOException {
        AIPlayer playerBot1 = new AIPlayer("bot1", 2);
        AIPlayer playerBot2 = new AIPlayer("bot2", 3);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("difficulty.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        DifficultyController difficultyController = loader.getController();
        difficultyController.setBots(playerBot1, playerBot2);
        difficultyController.setPlayer(player);
        Stage window =  (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void setPlayer (Player player) {
        this.player = player;
        System.out.println(this.player);
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
