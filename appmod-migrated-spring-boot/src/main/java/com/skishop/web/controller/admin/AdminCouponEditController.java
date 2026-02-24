package com.skishop.web.controller.admin;

import com.skishop.dao.coupon.CouponDao;
import com.skishop.domain.coupon.Coupon;
import com.skishop.web.dto.admin.AdminCouponRequest;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/admin/coupon/edit")
public class AdminCouponEditController {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private final CouponDao couponDao;

    public AdminCouponEditController(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @GetMapping
    public String showEditForm(@RequestParam(required = false) String code, Model model) {
        AdminCouponRequest couponRequest = new AdminCouponRequest();
        
        if (code != null && !code.isEmpty()) {
            Coupon coupon = couponDao.findByCode(code);
            if (coupon != null) {
                couponRequest.setId(coupon.getId());
                couponRequest.setCampaignId(coupon.getCampaignId());
                couponRequest.setCode(coupon.getCode());
                couponRequest.setCouponType(coupon.getCouponType());
                couponRequest.setDiscountValue(toStringValue(coupon.getDiscountValue()));
                couponRequest.setDiscountType(coupon.getDiscountType());
                couponRequest.setMinimumAmount(toStringValue(coupon.getMinimumAmount()));
                couponRequest.setMaximumDiscount(toStringValue(coupon.getMaximumDiscount()));
                couponRequest.setUsageLimit(coupon.getUsageLimit());
                couponRequest.setActive(coupon.isActive());
                couponRequest.setExpiresAt(formatDate(coupon.getExpiresAt()));
            }
        } else {
            couponRequest.setId(UUID.randomUUID().toString());
            couponRequest.setActive(true);
        }
        
        model.addAttribute("adminCouponRequest", couponRequest);
        return "admin/coupon-edit";
    }

    @PostMapping
    public String saveCoupon(@Valid @ModelAttribute AdminCouponRequest couponRequest,
                            BindingResult result,
                            Model model) {
        if (result.hasErrors()) {
            return "admin/coupon-edit";
        }

        if (couponRequest.getCode() == null || couponRequest.getCode().isEmpty()) {
            model.addAttribute("error", "Coupon code is required");
            return "admin/coupon-edit-failure";
        }

        BigDecimal discountValue;
        try {
            discountValue = new BigDecimal(couponRequest.getDiscountValue());
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Invalid discount value format");
            return "admin/coupon-edit-failure";
        }

        BigDecimal minimumAmount;
        BigDecimal maximumDiscount;
        Date expiresAt;
        
        try {
            minimumAmount = parseOptionalDecimal(couponRequest.getMinimumAmount());
            maximumDiscount = parseOptionalDecimal(couponRequest.getMaximumDiscount());
            expiresAt = parseOptionalDate(couponRequest.getExpiresAt());
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Invalid numeric format");
            return "admin/coupon-edit-failure";
        } catch (ParseException e) {
            model.addAttribute("error", "Invalid date format");
            return "admin/coupon-edit-failure";
        }

        Coupon coupon = new Coupon();
        coupon.setId(couponRequest.getId());
        if (coupon.getId() == null || coupon.getId().isEmpty()) {
            coupon.setId(UUID.randomUUID().toString());
        }
        coupon.setCampaignId(couponRequest.getCampaignId());
        coupon.setCode(couponRequest.getCode());
        coupon.setCouponType(couponRequest.getCouponType());
        coupon.setDiscountValue(discountValue);
        coupon.setDiscountType(couponRequest.getDiscountType());
        coupon.setMinimumAmount(minimumAmount);
        coupon.setMaximumDiscount(maximumDiscount);
        coupon.setUsageLimit(couponRequest.getUsageLimit());
        coupon.setActive(couponRequest.isActive());
        coupon.setExpiresAt(expiresAt);
        couponDao.saveOrUpdate(coupon);

        return "redirect:/admin/coupons";
    }

    private BigDecimal parseOptionalDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return new BigDecimal(value.trim());
    }

    private Date parseOptionalDate(String value) throws ParseException {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        formatter.setLenient(false);
        return formatter.parse(value.trim());
    }

    private String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.format(date);
    }

    private String toStringValue(BigDecimal value) {
        return value != null ? value.toString() : null;
    }
}
