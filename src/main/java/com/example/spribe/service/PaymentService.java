package com.example.spribe.service;

import com.example.spribe.exception.ConflictException;
import com.example.spribe.exception.NotFoundException;
import com.example.spribe.exception.OperationForbiddenException;
import com.example.spribe.model.api.CreatePaymentRequest;
import com.example.spribe.model.entity.BookingEntity;
import com.example.spribe.model.entity.PaymentEntity;
import com.example.spribe.model.entity.UnitEntity;
import com.example.spribe.model.enums.BookingStatus;
import com.example.spribe.repository.BookingRepository;
import com.example.spribe.repository.PaymentRepository;
import com.example.spribe.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final UnitRepository unitRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final UnitCacheService unitCacheService;

    @Transactional
    public Long create(CreatePaymentRequest request) {
        BookingEntity booking = bookingRepository.findById(request.getBookingId()).orElseThrow(() -> new NotFoundException("booking not found"));

        if (!booking.getUserId().equals(request.getUserId())) {
            throw new OperationForbiddenException("user not allowed to perform this operation");
        }

        if (!booking.getStatus().equals(BookingStatus.ACTIVE)) {
            throw new ConflictException("can't pay for not active booking");
        }

        booking.setStatus(BookingStatus.PAID);
        bookingRepository.save(booking);

        UnitEntity unit = unitRepository.findById(booking.getUnit().getId()).orElseThrow(() -> new NotFoundException("unit not found"));

        PaymentEntity payment = new PaymentEntity();
        payment.setUserId(request.getUserId());
        payment.setAmount(booking.getAmount());
        payment.setUnit(unit);
        payment.setBooking(booking);
        paymentRepository.save(payment);

        log.info("{} created for booking (id={})", payment, booking.getId());

        unitCacheService.updateCache(false, unit.getId());

        return payment.getId();
    }
}
