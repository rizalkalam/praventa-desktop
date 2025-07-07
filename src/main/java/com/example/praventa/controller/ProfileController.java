package com.example.praventa.controller;

import com.example.praventa.model.User;
import com.example.praventa.repository.UserRepository;
import com.example.praventa.utils.Database;
import com.example.praventa.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
//import javafx.scene.shape.Path;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProfileController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private Circle avatarCircle;
    @FXML private Button saveButton;

    private SidebarController sidebarController;

    @FXML
    public void initialize() {
        refreshFields();
        loadProfilePicture();
    }

    public void setSidebarController(SidebarController sidebarController) {
        this.sidebarController = sidebarController;
    }

    private void refreshFields() {
        User user = Session.getCurrentUser();
        nameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhoneNumber());
    }

    private void loadProfilePicture() {
        User user = Session.getCurrentUser();
        String path = user.getProfilePicture();

        try {
            Image image;
            if (path.startsWith("assets")) {
                image = new Image(getClass().getResourceAsStream("/com/example/praventa/" + path));
            } else {
                image = new Image(new FileInputStream(path));
            }
            avatarCircle.setFill(new ImagePattern(image));
        } catch (Exception e) {
            System.out.println("Gagal load foto profil: " + e.getMessage());
            setDefaultAvatar();
        }
    }

    private void setDefaultAvatar() {
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream("/com/example/praventa/assets/icn_profile_default.jpg"));
            avatarCircle.setFill(new ImagePattern(defaultImage));
        } catch (Exception e) {
            System.out.println("Gagal menampilkan default avatar: " + e.getMessage());
        }
    }

    private void loadUserFromDatabase() {
        try (Connection conn = Database.getConnection()) {
            User current = Session.getCurrentUser();
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, current.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User updatedUser = new User();
                updatedUser.setId(rs.getInt("id"));
                updatedUser.setUsername(rs.getString("username"));
                updatedUser.setEmail(rs.getString("email"));
                updatedUser.setPhoneNumber(rs.getString("phone_number"));
                updatedUser.setProfilePicture(rs.getString("profile_picture"));

                Session.setCurrentUser(updatedUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUserFromXML() {
        User current = Session.getCurrentUser();
        List<User> users = UserRepository.loadUsers();
        for (User u : users) {
            if (u.getId() == current.getId()) {
                Session.setCurrentUser(u); // replace dengan yang terbaru
                break;
            }
        }
    }


    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSave(ActionEvent event) {
        User user = Session.getCurrentUser();

        String newName = nameField.getText();
        String newEmail = emailField.getText();
        String newPhone = phoneField.getText();

        if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Semua field harus diisi.");
            return;
        }

        try {
            // Perbarui data user
            user.setUsername(newName);
            user.setEmail(newEmail);
            user.setPhoneNumber(newPhone);

            boolean updated = UserRepository.updateUser(user);
            if (updated) {
                loadUserFromXML();
                refreshFields();

                if (sidebarController != null) {
                    sidebarController.refreshProfileData();
                }

                showAlert(Alert.AlertType.INFORMATION, "Profil berhasil diperbarui!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal memperbarui data.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal memperbarui profil: " + e.getMessage());
        }
    }

    @FXML
    private void handleChangePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Profil");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                User user = Session.getCurrentUser();

                // Direktori penyimpanan
                String userHome = System.getProperty("user.home");
                Path assetDir = Paths.get(userHome, "praventa_assets");
                Files.createDirectories(assetDir);

                // Hapus gambar lama jika ada (selain default)
                String oldPath = user.getProfilePicture();
                if (oldPath != null && !oldPath.startsWith("assets")) {
                    File oldFile = new File(oldPath);
                    if (oldFile.exists()) oldFile.delete();
                }

                // Salin file baru
                String ext = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
                String newFileName = "profile_" + System.currentTimeMillis() + ext;
                Path destination = assetDir.resolve(newFileName);
                Files.copy(selectedFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

                // Simpan path dan update user
                user.setProfilePicture(destination.toString());
                boolean updated = UserRepository.updateUser(user);

                if (updated) {
                    loadUserFromXML();
                    loadProfilePicture();
                    if (sidebarController != null) sidebarController.refreshProfileData();

                    showAlert(Alert.AlertType.INFORMATION, "Foto profil berhasil diperbarui!");
                    reloadProfileView();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Gagal menyimpan perubahan foto.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Gagal mengganti foto: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeletePhoto() {
        String defaultImagePath = "assets/icn_profile_default.jpg";

        try {
            User user = Session.getCurrentUser();

            // Hapus gambar lama dari file sistem (jika bukan default)
            String oldPath = user.getProfilePicture();
            if (oldPath != null && !oldPath.startsWith("assets")) {
                File oldFile = new File(oldPath);
                if (oldFile.exists()) oldFile.delete();
            }

            // Update path foto ke default di XML
            List<User> users = UserRepository.loadUsers();
            for (User u : users) {
                if (u.getId() == user.getId()) {
                    u.setProfilePicture(defaultImagePath);
                    break;
                }
            }

            UserRepository.saveUsers(users); // simpan ke file XML
            loadUserFromXML();               // refresh data session
            loadProfilePicture();           // refresh tampilan

            if (sidebarController != null) {
                sidebarController.refreshProfileData();
            }

            showAlert(Alert.AlertType.INFORMATION, "Foto profil telah dihapus dan diganti ke default.");
            reloadProfileView();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal menghapus foto: " + e.getMessage());
        }
    }

    private void reloadProfileView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/profil.fxml"));
            Parent updatedProfile = loader.load();

            ProfileController controller = loader.getController();
            controller.setSidebarController(sidebarController);

            if (sidebarController != null) {
                AnchorPane mainContent = sidebarController.getMainContent();
                mainContent.getChildren().setAll(updatedProfile);
                AnchorPane.setTopAnchor(updatedProfile, 0.0);
                AnchorPane.setBottomAnchor(updatedProfile, 0.0);
                AnchorPane.setLeftAnchor(updatedProfile, 0.0);
                AnchorPane.setRightAnchor(updatedProfile, 0.0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
