package com.skishop.service;

import com.skishop.model.entity.CartItem;
import com.skishop.repository.CartItemRepository;
import org.springframework.stereotype.Service;

@Service
public class CartItemService extends BaseService<CartItem, String> {
    private final CartItemRepository repository;

    public CartItemService(CartItemRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public java.util.List<CartItem> findByCartId(String cartId) {
        return repository.findByCartId(cartId);
    }
}
