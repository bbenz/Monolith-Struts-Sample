package com.skiadvisor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents AI-generated equipment recommendations
 */
public class EquipmentRecommendation {
    private String summary;
    private List<Product> recommendedProducts;
    private String reasoning;
    private List<String> tips;

    public EquipmentRecommendation() {
        this.recommendedProducts = new ArrayList<>();
        this.tips = new ArrayList<>();
    }

    public EquipmentRecommendation(String summary, List<Product> recommendedProducts, 
                                  String reasoning, List<String> tips) {
        this.summary = summary;
        this.recommendedProducts = recommendedProducts != null ? recommendedProducts : new ArrayList<>();
        this.reasoning = reasoning;
        this.tips = tips != null ? tips : new ArrayList<>();
    }

    // Getters and Setters
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public List<Product> getRecommendedProducts() { return recommendedProducts; }
    public void setRecommendedProducts(List<Product> recommendedProducts) { 
        this.recommendedProducts = recommendedProducts; 
    }

    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }

    public List<String> getTips() { return tips; }
    public void setTips(List<String> tips) { this.tips = tips; }

    public void addProduct(Product product) {
        this.recommendedProducts.add(product);
    }

    public void addTip(String tip) {
        this.tips.add(tip);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== EQUIPMENT RECOMMENDATIONS ===\n\n");
        sb.append(summary).append("\n\n");
        sb.append("REASONING:\n").append(reasoning).append("\n\n");
        
        if (!recommendedProducts.isEmpty()) {
            sb.append("RECOMMENDED PRODUCTS:\n");
            for (int i = 0; i < recommendedProducts.size(); i++) {
                sb.append(String.format("%d. %s\n", i + 1, recommendedProducts.get(i)));
            }
            sb.append("\n");
        }
        
        if (!tips.isEmpty()) {
            sb.append("ADDITIONAL TIPS:\n");
            for (String tip : tips) {
                sb.append("• ").append(tip).append("\n");
            }
        }
        
        return sb.toString();
    }
}
