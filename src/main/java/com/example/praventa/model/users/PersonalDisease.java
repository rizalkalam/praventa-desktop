package com.example.praventa.model.users;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "personal_disease_history")
public class PersonalDisease {
    private String name;
    private int diagnosedYear;
    private String activeStatus;
    private boolean hospitalized;
    private boolean underTreatment;
    private String treatmentDetails;

    @XmlElement(name = "diseaseName")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public int getDiagnosedYear() {
        return diagnosedYear;
    }

    public void setDiagnosedYear(int diagnosedYear) {
        this.diagnosedYear = diagnosedYear;
    }

    @XmlElement(name = "status")
    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    @XmlElement
    public boolean isHospitalized() {
        return hospitalized;
    }

    public void setHospitalized(boolean hospitalized) {
        this.hospitalized = hospitalized;
    }

    @XmlElement
    public boolean isUnderTreatment() {
        return underTreatment;
    }

    public void setUnderTreatment(boolean underTreatment) {
        this.underTreatment = underTreatment;
    }

    @XmlElement
    public String getTreatmentDetails() {
        return treatmentDetails;
    }

    public void setTreatmentDetails(String treatmentDetails) {
        this.treatmentDetails = treatmentDetails;
    }
}
