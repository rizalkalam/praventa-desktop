package com.example.praventa.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ResourceBundle;

public class AnalisisController implements Initializable {
    @FXML
    private StackPane chartContainer;

    @FXML
    private PieChart pieChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupDonutChart();
    }

    private void setupDonutChart() {
        // Clear existing children di chartContainer
        chartContainer.getChildren().clear();

        // Buat PieChart baru untuk donut
        PieChart donutChart = new PieChart();

        // Data sesuai dengan gambar - perhatikan urutan dan nilai
        PieChart.Data diabetesData = new PieChart.Data("Diabetes", 49);
        PieChart.Data kankerData = new PieChart.Data("Kanker Serviks", 20);
        PieChart.Data jantungData = new PieChart.Data("Jantung", 13);
        // Tambahkan data tersembunyi untuk bagian yang kosong (27%)
        PieChart.Data emptyData = new PieChart.Data("Empty", 18);

        donutChart.getData().addAll(diabetesData, kankerData, jantungData, emptyData);

        // Konfigurasi chart
        donutChart.setTitle("");
        donutChart.setLabelsVisible(false);
        donutChart.setLegendVisible(false);
        donutChart.setStartAngle(90); // Mulai dari atas
        donutChart.setClockwise(true);
        donutChart.setPrefSize(200, 200);
        donutChart.setMaxSize(200, 200);
        donutChart.setMinSize(200, 200);

        // Circle untuk efek donut - lebih besar
        Circle innerCircle = new Circle(45);
        innerCircle.setFill(Color.web("#ffff")); // Sama dengan background
        innerCircle.setStroke(Color.TRANSPARENT);

        // Label 73% di tengah
        Label centerLabel = new Label("73%");
        centerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        centerLabel.setTextFill(Color.web("#4A4A4A"));

        // Stack untuk chart + circle + label
        StackPane chartStack = new StackPane();
        chartStack.getChildren().addAll(donutChart, innerCircle, centerLabel);
        chartStack.setPrefSize(200, 200);
        chartStack.setAlignment(Pos.CENTER);

        // Legend
        VBox legendBox = createCustomLegend();

        // Layout utama - vertikal
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPrefWidth(302);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(chartStack, legendBox);

        // Tambahkan ke container
        chartContainer.getChildren().add(mainLayout);

        // Apply colors setelah chart di-render
        Platform.runLater(() -> {
            setChartColors(donutChart);
        });
    }

    private void setChartColors(PieChart chart) {
        // Apply CSS class ke chart
        chart.getStyleClass().add("custom-pie-chart");

        // Hilangkan animasi untuk hasil yang lebih konsisten
        chart.setAnimated(false);

        // Set warna untuk setiap data
        for (int i = 0; i < chart.getData().size(); i++) {
            final int index = i;
            PieChart.Data data = chart.getData().get(i);

            // Set warna langsung jika node sudah ada
            if (data.getNode() != null) {
                applyColorToNode(data.getNode(), index);
            }

            // Listener untuk node yang belum tersedia
            data.nodeProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    applyColorToNode(newValue, index);
                }
            });
        }
    }

    private void applyColorToNode(javafx.scene.Node node, int index) {
        String color = "";
        switch (index) {
            case 0: // Diabetes - Turquoise/Cyan
                color = "#59FFE6";
                break;
            case 1: // Kanker Serviks - Yellow/Gold
                color = "#FFD700";
                break;
            case 2: // Jantung - Red
                color = "#FF4757";
                break;
            case 3: // Empty space - sama dengan background
                color = "#B8B5FF";
                // Atau bisa juga dibuat transparan
                // node.setVisible(false);
                break;
        }

        // Set style dengan warna yang tepat
        node.setStyle("-fx-pie-color: " + color + "; -fx-border-color: transparent; -fx-border-width: 0;");
    }

    private VBox createCustomLegend() {
        VBox legendBox = new VBox(12);
        legendBox.setAlignment(Pos.CENTER_LEFT);
        legendBox.setPadding(new Insets(0, 20, 0, 20));
        legendBox.setMaxWidth(250);

        // Diabetes
        HBox diabetesRow = createLegendRow("●", "#40E0D0", "Diabetes", "49%");

        // Kanker Serviks
        HBox kankerRow = createLegendRow("●", "#FFD700", "Kanker Serviks", "20%");

        // Jantung
        HBox jantungRow = createLegendRow("●", "#FF4757", "Jantung", "13%");

        legendBox.getChildren().addAll(diabetesRow, kankerRow, jantungRow);
        return legendBox;
    }

    private HBox createLegendRow(String bullet, String color, String label, String percentage) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPrefWidth(200);

        // Bullet point dengan warna - lebih besar
        Label bulletLabel = new Label(bullet);
        bulletLabel.setTextFill(Color.web(color));
        bulletLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        bulletLabel.setPrefWidth(25);

        // Label teks
        Label textLabel = new Label(label);
        textLabel.setTextFill(Color.WHITE);
        textLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        textLabel.setPrefWidth(120);

        // Persentase dengan warna yang sama seperti bullet
        Label percentLabel = new Label(percentage);
        percentLabel.setTextFill(Color.web(color));
        percentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        percentLabel.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(percentLabel, Priority.ALWAYS);

        row.getChildren().addAll(bulletLabel, textLabel, percentLabel);
        return row;
    }
}
