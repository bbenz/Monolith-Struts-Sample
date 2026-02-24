package com.skishop.web.controller.admin;

import com.skishop.dao.shipping.ShippingMethodDao;
import com.skishop.domain.shipping.ShippingMethod;
import com.skishop.web.dto.admin.AdminShippingMethodRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
@RequestMapping("/admin/shipping-method/edit")
public class AdminShippingMethodEditController {
    private final ShippingMethodDao shippingMethodDao;

    public AdminShippingMethodEditController(ShippingMethodDao shippingMethodDao) {
        this.shippingMethodDao = shippingMethodDao;
    }

    @GetMapping
    public String showEditForm(@RequestParam(required = false) String code, Model model) {
        AdminShippingMethodRequest shippingRequest = new AdminShippingMethodRequest();
        
        if (code != null && !code.isEmpty()) {
            ShippingMethod method = shippingMethodDao.findByCode(code);
            if (method != null) {
                shippingRequest.setId(method.getId());
                shippingRequest.setCode(method.getCode());
                shippingRequest.setName(method.getName());
                shippingRequest.setFee(method.getFee() != null ? method.getFee().toString() : null);
                shippingRequest.setActive(method.isActive());
                shippingRequest.setSortOrder(method.getSortOrder());
            }
        } else {
            shippingRequest.setId(generateId());
            shippingRequest.setActive(true);
        }
        
        model.addAttribute("adminShippingMethodRequest", shippingRequest);
        return "admin/shipping-method-edit";
    }

    @PostMapping
    public String saveShippingMethod(@Valid @ModelAttribute AdminShippingMethodRequest shippingRequest,
                                    BindingResult result,
                                    Model model) {
        if (result.hasErrors()) {
            return "admin/shipping-method-edit";
        }

        if (shippingRequest.getCode() == null || shippingRequest.getCode().isEmpty()) {
            model.addAttribute("error", "Shipping method code is required");
            return "admin/shipping-method-edit-failure";
        }

        BigDecimal fee;
        try {
            fee = new BigDecimal(shippingRequest.getFee());
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Invalid fee format");
            return "admin/shipping-method-edit-failure";
        }

        ShippingMethod existing = shippingMethodDao.findByCode(shippingRequest.getCode());
        ShippingMethod method = new ShippingMethod();
        String methodId = existing != null ? existing.getId() : shippingRequest.getId();
        if (methodId == null || methodId.isEmpty()) {
            methodId = generateId();
        }
        method.setId(methodId);
        method.setCode(shippingRequest.getCode());
        method.setName(shippingRequest.getName());
        method.setFee(fee);
        method.setActive(shippingRequest.isActive());
        method.setSortOrder(shippingRequest.getSortOrder());

        if (existing == null) {
            shippingMethodDao.insert(method);
        } else {
            shippingMethodDao.update(method);
        }

        return "redirect:/admin/shipping-methods";
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }
}
