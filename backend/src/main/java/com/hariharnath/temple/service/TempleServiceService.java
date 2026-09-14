package com.hariharnath.temple.service;

import com.hariharnath.temple.dto.TempleServiceDto;
import com.hariharnath.temple.entity.TempleService;
import com.hariharnath.temple.exception.ResourceNotFoundException;
import com.hariharnath.temple.repository.TempleServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TempleServiceService {

    private final TempleServiceRepository templeServiceRepository;

    @Transactional(readOnly = true)
    public List<TempleServiceDto> getActiveServices() {
        return templeServiceRepository.findByActiveTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TempleServiceDto> getAllServices() {
        return templeServiceRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TempleServiceDto createService(TempleServiceDto dto) {
        TempleService service = TempleService.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .active(dto.isActive())
                .build();

        return mapToDto(templeServiceRepository.save(service));
    }

    @Transactional
    public TempleServiceDto updateService(Long id, TempleServiceDto dto) {
        TempleService service = templeServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Temple service not found with id: " + id));

        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        service.setActive(dto.isActive());

        return mapToDto(templeServiceRepository.save(service));
    }

    @Transactional
    public void deleteService(Long id) {
        if (!templeServiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Temple service not found with id: " + id);
        }
        templeServiceRepository.deleteById(id);
    }

    private TempleServiceDto mapToDto(TempleService entity) {
        return TempleServiceDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
