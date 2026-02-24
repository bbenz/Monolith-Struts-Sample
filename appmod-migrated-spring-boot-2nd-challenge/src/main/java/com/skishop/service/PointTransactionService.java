package com.skishop.service;

import com.skishop.model.entity.PointTransaction;
import com.skishop.repository.PointTransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class PointTransactionService extends BaseService<PointTransaction, String> {
    public PointTransactionService(PointTransactionRepository repository) { super(repository); }
}
