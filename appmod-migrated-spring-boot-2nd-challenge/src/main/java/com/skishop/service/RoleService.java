package com.skishop.service;

import com.skishop.model.entity.Role;
import com.skishop.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService<Role, String> {
    public RoleService(RoleRepository repository) { super(repository); }
}
