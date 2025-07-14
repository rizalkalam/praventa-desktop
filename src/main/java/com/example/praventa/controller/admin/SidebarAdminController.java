package com.example.praventa.controller.admin;

import com.example.praventa.controller.BaseSidebarController;
import com.example.praventa.utils.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;

public class SidebarAdminController extends BaseSidebarController {
    @FXML private AnchorPane mainContent;

    @FXML private Circle avatarCircle;
    @FXML private Text usernameText;
    @FXML private Text statusText;

    @FXML private BorderPane navHome;
    @FXML private BorderPane navRiwayat;
    @FXML private BorderPane navAnalisis;
    @FXML private BorderPane navArtikel;
    @FXML private BorderPane navAkun;
    @FXML private BorderPane navKeluar;

    @FXML private ImageView iconHome;
    @FXML private ImageView iconRiwayat;
    @FXML private ImageView iconAnalisis;
    @FXML private ImageView iconArtikel;
    @FXML private ImageView iconAkun;
    @FXML private ImageView iconKeluar;

    @FXML private javafx.scene.text.Text textHome;
    @FXML private javafx.scene.text.Text textRiwayat;
    @FXML private javafx.scene.text.Text textAnalisis;
    @FXML private javafx.scene.text.Text textArtikel;
    @FXML private javafx.scene.text.Text textAkun;
    @FXML private javafx.scene.text.Text textKeluar;


    @FXML private Rectangle rectHome;
    @FXML private Rectangle rectRiwayat;
    @FXML private Rectangle rectAnalisis;
    @FXML private Rectangle rectArtikel;
    @FXML private Rectangle rectAkun;
    @FXML private Rectangle rectKeluar;

    private AnchorPane contentTarget;

    @FXML
    public void initialize() {
//        Session.setSidebarController(this);
    }

    @Override
    public void loadPage(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/" + fxmlName));
            Parent content = loader.load();

            contentTarget.getChildren().setAll(content);
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
