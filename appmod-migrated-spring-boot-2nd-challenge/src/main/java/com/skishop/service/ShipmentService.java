package com.skishop.service;

import com.skishop.model.entity.Shipment;
import com.skishop.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService extends BaseService<Shipment, String> {
    public ShipmentService(ShipmentRepository repository) { super(repository); }
}
