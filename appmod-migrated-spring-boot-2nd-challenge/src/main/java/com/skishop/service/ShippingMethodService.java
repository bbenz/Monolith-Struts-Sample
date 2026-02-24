package com.skishop.service;

import com.skishop.model.entity.ShippingMethod;
import com.skishop.repository.ShippingMethodRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShippingMethodService extends BaseService<ShippingMethod, String> {
    private final ShippingMethodRepository repository;
    public ShippingMethodService(ShippingMethodRepository repository) {
        super(repository);
        this.repository = repository;
    }
    public Optional<ShippingMethod> findByCode(String code) { return repository.findByCode(code); }
}
