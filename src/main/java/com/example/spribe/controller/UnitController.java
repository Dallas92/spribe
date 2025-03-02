package com.example.spribe.controller;

import com.example.spribe.model.api.CreateUnitRequest;
import com.example.spribe.model.api.CreateUnitResponse;
import com.example.spribe.model.api.GetUnitStatsResponse;
import com.example.spribe.model.api.UnitDto;
import com.example.spribe.model.enums.AccommodationType;
import com.example.spribe.service.UnitService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/api/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;

    @PostMapping
    public ResponseEntity<CreateUnitResponse> createUnit(@RequestBody CreateUnitRequest request) {
        var id = unitService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateUnitResponse(id));
    }

    @GetMapping
    public ResponseEntity<Page<UnitDto>> getUnits(
            @RequestParam(required = false) AccommodationType accommodationType,
            @RequestParam(required = false) Integer minNumberOfRooms,
            @RequestParam(required = false) Integer maxNumberOfRooms,
            @RequestParam(required = false) Integer minFloor,
            @RequestParam(required = false) Integer maxFloor,
            @RequestParam(required = false) Integer minCost,
            @RequestParam(required = false) Integer maxCost,
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo,
            @Parameter(example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(example = "cost,asc")
            @RequestParam(defaultValue = "cost,asc") String sort
    ) {
        var items = unitService.search(
                accommodationType,
                minNumberOfRooms,
                maxNumberOfRooms,
                minFloor,
                maxFloor,
                minCost,
                maxCost,
                dateFrom,
                dateTo,
                page,
                size,
                sort
        );
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

    @GetMapping("/stats")
    public ResponseEntity<GetUnitStatsResponse> getStats() {
        var items = unitService.getStats();
        return ResponseEntity.status(HttpStatus.OK).body(new GetUnitStatsResponse(items));
    }
}
