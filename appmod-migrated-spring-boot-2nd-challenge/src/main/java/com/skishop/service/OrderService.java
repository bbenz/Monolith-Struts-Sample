package com.skishop.service;

import com.skishop.model.entity.Order;
import com.skishop.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends BaseService<Order, String> {
    public OrderService(OrderRepository repository) { super(repository); }
}
