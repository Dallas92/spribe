package com.example.spribe.mapper;

import com.example.spribe.model.api.UnitDto;
import com.example.spribe.model.entity.UnitEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UnitMapper {
    UnitDto toDto(UnitEntity source);
}
