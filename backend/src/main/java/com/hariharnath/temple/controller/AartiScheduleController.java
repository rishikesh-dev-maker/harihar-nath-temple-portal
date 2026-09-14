package com.hariharnath.temple.controller;

import com.hariharnath.temple.dto.AartiScheduleDto;
import com.hariharnath.temple.service.AartiScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/aarti")
@RequiredArgsConstructor
@Tag(name = "Aarti Schedule", description = "Public endpoints for daily Aarti and ritual schedule")
public class AartiScheduleController {

    private final AartiScheduleService aartiScheduleService;

    @GetMapping
    @Operation(summary = "Get daily Aarti and ritual schedule")
    public ResponseEntity<List<AartiScheduleDto>> getActiveAartiSchedules() {
        return ResponseEntity.ok(aartiScheduleService.getActiveAartiSchedules());
    }
}
