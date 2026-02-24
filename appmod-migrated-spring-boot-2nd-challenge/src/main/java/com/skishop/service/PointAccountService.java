package com.skishop.service;

import com.skishop.model.entity.PointAccount;
import com.skishop.repository.PointAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class PointAccountService extends BaseService<PointAccount, String> {
    public PointAccountService(PointAccountRepository repository) { super(repository); }
}
