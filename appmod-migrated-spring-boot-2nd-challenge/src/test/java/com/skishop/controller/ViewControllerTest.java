package com.skishop.controller;

import com.skishop.model.entity.*;
import com.skishop.repository.*;
import com.skishop.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ViewController.class)
@Import(ViewControllerTest.Config.class)
class ViewControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ProductRepository productRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired OrderItemRepository orderItemRepository;
    @Autowired CartRepository cartRepository;
    @Autowired CartItemRepository cartItemRepository;
    @Autowired PriceRepository priceRepository;

    Product product;
    Order order;
    OrderItem orderItem;
    Cart cart;

    @BeforeEach
    void setup() {
        Mockito.reset(productRepository, orderRepository, orderItemRepository, cartRepository, cartItemRepository, priceRepository);
        var product = this.product = new Product();
        product.setId("p1");
        product.setName("Ski");
        product.setStatus("ACTIVE");
        product.setBrand("Brand");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        var order = this.order = new Order();
        order.setId("o1");
        order.setOrderNumber("ORDER-1");
        order.setStatus("NEW");
        order.setPaymentStatus("PENDING");
        order.setSubtotal(BigDecimal.TEN);
        order.setTax(BigDecimal.ONE);
        order.setShippingFee(BigDecimal.ONE);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setTotalAmount(BigDecimal.valueOf(12));
        order.setUsedPoints(0);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        var orderItem = this.orderItem = new OrderItem();
        orderItem.setId("oi1");
        orderItem.setOrderId(order.getId());
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setQuantity(1);
        orderItem.setUnitPrice(BigDecimal.TEN);
        orderItem.setSubtotal(BigDecimal.TEN);

        var cart = this.cart = new Cart();
        cart.setId("c1");
        cart.setStatus("OPEN");

        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));
        when(priceRepository.findByProductId("p1")).thenReturn(List.of());
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderRepository.findById("o1")).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId("o1")).thenReturn(List.of(orderItem));
        when(cartRepository.findById("c1")).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId("c1")).thenReturn(List.of());
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void index() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void productsList() throws Exception {
        mockMvc.perform(get("/ui/products"))
                .andExpect(status().isOk());
    }

    @Test
    void productDetail() throws Exception {
        mockMvc.perform(get("/ui/products/p1"))
                .andExpect(status().isOk());
    }

    @Test
    void cartView() throws Exception {
        mockMvc.perform(get("/ui/cart").sessionAttr("CART_ID", "c1"))
                .andExpect(status().isOk());
    }

    @Test
    void ordersList() throws Exception {
        mockMvc.perform(get("/ui/orders"))
                .andExpect(status().isOk());
    }

    @Test
    void orderDetail() throws Exception {
        mockMvc.perform(get("/ui/orders/o1"))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class Config {
        @Bean ProductRepository productRepository() { return Mockito.mock(ProductRepository.class); }
        @Bean OrderRepository orderRepository() { return Mockito.mock(OrderRepository.class); }
        @Bean OrderItemRepository orderItemRepository() { return Mockito.mock(OrderItemRepository.class); }
        @Bean UserRepository userRepository() { return Mockito.mock(UserRepository.class); }
        @Bean CartRepository cartRepository() { return Mockito.mock(CartRepository.class); }
        @Bean CartItemRepository cartItemRepository() { return Mockito.mock(CartItemRepository.class); }
        @Bean PriceRepository priceRepository() { return Mockito.mock(PriceRepository.class); }
        @Bean PointTransactionRepository pointTransactionRepository() { return Mockito.mock(PointTransactionRepository.class); }
        @Bean UserAddressRepository userAddressRepository() { return Mockito.mock(UserAddressRepository.class); }

        @Bean ProductService productService(ProductRepository r) { return new ProductService(r); }
        @Bean OrderService orderService(OrderRepository r) { return new OrderService(r); }
        @Bean OrderItemService orderItemService(OrderItemRepository r) { return new OrderItemService(r); }
        @Bean UserService userService(UserRepository r) { return new UserService(r); }
        @Bean CartService cartService(CartRepository r) { return new CartService(r); }
        @Bean CartItemService cartItemService(CartItemRepository r) { return new CartItemService(r); }
        @Bean PriceService priceService(PriceRepository r) { return new PriceService(r); }
        @Bean PointTransactionService pointTransactionService(PointTransactionRepository r) { return new PointTransactionService(r); }
        @Bean UserAddressService userAddressService(UserAddressRepository r) { return new UserAddressService(r); }
    }

    @org.junit.jupiter.api.Test
    void productDetail_notFound_returns404View() throws Exception {
        when(productRepository.findById("p404")).thenReturn(Optional.empty());

        mockMvc.perform(get("/ui/products/p404"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"));
    }

    @org.junit.jupiter.api.Test
    void index_runtimeException_returns500View() throws Exception {
        when(productRepository.findAll()).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error/500"));
    }
}
