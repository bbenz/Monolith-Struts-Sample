package com.skishop.service;

import com.skishop.model.entity.Product;
import com.skishop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceCachingTest {
    @Autowired ProductService productService;
    @Autowired CacheManager cacheManager;

    @MockBean ProductRepository productRepository;

    @Test
    void findById_isCached() {
        var id = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        var product = new Product();
        product.setId(id);
        product.setName("Cached Product");
        product.setStatus("ACTIVE");

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        var first = productService.findById(id);
        var second = productService.findById(id);

        assertThat(first).isPresent();
        assertThat(second).isPresent();
        verify(productRepository, times(1)).findById(id);
        assertThat(cacheManager.getCache("products").get(id).get()).isEqualTo(product);
    }
}
