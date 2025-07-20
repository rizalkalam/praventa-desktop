package com.example.praventa.controller.admin;

import com.example.praventa.model.articles.Article;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.File;

public class ArtikelCardAdminController {

    @FXML
    private ImageView imageView;

    @FXML
    private Text tanggalText;

    @FXML
    private Label judulText;

    @FXML
    private Label deskripsiText;

    public void setData(Article artikel) {
        if (artikel == null) return;

        tanggalText.setText(artikel.getTanggal());
        judulText.setText(artikel.getJudul());
        deskripsiText.setText(artikel.getDeskripsi());

        try {
            String imagePath = artikel.getImagePath();
            File imageFile = new File(imagePath);

            if (imageFile.exists()) {
                imageView.setImage(new Image(imageFile.toURI().toString()));
            } else {
                imageView.setImage(new Image(getClass().getResource("/assets/icn_article_default.png").toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}