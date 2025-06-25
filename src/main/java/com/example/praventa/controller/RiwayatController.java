package com.example.praventa.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class RiwayatController {

    @FXML
    private VBox navContainer;

    @FXML
    private AnchorPane mainContent;

    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/main.fxml"));
            Parent sidebar = loader.load();

            SidebarController controller = loader.getController();
            controller.setMainContent(mainContent); // inject mainContent dari luar

            navContainer.getChildren().add(sidebar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
