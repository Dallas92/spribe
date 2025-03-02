package com.example.spribe.model.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class GetUnitStatsResponse {
    private Set<Long> ids;
}
