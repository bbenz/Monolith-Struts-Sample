package com.skishop.service;

import com.skishop.model.entity.CouponUsage;
import com.skishop.repository.CouponUsageRepository;
import org.springframework.stereotype.Service;

@Service
public class CouponUsageService extends BaseService<CouponUsage, String> {
    public CouponUsageService(CouponUsageRepository repository) { super(repository); }
}
