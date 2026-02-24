package com.skishop.web.controller;

import com.skishop.common.util.PasswordHasher;
import com.skishop.dao.user.PasswordResetTokenDao;
import com.skishop.domain.user.PasswordResetToken;
import com.skishop.domain.user.User;
import com.skishop.service.user.UserService;
import com.skishop.web.dto.PasswordResetRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Controller
@RequestMapping("/password/reset")
public class PasswordResetController {
    private final PasswordResetTokenDao tokenDao;
    private final UserService userService;

    public PasswordResetController(PasswordResetTokenDao tokenDao, UserService userService) {
        this.tokenDao = tokenDao;
        this.userService = userService;
    }

    @GetMapping
    public String showPasswordReset(@RequestParam String token, Model model) {
        PasswordResetRequest resetRequest = new PasswordResetRequest();
        resetRequest.setToken(token);
        model.addAttribute("passwordResetRequest", resetRequest);
        return "password-reset";
    }

    @PostMapping
    public String processPasswordReset(@Valid @ModelAttribute PasswordResetRequest resetRequest,
                                      BindingResult result,
                                      Model model) {
        if (result.hasErrors()) {
            return "password-reset";
        }

        PasswordResetToken token = tokenDao.findByToken(resetRequest.getToken());
        if (token == null || token.getUsedAt() != null || isExpired(token)) {
            model.addAttribute("error", "Invalid or expired password reset token");
            return "password-reset-failure";
        }

        User user = userService.findById(token.getUserId());
        if (user == null) {
            model.addAttribute("error", "Invalid or expired password reset token");
            return "password-reset-failure";
        }

        try {
            String salt = PasswordHasher.generateSalt();
            String hashed = PasswordHasher.hash(resetRequest.getPassword(), salt);
            userService.updatePassword(user.getId(), hashed, salt);
            tokenDao.markUsed(token.getId());
            return "password-reset-success";
        } catch (NoSuchAlgorithmException e) {
            model.addAttribute("error", "Password reset failed. Please try again.");
            return "password-reset-failure";
        }
    }

    private boolean isExpired(PasswordResetToken token) {
        if (token.getExpiresAt() == null) {
            return true;
        }
        return token.getExpiresAt().before(new Date());
    }
}
