package com.skishop.repository;

import com.skishop.model.entity.OrderShipping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderShippingRepository extends JpaRepository<OrderShipping, String> {
    Optional<OrderShipping> findByOrderId(String orderId);
}
