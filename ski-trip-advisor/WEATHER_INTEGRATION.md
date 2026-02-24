# Weather Integration Guide

## Overview

The Ski Trip Advisor now integrates with **Open-Meteo**, a free open-source weather API that provides real-time weather data without requiring authentication or API keys.

## Features

- ✅ Real-time weather data (temperature, precipitation, snowfall, wind, humidity, cloud cover)
- ✅ Intelligent ski condition inference based on actual weather
- ✅ Configurable mock/real modes for testing and production
- ✅ Automatic fallback to mock data if API is unavailable
- ✅ Geocoding support (converts location names to coordinates)
- ✅ No API key required

## Configuration

Weather service settings are configured in `src/main/resources/database.properties`:

```properties
# Weather Service Configuration
weather.service.mode=real
weather.api.url=https://api.open-meteo.com/v1/forecast
weather.geocoding.url=https://geocoding-api.open-meteo.com/v1/search
weather.fallback.enabled=true
```

### Configuration Options

| Property | Values | Description |
|----------|--------|-------------|
| `weather.service.mode` | `mock` or `real` | Controls whether to use real API or mock data |
| `weather.api.url` | URL | Open-Meteo forecast API endpoint |
| `weather.geocoding.url` | URL | Open-Meteo geocoding API endpoint |
| `weather.fallback.enabled` | `true` or `false` | Fall back to mock data if API fails |

## How It Works

### 1. Weather Data Fetching

When `weather.service.mode=real`:

1. User enters a ski resort location (e.g., "Whistler", "Zermatt", "Aspen")
2. System geocodes the location to latitude/longitude using Open-Meteo Geocoding API
3. System fetches current weather data from Open-Meteo Forecast API:
   - Temperature (°C)
   - Precipitation (mm)
   - Snowfall (cm)
   - Wind speed (km/h)
   - Humidity (%)
   - Cloud cover (%)

### 2. Ski Condition Inference

The system intelligently infers ski conditions from weather data:

#### Snow Quality Algorithm

| Conditions | Snow Quality |
|------------|-------------|
| Snowfall > 10cm + Temp < -5°C | **Fresh Powder** |
| Snowfall > 5cm + Temp < -3°C | **Powder** |
| Temp < -2°C + Snowfall > 1cm | **Packed Powder** |
| Temp < 0°C + Cloud cover < 50% | **Groomed** |
| Temp < -1°C + No fresh snow | **Hard Pack** |
| Warmer temps | **Variable (Icy in spots)** |

#### Slope Availability Algorithm

Starts with 95% slopes open, reduces based on:
- **High winds** (> 60 km/h): -20% (lift closures)
- **Moderate winds** (> 40 km/h): -10%
- **Heavy snow/blizzard**: -5%
- **Warm temps** (> 2°C): -15% (rain risk closes higher slopes)

Minimum: 70% slopes open

#### Powder Day Determination

**Powder Day = Recent snowfall > 10cm + Temperature < -5°C**

### 3. Fallback Mechanism

If `weather.fallback.enabled=true` and the API call fails:
- System automatically falls back to mock weather data
- User is notified via console message
- Application continues without interruption

## Testing

### Test Real Weather Mode

1. Set `weather.service.mode=real` in `database.properties`
2. Run the application: `mvn clean package && mvn exec:java`
3. Enter real ski resort names:
   - Whistler (Canada)
   - Zermatt (Switzerland)
   - Aspen (USA)
   - Niseko (Japan)
   - Chamonix (France)

### Test Mock Mode

1. Set `weather.service.mode=mock` in `database.properties`
2. Run the application: `mvn clean package && mvn exec:java`
3. Enter any location name (data will be randomly generated)

### Test Fallback Mechanism

1. Set `weather.service.mode=real` and `weather.fallback.enabled=true`
2. Temporarily disable internet or use invalid API URL
3. Verify application falls back to mock data gracefully

## API Details

### Open-Meteo Forecast API

**Endpoint:** `https://api.open-meteo.com/v1/forecast`

**Parameters used:**
- `latitude`, `longitude` - Location coordinates
- `current=temperature_2m,precipitation,snowfall,cloudcover,windspeed_10m,relativehumidity_2m`
- `daily=snowfall_sum` - Daily snowfall accumulation
- `timezone=auto` - Automatic timezone detection

**Example request:**
```
https://api.open-meteo.com/v1/forecast?latitude=50.1163&longitude=-122.9574&current=temperature_2m,precipitation,snowfall,cloudcover,windspeed_10m,relativehumidity_2m&daily=snowfall_sum&timezone=auto
```

### Open-Meteo Geocoding API

