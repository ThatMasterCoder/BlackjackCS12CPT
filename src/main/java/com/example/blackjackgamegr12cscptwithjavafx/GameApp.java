package com.example.blackjackgamegr12cscptwithjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GameApp extends Application {


    /**
     * The start method is the entry point for the JavaFX application.
     * It sets up the main stage and loads the FXML layout for the Blackjack game.
     *
     * @param stage The primary stage for this application, onto which the application scene can be set.
     * @throws IOException If the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(GameApp.class.getResource("blackjack.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setTitle("Blackjack");
        stage.setScene(scene);

        stage.getIcons().add(new Image(
                Objects.requireNonNull(getClass().getResource("/com/example/blackjackgamegr12cscptwithjavafx/images/casino-chip.png")).toExternalForm()
        ));

        stage.show();

    }

    /**
     * The main method is the entry point for the JavaFX application.
     * It launches the application.
     */
    public static void main(String[] args) {
        launch();
    }
}