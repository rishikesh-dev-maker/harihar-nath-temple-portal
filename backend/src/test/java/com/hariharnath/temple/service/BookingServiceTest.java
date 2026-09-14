package com.hariharnath.temple.service;

import com.hariharnath.temple.dto.BookingRequest;
import com.hariharnath.temple.dto.BookingResponse;
import com.hariharnath.temple.entity.Booking;
import com.hariharnath.temple.entity.BookingStatus;
import com.hariharnath.temple.exception.ResourceNotFoundException;
import com.hariharnath.temple.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private BookingService bookingService;

    private Booking sampleBooking;
    private BookingRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleBooking = Booking.builder()
                .id(1L)
                .requestId("BHT-2026-000001")
                .service("Rudrabhishek")
                .bookingDate(LocalDate.now().plusDays(2))
                .name("Rishikesh Kumar")
                .mobile("9876543210")
                .email("rishikesh@example.com")
                .devotees(2)
                .notes("Family sankalp")
                .status(BookingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        sampleRequest = BookingRequest.builder()
                .service("Rudrabhishek")
                .bookingDate(LocalDate.now().plusDays(2))
                .name("Rishikesh Kumar")
                .mobile("9876543210")
                .email("rishikesh@example.com")
                .devotees(2)
                .notes("Family sankalp")
                .build();
    }

    @Test
    @DisplayName("Should create booking successfully with generated requestId and pending status")
    void testCreateBookingSuccess() {
        when(bookingRepository.findMaxId()).thenReturn(0L);
        when(bookingRepository.save(any(Booking.class))).thenReturn(sampleBooking);
        doNothing().when(emailService).sendBookingReceivedEmails(any(Booking.class));

        BookingResponse response = bookingService.createBooking(sampleRequest);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("BHT-2026-000001", response.getRequestId());
        assertEquals(BookingStatus.PENDING, response.getStatus());
        assertEquals("Rishikesh Kumar", response.getName());

        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(emailService, times(1)).sendBookingReceivedEmails(any(Booking.class));
    }

    @Test
    @DisplayName("Should retrieve booking by requestId")
    void testGetBookingByRequestIdSuccess() {
        when(bookingRepository.findByRequestId("BHT-2026-000001")).thenReturn(Optional.of(sampleBooking));

        BookingResponse response = bookingService.getBookingByRequestId("BHT-2026-000001");

        assertNotNull(response);
        assertEquals("BHT-2026-000001", response.getRequestId());
        assertEquals("Rudrabhishek", response.getService());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when requestId not found")
    void testGetBookingByRequestIdNotFound() {
        when(bookingRepository.findByRequestId("INVALID-ID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.getBookingByRequestId("INVALID-ID");
        });
    }

    @Test
    @DisplayName("Should update booking status and trigger notification")
    void testUpdateBookingStatus() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(sampleBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(sampleBooking);
        doNothing().when(emailService).sendBookingStatusUpdateEmail(any(Booking.class), eq(BookingStatus.CONFIRMED));

        var updated = bookingService.updateBookingStatus(1L, BookingStatus.CONFIRMED);

        assertNotNull(updated);
        verify(bookingRepository, times(1)).save(sampleBooking);
        verify(emailService, times(1)).sendBookingStatusUpdateEmail(any(Booking.class), eq(BookingStatus.CONFIRMED));
    }
}
