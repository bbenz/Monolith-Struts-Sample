package com.skishop.controller;

import com.skishop.service.PointAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/points")
public class PointController {
    private final PointAccountService pointAccountService;

    public PointController(PointAccountService pointAccountService) {
        this.pointAccountService = pointAccountService;
    }

    @GetMapping("/balance")
    public Map<String, Object> balance(@RequestParam String userId) {
        var account = pointAccountService.findAll().stream().filter(a -> userId.equals(a.getUserId())).findFirst();
        int balance = account.map(a -> a.getBalance()).orElse(0);
        return Map.of("userId", userId, "balance", balance);
    }
}
