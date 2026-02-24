package com.skishop.controller;

import com.skishop.model.entity.ShippingMethod;
import com.skishop.service.ShippingMethodService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/shipping-methods")
public class AdminShippingMethodController {
    private final ShippingMethodService shippingMethodService;

    public AdminShippingMethodController(ShippingMethodService shippingMethodService) {
        this.shippingMethodService = shippingMethodService;
    }

    @GetMapping
    public List<ShippingMethod> list() {
        return shippingMethodService.findAll();
    }

    @PostMapping
    public ShippingMethod create(@Valid @RequestBody ShippingMethod sm) {
        sm.setId(UUID.randomUUID().toString());
        return shippingMethodService.save(sm);
    }

    @PutMapping("/{id}")
    public ShippingMethod update(@PathVariable String id, @Valid @RequestBody ShippingMethod sm) {
        sm.setId(id);
        return shippingMethodService.save(sm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        shippingMethodService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
