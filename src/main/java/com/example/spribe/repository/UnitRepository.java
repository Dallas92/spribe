package com.example.spribe.repository;

import com.example.spribe.model.enums.AccommodationType;
import com.example.spribe.model.entity.UnitEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UnitRepository extends JpaRepository<UnitEntity, Long> {

    String SEARCH_SELECTOR = "SELECT * ";

    String SEARCH_COUNTER = "SELECT COUNT(id) ";

    String SEARCH_QUERY = """
            FROM units u
            WHERE (:accommodationType IS NULL OR u.accommodation_type = :accommodationType)
                AND (:minNumberOfRooms IS NULL OR u.number_of_rooms >= :minNumberOfRooms)
                AND (:maxNumberOfRooms IS NULL OR u.number_of_rooms <= :maxNumberOfRooms)
                AND (:minFloor IS NULL OR u.floor >= :minFloor)
                AND (:maxFloor IS NULL OR u.floor <= :maxFloor)
                AND (:minCost IS NULL OR u.cost >= :minCost)
                AND (:maxCost IS NULL OR u.cost <= :maxCost)
                AND NOT EXISTS (SELECT 1
                                FROM bookings b
                                WHERE b.unit_id = u.id
                                AND b.status IN ('ACTIVE', 'PAID')
                                AND b.date_to <= :dateTo AND b.date_from >= :dateFrom)
            """;

    @Query(value = SEARCH_SELECTOR + SEARCH_QUERY,
            countQuery = SEARCH_COUNTER + SEARCH_QUERY,
            nativeQuery = true)
    Page<UnitEntity> search(
            @Param("accommodationType") String accommodationType,
            @Param("minNumberOfRooms") Integer minNumberOfRooms,
            @Param("maxNumberOfRooms") Integer maxNumberOfRooms,
            @Param("minFloor") Integer minFloor,
            @Param("maxFloor") Integer maxFloor,
            @Param("minCost") Integer minCost,
            @Param("maxCost") Integer maxCost,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            Pageable pageable);
}
