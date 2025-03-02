package com.example.spribe.task;

import com.example.spribe.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CancellationScheduledTask {

    private final BookingService bookingService;

    @Scheduled(fixedRate = 60_000)
    public void cancelExpiredBookings() {
        bookingService.cancelExpired();
    }
}
