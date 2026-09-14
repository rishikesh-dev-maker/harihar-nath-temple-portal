package com.hariharnath.temple.config;

import com.hariharnath.temple.entity.*;
import com.hariharnath.temple.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AartiScheduleRepository aartiScheduleRepository;
    private final AnnouncementRepository announcementRepository;
    private final TempleServiceRepository templeServiceRepository;
    private final GalleryItemRepository galleryItemRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.name:Temple Administrator}")
    private String adminName;

    @Value("${admin.email:admin@hariharnath.in}")
    private String adminEmail;

    @Value("${admin.password:Admin@123}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        seedAdminUser();
        seedAartiSchedule();
        seedAnnouncements();
        seedTempleServices();
        seedGallery();
    }

    private void seedAdminUser() {
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = User.builder()
                    .name(adminName)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            log.info("Initial admin account created: {}", adminEmail);
        }

        String ownerEmail = "rishikkr725@gmail.com";
        if (!userRepository.existsByEmail(ownerEmail)) {
            User owner = User.builder()
                    .name("Rishikesh Kumar")
                    .email(ownerEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(owner);
            log.info("Owner admin account created: {}", ownerEmail);
        }
    }

    private void seedAartiSchedule() {
        if (aartiScheduleRepository.count() == 0) {
            List<AartiSchedule> schedules = List.of(
                    AartiSchedule.builder().name("Mangala Aarti").time("04:00 / 05:00").description("Morning awakening ritual").displayOrder(1).active(true).build(),
                    AartiSchedule.builder().name("Nitya Abhishek").time("06:00–07:00").description("Sacred bathing ceremony").displayOrder(2).active(true).build(),
                    AartiSchedule.builder().name("Rajbhog").time("11:30–12:00").description("Royal offering to deity").displayOrder(3).active(true).build(),
                    AartiSchedule.builder().name("Shayan").time("12:00–14:00").description("Midday rest").displayOrder(4).active(true).build(),
                    AartiSchedule.builder().name("Devotee Puja").time("14:00–16:00").description("Public worship and darshan").displayOrder(5).active(true).build(),
                    AartiSchedule.builder().name("Sandhya Aarti").time("18:30 / 19:30").description("Evening illumination").displayOrder(6).active(true).build(),
                    AartiSchedule.builder().name("Bhog").time("20:30").description("Evening offering").displayOrder(7).active(true).build(),
                    AartiSchedule.builder().name("Shayan").time("21:00").description("Night rest").displayOrder(8).active(true).build()
            );
            aartiScheduleRepository.saveAll(schedules);
            log.info("Default Aarti schedule seeded ({} items)", schedules.size());
        }
    }

    private void seedAnnouncements() {
        if (announcementRepository.count() == 0) {
            List<Announcement> announcements = List.of(
                    Announcement.builder()
                            .title("Kartik Purnima Mahotsav")
                            .message("🪔 Daily Darshan & Aarti Schedule · Kartik Purnima preparations begin. Join us on the full moon night.")
                            .active(true)
                            .displayOrder(1)
                            .startDate(LocalDate.now().minusDays(5))
                            .endDate(LocalDate.now().plusMonths(2))
                            .build(),
                    Announcement.builder()
                            .title("Sacred Evening Aarti")
                            .message("🙏 Join us for the sacred Sandhya Aarti every evening at Harihar Kshetra.")
                            .active(true)
                            .displayOrder(2)
                            .startDate(LocalDate.now().minusDays(10))
                            .endDate(LocalDate.now().plusMonths(6))
                            .build(),
                    Announcement.builder()
                            .title("Seasonal Timings Advisory")
                            .message("Temple timings may vary on festival days · Advance darshan booking recommended.")
                            .active(true)
                            .displayOrder(3)
                            .startDate(LocalDate.now().minusDays(10))
                            .endDate(LocalDate.now().plusMonths(6))
                            .build()
            );
            announcementRepository.saveAll(announcements);
            log.info("Default announcements seeded.");
        }
    }

    private void seedTempleServices() {
        if (templeServiceRepository.count() == 0) {
            List<TempleService> services = List.of(
                    TempleService.builder().name("Nitya Abhishek").description("Daily ritual bathing of the deity with sacred water, milk, and offerings.").price(new BigDecimal("251.00")).active(true).build(),
                    TempleService.builder().name("Archana").description("Offering of sacred flowers and chanting of 108 divine names for auspicious blessings.").price(new BigDecimal("101.00")).active(true).build(),
                    TempleService.builder().name("Rudrabhishek").description("Special Vedic chanting worship with Rudram hymns for peace, health, and prosperity.").price(new BigDecimal("1100.00")).active(true).build(),
                    TempleService.builder().name("Special Puja").description("Personalized family sankalp puja conducted by temple priests.").price(new BigDecimal("501.00")).active(true).build()
            );
            templeServiceRepository.saveAll(services);
            log.info("Default temple services seeded.");
        }
    }

    private void seedGallery() {
        if (galleryItemRepository.count() == 0) {
            List<GalleryItem> items = List.of(
                    GalleryItem.builder().title("Sacred Sanctum & Deity").imageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEBjfuvG44ar02MZKAlMvbOd213M4CacO5a6ug86Ak6BGNuetf3w0NJ7PF&s=10").description("Divine idol of Hariharnath").category("DEITY").active(true).displayOrder(1).build(),
                    GalleryItem.builder().title("River Confluence Worship").imageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTm1Hv20p2zdnIe2Nhu7ucJHej97_CedzwtQy9T_ksd0w&s=10").description("Sacred snan at Ganga-Gandak confluence").category("RITUAL").active(true).displayOrder(2).build(),
                    GalleryItem.builder().title("Ancient Temple Shikhara").imageUrl("https://coinventmediastorage.blob.core.windows.net/media-storage-container/gphoto_Ei1IYXJpaGFyYW5hdGggUmQsIFNvbmVwdXIsIEJpaGFyIDg0MTEwMSwgSW5kaWEiLiosChQKEgkf0YkW-FvtOREAMVfIJDjarxIUChIJm2NsaOpb7TkRjbMEPEFXXxc_0.jpg").description("Heritage stone architecture").category("ARCHITECTURE").active(true).displayOrder(3).build(),
                    GalleryItem.builder().title("Evening Aarti & Deep Daan").imageUrl("https://images.unsplash.com/photo-1542838132-92c53300491e?w=800&auto=format&fit=crop&q=80").description("Lamps illuminating the temple courtyard").category("AARTI").active(true).displayOrder(4).build()
            );
            galleryItemRepository.saveAll(items);
            log.info("Default gallery items seeded.");
        }
    }
}
