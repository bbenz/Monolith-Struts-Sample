package com.skishop.service;

import com.skishop.model.entity.Coupon;
import com.skishop.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CouponService extends BaseService<Coupon, String> {
    private final CouponRepository repository;
    public CouponService(CouponRepository repository) {
        super(repository);
        this.repository = repository;
    }
    public Optional<Coupon> findByCode(String code) { return repository.findByCode(code); }
}
