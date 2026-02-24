package com.skishop.web.controller.admin;

import com.skishop.domain.order.Order;
import com.skishop.domain.order.OrderItem;
import com.skishop.service.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/order/detail")
public class AdminOrderDetailController {
    private final OrderService orderService;

    public AdminOrderDetailController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String showOrderDetail(@RequestParam(required = false) String orderId, Model model) {
        if (orderId != null && !orderId.trim().isEmpty()) {
            Order order = orderService.findById(orderId);
            if (order != null) {
                List<OrderItem> items = orderService.listItems(orderId);
                model.addAttribute("order", order);
                model.addAttribute("orderItems", items);
            }
        }
        return "admin/order-detail";
    }
}
