package com.hariharnath.temple.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hariharnath.temple.dto.BookingRequest;
import com.hariharnath.temple.dto.BookingResponse;
import com.hariharnath.temple.entity.BookingStatus;
import com.hariharnath.temple.security.JwtTokenProvider;
import com.hariharnath.temple.service.BookingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Test
    @DisplayName("POST /api/v1/bookings - should create booking and return 201 Created")
    void testCreateBookingEndpoint() throws Exception {
        BookingRequest request = BookingRequest.builder()
                .service("Darshan")
                .bookingDate(LocalDate.now().plusDays(3))
                .name("Rishikesh Kumar")
                .mobile("9876543210")
                .email("rishikesh@example.com")
                .devotees(2)
                .notes("Special darshan")
                .build();

        BookingResponse mockResponse = BookingResponse.builder()
                .success(true)
                .message("Booking request received successfully")
                .requestId("BHT-2026-000001")
                .status(BookingStatus.PENDING)
                .service("Darshan")
                .bookingDate(request.getBookingDate())
                .name(request.getName())
                .mobile(request.getMobile())
                .email(request.getEmail())
                .devotees(2)
                .build();

        when(bookingService.createBooking(any(BookingRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.requestId").value("BHT-2026-000001"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("POST /api/v1/bookings - should fail validation with invalid mobile")
    void testCreateBookingValidationFailure() throws Exception {
        BookingRequest invalidRequest = BookingRequest.builder()
                .service("Darshan")
                .bookingDate(LocalDate.now().plusDays(3))
                .name("Rishikesh")
                .mobile("123") // Invalid Indian phone
                .email("invalid-email")
                .devotees(20) // Exceeds max 10
                .build();

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors.mobile").exists())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.devotees").exists());
    }

    @Test
    @DisplayName("GET /api/v1/bookings/{requestId} - should return booking details")
    void testGetBookingByRequestId() throws Exception {
        BookingResponse mockResponse = BookingResponse.builder()
                .success(true)
                .requestId("BHT-2026-000001")
                .status(BookingStatus.CONFIRMED)
                .service("Rudrabhishek")
                .name("Rishikesh Kumar")
                .build();

        when(bookingService.getBookingByRequestId("BHT-2026-000001")).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/bookings/BHT-2026-000001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value("BHT-2026-000001"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }
}
