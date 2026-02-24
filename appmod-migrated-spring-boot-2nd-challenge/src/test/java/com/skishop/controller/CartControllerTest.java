package com.skishop.controller;

import com.skishop.model.entity.Cart;
import com.skishop.model.entity.CartItem;
import com.skishop.repository.CartItemRepository;
import com.skishop.repository.CartRepository;
import com.skishop.service.CartItemService;
import com.skishop.service.CartService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartRepository cartRepository;
    @MockBean
    CartItemRepository cartItemRepository;

    @Autowired
    CartService cartService;
    @Autowired
    CartItemService cartItemService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        CartService cartService(CartRepository cartRepository) {
            return new CartService(cartRepository);
        }
        @Bean
        CartItemService cartItemService(CartItemRepository cartItemRepository) {
            return new CartItemService(cartItemRepository);
        }
    }

    @Test
    void create_returnsCart() throws Exception {
        Cart c = new Cart();
        c.setId("C1");
        c.setStatus("OPEN");
        Mockito.when(cartRepository.save(any(Cart.class))).thenReturn(c);

        mockMvc.perform(post("/carts"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("C1"));
    }

    @Test
    void get_returnsCartWithItems() throws Exception {
        Cart c = new Cart();
        c.setId("C1");
        c.setStatus("OPEN");
        Mockito.when(cartRepository.findById("C1")).thenReturn(Optional.of(c));
        CartItem item = new CartItem();
        item.setId("I1");
        item.setCartId("C1");
        item.setProductId("P1");
        item.setQuantity(1);
        item.setUnitPrice(new BigDecimal("10.00"));
        Mockito.when(cartItemRepository.findByCartId("C1")).thenReturn(List.of(item));

        mockMvc.perform(get("/carts/C1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value("I1"));
    }
}
