package com.example.spribe.task;

import com.example.spribe.model.api.CreateUnitRequest;
import com.example.spribe.model.enums.AccommodationType;
import com.example.spribe.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class StartupRunnerTask implements ApplicationRunner {

    private final UnitService unitService;

    @Override
    public void run(ApplicationArguments args) {
        unitService.loadAvailableUnitsToCache();

        Random random = new Random();
        for (int i = 0; i < 90; i++) {
            CreateUnitRequest createUnitRequest = new CreateUnitRequest();
            createUnitRequest.setFloor(random.nextInt(1, 20));
            createUnitRequest.setNumberOfRooms(random.nextInt(1, 10));
            createUnitRequest.setAccommodationType(Arrays.stream(AccommodationType.values()).toList().get(random.nextInt(0, 2)));
            createUnitRequest.setDescription("Description №%s".formatted(i + 1));
            createUnitRequest.setCost(BigDecimal.valueOf(random.nextInt(100, 2000)));
            unitService.create(createUnitRequest);
        }
    }
}
