package com.hariharnath.temple.repository;

import com.hariharnath.temple.entity.TempleService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TempleServiceRepository extends JpaRepository<TempleService, Long> {
    List<TempleService> findByActiveTrue();
}
