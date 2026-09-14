package com.hariharnath.temple.repository;

import com.hariharnath.temple.entity.AartiSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AartiScheduleRepository extends JpaRepository<AartiSchedule, Long> {
    List<AartiSchedule> findByActiveTrueOrderByDisplayOrderAsc();
    List<AartiSchedule> findAllByOrderByDisplayOrderAsc();
}
