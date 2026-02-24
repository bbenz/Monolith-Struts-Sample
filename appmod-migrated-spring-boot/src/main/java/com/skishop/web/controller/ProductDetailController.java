package com.skishop.web.controller;

import com.skishop.domain.product.Product;
import com.skishop.service.catalog.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/product")
public class ProductDetailController {
    private final ProductService productService;

    public ProductDetailController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showProductDetail(@RequestParam(required = false) String id, Model model) {
        if (id == null || id.isEmpty()) {
            return "products/notfound";
        }

        Product product = productService.findById(id);
        if (product == null) {
            return "products/notfound";
        }

        model.addAttribute("product", product);
        return "products/detail";
    }
}
