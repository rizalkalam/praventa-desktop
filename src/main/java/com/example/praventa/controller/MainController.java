package com.example.praventa.controller;

import com.example.praventa.model.users.User;
import com.example.praventa.utils.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MainController {
    @FXML
    private VBox navContainer;

    @FXML
    private AnchorPane mainContent;

    private String defaultPage = "beranda.fxml";

    public void initialize() {
        try {
            User currentUser = Session.getCurrentUser();
            if (currentUser == null) return;

            // Pilih FXML sidebar berdasarkan role
            FXMLLoader loader;
            String role = currentUser.getRole();

            switch (role.toLowerCase()) {
                case "pasien":
                    loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/sidebar.fxml"));
                    break;
                case "pakar":
                    loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/sidebar_pakar.fxml"));
                    break;
                case "admin":
                    loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/fxmlAdmin/sidebarAdmin.fxml"));
                    break;
                default:
                    throw new IllegalArgumentException("Role tidak dikenali: " + role);
            }

            Parent sidebar = loader.load();

            BaseSidebarController sidebarController = loader.getController();
            sidebarController.setMainContent(mainContent);

            navContainer.getChildren().add(sidebar);

            // Tampilkan halaman default
            sidebarController.loadPage(defaultPage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDefaultPage(String pageName) {
        this.defaultPage = pageName;
    }
}
