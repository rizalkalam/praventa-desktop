package com.example.praventa.controller.admin;

import com.example.praventa.controller.BaseSidebarController;
import com.example.praventa.controller.user.ArtikelDetailController;
import com.example.praventa.model.articles.Article;
import com.example.praventa.model.users.User;
import com.example.praventa.utils.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public class SidebarAdminController extends BaseSidebarController {
    @FXML private AnchorPane mainContent;

    @FXML private Circle avatarCircle;
    @FXML private Text usernameText;
    @FXML private Text statusText;

    @FXML private BorderPane navHome;
    @FXML private BorderPane navArtikel;
    @FXML private BorderPane navAkun;
    @FXML private BorderPane navKeluar;

    @FXML private ImageView iconHome;
    @FXML private ImageView iconArtikel;
    @FXML private ImageView iconAkun;
    @FXML private ImageView iconKeluar;

    @FXML private javafx.scene.text.Text textHome;
    @FXML private javafx.scene.text.Text textArtikel;
    @FXML private javafx.scene.text.Text textAkun;
    @FXML private javafx.scene.text.Text textKeluar;


    @FXML private Rectangle rectHome;
    @FXML private Rectangle rectArtikel;
    @FXML private Rectangle rectAkun;
    @FXML private Rectangle rectKeluar;

    private AnchorPane contentTarget;

    @FXML public void initialize() {
        Session.setSidebarController(this);
    }

    public void setContent(Parent node) {
        mainContent.getChildren().clear();
        mainContent.getChildren().add(node);
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

    public void setMainContent(AnchorPane contentTarget) {
        this.contentTarget = contentTarget;

        // Ambil user dari session
        User user = Session.getCurrentUser();
        if (user != null) {
            usernameText.setText(user.getUsername());
            statusText.setText(user.getRole());

            // Set profile picture
            try {
                String profilePicPath = user.getProfilePicture();
                File imageFile;

                if (profilePicPath != null && new File(profilePicPath).exists()) {
                    imageFile = new File(profilePicPath); // Absolute path
                } else {
                    imageFile = new File("src/main/resources/assets/icn_profile_default.png");
                }

                System.out.println("Final path used: " + imageFile.getAbsolutePath());

                Image img = new Image(new FileInputStream(imageFile));
                avatarCircle.setFill(new ImagePattern(img));

            } catch (Exception e) {
                System.out.println("Gagal load foto profil: " + e.getMessage());
                e.printStackTrace();
            }

        }

        // Default menu aktif
        handleNavHomeClick();
    }

    private void setActiveMenu(BorderPane activeNav, ImageView activeIcon, Rectangle activeRect, String activeIconName) {
        // Reset ikon
        iconHome.setImage(loadIcon("icn_home.png"));
        iconArtikel.setImage(loadIcon("icn_artikel.png"));
        iconAkun.setImage(loadIcon("icn_akun.png"));
        iconKeluar.setImage(loadIcon("icn_logout.png"));

        // Reset rectangle
        rectHome.setVisible(false);
        rectArtikel.setVisible(false);
        rectAkun.setVisible(false);
        rectKeluar.setVisible(false);

        // Reset warna teks ke default (hitam)
        textHome.setFill(Color.BLACK);
        textArtikel.setFill(Color.BLACK);
        textAkun.setFill(Color.BLACK);
        textKeluar.setFill(Color.web("#ff0000"));

        // Aktifkan menu
        activeIcon.setImage(loadIcon(activeIconName));
        activeRect.setFill(Color.web("#9E91E1"));
        activeRect.setVisible(true);

        // Ubah warna teks menu aktif
        if (activeIcon == iconHome) textHome.setFill(Color.web("#9E91E1"));
        else if (activeIcon == iconArtikel) textArtikel.setFill(Color.web("#9E91E1"));
        else if (activeIcon == iconAkun) textAkun.setFill(Color.web("#9E91E1"));
        else if (activeIcon == iconKeluar) textKeluar.setFill(Color.web("#9E91E1"));
    }

    private Image loadIcon(String fileName) {
        return new Image(getClass().getResourceAsStream("/com/example/praventa/assets/" + fileName));
    }

    public void handleNavHomeClick() {
        setActiveMenu(navHome, iconHome, rectHome, "icn_home_active.png");
        loadPage("beranda_admin.fxml");
    }

    public void handleNavArticleClick() {
        setActiveMenu(navArtikel, iconArtikel, rectArtikel, "icn_artikel_active.png");
        loadPage("artikel_admin.fxml");
    }

    public void handleNavAkunClick() {
        setActiveMenu(navAkun, iconAkun, rectAkun, "icn_akun_active.png");
        loadPage("profil.fxml");
    }

    public void handleNavKeluarClick() {
        // Konfirmasi terlebih dahulu
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText("Anda yakin ingin keluar?");
        alert.setContentText("Anda akan kembali ke halaman login.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Hapus session user
                Session.setCurrentUser(null);

                // Muat kembali halaman login.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/login.fxml"));
                Parent root = loader.load();

                // Dapatkan stage saat ini dari node (mainContent atau contentTarget)
                Stage stage = (Stage) contentTarget.getScene().getWindow();
                Scene scene = new Scene(root, 1200, 700);
                stage.setScene(scene);
                stage.setTitle("Praventa - Login");
                stage.setResizable(false);
                stage.centerOnScreen();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadPage(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/fxmlAdmin/" + fxmlName));
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

    public void loadDetailArtikelPage(Article artikel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/artikel_detail.fxml"));
            AnchorPane detailPage = loader.load();

            ArtikelDetailController controller = loader.getController();
            controller.setArtikel(artikel);

            contentTarget.getChildren().setAll(detailPage);  // Ganti dari mainContent
            AnchorPane.setTopAnchor(detailPage, 0.0);
            AnchorPane.setBottomAnchor(detailPage, 0.0);
            AnchorPane.setLeftAnchor(detailPage, 0.0);
            AnchorPane.setRightAnchor(detailPage, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void refreshProfileData() {
        User user = Session.getCurrentUser();
        if (user != null) {
            usernameText.setText(user.getUsername());
            statusText.setText(user.getRole());

            try {
                String profilePicPath = user.getProfilePicture();
                Image image;

                if (profilePicPath != null && !profilePicPath.isEmpty()) {
                    if (profilePicPath.startsWith("assets/")) {
                        image = new Image(getClass().getResourceAsStream("/com/example/praventa/" + profilePicPath));
                    } else {
                        image = new Image(new FileInputStream(profilePicPath));
                    }
                } else {
                    image = new Image(getClass().getResourceAsStream("/com/example/praventa/assets/icn_profile_default.png"));
                }

                avatarCircle.setFill(new ImagePattern(image));
            } catch (Exception e) {
                System.out.println("Gagal memuat ulang data profil: " + e.getMessage());
            }
        }
    }

    public AnchorPane getMainContent() {
        return contentTarget;
    }
}
