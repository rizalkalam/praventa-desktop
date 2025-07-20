package com.example.praventa.utils;

import com.example.praventa.controller.MainController;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;

public class SceneUtil {
    public static void switchToMainPage(Node sourceNode) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneUtil.class.getResource("/com/example/praventa/fxml/main.fxml"));

            // Ambil halaman default dari session
            MainController controller = new MainController();
            controller.setDefaultPage(Session.getDefaultPage());

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

    public static void replaceToMainPage(Node sourceNode) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneUtil.class.getResource("/com/example/praventa/fxml/main.fxml"));
            Parent root = loader.load();

            // ✅ Ambil controller dari FXML
            MainController controller = loader.getController();
            controller.setDefaultPage(Session.getDefaultPage());

            // ✅ Ambil stage dari node yang sudah ada
            Stage stage = (Stage) sourceNode.getScene().getWindow();

            // ✅ Ganti scene-nya
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);

            // ✅ Fade-in animation
            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Gagal memuat halaman main.fxml");
        }
    }

    public static void switchSceneFromNode(Node sourceNode, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneUtil.class.getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setMaximized(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
