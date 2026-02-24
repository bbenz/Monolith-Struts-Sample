package com.skishop.web.controller;

import com.skishop.dao.address.UserAddressDao;
import com.skishop.domain.address.Address;
import com.skishop.domain.user.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/addresses")
public class AddressListController {
    private final UserAddressDao addressDao;

    public AddressListController(UserAddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @GetMapping
    public String showAddressList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<Address> addresses = addressDao.listByUserId(user.getId());
        model.addAttribute("addresses", addresses);
        return "account/addresses";
    }
}
