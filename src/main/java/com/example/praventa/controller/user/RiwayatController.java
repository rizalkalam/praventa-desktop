package com.example.praventa.controller.user;

import com.example.praventa.model.questionnaire.QuestionAnswer;
import com.example.praventa.model.questionnaire.QuestionnaireResult;
import com.example.praventa.model.users.FamilyDiseaseHistory;
import com.example.praventa.model.users.PersonalDisease;
import com.example.praventa.model.users.User;
import com.example.praventa.model.users.UserListWrapper;
import com.example.praventa.utils.Session;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
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

    @FXML
    private BorderPane containerRiskInfo;

    @FXML
    private VBox containerPersonalDisease;
    @FXML
    private BorderPane warp_content_History; // template utama dari FXML
    @FXML
    private Text titleHistory;
    @FXML
    private Text dateHistory;
    @FXML
    private Text drugInfo;
    @FXML
    private Text statusRawat;

    @FXML
    private VBox containerSkrinning;
    @FXML
    private BorderPane warp_content_Skrinning;
    @FXML
    private Label skrinningLabel;
    @FXML
    private Text titleSkrinning;
    @FXML
    private Text statusSkrinning;
    @FXML
    private Text textDste;

    public void initialize() {
        User user = Session.getCurrentUser();
        if (user == null) return;

        // Personal Info
        usernameText.setText(user.getUsername());

        if (user.getPersonalData() != null) {
            ageText.setText(String.valueOf(user.getPersonalData().getAge()));
            genderText.setText(user.getPersonalData().getGender());

            if (user.getPersonalData().getBodyMetrics() != null) {
                int height = user.getPersonalData().getBodyMetrics().getHeight();
                int weight = user.getPersonalData().getBodyMetrics().getWeight();
                heightnweightText.setText(height + "cm / " + weight + "kg");
            }
        }

        // Family Disease
        List<FamilyDiseaseHistory> familyDiseases = user.getFamilyDiseaseHistoryList();
        if (familyDiseases != null && !familyDiseases.isEmpty()) {
            setFamilyDiseaseData(familyDiseases);
        } else {
            resetFamilyDiseaseDisplay();
        }

        // Personal Disease
        List<PersonalDisease> historyList = user.getPersonalDiseases();
        containerPersonalDisease.getChildren().clear(); // pastikan bersih dulu

        if (historyList != null && !historyList.isEmpty()) {
            for (PersonalDisease pd : historyList) {
                BorderPane item = createHistoryItem(pd);
                containerPersonalDisease.getChildren().add(item);
            }
        } else {
            Label kosongLabel = new Label("Belum ada riwayat penyakit pribadi.");
            kosongLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #888;");
            kosongLabel.setAlignment(Pos.CENTER);
            kosongLabel.setMaxWidth(Double.MAX_VALUE);

            VBox.setVgrow(kosongLabel, Priority.ALWAYS);
            containerPersonalDisease.setAlignment(Pos.CENTER);
            containerPersonalDisease.getChildren().add(kosongLabel);
        }


        // Risiko dari riwayat keluarga
        boolean hasRisk = familyDiseases != null && !familyDiseases.isEmpty();
        containerRiskInfo.setVisible(hasRisk);
        containerRiskInfo.setManaged(hasRisk);

        // Sembunyikan template skrining
        warp_content_Skrinning.setVisible(false);
        warp_content_Skrinning.setManaged(false);

        // Tampilkan skrining
        tampilkanSkriningStatik(user);
    }

    private BorderPane createHistoryItem(PersonalDisease pd) {
        BorderPane clone = new BorderPane();
        clone.setStyle("-fx-background-color: #fff; -fx-background-radius: 14;");
        clone.setPrefHeight(66);
        clone.setPrefWidth(1112);
        clone.setPadding(new Insets(0, 30, 0, 30));

        Text title = new Text(pd.getName() + " (" + pd.getActiveStatus() + ")");
        title.setFont(Font.font("System Bold", 14));
        StackPane leftPane = new StackPane(title);
        StackPane.setAlignment(title, javafx.geometry.Pos.CENTER_LEFT);
        leftPane.setPrefWidth(200);
        clone.setLeft(leftPane);

        String rawatText = "rawat inap : " + (pd.isHospitalized() ? "ya" : "tidak");
        String drugText = pd.getTreatmentDetails().isEmpty() ? "Tidak ada pengobatan" : pd.getTreatmentDetails();

        Text treatment = new Text(drugText);
        treatment.setFont(Font.font(14));
        Text rawat = new Text(rawatText);
        rawat.setFont(Font.font(14));

        HBox centerBox = new HBox(60, treatment, rawat);
        centerBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        centerBox.setPadding(new Insets(20, 0, 20, 0));
        clone.setCenter(centerBox);

        Text year = new Text(String.valueOf(pd.getDiagnosedYear()));
        year.setFont(Font.font(14));
        clone.setRight(year);
        BorderPane.setAlignment(year, javafx.geometry.Pos.CENTER);

        return clone;
    }

    private BorderPane createSkrinningItem(QuestionnaireResult result) {
        BorderPane box = new BorderPane();
        box.setId("dynamicSkrinningItem"); // Penanda untuk dibersihkan jika perlu
        box.setPrefHeight(66);
        box.setPrefWidth(1112);
        box.setStyle("-fx-background-color: #fff; -fx-background-radius: 14;");
        box.setPadding(new Insets(0, 30, 0, 30));

        // LEFT – Kategori
        Text title = new Text(result.getCategory());
        title.setFont(Font.font("System Bold", 14));
        StackPane left = new StackPane(title);
        StackPane.setAlignment(title, javafx.geometry.Pos.CENTER_LEFT);
        left.setPrefWidth(200);
        box.setLeft(left);

        // CENTER – Status
        long jumlahTrue = result.getQuestionAnswers().stream()
                .filter(QuestionAnswer::isAnswer)
                .count();
        String status = jumlahTrue >= 3 ? "Risiko Tinggi" : "Risiko Rendah";

        Text statusText = new Text(status);
        statusText.setFont(Font.font(14));
        HBox center = new HBox(statusText);
        center.setAlignment(javafx.geometry.Pos.CENTER);
        center.setPadding(new Insets(20, 0, 20, 0));
        box.setCenter(center);

        // RIGHT – Tanggal (dummy atau diambil dari result)
        Text dateText = new Text("2025"); // Bisa diganti sesuai waktu submit
        dateText.setFont(Font.font(14));
        box.setRight(dateText);
        BorderPane.setAlignment(dateText, javafx.geometry.Pos.CENTER);

        return box;
    }

    public void tampilkanSkriningStatik(User user) {
        if (user == null) return;

        // Hapus item lama (selain header dan warp_content)
        if (containerSkrinning.getChildren().size() > 2) {
            containerSkrinning.getChildren().remove(2, containerSkrinning.getChildren().size());
        }

        List<QuestionnaireResult> results = user.getQuestionnaireResults();

        if (results != null && !results.isEmpty()) {
            skrinningLabel.setVisible(true);
            skrinningLabel.setManaged(true);

            for (QuestionnaireResult result : results) {
                BorderPane item = createSkrinningItem(result);
                containerSkrinning.getChildren().add(item);
            }
        } else {
            skrinningLabel.setVisible(false);
            skrinningLabel.setManaged(false);

            Label kosongLabel = new Label("Belum ada data hasil skrining.");
            kosongLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #888;");
            kosongLabel.setAlignment(Pos.CENTER);
            kosongLabel.setMaxWidth(Double.MAX_VALUE);

            VBox.setVgrow(kosongLabel, Priority.ALWAYS);
            containerSkrinning.setAlignment(Pos.CENTER);
            containerSkrinning.getChildren().add(kosongLabel);
        }
    }

    @FXML
    private void handleAddHistory() {
        System.out.println("Tambah riwayat diklik");
        // Navigasi ke halaman form tambah
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
