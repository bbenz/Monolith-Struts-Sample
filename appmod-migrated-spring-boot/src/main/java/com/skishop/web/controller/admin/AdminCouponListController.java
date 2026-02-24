package com.skishop.web.controller.admin;

import com.skishop.dao.coupon.CouponDao;
import com.skishop.domain.coupon.Coupon;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/coupons")
public class AdminCouponListController {
    private final CouponDao couponDao;

    public AdminCouponListController(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @GetMapping
    public String listCoupons(Model model) {
        List<Coupon> coupons = couponDao.listAll();
        model.addAttribute("coupons", coupons);
        return "admin/coupon-list";
    }
}
