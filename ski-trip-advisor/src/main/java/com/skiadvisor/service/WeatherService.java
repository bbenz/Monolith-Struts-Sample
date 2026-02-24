package com.skiadvisor.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skiadvisor.model.SkiConditions;
import com.skiadvisor.model.WeatherInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

/**
 * Service for fetching weather and ski conditions
 * Supports both real weather API integration (Open-Meteo) and mock mode for testing
 */
public class WeatherService {
    private Random random = new Random();
    private Gson gson = new Gson();
    
    // Configuration
    private final String mode;
    private final boolean fallbackEnabled;
    private final OpenMeteoClient weatherClient;

    /**
     * Constructor - loads configuration from database.properties
     */
    public WeatherService() {
        Properties props = loadProperties();
        
        this.mode = props.getProperty("weather.service.mode", "mock");
        this.fallbackEnabled = Boolean.parseBoolean(
            props.getProperty("weather.fallback.enabled", "true")
        );
        
        // Initialize OpenMeteo client if in real mode
        if ("real".equalsIgnoreCase(this.mode)) {
            String weatherApiUrl = props.getProperty("weather.api.url",
                "https://api.open-meteo.com/v1/forecast");
            String geocodingApiUrl = props.getProperty("weather.geocoding.url",
                "https://geocoding-api.open-meteo.com/v1/search");
            this.weatherClient = new OpenMeteoClient(weatherApiUrl, geocodingApiUrl);
        } else {
            this.weatherClient = null;
        }
    }

    /**
     * Load properties from database.properties file
     */
    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load database.properties, using defaults");
        }
        return props;
    }

    /**
     * Get current weather for a ski resort
     * Uses real API in 'real' mode, falls back to mock if API fails and fallback is enabled
     */
    public WeatherInfo getWeather(String location) {
        if ("real".equalsIgnoreCase(mode) && weatherClient != null) {
            try {
                return weatherClient.fetchWeather(location);
            } catch (IOException e) {
                System.err.println("Weather API error: " + e.getMessage());
                if (fallbackEnabled) {
                    System.err.println("Falling back to mock weather data");
                    return getMockWeather(location);
                }
                throw new RuntimeException("Failed to fetch weather data", e);
            }
        }
        
        // Mock mode
        return getMockWeather(location);
    }

    /**
     * Generate mock weather data (for testing or when API is unavailable)
     */
    private WeatherInfo getMockWeather(String location) {
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
        weather.setCloudCoverPercent(random.nextInt(100)); // 0-100%
        weather.setPrecipitationMm(random.nextDouble() * 15); // 0-15 mm
        
        return weather;
    }

    /**
     * Get ski slope conditions for a resort
     * Infers conditions from weather data in real mode
     */
    public SkiConditions getSkiConditions(String resortName) {
        WeatherInfo weather = getWeather(resortName);
        
        SkiConditions conditions = new SkiConditions();
        conditions.setResortName(resortName);
        
        // Infer slope counts based on weather
        int totalSlopes = 50 + random.nextInt(100); // 50-150 slopes
        int openPercentage = calculateOpenPercentage(weather);
        int openSlopes = (int)(totalSlopes * openPercentage / 100.0);
        conditions.setOpenSlopesCount(openSlopes);
        conditions.setTotalSlopesCount(totalSlopes);
        
        // Snow depth - starts with base depth, adds recent snowfall
        double baseDepth = 100 + random.nextDouble() * 100; // 100-200 cm base
        conditions.setSnowDepthCm(baseDepth + weather.getSnowfallCm());
        
        // Infer snow quality from weather conditions
        String quality = inferSnowQuality(weather);
        conditions.setSnowQuality(quality);
        
        // Difficulty varies by resort (simplified - would be from resort database in production)
        String[] difficulties = {"Beginner Friendly", "Intermediate", "Advanced", "Expert Only"};
        conditions.setDifficulty(difficulties[random.nextInt(difficulties.length)]);
        
        // Powder day if significant recent snowfall and cold temps
        boolean isPowderDay = weather.getSnowfallCm() > 10 && weather.getTemperatureCelsius() < -5;
        conditions.setPowderDay(isPowderDay);
        
        return conditions;
    }

    /**
     * Calculate percentage of open slopes based on weather conditions
     */
    private int calculateOpenPercentage(WeatherInfo weather) {
        // Start with 95% open in good conditions
        int percentage = 95;
        
        // Reduce if high winds
        if (weather.getWindSpeedKmh() > 60) {
            percentage -= 20; // Lifts close in extreme wind
        } else if (weather.getWindSpeedKmh() > 40) {
            percentage -= 10;
        }
        
        // Reduce slightly in blizzard conditions
        if ("Blizzard".equals(weather.getConditions()) || "Heavy Snow".equals(weather.getConditions())) {
            percentage -= 5;
        }
        
        // Reduce if temperature too warm (rain risk)
        if (weather.getTemperatureCelsius() > 2) {
            percentage -= 15; // Rain closes higher elevation slopes
        }
        
        return Math.max(70, percentage); // Never less than 70% open
    }

    /**
     * Infer snow quality from weather conditions
     */
    private String inferSnowQuality(WeatherInfo weather) {
        double temp = weather.getTemperatureCelsius();
        double snowfall = weather.getSnowfallCm();
        int cloudCover = weather.getCloudCoverPercent();
        
        // Fresh powder: recent heavy snowfall + cold temps
        if (snowfall > 10 && temp < -5) {
            return "Fresh Powder";
        }
        
        // Powder: recent moderate snowfall + cold temps
        if (snowfall > 5 && temp < -3) {
            return "Powder";
        }
        
        // Packed powder: cold temps, some snow, good grooming conditions
        if (temp < -2 && snowfall > 1) {
            return "Packed Powder";
        }
        
        // Groomed: cold temps, clear/partly cloudy (good grooming weather)
        if (temp < 0 && cloudCover < 50) {
            return "Groomed";
        }
        
        // Hard pack: cold but no fresh snow
        if (temp < -1) {
            return "Hard Pack";
        }
        
        // Variable/icy: warmer temps
        return "Variable (Icy in spots)";
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
        weatherJson.addProperty("cloud_cover_percent", weather.getCloudCoverPercent());
        weatherJson.addProperty("precipitation_mm", weather.getPrecipitationMm());
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

    /**
     * Close resources
     */
    public void close() {
        if (weatherClient != null) {
            try {
                weatherClient.close();
            } catch (IOException e) {
                System.err.println("Error closing weather client: " + e.getMessage());
            }
        }
    }
}
