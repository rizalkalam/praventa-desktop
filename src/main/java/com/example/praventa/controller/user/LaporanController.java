package com.example.praventa.controller.user;

import com.example.praventa.model.questionnaire.QuestionAnswer;
import com.example.praventa.model.questionnaire.QuestionnaireResult;
import com.example.praventa.model.questionnaire.RiskAnalysis;
import com.example.praventa.model.questionnaire.RiskData;
import com.example.praventa.model.users.*;
import com.example.praventa.service.GeminiService;
import com.example.praventa.utils.SceneUtil;
import com.example.praventa.utils.Session;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class LaporanController implements Initializable {
    @FXML private AnchorPane rootPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox contentPane;
    @FXML private ImageView closeButton;

    @FXML
    private Label statusResiko;

    @FXML
    private Label penyakitTerdeteksi1;
    @FXML
    private Label penyakitTerdeteksi2;
    @FXML
    private Label penyakitTerdeteksi3;

    @FXML
    private Label rekomendasiPemeriksaan1;
    @FXML
    private Label rekomendasiPemeriksaan2;
    @FXML
    private Label rekomendasiPemeriksaan3;

    @FXML
    private Label name;
    @FXML
    private Label age;
    @FXML
    private Label gender;
    @FXML
    private Label weight;
    @FXML
    private Label height;

    @FXML
    private VBox containerFamilyHistory;
    @FXML
    private BorderPane titleContainerFamilyHistory;

    @FXML private Label eat;
    @FXML private Label activity;
    @FXML private Label sleep;
    @FXML private Label not_sleep;
    @FXML private Label stress;
    @FXML private Label alcohol;
    @FXML private Label smoke;

    @FXML
    private VBox containerDiseaseRisk;

    @FXML private Button unduhBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            scrollPane.setVvalue(0.0); // scroll ke atas
        });

        closeButton.setCursor(Cursor.HAND);
        // Validasi apakah closeButton null atau tidak terlihat
        closeButton.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText("Batal eksport laporan?");
            alert.setContentText("Kembali ke beranda?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                SceneUtil.switchToMainPage(rootPane);
            }
        });

        User currentUser = Session.getCurrentUser();

        tampilkanRisikoTertinggi();
        tampilkanRekomendasiDariAI();
        if (currentUser != null) {
            tampilkanInformasiPribadi(currentUser);

            List<FamilyDiseaseHistory> familyDiseases = currentUser.getFamilyDiseaseHistoryList();
            loadFamilyHistory(familyDiseases);

            setLifestyleData(currentUser);

            setQuestionnaireResults(currentUser);
        }
    }

    private void tampilkanRisikoTertinggi() {
        try {
            File file = new File("D:\\Kuliah\\Project\\praventa\\data\\users.xml");
            UserListWrapper wrapper = AnalisisController.XmlUtils.loadFromXml(file, UserListWrapper.class);
            User user = Session.getCurrentUser();

            if (user != null && wrapper != null) {
                RiskAnalysis riskAnalysis = user.getRiskAnalysis();

                if (riskAnalysis != null && riskAnalysis.getRisks() != null) {
                    List<RiskData> sortedRisks = riskAnalysis.getRisks().stream()
                            .sorted(Comparator.comparingDouble(RiskData::getPercentage).reversed())
                            .limit(3)
                            .collect(Collectors.toList());

                    Label[] labels = {penyakitTerdeteksi1, penyakitTerdeteksi2, penyakitTerdeteksi3};

                    for (int i = 0; i < sortedRisks.size(); i++) {
                        labels[i].setText(sortedRisks.get(i).getName());
                    }

                    // Kosongkan label jika kurang dari 3
                    for (int i = sortedRisks.size(); i < 3; i++) {
                        labels[i].setText("");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            penyakitTerdeteksi1.setText("Gagal memuat data risiko.");
            penyakitTerdeteksi2.setText("");
            penyakitTerdeteksi3.setText("");
        }
    }

    private void tampilkanRekomendasiDariAI() {
        try {
            File file = new File("D:\\Kuliah\\Project\\praventa\\data\\users.xml");
            UserListWrapper wrapper = AnalisisController.XmlUtils.loadFromXml(file, UserListWrapper.class);
            User user = Session.getCurrentUser();

            if (user != null && wrapper != null) {
                RiskAnalysis riskAnalysis = user.getRiskAnalysis();

                if (riskAnalysis != null && riskAnalysis.getRisks() != null) {
                    List<RiskData> allRisks = riskAnalysis.getRisks();

                    // Hitung status lokal
                    String statusLokal = GeminiService.calculateRiskStatus(allRisks);

                    // Ambil 3 risiko tertinggi
                    List<RiskData> topRisks = allRisks.stream()
                            .sorted(Comparator.comparingDouble(RiskData::getPercentage).reversed())
                            .limit(3)
                            .collect(Collectors.toList());

                    StringBuilder risiko = new StringBuilder();
                    for (RiskData risk : topRisks) {
                        risiko.append("- ").append(risk.getName())
                                .append(" (").append(String.format("%.0f", risk.getPercentage() * 100)).append("%)\n");
                    }

                    // Dapatkan hasil dari Gemini
                    String hasil = GeminiService.generateMedicalCheckupRecommendation(risiko.toString());

                    // Pisahkan hasil menjadi baris
                    String[] lines = hasil.strip().split("\\r?\\n");
                    List<String> rekomendasi = new ArrayList<>();

                    // Ambil hanya 3 baris pertama rekomendasi (jika ada)
                    for (String line : lines) {
                        if (line.startsWith("- ")) {
                            rekomendasi.add(line.replaceFirst("^-\\s*", ""));
                            if (rekomendasi.size() == 3) break;
                        }
                    }

                    // Tampilkan rekomendasi
                    Label[] labels = {rekomendasiPemeriksaan1, rekomendasiPemeriksaan2, rekomendasiPemeriksaan3};
                    for (int i = 0; i < labels.length; i++) {
                        if (i < rekomendasi.size()) {
                            labels[i].setText(rekomendasi.get(i));
                        } else {
                            labels[i].setText("");
                        }
                    }

                    // Tampilkan status lokal sebagai status risiko
                    statusResiko.setText(statusLokal);

                    // Optional: Warnai label sesuai status
                    switch (statusLokal) {
                        case "Tinggi" -> {
                            statusResiko.setStyle("-fx-background-color: #FFD9D9; -fx-background-radius: 10; -fx-text-fill: RED;");
                        }
                        case "Sedang" -> {
                            statusResiko.setStyle("-fx-background-color: #FFF2CC; -fx-background-radius: 10; -fx-text-fill: #E69138;");
                        }
                        case "Rendah" -> {
                            statusResiko.setStyle("-fx-background-color: #D9FFD9; -fx-background-radius: 10; -fx-text-fill: GREEN;");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            rekomendasiPemeriksaan1.setText("Gagal memuat rekomendasi.");
        }
    }

    private void tampilkanInformasiPribadi(User user) {
        name.setText(user.getUsername());

        PersonalData personalData = user.getPersonalData();
        if (personalData != null) {
            age.setText(personalData.getAge() + " Tahun");
            gender.setText(personalData.getGender());

            if (personalData.getBodyMetrics() != null) {
                weight.setText(personalData.getBodyMetrics().getWeight() + " kg");
                height.setText(personalData.getBodyMetrics().getHeight() + " cm");
            } else {
                weight.setText("-");
                height.setText("-");
            }
        }
    }

    public void loadFamilyHistory(List<FamilyDiseaseHistory> diseaseList) {
        // Hapus semua elemen di bawah titleContainerFamilyHistory
        containerFamilyHistory.getChildren().removeIf(node -> node != titleContainerFamilyHistory);

        // Tambahkan ulang item penyakit berdasarkan data
        for (FamilyDiseaseHistory disease : diseaseList) {
            BorderPane item = new BorderPane();
            item.setPrefHeight(45.0);
            item.setPrefWidth(983.0);
            item.setStyle("-fx-background-color: #ffff; -fx-background-radius: 12;");
            item.setPadding(new Insets(0, 12, 0, 12));

            // Label kiri: nama penyakit (bold)
            Label labelDisease = new Label(disease.getDiseaseName());
            labelDisease.setFont(Font.font("System", FontWeight.BOLD, 16));
            BorderPane.setAlignment(labelDisease, Pos.CENTER);
            item.setLeft(labelDisease);

            // Label tengah: hubungan keluarga
            Label labelRelation = new Label("(" + disease.getRelation() + ")");
            labelRelation.setFont(Font.font(16));
            BorderPane.setAlignment(labelRelation, Pos.CENTER);
            item.setCenter(labelRelation);

            // Label kanan: usia
            Label labelAge = new Label(disease.getDiagnosedAge() + " Tahun");
            labelAge.setFont(Font.font(16));
            BorderPane.setAlignment(labelAge, Pos.CENTER);
            item.setRight(labelAge);

            containerFamilyHistory.getChildren().add(item);
        }
    }

    public void setLifestyleData(User user) {
        LifestyleData lifestyle = user.getLifestyleData();
        if (lifestyle != null) {

            // Fast food
            Integer freq = lifestyle.getFastFoodFrequency();
            eat.setText(freq != null ? "Fast food " + freq + "x/minggu" : "-");

            // Aktivitas fisik
            String activityValue = lifestyle.getPhysicalActivity();
            activity.setText(activityValue != null ? capitalize(activityValue) : "-");

            // Sleep habits (List<String>)
            List<String> sleepHabits = lifestyle.getSleepHabits();
            if (sleepHabits != null && !sleepHabits.isEmpty()) {
                // Gabungkan semua kebiasaan tidur jadi 1 string
                String combined = String.join(", ", sleepHabits);
                sleep.setText(combined);
                not_sleep.setText(sleepHabits.stream().anyMatch(h -> h.toLowerCase().contains("begadang")) ? "Sering begadang" : "Tidak begadang");
            } else {
                sleep.setText("-");
                not_sleep.setText("-");
            }

            // Stres
            Integer stressValue = lifestyle.getStressLevel();
            stress.setText(stressValue != null ? "Level " + stressValue : "-");

            // Alkohol
            String alcoholValue = lifestyle.getAlcoholHabit();
            alcohol.setText(alcoholValue != null ? capitalize(alcoholValue) : "-");

            // Merokok
            String smokingValue = lifestyle.getSmokingHabit();
            smoke.setText(smokingValue != null ? capitalize(smokingValue) : "-");

        } else {
            // Kosongkan jika tidak ada data
            eat.setText("-");
            activity.setText("-");
            sleep.setText("-");
            not_sleep.setText("-");
            stress.setText("-");
            alcohol.setText("-");
            smoke.setText("-");
        }
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public void setQuestionnaireResults(User user) {
        containerDiseaseRisk.getChildren().clear();

        if (user.getQuestionnaireResults() != null) {
            for (QuestionnaireResult result : user.getQuestionnaireResults()) {
                // Hitung jumlah jawaban "true"
                long countYes = result.getQuestionAnswers().stream()
                        .filter(QuestionAnswer::isAnswer)
                        .count();

                if (countYes > 3) {
                    // Label kategori penyakit
                    Label diseaseLabel = new Label(result.getCategory());
                    diseaseLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
                    containerDiseaseRisk.getChildren().add(diseaseLabel);

                    // Tampilkan setiap pertanyaan dan jawaban
                    for (QuestionAnswer qa : result.getQuestionAnswers()) {
                        BorderPane pane = new BorderPane();
                        pane.setPrefHeight(45);
                        pane.setPrefWidth(983);

                        Label questionLabel = new Label(qa.getQuestion());
                        questionLabel.setFont(Font.font(16));
                        BorderPane.setAlignment(questionLabel, Pos.CENTER_LEFT);
                        pane.setLeft(questionLabel);

                        String answerText = qa.isAnswer() ? "Ya" : "Tidak";
                        Label answerLabel = new Label(answerText);
                        answerLabel.setFont(Font.font(16));
                        BorderPane.setAlignment(answerLabel, Pos.CENTER_RIGHT);
                        pane.setRight(answerLabel);

                        containerDiseaseRisk.getChildren().add(pane);
                    }

                    // Spacer
                    Region spacer = new Region();
                    spacer.setMinHeight(15);
                    containerDiseaseRisk.getChildren().add(spacer);
                }
            }
        }
    }

    @FXML
    private void handleUnduhLaporan(ActionEvent event) {
        try {
            // Sembunyikan elemen yang tidak ingin masuk PDF
            closeButton.setVisible(false);
            unduhBtn.setVisible(false);

            // Paksa layout agar update sebelum snapshot
            contentPane.applyCss();
            contentPane.layout();

            double width = contentPane.getWidth();
            double height = contentPane.getHeight();

            WritableImage image = new WritableImage((int) width, (int) height);
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            contentPane.snapshot(parameters, image);

            // Tampilkan kembali elemen UI
            closeButton.setVisible(true);
            unduhBtn.setVisible(true);

            // Konversi ke BufferedImage
            BufferedImage fullImage = SwingFXUtils.fromFXImage(image, null);

            // Dialog untuk pilih lokasi penyimpanan
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Simpan Laporan PDF");
            fileChooser.setInitialFileName("LaporanPraventa.pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File pdfFile = fileChooser.showSaveDialog(scrollPane.getScene().getWindow());

            if (pdfFile != null) {
                // Buat dokumen PDF
                Document document = new Document(PageSize.A1);
                PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
                document.open();

                float pdfWidth = PageSize.A1.getWidth();
                float pdfHeight = PageSize.A1.getHeight();

                int imageHeight = fullImage.getHeight();
                int imageWidth = fullImage.getWidth();
                int y = 0;

                while (y < imageHeight) {
                    int sliceHeight = Math.min((int) pdfHeight, imageHeight - y);
                    BufferedImage subImage = fullImage.getSubimage(0, y, imageWidth, sliceHeight);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(subImage, "png", baos);
                    Image pdfImage = Image.getInstance(baos.toByteArray());

                    pdfImage.scaleToFit(pdfWidth, pdfHeight);
                    document.add(pdfImage);

                    y += sliceHeight;
                    if (y < imageHeight) {
                        document.newPage();
                    }
                }

                document.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Laporan berhasil disimpan sebagai PDF.");
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menyimpan laporan.");
            alert.showAndWait();
        }
    }

}
