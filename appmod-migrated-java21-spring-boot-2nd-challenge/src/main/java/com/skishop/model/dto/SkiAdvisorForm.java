package com.skishop.model.dto;

public class SkiAdvisorForm {
    private String location;
    private String skillLevel;
    private String skiingType;
    private String budget;

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSkillLevel() { return skillLevel; }
    public void setSkillLevel(String skillLevel) { this.skillLevel = skillLevel; }

    public String getSkiingType() { return skiingType; }
    public void setSkiingType(String skiingType) { this.skiingType = skiingType; }

    public String getBudget() { return budget; }
    public void setBudget(String budget) { this.budget = budget; }
}
