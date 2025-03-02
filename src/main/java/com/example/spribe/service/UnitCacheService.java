package com.example.spribe.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.SetUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UnitCacheService {

    public static final String AVAILABLE_UNIT_IDS_KEY = "unit_ids";
    private final RedisTemplate<String, Set<Long>> redisTemplate;

    public Set<Long> getAvailableUnitIds() {
        return new HashSet<>(SetUtils.emptyIfNull(redisTemplate.opsForValue().get(AVAILABLE_UNIT_IDS_KEY)));
    }

    public void setAvailableUnitIds(Set<Long> ids) {
        redisTemplate.opsForValue().set(AVAILABLE_UNIT_IDS_KEY, ids);
    }

    public void clearCache() {
        redisTemplate.delete(AVAILABLE_UNIT_IDS_KEY);
    }

    public void updateCache(boolean add, Long unitIdToProcess) {
        Set<Long> unitIds = getAvailableUnitIds();
        if (add) {
            unitIds.add(unitIdToProcess);
        } else {
            unitIds.remove(unitIdToProcess);
        }
        setAvailableUnitIds(unitIds);
    }

    public void updateCache(boolean add, Set<Long> unitIdsToProcess) {
        Set<Long> unitIds = getAvailableUnitIds();
        if (add) {
            unitIds.addAll(unitIdsToProcess);
        } else {
            unitIds.removeAll(unitIdsToProcess);
        }
        setAvailableUnitIds(unitIds);
    }
}
