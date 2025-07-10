package com.example.praventa.controller;

import com.example.praventa.model.users.LifestyleData;
import com.example.praventa.model.users.User;
import com.example.praventa.model.users.UserListWrapper;
import com.example.praventa.utils.Session;
import com.example.praventa.utils.XmlUtils;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;

public class BerandaController {
    @FXML
    private Button startLifestyleButton;
    @FXML
    private Text usernameText;

    private static final String FILE_PATH = "D:/Kuliah/Project/praventa/data/users.xml";

    @FXML
    public void initialize() {
        checkLifestyleData();
        User currentUser = Session.getCurrentUser();
        if (currentUser != null) {
            usernameText.setText("Hai, " + currentUser.getUsername() + "!");
        } else {
            usernameText.setText("Hai, Pengguna!");
        }
    }

    @FXML
    private void handleStartLifestyleInput(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/lifestyle.fxml"));
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

    private void checkLifestyleData() {
        try {
            File file = new File(FILE_PATH);
            UserListWrapper wrapper = XmlUtils.loadUsers(file);
            User currentUser = Session.getCurrentUser();

            if (currentUser != null) {
                User userFromFile = wrapper.getUserById(currentUser.getId());

                if (userFromFile != null && userFromFile.getLifestyleData() != null) {
                    LifestyleData lifestyle = userFromFile.getLifestyleData();

                    boolean filled =
                            lifestyle.getFastFoodFrequency() != 0 &&
                                    !isNullOrEmpty(lifestyle.getDrinkType()) &&
                                    !isNullOrEmpty(lifestyle.getSmokingHabit()) &&
                                    !isNullOrEmpty(lifestyle.getAlcoholHabit()) &&
                                    !isNullOrEmpty(lifestyle.getPhysicalActivity()) &&
                                    lifestyle.getSleepHabits() != null && !lifestyle.getSleepHabits().isEmpty() &&
                                    lifestyle.getStressLevel() != 0;

                    if (filled) {
                        startLifestyleButton.setText("Sudah Diisi");
                        startLifestyleButton.setStyle("-fx-background-color: #9E91E1; -fx-background-radius: 10;");
                        startLifestyleButton.setDisable(true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty() || value.trim().equals("__");
    }
}
