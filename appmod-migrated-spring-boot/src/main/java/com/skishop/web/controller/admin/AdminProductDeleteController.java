package com.skishop.web.controller.admin;

import com.skishop.domain.product.Product;
import com.skishop.service.catalog.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/product/delete")
public class AdminProductDeleteController {
    private final ProductService productService;

    public AdminProductDeleteController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public String deleteProduct(@RequestParam(required = false) String id, Model model) {
        if (id == null || id.isEmpty()) {
            model.addAttribute("error", "Product ID is required");
            return "admin/product-delete-failure";
        }

        Product product = productService.findById(id);
        if (product == null) {
            model.addAttribute("error", "Product not found");
            return "admin/product-delete-failure";
        }

        productService.deactivateProduct(product.getId());
        return "redirect:/admin/products";
    }
}
