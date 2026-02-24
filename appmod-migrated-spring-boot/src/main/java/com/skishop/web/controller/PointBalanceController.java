package com.skishop.web.controller;

import com.skishop.domain.point.PointAccount;
import com.skishop.domain.user.User;
import com.skishop.service.point.PointService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/points")
public class PointBalanceController {
    private final PointService pointService;

    public PointBalanceController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping({"", "/", "/balance"})
    public String showPointBalance(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/login";
        }

        PointAccount account = pointService.getAccount(user.getId());
        model.addAttribute("pointBalance", account);
        return "points/balance";
    }
}
