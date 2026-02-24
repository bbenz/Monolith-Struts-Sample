package com.skishop.web.controller;

import com.skishop.domain.order.Order;
import com.skishop.domain.user.User;
import com.skishop.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderHistoryController {
    private final OrderService orderService;

    public OrderHistoryController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String showOrderHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<Order> orders;
        if (user.getRole() != null && "ADMIN".equalsIgnoreCase(user.getRole())) {
            orders = orderService.listAll(50);
        } else {
            orders = orderService.listByUserId(user.getId());
        }

        System.out.println("[OrderHistoryController] user=" + user.getEmail() + 
                          " role=" + user.getRole() + 
                          " orders=" + (orders != null ? orders.size() : null));
        
        model.addAttribute("orders", orders);
        return "orders/history";
    }
}
