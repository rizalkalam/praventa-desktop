package com.example.praventa.controller;

import com.example.praventa.model.Artikel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ArtikelController implements Initializable {
    @FXML private GridPane grid;
    @FXML private SidebarController sidebarController;

    private List<Artikel> artikelList = List.of(
            new Artikel("Interior Design , 16 Jun 2025", "Diabetes - Gejala, Penyebab, dan Pencegahan",
                    "Diabetes tipe 2 merupakan salah satu penyakit metabolik yang dapat dicegah dengan perubahan gaya hidup yang sehat. Salah satu langkah utama dalam pencegahan adalah memperbaiki pola makan. Mengonsumsi lebih banyak sayur dan buah secara rutin memberikan tubuh asupan serat, vitamin, dan antioksidan yang penting untuk menjaga kestabilan gula darah. Sayur dan buah juga membantu memperlambat penyerapan glukosa ke dalam darah, sehingga mengurangi risiko lonjakan gula darah secara tiba-tiba. Disarankan untuk memilih buah yang rendah indeks glikemik seperti apel, pir, atau buah beri, serta memperbanyak sayuran hijau seperti bayam, brokoli, dan sawi.\n" +
                            "\n" +
                            "Selain itu, penting untuk membatasi konsumsi gula dan garam dalam kehidupan sehari-hari. Gula tambahan yang berlebihan dapat menyebabkan resistensi insulin, yang merupakan salah satu pemicu utama diabetes tipe 2. Produk-produk seperti minuman manis, kue, dan makanan olahan sebaiknya dikurangi atau dihindari. Begitu juga dengan asupan garam yang tinggi dapat meningkatkan tekanan darah, yang kerap menjadi komplikasi pada penderita diabetes. Mengurangi makanan tinggi sodium seperti makanan cepat saji, mie instan, dan camilan asin juga berperan besar dalam menjaga kesehatan metabolik secara keseluruhan. Dengan konsistensi dalam pola makan sehat ini, risiko terkena diabetes tipe 2 dapat ditekan secara signifikan.",
                    "/assets/img_article1.png"),
            new Artikel("Lifestyle, 17 Jun 2025", "Pentingnya Tidur yang Cukup",
                    "Tidur cukup membantu regenerasi tubuh, meningkatkan daya tahan, dan menjaga mood.",
                    "/assets/img_article1.png"),
            new Artikel("Kesehatan, 18 Jun 2025", "Makanan Sehat Harian",
                    "Pilih makanan tinggi serat, protein, dan rendah gula untuk keseimbangan nutrisi.",
                    "/assets/img_article1.png")
    );

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int column = 0;
        int row = 0;

        try {
            for (Artikel artikel : artikelList) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/artikel_card.fxml"));
                VBox card = fxmlLoader.load();

                ArtikelCardController controller = fxmlLoader.getController();
                controller.setData(artikel);

                // Tambahkan ke grid
                grid.add(card, column++, row);

                // Tambahkan handler klik
                card.setOnMouseClicked(event -> openDetailPage(artikel));

                // Atur baris
                if (column == 3) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSidebarController(SidebarController sidebarController) {
        this.sidebarController = sidebarController;
    }

    private void openDetailPage(Artikel artikel) {
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
