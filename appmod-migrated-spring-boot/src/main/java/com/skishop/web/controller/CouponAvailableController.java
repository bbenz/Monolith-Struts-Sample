package com.skishop.web.controller;

import com.skishop.domain.coupon.Coupon;
import com.skishop.service.coupon.CouponService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/coupons/available")
public class CouponAvailableController {
    private final CouponService couponService;

    public CouponAvailableController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    public String showAvailableCoupons(Model model) {
        List<Coupon> coupons = couponService.listActiveCoupons();
        model.addAttribute("coupons", coupons);
        return "coupons/available";
    }
}
