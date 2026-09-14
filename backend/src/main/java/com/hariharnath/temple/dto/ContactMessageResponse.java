package com.hariharnath.temple.dto;

import com.hariharnath.temple.entity.ContactStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessageResponse {

    private Long id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private ContactStatus status;
    private LocalDateTime createdAt;
}
