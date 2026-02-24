package com.skishop.service;

import com.skishop.model.entity.Inventory;
import com.skishop.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService extends BaseService<Inventory, String> {
    public InventoryService(InventoryRepository repository) { super(repository); }
}
