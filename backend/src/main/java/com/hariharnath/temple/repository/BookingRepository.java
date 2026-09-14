package com.hariharnath.temple.repository;

import com.hariharnath.temple.entity.Booking;
import com.hariharnath.temple.entity.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByRequestId(String requestId);

    @Query("SELECT b FROM Booking b WHERE " +
           "(:status IS NULL OR b.status = :status) AND " +
           "(:date IS NULL OR b.bookingDate = :date) AND " +
           "(:search IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(b.requestId) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(b.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR b.mobile LIKE CONCAT('%', :search, '%'))")
    Page<Booking> findFilteredBookings(
            @Param("status") BookingStatus status,
            @Param("date") LocalDate date,
            @Param("search") String search,
            Pageable pageable
    );

    long countByStatus(BookingStatus status);

    long countByBookingDate(LocalDate date);

    @Query("SELECT MAX(b.id) FROM Booking b")
    Long findMaxId();
}
