package com.skishop.service;

import com.skishop.model.entity.EmailQueue;
import com.skishop.repository.EmailQueueRepository;
import org.springframework.stereotype.Service;

@Service
public class EmailQueueService extends BaseService<EmailQueue, String> {
    public EmailQueueService(EmailQueueRepository repository) { super(repository); }
}
