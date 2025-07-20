package com.example.praventa.controller.pakar;

import com.example.praventa.controller.user.AnalisisController;
import com.example.praventa.model.users.User;
import com.example.praventa.model.users.UserListWrapper;
import com.example.praventa.utils.XmlUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.w3c.dom.Node;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class LaporanPakarController implements Initializable {

    private User selectedUser;

    public void setUser(User user) {
        this.selectedUser = user;
    }

    @FXML private Text usernameLabel;
    @FXML private Text tanggalLabel;
    @FXML private Button validateButton;
    @FXML private VBox laporanContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            if (selectedUser != null) {
                usernameLabel.setText(selectedUser.getUsername());
                tanggalLabel.setText(LocalDate.now().toString());

                validateButton.setVisible(true);
                openPDFInBrowser(selectedUser);
            } else {
                validateButton.setVisible(false);
            }
        });
    }

    private void openPDFInBrowser(User user) {
        try {
            String username = user.getUsername().toLowerCase().replaceAll("\\s+", "");
            File pdfFile = new File("D:/Kuliah/Project/praventa/data/docs/" + username + "_laporan.pdf");

            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile); // Open in Chrome/default PDF app
            } else {
                showAlert("File Tidak Ditemukan", "Laporan untuk user '" + user.getUsername() + "' tidak ditemukan.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Gagal Membuka PDF", e.getMessage());
        }
    }

    @FXML
    private void handleValidasi() {
        if (selectedUser != null) {
            try {
                // Load users.xml
                File file = new File("D:/Kuliah/Project/praventa/data/users.xml");
                UserListWrapper wrapper = AnalisisController.XmlUtils.loadFromXml(file, UserListWrapper.class);

                // Cari user dan update validasi
                for (User user : wrapper.getUsers()) {
                    if (user.getUsername().equals(selectedUser.getUsername())) {
                        user.setValidasi("valid");
                        break;
                    }
                }

                // Simpan kembali ke file
                XmlUtils.saveUsers(wrapper, file);

                // Tampilkan alert sukses
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Validasi Berhasil");
                alert.setHeaderText(null);
                alert.setContentText("Laporan untuk " + selectedUser.getUsername() + " berhasil divalidasi.");
                alert.showAndWait();

                // Tutup jendela jika mau
                Stage stage = (Stage) laporanContainer.getScene().getWindow();
                stage.close();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Gagal Validasi", "Terjadi kesalahan saat memvalidasi laporan.");
            }
        }
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) laporanContainer.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
