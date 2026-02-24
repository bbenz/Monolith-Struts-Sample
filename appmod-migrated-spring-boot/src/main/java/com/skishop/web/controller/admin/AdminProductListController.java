package com.skishop.web.controller.admin;

import com.skishop.domain.product.Product;
import com.skishop.service.catalog.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductListController {
    private static final int MAX_ADMIN_PRODUCTS = 200;
    private final ProductService productService;

    public AdminProductListController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.search(null, null, 0, MAX_ADMIN_PRODUCTS);
        model.addAttribute("products", products);
        return "admin/product-list";
    }
}
