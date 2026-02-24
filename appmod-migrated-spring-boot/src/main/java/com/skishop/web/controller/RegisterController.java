package com.skishop.web.controller;

import com.skishop.common.util.PasswordHasher;
import com.skishop.domain.user.User;
import com.skishop.service.user.UserService;
import com.skishop.web.dto.RegisterRequest;
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
 * Spring MVC Controller for user registration operations.
 * Migrated from Struts RegisterAction to Spring Boot.
 */
@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerForm", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping
    public String processRegistration(@Valid @ModelAttribute("registerForm") RegisterRequest registerRequest,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        // Check if passwords match
        if (!registerRequest.isPasswordMatch()) {
            model.addAttribute("errorMessage", "Passwords do not match");
            return "auth/register";
        }

        // Check if email already exists
        if (userService.findByEmail(registerRequest.getEmail()) != null) {
            model.addAttribute("errorMessage", "Email already registered");
            return "auth/register";
        }

        try {
            // Create new user
            User user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setUsername(registerRequest.getUsername());
            
            // Hash password
            String salt = PasswordHasher.generateSalt();
            String hash = PasswordHasher.hash(registerRequest.getPassword(), salt);
            user.setPasswordHash(hash);
            user.setSalt(salt);
            
            userService.register(user);
            
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful. Please login.");
            return "redirect:/login";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "auth/register";
        }
    }
}
