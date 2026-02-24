package com.skishop.service;

import com.skishop.model.entity.OrderItem;
import com.skishop.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService extends BaseService<OrderItem, String> {
    private final OrderItemRepository repository;

    public OrderItemService(OrderItemRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public java.util.List<OrderItem> findByOrderId(String orderId) { return repository.findByOrderId(orderId); }
}
