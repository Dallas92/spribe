package com.example.spribe.controller;

import com.example.spribe.model.api.CreateUnitRequest;
import com.example.spribe.model.api.UnitDto;
import com.example.spribe.model.enums.AccommodationType;
import com.example.spribe.service.UnitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UnitController.class)
class UnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UnitService unitService;

    @Test
    void createUnit_Success() throws Exception {
        // Arrange
        CreateUnitRequest request = new CreateUnitRequest();
        request.setFloor(1);
        request.setNumberOfRooms(2);
        request.setCost(BigDecimal.valueOf(20));
        request.setDescription("Some description");
        request.setAccommodationType(AccommodationType.APARTMENTS);

        when(unitService.create(any())).thenReturn(1L);

        // Act & Assert
        mockMvc.perform(post("/api/units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAllUnits_Success() throws Exception {
        UnitDto unit1 = new UnitDto();
        unit1.setId(1L);
        unit1.setNumberOfRooms(5);
        unit1.setAccommodationType(AccommodationType.APARTMENTS);
        unit1.setCost(BigDecimal.valueOf(200));
        unit1.setDescription("description 1");

        UnitDto unit2 = new UnitDto();
        unit2.setId(2L);
        unit2.setNumberOfRooms(3);
        unit2.setAccommodationType(AccommodationType.FLAT);
        unit2.setCost(BigDecimal.valueOf(100));
        unit2.setDescription("description 2");

        List<UnitDto> units = Arrays.asList(unit1, unit2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "cost"));
        Page<UnitDto> pageResult = new PageImpl<>(units, pageable, units.size());
        when(unitService.search(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(pageResult);

        // Act & Assert
        mockMvc.perform(get("/api/units")
                        .param("dateFrom", LocalDate.of(2025,1,1).toString())
                        .param("dateTo", LocalDate.of(2025,12,1).toString())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "cost,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                // First item
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].numberOfRooms").value(5))
                .andExpect(jsonPath("$.content[0].accommodationType").value("APARTMENTS"))
                .andExpect(jsonPath("$.content[0].floor").doesNotExist())
                .andExpect(jsonPath("$.content[0].cost").value(200))
                .andExpect(jsonPath("$.content[0].description").value("description 1"))
                // Second item
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].numberOfRooms").value(3))
                .andExpect(jsonPath("$.content[1].accommodationType").value("FLAT"))
                .andExpect(jsonPath("$.content[1].floor").doesNotExist())
                .andExpect(jsonPath("$.content[1].cost").value(100))
                .andExpect(jsonPath("$.content[1].description").value("description 2"))
                // Pagination
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10))
                .andExpect(jsonPath("$.pageable.sort.empty").value(false))
                .andExpect(jsonPath("$.pageable.offset").value(0))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andExpect(jsonPath("$.empty").value(false));
    }
}
