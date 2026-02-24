package com.skishop.repository;

import com.skishop.model.entity.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, String> {
    Optional<ShippingMethod> findByCode(String code);
}
