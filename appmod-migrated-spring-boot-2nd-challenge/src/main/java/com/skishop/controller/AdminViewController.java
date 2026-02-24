package com.skishop.controller;

import com.skishop.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminViewController {
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;
    private final CouponService couponService;
    private final ReturnRequestService returnRequestService;
    private final ShippingMethodService shippingMethodService;

    public AdminViewController(ProductService productService,
                              OrderService orderService,
                              UserService userService,
                              CouponService couponService,
                              ReturnRequestService returnRequestService,
                              ShippingMethodService shippingMethodService) {
        this.productService = productService;
        this.orderService = orderService;
        this.userService = userService;
        this.couponService = couponService;
        this.returnRequestService = returnRequestService;
        this.shippingMethodService = shippingMethodService;
    }

    @GetMapping("/ui/admin/products")
    public String adminProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/products/list";
    }

    @GetMapping("/ui/admin/orders")
    public String adminOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "admin/orders/list";
    }

    @GetMapping("/ui/admin/users")
    public String adminUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users/list";
    }

    @GetMapping("/ui/admin/coupons")
    public String adminCoupons(Model model) {
        model.addAttribute("coupons", couponService.findAll());
        return "admin/coupons/list";
    }

    @GetMapping("/ui/admin/returns")
    public String adminReturns(Model model) {
        model.addAttribute("returns", returnRequestService.findAll());
        return "admin/returns/list";
    }

    @GetMapping("/ui/admin/shipping-methods")
    public String adminShippingMethods(Model model) {
        model.addAttribute("shippingMethods", shippingMethodService.findAll());
        return "admin/shipping_methods/list";
    }

    @GetMapping("/ui/admin/reports")
    public String adminReports(Model model) {
        model.addAttribute("sales", 0);
        return "admin/reports/dashboard";
    }
}
