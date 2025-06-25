package com.example.praventa.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    @FXML
    private VBox sidebar;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private Button btnHome, btnAbout;

    private Map<Button, String> buttonToFxml = new HashMap<>();

    @FXML
    public void initialize() {
        buttonToFxml.put(btnHome, "home.fxml");
        buttonToFxml.put(btnAbout, "about.fxml");

        btnHome.setOnAction(e -> switchPage(btnHome));
        btnAbout.setOnAction(e -> switchPage(btnAbout));

        switchPage(btnHome);
    }

    private void switchPage(Button button) {
        for (Button btn : buttonToFxml.keySet()) {
            btn.getStyleClass().remove("active");
        }
        button.getStyleClass().add("active");

        try {
            Node page = FXMLLoader.load(getClass().getResource("/com/example/app1/sidebarapp/fxml/" + buttonToFxml.get(button)));
            contentPane.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
