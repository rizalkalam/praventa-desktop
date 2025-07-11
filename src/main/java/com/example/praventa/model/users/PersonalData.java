package com.example.praventa.model.users;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "personal_data")
public class PersonalData {

    private String gender = "__";
    private String age = "__";
    private BodyMetrics bodyMetrics = new BodyMetrics();

    public PersonalData() {}

    @XmlElement
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @XmlElement(name = "age")
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @XmlElement(name = "body_metrics")
    public BodyMetrics getBodyMetrics() {
        return bodyMetrics;
    }

    public void setBodyMetrics(BodyMetrics bodyMetrics) {
        this.bodyMetrics = bodyMetrics;
    }
}
