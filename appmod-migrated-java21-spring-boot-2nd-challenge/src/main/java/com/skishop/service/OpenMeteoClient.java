package com.skishop.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skishop.model.dto.WeatherInfo;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Client for Open-Meteo Weather API
 * Open-Meteo is a free, open-source weather API that doesn't require authentication
 * API Documentation: https://open-meteo.com/en/docs
 */
@Component
public class OpenMeteoClient {
    private static final Logger log = LoggerFactory.getLogger(OpenMeteoClient.class);
    
    private final String weatherApiUrl;
    private final String geocodingApiUrl;
    private final Gson gson;
    private final CloseableHttpClient httpClient;

    public OpenMeteoClient() {
        this.weatherApiUrl = "https://api.open-meteo.com/v1/forecast";
        this.geocodingApiUrl = "https://geocoding-api.open-meteo.com/v1/search";
        this.gson = new Gson();
        this.httpClient = HttpClients.createDefault();
    }

    /**
     * Fetch current weather data for a location
     * @param location Location name (e.g., "Whistler", "Zermatt", "Aspen")
     * @return WeatherInfo object with current conditions
     * @throws IOException if API call fails
     */
    public WeatherInfo fetchWeather(String location) throws IOException {
        log.debug("Fetching weather for location: {}", location);
        
        // Step 1: Geocode location to get latitude/longitude
        double[] coordinates = geocodeLocation(location);
        double latitude = coordinates[0];
        double longitude = coordinates[1];
        
        log.debug("Location coordinates: lat={}, lon={}", latitude, longitude);
        
        // Step 2: Fetch weather data for coordinates
        String url = String.format(
            "%s?latitude=%.4f&longitude=%.4f&current=temperature_2m,precipitation,snowfall,cloudcover,windspeed_10m,relativehumidity_2m&daily=snowfall_sum&timezone=auto",
            weatherApiUrl, latitude, longitude
        );
        
        String response = executeHttpGet(url);
        JsonObject json = gson.fromJson(response, JsonObject.class);
        
        // Parse weather data
        WeatherInfo weather = new WeatherInfo();
        weather.setLocation(location);
        
        JsonObject current = json.getAsJsonObject("current");
        weather.setTemperatureCelsius(current.get("temperature_2m").getAsDouble());
        weather.setWindSpeedKmh(current.get("windspeed_10m").getAsDouble());
        weather.setHumidityPercent(current.get("relativehumidity_2m").getAsInt());
        weather.setCloudCoverPercent(current.get("cloudcover").getAsInt());
        
        // Snowfall in cm (convert from mm if needed)
        double snowfallMm = current.get("snowfall").getAsDouble();
        weather.setSnowfallCm(snowfallMm / 10.0); // mm to cm
        
        // Precipitation in mm
        weather.setPrecipitationMm(current.get("precipitation").getAsDouble());
        
        // Determine weather conditions based on data
        weather.setConditions(determineConditions(
            weather.getCloudCoverPercent(),
            weather.getSnowfallCm(),
            weather.getPrecipitationMm()
        ));
        
        log.debug("Weather fetched successfully: {}", weather);
        return weather;
    }

    /**
     * Geocode a location name to latitude/longitude
     * @param location Location name
     * @return Array with [latitude, longitude]
     * @throws IOException if geocoding fails
     */
    private double[] geocodeLocation(String location) throws IOException {
        String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);
        String url = String.format("%s?name=%s&count=1&language=en&format=json", 
            geocodingApiUrl, encodedLocation);
        
        String response = executeHttpGet(url);
        JsonObject json = gson.fromJson(response, JsonObject.class);
        
        if (!json.has("results") || json.getAsJsonArray("results").isEmpty()) {
            log.warn("Location not found: {}, using default coordinates (Whistler)", location);
            // Default to famous ski resort coordinates if location not found
            // Using Whistler Blackcomb as default (50.1163° N, 122.9574° W)
            return new double[]{50.1163, -122.9574};
        }
        
        JsonObject result = json.getAsJsonArray("results").get(0).getAsJsonObject();
        double latitude = result.get("latitude").getAsDouble();
        double longitude = result.get("longitude").getAsDouble();
        
        return new double[]{latitude, longitude};
    }

    /**
     * Execute HTTP GET request and return response body
     */
    private String executeHttpGet(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (org.apache.hc.core5.http.ParseException e) {
            throw new IOException("Failed to parse HTTP response", e);
        }
    }

    /**
     * Determine weather conditions description based on data
     */
    private String determineConditions(int cloudCover, double snowfallCm, double precipitationMm) {
        if (snowfallCm > 5) {
            return "Heavy Snow";
        } else if (snowfallCm > 1) {
            return "Light Snow";
        } else if (precipitationMm > 0) {
            return "Light Rain/Snow";
        } else if (cloudCover > 80) {
            return "Cloudy";
        } else if (cloudCover > 40) {
            return "Partly Cloudy";
        } else {
            return "Clear";
        }
    }

    /**
     * Close the HTTP client
     */
    public void close() throws IOException {
        httpClient.close();
    }
}
