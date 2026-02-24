package com.skishop.service;

import com.skishop.model.dto.SkiConditions;
import com.skishop.model.dto.WeatherInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;

@Service
public class SkiWeatherService {
    private static final Logger log = LoggerFactory.getLogger(SkiWeatherService.class);
    private final Random random = new Random();
    
    private final String mode;
    private final boolean fallbackEnabled;
    private final OpenMeteoClient weatherClient;

    public SkiWeatherService(
            @Value("${weather.service.mode:mock}") String mode,
            @Value("${weather.fallback.enabled:true}") boolean fallbackEnabled,
            OpenMeteoClient weatherClient) {
        this.mode = mode;
        this.fallbackEnabled = fallbackEnabled;
        this.weatherClient = weatherClient;
        log.info("SkiWeatherService initialized: mode={}, fallbackEnabled={}", mode, fallbackEnabled);
    }

    public WeatherInfo getWeather(String location) {
        if ("real".equalsIgnoreCase(mode)) {
            try {
                log.debug("Fetching real weather data for: {}", location);
                return weatherClient.fetchWeather(location);
            } catch (IOException e) {
                log.error("Weather API error: {}", e.getMessage());
                if (fallbackEnabled) {
                    log.warn("Falling back to mock weather data");
                    return getMockWeather(location);
                }
                throw new RuntimeException("Failed to fetch weather data", e);
            }
        }
        
        log.debug("Using mock weather data for: {}", location);
        return getMockWeather(location);
    }

    private WeatherInfo getMockWeather(String location) {
        WeatherInfo weather = new WeatherInfo();
        weather.setLocation(location);
        weather.setTemperatureCelsius(-15 + random.nextDouble() * 10);

        String[] conditions = {"Clear", "Partly Cloudy", "Cloudy", "Light Snow", "Heavy Snow", "Blizzard"};
        weather.setConditions(conditions[random.nextInt(conditions.length)]);
        weather.setWindSpeedKmh(5 + random.nextDouble() * 35);
        weather.setSnowfallCm(random.nextDouble() * 20);
        weather.setHumidityPercent(60 + random.nextInt(30));
        weather.setCloudCoverPercent(random.nextInt(100));
        weather.setPrecipitationMm(random.nextDouble() * 15);

        return weather;
    }

    public SkiConditions getSkiConditions(String resortName) {
        WeatherInfo weather = getWeather(resortName);
        
        SkiConditions conditions = new SkiConditions();
        conditions.setResortName(resortName);

        // Infer slope counts based on weather
        int totalSlopes = 50 + random.nextInt(100);
        int openPercentage = calculateOpenPercentage(weather);
        int openSlopes = (int)(totalSlopes * openPercentage / 100.0);
        conditions.setOpenSlopesCount(openSlopes);
        conditions.setTotalSlopesCount(totalSlopes);

        // Snow depth - starts with base depth, adds recent snowfall
        double baseDepth = 100 + random.nextDouble() * 100;
        conditions.setSnowDepthCm(baseDepth + weather.getSnowfallCm());

        // Infer snow quality from weather conditions
        String quality = inferSnowQuality(weather);
        conditions.setSnowQuality(quality);

        String[] difficulties = {"Beginner Friendly", "Intermediate", "Advanced", "Expert Only"};
        conditions.setDifficulty(difficulties[random.nextInt(difficulties.length)]);
        
        // Powder day if significant recent snowfall and cold temps
        boolean isPowderDay = weather.getSnowfallCm() > 10 && weather.getTemperatureCelsius() < -5;
        conditions.setPowderDay(isPowderDay);

        return conditions;
    }

    private int calculateOpenPercentage(WeatherInfo weather) {
        int percentage = 95;
        
        if (weather.getWindSpeedKmh() > 60) {
            percentage -= 20;
        } else if (weather.getWindSpeedKmh() > 40) {
            percentage -= 10;
        }
        
        if ("Blizzard".equals(weather.getConditions()) || "Heavy Snow".equals(weather.getConditions())) {
            percentage -= 5;
        }
        
        if (weather.getTemperatureCelsius() > 2) {
            percentage -= 15;
        }
        
        return Math.max(70, percentage);
    }

    private String inferSnowQuality(WeatherInfo weather) {
        double temp = weather.getTemperatureCelsius();
        double snowfall = weather.getSnowfallCm();
        int cloudCover = weather.getCloudCoverPercent();
        
        if (snowfall > 10 && temp < -5) {
            return "Fresh Powder";
        }
        
        if (snowfall > 5 && temp < -3) {
            return "Powder";
        }
        
        if (temp < -2 && snowfall > 1) {
            return "Packed Powder";
        }
        
        if (temp < 0 && cloudCover < 50) {
            return "Groomed";
        }
        
        if (temp < -1) {
            return "Hard Pack";
        }
        
        return "Variable (Icy in spots)";
    }

    public String getConditionsAsJson(WeatherInfo weather, SkiConditions ski) {
        return String.format(
            "{\"location\":\"%s\",\"weather\":{\"temperature_celsius\":%.1f,\"conditions\":\"%s\"," +
            "\"wind_speed_kmh\":%.1f,\"snowfall_cm\":%.1f,\"humidity_percent\":%d," +
            "\"cloud_cover_percent\":%d,\"precipitation_mm\":%.1f}," +
            "\"ski_conditions\":{\"resort_name\":\"%s\",\"open_slopes\":%d,\"total_slopes\":%d," +
            "\"snow_depth_cm\":%.1f,\"snow_quality\":\"%s\",\"difficulty\":\"%s\",\"is_powder_day\":%b}}",
            weather.getLocation(), weather.getTemperatureCelsius(), weather.getConditions(),
            weather.getWindSpeedKmh(), weather.getSnowfallCm(), weather.getHumidityPercent(),
            weather.getCloudCoverPercent(), weather.getPrecipitationMm(),
            ski.getResortName(), ski.getOpenSlopesCount(), ski.getTotalSlopesCount(),
            ski.getSnowDepthCm(), ski.getSnowQuality(), ski.getDifficulty(), ski.isPowderDay()
        );
    }
}
