package com.example.spribe.task;

import com.example.spribe.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupRunnerTask implements ApplicationRunner {

    private final UnitService unitService;

    @Override
    public void run(ApplicationArguments args) {
        unitService.loadAvailableUnitsToCache();
    }
}
