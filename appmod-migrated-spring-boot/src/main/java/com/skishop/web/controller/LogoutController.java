package com.skishop.web.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Spring MVC Controller for logout operations.
 * Migrated from Struts LogoutAction to Spring Boot.
 */
@Controller
@RequestMapping("/logout")
public class LogoutController {

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}
