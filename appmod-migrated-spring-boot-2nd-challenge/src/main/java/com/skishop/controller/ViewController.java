package com.skishop.controller;

import com.skishop.exception.ResourceNotFoundException;
import com.skishop.model.entity.Cart;
import com.skishop.model.entity.CartItem;
import com.skishop.model.entity.Price;
import com.skishop.model.entity.Product;
import com.skishop.model.entity.Order;
import com.skishop.model.entity.OrderItem;
import com.skishop.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class ViewController {
    private static final String SESSION_CART_ID = "CART_ID";
    private final ProductService productService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final PriceService priceService;
    private final PointTransactionService pointTransactionService;
    private final UserAddressService userAddressService;

    public ViewController(ProductService productService,
                         OrderService orderService,
                         OrderItemService orderItemService,
                         UserService userService,
                         CartService cartService,
                         CartItemService cartItemService,
                         PriceService priceService,
                         PointTransactionService pointTransactionService,
                         UserAddressService userAddressService) {
        this.productService = productService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.userService = userService;
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.priceService = priceService;
        this.pointTransactionService = pointTransactionService;
        this.userAddressService = userAddressService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", productService.findAll());
        return "index";
    }

    @GetMapping("/ui/products")
    public String products(Model model, @RequestParam(required = false) String keyword) {
        var products = productService.findAll();
        if (keyword != null && !keyword.isBlank()) {
            var kw = keyword.toLowerCase();
            products = products.stream()
                    .filter(p ->
                            (p.getName() != null && p.getName().toLowerCase().contains(kw)) ||
                            (p.getBrand() != null && p.getBrand().toLowerCase().contains(kw)) ||
                            (p.getDescription() != null && p.getDescription().toLowerCase().contains(kw))
                    ).toList();
        }
        // map price by productId (first price)
        var prices = priceService.findAll().stream()
            .collect(java.util.stream.Collectors.toMap(
                Price::getProductId,
                p -> p,
                (p1, p2) -> p1,
                java.util.LinkedHashMap::new
            ));
        model.addAttribute("products", products);
        model.addAttribute("prices", prices);
        model.addAttribute("keyword", keyword);
        return "products/list";
    }
    @PostMapping("/ui/cart/add")
    public String addToCart(@RequestParam String productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session,
                            RedirectAttributes ra) {
        String cartId = (String) session.getAttribute(SESSION_CART_ID);
        if (cartId == null) {
            var cart = new Cart();
            cart.setId(UUID.randomUUID().toString());
            cart.setStatus("OPEN");
            cartService.save(cart);
            cartId = cart.getId();
            session.setAttribute(SESSION_CART_ID, cartId);
        }
        var price = priceService.findByProductId(productId).stream().findFirst().map(Price::getRegularPrice).orElse(java.math.BigDecimal.ZERO);
        var item = new CartItem();
        item.setId(UUID.randomUUID().toString());
        item.setCartId(cartId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setUnitPrice(price);
        cartItemService.save(item);
        ra.addFlashAttribute("message", "Added to cart");
        return "redirect:/ui/cart";
    }

    @GetMapping("/ui/products/{id}")
    public String productDetail(@PathVariable String id, Model model) {
        var product = productService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        var prices = priceService.findByProductId(id);
        model.addAttribute("product", product);
        model.addAttribute("prices", prices);
        return "products/detail";
    }

    @GetMapping("/ui/cart")
    public String cart(Model model, HttpSession session) {
        String cartId = (String) session.getAttribute(SESSION_CART_ID);
        if (cartId == null) {
            var cart = new Cart();
            cart.setId(UUID.randomUUID().toString());
            cart.setStatus("OPEN");
            cartService.save(cart);
            cartId = cart.getId();
            session.setAttribute(SESSION_CART_ID, cartId);
        }
        var cart = cartService.findById(cartId).orElse(null);
        var items = cartItemService.findByCartId(cartId);
        Map<String, Product> productMap = productService.findAll().stream()
            .collect(java.util.stream.Collectors.toMap(Product::getId, p -> p, (a, b) -> a));
        model.addAttribute("cart", cart);
        model.addAttribute("items", items);
        model.addAttribute("productMap", productMap);
        return "cart/detail";
    }

    @GetMapping("/ui/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "orders/list";
    }

    @GetMapping("/ui/orders/{id}")
    public String orderDetail(@PathVariable String id, Model model) {
        var order = orderService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
        var items = orderItemService.findByOrderId(id);
        model.addAttribute("order", order);
        model.addAttribute("items", items);
        return "orders/detail";
    }

    @GetMapping("/ui/points")
    public String points(Model model) {
        model.addAttribute("points", pointTransactionService.findAll());
        return "points/history";
    }

    @GetMapping("/ui/addresses")
    public String addresses(Model model) {
        model.addAttribute("addresses", userAddressService.findAll());
        return "addresses/list";
    }

}
