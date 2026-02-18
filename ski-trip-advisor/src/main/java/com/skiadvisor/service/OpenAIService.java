package com.skiadvisor.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionMessage;
import com.openai.azure.credential.AzureApiKeyCredential;
import com.skiadvisor.model.Product;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Service for interacting with OpenAI API to generate equipment recommendations
 */
public class OpenAIService {
    private OpenAIClient client;
    private String deploymentName;

    public OpenAIService() throws IOException {
        initializeClient();
    }

    private void initializeClient() throws IOException {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new IOException("Unable to find database.properties");
            }
            props.load(input);
            
            String endpoint = props.getProperty("openai.endpoint");
            String apiKey = props.getProperty("openai.apikey");
            deploymentName = props.getProperty("openai.deployment");
            
            client = OpenAIOkHttpClient.builder()
                .baseUrl(endpoint)
                .credential(AzureApiKeyCredential.create(apiKey))
                .build();
        }
    }

    /**
     * Generate equipment recommendations based on weather, ski conditions, and available products
     */
    public String generateRecommendations(String weatherData, String skiConditions, 
                                         List<Product> availableProducts, 
                                         String userPreferences) {
        
        // Build product catalog summary
        StringBuilder productCatalog = new StringBuilder();
        productCatalog.append("Available Products:\n");
        
        for (Product product : availableProducts) {
            productCatalog.append(String.format("- %s - %s - $%.2f - Stock: %d - %s\n",
                product.getName(), product.getCategoryName(),
                product.getPrice(), product.getQuantity(),
                product.getDescription() != null ? product.getDescription() : ""));
        }

        // Create the system prompt
        String systemPrompt = "You are an expert ski equipment advisor. " +
            "Based on the weather conditions, ski slope conditions, available products, " +
            "and user preferences, recommend the most suitable ski equipment. " +
            "Provide specific product recommendations from the available catalog, " +
            "explain your reasoning, and give practical tips for the ski trip. " +
            "Format your response clearly with sections for: SUMMARY, RECOMMENDED PRODUCTS, " +
            "REASONING, and TIPS.";

        // Create the user prompt
        String userPrompt = String.format(
            "Weather and Ski Conditions:\n%s\n\n" +
            "Ski Slope Conditions:\n%s\n\n" +
            "%s\n\n" +
            "User Preferences: %s\n\n" +
            "Please recommend the best equipment for this ski trip.",
            weatherData, skiConditions, productCatalog.toString(), userPreferences
        );

        try {
            ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                .model(ChatModel.of(deploymentName))
                .addSystemMessage(systemPrompt)
                .addUserMessage(userPrompt)
                .maxCompletionTokens(1500)
                .build();

            ChatCompletion chatCompletion = client.chat().completions().create(createParams);
            
            if (chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
                ChatCompletionMessage message = chatCompletion.choices().get(0).message();
                return message.content().orElse("No content in response");
            } else {
                return "Error: No response from AI service";
            }
            
        } catch (Exception e) {
            return "Error generating recommendations: " + e.getMessage() + 
                   "\n\nPlease check your OpenAI API configuration in database.properties";
        }
    }

    /**
     * Generate a simple recommendation without full product catalog
     */
    public String generateQuickRecommendation(String weatherData, String userSkillLevel) {
        String systemPrompt = "You are an expert ski equipment advisor. " +
            "Provide concise equipment recommendations based on weather and skill level.";

        String userPrompt = String.format(
            "Weather Conditions:\n%s\n\nSkier Skill Level: %s\n\n" +
            "What equipment categories (skis, boots, wear, accessories) should they focus on?",
            weatherData, userSkillLevel
        );

        try {
            ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                .model(ChatModel.of(deploymentName))
                .addSystemMessage(systemPrompt)
                .addUserMessage(userPrompt)
                .maxCompletionTokens(500)
                .build();

            ChatCompletion chatCompletion = client.chat().completions().create(createParams);
            
            if (chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
                ChatCompletionMessage message = chatCompletion.choices().get(0).message();
                return message.content().orElse("No content in response");
            } else {
                return "Error: No response from AI service";
            }
            
        } catch (Exception e) {
            return "Error generating recommendations: " + e.getMessage();
        }
    }
}
