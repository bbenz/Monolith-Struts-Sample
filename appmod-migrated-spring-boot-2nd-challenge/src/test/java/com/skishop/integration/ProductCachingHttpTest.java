package com.skishop.integration;

import com.skishop.model.entity.Product;
import com.skishop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProductCachingHttpTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @MockBean
    ProductRepository productRepository;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void detail_isCachedAcrossHttpRequests() {
        var id = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        var p = new Product();
        p.setId(id);
        p.setName("Cached via HTTP");
        p.setStatus("ACTIVE");
        when(productRepository.findById(id)).thenReturn(Optional.of(p));

        var t1 = System.nanoTime();
        var r1 = restTemplate.getForEntity(url("/products/" + id), String.class);
        var t2 = System.nanoTime();
        var r2 = restTemplate.getForEntity(url("/products/" + id), String.class);
        var t3 = System.nanoTime();

        assertThat(r1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r2.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(productRepository, times(1)).findById(id);

        var firstMs = (t2 - t1) / 1_000_000.0;
        var secondMs = (t3 - t2) / 1_000_000.0;
        assertThat(secondMs).isLessThanOrEqualTo(firstMs * 1.2);
        System.out.printf("first=%.2f ms, second=%.2f ms%n", firstMs, secondMs);
    }
}
