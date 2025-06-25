package com.example.praventa.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class SidebarController {

    @FXML private Circle avatarCircle;
    @FXML private Text usernameText;
    @FXML private Text statusText;

    @FXML private BorderPane navBeranda, navRiwayat, navAnalisis, navArtikel;
    @FXML private ImageView iconBeranda, iconRiwayat, iconAnalisis, iconArtikel;
    @FXML private Text textBeranda, textRiwayat, textAnalisis, textArtikel;
    @FXML private Rectangle rectHome, rectRiwayat, rectAnalisis, rectArtikel;

    private final Map<String, NavElement> navElements = new HashMap<>();
    private AnchorPane mainContent;
    private String currentPage = "";

    public void setMainContent(AnchorPane mainContent) {
        this.mainContent = mainContent;
    }

    @FXML
    public void initialize() {
        setupNavElements();
        setActive("HOME");
        loadPage("/com/example/praventa/fxml/beranda.fxml");
    }

    private void setupNavElements() {
        navElements.put("HOME", new NavElement(navBeranda, iconBeranda, textBeranda, rectHome, "/com/example/praventa/assets/icn_home.png", "/com/example/praventa/assets/icn_home_active.png", "/com/example/praventa/fxml/beranda.fxml"));
        navElements.put("RIWAYAT", new NavElement(navRiwayat, iconRiwayat, textRiwayat, rectRiwayat, "/com/example/praventa/assets/icn_riwayat.png", "/com/example/praventa/assets/icn_riwayat_active.png", "/com/example/praventa/fxml/riwayat.fxml"));
        navElements.put("ANALISIS", new NavElement(navAnalisis, iconAnalisis, textAnalisis, rectAnalisis, "/com/example/praventa/assets/icn_analisis.png", "/com/example/praventa/assets/icn_analisis_active.png", "/com/example/praventa/fxml/analisis.fxml"));
        navElements.put("ARTIKEL", new NavElement(navArtikel, iconArtikel, textArtikel, rectArtikel, "/com/example/praventa/assets/icn_artikel.png", "/com/example/praventa/assets/icn_artikel_active.png", "/com/example/praventa/fxml/artikel.fxml"));
    }

    private void setActive(String key) {
        for (Map.Entry<String, NavElement> entry : navElements.entrySet()) {
            boolean active = entry.getKey().equals(key);
            NavElement el = entry.getValue();

            if (el.label != null) el.label.setFill(active ? Color.web("#9e91e1") : Color.BLACK);
            if (el.indicator != null) el.indicator.setFill(active ? Color.web("#9e91e1") : Color.WHITE);
            if (el.icon != null) el.icon.setImage(new Image(getClass().getResourceAsStream(active ? el.activeIcon : el.defaultIcon)));
            if (el.navPane != null) el.navPane.setStyle(active ? "-fx-background-color: rgba(158, 145, 225, 0.1); -fx-background-radius: 8px;" : "");
        }

        currentPage = key;
    }

    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent page = loader.load();
            mainContent.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNavClick(MouseEvent event) {
        Object source = event.getSource();
        if (source == navBeranda) {
            setActive("HOME");
            loadPage(navElements.get("HOME").fxmlPath);
        } else if (source == navRiwayat) {
            setActive("RIWAYAT");
            loadPage(navElements.get("RIWAYAT").fxmlPath);
        } else if (source == navAnalisis) {
            setActive("ANALISIS");
            loadPage(navElements.get("ANALISIS").fxmlPath);
        } else if (source == navArtikel) {
            setActive("ARTIKEL");
            loadPage(navElements.get("ARTIKEL").fxmlPath);
        }
    }

    private static class NavElement {
        BorderPane navPane;
        ImageView icon;
        Text label;
        Rectangle indicator;
        String defaultIcon, activeIcon, fxmlPath;

        NavElement(BorderPane navPane, ImageView icon, Text label, Rectangle indicator, String defaultIcon, String activeIcon, String fxmlPath) {
            this.navPane = navPane;
            this.icon = icon;
            this.label = label;
            this.indicator = indicator;
            this.defaultIcon = defaultIcon;
            this.activeIcon = activeIcon;
            this.fxmlPath = fxmlPath;
        }
    }
}