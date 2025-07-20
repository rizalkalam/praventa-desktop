package com.example.praventa.controller.admin;

import com.example.praventa.controller.MainController;
import com.example.praventa.utils.SceneUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class AddArtikelCardController {
    @FXML
    private VBox addArticleCard;

    private SidebarAdminController sidebarController;

    public void setSidebarController(SidebarAdminController sidebarController) {
        this.sidebarController = sidebarController;
    }

    @FXML
    private void handleAddArticle(MouseEvent event) {
        if (sidebarController != null) {
            sidebarController.loadPage("upload_artikel.fxml");
        } else {
            System.out.println("❌ Sidebar controller belum diset.");
        }
    }
}