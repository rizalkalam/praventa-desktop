package com.example.praventa.controller.user;

import com.example.praventa.utils.SceneUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DiseaseRiskController implements Initializable {
    @FXML private AnchorPane rootPane;
    @FXML
    private ImageView closeButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        closeButton.setCursor(Cursor.HAND);
        // Validasi apakah closeButton null atau tidak terlihat
        closeButton.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText("Kembali ke beranda?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                SceneUtil.switchToMainPage(rootPane);
            }
        });
    }
}
