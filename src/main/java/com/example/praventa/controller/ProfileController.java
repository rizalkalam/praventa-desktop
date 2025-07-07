package com.example.praventa.controller;

import com.example.praventa.model.User;
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

    @FXML
    private void handleSave(ActionEvent event) {
        User user = Session.getCurrentUser();

        String newName = nameField.getText();
        String newEmail = emailField.getText();
        String newPhone = phoneField.getText();

        if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Semua field harus diisi.");
            alert.showAndWait();
            return;
        }

        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE users SET username = ?, email = ?, phone_number = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newName);
            stmt.setString(2, newEmail);
            stmt.setString(3, newPhone);
            stmt.setInt(4, user.getId());

            if (stmt.executeUpdate() > 0) {
                loadUserFromDatabase();
                refreshFields();

                // ⬇️ Tambahan: update tampilan sidebar
                if (sidebarController != null) {
                    sidebarController.refreshProfileData();
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Berhasil");
                alert.setHeaderText(null);
                alert.setContentText("Profil berhasil diperbarui!");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChangePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Profil");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                User user = Session.getCurrentUser();
                String userHome = System.getProperty("user.home");
                Path assetDir = Paths.get(userHome, "praventa_assets");
                Files.createDirectories(assetDir);

                String oldPath = user.getProfilePicture();
                if (oldPath != null && !oldPath.startsWith("assets")) {
                    File oldFile = new File(oldPath);
                    if (oldFile.exists()) oldFile.delete();
                }

                String ext = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
                String newFileName = "profile_" + System.currentTimeMillis() + ext;
                Path destination = assetDir.resolve(newFileName);
                Files.copy(selectedFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

                String savedPath = destination.toString();

                try (Connection conn = Database.getConnection()) {
                    String sql = "UPDATE users SET profile_picture = ? WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, savedPath);
                    stmt.setInt(2, user.getId());
                    stmt.executeUpdate();
                }

                loadUserFromDatabase();
                loadProfilePicture();
                if (sidebarController != null) sidebarController.refreshProfileData();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Foto profil berhasil diperbarui!");
                alert.showAndWait();

                reloadProfileView();

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleDeletePhoto() {
        String defaultImagePath = "assets/icn_profile_default.jpg";

        try (Connection conn = Database.getConnection()) {
            User user = Session.getCurrentUser();

            String oldPath = user.getProfilePicture();
            if (oldPath != null && !oldPath.startsWith("assets")) {
                File oldFile = new File(oldPath);
                if (oldFile.exists()) oldFile.delete();
            }

            String sql = "UPDATE users SET profile_picture = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, defaultImagePath);
            stmt.setInt(2, user.getId());

            if (stmt.executeUpdate() > 0) {
                loadUserFromDatabase();
                loadProfilePicture();
                if (sidebarController != null) sidebarController.refreshProfileData();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Foto profil telah dihapus dan diganti ke default.");
                alert.showAndWait();

                reloadProfileView();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
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
