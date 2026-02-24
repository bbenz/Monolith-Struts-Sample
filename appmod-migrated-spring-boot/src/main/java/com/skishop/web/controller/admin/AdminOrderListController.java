package com.skishop.web.controller.admin;

import com.skishop.domain.order.Order;
import com.skishop.service.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderListController {
    private static final int MAX_ADMIN_ORDERS = 200;
    private final OrderService orderService;

    public AdminOrderListController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.listAll(MAX_ADMIN_ORDERS);
        model.addAttribute("orders", orders);
        return "admin/order-list";
    }
}
