package com.skishop.web.controller;

import com.skishop.domain.order.Order;
import com.skishop.domain.user.User;
import com.skishop.service.order.OrderFacade;
import com.skishop.web.dto.CheckoutRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private final OrderFacade orderFacade;

    public CheckoutController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @GetMapping
    public String showCheckout(HttpSession session, Model model) {
        var cartId = (String) session.getAttribute("cartId");
        if (cartId == null) {
            return "redirect:/cart";
        }
        var checkoutRequest = new CheckoutRequest();
        checkoutRequest.setCartId(cartId);
        model.addAttribute("checkoutRequest", checkoutRequest);
        return "cart/checkout";
    }

    @PostMapping
    public String processCheckout(@Valid @ModelAttribute CheckoutRequest checkoutRequest,
                                  BindingResult result,
                                  HttpSession session,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "cart/checkout";
        }

        String cartId = checkoutRequest.getCartId();
        if (cartId == null || cartId.isEmpty()) {
            cartId = (String) session.getAttribute("cartId");
            checkoutRequest.setCartId(cartId);
        }

        User user = (User) session.getAttribute("loginUser");
        String userId = user != null ? user.getId() : null;

        try {
            Order order = orderFacade.placeOrder(
                cartId,
                checkoutRequest.getCouponCode(),
                checkoutRequest.getUsePoints(),
                checkoutRequest.toPaymentInfo(),
                userId
            );
            model.addAttribute("order", order);
            return "cart/confirmation";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Checkout failed. Please try again.");
            return "cart/checkout";
        }
    }
}
