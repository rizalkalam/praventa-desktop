package com.example.praventa.model.users;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class FamilyDiseaseHistory {

    private String relation;
    private String diseaseName;
    private int diagnosedAge;
    private String status;

    @Override
    public String toString() {
        return "FamilyDiseaseHistory{" +
                "relation='" + relation + '\'' +
                ", diseaseName='" + diseaseName + '\'' +
                ", diagnosedAge=" + diagnosedAge +
                ", status='" + status + '\'' +
                '}';
    }

    // Getter dan Setter tetap ada, tapi tanpa anotasi
    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public int getDiagnosedAge() {
        return diagnosedAge;
    }

    public void setDiagnosedAge(int diagnosedAge) {
        this.diagnosedAge = diagnosedAge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}