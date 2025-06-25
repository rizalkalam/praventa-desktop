package com.example.praventa.controller;

import com.example.praventa.Main;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.equals("admin") && pass.equals("admin")) {
            try {
                Main.setRoot("/fxml/main.fxml"); // Pindah ke main layout (sidebar + content)
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // tampilkan error atau alert
            System.out.println("Login gagal!");
        }
    }
}
