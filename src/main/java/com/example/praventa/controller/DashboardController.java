package com.example.praventa.controller;

import com.example.praventa.controller.user.SidebarController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class DashboardController {

    @FXML
    private VBox navContainer;

    @FXML
    private AnchorPane mainContent;

    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sidebar.fxml"));
            Parent sidebar = loader.load();

            // Ambil controller sidebar dan inject kontainer utama
            SidebarController sidebarController = loader.getController();
            sidebarController.setMainContent(mainContent); // <- inject dulu

            navContainer.getChildren().add(sidebar);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
