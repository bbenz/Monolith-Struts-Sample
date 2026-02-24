package com.skishop.service;

import com.skishop.model.entity.ReturnRequest;
import com.skishop.repository.ReturnRequestRepository;
import org.springframework.stereotype.Service;

@Service
public class ReturnRequestService extends BaseService<ReturnRequest, String> {
    public ReturnRequestService(ReturnRequestRepository repository) { super(repository); }
}
