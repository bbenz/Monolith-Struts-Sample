package com.skishop.repository;

import com.skishop.model.entity.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponUsageRepository extends JpaRepository<CouponUsage, String> {
    List<CouponUsage> findByCouponId(String couponId);
}
