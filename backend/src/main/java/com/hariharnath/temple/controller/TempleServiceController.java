package com.hariharnath.temple.controller;

import com.hariharnath.temple.dto.TempleServiceDto;
import com.hariharnath.temple.service.TempleServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
@Tag(name = "Services", description = "Public endpoints for temple puja and worship services")
public class TempleServiceController {

    private final TempleServiceService templeServiceService;

    @GetMapping
    @Operation(summary = "Get all active temple services (e.g. Abhishek, Archana, Rudrabhishek)")
    public ResponseEntity<List<TempleServiceDto>> getActiveServices() {
        return ResponseEntity.ok(templeServiceService.getActiveServices());
    }
}
