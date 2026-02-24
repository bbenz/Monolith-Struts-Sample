package com.skishop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/reports")
public class AdminReportController {
    @GetMapping("/sales")
    public Map<String, Object> sales() {
        return Map.of("totalSales", 0, "orders", 0);
    }
}