**Endpoint:** `https://geocoding-api.open-meteo.com/v1/search`

**Parameters:**
- `name` - Location name to search for
- `count=1` - Return top result
- `language=en` - English language
- `format=json` - JSON response

**Example request:**
```
https://geocoding-api.open-meteo.com/v1/search?name=Whistler&count=1&language=en&format=json
```

## Code Architecture

### New Classes

**`OpenMeteoClient.java`**
- HTTP client for Open-Meteo API
- Methods: `fetchWeather(location)`, `geocodeLocation(location)`
- Uses Apache HttpClient 5 and Gson for JSON parsing

**Updates to `WeatherService.java`**
- Constructor loads configuration from `database.properties`
- Mode switching logic (mock vs real)
- Ski condition inference methods
- Fallback error handling

**Updates to `WeatherInfo.java`**
- Added `cloudCoverPercent` field (for snow quality inference)
- Added `precipitationMm` field (distinguish rain vs snow)
- Backward-compatible constructors

### Dependencies

All required dependencies already exist in `pom.xml`:

```xml
<!-- HTTP Client for weather API calls -->
<dependency>
    <groupId>org.apache.httpcomponents.client5</groupId>
    <artifactId>httpclient5</artifactId>
    <version>5.3.1</version>
</dependency>

<!-- JSON parsing -->
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

## Benefits of Open-Meteo

1. **Free & Open Source** - No API key required, no rate limits for reasonable use
2. **High Quality Data** - Integrates multiple professional weather models (NOAA, DWD, etc.)
3. **Global Coverage** - Works for ski resorts worldwide
4. **Reliable** - 99.9% uptime, fast response times
5. **Privacy-Focused** - No user tracking, no authentication
6. **Well-Documented** - Excellent API documentation at https://open-meteo.com/en/docs

## Troubleshooting

### "Weather API error" Message

**Possible causes:**
1. No internet connection
2. Invalid location name (geocoding failed)
3. Open-Meteo API temporarily unavailable

**Solution:**
- Check internet connectivity
- Verify location name is valid (try famous ski resorts)
- Enable fallback mode: `weather.fallback.enabled=true`

### Mock Data Always Returned

**Check:**
1. `weather.service.mode` is set to `real` (not `mock`)
2. No API errors in console output
3. Internet connectivity is working

### Compilation Errors

**Run:**
```bash
mvn clean compile
```

**If errors persist:**
- Verify Java 25 is installed: `java --version`
- Verify Maven dependencies downloaded: `mvn dependency:resolve`

## Example Output

### Real Weather Mode (Whistler)

```
==================================================
FETCHING WEATHER AND SKI CONDITIONS...
==================================================
Location: Whistler
Weather: Whistler: -8.2°C, Light Snow, Wind: 15.3 km/h, Snowfall: 6.2 cm, Humidity: 82%, Clouds: 85%, Precip: 6.8 mm
Ski Conditions: Whistler: 119/140 slopes open, Snow depth: 205.2 cm, Quality: Powder, Difficulty: Advanced, Powder Day: NO

==================================================
GENERATING AI RECOMMENDATIONS...
==================================================
```

### Mock Mode

```
==================================================
FETCHING WEATHER AND SKI CONDITIONS...
==================================================
Location: TestResort
Weather: TestResort: -11.5°C, Clear, Wind: 22.8 km/h, Snowfall: 12.3 cm, Humidity: 75%, Clouds: 35%, Precip: 3.2 mm
Ski Conditions: TestResort: 98/125 slopes open, Snow depth: 187.4 cm, Quality: Fresh Powder, Difficulty: Intermediate, Powder Day: YES
```

## Future Enhancements

Possible improvements for future versions:

1. **Historical weather data** - Analyze snowfall trends over past week
2. **Forecast data** - Show 7-day weather forecast
3. **Multiple resorts comparison** - Compare conditions across resorts
4. **Avalanche risk** - Integrate avalanche warning systems
5. **Lift status** - Real-time lift operational status (requires resort APIs)
6. **Snow report images** - Fetch recent snow condition photos
7. **Caching** - Cache weather data to reduce API calls

## Resources

- **Open-Meteo Documentation:** https://open-meteo.com/en/docs
- **Open-Meteo GitHub:** https://github.com/open-meteo/open-meteo
- **Apache HttpClient 5:** https://hc.apache.org/httpcomponents-client-5.3.x/
- **Gson Documentation:** https://github.com/google/gson

## License

The weather integration uses Open-Meteo API under Attribution 4.0 International (CC BY 4.0).
Please ensure appropriate attribution when deploying this application publicly.
