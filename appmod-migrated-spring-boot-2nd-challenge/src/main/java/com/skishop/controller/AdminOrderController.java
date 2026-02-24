package com.skishop.controller;

import com.skishop.exception.ResourceNotFoundException;
import com.skishop.model.entity.Order;
import com.skishop.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {
    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> list() { return orderService.findAll(); }

    @GetMapping("/{id}")
    public Order detail(@PathVariable String id) {
        return orderService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
    }

    @PostMapping("/{id}/refund")
    public Order refund(@PathVariable String id) {
        Order order = orderService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
        order.setStatus("REFUNDED");
        return orderService.save(order);
    }

    @PostMapping("/{id}/update-status")
    public Order updateStatus(@PathVariable String id, @RequestParam String status) {
        Order order = orderService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
        order.setStatus(status);
        return orderService.save(order);
    }
}
