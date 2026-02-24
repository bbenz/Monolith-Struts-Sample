package com.skishop.web.controller.admin;

import com.skishop.domain.order.Order;
import com.skishop.service.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/order/update")
public class AdminOrderUpdateController {
    private final OrderService orderService;

    public AdminOrderUpdateController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public String updateOrder(@RequestParam(required = false) String orderId,
                             @RequestParam(required = false) String status,
                             @RequestParam(required = false) String paymentStatus,
                             Model model) {
        if (orderId == null || orderId.isEmpty()) {
            model.addAttribute("error", "Order not found");
            return "admin/order-update-failure";
        }

        Order order = orderService.findById(orderId);
        if (order == null) {
            model.addAttribute("error", "Order not found");
            return "admin/order-update-failure";
        }

        if (status != null && !status.isEmpty()) {
            orderService.updateStatus(orderId, status);
        }
        if (paymentStatus != null && !paymentStatus.isEmpty()) {
            orderService.updatePaymentStatus(orderId, paymentStatus);
        }

        return "redirect:/admin/order/detail?orderId=" + orderId;
    }
}
