package com.example.praventa.controller.user;

import com.example.praventa.model.users.FamilyDiseaseHistory;
import com.example.praventa.model.users.User;
import com.example.praventa.model.users.UserListWrapper;
import com.example.praventa.utils.Session;
import com.example.praventa.utils.XmlUtils;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FamilyDiseaseController {
    @FXML private AnchorPane rootPane;

    @FXML private Label progressLabel;
    @FXML private Text titleText;
    @FXML private Text descriptionText;

    @FXML private RadioButton rbYes;
    @FXML private RadioButton rbNo;
    @FXML private ComboBox<String> relationComboBox;
    @FXML private TextField diseaseNameField;
    @FXML private TextField diagnosedAgeField;
    @FXML private RadioButton rbAlive;
    @FXML private RadioButton rbDeceased;
    @FXML private Button submitButton;
    @FXML private HBox statusBox;

    @FXML private VBox formContainer;
    private int currentStep = 0;

    private final ToggleGroup yesNoGroup = new ToggleGroup();
    private final ToggleGroup statusGroup = new ToggleGroup();

    private static final String FILE_PATH = "D:\\Kuliah\\Project\\praventa\\data\\users.xml";

    @FXML
    public void initialize() {
        rbYes.setToggleGroup(yesNoGroup);
        rbNo.setToggleGroup(yesNoGroup);
        rbAlive.setToggleGroup(statusGroup);
        rbDeceased.setToggleGroup(statusGroup);

        yesNoGroup.selectToggle(null);   // Reset pilihan Ya/Tidak
        statusGroup.selectToggle(null); // Reset status Hidup/Wafat

        relationComboBox.getItems().addAll("Ayah", "Ibu", "Kakek", "Nenek", "Saudara Kandung", "Lainnya");

        submitButton.setOnAction(this::handleNextStep);
        showStep(currentStep);
    }

    private void showStep(int step) {
        hideAllInputs();  // reset

        switch (step) {
            case 0:
                progressLabel.setText("1/5");
                titleText.setText("Pertanyaan 1");
                descriptionText.setText("Apakah ada anggota keluarga dengan penyakit kronis?");
                rbYes.setVisible(true);
                rbNo.setVisible(true);
                break;

            case 1:
                progressLabel.setText("2/5");
                titleText.setText("Pertanyaan 2");
                descriptionText.setText("Siapa hubungan keluarga tersebut?");
                relationComboBox.setVisible(true);
                break;

            case 2:
                progressLabel.setText("3/5");
                titleText.setText("Pertanyaan 3");
                descriptionText.setText("Apa nama penyakit yang diderita?");
                diseaseNameField.setVisible(true);
                break;

            case 3:
                progressLabel.setText("4/5");
                titleText.setText("Pertanyaan 4");
                descriptionText.setText("Usia saat didiagnosis (opsional)");
                diagnosedAgeField.setVisible(true);
                break;

            case 4:
                progressLabel.setText("5/5");
                titleText.setText("Pertanyaan 5");
                descriptionText.setText("Status anggota keluarga");
                statusBox.setVisible(true);
                submitButton.setText("Selesai");
                break;

            case 5:
                saveFamilyHistory();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Tambah Data");
                alert.setHeaderText("Data berhasil disimpan.");
                alert.setContentText("Apakah Anda ingin menambahkan riwayat keluarga lainnya?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // reset form untuk input baru
                    clearForm();
                    currentStep = 1;
                } else {
                    currentStep = 0;
                }
                submitButton.setText("Lanjut");
                showStep(currentStep);
                break;
        }
    }

    private void clearForm() {
        relationComboBox.setValue(null);
        diseaseNameField.clear();
        diagnosedAgeField.clear();
        statusGroup.selectToggle(null);
    }

    private void openMainPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/praventa/fxml/main.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setMaximized(true);

            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();

            stage.show();

            Stage current = (Stage) rootPane.getScene().getWindow();
            current.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handleNextStep(ActionEvent event) {
        if (!validateForm()) return;

        if (currentStep == 0 && rbNo.isSelected()) {
            showAlert("Tersimpan", "Data riwayat penyakit keluarga berhasil disimpan.");
            openMainPage(); // pakai method yang tadi
            return;
        }

        currentStep++;
        showStep(currentStep);
    }

    private boolean validateForm() {
        switch (currentStep) {
            case 0: // Ya/Tidak
                if (!rbYes.isSelected() && !rbNo.isSelected()) {
                    showAlert("Peringatan", "Silakan pilih Ya atau Tidak terlebih dahulu.");
                    return false;
                }
                break;

            case 1: // Hubungan keluarga
                if (relationComboBox.getValue() == null || relationComboBox.getValue().isBlank()) {
                    showAlert("Peringatan", "Silakan pilih hubungan keluarga.");
                    return false;
                }
                break;

            case 2: // Nama penyakit
                if (diseaseNameField.getText().isBlank()) {
                    showAlert("Peringatan", "Silakan isi nama penyakit.");
                    return false;
                }
                break;

            case 3: // Usia (opsional)
                String ageText = diagnosedAgeField.getText();
                if (!ageText.isBlank()) {
                    try {
                        Integer.parseInt(ageText);
                    } catch (NumberFormatException e) {
                        showAlert("Error", "Usia harus berupa angka.");
                        return false;
                    }
                }
                break;

            case 4: // Status hidup/wafat
                if (!rbAlive.isSelected() && !rbDeceased.isSelected()) {
                    showAlert("Peringatan", "Silakan pilih status anggota keluarga.");
                    return false;
                }
                break;
        }
        return true;
    }

    private void saveEmptyHistory() {
        try {
            File file = new File(FILE_PATH);
            UserListWrapper wrapper = XmlUtils.loadUsers(file);
            User currentUser = Session.getCurrentUser();

            if (currentUser != null) {
                currentUser.setFamilyDiseaseHistoryList(new ArrayList<>());
                wrapper.replaceUser(currentUser);
                XmlUtils.saveUsers(wrapper, file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFamilyHistory() {
        try {
            File file = new File(FILE_PATH);
            UserListWrapper wrapper = XmlUtils.loadUsers(file);

            User currentUser = Session.getCurrentUser();
            if (currentUser == null) {
                System.out.println("[ERROR] Tidak ada user aktif saat menyimpan data!");
                return;
            }

            // 🔧 Buat list baru hanya berisi 1 data baru
            FamilyDiseaseHistory history = new FamilyDiseaseHistory();
            history.setRelation(relationComboBox.getValue());
            history.setDiseaseName(diseaseNameField.getText());

            String ageText = diagnosedAgeField.getText();
            history.setDiagnosedAge(ageText.isBlank() ? -1 : Integer.parseInt(ageText));
            history.setStatus(rbAlive.isSelected() ? "Hidup" : "Wafat");

            List<FamilyDiseaseHistory> singleNewList = new ArrayList<>();
            singleNewList.add(history);

            // ❗Set list baru ini (sementara) ke currentUser → akan digabung oleh replaceUser
            currentUser.setFamilyDiseaseHistoryList(singleNewList);

            // Simpan
            saveToXml(currentUser);

            // 🔁 Refresh session
            UserListWrapper refreshedWrapper = XmlUtils.loadUsers(file);
            User refreshedUser = refreshedWrapper.getUserByUsername(currentUser.getUsername());
            if (refreshedUser != null) {
                Session.setCurrentUser(refreshedUser);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    private void hideAllInputs() {
        rbYes.setVisible(false);
        rbNo.setVisible(false);

        formContainer.setVisible(true); // tetap ditampilkan tapi anak-anak dikontrol manual
        relationComboBox.setVisible(false);
        diseaseNameField.setVisible(false);
        diagnosedAgeField.setVisible(false);
        statusBox.setVisible(false);
    }

    public static void saveToXml(User updatedUser) {
        try {
            File file = new File("D:\\Kuliah\\Project\\praventa\\data\\users.xml");
            UserListWrapper wrapper = XmlUtils.loadUsers(file);

            if (updatedUser != null) {
                wrapper.replaceUser(updatedUser);
                XmlUtils.saveUsers(wrapper, file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
