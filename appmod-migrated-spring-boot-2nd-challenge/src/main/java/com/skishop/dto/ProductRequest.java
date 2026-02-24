package com.skishop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductRequest {
    @NotBlank
    @Size(max = 255)
    private String name;
    @Size(max = 100)
    private String brand;
    @Size(max = 2000)
    private String description;
    @Size(max = 36)
    private String categoryId;
    @Size(max = 100)
    private String sku;
    @NotBlank
    @Size(max = 20)
    private String status;

    public ProductRequest() {}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
