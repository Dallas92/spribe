package com.example.spribe.model.api;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateBookingRequest {
    private Long unitId;
    private Long userId;
    private LocalDate dateFrom;
    private LocalDate dateTo;
}
