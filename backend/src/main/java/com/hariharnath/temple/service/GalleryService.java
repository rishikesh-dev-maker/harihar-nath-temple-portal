package com.hariharnath.temple.service;

import com.hariharnath.temple.dto.GalleryItemDto;
import com.hariharnath.temple.entity.GalleryItem;
import com.hariharnath.temple.exception.ResourceNotFoundException;
import com.hariharnath.temple.repository.GalleryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GalleryService {

    private final GalleryItemRepository galleryItemRepository;

    @Transactional(readOnly = true)
    public List<GalleryItemDto> getActiveGalleryItems() {
        return galleryItemRepository.findByActiveTrueOrderByDisplayOrderAscCreatedAtDesc().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GalleryItemDto> getAllGalleryItems() {
        return galleryItemRepository.findAllByOrderByDisplayOrderAscCreatedAtDesc().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public GalleryItemDto createGalleryItem(GalleryItemDto dto) {
        GalleryItem item = GalleryItem.builder()
                .title(dto.getTitle())
                .imageUrl(dto.getImageUrl())
                .description(dto.getDescription())
                .category(dto.getCategory() != null ? dto.getCategory() : "GENERAL")
                .active(dto.isActive())
                .displayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0)
                .build();

        return mapToDto(galleryItemRepository.save(item));
    }

    @Transactional
    public GalleryItemDto updateGalleryItem(Long id, GalleryItemDto dto) {
        GalleryItem item = galleryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery item not found with id: " + id));

        item.setTitle(dto.getTitle());
        item.setImageUrl(dto.getImageUrl());
        item.setDescription(dto.getDescription());
        item.setCategory(dto.getCategory());
        item.setActive(dto.isActive());
        if (dto.getDisplayOrder() != null) {
            item.setDisplayOrder(dto.getDisplayOrder());
        }

        return mapToDto(galleryItemRepository.save(item));
    }

    @Transactional
    public void deleteGalleryItem(Long id) {
        if (!galleryItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Gallery item not found with id: " + id);
        }
        galleryItemRepository.deleteById(id);
    }

    private GalleryItemDto mapToDto(GalleryItem entity) {
        return GalleryItemDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .imageUrl(entity.getImageUrl())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .active(entity.isActive())
                .displayOrder(entity.getDisplayOrder())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
