package com.skishop.controller;

import com.skishop.model.entity.User;
import com.skishop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/ui/login")
    public String loginForm(@RequestParam(required = false) String email, Model model, HttpSession session, RedirectAttributes ra) {
        if (email != null && !email.isBlank()) {
            var user = userService.findByEmail(email).orElse(null);
            if (user != null) {
                session.setAttribute("loginUser", user);
                ra.addFlashAttribute("message", "Logged in: " + user.getUsername());
                return "redirect:/";
            }
            ra.addFlashAttribute("error", "User not found");
        }
        model.addAttribute("users", userService.findAll());
        return "auth/login";
    }

    @PostMapping("/ui/login")
    public String doLogin(@RequestParam String email, HttpSession session, RedirectAttributes ra) {
        var user = userService.findByEmail(email).orElse(null);
        if (user == null) {
            ra.addFlashAttribute("error", "User not found");
            return "redirect:/ui/login";
        }
        session.setAttribute("loginUser", user);
        ra.addFlashAttribute("message", "Logged in: " + user.getUsername());
        return "redirect:/";
    }

    @GetMapping("/ui/logout")
    public String logout(HttpSession session, RedirectAttributes ra) {
        session.removeAttribute("loginUser");
        ra.addFlashAttribute("message", "Logged out");
        return "redirect:/";
    }
}
