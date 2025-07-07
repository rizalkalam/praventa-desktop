package com.example.praventa.controller;

import com.example.praventa.model.User;
import com.example.praventa.repository.UserRepository;
import com.example.praventa.utils.Database;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;

    @FXML
    private void handleDaftarClick(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Semua field harus diisi!");
            return;
        }

        // Buat user baru
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setPhoneNumber("__");
        newUser.setProfilePicture("assets/icn_akun.png");
        newUser.setRole("pasien");

        // Gunakan repository untuk register
        boolean success = UserRepository.register(newUser);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Pendaftaran berhasil!");
            openQuestionnairePage(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Username atau email sudah digunakan!");
        }
    }

    @FXML
    private void handleLoginClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Praventa - Masuk Akun");
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

    private void openLoginPage(javafx.event.Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Praventa - Login");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();

            // Fade animation
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            stage.show();

            // Close register page
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openQuestionnairePage(javafx.event.Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/questionnaire.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Praventa - Data Dasar");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();

            // Optional: animasi fade
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            stage.show();

            // Tutup window register
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
