package com.skishop.repository;

import com.skishop.model.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, String> {
    List<Price> findByProductId(String productId);
}
