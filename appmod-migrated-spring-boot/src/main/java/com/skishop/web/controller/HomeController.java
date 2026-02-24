package com.skishop.web.controller;

import com.skishop.domain.product.Product;
import com.skishop.service.catalog.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Home page controller for root path.
 * Handles "/" and "/home" endpoints.
 */
@Controller
public class HomeController {

    private final ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = {"/", "/home"})
    public String home(Model model) {
        // Get featured products (first 8 products)
        List<Product> featuredProducts = productService.search(null, null, null, 0, 8);
        model.addAttribute("featuredProducts", featuredProducts);
        return "home";
    }
}
