package com.skishop.service;

import com.skishop.model.entity.Price;
import com.skishop.repository.PriceRepository;
import org.springframework.stereotype.Service;

@Service
public class PriceService extends BaseService<Price, String> {
    private final PriceRepository repository;

    public PriceService(PriceRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public java.util.List<Price> findByProductId(String productId) {
        return repository.findByProductId(productId);
    }
}
