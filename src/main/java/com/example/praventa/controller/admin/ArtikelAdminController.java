package com.example.praventa.controller.admin;

import com.example.praventa.controller.MainController;
import com.example.praventa.controller.user.ArtikelCardController;
import com.example.praventa.controller.user.ArtikelDetailController;
import com.example.praventa.controller.user.SidebarController;
import com.example.praventa.model.articles.Article;
import com.example.praventa.model.articles.ArticleListWrapper;
import com.example.praventa.utils.Session;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ArtikelAdminController implements Initializable {

    @FXML
    private GridPane grid;

    private List<Article> artikelList;

    private MainController mainController;

    public void setMainController(MainController controller) {
        this.mainController = controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadArticlesFromXml();

        try {
            // Ambil sidebar controller dari session
            SidebarAdminController sidebarController = (SidebarAdminController) Session.getSidebarController();

            // 1. Tambahkan kartu 'Tambah Artikel' sebagai elemen pertama
            FXMLLoader addLoader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/fxmlAdmin/add_artikel_card.fxml"));
            VBox addCard = addLoader.load();

            // Ambil controller dan inject sidebar controller
            AddArtikelCardController addCardController = addLoader.getController();
            if (sidebarController != null) {
                addCardController.setSidebarController(sidebarController);
            } else {
                System.out.println("❌ Sidebar controller belum tersedia di session.");
            }

            grid.add(addCard, 0, 0); // Kolom 0 baris 0

            int column = 1; // Mulai dari kolom 1
            int row = 0;

            // 2. Tambahkan artikel dari XML
            for (Article artikel : artikelList) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/fxmlAdmin/artikel_card_admin.fxml"));
                VBox card = fxmlLoader.load();

                ArtikelCardAdminController controller = fxmlLoader.getController();
                controller.setData(artikel);

                grid.add(card, column++, row);

                if (column == 3) {
                    column = 0;
                    row++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadArticlesFromXml() {
        try {
            File file = new File("D:\\Kuliah\\Project\\praventa\\data\\articles.xml");
            JAXBContext context = JAXBContext.newInstance(ArticleListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();
            ArticleListWrapper wrapper = (ArticleListWrapper) um.unmarshal(file);
            artikelList = wrapper.getArticleList();
        } catch (Exception e) {
            e.printStackTrace();
            artikelList = List.of(); // fallback jika gagal load
        }
    }
}

