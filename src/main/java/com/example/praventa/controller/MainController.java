package com.example.praventa.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    @FXML
    private VBox navContainer;

    @FXML
    private AnchorPane mainContent;

    public void initialize() {
        try {
            // Muat sidebar.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/sidebar.fxml"));
            Parent sidebar = loader.load();

            // Ambil controller sidebar, lalu inject mainContent
            SidebarController navController = loader.getController();
            navController.setMainContent(mainContent);

            // Tambahkan sidebar ke navContainer
            navContainer.getChildren().add(sidebar);

            // Tampilkan halaman default: home
            navController.handleNavHomeClick(); // Harus public!

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
