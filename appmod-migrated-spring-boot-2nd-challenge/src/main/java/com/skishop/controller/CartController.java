package com.skishop.controller;

import com.skishop.dto.CartItemRequest;
import com.skishop.dto.CartResponse;
import com.skishop.dto.CartResponse.CartItemDto;
import com.skishop.exception.ResourceNotFoundException;
import com.skishop.model.entity.Cart;
import com.skishop.model.entity.CartItem;
import com.skishop.service.CartItemService;
import com.skishop.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;

    public CartController(CartService cartService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<CartResponse> create(@RequestParam(required = false) String userId,
                                               @RequestParam(required = false) String sessionId) {
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID().toString());
        cart.setUserId(userId);
        cart.setSessionId(sessionId);
        cart.setStatus("OPEN");
        Cart saved = cartService.save(cart);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved, List.of()));
    }

    @GetMapping("/{id}")
    public CartResponse get(@PathVariable String id) {
        Cart cart = cartService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found: " + id));
        List<CartItem> items = cartItemService.findByCartId(id);
        return toResponse(cart, items);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<CartResponse> addItem(@PathVariable String id, @Valid @RequestBody CartItemRequest request) {
        Cart cart = cartService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found: " + id));
        CartItem item = new CartItem();
        item.setId(UUID.randomUUID().toString());
        item.setCartId(cart.getId());
        item.setProductId(request.getProductId());
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(request.getUnitPrice());
        cartItemService.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(get(id));
    }

    @PutMapping("/{id}/items/{itemId}")
    public CartResponse updateItem(@PathVariable String id, @PathVariable String itemId, @Valid @RequestBody CartItemRequest request) {
        CartItem item = cartItemService.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + itemId));
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(request.getUnitPrice());
        cartItemService.save(item);
        return get(id);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable String id, @PathVariable String itemId) {
        cartItemService.deleteById(itemId);
        return ResponseEntity.ok(get(id));
    }

    private CartResponse toResponse(Cart cart, List<CartItem> items) {
        CartResponse res = new CartResponse();
        res.setId(cart.getId());
        res.setUserId(cart.getUserId());
        res.setSessionId(cart.getSessionId());
        res.setStatus(cart.getStatus());
        res.setExpiresAt(cart.getExpiresAt());
        List<CartItemDto> dtos = items.stream().map(ci -> {
            CartItemDto dto = new CartItemDto();
            dto.setId(ci.getId());
            dto.setProductId(ci.getProductId());
            dto.setQuantity(ci.getQuantity());
            dto.setUnitPrice(ci.getUnitPrice());
            return dto;
        }).toList();
        res.setItems(dtos);
        return res;
    }
}
