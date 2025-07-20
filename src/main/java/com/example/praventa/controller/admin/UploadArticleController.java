package com.example.praventa.controller.admin;

import com.example.praventa.model.articles.Article;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

public class UploadArticleController {
    @FXML
    private TextField title;

    @FXML
    private TextField category;

    @FXML
    private TextField date;

    @FXML
    private TextField ringkasan;

    @FXML
    private TextField desc;

    @FXML
    private Button inputImg;

    @FXML
    private Button btnPublish;

    @FXML
    private Button btnCancel;

    private String selectedImagePath;

    private ArtikelAdminController adminController;

    public void setAdminController(ArtikelAdminController adminController) {
        this.adminController = adminController;
    }

    @FXML
    public void initialize() {
        inputImg.setOnAction(e -> handleChooseImage());
        btnPublish.setOnAction(this::handlePublish);
        btnCancel.setOnAction(e -> clearForm());
    }

    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Gambar Artikel");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            inputImg.setText(selectedFile.getName());
        }
    }

    private void handlePublish(ActionEvent event) {
        if (title.getText().isEmpty() || category.getText().isEmpty()
                || date.getText().isEmpty() || ringkasan.getText().isEmpty()
                || desc.getText().isEmpty() || selectedImagePath == null) {
            System.out.println("Mohon lengkapi semua bidang sebelum publikasi.");
            return;
        }

//        Article newArticle = new Article();
//        newArticle.setJudul(title.getText());
//        newArticle.setTanggal(date.getText());
//        newArticle.setDeskripsi(ringkasan.getText());
//        newArticle.setImagePath(selectedImagePath); // path lokal gambar

//        if (adminController != null) {
//            adminController.addArticle(newArticle);
//        }

        clearForm(); // kosongkan form
    }

    private void clearForm() {
        title.clear();
        category.clear();
        date.clear();
        ringkasan.clear();
        desc.clear();
        selectedImagePath = null;
        inputImg.setText("max (10 mb)");
    }
}
