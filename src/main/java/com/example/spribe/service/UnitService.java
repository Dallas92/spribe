package com.example.spribe.service;

import com.example.spribe.mapper.UnitMapper;
import com.example.spribe.model.api.CreateUnitRequest;
import com.example.spribe.model.api.UnitDto;
import com.example.spribe.model.entity.UnitEntity;
import com.example.spribe.model.enums.AccommodationType;
import com.example.spribe.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnitService {

    private static final BigDecimal SYSTEM_MARKUP = BigDecimal.valueOf(0.15);

    private final UnitRepository unitRepository;
    private final UnitCacheService unitCacheService;
    private final UnitMapper unitMapper;

    private static Sort generateSort(String sortParam) {
        Sort sort = Sort.unsorted();
        if (sortParam != null && !sortParam.isEmpty()) {
            String[] parts = sortParam.split(",");
            String property = parts[0].trim(); // Extract property name
            Sort.Direction direction = (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;

            sort = Sort.by(direction, property);
        }
        return sort;
    }

    public Long create(CreateUnitRequest request) {
        UnitEntity unit = new UnitEntity();
        unit.setNumberOfRooms(request.getNumberOfRooms());
        unit.setAccommodationType(request.getAccommodationType());
        unit.setFloor(request.getFloor());
        unit.setDescription(request.getDescription());
        unit.setCost(request.getCost().add(request.getCost().multiply(SYSTEM_MARKUP)));
        unitRepository.save(unit);

        log.info("{} created", unit);

        unitCacheService.updateCache(true, unit.getId());

        return unit.getId();
    }

    public Page<UnitDto> search(
            AccommodationType accommodationType,
            Integer minNumberOfRooms,
            Integer maxNumberOfRooms,
            Integer minFloor,
            Integer maxFloor,
            Integer minCost,
            Integer maxCost,
            LocalDate dateFrom,
            LocalDate dateTo,
            Integer pageNumber,
            Integer pageSize,
            String sortParam) {
        var units = unitRepository.search(
                accommodationType != null ? accommodationType.toString() : null,
                minNumberOfRooms,
                maxNumberOfRooms,
                minFloor,
                maxFloor,
                minCost,
                maxCost,
                dateFrom,
                dateTo,
                PageRequest.of(pageNumber, pageSize, generateSort(sortParam)));
        return units.map(unitMapper::toDto);
    }

    public Set<Long> getStats() {
        return unitCacheService.getAvailableUnitIds();
    }

    public void loadAvailableUnitsToCache() {
        var availableUnits = unitRepository.search(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Pageable.ofSize(Integer.MAX_VALUE)
        );

        unitCacheService.clearCache();

        var unitIds = availableUnits.stream().map(UnitEntity::getId).collect(Collectors.toSet());
        unitCacheService.updateCache(true, unitIds);
    }
}
