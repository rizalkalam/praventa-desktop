package com.example.praventa.model.questionnaire;

import java.util.List;

public class Question {
    public enum InputType {
        TEXT,        // TextField biasa
        NUMBER,      // TextField hanya angka
        RADIO,       // RadioButton (satu pilihan)
        CHECKBOX,    // Checkbox multiple
        SLIDER       // Slider angka
    }

    private final String label;           // Label/pertanyaan
    private final InputType inputType;    // Jenis input
    private final List<String> options;   // Untuk RADIO, CHECKBOX, SLIDER
    private final boolean optional;       // Apakah wajib dijawab

    public Question(String label, InputType inputType, List<String> options, boolean optional) {
        this.label = label;
        this.inputType = inputType;
        this.options = options;
        this.optional = optional;
    }

    public String getLabel() {
        return label;
    }

    public InputType getInputType() {
        return inputType;
    }

    public List<String> getOptions() {
        return options;
    }

    public boolean isOptional() {
        return optional;
    }
}
