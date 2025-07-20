package com.example.praventa.controller.user;

import com.example.praventa.model.questionnaire.RiskData;
import com.example.praventa.model.users.User;
import com.example.praventa.model.users.UserListWrapper;
import com.example.praventa.utils.SceneUtil;
import com.example.praventa.utils.Session;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DiseaseRiskController implements Initializable {
    @FXML private AnchorPane rootPane;
    @FXML
    private ImageView closeButton;

    @FXML private Label countDisease;
    @FXML private Label predictedDisease;

    private User loadedUser;

    @FXML private VBox containerFoodRekomendation;
    @FXML private VBox containerSleepRekomendation;
    @FXML private VBox containerActivityRekomendation;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        closeButton.setCursor(Cursor.HAND);
        closeButton.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText("Kembali ke beranda?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                SceneUtil.switchToMainPage(rootPane);
            }
        });

        loadUserRiskData();
        loadRecommendation();
    }

    private void loadUserRiskData() {
        try {
            File file = new File("D:/Kuliah/Project/praventa/data/users.xml");
            UserListWrapper wrapper = AnalisisController.XmlUtils.loadFromXml(file, UserListWrapper.class);
            User currentUser = Session.getCurrentUser();

            if (currentUser != null) {
                loadedUser = wrapper.getUserByUsername(currentUser.getUsername());

                if (loadedUser != null && loadedUser.getRiskAnalysis() != null) {
                    List<RiskData> risks = loadedUser.getRiskAnalysis().getRisks();

                    // Ambil risiko tertinggi
                    RiskData highestRisk = risks.stream()
                            .max(Comparator.comparingDouble(RiskData::getPercentage))
                            .orElse(null);

                    if (highestRisk != null) {
                        double percentage = highestRisk.getPercentage() * 100;
                        countDisease.setText(String.format("%.0f%%", percentage));

                        String kategori = getKategoriResiko(highestRisk.getPercentage());
                        predictedDisease.setText(kategori);

                        // Set background warna berdasarkan kategori
                        switch (kategori) {
                            case "Tinggi":
                                predictedDisease.setStyle("-fx-background-color: #F44336; -fx-background-radius: 18;");
                                break;
                            case "Sedang":
                                predictedDisease.setStyle("-fx-background-color: #FFEB3B; -fx-background-radius: 18;");
                                break;
                            case "Rendah":
                                predictedDisease.setStyle("-fx-background-color: #8BC34A; -fx-background-radius: 18;");
                                break;
                        }

                    } else {
                        countDisease.setText("0%");
                        predictedDisease.setText("-");
                        predictedDisease.setStyle("-fx-background-color: #BDBDBD; -fx-background-radius: 18;");
                    }

                } else {
                    countDisease.setText("0%");
                    predictedDisease.setText("-");
                    predictedDisease.setStyle("-fx-background-color: #BDBDBD; -fx-background-radius: 18;");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            countDisease.setText("0%");
            predictedDisease.setText("-");
            predictedDisease.setStyle("-fx-background-color: #BDBDBD; -fx-background-radius: 18;");
        }
    }

    private String getKategoriResiko(double persen) {
        if (persen >= 0.8) return "Tinggi";
        else if (persen >= 0.5) return "Sedang";
        else return "Rendah";
    }

    private void loadRecommendation() {
        if (loadedUser != null && loadedUser.getRecommendation() != null) {
            String rekomendasi = loadedUser.getRecommendation();
            String[] sections = rekomendasi.split("Rekomendasi ");

            for (String section : sections) {
                if (section.trim().isEmpty()) continue;

                String[] lines = section.split(":\n");
                if (lines.length < 2) continue;

                String title = lines[0].trim(); // Makan, Tidur, Aktivitas
                String[] tips = lines[1].split("\n");

                VBox targetContainer = switch (title.toLowerCase()) {
                    case "makan" -> containerFoodRekomendation;
                    case "tidur" -> containerSleepRekomendation;
                    case "aktivitas" -> containerActivityRekomendation;
                    default -> null;
                };

                if (targetContainer != null) {
                    // Kosongkan isi default kecuali icon dan label
                    targetContainer.getChildren().removeIf(node -> node instanceof Text);

                    for (String tip : tips) {
                        if (tip.trim().isEmpty()) continue;
                        Text tipText = new Text(tip.trim());
                        tipText.setFont(Font.font(16));
                        targetContainer.getChildren().add(tipText);
                    }
                }
            }
        }
    }

}
