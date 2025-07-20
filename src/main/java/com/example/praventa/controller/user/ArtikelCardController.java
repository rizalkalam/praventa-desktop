package com.example.praventa.controller.user;

import com.example.praventa.model.articles.Article;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ArtikelCardController {
    @FXML
    private Text tanggalText;
    @FXML private Label judulText;
    @FXML private Label deskripsiText;
    @FXML private ImageView imageView;

    public void setData(Article artikel) {
        tanggalText.setText(artikel.getTanggal());
        judulText.setText(artikel.getJudul());
        deskripsiText.setText(artikel.getDeskripsi());
        Image image = new Image(getClass().getResource(artikel.getImagePath()).toExternalForm());
        imageView.setImage(image);
    }
}
