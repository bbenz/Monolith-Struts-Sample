package com.skishop.service;

import com.skishop.model.entity.Product;
import com.skishop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
    }

    @Test
    void search_returns_products() {
        Product p = new Product();
        p.setId("P002");
        p.setName("Ski Board");
        p.setBrand("BrandY");
        p.setDescription("All mountain board");
        p.setCategoryId("c-2");
        p.setStatus("ACTIVE");
        productService.save(p);

        List<Product> result = productService.search("board");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("P002");
    }
}
