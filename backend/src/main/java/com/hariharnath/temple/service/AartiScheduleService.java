package com.hariharnath.temple.service;

import com.hariharnath.temple.dto.AartiScheduleDto;
import com.hariharnath.temple.entity.AartiSchedule;
import com.hariharnath.temple.exception.ResourceNotFoundException;
import com.hariharnath.temple.repository.AartiScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AartiScheduleService {

    private final AartiScheduleRepository aartiScheduleRepository;

    @Transactional(readOnly = true)
    public List<AartiScheduleDto> getActiveAartiSchedules() {
        return aartiScheduleRepository.findByActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AartiScheduleDto> getAllAartiSchedules() {
        return aartiScheduleRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AartiScheduleDto createAartiSchedule(AartiScheduleDto dto) {
        AartiSchedule schedule = AartiSchedule.builder()
                .name(dto.getName())
                .time(dto.getTime())
                .description(dto.getDescription())
                .active(dto.isActive())
                .displayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0)
                .build();

        return mapToDto(aartiScheduleRepository.save(schedule));
    }

    @Transactional
    public AartiScheduleDto updateAartiSchedule(Long id, AartiScheduleDto dto) {
        AartiSchedule schedule = aartiScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aarti schedule not found with id: " + id));

        schedule.setName(dto.getName());
        schedule.setTime(dto.getTime());
        schedule.setDescription(dto.getDescription());
        schedule.setActive(dto.isActive());
        if (dto.getDisplayOrder() != null) {
            schedule.setDisplayOrder(dto.getDisplayOrder());
        }

        return mapToDto(aartiScheduleRepository.save(schedule));
    }

    @Transactional
    public void deleteAartiSchedule(Long id) {
        if (!aartiScheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aarti schedule not found with id: " + id);
        }
        aartiScheduleRepository.deleteById(id);
    }

    private AartiScheduleDto mapToDto(AartiSchedule entity) {
        return AartiScheduleDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .time(entity.getTime())
                .description(entity.getDescription())
                .active(entity.isActive())
                .displayOrder(entity.getDisplayOrder())
                .build();
    }
}
