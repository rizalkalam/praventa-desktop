package com.example.praventa.controller.user;

import com.example.praventa.model.articles.Article;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

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

        Image image = new Image(getClass().getResource(artikel.getImagePath()).toExternalForm());
        imageView.setImage(image);
    }

    @FXML
    private void handleBackArticle(MouseEvent event) {
        if (sidebarController != null) {
            sidebarController.handleNavArticleClick(); // akan menampilkan artikel.fxml dan aktifkan menu artikel
        }
    }
}
