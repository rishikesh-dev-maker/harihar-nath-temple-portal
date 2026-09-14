package com.hariharnath.temple.service;

import com.hariharnath.temple.dto.ContactMessageRequest;
import com.hariharnath.temple.dto.ContactMessageResponse;
import com.hariharnath.temple.entity.ContactMessage;
import com.hariharnath.temple.entity.ContactStatus;
import com.hariharnath.temple.exception.ResourceNotFoundException;
import com.hariharnath.temple.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final ContactMessageRepository contactMessageRepository;

    @Transactional
    public ContactMessageResponse submitContact(ContactMessageRequest request) {
        ContactMessage contactMessage = ContactMessage.builder()
                .name(request.getName())
                .email(request.getEmail())
                .subject(request.getSubject())
                .message(request.getMessage())
                .status(ContactStatus.NEW)
                .build();

        ContactMessage saved = contactMessageRepository.save(contactMessage);
        log.info("New contact message received from: {}", saved.getEmail());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<ContactMessageResponse> getAllMessages(int page, int size, ContactStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ContactMessage> messages = status != null
                ? contactMessageRepository.findByStatus(status, pageable)
                : contactMessageRepository.findAll(pageable);
        return messages.map(this::mapToResponse);
    }

    @Transactional
    public ContactMessageResponse updateMessageStatus(Long id, ContactStatus status) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact message not found with id: " + id));

        message.setStatus(status);
        ContactMessage updated = contactMessageRepository.save(message);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteMessage(Long id) {
        if (!contactMessageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contact message not found with id: " + id);
        }
        contactMessageRepository.deleteById(id);
    }

    private ContactMessageResponse mapToResponse(ContactMessage entity) {
        return ContactMessageResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .subject(entity.getSubject())
                .message(entity.getMessage())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
