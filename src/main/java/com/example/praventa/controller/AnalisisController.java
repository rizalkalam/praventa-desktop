package com.example.praventa.controller;

import com.example.praventa.model.questionnaire.RiskAnalysis;
import com.example.praventa.model.questionnaire.RiskData;
import com.example.praventa.model.users.User;
import com.example.praventa.model.users.UserListWrapper;
import com.example.praventa.utils.Session;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AnalisisController {

    @FXML private PieChart pieChart;
    @FXML private Label centerLabel;
    @FXML private VBox legendBox;
    @FXML private StackPane donutContainer;
    @FXML private VBox riskBox;

    private User loadedUser;

    @FXML
    public void initialize() {
        makeDonut();

        try {
            File file = new File("D:\\Kuliah\\Project\\praventa\\data\\users.xml");
            UserListWrapper wrapper = XmlUtils.loadFromXml(file, UserListWrapper.class);

            User currentUser = Session.getCurrentUser();
            if (currentUser != null) {
                loadedUser = wrapper.getUserByUsername(currentUser.getUsername());

                if (loadedUser != null && loadedUser.getRiskAnalysis() != null) {
                    RiskAnalysis analysis = loadedUser.getRiskAnalysis();

                    List<RiskData> risks = analysis.getRisks();
                    if (risks != null && !risks.isEmpty()) {
                        setChartDataFromRisks(risks);
                        showDiseaseRiskBars();
                    } else {
                        showFallbackDiseaseBars();
                    }
                } else {
                    showFallbackDiseaseBars();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showFallbackDiseaseBars();
        }

        pieChart.getData().addListener((ListChangeListener<PieChart.Data>) change -> updateCenterTextFromChart());
    }

    public void setChartDataFromRisks(List<RiskData> risks) {
        pieChart.getData().clear();

        // Buat salinan list agar bisa disort
        List<RiskData> sortedRisks = new ArrayList<>(risks);

        // Urutkan berdasarkan persentase menurun
        sortedRisks.sort((a, b) -> Double.compare(b.getPercentage(), a.getPercentage()));

        // Ambil hanya 3 risiko tertinggi
        List<RiskData> top3Risks = sortedRisks.subList(0, Math.min(3, sortedRisks.size()));

        List<PieChart.Data> pieDataList = new ArrayList<>();

        for (RiskData risk : top3Risks) {
            double percentValue = risk.getPercentage() * 100;
            PieChart.Data data = new PieChart.Data(
                    risk.getName(),
                    percentValue
            );
            pieDataList.add(data);
        }

        pieChart.getData().addAll(pieDataList);

        Platform.runLater(() -> {
            for (int i = 0; i < pieDataList.size(); i++) {
                PieChart.Data data = pieDataList.get(i);
                String color = top3Risks.get(i).getColor();

                data.getNode().setStyle("-fx-pie-color: " + color + ";");
                data.getNode().setUserData(new PieMeta(data.getName(), color));
            }

            updateCenterTextFromChart();
        });
    }

    public void showDiseaseRiskBars() {
        riskBox.getChildren().clear();
        double barWidth = 179;

        if (loadedUser == null || loadedUser.getRiskAnalysis() == null) return;

        List<RiskData> risks = loadedUser.getRiskAnalysis().getRisks();

        for (RiskData risk : risks) {
            Label label = new Label(risk.getName());
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #000000;");

            StackPane barBackground = new StackPane();
            barBackground.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 10;");
            barBackground.setPrefSize(barWidth, 14);

            Region fillBar = new Region();
            fillBar.setStyle("-fx-background-color: " + risk.getColor() + "; -fx-background-radius: 10;");
            fillBar.setPrefWidth(barWidth * risk.getPercentage());
            fillBar.setPrefHeight(14);

            Region marker = new Region();
            marker.setPrefSize(6, 14);
            marker.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 3;");
            marker.setTranslateX((barWidth * risk.getPercentage()) - 3);

            barBackground.getChildren().addAll(fillBar, marker);

            VBox wrapper = new VBox(6, label, barBackground);
            wrapper.setPadding(new Insets(5, 0, 5, 0));
            wrapper.setAlignment(Pos.CENTER_LEFT);

            riskBox.getChildren().add(wrapper);
        }
    }

    public void showFallbackDiseaseBars() {
        List<RiskData> fallbackRisks = List.of(
                new RiskData("Diabetes Tipe 2", 0.5, "#4FC3F7"),
                new RiskData("Hipertensi", 0.8, "#FF7043"),
                new RiskData("Penyakit Jantung", 0.4, "#3F51B5"),
                new RiskData("Stroke", 0.6, "#EF5350"),
                new RiskData("Kanker Serviks", 0.3, "#81C784")
        );
        loadedUser = new User();
        loadedUser.setRiskAnalysis(new RiskAnalysis(fallbackRisks, new ArrayList<>()));
        setChartDataFromRisks(fallbackRisks);
        showDiseaseRiskBars();
    }

    private void makeDonut() {
        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(false);

        Circle innerCircle = new Circle();
        innerCircle.setFill(Color.WHITE);
        innerCircle.radiusProperty().bind(
                Bindings.min(donutContainer.widthProperty(), donutContainer.heightProperty()).divide(3.2)
        );

        centerLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        StackPane.setAlignment(innerCircle, Pos.CENTER);
        StackPane.setAlignment(centerLabel, Pos.CENTER);

        donutContainer.getChildren().addAll(innerCircle, centerLabel);
    }

    private void updateCenterTextFromChart() {
        if (pieChart == null || centerLabel == null) return;

        double totalRisiko = 100.0;
        double totalTop3 = pieChart.getData().stream()
                .mapToDouble(PieChart.Data::getPieValue)
                .sum();

        legendBox.getChildren().clear();

        for (PieChart.Data data : pieChart.getData()) {
            PieMeta meta = (PieMeta) data.getNode().getUserData();
            if (meta == null || meta.name.equalsIgnoreCase("Tidak Berisiko")) continue;

            double percent = data.getPieValue(); // Sudah dalam skala 0–100

            Tooltip tooltip = new Tooltip(String.format("%s: %.0f%%", meta.name, percent));
            Tooltip.install(data.getNode(), tooltip);

            Region colorBox = new Region();
            colorBox.setPrefSize(12, 12);
            colorBox.setStyle("-fx-background-color: " + meta.colorHex + ";");

            Label label = new Label(meta.name);
            Label percentLabel = new Label(String.format("%.0f%%", percent));
            label.setStyle("-fx-font-size: 14px;");
            percentLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + meta.colorHex + ";");

            HBox legendItem = new HBox(10, colorBox, label, percentLabel);
            legendItem.setAlignment(Pos.CENTER_LEFT);

            legendBox.getChildren().add(legendItem);
        }

        centerLabel.setText("Top 3");
    }

    public static class PieMeta {
        public final String name;
        public final String colorHex;

        public PieMeta(String name, String colorHex) {
            this.name = name;
            this.colorHex = colorHex;
        }
    }

    public static class XmlUtils {
        public static <T> T loadFromXml(File file, Class<T> clazz) throws Exception {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(file);
        }
    }

    @FXML
    private void handleQestionnaire(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/input_Kuisioner.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Input Gaya Hidup - Praventa");
            stage.setScene(scene);
            stage.setMaximized(true);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExport(MouseEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/laporan_detail.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Laporan");
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