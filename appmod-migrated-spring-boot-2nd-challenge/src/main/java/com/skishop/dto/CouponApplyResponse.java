package com.skishop.dto;

import java.math.BigDecimal;

public class CouponApplyResponse {
    private String couponCode;
    private BigDecimal discountApplied;
    private boolean valid;
    private String message;

    public CouponApplyResponse() {}
    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public BigDecimal getDiscountApplied() { return discountApplied; }
    public void setDiscountApplied(BigDecimal discountApplied) { this.discountApplied = discountApplied; }
    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
