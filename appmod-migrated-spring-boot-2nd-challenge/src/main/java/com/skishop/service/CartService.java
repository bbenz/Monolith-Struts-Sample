package com.skishop.service;

import com.skishop.model.entity.Cart;
import com.skishop.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService extends BaseService<Cart, String> {
    public CartService(CartRepository repository) { super(repository); }
}
