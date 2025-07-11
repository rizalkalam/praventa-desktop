package com.example.praventa.controller;

import com.example.praventa.model.users.User;
import com.example.praventa.utils.Session;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

public class RiwayatController {

    @FXML
    private VBox navContainer;

    @FXML
    private AnchorPane mainContent;

    @FXML
    private Text usernameText;
    @FXML
    private Text ageText;
    @FXML
    private Text genderText;
    @FXML
    private Text heightnweightText;

    public void initialize() {
        User currentUser = Session.getCurrentUser();
        if (currentUser != null) {
            usernameText.setText(currentUser.getUsername());
            String birthDateStr = currentUser.getPersonalData().getAge(); // Contoh: "2001-05-20"
            LocalDate birthDate = LocalDate.parse(birthDateStr); // Konversi String ke LocalDate
            LocalDate today = LocalDate.now(); // Tanggal hari ini

            int age = Period.between(birthDate, today).getYears();
            ageText.setText(String.valueOf(age));
            genderText.setText(currentUser.getPersonalData().getGender());
            int height = currentUser.getPersonalData().getBodyMetrics().getHeight();
            int weight = currentUser.getPersonalData().getBodyMetrics().getWeight();
            heightnweightText.setText(String.valueOf(height)+"cm"+"/"+String.valueOf(weight)+"kg");
        }
    }

    @FXML
    private void handleEditFamilyDisease(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/family_disease.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Input Gaya Hidup - Praventa");
            stage.setScene(scene);
            stage.setMaximized(true);

            // Fade in animation (opsional)
            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            stage.show();

            // Tutup window sekarang
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddHistory(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/personal_disease.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Input Gaya Hidup - Praventa");
            stage.setScene(scene);
            stage.setMaximized(true);

            // Fade in animation (opsional)
            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            stage.show();

            // Tutup window sekarang
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
