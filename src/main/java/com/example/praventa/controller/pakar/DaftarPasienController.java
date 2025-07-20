package com.example.praventa.controller.pakar;

import com.example.praventa.controller.user.AnalisisController;
import com.example.praventa.model.users.User;
import com.example.praventa.model.users.UserListWrapper;
import com.example.praventa.utils.XmlUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DaftarPasienController implements Initializable {

    @FXML
    private VBox containerPasien;
    @FXML
    private Label listLabel;
    @FXML
    private VBox listContent;

    private final File xmlFile = new File("D:\\Kuliah\\Project\\praventa\\data\\users.xml");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPasien();
    }

    private void loadPasien() {
        listContent.getChildren().clear();

        try {
            UserListWrapper wrapper = XmlUtils.loadUsers(xmlFile);
            if (wrapper == null) return;

            for (User user : wrapper.getUsers()) {
                // Filter: hanya tampilkan pasien
                String role = user.getRole();
                if (role != null && !role.equalsIgnoreCase("admin") && !role.equalsIgnoreCase("pakar")) {
                    BorderPane card = createUserCard(user, wrapper);
                    listContent.getChildren().add(card);
                    VBox.setMargin(card, new Insets(0, 0, 10, 0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BorderPane createUserCard(User user, UserListWrapper wrapper) {
        BorderPane pane = new BorderPane();
        pane.setPrefSize(1112, 66);
        pane.setStyle("-fx-background-color: white; -fx-background-radius: 14;");
        pane.setPadding(new Insets(0, 30, 0, 30));

        // Kiri: Username
        Text nameText = new Text(user.getUsername());
        nameText.setFont(Font.font("System Bold", 14));
        StackPane leftPane = new StackPane(nameText);
        StackPane.setAlignment(nameText, Pos.CENTER_LEFT);
        leftPane.setPrefSize(200, 66);
        pane.setLeft(leftPane);

        // Tengah: Kosong (opsional info lain)
        HBox centerBox = new HBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(20);
        centerBox.setPrefSize(700, 66);
        pane.setCenter(centerBox);

        // Kanan: Gender + Delete icon
        HBox rightBox = new HBox();
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.setSpacing(10);

        String gender = (user.getPersonalData() != null && user.getPersonalData().getGender() != null)
                ? user.getPersonalData().getGender()
                : "-";
        Text genderText = new Text(gender);
        genderText.setFont(Font.font(14));

        ImageView deleteIcon = new ImageView(new Image("file:assets/delete_icon.png"));
        deleteIcon.setFitWidth(20);
        deleteIcon.setFitHeight(20);
        deleteIcon.setCursor(Cursor.HAND);
        deleteIcon.setOnMouseClicked(e -> handleDeleteUser(user));

        rightBox.getChildren().addAll(genderText, deleteIcon);
        pane.setRight(rightBox);

        // ✅ Tambahkan event klik untuk buka laporan
        pane.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/fxmlPakar/laporan.fxml"));
                Parent root = loader.load();

                LaporanPakarController controller = loader.getController();
                controller.setUser(user); // kirim data user

                Stage stage = new Stage();
                stage.setTitle("Laporan Pasien - " + user.getUsername());
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return pane;
    }

    private void handleDeleteUser(User user) {
        try {
            UserListWrapper wrapper = XmlUtils.loadUsers(xmlFile);
            wrapper.getUsers().removeIf(u -> u.getUsername().equals(user.getUsername()));
            XmlUtils.saveUsers(wrapper, xmlFile); // simpan ke file
            loadPasien(); // refresh tampilan
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}