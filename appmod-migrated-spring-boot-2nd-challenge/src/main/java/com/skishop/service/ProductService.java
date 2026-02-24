package com.skishop.service;

import com.skishop.model.entity.Product;
import com.skishop.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService extends BaseService<Product, String> {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        super(productRepository);
        this.productRepository = productRepository;
    }

    public List<Product> search(String keyword) {
        return productRepository.search(keyword);
    }

    @Cacheable(value = "products", key = "#id")
    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findById(String id) {
        return super.findById(id);
    }

    @CacheEvict(value = "products", key = "#entity.id", condition = "#entity != null && #entity.id != null")
    @Override
    public Product save(Product entity) {
        return super.save(entity);
    }

    @CacheEvict(value = "products", key = "#id")
    @Override
    public void deleteById(String id) {
        super.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() { return super.findAll(); }
}
