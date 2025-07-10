package com.example.praventa.controller;

import com.example.praventa.model.users.PersonalDisease;
import com.example.praventa.model.users.User;
import com.example.praventa.model.users.UserListWrapper;
import com.example.praventa.utils.SceneUtil;
import com.example.praventa.utils.Session;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PersonalDiseaseController {

    @FXML
    private AnchorPane rootPane;
    @FXML private AnchorPane formPane;
    @FXML private Label progressLabel;
    @FXML private Text titleText;
    @FXML private Text descriptionText;
    @FXML private TextField emailField;
    @FXML private Button nextButton;

    private ComboBox<String> choiceBox;
    private CheckBox yesCheckBox;
    private TextField extraTextField;

    private int currentStep = 0;

    // Penyimpanan jawaban
    private boolean hasDisease;
    private String diseaseName;
    private int diagnosedYear;
    private String stillActive;
    private boolean hospitalized;
    private boolean underTreatment;
    private String treatmentDetails;

    private final QuestionStep[] steps = new QuestionStep[]{
            new QuestionStep("1/6", "Apakah Anda memiliki riwayat penyakit pribadi?",
                    "Pertanyaan ini akan menentukan langkah berikutnya.",
                    InputType.YES_NO),

            new QuestionStep("2/6", "Jika ya, sebutkan nama penyakitnya.",
                    "Masukkan nama penyakit yang pernah Anda derita.",
                    InputType.TEXT),

            new QuestionStep("3/6", "Tahun pertama kali didiagnosis",
                    "Masukkan tahun pertama kali Anda menerima diagnosis.",
                    InputType.TEXT),

            new QuestionStep("4/6", "Apakah penyakit ini masih aktif?",
                    "Pilih apakah penyakit ini masih aktif atau tidak.",
                    InputType.SELECT, List.of("Masih aktif", "Sudah sembuh", "Tidak yakin")),

            new QuestionStep("5/6", "Apakah Anda pernah dirawat inap terkait penyakit tersebut?",
                    "Jawaban ini membantu dalam menilai tingkat keparahan.",
                    InputType.YES_NO),

            new QuestionStep("6/6", "Apakah Anda sedang/masih menjalani pengobatan?",
                    "Jika ya, mohon sebutkan pengobatan yang sedang dijalani.",
                    InputType.YES_NO_TEXT)
    };

    @FXML
    public void initialize() {
        applyStep(currentStep);
        nextButton.setOnAction(e -> handleNext());
    }

    private void applyStep(int index) {
        // Bersihkan semua komponen dinamis, kecuali label, title, deskripsi, tombol, dan emailField
        formPane.getChildren().removeIf(node ->
                node instanceof CheckBox || node instanceof ComboBox ||
                        (node instanceof TextField && node != emailField)
        );

        // Reset semua komponen
        emailField.setVisible(false);
        QuestionStep step = steps[index];

        progressLabel.setText(step.progress);
        titleText.setText(step.title);
        descriptionText.setText(step.description);

        switch (step.inputType) {
            case TEXT -> {
                emailField.setVisible(true);
                emailField.setText("");
            }
            case YES_NO -> {
                yesCheckBox = new CheckBox("Ya");
                yesCheckBox.setLayoutX(emailField.getLayoutX());
                yesCheckBox.setLayoutY(emailField.getLayoutY());
                formPane.getChildren().add(yesCheckBox);
            }
            case SELECT -> {
                choiceBox = new ComboBox<>();
                choiceBox.getItems().setAll(step.options);
                choiceBox.setLayoutX(emailField.getLayoutX());
                choiceBox.setLayoutY(emailField.getLayoutY());
                choiceBox.setPrefWidth(emailField.getPrefWidth());
                formPane.getChildren().add(choiceBox);
            }
            case YES_NO_TEXT -> {
                yesCheckBox = new CheckBox("Ya");
                yesCheckBox.setLayoutX(emailField.getLayoutX());
                yesCheckBox.setLayoutY(emailField.getLayoutY());

                extraTextField = new TextField();
                extraTextField.setPromptText("Detail pengobatan");
                extraTextField.setLayoutX(emailField.getLayoutX());
                extraTextField.setLayoutY(emailField.getLayoutY() + 40);
                extraTextField.setPrefWidth(emailField.getPrefWidth());

                formPane.getChildren().addAll(yesCheckBox, extraTextField);
            }
        }
    }

    private void handleNext() {
        if (!saveCurrentAnswer()) return;

        if (currentStep < steps.length - 1) {
            currentStep++;
            applyStep(currentStep);
        } else {
            saveToXML();
            SceneUtil.switchToMainPage(rootPane);
        }
    }

    private boolean saveCurrentAnswer() {
        QuestionStep step = steps[currentStep];

        try {
            switch (step.inputType) {
                case TEXT -> {
                    String input = emailField.getText().trim();
                    if (input.isEmpty()) throw new IllegalArgumentException("Field tidak boleh kosong.");
                    if (currentStep == 2) diagnosedYear = Integer.parseInt(input);
                    else if (currentStep == 1) diseaseName = input;
                }
                case YES_NO -> {
                    hasDisease = yesCheckBox.isSelected();
                    if (currentStep == 0 && !hasDisease) {
                        // Skip ke akhir jika tidak ada penyakit
                        currentStep = steps.length - 1;
                        return true;
                    }
                    if (currentStep == 4) hospitalized = yesCheckBox.isSelected();
                }
                case SELECT -> {
                    stillActive = choiceBox.getValue();
                    if (stillActive == null) throw new IllegalArgumentException("Pilih salah satu opsi.");
                }
                case YES_NO_TEXT -> {
                    underTreatment = yesCheckBox.isSelected();
                    treatmentDetails = underTreatment ? extraTextField.getText().trim() : "";
                }
            }
            return true;
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Input tidak valid: " + e.getMessage());
            return false;
        }
    }

    private void saveToXML() {
        try {
            File file = new File("D:/Kuliah/Project/praventa/data/users.xml");
            JAXBContext context = JAXBContext.newInstance(UserListWrapper.class, User.class, PersonalDisease.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            List<User> users;
            UserListWrapper wrapper;

            if (file.exists() && file.length() > 0) {
                wrapper = (UserListWrapper) unmarshaller.unmarshal(file);
                users = wrapper.getUsers();
            } else {
                wrapper = new UserListWrapper();
                users = new ArrayList<>();
                wrapper.setUsers(users);
            }

            User currentUser = Session.getCurrentUser();
            if (currentUser == null) return;

            // Siapkan data penyakit pribadi
            PersonalDisease disease = new PersonalDisease();
            disease.setName(diseaseName);
            disease.setDiagnosedYear(diagnosedYear);
            disease.setActiveStatus(stillActive);
            disease.setHospitalized(hospitalized);
            disease.setUnderTreatment(underTreatment);
            disease.setTreatmentDetails(treatmentDetails);

            for (User u : users) {
                if (u.getUsername().equals(currentUser.getUsername())) {
                    List<PersonalDisease> history = u.getPersonalDiseases();
                    if (history == null) history = new ArrayList<>();
                    history.add(disease);
                    u.setPersonalDiseases(history);
                    break;
                }
            }

            marshaller.marshal(wrapper, file);
            System.out.println("✅ Riwayat penyakit pribadi disimpan.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Gagal menyimpan riwayat penyakit pribadi.");
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private enum InputType { TEXT, YES_NO, SELECT, YES_NO_TEXT }

    private static class QuestionStep {
        String progress, title, description;
        InputType inputType;
        List<String> options;

        public QuestionStep(String progress, String title, String description, InputType inputType) {
            this(progress, title, description, inputType, null);
        }

        public QuestionStep(String progress, String title, String description, InputType inputType, List<String> options) {
            this.progress = progress;
            this.title = title;
            this.description = description;
            this.inputType = inputType;
            this.options = options;
        }
    }
}
