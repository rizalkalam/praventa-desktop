package com.example.praventa.controller.user;

import com.example.praventa.model.articles.Article;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.File;
import java.net.URL;

public class ArtikelDetailController {
    @FXML
    private ImageView imageView;
    @FXML private Text tanggalText;
    @FXML private Text judulText;
    @FXML private Text deskripsiText;
    private SidebarController sidebarController;

    public void setSidebarController(SidebarController sidebarController) {
        this.sidebarController = sidebarController;
    }

    public void setArtikel(Article artikel) {
        tanggalText.setText(artikel.getTanggal());
        judulText.setText(artikel.getJudul());
        deskripsiText.setText(artikel.getDeskripsi());

        String imagePath = artikel.getImagePath();
        Image image;

        try {
            if (imagePath.startsWith("/") || imagePath.startsWith("com")) {
                URL resource = getClass().getResource(imagePath);
                if (resource != null) {
                    image = new Image(resource.toExternalForm());
                } else {
                    System.err.println("Gambar tidak ditemukan di resource: " + imagePath);
                    image = new Image("/com/example/praventa/assets/icn_default_article.jpg"); // fallback
                }
            } else {
                File file = new File(imagePath);
                if (file.exists()) {
                    image = new Image(file.toURI().toString());
                } else {
                    System.err.println("Gambar tidak ditemukan di path lokal: " + imagePath);
                    image = new Image("/com/example/praventa/assets/icn_default_article.jpg"); // fallback
                }
            }

            imageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackArticle(MouseEvent event) {
        if (sidebarController != null) {
            sidebarController.handleNavArticleClick(); // akan menampilkan artikel.fxml dan aktifkan menu artikel
        }
    }
}
