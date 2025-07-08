package com.example.praventa.controller;

import com.example.praventa.model.users.User;
import com.example.praventa.repository.UserRepository;
import com.example.praventa.utils.SceneUtil;
import com.example.praventa.utils.Session;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private AnchorPane rootPane;

    @FXML
    private void handleMasukClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = checkLogin(username, password);
        if (user != null) {
            // Simpan user ke session
            Session.setCurrentUser(user);

            SceneUtil.switchToMainPage(rootPane);

        } else {
            System.out.println("Login gagal. Username atau password salah.");
        }
    }

    private User checkLogin(String username, String password) {
        return UserRepository.findByUsernameAndPassword(username, password);
    }

    @FXML
    private void handleRegisterClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/register.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Praventa - Daftar Akun");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();

            // Optional: animasi fade
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            stage.show();

            // Tutup window login
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
