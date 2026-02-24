package com.skishop.controller;

import com.skishop.dto.UserRegisterRequest;
import com.skishop.dto.UserResponse;
import com.skishop.model.entity.User;
import com.skishop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPasswordHash(request.getPasswordHash());
        user.setSalt(request.getSalt());
        user.setStatus("ACTIVE");
        user.setRole("USER");
        User saved = userService.save(user);
        UserResponse resp = new UserResponse();
        resp.setId(saved.getId());
        resp.setEmail(saved.getEmail());
        resp.setUsername(saved.getUsername());
        resp.setStatus(saved.getStatus());
        resp.setRole(saved.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
