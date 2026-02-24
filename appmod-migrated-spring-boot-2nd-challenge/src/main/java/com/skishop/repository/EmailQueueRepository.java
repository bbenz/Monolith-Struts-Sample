package com.skishop.repository;

import com.skishop.model.entity.EmailQueue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailQueueRepository extends JpaRepository<EmailQueue, String> {
    List<EmailQueue> findByStatus(String status);
}
