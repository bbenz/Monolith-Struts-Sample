package com.skiadvisor.model;

/**
 * Represents ski slope conditions
 */
public class SkiConditions {
    private String resortName;
    private int openSlopesCount;
    private int totalSlopesCount;
    private double snowDepthCm;
    private String snowQuality;
    private String difficulty;
    private boolean isPowderDay;

    public SkiConditions() {
    }

    public SkiConditions(String resortName, int openSlopesCount, int totalSlopesCount,
                        double snowDepthCm, String snowQuality, String difficulty,
                        boolean isPowderDay) {
        this.resortName = resortName;
        this.openSlopesCount = openSlopesCount;
        this.totalSlopesCount = totalSlopesCount;
        this.snowDepthCm = snowDepthCm;
        this.snowQuality = snowQuality;
        this.difficulty = difficulty;
        this.isPowderDay = isPowderDay;
    }

    // Getters and Setters
    public String getResortName() { return resortName; }
    public void setResortName(String resortName) { this.resortName = resortName; }

    public int getOpenSlopesCount() { return openSlopesCount; }
    public void setOpenSlopesCount(int openSlopesCount) { 
        this.openSlopesCount = openSlopesCount; 
    }

    public int getTotalSlopesCount() { return totalSlopesCount; }
    public void setTotalSlopesCount(int totalSlopesCount) { 
        this.totalSlopesCount = totalSlopesCount; 
    }

    public double getSnowDepthCm() { return snowDepthCm; }
    public void setSnowDepthCm(double snowDepthCm) { this.snowDepthCm = snowDepthCm; }

    public String getSnowQuality() { return snowQuality; }
    public void setSnowQuality(String snowQuality) { this.snowQuality = snowQuality; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public boolean isPowderDay() { return isPowderDay; }
    public void setPowderDay(boolean powderDay) { isPowderDay = powderDay; }

    @Override
    public String toString() {
        return String.format("%s: %d/%d slopes open, Snow depth: %.1f cm, Quality: %s, Difficulty: %s%s",
            resortName, openSlopesCount, totalSlopesCount, snowDepthCm, snowQuality, 
            difficulty, isPowderDay ? " (POWDER DAY!)" : "");
    }
}
