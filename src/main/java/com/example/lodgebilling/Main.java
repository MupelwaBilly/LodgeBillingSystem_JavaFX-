package com.example.lodgebilling;

import com.example.lodgebilling.database.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        DatabaseManager.initializeDatabase();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 550);

        stage.setTitle("Paradise Lodge - Authentication");

        // CHANGE THIS LINE TO TRUE
        stage.setResizable(true);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}