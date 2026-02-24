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
@RequestMapping("/order/return")
public class OrderReturnController {
    private final OrderFacade orderFacade;

    public OrderReturnController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @PostMapping
    public String returnOrder(@RequestParam(required = false) String orderId,
                             HttpSession session,
                             Model model) {
        User user = (User) session.getAttribute("loginUser");

        if (orderId == null || orderId.isEmpty()) {
            model.addAttribute("error", "Order not found");
            return "order-return-failure";
        }

        try {
            orderFacade.returnOrder(orderId, user != null ? user.getId() : null);
            return "order-return-success";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Failed to return order");
            return "order-return-failure";
        }
    }
}
