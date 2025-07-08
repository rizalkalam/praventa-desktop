package com.example.praventa.utils;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneUtil {
    public static void switchToMainPage(Node sourceNode) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneUtil.class.getResource("/com/example/praventa/fxml/main.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Praventa - Dashboard");
            stage.setScene(scene);
            stage.setMaximized(true);

            // Fade-in animation
            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            stage.show();

            // Tutup window sebelumnya
            Stage currentStage = (Stage) sourceNode.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Gagal memuat halaman main.fxml");
        }
    }
}
