package com.skiadvisor.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skiadvisor.model.SkiConditions;
import com.skiadvisor.model.WeatherInfo;

import java.util.Random;

/**
 * Service for fetching weather and ski conditions
 * This is a mock implementation. In production, you would integrate with real weather APIs
 * like OpenWeatherMap, WeatherAPI, or ski resort APIs
 */
public class WeatherService {
    private Random random = new Random();
    private Gson gson = new Gson();

    /**
     * Get current weather for a ski resort
     * TODO: Replace with real weather API integration
     */
    public WeatherInfo getWeather(String location) {
        // Mock weather data - replace with actual API call
        WeatherInfo weather = new WeatherInfo();
        weather.setLocation(location);
        
        // Simulate winter ski conditions
        double temp = -15 + random.nextDouble() * 10; // -15°C to -5°C
        weather.setTemperatureCelsius(temp);
        
        String[] conditions = {"Clear", "Partly Cloudy", "Cloudy", "Light Snow", "Heavy Snow", "Blizzard"};
        weather.setConditions(conditions[random.nextInt(conditions.length)]);
        
        weather.setWindSpeedKmh(5 + random.nextDouble() * 35); // 5-40 km/h
        weather.setSnowfallCm(random.nextDouble() * 20); // 0-20 cm
        weather.setHumidityPercent(60 + random.nextInt(30)); // 60-90%
        
        return weather;
    }

    /**
     * Get ski slope conditions for a resort
     * TODO: Replace with real ski resort API integration
     */
    public SkiConditions getSkiConditions(String resortName) {
        // Mock ski conditions - replace with actual API call
        SkiConditions conditions = new SkiConditions();
        conditions.setResortName(resortName);
        
        int totalSlopes = 50 + random.nextInt(100); // 50-150 slopes
        int openSlopes = (int)(totalSlopes * (0.7 + random.nextDouble() * 0.3)); // 70-100% open
        conditions.setOpenSlopesCount(openSlopes);
        conditions.setTotalSlopesCount(totalSlopes);
        
        conditions.setSnowDepthCm(100 + random.nextDouble() * 150); // 100-250 cm base
        
        String[] qualities = {"Hard Pack", "Packed Powder", "Powder", "Fresh Powder", "Groomed"};
        conditions.setSnowQuality(qualities[random.nextInt(qualities.length)]);
        
        String[] difficulties = {"Beginner Friendly", "Intermediate", "Advanced", "Expert Only"};
        conditions.setDifficulty(difficulties[random.nextInt(difficulties.length)]);
        
        conditions.setPowderDay(random.nextDouble() > 0.7); // 30% chance of powder day
        
        return conditions;
    }

    /**
     * Get combined weather and ski conditions summary
     */
    public String getConditionsSummary(String location) {
        WeatherInfo weather = getWeather(location);
        SkiConditions skiConditions = getSkiConditions(location);
        
        return String.format(
            "Location: %s\n" +
            "Weather: %s\n" +
            "Ski Conditions: %s\n",
            location, weather.toString(), skiConditions.toString()
        );
    }

    /**
     * Convert weather and ski conditions to JSON for AI processing
     */
    public String getConditionsAsJson(String location) {
        WeatherInfo weather = getWeather(location);
        SkiConditions skiConditions = getSkiConditions(location);
        
        JsonObject json = new JsonObject();
        json.addProperty("location", location);
        
        JsonObject weatherJson = new JsonObject();
        weatherJson.addProperty("temperature_celsius", weather.getTemperatureCelsius());
        weatherJson.addProperty("conditions", weather.getConditions());
        weatherJson.addProperty("wind_speed_kmh", weather.getWindSpeedKmh());
        weatherJson.addProperty("snowfall_cm", weather.getSnowfallCm());
        weatherJson.addProperty("humidity_percent", weather.getHumidityPercent());
        json.add("weather", weatherJson);
        
        JsonObject skiJson = new JsonObject();
        skiJson.addProperty("resort_name", skiConditions.getResortName());
        skiJson.addProperty("open_slopes", skiConditions.getOpenSlopesCount());
        skiJson.addProperty("total_slopes", skiConditions.getTotalSlopesCount());
        skiJson.addProperty("snow_depth_cm", skiConditions.getSnowDepthCm());
        skiJson.addProperty("snow_quality", skiConditions.getSnowQuality());
        skiJson.addProperty("difficulty", skiConditions.getDifficulty());
        skiJson.addProperty("is_powder_day", skiConditions.isPowderDay());
        json.add("ski_conditions", skiJson);
        
        return gson.toJson(json);
    }
}
