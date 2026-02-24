package com.skishop.web.controller.admin;

import com.skishop.service.order.OrderFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/order/refund")
public class AdminOrderRefundController {
    private final OrderFacade orderFacade;

    public AdminOrderRefundController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @PostMapping
    public String refundOrder(@RequestParam(required = false) String orderId,
                             @RequestParam(required = false) String id,
                             Model model) {
        String effectiveOrderId = orderId != null ? orderId : id;
        
        if (effectiveOrderId == null || effectiveOrderId.isEmpty()) {
            model.addAttribute("error", "Order not found");
            return "admin/order-refund-failure";
        }

        try {
            orderFacade.returnOrder(effectiveOrderId, null);
            return "redirect:/admin/orders";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Failed to refund order");
            return "admin/order-refund-failure";
        }
    }
}
