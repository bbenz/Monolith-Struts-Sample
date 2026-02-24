package com.skishop.web.dto.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class AdminShippingMethodRequest {
    private String id;

    @NotBlank(message = "Shipping method code is required")
    private String code;

    @NotBlank(message = "Shipping method name is required")
    private String name;

    @NotBlank(message = "Fee is required")
    private String fee;

    private boolean active;

    @Min(value = 0, message = "Sort order must be non-negative")
    private int sortOrder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
