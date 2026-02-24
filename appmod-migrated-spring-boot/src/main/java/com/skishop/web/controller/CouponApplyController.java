package com.skishop.web.controller;

import com.skishop.domain.cart.CartItem;
import com.skishop.domain.coupon.Coupon;
import com.skishop.service.cart.CartService;
import com.skishop.service.coupon.CouponService;
import com.skishop.web.dto.CouponRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/coupon/apply")
public class CouponApplyController {
    private final CouponService couponService;
    private final CartService cartService;

    public CouponApplyController(CouponService couponService, CartService cartService) {
        this.couponService = couponService;
        this.cartService = cartService;
    }

    @PostMapping
    public String applyCoupon(@Valid @ModelAttribute CouponRequest couponRequest,
                             BindingResult result,
                             HttpSession session,
                             Model model) {
        String cartId = (String) session.getAttribute("cartId");
        if (cartId == null) {
            model.addAttribute("error", "Cart not found");
            return "coupon-apply-failure";
        }

        List<CartItem> items = cartService.getItems(cartId);
        BigDecimal subtotal = cartService.calculateSubtotal(items);
        model.addAttribute("cartItems", items);
        model.addAttribute("cartSubtotal", subtotal);

        if (result.hasErrors()) {
            return "coupon-apply-failure";
        }

        try {
            Coupon coupon = couponService.validateCoupon(couponRequest.getCode(), subtotal);
            BigDecimal discount = couponService.calculateDiscount(coupon, subtotal);
            model.addAttribute("coupon", coupon);
            model.addAttribute("discountAmount", discount);
            return "coupon-apply-success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid coupon code");
            return "coupon-apply-failure";
        }
    }
}
