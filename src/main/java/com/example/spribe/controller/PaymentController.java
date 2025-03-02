package com.example.spribe.controller;

import com.example.spribe.model.api.CreatePaymentRequest;
import com.example.spribe.model.api.CreatePaymentResponse;
import com.example.spribe.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<CreatePaymentResponse> create(@RequestBody CreatePaymentRequest request) {
        var id = paymentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatePaymentResponse(id));
    }
}
