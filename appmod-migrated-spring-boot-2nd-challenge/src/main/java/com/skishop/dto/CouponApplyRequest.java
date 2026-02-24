package com.skishop.dto;

import jakarta.validation.constraints.NotBlank;

public class CouponApplyRequest {
    @NotBlank
    private String couponCode;
    private String userId;
    private String orderId;

    public CouponApplyRequest() {}
    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
}
