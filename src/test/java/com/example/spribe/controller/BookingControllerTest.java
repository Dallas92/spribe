package com.example.spribe.controller;

import com.example.spribe.model.api.CancelBookingRequest;
import com.example.spribe.model.api.CreateBookingRequest;
import com.example.spribe.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @BeforeEach
    void beforeEach() {
        reset(bookingService);
    }

    @Test
    void createBooking_Success() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest();
        request.setUnitId(1L);
        request.setUserId(10L);
        request.setDateFrom(LocalDate.now());
        request.setDateTo(LocalDate.now().plusDays(1));

        when(bookingService.create(any())).thenReturn(1L);

        // Act & Assert
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void cancelBooking_Success() throws Exception {
        CancelBookingRequest request = new CancelBookingRequest();
        request.setUserId(10L);

        doNothing().when(bookingService).cancel(any(), any());

        // Act & Assert
        mockMvc.perform(post("/api/bookings/{id}/cancel", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

}
