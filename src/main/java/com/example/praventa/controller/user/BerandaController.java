package com.example.praventa.controller.user;

import com.example.praventa.model.users.LifestyleData;
import com.example.praventa.model.users.User;
import com.example.praventa.model.users.UserListWrapper;
import com.example.praventa.service.ChatbotService;
import com.example.praventa.utils.Session;
import com.example.praventa.utils.XmlUtils;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class BerandaController {
    @FXML
    private Button startLifestyleButton;
    @FXML
    private Text usernameText;
    @FXML
    private VBox messageContainer;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField inputField;

    private static final String FILE_PATH = "D:/Kuliah/Project/praventa/data/users.xml";

    @FXML
    public void initialize() {
        checkLifestyleData();

        inputField.setOnAction(event -> handleUserMessage());
        // Auto scroll saat messageContainer height berubah
        messageContainer.heightProperty().addListener((obs, oldVal, newVal) -> {
            scrollPane.setVvalue(1.0);
        });

        User currentUser = Session.getCurrentUser();
        if (currentUser != null) {
            usernameText.setText("Hai, " + currentUser.getUsername() + "!");
        } else {
            usernameText.setText("Hai, Pengguna!");
        }
    }

    @FXML
    private void handleStartLifestyleInput(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/lifestyle.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Input Gaya Hidup - Praventa");
            stage.setScene(scene);
            stage.setMaximized(true);

            // Fade in animation (opsional)
            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            stage.show();

            // Tutup window sekarang
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkLifestyleData() {
        try {
            File file = new File(FILE_PATH);
            UserListWrapper wrapper = XmlUtils.loadUsers(file);
            User currentUser = Session.getCurrentUser();

            if (currentUser != null) {
                User userFromFile = wrapper.getUserById(currentUser.getId());

                if (userFromFile != null && userFromFile.getLifestyleData() != null) {
                    LifestyleData lifestyle = userFromFile.getLifestyleData();

                    boolean filled =
                            lifestyle.getFastFoodFrequency() != 0 &&
                                    !isNullOrEmpty(lifestyle.getDrinkType()) &&
                                    !isNullOrEmpty(lifestyle.getSmokingHabit()) &&
                                    !isNullOrEmpty(lifestyle.getAlcoholHabit()) &&
                                    !isNullOrEmpty(lifestyle.getPhysicalActivity()) &&
                                    lifestyle.getSleepHabits() != null && !lifestyle.getSleepHabits().isEmpty() &&
                                    lifestyle.getStressLevel() != 0;

                    if (filled) {
                        startLifestyleButton.setText("Sudah Diisi");
                        startLifestyleButton.setStyle("-fx-background-color: #9E91E1; -fx-background-radius: 10;");
                        startLifestyleButton.setDisable(true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleUserMessage() {
        String userInput = inputField.getText().trim();
        if (userInput.isEmpty()) return;

        addBubble(userInput, true); // user
        inputField.clear();

        new Thread(() -> {
            try {
                String response = ChatbotService.askVita(userInput); // atau pakai GeminiService.generateRecommendation(userInput)
                Platform.runLater(() -> addBubble(response, false)); // bot
            } catch (Exception e) {
                Platform.runLater(() -> addBubble("Maaf, terjadi kesalahan. 😢", false));
            }
        }).start();
    }

    private void addBubble(String message, boolean isUser) {
        HBox bubbleRow = new HBox(10);
        bubbleRow.setAlignment(isUser ? Pos.TOP_RIGHT : Pos.TOP_LEFT);

        // Avatar
        ImageView avatar = new ImageView();
        avatar.setFitHeight(28);
        avatar.setFitWidth(28);
        avatar.setClip(new Circle(14, 14, 14));

        String avatarPath;
        if (isUser) {
            avatarPath = getUserAvatarPath();
        } else {
            URL vitaResource = getClass().getResource("/assets/vita.png");
            if (vitaResource != null) {
                avatarPath = vitaResource.toExternalForm();
            } else {
                System.err.println("Gagal menemukan vita.png di resources!");
                return; // Atau bisa skip avatar
            }
        }

        if (!avatarPath.isEmpty()) {
            avatar.setImage(new Image(avatarPath));
        }


        // Text bubble
        Label text = new Label(message);
        text.setWrapText(true);
        text.setMaxWidth(250); // maksimal lebar bubble
        text.setPadding(new Insets(10));
        text.setStyle(isUser
                ? "-fx-background-color: #D1D1FF; -fx-background-radius: 12; -fx-font-size: 13px;"
                : "-fx-background-color: #FFFF; -fx-background-radius: 12; -fx-font-size: 13px;");

        text.setTextAlignment(TextAlignment.LEFT);
        text.setEffect(new DropShadow(2, Color.rgb(200, 200, 200, 0.3)));


        if (isUser) {
            bubbleRow.getChildren().addAll(text, avatar);
        } else {
            bubbleRow.getChildren().addAll(avatar, text);
        }

        messageContainer.getChildren().add(bubbleRow);
    }

    private String getUserAvatarPath() {
        User currentUser = Session.getCurrentUser();
        if (currentUser != null && currentUser.getProfilePicture() != null) {
            File f = new File(currentUser.getProfilePicture());
            if (f.exists()) {
                return f.toURI().toString();
            }
        }

        // Default fallback dari resource
        URL defaultAvatarUrl = getClass().getResource("/assets/icn_profile_default.png");
        return defaultAvatarUrl != null ? defaultAvatarUrl.toExternalForm() : "file:/D:/Kuliah/Project/praventa/src/main/resources/assets/icn_profile_default.png"; // fallback hardcoded jika develop
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty() || value.trim().equals("__");
    }
}
