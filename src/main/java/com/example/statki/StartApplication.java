package com.example.statki;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class StartApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent welcomeRoot = FXMLLoader.load(getClass().getResource("start.fxml"));
        Scene welcome = new Scene(welcomeRoot);
        stage.setTitle("Start");
        stage.setScene(welcome);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}