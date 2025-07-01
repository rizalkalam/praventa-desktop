package com.example.praventa.controller;

import com.example.praventa.model.questionnaire.Question;
import com.example.praventa.model.questionnaire.QuestionnaireType;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class QuestionnaireController {

    @FXML
    private AnchorPane rootPane;
    @FXML private TextField emailField; // Default input dari FXML
    @FXML private Label progressLabel;
    @FXML private Text titleText;
    @FXML private Text descriptionText;
    @FXML private Button nextButton;

    private int currentStep = 0;

    private final QuestionStep[] questionSteps = {
            new QuestionStep("1/4", "Masukkan Umur Anda",
                    "Masukkan usia Anda saat ini dalam satuan tahun. Usia akan memengaruhi perhitungan risiko kesehatan tertentu.",
                    InputType.TEXT, null),

            new QuestionStep("2/4", "Pilih Jenis Kelamin",
                    "Data ini digunakan untuk analisis risiko berbasis gender.",
                    InputType.SELECT, List.of("Pria", "Wanita", "Other")),

            new QuestionStep("3/4", "Masukkan Berat Badan",
                    "Dalam satuan kilogram (kg). Berat badan dibutuhkan untuk perhitungan BMI.",
                    InputType.TEXT, null),

            new QuestionStep("4/4", "Masukkan Tinggi Badan",
                    "Dalam satuan sentimeter (cm). Tinggi badan juga digunakan untuk perhitungan BMI.",
                    InputType.TEXT, null)
    };

    // Dinamis input: ComboBox jika SELECT
    private ComboBox<String> genderComboBox;

    @FXML
    public void initialize() {
        genderComboBox = new ComboBox<>();
        genderComboBox.setPrefWidth(emailField.getPrefWidth());
        genderComboBox.setLayoutX(emailField.getLayoutX());
        genderComboBox.setLayoutY(emailField.getLayoutY());
        genderComboBox.setStyle("-fx-background-radius: 10; -fx-border-color: #505050;");

        applyStep(currentStep);

        nextButton.setOnAction(event -> {
            if (currentStep < questionSteps.length - 1) {
                currentStep++;
                applyStep(currentStep);
            } else {
                System.out.println("Kuisioner selesai, bisa simpan jawaban.");
                // TODO: simpan jawaban
            }
        });
    }

    private void applyStep(int stepIndex) {
        QuestionStep step = questionSteps[stepIndex];
        progressLabel.setText(step.progress);
        titleText.setText(step.title);
        descriptionText.setText(step.description);

        // Hapus input sebelumnya
        rootPane.getChildren().remove(genderComboBox);
        emailField.setVisible(false);

        if (step.inputType == InputType.TEXT) {
            emailField.setVisible(true);
            emailField.setText(""); // Kosongkan untuk input baru
        } else if (step.inputType == InputType.SELECT) {
            genderComboBox.getItems().setAll(step.options);
            genderComboBox.getSelectionModel().clearSelection();
            rootPane.getChildren().add(genderComboBox);
        }
    }

    private enum InputType {
        TEXT,
        SELECT
    }

    private static class QuestionStep {
        String progress;
        String title;
        String description;
        InputType inputType;
        List<String> options;

        QuestionStep(String progress, String title, String description, InputType inputType, List<String> options) {
            this.progress = progress;
            this.title = title;
            this.description = description;
            this.inputType = inputType;
            this.options = options;
        }
    }
}
