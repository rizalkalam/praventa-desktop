package com.example.praventa.model.questionnaire;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class QuestionnaireResult {
    @XmlElement(name = "category")
    private String category;

    @XmlElement(name = "question")
    private List<QuestionAnswer> questionAnswers;

    public QuestionnaireResult() {}

    public QuestionnaireResult(String category, List<QuestionAnswer> questionAnswers) {
        this.category = category;
        this.questionAnswers = questionAnswers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<QuestionAnswer> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(List<QuestionAnswer> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }
}
