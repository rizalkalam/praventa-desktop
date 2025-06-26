package com.example.praventa.controller;

import com.example.praventa.model.User;
import com.example.praventa.util.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;


public class SidebarController {
    @FXML private AnchorPane mainContent;

    @FXML private Circle avatarCircle;
    @FXML private Text usernameText;
    @FXML private Text statusText;

    @FXML private BorderPane navHome;
    @FXML private BorderPane navRiwayat;
    @FXML private BorderPane navAnalisis;
    @FXML private BorderPane navArtikel;

    @FXML private ImageView iconHome;
    @FXML private ImageView iconRiwayat;
    @FXML private ImageView iconAnalisis;
    @FXML private ImageView iconArtikel;

    @FXML private javafx.scene.text.Text textHome;
    @FXML private javafx.scene.text.Text textRiwayat;
    @FXML private javafx.scene.text.Text textAnalisis;
    @FXML private javafx.scene.text.Text textArtikel;


    @FXML private Rectangle rectHome;
    @FXML private Rectangle rectRiwayat;
    @FXML private Rectangle rectAnalisis;
    @FXML private Rectangle rectArtikel;

    private AnchorPane contentTarget;

    public void setMainContent(AnchorPane contentTarget) {
        this.contentTarget = contentTarget;

        // Ambil user dari session
        User user = Session.getCurrentUser();
        if (user != null) {
            usernameText.setText(user.getUsername());
            statusText.setText(user.getRole());

            // Set profile picture
            try {
                String profilePicPath = user.getProfilePicture(); // misal: "assets/farid.jpg"
                Image img = new Image(getClass().getResourceAsStream("/com/example/praventa/" + profilePicPath));
                avatarCircle.setFill(new ImagePattern(img));
            } catch (Exception e) {
                e.printStackTrace(); // fallback kalau gambar gagal
            }
        }

        // Default menu aktif
        handleNavHomeClick();
    }

    private void setActiveMenu(BorderPane activeNav, ImageView activeIcon, Rectangle activeRect, String activeIconName) {
        // Reset ikon
        iconHome.setImage(loadIcon("icn_home.png"));
        iconRiwayat.setImage(loadIcon("icn_riwayat.png"));
        iconAnalisis.setImage(loadIcon("icn_analisis.png"));
        iconArtikel.setImage(loadIcon("icn_artikel.png"));

        // Reset rectangle
        rectHome.setVisible(false);
        rectRiwayat.setVisible(false);
        rectAnalisis.setVisible(false);
        rectArtikel.setVisible(false);

        // Reset warna teks ke default (hitam)
        textHome.setFill(Color.BLACK);
        textRiwayat.setFill(Color.BLACK);
        textAnalisis.setFill(Color.BLACK);
        textArtikel.setFill(Color.BLACK);

        // Aktifkan menu
        activeIcon.setImage(loadIcon(activeIconName));
        activeRect.setFill(Color.web("#9E91E1"));
        activeRect.setVisible(true);

        // Ubah warna teks menu aktif
        if (activeIcon == iconHome) textHome.setFill(Color.web("#9E91E1"));
        else if (activeIcon == iconRiwayat) textRiwayat.setFill(Color.web("#9E91E1"));
        else if (activeIcon == iconAnalisis) textAnalisis.setFill(Color.web("#9E91E1"));
        else if (activeIcon == iconArtikel) textArtikel.setFill(Color.web("#9E91E1"));
    }

    private Image loadIcon(String fileName) {
        return new Image(getClass().getResourceAsStream("/com/example/praventa/assets/" + fileName));
    }

    public void handleNavHomeClick() {
        setActiveMenu(navHome, iconHome, rectHome, "icn_home_active.png");
        loadPage("beranda.fxml");
    }

    public void handleNavRiwayatClick() {
        setActiveMenu(navRiwayat, iconRiwayat, rectRiwayat, "icn_riwayat_active.png");
        loadPage("riwayat.fxml");
    }

    public void handleNavAnalysisClick() {
        setActiveMenu(navAnalisis, iconAnalisis, rectAnalisis, "icn_analisis_active.png");
        loadPage("analisis.fxml");
    }

    public void handleNavArticleClick() {
        setActiveMenu(navArtikel, iconArtikel, rectArtikel, "icn_artikel_active.png");
        loadPage("artikel.fxml");
    }

    private void loadPage(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/" + fxmlName));
            Parent content = loader.load();
            contentTarget.getChildren().setAll(content);
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}