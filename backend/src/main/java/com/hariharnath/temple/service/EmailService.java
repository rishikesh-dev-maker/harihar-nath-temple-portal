package com.hariharnath.temple.service;

import com.hariharnath.temple.entity.Booking;
import com.hariharnath.temple.entity.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${temple.admin.email:booking@hariharnath.in}")
    private String templeAdminEmail;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Async
    public void sendBookingReceivedEmails(Booking booking) {
        // 1. Email to Visitor
        try {
            if (isMailConfigured()) {
                SimpleMailMessage visitorMsg = new SimpleMailMessage();
                visitorMsg.setFrom(fromEmail.isBlank() ? "noreply@hariharnath.in" : fromEmail);
                visitorMsg.setTo(booking.getEmail());
                visitorMsg.setSubject("Darshan Request Received - " + booking.getRequestId() + " | Baba Hariharnath Temple");
                visitorMsg.setText(String.format(
                        "Namaste %s,\n\n" +
                        "Jai Baba Hariharnath!\n\n" +
                        "Your request for %s has been received by the temple administration.\n\n" +
                        "Details of your booking:\n" +
                        "Request ID: %s\n" +
                        "Service: %s\n" +
                        "Preferred Date: %s\n" +
                        "Devotees Count: %d\n" +
                        "Status: %s\n\n" +
                        "Our priests and administration will review your request. You can check your booking status anytime using your Request ID.\n\n" +
                        "Om Namah Shivaya,\n" +
                        "Baba Hariharnath Temple Administration\n" +
                        "Sonepur, Saran, Bihar",
                        booking.getName(),
                        booking.getService(),
                        booking.getRequestId(),
                        booking.getService(),
                        booking.getBookingDate(),
                        booking.getDevotees(),
                        booking.getStatus()
                ));
                mailSender.send(visitorMsg);
                log.info("Confirmation email dispatched to visitor: {}", booking.getEmail());
            } else {
                log.info("[SIMULATED EMAIL TO DEVOTEE] To: {}, Subject: Darshan Request Received ({}), Status: {}",
                        booking.getEmail(), booking.getRequestId(), booking.getStatus());
            }
        } catch (Exception ex) {
            log.warn("Could not dispatch visitor confirmation email (SMTP offline or misconfigured): {}", ex.getMessage());
        }

        // 2. Email to Temple Administration
        try {
            if (isMailConfigured()) {
                SimpleMailMessage adminMsg = new SimpleMailMessage();
                adminMsg.setFrom(fromEmail.isBlank() ? "noreply@hariharnath.in" : fromEmail);
                adminMsg.setTo(templeAdminEmail);
                adminMsg.setSubject("New Darshan Booking: " + booking.getRequestId() + " - " + booking.getName());
                adminMsg.setText(String.format(
                        "New Darshan Request Received:\n\n" +
                        "Request ID: %s\n" +
                        "Devotee Name: %s\n" +
                        "Mobile: %s\n" +
                        "Email: %s\n" +
                        "Service: %s\n" +
                        "Date: %s\n" +
                        "Devotees: %d\n" +
                        "Notes: %s\n\n" +
                        "Please login to the Admin Dashboard to review and approve.",
                        booking.getRequestId(),
                        booking.getName(),
                        booking.getMobile(),
                        booking.getEmail(),
                        booking.getService(),
                        booking.getBookingDate(),
                        booking.getDevotees(),
                        booking.getNotes() != null ? booking.getNotes() : "None"
                ));
                mailSender.send(adminMsg);
                log.info("New booking notification dispatched to admin: {}", templeAdminEmail);
            } else {
                log.info("[SIMULATED EMAIL TO ADMIN] To: {}, New booking from {} for {}",
                        templeAdminEmail, booking.getName(), booking.getService());
            }
        } catch (Exception ex) {
            log.warn("Could not dispatch admin notification email: {}", ex.getMessage());
        }
    }

    @Async
    public void sendBookingStatusUpdateEmail(Booking booking, BookingStatus newStatus) {
        try {
            if (isMailConfigured()) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromEmail.isBlank() ? "noreply@hariharnath.in" : fromEmail);
                message.setTo(booking.getEmail());

                String subject = String.format("Darshan Booking Update [%s] - %s", newStatus, booking.getRequestId());
                String body;

                if (newStatus == BookingStatus.CONFIRMED) {
                    body = String.format(
                            "Namaste %s,\n\n" +
                            "Jai Baba Hariharnath!\n\n" +
                            "We are pleased to inform you that your darshan booking has been CONFIRMED.\n\n" +
                            "Request ID: %s\n" +
                            "Service: %s\n" +
                            "Date: %s\n" +
                            "Devotees: %d\n\n" +
                            "Please arrive at the temple complex 30 minutes before your scheduled ritual.\n\n" +
                            "Om Namah Shivaya,\n" +
                            "Baba Hariharnath Temple Administration",
                            booking.getName(), booking.getRequestId(), booking.getService(),
                            booking.getBookingDate(), booking.getDevotees()
                    );
                } else if (newStatus == BookingStatus.CANCELLED) {
                    body = String.format(
                            "Namaste %s,\n\n" +
                            "Your darshan booking (Request ID: %s) for %s on %s has been CANCELLED.\n\n" +
                            "If you believe this is an error or wish to reschedule, please contact us at %s.\n\n" +
                            "Baba Hariharnath Temple Administration",
                            booking.getName(), booking.getRequestId(), booking.getService(),
                            booking.getBookingDate(), templeAdminEmail
                    );
                } else {
                    body = String.format(
                            "Namaste %s,\n\n" +
                            "Your darshan booking (Request ID: %s) status is now: %s.\n\n" +
                            "May Baba Hariharnath shower divine blessings upon you and your family.\n\n" +
                            "Baba Hariharnath Temple Administration",
                            booking.getName(), booking.getRequestId(), newStatus
                    );
                }

                message.setSubject(subject);
                message.setText(body);
                mailSender.send(message);
                log.info("Status update email ({}) sent to: {}", newStatus, booking.getEmail());
            } else {
                log.info("[SIMULATED EMAIL STATUS UPDATE] Devotee: {}, Status: {}", booking.getEmail(), newStatus);
            }
        } catch (Exception ex) {
            log.warn("Could not dispatch status update email: {}", ex.getMessage());
        }
    }

    private boolean isMailConfigured() {
        return fromEmail != null && !fromEmail.isBlank() && !fromEmail.contains("example.com");
    }
}
