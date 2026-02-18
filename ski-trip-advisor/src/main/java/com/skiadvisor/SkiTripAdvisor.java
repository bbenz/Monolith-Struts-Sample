package com.skiadvisor;

import com.skiadvisor.model.Product;
import com.skiadvisor.service.DatabaseService;
import com.skiadvisor.service.OpenAIService;
import com.skiadvisor.service.WeatherService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * AI Ski Trip Advisor - Main Application
 * 
 * This application provides intelligent ski equipment recommendations based on:
 * - Current weather conditions
 * - Ski slope conditions
 * - Available products from the ski shop database
 * - User preferences and skill level
 */
public class SkiTripAdvisor {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   AI SKI TRIP ADVISOR                      ║");
        System.out.println("║   Powered by OpenAI & Ski Shop Database    ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println();

        try {
            // Initialize services
            DatabaseService dbService = new DatabaseService();
            WeatherService weatherService = new WeatherService();
            OpenAIService aiService = new OpenAIService();

            Scanner scanner = new Scanner(System.in);

            // Get user input
            System.out.print("Enter ski resort location: ");
            String location = scanner.nextLine();

            System.out.print("Enter your skill level (Beginner/Intermediate/Advanced/Expert): ");
            String skillLevel = scanner.nextLine();

            System.out.print("What type of skiing? (On-piste/Off-piste/All-mountain/Racing): ");
            String skiingType = scanner.nextLine();

            System.out.print("Budget range (Low/Medium/High): ");
            String budget = scanner.nextLine();

            System.out.println("\n" + "=".repeat(50));
            System.out.println("FETCHING WEATHER AND SKI CONDITIONS...");
            System.out.println("=".repeat(50));

            // Get weather and ski conditions
            String weatherData = weatherService.getConditionsAsJson(location);
            String conditionsSummary = weatherService.getConditionsSummary(location);
            System.out.println(conditionsSummary);

            System.out.println("\n" + "=".repeat(50));
            System.out.println("LOADING AVAILABLE EQUIPMENT FROM DATABASE...");
            System.out.println("=".repeat(50));

            // Get product statistics
            System.out.println(dbService.getProductStatistics());

            // Fetch relevant products from database
            List<Product> allProducts = new ArrayList<>();
            
            // Get products from all relevant categories
            // "Snowboards" also fetches sub-categories: All-round, Freestyle, Freeride, Carving
            String[] categories = {"Snowboards", "Boots", "Bindings", "Wax"};
            for (String category : categories) {
                List<Product> categoryProducts = dbService.getProductsByCategory(category);
                allProducts.addAll(categoryProducts);
            }

            System.out.println("Total available products: " + allProducts.size());

            // Filter products based on budget
            List<Product> filteredProducts = filterProductsByBudget(allProducts, budget);
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("GENERATING AI RECOMMENDATIONS...");
            System.out.println("=".repeat(50));

            // Build user preferences string
            String userPreferences = String.format(
                "Skill Level: %s, Skiing Type: %s, Budget: %s",
                skillLevel, skiingType, budget
            );

            // Generate recommendations using AI
            String recommendations = aiService.generateRecommendations(
                weatherData,
                conditionsSummary,
                filteredProducts,
                userPreferences
            );

            System.out.println("\n" + "=".repeat(50));
            System.out.println("AI EQUIPMENT RECOMMENDATIONS");
            System.out.println("=".repeat(50));
            System.out.println(recommendations);

            // Ask if user wants detailed product information
            System.out.print("\n\nWould you like to see detailed product listings? (yes/no): ");
            String showDetails = scanner.nextLine();

            if (showDetails.trim().toLowerCase().startsWith("y")) {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("DETAILED PRODUCT LISTINGS");
                System.out.println("=".repeat(50));
                
                for (String category : categories) {
                    List<Product> categoryProducts = dbService.getProductsByCategory(category);
                    if (!categoryProducts.isEmpty()) {
                        System.out.println("\n--- " + category.toUpperCase() + " ---");
                        int count = 0;
                        for (Product product : categoryProducts) {
                            if (count++ >= 5) break; // Show top 5 per category
                            System.out.println(product);
                        }
                    }
                }
            }

            scanner.close();
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("Thank you for using AI Ski Trip Advisor!");
            System.out.println("Have a great ski trip! 🎿");
            System.out.println("=".repeat(50));

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("TROUBLESHOOTING TIPS:");
            System.out.println("=".repeat(50));
            System.out.println("1. Make sure PostgreSQL database is running");
            System.out.println("2. Update database.properties with your database credentials");
            System.out.println("3. Update OpenAI API key in database.properties");
            System.out.println("4. Run: mvn clean package");
            System.out.println("5. Run: mvn exec:java");
        }
    }

    /**
     * Filter products based on budget range
     */
    private static List<Product> filterProductsByBudget(List<Product> products, String budget) {
        List<Product> filtered = new ArrayList<>();
        
        for (Product product : products) {
            if (product.getPrice() == null) continue;
            
            double price = product.getPrice().doubleValue();
            
            switch (budget.toLowerCase()) {
                case "low":
                    if (price < 300) filtered.add(product);
                    break;
                case "medium":
                    if (price >= 300 && price < 600) filtered.add(product);
                    break;
                case "high":
                    if (price >= 600) filtered.add(product);
                    break;
                default:
                    filtered.add(product); // Include all if budget not specified
            }
        }
        
        return filtered.isEmpty() ? products : filtered;
    }
}
