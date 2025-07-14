package com.example.praventa.controller.user;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/sidebar.fxml"));
            Parent sidebar = loader.load();

            SidebarController navController = loader.getController();
            navController.setMainContent(mainContent);

            navContainer.getChildren().add(sidebar);

            // Tampilkan halaman default (bisa beranda, riwayat, dsb)
            navController.loadPage(defaultPage); // <-- ini loadPage langsung tanpa mengubah highlight

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDefaultPage(String pageName) {
        this.defaultPage = pageName;
    }
}
