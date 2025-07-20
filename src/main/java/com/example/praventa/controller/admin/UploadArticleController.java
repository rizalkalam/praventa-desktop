package com.example.praventa.controller.admin;

import com.example.praventa.controller.MainController;
import com.example.praventa.model.articles.Article;
import com.example.praventa.model.articles.ArticleListWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    private final File xmlFile = new File("D:\\Kuliah\\Project\\praventa\\data\\articles.xml");

    private SidebarAdminController sidebarController;

    public void setSidebarController(SidebarAdminController sidebarController) {
        this.sidebarController = sidebarController;
    }

    @FXML
    public void initialize() {
        inputImg.setOnAction(e -> handleSelectImage());
        btnPublish.setOnAction(e -> handlePublishArticle());
        btnCancel.setOnAction(e -> handleCancel());

        Article article = ArtikelCardAdminController.EditSession.getArticleToEdit();
        if (article != null) {
            setEditData(article);
            ArtikelCardAdminController.EditSession.clear(); // optional, agar tidak tertinggal
        }
    }

    private void handleSelectImage() {
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

    private void handlePublishArticle() {
        String judul = title.getText();
        String kategori = category.getText();
        String tanggal = date.getText();
        String summary = ringkasan.getText();
        String isi = desc.getText();

        if (judul.isEmpty() || tanggal.isEmpty() || isi.isEmpty()) {
            showAlert("Peringatan", "Judul, tanggal, dan konten tidak boleh kosong.");
            return;
        }

        String deskripsiGabungan = kategori + " - " + summary + "\n\n" + isi;

        List<Article> articles = new ArrayList<>();
        try {
            if (xmlFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(ArticleListWrapper.class);
                Unmarshaller um = context.createUnmarshaller();
                ArticleListWrapper wrapper = (ArticleListWrapper) um.unmarshal(xmlFile);
                articles = wrapper.getArticleList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isEditMode && editingArticle != null) {
            // Cari artikel lama dan update datanya
            for (Article art : articles) {
                if (art.getJudul().equals(editingArticle.getJudul()) &&
                        art.getTanggal().equals(editingArticle.getTanggal())) {
                    art.setJudul(judul);
                    art.setDeskripsi(deskripsiGabungan);
                    art.setTanggal(tanggal);
                    art.setImagePath(selectedImagePath);
                    break;
                }
            }
        } else {
            // Tambah artikel baru
            Article newArticle = new Article(tanggal, judul, deskripsiGabungan, selectedImagePath);
            articles.add(0, newArticle);
        }

        try {
            ArticleListWrapper wrapper = new ArticleListWrapper();
            wrapper.setArticleList(articles);

            JAXBContext context = JAXBContext.newInstance(ArticleListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(wrapper, xmlFile);

            showAlert("Sukses", isEditMode ? "Artikel berhasil diperbarui!" : "Artikel berhasil dipublikasikan!");

            if (sidebarController != null) {
                sidebarController.loadPage("artikel_admin.fxml");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Gagal menyimpan artikel ke file XML.");
        }
    }

    private void handleCancel() {
        if (sidebarController != null) {
            sidebarController.loadPage("artikel_admin.fxml");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isEditMode = false;
    private Article editingArticle;

    public void setEditData(Article article) {
        if (article == null) return;

        isEditMode = true;
        editingArticle = article;

        title.setText(article.getJudul());
        category.setText(extractCategory(article.getDeskripsi()));
        ringkasan.setText(extractRingkasan(article.getDeskripsi()));
        desc.setText(extractIsi(article.getDeskripsi()));
        date.setText(article.getTanggal());

        selectedImagePath = article.getImagePath();
        if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
            inputImg.setText(new File(selectedImagePath).getName());
        }
    }

    // Contoh ekstraksi sederhana
    private String extractCategory(String deskripsi) {
        if (deskripsi.contains("-")) {
            return deskripsi.split("-")[0].trim();
        }
        return "";
    }

    private String extractRingkasan(String deskripsi) {
        if (deskripsi.contains("-") && deskripsi.contains("\n")) {
            String[] parts = deskripsi.split("-");
            return parts[1].split("\n")[0].trim();
        }
        return "";
    }

    private String extractIsi(String deskripsi) {
        if (deskripsi.contains("\n\n")) {
            return deskripsi.split("\n\n")[1].trim();
        }
        return "";
    }
}

