package com.example.praventa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage; // untuk akses di controller (misal saat pindah scene)

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Praventa - Login");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public static void setRoot(String fxmlPath) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}
