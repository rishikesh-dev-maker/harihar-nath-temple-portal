package com.hariharnath.temple.service;

import com.hariharnath.temple.dto.DashboardStatsResponse;
import com.hariharnath.temple.dto.UserDto;
import com.hariharnath.temple.entity.BookingStatus;
import com.hariharnath.temple.entity.ContactStatus;
import com.hariharnath.temple.entity.User;
import com.hariharnath.temple.exception.ResourceNotFoundException;
import com.hariharnath.temple.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final BookingRepository bookingRepository;
    private final ContactMessageRepository contactMessageRepository;
    private final TempleServiceRepository templeServiceRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        long totalBookings = bookingRepository.count();
        long pendingBookings = bookingRepository.countByStatus(BookingStatus.PENDING);
        long confirmedBookings = bookingRepository.countByStatus(BookingStatus.CONFIRMED);
        long cancelledBookings = bookingRepository.countByStatus(BookingStatus.CANCELLED);
        long completedBookings = bookingRepository.countByStatus(BookingStatus.COMPLETED);
        long todayBookings = bookingRepository.countByBookingDate(LocalDate.now());

        long totalContact = contactMessageRepository.count();
        long unreadContact = contactMessageRepository.countByStatus(ContactStatus.NEW);

        long activeServices = templeServiceRepository.findByActiveTrue().size();
        long activeAnnouncements = announcementRepository.findActiveAnnouncements(LocalDate.now()).size();

        return DashboardStatsResponse.builder()
                .totalBookings(totalBookings)
                .pendingBookings(pendingBookings)
                .confirmedBookings(confirmedBookings)
                .cancelledBookings(cancelledBookings)
                .completedBookings(completedBookings)
                .todayBookings(todayBookings)
                .totalContactMessages(totalContact)
                .unreadContactMessages(unreadContact)
                .activeServices(activeServices)
                .activeAnnouncements(activeAnnouncements)
                .build();
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .enabled(user.isEnabled())
                        .createdAt(user.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void toggleUserStatus(Long id, boolean enabled) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setEnabled(enabled);
        userRepository.save(user);
    }
}
