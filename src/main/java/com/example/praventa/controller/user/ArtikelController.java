package com.example.praventa.controller.user;

import com.example.praventa.model.articles.Article;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ArtikelController implements Initializable {
    @FXML private GridPane grid;
    @FXML private SidebarController sidebarController;

    private List<Article> artikelList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        artikelList = loadArticlesFromXML("D:\\Kuliah\\Project\\praventa\\data\\articles.xml");

        int column = 0;
        int row = 0;

        try {
            for (Article artikel : artikelList) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/artikel_card.fxml"));
                VBox card = fxmlLoader.load();

                ArtikelCardController controller = fxmlLoader.getController();
                controller.setData(artikel);

                grid.add(card, column++, row);

                card.setOnMouseClicked(event -> openDetailPage(artikel));

                if (column == 3) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Article> loadArticlesFromXML(String filePath) {
        List<Article> articles = new ArrayList<>();
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("article");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) nNode;

                    String tanggal = elem.getElementsByTagName("tanggal").item(0).getTextContent();
                    String judul = elem.getElementsByTagName("judul").item(0).getTextContent();
                    String deskripsi = elem.getElementsByTagName("deskripsi").item(0).getTextContent();
                    String imagePath = elem.getElementsByTagName("imagePath").item(0).getTextContent();

                    articles.add(new Article(tanggal, judul, deskripsi, imagePath));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }

    public void setSidebarController(SidebarController sidebarController) {
        this.sidebarController = sidebarController;
    }

    private void openDetailPage(Article artikel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/artikel_detail.fxml"));
            Parent root = loader.load();

            // Kirim data artikel ke controller detail
            ArtikelDetailController controller = loader.getController();
            controller.setArtikel(artikel);

            // Ganti scene
            Stage stage = (Stage) grid.getScene().getWindow(); // Ambil stage dari komponen apa pun
            if (sidebarController != null) {
                sidebarController.loadDetailArtikelPage(artikel);
            } else {
                System.err.println("SidebarController belum di-set!");
            }
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
