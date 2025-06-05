package com.example.blackjackgamegr12cscptwithjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApp.class.getResource("blackjack.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setTitle("Blackjack");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}