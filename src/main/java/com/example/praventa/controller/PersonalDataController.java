package com.example.praventa.controller;

import com.example.praventa.model.users.BodyMetrics;
import com.example.praventa.model.users.PersonalData;
import com.example.praventa.model.users.User;
import com.example.praventa.model.users.UserListWrapper;
import com.example.praventa.utils.SceneUtil;
import com.example.praventa.utils.Session;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

public class PersonalDataController {

    @FXML private AnchorPane rootPane;
    @FXML private TextField emailField; // digunakan ulang sebagai input text
    @FXML private Label progressLabel;
    @FXML private Text titleText;
    @FXML private Text descriptionText;
    @FXML private Button nextButton;

    private int currentStep = 0;

    // Penyimpanan jawaban pengguna
    private int age;
    private String gender;
    private double weight;
    private double height;

    private static final String FILE_PATH = "D:/Kuliah/Project/praventa/data/users.xml";

    private final QuestionStep[] questionSteps = {
            new QuestionStep("1/4", "Masukkan Umur Anda",
                    "Masukkan usia Anda saat ini dalam satuan tahun.",
                    InputType.TEXT, null),

            new QuestionStep("2/4", "Pilih Jenis Kelamin",
                    "Data ini digunakan untuk analisis risiko berbasis gender.",
                    InputType.SELECT, List.of("Pria", "Wanita", "Other")),

            new QuestionStep("3/4", "Masukkan Berat Badan",
                    "Dalam satuan kilogram (kg).",
                    InputType.TEXT, null),

            new QuestionStep("4/4", "Masukkan Tinggi Badan",
                    "Dalam satuan sentimeter (cm).",
                    InputType.TEXT, null)
    };

    // Untuk input dinamis jenis kelamin
    private ComboBox<String> genderComboBox;

    @FXML
    public void initialize() {
        genderComboBox = new ComboBox<>();
        genderComboBox.setPrefWidth(emailField.getPrefWidth());
        genderComboBox.setLayoutX(emailField.getLayoutX());
        genderComboBox.setLayoutY(emailField.getLayoutY());
        genderComboBox.setStyle("-fx-background-radius: 10; -fx-border-color: #505050;");

        applyStep(currentStep);

        nextButton.setOnAction(event -> {
            if (!saveCurrentAnswer()) return;

            if (currentStep < questionSteps.length - 1) {
                currentStep++;
                applyStep(currentStep);
            } else {
                // Semua pertanyaan sudah dijawab
                saveToUserXml(gender, age, (int) weight, (int) height);
                System.out.println("Data personal berhasil disimpan ke user.xml");
                System.out.println("Umur: " + age);
                System.out.println("Gender: " + gender);
                System.out.println("Berat: " + weight);
                System.out.println("Tinggi: " + height);

                SceneUtil.switchToMainPage(rootPane);
            }
        });
    }

    private void applyStep(int stepIndex) {
        QuestionStep step = questionSteps[stepIndex];
        progressLabel.setText(step.progress);
        titleText.setText(step.title);
        descriptionText.setText(step.description);

        rootPane.getChildren().remove(genderComboBox);
        emailField.setVisible(false);

        if (step.inputType == InputType.TEXT) {
            emailField.setVisible(true);
            emailField.setText(""); // kosongkan input text
        } else if (step.inputType == InputType.SELECT) {
            genderComboBox.getItems().setAll(step.options);
            genderComboBox.getSelectionModel().clearSelection();
            rootPane.getChildren().add(genderComboBox);
        }
    }

    private boolean saveCurrentAnswer() {
        QuestionStep step = questionSteps[currentStep];

        try {
            if (step.inputType == InputType.TEXT) {
                String input = emailField.getText().trim();
                if (input.isEmpty()) throw new IllegalArgumentException("Field tidak boleh kosong.");

                switch (currentStep) {
                    case 0: age = Integer.parseInt(input); break;
                    case 2: weight = Double.parseDouble(input); break;
                    case 3: height = Double.parseDouble(input); break;
                }

            } else if (step.inputType == InputType.SELECT) {
                String selected = genderComboBox.getValue();
                if (selected == null) throw new IllegalArgumentException("Pilih salah satu opsi.");
                gender = selected;
            }
            return true;

        } catch (Exception e) {
            // TODO: tampilkan alert di UI
            System.out.println("Input tidak valid: " + e.getMessage());
            return false;
        }
    }

    private void saveToUserXml(String gender, int age, int weight, int height) {
        try {
            File file = new File(FILE_PATH);
            File parentDir = file.getParentFile();

            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            JAXBContext context = JAXBContext.newInstance(
                    UserListWrapper.class,
                    User.class,
                    PersonalData.class,
                    BodyMetrics.class
            );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            UserListWrapper wrapper;
            List<User> users;

            if (file.exists() && file.length() > 0) {
                wrapper = (UserListWrapper) unmarshaller.unmarshal(file);
                users = wrapper.getUsers();
            } else {
                wrapper = new UserListWrapper();
                users = new ArrayList<>();
                wrapper.setUsers(users);
            }

            User currentUser = Session.getCurrentUser();
            if (currentUser == null) {
                System.err.println("❌ Tidak ada user aktif di sesi.");
                return;
            }

            // Buat objek PersonalData
            PersonalData personalData = new PersonalData();
            personalData.setGender(gender);
            personalData.setAge(Integer.toString(age));

            BodyMetrics bodyMetrics = new BodyMetrics();
            bodyMetrics.setWeight(weight);
            bodyMetrics.setHeight(height);
            personalData.setBodyMetrics(bodyMetrics);

            // Update user dalam list berdasarkan username
            boolean updated = false;
            for (User user : users) {
                if (user.getUsername().equals(currentUser.getUsername())) {
                    user.setPersonalData(personalData);
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                // Jika user belum ada, tambahkan user dari sesi
                currentUser.setPersonalData(personalData);
                users.add(currentUser);
            }

            // Simpan ke file XML
            marshaller.marshal(wrapper, file);
            System.out.println("✅ Data personal disimpan ke user.xml");

        } catch (Exception e) {
            System.err.println("❌ Gagal menyimpan data personal:");
            e.printStackTrace();
        }
    }

    private enum InputType {
        TEXT, SELECT
    }

    private static class QuestionStep {
        String progress;
        String title;
        String description;
        InputType inputType;
        List<String> options;

        QuestionStep(String progress, String title, String description, InputType inputType, List<String> options) {
            this.progress = progress;
            this.title = title;
            this.description = description;
            this.inputType = inputType;
            this.options = options;
        }
    }
}

