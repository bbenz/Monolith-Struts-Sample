package com.skishop.controller;

import com.skishop.dto.AddressRequest;
import com.skishop.exception.ResourceNotFoundException;
import com.skishop.model.entity.UserAddress;
import com.skishop.service.UserAddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final UserAddressService userAddressService;

    public AddressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    @GetMapping
    public List<UserAddress> list(@RequestParam String userId) {
        return userAddressService.findAll().stream()
                .filter(a -> userId.equals(a.getUserId()))
            .toList(); // Ensure no Collectors remains
    }

    @PostMapping
    public ResponseEntity<UserAddress> create(@Valid @RequestBody AddressRequest req) {
        UserAddress ua = new UserAddress();
        ua.setId(UUID.randomUUID().toString());
        ua.setUserId(req.getUserId());
        ua.setLabel(req.getLabel());
        ua.setRecipientName(req.getRecipientName());
        ua.setPostalCode(req.getPostalCode());
        ua.setPrefecture(req.getPrefecture());
        ua.setAddress1(req.getAddress1());
        ua.setAddress2(req.getAddress2());
        ua.setPhone(req.getPhone());
        ua.setIsDefault(req.getIsDefault());
        return ResponseEntity.status(HttpStatus.CREATED).body(userAddressService.save(ua));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        UserAddress ua = userAddressService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found: " + id));
        userAddressService.deleteById(ua.getId());
        return ResponseEntity.noContent().build();
    }
}
