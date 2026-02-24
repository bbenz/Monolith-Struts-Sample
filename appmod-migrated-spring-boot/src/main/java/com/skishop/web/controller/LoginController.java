package com.skishop.web.controller;

import com.skishop.domain.user.User;
import com.skishop.service.auth.AuthResult;
import com.skishop.service.auth.AuthService;
import com.skishop.web.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Spring MVC Controller for authentication operations.
 * Migrated from Struts LoginAction to Spring Boot.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    @Autowired
    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginRequest());
        return "auth/login";
    }

    @PostMapping
    public String processLogin(@Valid @ModelAttribute("loginForm") LoginRequest loginRequest,
                               BindingResult bindingResult,
                               HttpServletRequest request,
                               HttpSession session,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }

        AuthResult result = authService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword(),
                request.getRemoteAddr(),
                request.getHeader("User-Agent")
        );

        if (!result.isSuccess()) {
            model.addAttribute("errorMessage", "Login failed. Please check your email and password.");
            return "auth/login";
        }

        // Invalidate old session and create new one for security
        session.invalidate();
        session = request.getSession(true);
        
        User user = result.getUser();
        session.setAttribute("loginUser", user);

        return "redirect:/home";
    }
}
