package com.example.praventa.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AnalisisController {

    @FXML private PieChart pieChart;
    @FXML private Label centerLabel;
    @FXML private VBox legendBox;
    @FXML private StackPane donutContainer;
    @FXML private VBox riskBox;

    private final String[] pieColors = {"#00E0DC", "#6A67CE", "#FFB347"}; // custom color set

    @FXML
    public void initialize() {
        makeDonut();
        setChartData(40, 20, 13); // contoh data
        pieChart.getData().addListener((ListChangeListener<PieChart.Data>) change -> updateCenterTextFromChart());
        showDiseaseRiskBars();
    }

    public void setChartData(double diabetes, double serviks, double jantung) {
        pieChart.getData().clear();

        PieChart.Data data1 = new PieChart.Data("Diabetes", diabetes);
        PieChart.Data data2 = new PieChart.Data("Kanker Serviks", serviks);
        PieChart.Data data3 = new PieChart.Data("Jantung", jantung);

        pieChart.getData().addAll(data1, data2, data3);

        Platform.runLater(() -> {
            PieChart.Data[] dataArr = {data1, data2, data3};

            for (int i = 0; i < dataArr.length; i++) {
                PieChart.Data d = dataArr[i];
                String color = pieColors[i % pieColors.length];
                d.getNode().setStyle("-fx-pie-color: " + color + ";");
                d.getNode().setUserData(new PieMeta(d.getName(), color));
            }

            updateCenterTextFromChart();
        });
    }

    private void makeDonut() {
        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(false);

        Circle innerCircle = new Circle();
        innerCircle.setFill(Color.WHITE);

        // Perbesar ukuran lingkaran dalam
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

        double total = pieChart.getData().stream()
                .mapToDouble(PieChart.Data::getPieValue)
                .sum();

        if (total == 0) {
            centerLabel.setText("0%");
            return;
        }

        legendBox.getChildren().clear();

        for (PieChart.Data data : pieChart.getData()) {
            PieMeta meta = (PieMeta) data.getNode().getUserData();
            if (meta == null) continue;

            double value = data.getPieValue();
            double percent = (value / total) * 100;
            data.setName(String.format("%s (%.0f%%)", meta.name, percent));

            Region colorBox = new Region();
            colorBox.setPrefSize(12, 12);
            colorBox.setStyle("-fx-background-color: " + meta.colorHex + "; -fx-background-radius: 2px;");

            Label label = new Label(meta.name);
            Label percentLabel = new Label(String.format("%.0f%%", percent));
            label.setStyle("-fx-font-size: 14px;");
            percentLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + meta.colorHex + ";");

            HBox legendItem = new HBox(10, colorBox, label, percentLabel);
            legendItem.setAlignment(Pos.CENTER_LEFT);

            legendBox.getChildren().add(legendItem);
        }

        centerLabel.setText(String.format("%.0f%%", total));
    }

    // Class untuk menyimpan nama dan warna
    public static class PieMeta {
        public final String name;
        public final String colorHex;

        public PieMeta(String name, String colorHex) {
            this.name = name;
            this.colorHex = colorHex;
        }
    }

    // Data penyakit dan nilainya
    private static class RiskData {
        String name;
        double percentage; // 0.0 to 1.0
        String color;

        RiskData(String name, double percentage, String color) {
            this.name = name;
            this.percentage = percentage;
            this.color = color;
        }
    }

    public void showDiseaseRiskBars() {
        List<RiskData> risks = List.of(
                new RiskData("Diabetes Tipe 2", 0.5, "#4FC3F7"),
                new RiskData("Hipertensi", 0.8, "#FF7043"),
                new RiskData("Penyakit Jantung", 0.4, "#3F51B5"),
                new RiskData("Stroke", 0.6, "#EF5350"),
                new RiskData("Kanker Serviks", 0.3, "#81C784")
        );

        riskBox.getChildren().clear();

        double barWidth = 179; // sesuai prefWidth riskBox (219) - padding kiri kanan (20 + 20)

        for (RiskData risk : risks) {
            Label label = new Label(risk.name);
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #000000;");

            StackPane barBackground = new StackPane();
            barBackground.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 10;");
            barBackground.setPrefHeight(14);
            barBackground.setPrefWidth(barWidth);
            barBackground.setMaxWidth(barWidth);
            barBackground.setMinWidth(barWidth);

            Region fillBar = new Region();
            fillBar.setStyle("-fx-background-color: " + risk.color + "; -fx-background-radius: 10;");
            fillBar.setPrefHeight(14);
            fillBar.setPrefWidth(barWidth * risk.percentage);

            Region marker = new Region();
            marker.setPrefSize(6, 14);
            marker.setStyle("-fx-background-color:  #F5F5F5; -fx-background-radius: 3;");
            marker.setTranslateX((barWidth * risk.percentage) - 3);

            barBackground.getChildren().addAll(fillBar, marker);

            VBox wrapper = new VBox(6, label, barBackground);
            wrapper.setPadding(new Insets(5, 0, 5, 0));
            wrapper.setAlignment(Pos.CENTER_LEFT);

            riskBox.getChildren().add(wrapper);
        }
    }
}