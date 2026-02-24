package com.skishop.service;

import com.skishop.model.entity.SecurityLog;
import com.skishop.repository.SecurityLogRepository;
import org.springframework.stereotype.Service;

@Service
public class SecurityLogService extends BaseService<SecurityLog, String> {
    public SecurityLogService(SecurityLogRepository repository) { super(repository); }
}
