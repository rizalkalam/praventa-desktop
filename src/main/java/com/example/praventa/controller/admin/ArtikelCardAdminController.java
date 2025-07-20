package com.example.praventa.controller.admin;

import com.example.praventa.model.articles.Article;
import com.example.praventa.model.articles.ArticleListWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.example.praventa.utils.Session.sidebarController;

public class ArtikelCardAdminController {
    @FXML
    private ImageView imageView;

    @FXML
    private Text tanggalText;

    @FXML
    private Label judulText;

    @FXML
    private Label deskripsiText;
    private Article currentArticle;

    public void setData(Article artikel) {
        if (artikel == null) return;

        this.currentArticle = artikel;

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

    @FXML
    private void handleEditArticle(MouseEvent event) {
        if (sidebarController != null) {
            EditSession.setArticleToEdit(currentArticle);
            sidebarController.loadPage("upload_artikel.fxml");
        } else {
            System.out.println("❌ Sidebar controller belum diset.");
        }
    }

    public class EditSession {
        private static Article articleToEdit;

        public static void setArticleToEdit(Article article) {
            EditSession.articleToEdit = article;
        }

        public static Article getArticleToEdit() {
            return articleToEdit;
        }

        public static void clear() {
            articleToEdit = null;
        }
    }

    @FXML
    private void handleDeleteArticle(MouseEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi");
        confirm.setHeaderText("Hapus Artikel");
        confirm.setContentText("Apakah kamu yakin ingin menghapus artikel ini?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteArticleFromXML();
        }
    }

    private void deleteArticleFromXML() {
        try {
            File file = new File("D:\\Kuliah\\Project\\praventa\\data\\articles.xml");
            JAXBContext context = JAXBContext.newInstance(ArticleListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();
            ArticleListWrapper wrapper = (ArticleListWrapper) um.unmarshal(file);
            List<Article> list = wrapper.getArticleList();

            list.removeIf(a -> a.getJudul().equals(currentArticle.getJudul())
                    && a.getTanggal().equals(currentArticle.getTanggal()));

            wrapper.setArticleList(list);

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(wrapper, file);

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Sukses");
            info.setHeaderText(null);
            info.setContentText("Artikel berhasil dihapus.");
            info.showAndWait();

            // Reload halaman
            if (sidebarController != null) {
                sidebarController.loadPage("artikel_admin.fxml");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText(null);
            error.setContentText("Gagal menghapus artikel.");
            error.showAndWait();
        }
    }
}