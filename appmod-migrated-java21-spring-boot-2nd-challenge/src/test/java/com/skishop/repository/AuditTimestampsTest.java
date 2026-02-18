package com.skishop.repository;

import com.skishop.model.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AuditTimestampsTest {

    @Autowired OrderRepository orderRepository;
    @Autowired PaymentRepository paymentRepository;
    @Autowired PointTransactionRepository pointTransactionRepository;
    @Autowired UserRepository userRepository;
    @Autowired UserAddressRepository userAddressRepository;

    @Test
    void orderPrePersist_setsTimestamps_andDefaults() {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setOrderNumber("ORD-1");
        order.setStatus("NEW");
        order.setPaymentStatus("PENDING");
        order.setSubtotal(BigDecimal.ZERO);
        order.setTax(BigDecimal.ZERO);
        order.setShippingFee(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setTotalAmount(BigDecimal.ZERO);
        // usedPoints should default to 0

        order = orderRepository.saveAndFlush(order);

        assertThat(order.getCreatedAt()).isNotNull();
        assertThat(order.getUpdatedAt()).isNotNull();
        assertThat(order.getUsedPoints()).isEqualTo(0);
    }

    @Test
    void paymentPrePersist_setsCreatedAt() {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString());
        payment.setAmount(BigDecimal.ONE);
        payment.setCurrency("USD");
        payment.setStatus("PENDING");

        payment = paymentRepository.saveAndFlush(payment);

        assertThat(payment.getCreatedAt()).isNotNull();
    }

    @Test
    void pointTransactionPrePersist_setsDefaults() {
        PointTransaction txn = new PointTransaction();
        txn.setId(UUID.randomUUID().toString());
        txn.setUserId(UUID.randomUUID().toString());
        txn.setType("EARN");
        txn.setAmount(10);
        // isExpired null to test default

        txn = pointTransactionRepository.saveAndFlush(txn);

        assertThat(txn.getCreatedAt()).isNotNull();
        assertThat(txn.getIsExpired()).isFalse();
    }

    @Test
    void userPrePersist_setsTimestamps() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail("user@example.com");
        user.setUsername("user1");
        user.setPasswordHash("hash");
        user.setSalt("salt");
        user.setStatus("ACTIVE");
        user.setRole("USER");

        user = userRepository.saveAndFlush(user);

        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    void userAddressPrePersist_setsTimestamps() {
        UserAddress ua = new UserAddress();
        ua.setId(UUID.randomUUID().toString());
        ua.setUserId(UUID.randomUUID().toString());
        ua.setLabel("home");
        ua.setRecipientName("John");
        ua.setPostalCode("80435");
        ua.setPrefecture("Colorado");
        ua.setAddress1("1-2-3");
        ua.setIsDefault(Boolean.TRUE);

        ua = userAddressRepository.saveAndFlush(ua);

        assertThat(ua.getCreatedAt()).isNotNull();
        assertThat(ua.getUpdatedAt()).isNotNull();
    }
}
