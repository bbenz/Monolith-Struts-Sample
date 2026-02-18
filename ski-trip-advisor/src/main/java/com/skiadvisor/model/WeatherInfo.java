package com.skiadvisor.model;

/**
 * Represents weather information for a ski resort
 */
public class WeatherInfo {
    private String location;
    private double temperatureCelsius;
    private String conditions;
    private double windSpeedKmh;
    private double snowfallCm;
    private int humidityPercent;

    public WeatherInfo() {
    }

    public WeatherInfo(String location, double temperatureCelsius, String conditions,
                       double windSpeedKmh, double snowfallCm, int humidityPercent) {
        this.location = location;
        this.temperatureCelsius = temperatureCelsius;
        this.conditions = conditions;
        this.windSpeedKmh = windSpeedKmh;
        this.snowfallCm = snowfallCm;
        this.humidityPercent = humidityPercent;
    }

    // Getters and Setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getTemperatureCelsius() { return temperatureCelsius; }
    public void setTemperatureCelsius(double temperatureCelsius) { 
        this.temperatureCelsius = temperatureCelsius; 
    }

    public String getConditions() { return conditions; }
    public void setConditions(String conditions) { this.conditions = conditions; }

    public double getWindSpeedKmh() { return windSpeedKmh; }
    public void setWindSpeedKmh(double windSpeedKmh) { this.windSpeedKmh = windSpeedKmh; }

    public double getSnowfallCm() { return snowfallCm; }
    public void setSnowfallCm(double snowfallCm) { this.snowfallCm = snowfallCm; }

    public int getHumidityPercent() { return humidityPercent; }
    public void setHumidityPercent(int humidityPercent) { 
        this.humidityPercent = humidityPercent; 
    }

    @Override
    public String toString() {
        return String.format("%s: %.1f°C, %s, Wind: %.1f km/h, Snowfall: %.1f cm, Humidity: %d%%",
            location, temperatureCelsius, conditions, windSpeedKmh, snowfallCm, humidityPercent);
    }
}
