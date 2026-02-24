package com.skishop.web.dto;

import jakarta.validation.constraints.NotBlank;

public class CouponRequest {
    @NotBlank(message = "Coupon code is required")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
