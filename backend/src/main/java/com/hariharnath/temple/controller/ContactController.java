package com.hariharnath.temple.controller;

import com.hariharnath.temple.dto.ApiResponse;
import com.hariharnath.temple.dto.ContactMessageRequest;
import com.hariharnath.temple.dto.ContactMessageResponse;
import com.hariharnath.temple.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
@Tag(name = "Contact", description = "Public contact form submission")
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @Operation(summary = "Submit an enquiry or feedback to temple administration")
    public ResponseEntity<ApiResponse<ContactMessageResponse>> submitContact(
            @Valid @RequestBody ContactMessageRequest request) {

        ContactMessageResponse response = contactService.submitContact(request);
        return new ResponseEntity<>(
                ApiResponse.success("Message sent successfully. Temple administration will respond shortly.", response),
                HttpStatus.CREATED
        );
    }
}
