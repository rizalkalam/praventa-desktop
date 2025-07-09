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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class LifestyleDataController {
    @FXML private AnchorPane rootPane;
    @FXML private VBox formContainer;
    @FXML private Label progressLabel;
    @FXML private Text titleText;
    @FXML private Text descriptionText;
    @FXML private TextField emailField;
    @FXML private Button nextButton;
    @FXML private CheckBox cbTidurNormal;
    @FXML private CheckBox cbBegadang;
    @FXML private CheckBox cbBangunSiang;
    @FXML private CheckBox cbTerbangunMalam;
    @FXML private Slider stressSlider;
    @FXML private Label stressLabel;

    private final List<CheckBox> sleepCheckboxes = new ArrayList<>();
    private final LifestyleData lifestyleData = new LifestyleData();
    private int currentStep = 0;

    private static final String FILE_PATH = "D:/Kuliah/Project/praventa/data/users.xml";

    @FXML
    public void initialize() {
        showStep();
        nextButton.setOnAction(this::handleNext);

        sleepCheckboxes.addAll(List.of(
                cbTidurNormal,
                cbBegadang,
                cbBangunSiang,
                cbTerbangunMalam
        ));

    }

    private void handleNext(ActionEvent event) {
        switch (currentStep) {
            case 0 -> lifestyleData.setFastFoodFrequency(parseInt(emailField.getText()));
            case 1 -> lifestyleData.setDrinkType(emailField.getText());
            case 2 -> lifestyleData.setSmokingHabit(emailField.getText());
            case 3 -> lifestyleData.setAlcoholHabit(emailField.getText());
            case 4 -> lifestyleData.setPhysicalActivity(emailField.getText());
            case 5 -> {
                List<String> selectedHabits = new ArrayList<>();
                for (CheckBox cb : sleepCheckboxes) {
                    if (cb.isSelected()) selectedHabits.add(cb.getText());
                }
                lifestyleData.setSleepHabits(selectedHabits);
            }
            case 6 -> lifestyleData.setStressLevel((int) stressSlider.getValue());
        }

        currentStep++;

        if (currentStep >= 7) {
            saveToUserXml();
            navigateToDashboard(event);
        } else {
            showStep();
        }
    }

    private void showStep() {
        VBox parent = formContainer;

        emailField.setVisible(true);
        emailField.clear();
        emailField.setPromptText("");

        sleepCheckboxes.forEach(cb -> {
            cb.setVisible(false);
            parent.getChildren().remove(cb);
        });

        stressSlider.setVisible(false);
        stressLabel.setVisible(false);

        switch (currentStep) {
            case 0 -> {
                progressLabel.setText("1/7");
                titleText.setText("Frekuensi Fast Food");
                descriptionText.setText("Berapa kali kamu jajan fast food minggu ini?");
                emailField.setPromptText("Misalnya: 3");
            }
            case 1 -> {
                progressLabel.setText("2/7");
                titleText.setText("Jenis Minuman");
                descriptionText.setText("Kamu lebih sering minum apa minggu ini?");
                emailField.setPromptText("Air putih / Minuman manis / Campuran");
            }
            case 2 -> {
                progressLabel.setText("3/7");
                titleText.setText("Kebiasaan Merokok");
                descriptionText.setText("Apakah kamu merokok minggu ini?");
                emailField.setPromptText("Tidak / Kadang-kadang / Setiap hari");
            }
            case 3 -> {
                progressLabel.setText("4/7");
                titleText.setText("Konsumsi Alkohol");
                descriptionText.setText("Apakah kamu mengonsumsi alkohol minggu ini?");
                emailField.setPromptText("Tidak / Kadang-kadang / Setiap hari");
            }
            case 4 -> {
                progressLabel.setText("5/7");
                titleText.setText("Aktivitas Fisik");
                descriptionText.setText("Bagaimana aktivitas fisikmu minggu ini?");
                emailField.setPromptText("Aktif / Sedang / Kurang");
            }
            case 5 -> {
                progressLabel.setText("6/7");
                titleText.setText("Kebiasaan Tidur");
                descriptionText.setText("Pilih semua yang menggambarkan kebiasaan tidur kamu minggu ini:");
                emailField.setVisible(false);

                sleepCheckboxes.forEach(cb -> {
                    cb.setVisible(true);
                    int nextButtonIndex = parent.getChildren().indexOf(nextButton);
                    if (!parent.getChildren().contains(cb) && nextButtonIndex != -1) {
                        parent.getChildren().add(nextButtonIndex, cb);
                    }
                });
            }
            case 6 -> {
                progressLabel.setText("7/7");
                titleText.setText("Level Stres Mingguan");
                descriptionText.setText("Seberapa besar tingkat stres kamu minggu ini? (0 = tidak stres, 10 = sangat stres)");

                emailField.setVisible(false);
                sleepCheckboxes.forEach(cb -> cb.setVisible(false));

                stressSlider.setVisible(true);
                stressLabel.setVisible(true);
                stressSlider.setValue(0);
                stressLabel.setText("Level stres: 0");

                // Optional: update label ketika slider digeser
                stressSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                    stressLabel.setText("Level stres: " + newVal.intValue());
                });
            }
        }
    }

    private void saveToUserXml() {
        try {
            File file = new File(FILE_PATH);
            UserListWrapper wrapper = XmlUtils.loadUsers(file);
            User currentUser = Session.getCurrentUser();

            if (currentUser != null) {
                currentUser.setLifestyleData(lifestyleData);
                wrapper.replaceUser(currentUser);
                XmlUtils.saveUsers(wrapper, file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/main.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setMaximized(true);

            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();

            stage.show();

            Stage current = (Stage) ((Button) event.getSource()).getScene().getWindow();
            current.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
