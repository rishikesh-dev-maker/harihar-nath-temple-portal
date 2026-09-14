package com.hariharnath.temple.repository;

import com.hariharnath.temple.entity.ContactMessage;
import com.hariharnath.temple.entity.ContactStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    Page<ContactMessage> findByStatus(ContactStatus status, Pageable pageable);
    long countByStatus(ContactStatus status);
}
