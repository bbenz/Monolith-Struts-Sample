package com.skishop.service;

import com.skishop.model.entity.PasswordResetToken;
import com.skishop.repository.PasswordResetTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordResetTokenService extends BaseService<PasswordResetToken, String> {
    private final PasswordResetTokenRepository repository;
    public PasswordResetTokenService(PasswordResetTokenRepository repository) {
        super(repository);
        this.repository = repository;
    }
    public Optional<PasswordResetToken> findByToken(String token) { return repository.findByToken(token); }
}
