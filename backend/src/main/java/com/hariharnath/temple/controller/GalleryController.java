package com.hariharnath.temple.controller;

import com.hariharnath.temple.dto.GalleryItemDto;
import com.hariharnath.temple.service.GalleryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gallery")
@RequiredArgsConstructor
@Tag(name = "Gallery", description = "Public temple media and photo gallery endpoints")
public class GalleryController {

    private final GalleryService galleryService;

    @GetMapping
    @Operation(summary = "Get active temple gallery photos")
    public ResponseEntity<List<GalleryItemDto>> getGalleryItems() {
        return ResponseEntity.ok(galleryService.getActiveGalleryItems());
    }
}
