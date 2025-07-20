package com.example.praventa.controller.user;

import com.example.praventa.model.articles.Article;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;

public class ArtikelCardController {
    @FXML
    private Text tanggalText;
    @FXML private Label judulText;
    @FXML private Label deskripsiText;
    @FXML private ImageView imageView;

    public void setData(Article artikel) {
        judulText.setText(artikel.getJudul());
        deskripsiText.setText(artikel.getDeskripsi());
        tanggalText.setText(artikel.getTanggal());

        try {
            String imagePath = artikel.getImagePath();
            Image image;

            if (imagePath != null) {
                File file = new File(imagePath);
                if (file.exists()) {
                    image = new Image(file.toURI().toString());
                } else {
                    // Jika bukan file lokal, coba dari resource
                    URL url = getClass().getResource(imagePath);
                    if (url != null) {
                        image = new Image(url.toString());
                    } else {
                        System.out.println("Gambar tidak ditemukan: " + imagePath);
                        // fallback default
                        image = new Image(getClass().getResource("/assets/icn_article_default.png").toString());
                    }
                }
                imageView.setImage(image);
            }

        } catch (Exception e) {
            System.out.println("Gagal set gambar artikel: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
