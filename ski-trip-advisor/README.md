# AI Ski Trip Advisor

An intelligent ski equipment recommendation system that uses OpenAI's GPT model to provide personalized equipment suggestions based on weather conditions, ski slope conditions, and available products from the ski shop database.

## Features

- **Real-time Weather Integration**: Fetches current weather and ski conditions (mock implementation - ready for real API integration)
- **Database Integration**: Connects to the existing ski shop PostgreSQL database to access product inventory
- **AI-Powered Recommendations**: Uses OpenAI/Azure OpenAI to generate intelligent equipment recommendations
- **Personalized Suggestions**: Considers user skill level, skiing type, and budget
- **Comprehensive Product Catalog**: Accesses 140+ products across 7 categories (Skis, Boots, Wear, Helmets, Gloves, Poles, Wax)

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- PostgreSQL database (from the main ski shop application)
- Azure OpenAI API access (or OpenAI API key)

## Configuration

1. **Database Configuration**

Edit `src/main/resources/database.properties`:

```properties
db.url=jdbc:postgresql://localhost:5432/skishop
db.username=your_username
db.password=your_password
```

2. **OpenAI Configuration**

Update the OpenAI settings in `database.properties`:

```properties
openai.endpoint=https://your-endpoint.cognitiveservices.azure.com/openai/v1/
openai.apikey=your-api-key-here
openai.deployment=gpt-5.2-chat
```

## Installation

1. Navigate to the ski-trip-advisor directory:
```bash
cd ski-trip-advisor
```

2. Build the project:
```bash
mvn clean package
```

## Running the Application

### Method 1: Using Maven
```bash
mvn exec:java
```

### Method 2: Using Java
```bash
java -jar target/ski-trip-advisor-1.0.0.jar
```

## Usage

When you run the application, you'll be prompted for:

1. **Ski Resort Location**: Name of the ski resort (e.g., "Whistler", "Aspen", "Niseko")
2. **Skill Level**: Your skiing ability (Beginner/Intermediate/Advanced/Expert)
3. **Skiing Type**: Your preferred style (On-piste/Off-piste/All-mountain/Racing)
4. **Budget Range**: Your budget category (Low/Medium/High)

The application will then:
- Fetch weather and ski conditions for the location
- Query the database for available equipment
- Generate AI-powered equipment recommendations
- Provide specific product suggestions with reasoning
- Offer practical tips for your ski trip

## Example Output

```
╔════════════════════════════════════════════╗
║   AI SKI TRIP ADVISOR                      ║
║   Powered by OpenAI & Ski Shop Database    ║
╚════════════════════════════════════════════╝

Enter ski resort location: Niseko
Enter your skill level: Advanced
What type of skiing?: All-mountain
Budget range: Medium

==================================================
FETCHING WEATHER AND SKI CONDITIONS...
==================================================
Niseko: -8.5°C, Powder, Wind: 12.3 km/h, Snowfall: 15.2 cm, Humidity: 78%
Niseko: 95/120 slopes open, Snow depth: 185.3 cm, Quality: Fresh Powder, Difficulty: Advanced (POWDER DAY!)

==================================================
LOADING AVAILABLE EQUIPMENT FROM DATABASE...
==================================================
=== PRODUCT INVENTORY STATISTICS ===
Boots          :  20 products, Avg Price: $410.25
Glove          :  20 products, Avg Price: $99.29
Helmet         :  20 products, Avg Price: $163.43
Pole           :  20 products, Avg Price: $112.94
Ski            :  20 products, Avg Price: $621.54
Wax            :  20 products, Avg Price: $13.31
Wear           :  20 products, Avg Price: $390.75

==================================================
AI EQUIPMENT RECOMMENDATIONS
==================================================
[AI generates personalized recommendations based on conditions]
```

## Project Structure

```
ski-trip-advisor/
├── src/main/java/com/skiadvisor/
│   ├── SkiTripAdvisor.java           # Main application
│   ├── model/
│   │   ├── Product.java               # Product data model
│   │   ├── WeatherInfo.java           # Weather data model
│   │   ├── SkiConditions.java         # Ski conditions model
│   │   └── EquipmentRecommendation.java # Recommendation model
│   └── service/
│       ├── DatabaseService.java       # Database access layer
│       ├── WeatherService.java        # Weather API integration
│       └── OpenAIService.java         # OpenAI API integration
├── src/main/resources/
│   └── database.properties            # Configuration file
├── pom.xml                            # Maven dependencies
└── README.md                          # This file
```

## Database Schema

The application queries the following tables from the ski shop database:

- `products` - Equipment inventory
- `categories` - Product categories
- `prices` - Product pricing
- `inventory` - Stock levels

## Future Enhancements

- [ ] Integrate with real weather APIs (OpenWeatherMap, WeatherAPI)
- [ ] Connect to actual ski resort APIs for live slope conditions
- [ ] Add user authentication and saved preferences
- [ ] Build REST API endpoints for web/mobile integration
- [ ] Implement equipment comparison features
- [ ] Add booking/reservation functionality
- [ ] Create web UI with React/Angular
- [ ] Add multi-language support
- [ ] Implement caching for weather data
- [ ] Add email notifications for weather alerts

## Technologies Used

- **Java 11**: Core programming language
- **Maven**: Build and dependency management
- **PostgreSQL**: Database
- **OpenAI Java SDK**: AI integration
- **Azure OpenAI**: AI service provider
- **Gson**: JSON processing
- **Apache HttpClient**: HTTP requests
- **SLF4J**: Logging

## Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL is running
- Verify database credentials in `database.properties`
- Check that the skishop database exists and is populated

### OpenAI API Errors
- Verify your API key is correct
- Check that your deployment name matches your Azure OpenAI deployment
- Ensure your API endpoint URL is correct

### Build Errors
- Make sure you're using Java 11 or higher: `java -version`
- Clean and rebuild: `mvn clean install`

## License

This project is part of the Monolith-Struts-Sample ski shop application.

## Support

For issues or questions, please refer to the main project documentation.

---

**Happy Skiing! 🎿⛷️**
