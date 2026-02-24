package com.skishop.service;

import com.skishop.model.entity.UserAddress;
import com.skishop.repository.UserAddressRepository;
import org.springframework.stereotype.Service;

@Service
public class UserAddressService extends BaseService<UserAddress, String> {
    public UserAddressService(UserAddressRepository repository) { super(repository); }
}
