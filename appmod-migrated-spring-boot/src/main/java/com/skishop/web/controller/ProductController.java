package com.skishop.web.controller;

import com.skishop.domain.product.Category;
import com.skishop.domain.product.Product;
import com.skishop.service.catalog.CategoryService;
import com.skishop.service.catalog.ProductService;
import com.skishop.web.dto.ProductSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Spring MVC Controller for product listing operations.
 * Migrated from Struts ProductListAction to Spring Boot.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private static final int DEFAULT_SIZE = 10;
    
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listProducts(@ModelAttribute ProductSearchRequest searchRequest, Model model) {
        
        int page = searchRequest.getPage();
        int size = searchRequest.getSize();
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = DEFAULT_SIZE;
        }

        String keyword = searchRequest.getKeyword();
        String categoryId = searchRequest.getCategoryId();
        String sort = searchRequest.getSort();
        
        int offset = (page - 1) * size;
        
        List<Product> products = productService.search(keyword, categoryId, sort, offset, size);
        List<Category> categories = categoryService.listAll();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("searchForm", searchRequest);
        model.addAttribute("currentPage", page);
        
        return "products/list";
    }
}
