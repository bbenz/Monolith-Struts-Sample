package com.skishop.controller;

import com.skishop.model.entity.Product;
import com.skishop.repository.ProductRepository;
import com.skishop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductService productService;

    @MockBean
    ProductRepository productRepository;

    @TestConfiguration
    static class TestConfig {
        @Bean
        ProductService productService(ProductRepository productRepository) {
            return new ProductService(productRepository);
        }
    }

    @Test
    void list_returnsProducts() throws Exception {
        Product p = new Product();
        p.setId("P1");
        p.setName("Ski");
        p.setStatus("ACTIVE");
        Mockito.when(productService.findAll()).thenReturn(List.of(p));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("P1"));
    }

    @Test
    void detail_returnsProduct() throws Exception {
        Product p = new Product();
        p.setId("P1");
        p.setName("Ski");
        p.setStatus("ACTIVE");
        Mockito.when(productService.findById(eq("P1"))).thenReturn(Optional.of(p));

        mockMvc.perform(get("/products/P1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("P1"));
    }

    @Test
    void create_succeeds() throws Exception {
        Product p = new Product();
        p.setId("P1");
        p.setName("Ski");
        p.setStatus("ACTIVE");
        Mockito.when(productService.save(any(Product.class))).thenReturn(p);

        String body = "{\"name\":\"Ski\",\"status\":\"ACTIVE\"}";
        mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("P1"));
    }
}
