package com.example.spribe.service;

import com.example.spribe.model.api.CreateUnitRequest;
import com.example.spribe.model.api.UnitDto;
import com.example.spribe.model.entity.BookingEntity;
import com.example.spribe.model.entity.UnitEntity;
import com.example.spribe.model.enums.AccommodationType;
import com.example.spribe.model.enums.BookingStatus;
import com.example.spribe.repository.BookingRepository;
import com.example.spribe.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class UnitServiceTest {

    @Autowired
    private UnitService unitService;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    void beforeEach() {
        unitRepository.deleteAll();
    }

    @Test
    void create_unit() {
        CreateUnitRequest request = new CreateUnitRequest();
        request.setFloor(1);
        request.setNumberOfRooms(2);
        request.setCost(BigDecimal.valueOf(20));
        request.setDescription("Test apartment");
        request.setAccommodationType(AccommodationType.APARTMENTS);

        Long unitId = unitService.create(request);

        assertNotNull(unitId);
        assertTrue(unitRepository.existsById(unitId));

        UnitEntity savedUnit = unitRepository.findById(unitId).orElseThrow();
        assertEquals(request.getFloor(), savedUnit.getFloor());
        assertEquals(request.getNumberOfRooms(), savedUnit.getNumberOfRooms());
        assertEquals(request.getCost().add(request.getCost().multiply(BigDecimal.valueOf(0.15))), savedUnit.getCost());
        assertEquals(request.getDescription(), savedUnit.getDescription());
        assertEquals(request.getAccommodationType(), savedUnit.getAccommodationType());
    }

    @Test
    void search_units_when_no_bookings() {
        Random random = new Random();
        for (int i = 0; i < 90; i++) {
            UnitEntity unit = new UnitEntity();
            unit.setNumberOfRooms(random.nextInt(1, 10));
            unit.setAccommodationType(Arrays.stream(AccommodationType.values()).toList().get(random.nextInt(0, 2)));
            unit.setFloor(random.nextInt(1, 20));
            unit.setDescription("Description №%s".formatted(i + 1));
            unit.setCost(BigDecimal.valueOf(random.nextInt(100, 2000)));
            unitRepository.save(unit);
        }

        Page<UnitDto> result = unitService.search(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 1),
                0,
                1000,
                "cost,asc"
        );

        assertNotNull(result);
        assertEquals(90, result.getContent().size());
    }

    @ParameterizedTest
    @CsvSource({
            "ACTIVE,0",
            "CANCELLED, 10",
            "PAID, 0"
    })
    void search_units_when_with_bookings(String status, Integer expectedSize) {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            UnitEntity unit = new UnitEntity();
            unit.setNumberOfRooms(random.nextInt(1, 10));
            unit.setAccommodationType(Arrays.stream(AccommodationType.values()).toList().get(random.nextInt(0, 2)));
            unit.setFloor(random.nextInt(1, 20));
            unit.setDescription("Description №%s".formatted(i + 1));
            unit.setCost(BigDecimal.valueOf(random.nextInt(100, 2000)));
            unitRepository.save(unit);

            BookingEntity booking = new BookingEntity();
            booking.setUnit(unit);
            booking.setUserId(1L);
            booking.setStatus(BookingStatus.valueOf(status));
            booking.setDateFrom(LocalDate.of(2025, 2, 1));
            booking.setDateTo(LocalDate.of(2025, 10, 1));
            booking.setAmount(unit.getCost());
            bookingRepository.save(booking);
        }

        Page<UnitDto> result = unitService.search(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 1),
                0,
                1000,
                "cost,asc"
        );

        assertNotNull(result);
        assertEquals(expectedSize, result.getContent().size());
    }
}
