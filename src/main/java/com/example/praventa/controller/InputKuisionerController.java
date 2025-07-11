package com.example.praventa.controller;

import com.example.praventa.model.questionnaire.RiskAnalysis;
import com.example.praventa.model.questionnaire.RiskData;
import com.example.praventa.model.questionnaire.QuestionAnswer;
import com.example.praventa.model.questionnaire.QuestionnaireResult;
import com.example.praventa.model.users.User;
import com.example.praventa.repository.UserRepository;
import com.example.praventa.service.GeminiService;
import com.example.praventa.utils.SceneUtil;
import com.example.praventa.utils.Session;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputKuisionerController {
    @FXML private AnchorPane rootPane;
    @FXML private Text titleText;
    @FXML private Text descriptionText;
    @FXML private Text descriptionText1;
    @FXML private Text descriptionText2;
    @FXML private Text descriptionText3;
    @FXML private Text descriptionText4;
    @FXML private Text descriptionText5;

    @FXML private RadioButton rbYa0, rbTidak0;
    @FXML private RadioButton rbYa1, rbTidak1;
    @FXML private RadioButton rbYa2, rbTidak2;
    @FXML private RadioButton rbYa3, rbTidak3;
    @FXML private RadioButton rbYa4, rbTidak4;
    @FXML private RadioButton rbYa5, rbTidak5;

    @FXML private Label progressLabel;
    @FXML private Button nextButton, nextButton1;
    private Alert activeAlert;

    private final List<QuestionSet> allQuestionSets = new ArrayList<>();
    private int currentStep = 0;
    private final List<QuestionnaireResult> resultList = new ArrayList<>();
    private final ToggleGroup[] toggleGroups = new ToggleGroup[6];

    @FXML
    public void initialize() {
        initQuestions();
        initToggleGroups();
        loadStep();

        nextButton.setOnAction(e -> {
            if (saveCurrentStepAnswers()) {
                if (currentStep < allQuestionSets.size() - 1) {
                    currentStep++;
                    loadStep();
                } else {
                    saveResultsToUser();
                }
            }
        });

        nextButton1.setOnAction(e -> {
            if (currentStep > 0) {
                currentStep--;
                loadStep();
            }
        });
    }

    private void initQuestions() {
        allQuestionSets.add(new QuestionSet("Diabetes", Arrays.asList(
                "Sering merasa haus sepanjang hari?",
                "Sering merasa lapar?",
                "Sering buang air kecil malam hari?",
                "Tubuh cepat lelah tanpa alasan yang jelas?",
                "Berat badan berlebih?",
                "Riwayat penyakit keluarga diabetes?"
        )));
        allQuestionSets.add(new QuestionSet("Hipertensi", Arrays.asList(
                "Sering merasa pusing sakit kepala mendadak?",
                "Sering merasa tegang mudah marah?",
                "Sering makan makanan manis?",
                "Jarang berolahraga?",
                "Jantung berdetak cepat tanpa sebab?"
        )));
        allQuestionSets.add(new QuestionSet("Penyakit Jantung", Arrays.asList(
                "Nyeri dada saat beraktivitas?",
                "Cepat lelah saat naik tangga/jalan jauh?",
                "Jantung berdebar/tidak teratur?",
                "Sering makan gorengan dan makanan berlemak?",
                "Jarang berolahraga?"
        )));
        allQuestionSets.add(new QuestionSet("Stroke", Arrays.asList(
                "Tiba-tiba lemas di satu sisi tubuh?",
                "Kesulitan bicara mendadak?",
                "Merokok setiap hari?",
                "Stres berat berkepanjangan?",
                "Riwayat keluarga pernah stroke?"
        )));
        allQuestionSets.add(new QuestionSet("Kanker Serviks", Arrays.asList(
                "Belum pernah melakukan Pap smear?",
                "Keputihan berbau/disertai darah?",
                "Pendarahan setelah berhubungan?",
                "Aktif seksual sejak usia muda?",
                "Pasangan seksual lebih dari satu?"
        )));
    }

    private void initToggleGroups() {
        for (int i = 0; i < toggleGroups.length; i++) {
            toggleGroups[i] = new ToggleGroup();
        }

        getYaButton(0).setToggleGroup(toggleGroups[0]);
        getTidakButton(0).setToggleGroup(toggleGroups[0]);
        getYaButton(1).setToggleGroup(toggleGroups[1]);
        getTidakButton(1).setToggleGroup(toggleGroups[1]);
        getYaButton(2).setToggleGroup(toggleGroups[2]);
        getTidakButton(2).setToggleGroup(toggleGroups[2]);
        getYaButton(3).setToggleGroup(toggleGroups[3]);
        getTidakButton(3).setToggleGroup(toggleGroups[3]);
        getYaButton(4).setToggleGroup(toggleGroups[4]);
        getTidakButton(4).setToggleGroup(toggleGroups[4]);
        getYaButton(5).setToggleGroup(toggleGroups[5]);
        getTidakButton(5).setToggleGroup(toggleGroups[5]);
    }

    private void loadStep() {
        QuestionSet qs = allQuestionSets.get(currentStep);
        List<String> q = qs.getQuestions();

        titleText.setText(qs.getTitle());
        progressLabel.setText("Pertanyaan " + (currentStep + 1) + " dari " + allQuestionSets.size());

        // Loop untuk isi pertanyaan
        for (int i = 0; i < 6; i++) {
            if (i < q.size()) {
                getDescription(i).setText((i + 1) + ". " + q.get(i));
                getDescription(i).setVisible(true);
                getYaButton(i).setVisible(true);
                getTidakButton(i).setVisible(true);
            } else {
                getDescription(i).setText("");
                getDescription(i).setVisible(false);
                getYaButton(i).setVisible(false);
                getTidakButton(i).setVisible(false);
            }

            // Reset toggle (penting jika user kembali ke step sebelumnya)
            toggleGroups[i].selectToggle(null);
        }
    }

    private boolean saveCurrentStepAnswers() {
        QuestionSet qs = allQuestionSets.get(currentStep);
        List<QuestionAnswer> questionAnswers = new ArrayList<>();
        List<String> questions = qs.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            Toggle selected = toggleGroups[i].getSelectedToggle();
            if (selected == null) {
                showAlert("Harap isi semua pertanyaan!");
                return false;
            }

            boolean answer = getYaButton(i).isSelected();
            String questionText = questions.get(i);
            questionAnswers.add(new QuestionAnswer(questionText, answer));
        }

        // Jika sudah ada hasil sebelumnya untuk kategori ini, ganti
        resultList.removeIf(r -> r.getCategory().equals(qs.getTitle()));
        resultList.add(new QuestionnaireResult(qs.getTitle(), questionAnswers));
        return true;
    }

    private void saveResultsToUser() {
        try {
            User currentUser = Session.getCurrentUser();
            if (currentUser == null) {
                showAlert("User belum login.");
                return;
            }

            // Update hasil kuisioner user
            currentUser.getQuestionnaireResults().clear();
            currentUser.getQuestionnaireResults().addAll(resultList);

            // Muat semua user dari XML
            List<User> users = UserRepository.loadUsers();

            // Ganti user yang cocok dengan versi yang sudah diperbarui
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId() == currentUser.getId()) {
                    users.set(i, currentUser);
                    break;
                }
            }

            // Simpan seluruh user kembali ke XML
            UserRepository.saveUsers(users);
            analyzeRiskWithGemini(resultList);

            System.out.println("[DEBUG] Kuisioner disimpan untuk user: " + currentUser.getUsername());
            showAlert("Kuisioner berhasil disimpan.");
            SceneUtil.switchToMainPage(rootPane);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Gagal menyimpan kuisioner!");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Helper
    private Text getDescription(int index) {
        return switch (index) {
            case 0 -> descriptionText;
            case 1 -> descriptionText1;
            case 2 -> descriptionText2;
            case 3 -> descriptionText3;
            case 4 -> descriptionText4;
            case 5 -> descriptionText5;
            default -> null;
        };
    }

    private RadioButton getYaButton(int index) {
        return switch (index) {
            case 0 -> rbYa0;
            case 1 -> rbYa1;
            case 2 -> rbYa2;
            case 3 -> rbYa3;
            case 4 -> rbYa4;
            case 5 -> rbYa5;
            default -> null;
        };
    }

    private RadioButton getTidakButton(int index) {
        return switch (index) {
            case 0 -> rbTidak0;
            case 1 -> rbTidak1;
            case 2 -> rbTidak2;
            case 3 -> rbTidak3;
            case 4 -> rbTidak4;
            case 5 -> rbTidak5;
            default -> null;
        };
    }

    // Inner class untuk pertanyaan per kategori
    private static class QuestionSet {
        private final String title;
        private final List<String> questions;

        public QuestionSet(String title, List<String> questions) {
            this.title = title;
            this.questions = questions;
        }

        public String getTitle() {
            return title;
        }

        public List<String> getQuestions() {
            return questions;
        }
    }

    private void analyzeRiskWithGemini(List<QuestionnaireResult> results) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Saya memiliki hasil kuisioner risiko penyakit ...\n");

        for (QuestionnaireResult r : results) {
            prompt.append("Kategori: ").append(r.getCategory()).append("\n");
            for (QuestionAnswer qa : r.getQuestionAnswers()) {
                prompt.append("- ").append(qa.getQuestion())
                        .append(" Jawaban: ").append(qa.isAnswer() ? "Ya" : "Tidak").append("\n");
            }
            prompt.append("\n");
        }

        // Tambahan instruksi Gemini
        prompt.append("Berdasarkan data tersebut, berikan saya:\n");
        prompt.append("1. Nilai probabilitas ...\n");
        prompt.append("2. Tampilkan dalam bentuk: setChartData(...);\n");
        prompt.append("3. Berikan juga data: new RiskData(...);\n");

        // 🔁 Kirim ke Gemini di thread baru
        new Thread(() -> {
            try {
                String response = GeminiService.generateRecommendation(prompt.toString());
                System.out.println("[Gemini Output]\n" + response);

                // ✅ Simpan hasil Gemini
                saveGeminiResultToUser(response);

                // Tampilkan hasil ke pengguna
                javafx.application.Platform.runLater(() -> {
                    showShortInfo("Hasil analisis berhasil disimpan!");
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void saveGeminiResultToUser(String geminiOutput) {
        try {
            // 1. Parsing chart data dari format: setChartData(40, 20, 13);
            List<Integer> chartValues = new ArrayList<>();
            Matcher chartMatcher = Pattern.compile("setChartData\\(([^)]+)\\);").matcher(geminiOutput);
            if (chartMatcher.find()) {
                String[] parts = chartMatcher.group(1).split(",");
                for (String part : parts) {
                    part = part.trim();
                    if (part.matches("\\d+")) { // hanya parse angka bulat
                        chartValues.add(Integer.parseInt(part));
                    }
                }
            }

            // 2. Parsing RiskData dari format: new RiskData("Nama", 0.5, "#HEX")
            List<RiskData> riskList = new ArrayList<>();
            Matcher riskMatcher = Pattern.compile(
                    "new RiskData\\(\"(.*?)\",\\s*([0-9.]+),\\s*\"(#[A-Fa-f0-9]+)\"\\)"
            ).matcher(geminiOutput);
            while (riskMatcher.find()) {
                String name = riskMatcher.group(1);
                double percentage = Double.parseDouble(riskMatcher.group(2));
                String color = riskMatcher.group(3);
                riskList.add(new RiskData(name, percentage, color));
            }

            // Fallback jika parsing gagal
            if (chartValues.size() < 3 || chartValues.size() > 4) {
                System.out.println("Chart data tidak valid, menggunakan nilai default.");
                chartValues = List.of(60, 30, 65, 75); // default fallback
            }

            if (riskList.isEmpty()) {
                System.out.println("Risk data kosong, menggunakan data default.");
                riskList = List.of(
                        new RiskData("Diabetes Tipe 2", 0.5, "#4FC3F7"),
                        new RiskData("Hipertensi", 0.8, "#FF7043"),
                        new RiskData("Penyakit Jantung", 0.4, "#3F51B5"),
                        new RiskData("Stroke", 0.6, "#EF5350"),
                        new RiskData("Kanker Serviks", 0.3, "#81C784")
                );
            }

            // 3. Simpan ke XML user
            User currentUser = Session.getCurrentUser();
            if (currentUser != null) {
                RiskAnalysis analysis = new RiskAnalysis(riskList, chartValues);
                currentUser.setRiskAnalysis(analysis);

                List<User> allUsers = UserRepository.loadUsers();
                for (int i = 0; i < allUsers.size(); i++) {
                    if (allUsers.get(i).getId() == currentUser.getId()) {
                        allUsers.set(i, currentUser);
                        break;
                    }
                }

                UserRepository.saveUsers(allUsers);
                System.out.println("[DEBUG] RiskAnalysis berhasil disimpan ke XML user.");
                showShortInfo("✅ Hasil analisis risiko berhasil disimpan.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showShortInfo("❌ Gagal menyimpan hasil analisis ke XML.");
        }
    }

    private void showShortInfo(String message) {
        Platform.runLater(() -> {
            if (activeAlert != null && activeAlert.isShowing()) {
                activeAlert.setContentText(message); // ganti teks jika sudah tampil
                return; // tidak buat alert baru
            }

            activeAlert = new Alert(Alert.AlertType.INFORMATION);
            activeAlert.setTitle(null);
            activeAlert.setHeaderText(null);
            activeAlert.setContentText(message);
            activeAlert.initModality(Modality.NONE); // Tidak blok UI
            activeAlert.show();

            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(e -> {
                activeAlert.close();
                activeAlert = null; // reset alert
            });
            delay.play();
        });
    }

}
