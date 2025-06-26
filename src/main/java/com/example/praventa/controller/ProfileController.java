package com.example.praventa.controller;

import com.example.praventa.model.User;
import com.example.praventa.util.Database;
import com.example.praventa.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
//import javafx.scene.shape.Path;
import javafx.stage.FileChooser;

import javax.print.DocFlavor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProfileController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private Circle avatarCircle;

    @FXML private Button saveButton;

    private final String assetDir = "src/main/resources/assets/";
    private final String relativeAssetDir = "assets/";
    private SidebarController sidebarController;

    @FXML
    public void initialize() {
        loadProfilePicture();
    }

    private void loadProfilePicture() {
        User user = Session.getCurrentUser();
        String path = user.getProfilePicture();

        if (path == null || path.isEmpty() || !(new File(path).exists())) {
            path = "src/main/resources/assets/icn_profile_default.png"; // fallback ke default
        }

        try {
            File file = new File(path);
            System.out.println("Memuat foto dari: " + file.getAbsolutePath());

            Image image = new Image(new FileInputStream(file));
            avatarCircle.setFill(new ImagePattern(image));
        } catch (Exception e) {
            System.out.println("Gagal load foto profil: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setSidebarController(SidebarController sidebarController) {
        this.sidebarController = sidebarController;
    }

    @FXML
    private void handleSave(ActionEvent event) {
        User user = Session.getCurrentUser();

        if (user == null) {
            System.out.println("User tidak ditemukan di session.");
            return;
        }

        String newName = nameField.getText();
        String newEmail = emailField.getText();
        String newPhone = phoneField.getText();

        // Validasi sederhana
        if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty()) {
            System.out.println("Semua field harus diisi.");
            return;
        }

        // Update ke database
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE users SET username = ?, email = ?, phone_number = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newName);
            stmt.setString(2, newEmail);
            stmt.setString(3, newPhone);
            stmt.setInt(4, user.getId()); // pastikan User punya id

            int updated = stmt.executeUpdate();

            if (updated > 0) {
                System.out.println("✅ Profil berhasil diperbarui.");

                // Update session
                user.setUsername(newName);
                user.setEmail(newEmail);
                user.setPhoneNumber(newPhone);
                Session.setCurrentUser(user);

                // Popup berhasil
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Berhasil");
                alert.setHeaderText(null);
                alert.setContentText("Profil berhasil diperbarui!");
                alert.showAndWait();
            } else {
                System.out.println("⚠️ Gagal memperbarui profil.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Kesalahan saat update profil: " + e.getMessage());
            // Popup gagal
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gagal");
            alert.setHeaderText("Kesalahan saat memperbarui data");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    private void setDefaultAvatar() {
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream("/com/example/praventa/assets/icn_profile_default.png"));
            avatarCircle.setFill(new ImagePattern(defaultImage));
        } catch (Exception ex) {
            System.out.println("Gagal menampilkan default avatar: " + ex.getMessage());
        }
    }


    @FXML
    private void handleChangePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Profil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Ambil user saat ini
                User user = Session.getCurrentUser();

                // Tentukan folder penyimpanan eksternal
                String userHome = System.getProperty("user.home");
                Path assetDir = Paths.get(userHome, "praventa_assets");
                Files.createDirectories(assetDir); // Pastikan folder ada

                // Hapus foto lama jika ada dan bukan default
                String oldPath = user.getProfilePicture();
                if (oldPath != null && !oldPath.isEmpty()) {
                    File oldFile = new File(oldPath);
                    if (oldFile.exists() && oldFile.getParentFile().equals(assetDir.toFile())) {
                        avatarCircle.setFill(null); // Lepas image pattern dulu
                        System.gc(); // Paksa garbage collector (kadang perlu)
                        boolean deleted = oldFile.delete();
                        System.out.println("File lama dihapus: " + deleted);
                    }
                }

                // Generate nama file unik
                String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
                String newFileName = "profile_" + System.currentTimeMillis() + extension;
                Path destinationPath = assetDir.resolve(newFileName);

                // Salin file ke folder eksternal
                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File berhasil disalin ke: " + destinationPath.toAbsolutePath());

                // Simpan path lengkap ke DB
                String savedPath = destinationPath.toString();

                // Update di database
                try (Connection conn = Database.getConnection()) {
                    String sql = "UPDATE users SET profile_picture = ? WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, savedPath);
                    stmt.setInt(2, user.getId());
                    stmt.executeUpdate();

                    user.setProfilePicture(savedPath); // Update di session juga
                }

                // Tampilkan ke UI
                Image image = new Image(new FileInputStream(destinationPath.toFile()));
                avatarCircle.setFill(new ImagePattern(image));
                if (sidebarController != null) {
                    sidebarController.refreshAvatar();
                }

                // Notifikasi
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Berhasil");
                alert.setHeaderText(null);
                alert.setContentText("Foto profil berhasil diperbarui!");
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Gagal mengubah foto profil");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleDeletePhoto() {
        // Path default image
        String defaultImagePath = "assets/icn_profile_default.png";

        try (Connection conn = Database.getConnection()) {
            User user = Session.getCurrentUser();

            // Update DB
            String sql = "UPDATE users SET profile_picture = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, defaultImagePath);
            stmt.setInt(2, user.getId());
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                // Update session user
                user.setProfilePicture(defaultImagePath);

                // Update UI
                Image defaultImage = new Image(getClass().getResourceAsStream("/com/example/praventa/" + defaultImagePath));
                avatarCircle.setFill(new ImagePattern(defaultImage));

                // Popup sukses
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Berhasil");
                alert.setHeaderText(null);
                alert.setContentText("Foto profil telah dihapus dan diganti ke default.");
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Gagal menghapus foto profil");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
