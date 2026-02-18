package com.skishop.service;

import com.skishop.model.dto.SkiConditions;
import com.skishop.model.dto.WeatherInfo;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SkiWeatherService {
    private final Random random = new Random();

    public WeatherInfo getWeather(String location) {
        WeatherInfo weather = new WeatherInfo();
        weather.setLocation(location);
        weather.setTemperatureCelsius(-15 + random.nextDouble() * 10);

        String[] conditions = {"Clear", "Partly Cloudy", "Cloudy", "Light Snow", "Heavy Snow", "Blizzard"};
        weather.setConditions(conditions[random.nextInt(conditions.length)]);
        weather.setWindSpeedKmh(5 + random.nextDouble() * 35);
        weather.setSnowfallCm(random.nextDouble() * 20);
        weather.setHumidityPercent(60 + random.nextInt(30));

        return weather;
    }

    public SkiConditions getSkiConditions(String resortName) {
        SkiConditions conditions = new SkiConditions();
        conditions.setResortName(resortName);

        int totalSlopes = 50 + random.nextInt(100);
        int openSlopes = (int) (totalSlopes * (0.7 + random.nextDouble() * 0.3));
        conditions.setOpenSlopesCount(openSlopes);
        conditions.setTotalSlopesCount(totalSlopes);
        conditions.setSnowDepthCm(100 + random.nextDouble() * 150);

        String[] qualities = {"Hard Pack", "Packed Powder", "Powder", "Fresh Powder", "Groomed"};
        conditions.setSnowQuality(qualities[random.nextInt(qualities.length)]);

        String[] difficulties = {"Beginner Friendly", "Intermediate", "Advanced", "Expert Only"};
        conditions.setDifficulty(difficulties[random.nextInt(difficulties.length)]);
        conditions.setPowderDay(random.nextDouble() > 0.7);

        return conditions;
    }

    public String getConditionsAsJson(WeatherInfo weather, SkiConditions ski) {
        return String.format(
            "{\"location\":\"%s\",\"weather\":{\"temperature_celsius\":%.1f,\"conditions\":\"%s\"," +
            "\"wind_speed_kmh\":%.1f,\"snowfall_cm\":%.1f,\"humidity_percent\":%d}," +
            "\"ski_conditions\":{\"resort_name\":\"%s\",\"open_slopes\":%d,\"total_slopes\":%d," +
            "\"snow_depth_cm\":%.1f,\"snow_quality\":\"%s\",\"difficulty\":\"%s\",\"is_powder_day\":%b}}",
            weather.getLocation(), weather.getTemperatureCelsius(), weather.getConditions(),
            weather.getWindSpeedKmh(), weather.getSnowfallCm(), weather.getHumidityPercent(),
            ski.getResortName(), ski.getOpenSlopesCount(), ski.getTotalSlopesCount(),
            ski.getSnowDepthCm(), ski.getSnowQuality(), ski.getDifficulty(), ski.isPowderDay()
        );
    }
}
