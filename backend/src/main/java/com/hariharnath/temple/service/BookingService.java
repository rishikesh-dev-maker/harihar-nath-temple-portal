package com.hariharnath.temple.service;

import com.hariharnath.temple.dto.*;
import com.hariharnath.temple.entity.Booking;
import com.hariharnath.temple.entity.BookingStatus;
import com.hariharnath.temple.exception.ResourceNotFoundException;
import com.hariharnath.temple.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EmailService emailService;

    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        String generatedRequestId = generateUniqueRequestId();

        Booking booking = Booking.builder()
                .requestId(generatedRequestId)
                .service(request.getService())
                .bookingDate(request.getBookingDate())
                .name(request.getName())
                .mobile(request.getMobile())
                .email(request.getEmail())
                .devotees(request.getDevotees())
                .notes(request.getNotes())
                .status(BookingStatus.PENDING)
                .build();

        Booking saved = bookingRepository.save(booking);
        log.info("Created new darshan booking with Request ID: {}", saved.getRequestId());

        // Asynchronously dispatch emails
        emailService.sendBookingReceivedEmails(saved);

        return BookingResponse.builder()
                .success(true)
                .message("Booking request received successfully")
                .requestId(saved.getRequestId())
                .status(saved.getStatus())
                .service(saved.getService())
                .bookingDate(saved.getBookingDate())
                .name(saved.getName())
                .mobile(saved.getMobile())
                .email(saved.getEmail())
                .devotees(saved.getDevotees())
                .notes(saved.getNotes())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingByRequestId(String requestId) {
        Booking booking = bookingRepository.findByRequestId(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with Request ID: " + requestId));

        return BookingResponse.builder()
                .success(true)
                .message("Booking details retrieved successfully")
                .requestId(booking.getRequestId())
                .status(booking.getStatus())
                .service(booking.getService())
                .bookingDate(booking.getBookingDate())
                .name(booking.getName())
                .mobile(booking.getMobile())
                .email(booking.getEmail())
                .devotees(booking.getDevotees())
                .notes(booking.getNotes())
                .createdAt(booking.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<BookingAdminDto> getAllBookings(int page, int size, BookingStatus status, LocalDate date, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Booking> bookings = bookingRepository.findFilteredBookings(status, date, search, pageable);
        return bookings.map(this::mapToAdminDto);
    }

    @Transactional
    public BookingAdminDto updateBookingStatus(Long id, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        booking.setStatus(newStatus);
        Booking updated = bookingRepository.save(booking);
        log.info("Booking id {} (Request ID: {}) updated to status {}", id, updated.getRequestId(), newStatus);

        emailService.sendBookingStatusUpdateEmail(updated, newStatus);
        return mapToAdminDto(updated);
    }

    @Transactional
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
        log.info("Booking with id {} deleted by administrator", id);
    }

    private synchronized String generateUniqueRequestId() {
        Long maxId = bookingRepository.findMaxId();
        long nextId = (maxId == null ? 0L : maxId) + 1L;
        int currentYear = LocalDate.now().getYear();
        return String.format("BHT-%d-%06d", currentYear, nextId);
    }

    private BookingAdminDto mapToAdminDto(Booking booking) {
        return BookingAdminDto.builder()
                .id(booking.getId())
                .requestId(booking.getRequestId())
                .service(booking.getService())
                .bookingDate(booking.getBookingDate())
                .name(booking.getName())
                .mobile(booking.getMobile())
                .email(booking.getEmail())
                .devotees(booking.getDevotees())
                .notes(booking.getNotes())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}
