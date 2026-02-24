package com.skishop.web.controller;

import com.skishop.dao.user.PasswordResetTokenDao;
import com.skishop.domain.user.PasswordResetToken;
import com.skishop.domain.user.User;
import com.skishop.service.mail.MailService;
import com.skishop.service.user.UserService;
import com.skishop.web.dto.PasswordResetRequestRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/password/forgot")
public class PasswordForgotController {
    private final UserService userService;
    private final PasswordResetTokenDao tokenDao;
    private final MailService mailService;

    public PasswordForgotController(UserService userService, 
                                   PasswordResetTokenDao tokenDao, 
                                   MailService mailService) {
        this.userService = userService;
        this.tokenDao = tokenDao;
        this.mailService = mailService;
    }

    @GetMapping
    public String showPasswordForgot(Model model) {
        model.addAttribute("passwordResetRequestRequest", new PasswordResetRequestRequest());
        return "password-forgot";
    }

    @PostMapping
    public String processPasswordForgot(@Valid @ModelAttribute PasswordResetRequestRequest resetRequest,
                                       BindingResult result,
                                       Model model) {
        if (result.hasErrors()) {
            return "password-forgot";
        }

        User user = userService.findByEmail(resetRequest.getEmail());
        if (user != null) {
            PasswordResetToken token = new PasswordResetToken();
            token.setId(UUID.randomUUID().toString());
            token.setUserId(user.getId());
            token.setToken(UUID.randomUUID().toString());
            token.setExpiresAt(addHours(new Date(), 1));
            token.setUsedAt(null);
            tokenDao.insert(token);
            model.addAttribute("resetToken", token.getToken());
            mailService.enqueuePasswordReset(user.getEmail(), token.getToken());
        }

        return "password-forgot-success";
    }

    private Date addHours(Date base, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(base);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return cal.getTime();
    }
}
