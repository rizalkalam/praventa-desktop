package com.example.praventa.controller;

import com.example.praventa.Main;
import com.example.praventa.model.User;
import com.example.praventa.util.Database;
import com.example.praventa.util.Session;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleMasukClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = checkLogin(username, password);
        if (user != null) {
            // Simpan user ke session
            Session.setCurrentUser(user);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/main.fxml"));
                Parent root = loader.load();

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Praventa - Dashboard");
                stage.setScene(scene);
                stage.setMaximized(true);
                //stage.setResizable(false);

                // Fade-in animation
                FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();

                stage.show();

                // Tutup window login
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Login gagal. Username atau password salah.");
        }
    }

    private User checkLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // Belum hashed

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Buat objek user untuk disimpan ke session
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("profile_picture"),
                        rs.getString("phone_number")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
