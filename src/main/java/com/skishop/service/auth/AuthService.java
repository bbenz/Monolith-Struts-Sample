package com.skishop.service.auth;

import com.skishop.dao.user.UserDao;
import com.skishop.dao.user.UserDaoImpl;
import com.skishop.domain.user.User;
import com.skishop.common.util.PasswordHasher;

public class AuthService {
  private final UserDao userDao = new UserDaoImpl();

  public AuthResult authenticate(String email, String passwordRaw) {
    if (email == null || passwordRaw == null) {
      return AuthResult.failure("INVALID_INPUT");
    }
    User user = userDao.findByEmail(email);
    if (user == null) {
      return AuthResult.failure("USER_NOT_FOUND");
    }
    if (!PasswordHasher.matches(passwordRaw, user.getPasswordHash(), user.getSalt())) {
      return AuthResult.failure("INVALID_CREDENTIALS");
    }
    return AuthResult.success(user);
  }
}
