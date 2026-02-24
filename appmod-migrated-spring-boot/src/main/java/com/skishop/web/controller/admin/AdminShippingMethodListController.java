package com.skishop.web.controller.admin;

import com.skishop.dao.shipping.ShippingMethodDao;
import com.skishop.domain.shipping.ShippingMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/shipping-methods")
public class AdminShippingMethodListController {
    private final ShippingMethodDao shippingMethodDao;

    public AdminShippingMethodListController(ShippingMethodDao shippingMethodDao) {
        this.shippingMethodDao = shippingMethodDao;
    }

    @GetMapping
    public String listShippingMethods(Model model) {
        List<ShippingMethod> methods = shippingMethodDao.listAll();
        model.addAttribute("shippingMethods", methods);
        return "admin/shipping-method-list";
    }
}
