package com.example.praventa.model.questionnaire;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "risk")
@XmlAccessorType(XmlAccessType.FIELD)
public class RiskData {
    private String name;
    private double percentage;
    private String color;

    public RiskData() {}
    public RiskData(String name, double percentage, String color) {
        this.name = name;
        this.percentage = percentage;
        this.color = color;
    }

    public String getName() { return name; }
    public double getPercentage() { return percentage; }
    public String getColor() { return color; }
}
