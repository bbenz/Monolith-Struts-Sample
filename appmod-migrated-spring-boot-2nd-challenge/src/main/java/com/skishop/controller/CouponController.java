package com.skishop.controller;

import com.skishop.dto.CouponApplyRequest;
import com.skishop.dto.CouponApplyResponse;
import com.skishop.model.entity.Coupon;
import com.skishop.model.entity.CouponUsage;
import com.skishop.service.CouponService;
import com.skishop.service.CouponUsageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/coupons")
public class CouponController {
    private final CouponService couponService;
    private final CouponUsageService couponUsageService;

    public CouponController(CouponService couponService, CouponUsageService couponUsageService) {
        this.couponService = couponService;
        this.couponUsageService = couponUsageService;
    }

    @PostMapping("/apply")
    public ResponseEntity<CouponApplyResponse> apply(@Valid @RequestBody CouponApplyRequest request) {
        CouponApplyResponse resp = new CouponApplyResponse();
        resp.setCouponCode(request.getCouponCode());
        Coupon coupon = couponService.findAll().stream()
                .filter(c -> request.getCouponCode().equals(c.getCode()))
                .findFirst().orElse(null);
        if (coupon == null || Boolean.FALSE.equals(coupon.getIsActive())) {
            resp.setValid(false);
            resp.setDiscountApplied(BigDecimal.ZERO);
            resp.setMessage("Invalid coupon");
            return ResponseEntity.ok(resp);
        }
        // Simplified discount: use discountValue as amount
        resp.setValid(true);
        resp.setDiscountApplied(coupon.getDiscountValue());
        resp.setMessage("Coupon applied");
        // Record usage (simplified)
        CouponUsage usage = new CouponUsage();
        usage.setId(UUID.randomUUID().toString());
        usage.setCouponId(coupon.getId());
        usage.setUserId(request.getUserId() == null ? "anonymous" : request.getUserId());
        usage.setOrderId(request.getOrderId() == null ? "" : request.getOrderId());
        usage.setDiscountApplied(coupon.getDiscountValue());
        usage.setUsedAt(LocalDateTime.now());
        couponUsageService.save(usage);
        return ResponseEntity.ok(resp);
    }
}
