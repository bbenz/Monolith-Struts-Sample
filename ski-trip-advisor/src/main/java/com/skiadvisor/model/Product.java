package com.skiadvisor.model;

import java.math.BigDecimal;

/**
 * Represents a ski product from the database
 */
public class Product {
    private String id;
    private String name;
    private String description;
    private String categoryId;
    private String categoryName;
    private String status;
    private BigDecimal price;
    private int quantity;

    public Product() {
    }

    public Product(String id, String name, String description, 
                   String categoryId, String categoryName,
                   String status, BigDecimal price, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.status = status;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return String.format("%s ($%.2f) - %s [Stock: %d]", 
            name, price != null ? price.doubleValue() : 0.0, description, quantity);
    }
}
