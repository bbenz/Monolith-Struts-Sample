package com.skishop.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.azure.credential.AzureApiKeyCredential;
import com.skishop.model.dto.SkiAdvisorResult.ProductSummary;
import jakarta.annotation.PostConstruct;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkiOpenAIService {
    private static final Logger log = LoggerFactory.getLogger(SkiOpenAIService.class);

    @Value("${openai.endpoint}")
    private String endpoint;

    @Value("${openai.apikey}")
    private String apiKey;

    @Value("${openai.deployment}")
    private String deploymentName;

    private OpenAIClient client;
    private final Parser markdownParser = Parser.builder().build();
    private final HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();

    @PostConstruct
    public void init() {
        client = OpenAIOkHttpClient.builder()
            .baseUrl(endpoint)
            .credential(AzureApiKeyCredential.create(apiKey))
            .build();
    }

    public String generateRecommendations(String weatherData, String conditionsSummary,
                                          List<ProductSummary> products, String userPreferences) {
        StringBuilder productCatalog = new StringBuilder("Available Products:\n");
        for (ProductSummary p : products) {
            productCatalog.append(String.format("- %s - %s - $%.2f - Stock: %d - %s\n",
                p.getName(), p.getCategoryName(),
                p.getPrice(), p.getQuantity(),
                p.getDescription() != null ? p.getDescription() : ""));
        }

        String systemPrompt = "You are an expert ski equipment advisor. " +
            "Based on the weather conditions, ski slope conditions, available products, " +
            "and user preferences, recommend the most suitable ski equipment. " +
            "Provide specific product recommendations from the available catalog, " +
            "explain your reasoning, and give practical tips for the ski trip. " +
            "Format your response clearly with sections for: SUMMARY, RECOMMENDED PRODUCTS, " +
            "REASONING, and TIPS.";

        String userPrompt = String.format(
            "Weather and Ski Conditions:\n%s\n\n" +
            "Ski Slope Conditions:\n%s\n\n" +
            "%s\n\n" +
            "User Preferences: %s\n\n" +
            "Please recommend the best equipment for this ski trip.",
            weatherData, conditionsSummary, productCatalog.toString(), userPreferences
        );

        try {
            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.of(deploymentName))
                .addSystemMessage(systemPrompt)
                .addUserMessage(userPrompt)
                .maxCompletionTokens(1500)
                .build();

            ChatCompletion completion = client.chat().completions().create(params);

            if (completion.choices() != null && !completion.choices().isEmpty()) {
                String markdown = completion.choices().get(0).message().content().orElse("No content in response");
                Node document = markdownParser.parse(markdown);
                return htmlRenderer.render(document);
            }
            return "<p>Error: No response from AI service</p>";
        } catch (Exception e) {
            log.error("Error generating recommendations", e);
            return "Error generating recommendations: " + e.getMessage();
        }
    }
}
