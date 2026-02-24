package com.skishop.service;

import com.skishop.model.entity.User;
import com.skishop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends BaseService<User, String> {
    private final UserRepository userRepository;
    public UserService(UserRepository repository) {
        super(repository);
        this.userRepository = repository;
    }
    public Optional<User> findByEmail(String email) { return userRepository.findByEmail(email); }
}
