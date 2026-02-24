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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AdminViewController.class)
@Import(AdminViewControllerTest.Config.class)
class AdminViewControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ProductRepository productRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired UserRepository userRepository;
    @Autowired CouponRepository couponRepository;
    @Autowired ReturnRequestRepository returnRequestRepository;
    @Autowired ShippingMethodRepository shippingMethodRepository;

    Product product;
    Order order;
    User user;
    Coupon coupon;
    ReturnRequest ret;
    ShippingMethod sm;

    @BeforeEach
    void setup() {
        Mockito.reset(productRepository, orderRepository, userRepository, couponRepository, returnRequestRepository, shippingMethodRepository);
        var product = this.product = new Product();
        product.setId("p1"); product.setName("Ski"); product.setStatus("ACTIVE"); product.setCreatedAt(java.time.LocalDateTime.now()); product.setUpdatedAt(java.time.LocalDateTime.now());
        var order = this.order = new Order();
        order.setId("o1"); order.setOrderNumber("ORDER-1"); order.setStatus("NEW"); order.setPaymentStatus("PENDING"); order.setSubtotal(BigDecimal.TEN); order.setTax(BigDecimal.ONE); order.setShippingFee(BigDecimal.ONE); order.setDiscountAmount(BigDecimal.ZERO); order.setTotalAmount(BigDecimal.valueOf(12)); order.setUsedPoints(0); order.setCreatedAt(java.time.LocalDateTime.now()); order.setUpdatedAt(java.time.LocalDateTime.now());
        var user = this.user = new User();
        user.setId("u1"); user.setUsername("user1"); user.setEmail("u@example.com"); user.setPasswordHash("x"); user.setSalt("s"); user.setStatus("ACTIVE"); user.setRole("USER"); user.setCreatedAt(java.time.LocalDateTime.now()); user.setUpdatedAt(java.time.LocalDateTime.now());
        var coupon = this.coupon = new Coupon();
        coupon.setId("c1"); coupon.setCode("CPN"); coupon.setCouponType("TYPE"); coupon.setDiscountType("FLAT"); coupon.setDiscountValue(BigDecimal.TEN); coupon.setUsageLimit(1); coupon.setUsedCount(0); coupon.setIsActive(true);
        var ret = this.ret = new ReturnRequest();
        ret.setId("r1"); ret.setOrderId(order.getId()); ret.setOrderItemId("oi1"); ret.setQuantity(1); ret.setRefundAmount(BigDecimal.ONE); ret.setStatus("REQUESTED");
        var sm = this.sm = new ShippingMethod();
        sm.setId("s1"); sm.setCode("STD"); sm.setName("Standard"); sm.setFee(BigDecimal.ONE); sm.setIsActive(true); sm.setSortOrder(1);

        when(productRepository.findAll()).thenReturn(List.of(product));
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(couponRepository.findAll()).thenReturn(List.of(coupon));
        when(returnRequestRepository.findAll()).thenReturn(List.of(ret));
        when(shippingMethodRepository.findAll()).thenReturn(List.of(sm));
    }

    @Test
    void adminProducts() throws Exception { mockMvc.perform(get("/ui/admin/products")).andExpect(status().isOk()); }
    @Test
    void adminOrders() throws Exception { mockMvc.perform(get("/ui/admin/orders")).andExpect(status().isOk()); }
    @Test
    void adminUsers() throws Exception { mockMvc.perform(get("/ui/admin/users")).andExpect(status().isOk()); }
    @Test
    void adminCoupons() throws Exception { mockMvc.perform(get("/ui/admin/coupons")).andExpect(status().isOk()); }
    @Test
    void adminReturns() throws Exception { mockMvc.perform(get("/ui/admin/returns")).andExpect(status().isOk()); }
    @Test
    void adminShippingMethods() throws Exception { mockMvc.perform(get("/ui/admin/shipping-methods")).andExpect(status().isOk()); }
    @Test
    void adminReports() throws Exception { mockMvc.perform(get("/ui/admin/reports")).andExpect(status().isOk()); }

    @Test
    void adminProducts_runtimeException_returns500View() throws Exception {
        when(productRepository.findAll()).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/ui/admin/products"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error/500"));
    }

    @TestConfiguration
    static class Config {
        @Bean ProductRepository productRepository() { return Mockito.mock(ProductRepository.class); }
        @Bean OrderRepository orderRepository() { return Mockito.mock(OrderRepository.class); }
        @Bean UserRepository userRepository() { return Mockito.mock(UserRepository.class); }
        @Bean CouponRepository couponRepository() { return Mockito.mock(CouponRepository.class); }
        @Bean ReturnRequestRepository returnRequestRepository() { return Mockito.mock(ReturnRequestRepository.class); }
        @Bean ShippingMethodRepository shippingMethodRepository() { return Mockito.mock(ShippingMethodRepository.class); }

        @Bean ProductService productService(ProductRepository r) { return new ProductService(r); }
        @Bean OrderService orderService(OrderRepository r) { return new OrderService(r); }
        @Bean UserService userService(UserRepository r) { return new UserService(r); }
        @Bean CouponService couponService(CouponRepository r) { return new CouponService(r); }
        @Bean ReturnRequestService returnRequestService(ReturnRequestRepository r) { return new ReturnRequestService(r); }
        @Bean ShippingMethodService shippingMethodService(ShippingMethodRepository r) { return new ShippingMethodService(r); }
    }
}
