package com.skishop.service;

import com.skishop.model.entity.OrderShipping;
import com.skishop.repository.OrderShippingRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderShippingService extends BaseService<OrderShipping, String> {
    public OrderShippingService(OrderShippingRepository repository) { super(repository); }
}
