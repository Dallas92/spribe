package com.example.spribe.model.api;

import com.example.spribe.model.enums.AccommodationType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UnitDto {
    private Long id;
    private Integer numberOfRooms;
    private AccommodationType accommodationType;
    private Integer floor;
    private BigDecimal cost;
    private String description;
}
