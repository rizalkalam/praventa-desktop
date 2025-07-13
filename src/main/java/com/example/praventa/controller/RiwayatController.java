package com.example.praventa.controller;

import com.example.praventa.model.users.FamilyDiseaseHistory;
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
import java.util.List;

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

    @FXML private Text relation1;
    @FXML private Text relation2;
    @FXML private Text relation3;
    @FXML private Text relation4;
    @FXML private Text disease1;
    @FXML private Text disease2;
    @FXML private Text disease3;
    @FXML private Text disease4;


    public void initialize() {
        User currentUser = Session.getCurrentUser();
        if (currentUser != null) {
            // Info personal
            usernameText.setText(currentUser.getUsername());
            ageText.setText(String.valueOf(currentUser.getPersonalData().getAge()));
            genderText.setText(currentUser.getPersonalData().getGender());

            int height = currentUser.getPersonalData().getBodyMetrics().getHeight();
            int weight = currentUser.getPersonalData().getBodyMetrics().getWeight();
            heightnweightText.setText(height + "cm/" + weight + "kg");

            // Info riwayat keluarga
            List<FamilyDiseaseHistory> familyDiseases = currentUser.getFamilyDiseaseHistoryList();
            if (familyDiseases != null && !familyDiseases.isEmpty()) {
                setFamilyDiseaseData(familyDiseases);
            } else {
                resetFamilyDiseaseDisplay();
            }
        }
    }

    private void setFamilyDiseaseData(List<FamilyDiseaseHistory> list) {
        if (list.size() > 0) {
            relation1.setText(list.get(0).getRelation());
            disease1.setText(list.get(0).getDiseaseName());
        }
        if (list.size() > 1) {
            relation2.setText(list.get(1).getRelation());
            disease2.setText(list.get(1).getDiseaseName());
        }
        if (list.size() > 2) {
            relation3.setText(list.get(2).getRelation());
            disease3.setText(list.get(2).getDiseaseName());
        }
        if (list.size() > 3) {
            relation4.setText(list.get(3).getRelation());
            disease4.setText(list.get(3).getDiseaseName());
        }

        // Jika jumlah data < 4, sisanya diisi default
        if (list.size() < 4) {
            if (list.size() < 1) {
                relation1.setText("__"); disease1.setText("__");
            }
            if (list.size() < 2) {
                relation2.setText("__"); disease2.setText("__");
            }
            if (list.size() < 3) {
                relation3.setText("__"); disease3.setText("__");
            }
            if (list.size() < 4) {
                relation4.setText("__"); disease4.setText("__");
            }
        }
    }

    private void resetFamilyDiseaseDisplay() {
        relation1.setText("__"); disease1.setText("__");
        relation2.setText("__"); disease2.setText("__");
        relation3.setText("__"); disease3.setText("__");
        relation4.setText("__"); disease4.setText("__");
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
