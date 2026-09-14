package com.hariharnath.temple.dto;

import com.hariharnath.temple.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private boolean success;
    private String message;
    private String requestId;
    private BookingStatus status;
    private String service;
    private LocalDate bookingDate;
    private String name;
    private String mobile;
    private String email;
    private Integer devotees;
    private String notes;
    private LocalDateTime createdAt;
}
