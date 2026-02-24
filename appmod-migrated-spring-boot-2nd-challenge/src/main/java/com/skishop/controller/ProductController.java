package com.skishop.controller;

import com.skishop.dto.ProductRequest;
import com.skishop.dto.ProductResponse;
import com.skishop.exception.ResourceNotFoundException;
import com.skishop.model.entity.Product;
import com.skishop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> list(@RequestParam(value = "q", required = false) String q) {
        List<Product> products = (q == null || q.isBlank()) ? productService.findAll() : productService.search(q);
        return products.stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ProductResponse detail(@PathVariable String id) {
        Product product = productService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        return toResponse(product);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        Product p = fromRequest(request, new Product());
        Product saved = productService.save(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable String id, @Valid @RequestBody ProductRequest request) {
        Product product = productService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        Product updated = productService.save(fromRequest(request, product));
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Product fromRequest(ProductRequest req, Product p) {
        p.setName(req.getName());
        p.setBrand(req.getBrand());
        p.setDescription(req.getDescription());
        p.setCategoryId(req.getCategoryId());
        p.setSku(req.getSku());
        p.setStatus(req.getStatus());
        return p;
    }

    private ProductResponse toResponse(Product p) {
        ProductResponse res = new ProductResponse();
        res.setId(p.getId());
        res.setName(p.getName());
        res.setBrand(p.getBrand());
        res.setDescription(p.getDescription());
        res.setCategoryId(p.getCategoryId());
        res.setSku(p.getSku());
        res.setStatus(p.getStatus());
        res.setCreatedAt(p.getCreatedAt());
        res.setUpdatedAt(p.getUpdatedAt());
        return res;
    }
}
