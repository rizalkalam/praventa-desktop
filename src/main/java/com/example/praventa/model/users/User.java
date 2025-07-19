package com.example.praventa.model.users;

import java.util.List;
import java.util.ArrayList;

import com.example.praventa.model.questionnaire.RiskAnalysis;
import com.example.praventa.model.questionnaire.QuestionnaireResult;
import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private String role;

    @XmlElement(name = "profile_picture")
    private String profilePicture;

    @XmlElement(name = "phone_number")
    private String phoneNumber;

    @XmlElement(name = "personal_data")
    private PersonalData personalData;

    @XmlElement(name = "lifestyle_data")
    private LifestyleData lifestyleData;

    @XmlElementWrapper(name = "family_disease_history")
    @XmlElement(name = "disease")
    private List<FamilyDiseaseHistory> familyDiseaseHistoryList;

    @XmlElementWrapper(name = "personal_disease_history")
    @XmlElement(name = "disease")
    private List<PersonalDisease> personalDiseases;

    @XmlElementWrapper(name = "questionnaire_results")
    @XmlElement(name = "result")
    private List<QuestionnaireResult> questionnaireResults = new ArrayList<>();

    @XmlElement(name = "recommendation")
    private String recommendation;

    @XmlElement(name = "risk_analysis")
    private RiskAnalysis riskAnalysis;

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    // Constructors
    public User() {}

    public User(int id, String username, String email, String role, String profilePicture, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.profilePicture = profilePicture;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public PersonalData getPersonalData() { return personalData; }
    public void setPersonalData(PersonalData personalData) { this.personalData = personalData; }

    public LifestyleData getLifestyleData() { return lifestyleData; }
    public void setLifestyleData(LifestyleData lifestyleData) { this.lifestyleData = lifestyleData; }

    public List<FamilyDiseaseHistory> getFamilyDiseaseHistoryList() {
        return familyDiseaseHistoryList;
    }

    public void setFamilyDiseaseHistoryList(List<FamilyDiseaseHistory> familyDiseaseHistoryList) {
        this.familyDiseaseHistoryList = familyDiseaseHistoryList;
    }

    public List<PersonalDisease> getPersonalDiseases() {
        return personalDiseases;
    }

    public void setPersonalDiseases(List<PersonalDisease> personalDiseases) {
        this.personalDiseases = personalDiseases;
    }

    public List<QuestionnaireResult> getQuestionnaireResults() {
        return questionnaireResults;
    }

    public void setQuestionnaireResults(List<QuestionnaireResult> questionnaireResults) {
        this.questionnaireResults = questionnaireResults;
    }

    public void addQuestionnaireResult(QuestionnaireResult result) {
        this.questionnaireResults.add(result);
    }

    // Getter & Setter
    public RiskAnalysis getRiskAnalysis() {
        return riskAnalysis;
    }

    public void setRiskAnalysis(RiskAnalysis riskAnalysis) {
        this.riskAnalysis = riskAnalysis;
    }

    // menangani data hasil rekomendasi untuk analisis
    public List<String> getFoodRecommendations() {
        return extractSection("Rekomendasi Makan");
    }

    public List<String> getSleepRecommendations() {
        return extractSection("Rekomendasi Tidur");
    }

    public List<String> getActivityRecommendations() {
        return extractSection("Rekomendasi Aktivitas");
    }

    private List<String> extractSection(String sectionTitle) {
        if (recommendation == null || recommendation.isEmpty()) return List.of();

        List<String> result = new ArrayList<>();
        String[] lines = recommendation.split("\\r?\\n");
        boolean inSection = false;

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("Rekomendasi")) {
                inSection = line.startsWith(sectionTitle);
                continue;
            }

            if (inSection) {
                if (line.startsWith("-")) {
                    result.add(line);
                } else if (line.startsWith("Rekomendasi")) {
                    break; // keluar jika sudah masuk ke section lain
                }
            }
        }

        return result;
    }
}