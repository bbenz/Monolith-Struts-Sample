package com.skishop.controller;

import com.skishop.exception.ResourceNotFoundException;
import com.skishop.model.entity.ReturnRequest;
import com.skishop.service.ReturnRequestService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/returns")
public class ReturnRequestController {
    private final ReturnRequestService returnRequestService;

    public ReturnRequestController(ReturnRequestService returnRequestService) {
        this.returnRequestService = returnRequestService;
    }

    @PostMapping
    public ResponseEntity<ReturnRequest> create(@RequestParam @NotBlank String orderId,
                                                @RequestParam @NotBlank String orderItemId,
                                                @RequestParam @NotNull Integer quantity,
                                                @RequestParam @NotNull BigDecimal refundAmount) {
        ReturnRequest rr = new ReturnRequest();
        rr.setId(UUID.randomUUID().toString());
        rr.setOrderId(orderId);
        rr.setOrderItemId(orderItemId);
        rr.setQuantity(quantity);
        rr.setRefundAmount(refundAmount);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnRequestService.save(rr));
    }

    @GetMapping("/{id}")
    public ReturnRequest get(@PathVariable String id) {
        return returnRequestService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Return not found: " + id));
    }
}
