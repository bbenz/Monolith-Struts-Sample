package com.skishop.controller;

import com.skishop.dto.OrderRequest;
import com.skishop.dto.OrderItemRequest;
import com.skishop.exception.ResourceNotFoundException;
import com.skishop.model.entity.Order;
import com.skishop.model.entity.OrderItem;
import com.skishop.service.OrderItemService;
import com.skishop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    public OrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public ResponseEntity<Order> create(@Valid @RequestBody OrderRequest request) {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setStatus("NEW");
        order.setPaymentStatus("PENDING");
        order.setSubtotal(request.getSubtotal());
        order.setTax(request.getTax());
        order.setShippingFee(request.getShippingFee());
        order.setDiscountAmount(request.getDiscountAmount());
        order.setTotalAmount(request.getTotalAmount());
        order.setCouponCode(request.getCouponCode());
        order.setUsedPoints(request.getUsedPoints());
        Order saved = orderService.save(order);
        if (request.getItems() != null) {
            for (OrderItemRequest oi : request.getItems()) {
                OrderItem item = new OrderItem();
                item.setId(UUID.randomUUID().toString());
                item.setOrderId(saved.getId());
                item.setProductId(oi.getProductId());
                item.setQuantity(oi.getQuantity());
                item.setUnitPrice(oi.getUnitPrice());
                item.setSubtotal(oi.getUnitPrice().multiply(java.math.BigDecimal.valueOf(oi.getQuantity())));
                item.setProductName(oi.getProductName());
                orderItemService.save(item);
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable String id) {
        return orderService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
    }

    @PostMapping("/{id}/cancel")
    public Order cancel(@PathVariable String id) {
        Order order = orderService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
        order.setStatus("CANCELLED");
        return orderService.save(order);
    }
}
