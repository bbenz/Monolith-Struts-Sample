package com.skishop.repository;

import com.skishop.model.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void save_and_search_by_keyword() {
        Product p = new Product();
        p.setId("P001");
        p.setName("Ski Boots");
        p.setBrand("BrandX");
        p.setDescription("High performance ski boots");
        p.setCategoryId("c-1");
        p.setSku("SKU-1");
        p.setStatus("ACTIVE");
        productRepository.save(p);

        List<Product> found = productRepository.search("ski");
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getId()).isEqualTo("P001");
    }
}
