package com.skishop.web.controller;

import com.skishop.dao.address.UserAddressDao;
import com.skishop.domain.address.Address;
import com.skishop.domain.user.User;
import com.skishop.web.dto.AddressRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/addresses/save")
public class AddressSaveController {
    private static final int MAX_ADDRESSES_PER_USER = 10;
    private final UserAddressDao addressDao;

    public AddressSaveController(UserAddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @GetMapping
    public String showAddressForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("addressRequest", new AddressRequest());
        return "account/address_edit";
    }

    @PostMapping
    public String saveAddress(@Valid @ModelAttribute AddressRequest addressRequest,
                             BindingResult result,
                             HttpSession session,
                             Model model) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "account/address_edit";
        }

        List<Address> existing = addressDao.listByUserId(user.getId());
        if (existing != null && existing.size() >= MAX_ADDRESSES_PER_USER) {
            model.addAttribute("error", "Maximum address limit reached");
            return "address-save-failure";
        }

        Address address = new Address();
        address.setId(resolveAddressId(addressRequest));
        address.setUserId(user.getId());
        address.setLabel(addressRequest.getLabel());
        address.setRecipientName(addressRequest.getRecipientName());
        address.setPostalCode(addressRequest.getPostalCode());
        address.setPrefecture(addressRequest.getPrefecture());
        address.setAddress1(addressRequest.getAddress1());
        address.setAddress2(addressRequest.getAddress2());
        address.setPhone(addressRequest.getPhone());
        address.setDefault(addressRequest.isDefault());
        Date now = new Date();
        address.setCreatedAt(now);
        address.setUpdatedAt(now);
        addressDao.save(address);

        return "redirect:/addresses";
    }

    private String resolveAddressId(AddressRequest request) {
        if (request.getId() != null && !request.getId().isEmpty()) {
            return request.getId();
        }
        return UUID.randomUUID().toString();
    }
}
