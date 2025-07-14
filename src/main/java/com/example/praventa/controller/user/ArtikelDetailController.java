package com.example.praventa.controller.user;

import com.example.praventa.model.Artikel;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ArtikelDetailController {
    @FXML
    private ImageView imageView;
    @FXML private Text tanggalText;
    @FXML private Text judulText;
    @FXML private Text deskripsiText;

    public void setArtikel(Artikel artikel) {
        tanggalText.setText(artikel.getTanggal());
        judulText.setText(artikel.getJudul());
        deskripsiText.setText(artikel.getDeskripsi());

        Image image = new Image(getClass().getResource(artikel.getImagePath()).toExternalForm());
        imageView.setImage(image);
    }
}
