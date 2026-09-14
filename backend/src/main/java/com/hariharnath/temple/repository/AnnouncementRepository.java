package com.hariharnath.temple.repository;

import com.hariharnath.temple.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    @Query("SELECT a FROM Announcement a WHERE a.active = true AND " +
           "(a.startDate IS NULL OR a.startDate <= :currentDate) AND " +
           "(a.endDate IS NULL OR a.endDate >= :currentDate) " +
           "ORDER BY a.displayOrder ASC, a.createdAt DESC")
    List<Announcement> findActiveAnnouncements(LocalDate currentDate);

    List<Announcement> findAllByOrderByDisplayOrderAscCreatedAtDesc();
}
