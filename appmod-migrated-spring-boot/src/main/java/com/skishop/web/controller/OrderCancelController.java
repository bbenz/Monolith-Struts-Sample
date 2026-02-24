package com.skishop.web.controller;

import com.skishop.domain.user.User;
import com.skishop.service.order.OrderFacade;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/order/cancel")
public class OrderCancelController {
    private final OrderFacade orderFacade;

    public OrderCancelController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @PostMapping
    public String cancelOrder(@RequestParam(required = false) String orderId,
                             HttpSession session,
                             Model model) {
        User user = (User) session.getAttribute("loginUser");

        if (orderId == null || orderId.isEmpty()) {
            model.addAttribute("error", "Order not found");
            return "order-cancel-failure";
        }

        try {
            orderFacade.cancelOrder(orderId, user != null ? user.getId() : null);
            return "order-cancel-success";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Failed to cancel order");
            return "order-cancel-failure";
        }
    }
}
