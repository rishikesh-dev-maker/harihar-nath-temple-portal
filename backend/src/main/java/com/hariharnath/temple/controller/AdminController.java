package com.hariharnath.temple.controller;

import com.hariharnath.temple.dto.*;
import com.hariharnath.temple.entity.BookingStatus;
import com.hariharnath.temple.entity.ContactStatus;
import com.hariharnath.temple.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
@Tag(name = "Admin Management", description = "Protected administrative operations for temple portal management")
public class AdminController {

    private final BookingService bookingService;
    private final ContactService contactService;
    private final AnnouncementService announcementService;
    private final TempleServiceService templeServiceService;
    private final AartiScheduleService aartiScheduleService;
    private final GalleryService galleryService;
    private final AdminDashboardService adminDashboardService;

    // --- DASHBOARD STATS ---
    @GetMapping("/dashboard/stats")
    @Operation(summary = "Get aggregate statistics for the admin dashboard")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        return ResponseEntity.ok(adminDashboardService.getDashboardStats());
    }

    // --- BOOKINGS MANAGEMENT ---
    @GetMapping("/bookings")
    @Operation(summary = "Get paginated bookings with optional status, date, and search filter")
    public ResponseEntity<Page<BookingAdminDto>> getBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String search) {

        return ResponseEntity.ok(bookingService.getAllBookings(page, size, status, date, search));
    }

    @PutMapping("/bookings/{id}")
    @Operation(summary = "Update booking status (PENDING, CONFIRMED, CANCELLED, COMPLETED)")
    public ResponseEntity<BookingAdminDto> updateBookingStatus(
            @PathVariable Long id,
            @Valid @RequestBody BookingStatusUpdateRequest request) {

        return ResponseEntity.ok(bookingService.updateBookingStatus(id, request.getStatus()));
    }

    @DeleteMapping("/bookings/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete booking (Admin only)")
    public ResponseEntity<ApiResponse<Void>> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok(ApiResponse.success("Booking deleted successfully"));
    }

    // --- CONTACT MESSAGES MANAGEMENT ---
    @GetMapping("/contact")
    @Operation(summary = "Get all contact messages with pagination and status filtering")
    public ResponseEntity<Page<ContactMessageResponse>> getContactMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) ContactStatus status) {

        return ResponseEntity.ok(contactService.getAllMessages(page, size, status));
    }

    @PutMapping("/contact/{id}/status")
    @Operation(summary = "Update contact message status (NEW, READ, REPLIED, ARCHIVED)")
    public ResponseEntity<ContactMessageResponse> updateContactStatus(
            @PathVariable Long id,
            @RequestParam ContactStatus status) {

        return ResponseEntity.ok(contactService.updateMessageStatus(id, status));
    }

    @DeleteMapping("/contact/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete contact message (Admin only)")
    public ResponseEntity<ApiResponse<Void>> deleteContactMessage(@PathVariable Long id) {
        contactService.deleteMessage(id);
        return ResponseEntity.ok(ApiResponse.success("Contact message deleted successfully"));
    }

    // --- ANNOUNCEMENTS MANAGEMENT ---
    @GetMapping("/announcements")
    @Operation(summary = "Get all announcements (active and inactive)")
    public ResponseEntity<List<AnnouncementDto>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }

    @PostMapping("/announcements")
    @Operation(summary = "Create a new announcement")
    public ResponseEntity<AnnouncementDto> createAnnouncement(@Valid @RequestBody AnnouncementDto dto) {
        return new ResponseEntity<>(announcementService.createAnnouncement(dto), HttpStatus.CREATED);
    }

    @PutMapping("/announcements/{id}")
    @Operation(summary = "Update announcement details")
    public ResponseEntity<AnnouncementDto> updateAnnouncement(
            @PathVariable Long id,
            @Valid @RequestBody AnnouncementDto dto) {

        return ResponseEntity.ok(announcementService.updateAnnouncement(id, dto));
    }

    @DeleteMapping("/announcements/{id}")
    @Operation(summary = "Delete announcement")
    public ResponseEntity<ApiResponse<Void>> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok(ApiResponse.success("Announcement deleted successfully"));
    }

    // --- TEMPLE SERVICES MANAGEMENT ---
    @PostMapping("/services")
    @Operation(summary = "Add a new temple service")
    public ResponseEntity<TempleServiceDto> createService(@Valid @RequestBody TempleServiceDto dto) {
        return new ResponseEntity<>(templeServiceService.createService(dto), HttpStatus.CREATED);
    }

    @PutMapping("/services/{id}")
    @Operation(summary = "Update a temple service")
    public ResponseEntity<TempleServiceDto> updateService(
            @PathVariable Long id,
            @Valid @RequestBody TempleServiceDto dto) {

        return ResponseEntity.ok(templeServiceService.updateService(id, dto));
    }

    @DeleteMapping("/services/{id}")
    @Operation(summary = "Delete a temple service")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long id) {
        templeServiceService.deleteService(id);
        return ResponseEntity.ok(ApiResponse.success("Service deleted successfully"));
    }

    // --- AARTI SCHEDULE MANAGEMENT ---
    @PostMapping("/aarti")
    @Operation(summary = "Add a new Aarti timing/schedule")
    public ResponseEntity<AartiScheduleDto> createAarti(@Valid @RequestBody AartiScheduleDto dto) {
        return new ResponseEntity<>(aartiScheduleService.createAartiSchedule(dto), HttpStatus.CREATED);
    }

    @PutMapping("/aarti/{id}")
    @Operation(summary = "Update Aarti timing/schedule")
    public ResponseEntity<AartiScheduleDto> updateAarti(
            @PathVariable Long id,
            @Valid @RequestBody AartiScheduleDto dto) {

        return ResponseEntity.ok(aartiScheduleService.updateAartiSchedule(id, dto));
    }

    @DeleteMapping("/aarti/{id}")
    @Operation(summary = "Delete Aarti schedule")
    public ResponseEntity<ApiResponse<Void>> deleteAarti(@PathVariable Long id) {
        aartiScheduleService.deleteAartiSchedule(id);
        return ResponseEntity.ok(ApiResponse.success("Aarti schedule deleted successfully"));
    }

    // --- GALLERY MANAGEMENT ---
    @PostMapping("/gallery")
    @Operation(summary = "Add a new gallery photo item")
    public ResponseEntity<GalleryItemDto> createGalleryItem(@Valid @RequestBody GalleryItemDto dto) {
        return new ResponseEntity<>(galleryService.createGalleryItem(dto), HttpStatus.CREATED);
    }

    @PutMapping("/gallery/{id}")
    @Operation(summary = "Update gallery photo item")
    public ResponseEntity<GalleryItemDto> updateGalleryItem(
            @PathVariable Long id,
            @Valid @RequestBody GalleryItemDto dto) {

        return ResponseEntity.ok(galleryService.updateGalleryItem(id, dto));
    }

    @DeleteMapping("/gallery/{id}")
    @Operation(summary = "Delete gallery item")
    public ResponseEntity<ApiResponse<Void>> deleteGalleryItem(@PathVariable Long id) {
        galleryService.deleteGalleryItem(id);
        return ResponseEntity.ok(ApiResponse.success("Gallery item deleted successfully"));
    }

    // --- USER MANAGEMENT ---
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get list of staff & admin users (Admin only)")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(adminDashboardService.getAllUsers());
    }

    @PutMapping("/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Enable or disable user account (Admin only)")
    public ResponseEntity<ApiResponse<Void>> toggleUserStatus(
            @PathVariable Long id,
            @RequestParam boolean enabled) {

        adminDashboardService.toggleUserStatus(id, enabled);
        return ResponseEntity.ok(ApiResponse.success("User status updated successfully"));
    }
}
