package com.example.spribe.controller;

import com.example.spribe.model.api.CancelBookingRequest;
import com.example.spribe.model.api.CreateBookingRequest;
import com.example.spribe.model.api.CreateBookingResponse;
import com.example.spribe.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<CreateBookingResponse> create(@RequestBody CreateBookingRequest request) {
        var id = bookingService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateBookingResponse(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity cancel(@PathVariable Long id, @RequestBody CancelBookingRequest request) {
        bookingService.cancel(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
