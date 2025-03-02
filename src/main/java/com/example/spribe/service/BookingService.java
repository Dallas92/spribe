package com.example.spribe.service;

import com.example.spribe.exception.ConflictException;
import com.example.spribe.exception.NotFoundException;
import com.example.spribe.exception.OperationForbiddenException;
import com.example.spribe.model.api.CancelBookingRequest;
import com.example.spribe.model.api.CreateBookingRequest;
import com.example.spribe.model.entity.BookingEntity;
import com.example.spribe.model.entity.UnitEntity;
import com.example.spribe.model.enums.BookingStatus;
import com.example.spribe.repository.BookingRepository;
import com.example.spribe.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {

    private static final Long THRESHOLD_MINUTES = 15L;

    private final UnitRepository unitRepository;
    private final BookingRepository bookingRepository;
    private final UnitCacheService unitCacheService;

    public Long create(CreateBookingRequest request) {
        UnitEntity unit = unitRepository.findById(request.getUnitId()).orElseThrow(() -> new NotFoundException("unit not found"));

        boolean hasBookingForUnit = bookingRepository.hasBookingForUnit(request.getUnitId(), request.getDateFrom(), request.getDateTo());
        if (hasBookingForUnit) {
            throw new ConflictException("unit can't be booked, please select other dates");
        }

        BookingEntity booking = new BookingEntity();
        booking.setUnit(unit);
        booking.setUserId(request.getUserId());
        booking.setDateFrom(request.getDateFrom());
        booking.setDateTo(request.getDateTo());
        booking.setAmount(unit.getCost());
        booking.setStatus(BookingStatus.ACTIVE);
        bookingRepository.save(booking);

        log.info("%s created");

        unitCacheService.updateCache(false, unit.getId());

        return booking.getId();
    }

    public void cancel(Long id, CancelBookingRequest request) {
        BookingEntity booking = bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("booking not found"));

        if (!booking.getUserId().equals(request.getUserId())) {
            throw new OperationForbiddenException("user not allowed to perform this operation");
        }

        if (!booking.getStatus().equals(BookingStatus.ACTIVE)) {
            throw new ConflictException("can't cancel not active booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        log.info("booking (id={}) was cancelled by user (id={})", booking.getId(), booking.getUserId());

        unitCacheService.updateCache(true, booking.getUnit().getId());
    }

    public void cancelExpired() {
        LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(THRESHOLD_MINUTES);

        List<BookingEntity> expiredBookings = bookingRepository.findExpiredBookings(thresholdTime);
        if (!expiredBookings.isEmpty()) {
            expiredBookings.forEach(booking -> {

                booking.setStatus(BookingStatus.CANCELLED);
            });
            bookingRepository.saveAll(expiredBookings);
            log.info("Cancelled {} expired booking(s)", expiredBookings.size());

            unitCacheService.updateCache(true, expiredBookings.stream().map(b -> b.getUnit().getId()).collect(Collectors.toSet()));
        }
    }
}
