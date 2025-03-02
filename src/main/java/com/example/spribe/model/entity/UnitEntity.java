package com.example.spribe.model.entity;

import com.example.spribe.model.enums.AccommodationType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "units")
@Getter
@Setter
@ToString
public class UnitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numberOfRooms;
    @Enumerated(EnumType.STRING)
    private AccommodationType accommodationType;
    private Integer floor;
    private BigDecimal cost;
    private String description;
    @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY)
    private List<BookingEntity> bookings = new ArrayList<>();
}
