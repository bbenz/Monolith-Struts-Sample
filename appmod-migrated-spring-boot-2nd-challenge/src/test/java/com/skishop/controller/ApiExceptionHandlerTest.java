package com.skishop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ApiExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    com.skishop.repository.ProductRepository productRepository;

    @TestConfiguration
    static class Config {
        @Bean
        com.skishop.repository.ProductRepository productRepository() {
            return org.mockito.Mockito.mock(com.skishop.repository.ProductRepository.class);
        }

        @Bean
        com.skishop.service.ProductService productService(com.skishop.repository.ProductRepository r) {
            return new com.skishop.service.ProductService(r);
        }
    }

    @Test
    void product_notFound_returnsJson404() throws Exception {
        when(productRepository.findById("P404")).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/P404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", startsWith("Product not found")));
    }

    @Test
    void product_list_runtimeException_returnsJson500() throws Exception {
        when(productRepository.findAll()).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/products"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Unexpected error"));
    }
}
