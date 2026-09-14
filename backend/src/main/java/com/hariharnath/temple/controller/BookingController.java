package com.hariharnath.temple.controller;

import com.hariharnath.temple.dto.BookingRequest;
import com.hariharnath.temple.dto.BookingResponse;
import com.hariharnath.temple.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Public Bookings", description = "Public endpoints for Darshan & Puja bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Submit a new darshan or puja booking request")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{requestId}")
    @Operation(summary = "Get booking details by Request ID (e.g. BHT-2026-000001)")
    public ResponseEntity<BookingResponse> getBookingByRequestId(@PathVariable String requestId) {
        BookingResponse response = bookingService.getBookingByRequestId(requestId);
        return ResponseEntity.ok(response);
    }
}
