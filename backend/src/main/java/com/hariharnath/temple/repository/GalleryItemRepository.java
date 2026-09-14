package com.hariharnath.temple.repository;

import com.hariharnath.temple.entity.GalleryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryItemRepository extends JpaRepository<GalleryItem, Long> {
    List<GalleryItem> findByActiveTrueOrderByDisplayOrderAscCreatedAtDesc();
    List<GalleryItem> findByCategoryAndActiveTrueOrderByDisplayOrderAsc(String category);
    List<GalleryItem> findAllByOrderByDisplayOrderAscCreatedAtDesc();
}
