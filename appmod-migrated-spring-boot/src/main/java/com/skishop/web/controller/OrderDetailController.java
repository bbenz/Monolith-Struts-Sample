package com.skishop.web.controller;

import com.skishop.domain.order.Order;
import com.skishop.domain.order.OrderItem;
import com.skishop.domain.user.User;
import com.skishop.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/order/detail")
public class OrderDetailController {
    private final OrderService orderService;

    public OrderDetailController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String showOrderDetail(@RequestParam(required = false) String orderId,
                                  HttpSession session,
                                  Model model) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/login";
        }

        if (orderId != null && !orderId.trim().isEmpty()) {
            Order order = orderService.findById(orderId);
            if (order != null && user.getId().equals(order.getUserId())) {
                List<OrderItem> items = orderService.listItems(orderId);
                model.addAttribute("order", order);
                model.addAttribute("orderItems", items);
            }
        }

        return "orders/detail";
    }
}
