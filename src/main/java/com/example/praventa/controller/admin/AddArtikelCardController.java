package com.example.praventa.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AddArtikelCardController {
    @FXML
    private Button btnAddArtikel;

    private ArtikelAdminController adminController;

    public void setAdminController(ArtikelAdminController controller) {
        this.adminController = controller;
    }

    @FXML
    public void initialize() {
//        btnAddArtikel.setOnAction(e -> {
//            if (adminController != null) {
//                adminController.openUploadForm();
//            }
//        });
    }
}
