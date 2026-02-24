package com.skishop.web.controller;

import com.skishop.domain.cart.Cart;
import com.skishop.domain.cart.CartItem;
import com.skishop.domain.user.User;
import com.skishop.service.cart.CartService;
import com.skishop.web.dto.AddCartRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    private static final int CART_COOKIE_MAX_AGE = 30 * 24 * 60 * 60; // 30 days in seconds
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String showCart(HttpServletRequest request, HttpServletResponse response, Model model) {
        String cartId = getOrCreateCartId(request, response);
        loadCartData(cartId, model);
        return "cart/view";
    }

    @PostMapping
    public String addToCart(@Valid @ModelAttribute AddCartRequest addCartRequest,
                           BindingResult result,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           Model model) {
        if (result.hasErrors()) {
            String cartId = getOrCreateCartId(request, response);
            loadCartData(cartId, model);
            return "cart/view";
        }

        String cartId = getOrCreateCartId(request, response);
        cartService.addItem(cartId, addCartRequest.getProductId(), addCartRequest.getQuantity());
        loadCartData(cartId, model);
        return "cart/view";
    }

    private String getOrCreateCartId(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        String cartId = (String) session.getAttribute("cartId");
        
        if (cartId == null) {
            User user = (User) session.getAttribute("loginUser");
            Cart cart = cartService.createCart(user != null ? user.getId() : null, session.getId());
            cartId = cart.getId();
            session.setAttribute("cartId", cartId);
            addCartCookie(request, response, cartId);
        }
        
        return cartId;
    }

    private void loadCartData(String cartId, Model model) {
        List<CartItem> items = cartService.getItems(cartId);
        BigDecimal subtotal = cartService.calculateSubtotal(items);
        model.addAttribute("cartItems", items);
        model.addAttribute("cartSubtotal", subtotal);
        model.addAttribute("cartId", cartId);
    }

    private void addCartCookie(HttpServletRequest request, HttpServletResponse response, String cartId) {
        Cookie cookie = new Cookie("CART_ID", cartId);
        cookie.setMaxAge(CART_COOKIE_MAX_AGE);
        cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        cookie.setHttpOnly(true);
        if (request.isSecure()) {
            cookie.setSecure(true);
        }
        response.addCookie(cookie);
    }
}
