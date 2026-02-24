package com.skishop.model.dto;

import java.math.BigDecimal;
import java.util.List;

public class SkiAdvisorResult {
    private WeatherInfo weather;
    private SkiConditions skiConditions;
    private String recommendations;
    private List<ProductSummary> products;
    private SkiAdvisorForm form;

    public WeatherInfo getWeather() { return weather; }
    public void setWeather(WeatherInfo weather) { this.weather = weather; }

    public SkiConditions getSkiConditions() { return skiConditions; }
    public void setSkiConditions(SkiConditions skiConditions) { this.skiConditions = skiConditions; }

    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }

    public List<ProductSummary> getProducts() { return products; }
    public void setProducts(List<ProductSummary> products) { this.products = products; }

    public SkiAdvisorForm getForm() { return form; }
    public void setForm(SkiAdvisorForm form) { this.form = form; }

    public static class ProductSummary {
        private String id;
        private String name;
        private String description;
        private String categoryName;
        private BigDecimal price;
        private int quantity;

        public ProductSummary(String id, String name, String description,
                              String categoryName, BigDecimal price, int quantity) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.categoryName = categoryName;
            this.price = price;
            this.quantity = quantity;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getCategoryName() { return categoryName; }
        public BigDecimal getPrice() { return price; }
        public int getQuantity() { return quantity; }
    }
}
