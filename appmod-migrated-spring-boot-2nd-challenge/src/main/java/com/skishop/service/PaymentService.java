package com.skishop.service;

import com.skishop.model.entity.Payment;
import com.skishop.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService extends BaseService<Payment, String> {
    public PaymentService(PaymentRepository repository) { super(repository); }
}
