package com.example.spribe.model.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentRequest {
    private Long bookingId;
    private Long userId;
}
