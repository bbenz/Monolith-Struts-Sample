package com.skishop.controller;

import com.skishop.model.entity.User;
import com.skishop.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> search(@RequestParam(required = false) String q) {
        return userService.findAll().stream()
            .filter(u -> q == null || u.getUsername().contains(q) || u.getEmail().contains(q))
            .toList();
    }
}
