package com.example.praventa.model.users;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "lifestyle_data")
@XmlAccessorType(XmlAccessType.FIELD)
public class LifestyleData {
    private Integer fastFoodFrequency;
    private String drinkType;
    private String smokingHabit;
    private String alcoholHabit;
    private String physicalActivity;

    @XmlElementWrapper(name = "sleepHabits")
    @XmlElement(name = "habit")
    private List<String> sleepHabits = new ArrayList<>();

    private Integer stressLevel;
    private String mainStressCause;

    public int getFastFoodFrequency() {
        return fastFoodFrequency;
    }

    public void setFastFoodFrequency(Integer fastFoodFrequency) {
        this.fastFoodFrequency = fastFoodFrequency;
    }

    public String getDrinkType() {
        return drinkType;
    }

    public void setDrinkType(String drinkType) {
        this.drinkType = drinkType;
    }

    public String getSmokingHabit() {
        return smokingHabit;
    }

    public void setSmokingHabit(String smokingHabit) {
        this.smokingHabit = smokingHabit;
    }

    public String getAlcoholHabit() {
        return alcoholHabit;
    }

    public void setAlcoholHabit(String alcoholHabit) {
        this.alcoholHabit = alcoholHabit;
    }

    public String getPhysicalActivity() {
        return physicalActivity;
    }

    public void setPhysicalActivity(String physicalActivity) {
        this.physicalActivity = physicalActivity;
    }

    public List<String> getSleepHabits() {
        return sleepHabits;
    }

    public void setSleepHabits(List<String> sleepHabits) {
        this.sleepHabits = sleepHabits;
    }

    public int getStressLevel() {
        return stressLevel;
    }

    public void setStressLevel(Integer stressLevel) {
        this.stressLevel = stressLevel;
    }

    public String getMainStressCause() {
        return mainStressCause;
    }

    public void setMainStressCause(String mainStressCause) {
        this.mainStressCause = mainStressCause;
    }
}
