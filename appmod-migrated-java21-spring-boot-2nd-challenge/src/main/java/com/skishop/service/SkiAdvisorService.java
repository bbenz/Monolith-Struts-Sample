package com.skishop.service;

import com.skishop.model.dto.*;
import com.skishop.model.dto.SkiAdvisorResult.ProductSummary;
import com.skishop.model.entity.Category;
import com.skishop.model.entity.Price;
import com.skishop.model.entity.Product;
import com.skishop.repository.CategoryRepository;
import com.skishop.repository.InventoryRepository;
import com.skishop.repository.PriceRepository;
import com.skishop.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SkiAdvisorService {
    private static final Logger log = LoggerFactory.getLogger(SkiAdvisorService.class);

    private final SkiWeatherService weatherService;
    private final SkiOpenAIService openAIService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PriceRepository priceRepository;
    private final InventoryRepository inventoryRepository;

    private static final String[] EQUIPMENT_CATEGORIES = {"Snowboards", "Boots", "Bindings", "Wax"};

    public SkiAdvisorService(SkiWeatherService weatherService, SkiOpenAIService openAIService,
                             ProductRepository productRepository, CategoryRepository categoryRepository,
                             PriceRepository priceRepository, InventoryRepository inventoryRepository) {
        this.weatherService = weatherService;
        this.openAIService = openAIService;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.priceRepository = priceRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public SkiAdvisorResult getRecommendations(SkiAdvisorForm form) {
        WeatherInfo weather = weatherService.getWeather(form.getLocation());
        SkiConditions ski = weatherService.getSkiConditions(form.getLocation());
        String weatherJson = weatherService.getConditionsAsJson(weather, ski);
        String conditionsSummary = weather.toString() + "\n" + ski.toString();

        // Gather category IDs for equipment categories (including subcategories)
        List<String> categoryIds = new ArrayList<>();
        for (String catName : EQUIPMENT_CATEGORIES) {
            categoryRepository.findByNameIgnoreCase(catName).ifPresent(cat -> {
                categoryIds.add(cat.getId());
                // Include subcategories
                categoryRepository.findByParentId(cat.getId())
                    .forEach(sub -> categoryIds.add(sub.getId()));
            });
        }

        // Fetch active products in those categories
        List<Product> products = categoryIds.isEmpty()
            ? productRepository.findByStatus("ACTIVE")
            : productRepository.findByCategoryIdInAndStatus(categoryIds, "ACTIVE");

        // Build product summaries with price and inventory
        List<ProductSummary> summaries = new ArrayList<>();
        for (Product p : products) {
            BigDecimal price = priceRepository.findByProductId(p.getId()).stream()
                .findFirst().map(Price::getRegularPrice).orElse(BigDecimal.ZERO);
            int qty = inventoryRepository.findByProductId(p.getId())
                .map(inv -> inv.getQuantity()).orElse(0);
            String categoryName = categoryRepository.findById(p.getCategoryId())
                .map(Category::getName).orElse("Unknown");

            summaries.add(new ProductSummary(
                p.getId(), p.getName(), p.getDescription(),
                categoryName, price, qty
            ));
        }

        // Filter by budget
        List<ProductSummary> filtered = filterByBudget(summaries, form.getBudget());

        // Generate AI recommendations
        String preferences = String.format("Skill Level: %s, Skiing Type: %s, Budget: %s",
            form.getSkillLevel(), form.getSkiingType(), form.getBudget());

        String recommendations = openAIService.generateRecommendations(
            weatherJson, conditionsSummary, filtered, preferences);

        SkiAdvisorResult result = new SkiAdvisorResult();
        result.setWeather(weather);
        result.setSkiConditions(ski);
        result.setRecommendations(recommendations);
        result.setProducts(filtered);
        result.setForm(form);
        return result;
    }

    private List<ProductSummary> filterByBudget(List<ProductSummary> products, String budget) {
        List<ProductSummary> filtered = products.stream().filter(p -> {
            if (p.getPrice() == null) return false;
            double price = p.getPrice().doubleValue();
            return switch (budget.toLowerCase()) {
                case "low" -> price < 300;
                case "medium" -> price >= 300 && price < 600;
                case "high" -> price >= 600;
                default -> true;
            };
        }).toList();
        return filtered.isEmpty() ? products : filtered;
    }
}
