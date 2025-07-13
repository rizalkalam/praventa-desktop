package com.example.praventa.model.questionnaire;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "risk_analysis")
@XmlAccessorType(XmlAccessType.FIELD)
public class RiskAnalysis {
    @XmlElementWrapper(name = "risks")
    @XmlElement(name = "risk")
    private List<RiskData> risks;

    @XmlElementWrapper(name = "chart_values")
    @XmlElement(name = "value")
    private List<Integer> chartValues;

    public RiskAnalysis() {} // Required by JAXB

    public RiskAnalysis(List<RiskData> risks, List<Integer> chartValues) {
        this.risks = risks;
        this.chartValues = chartValues;
    }

    public List<RiskData> getRisks() {
        return risks;
    }

    public List<Integer> getChartValues() {
        return chartValues;
    }

    public void setRisks(List<RiskData> risks) {
        this.risks = risks;
    }

    public void setChartValues(List<Integer> chartValues) {
        this.chartValues = chartValues;
    }
}