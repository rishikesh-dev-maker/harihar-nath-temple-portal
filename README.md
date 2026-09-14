# Baba Hariharnath Temple Official Portal

A professional, production-ready digital gateway for the historic Baba Hariharnath Temple in Sonepur, Bihar. This project integrates a cinematic, responsive frontend with a robust Spring Boot backend (or an alternative Express development server) to provide devotees with live information and seamless darshan booking services.

## Project Overview

The Baba Hariharnath Temple Portal is designed to bridge the gap between ancient Vedic traditions and modern digital convenience. Located at the sacred confluence of the Ganga and Gandak rivers, the temple attracts millions of devotees annually. This portal serves as the official administrative and devotional hub, managing everything from live ritual timings to secure pilgrim registrations. 

## Problem Statement

Managing a high-traffic pilgrimage site involves significant logistical challenges:
*   Devotees often lack real-time information regarding Aarti schedules and festival announcements.
*   Manual booking processes for special pujas are prone to errors and lack transparency.
*   Temple administration requires a centralized dashboard to track registrations, manage staff, and update public information instantly.

## Project Objectives

*   **Enhance Devotional Experience:** Provide a cinematic, mobile-responsive interface for information and virtual darshan.
*   **Streamline Operations:** Implement an automated booking system with unique Request ID tracking.
*   **Centralized Administration:** Create a secure, JWT-protected dashboard for temple staff to manage all data points.
*   **Scalable Architecture:** Deliver a modular full-stack solution using industry-standard frameworks (Spring Boot and React-ready Express).

## Key Features

### Devotee-Facing Portal
*   **Cinematic Hero Section:** A responsive, auto-sliding gallery showcasing the temple's beauty with integrated cinematic overlays.
*   **Dynamic Announcement Bar:** Real-time rotating announcements fetched directly from the database to inform pilgrims of upcoming events like Kartik Purnima.
*   **Live Ritual Schedule:** Synchronized Aarti timings and temple opening/closing hours.
*   **Temple Services Directory:** Detailed list of available pujas (Rudrabhishek, Nitya Abhishek, etc.) with transparent pricing.
*   **Darshan Booking System:** A multi-step form for scheduling visits, generating unique tracking IDs (e.g., BHT-2026-000001).
*   **Booking Status Tracker:** Dedicated lookup tool for devotees to check their booking confirmation status using their Request ID.
*   **Sacred Timeline & Architecture:** Interactive sections detailing the Puranic history and Nagara-style architecture of the temple.

### Administration Dashboard (Staff Portal)
*   **Secure Authentication:** JWT-based login system with staff role management.
*   **Unified Statistics:** Real-time counters for pending bookings, confirmed darshans, and unread inquiries.
*   **Booking Management:** Complete CRUD operations for pilgrim requests, including status updates (Confirm/Complete/Cancel).
*   **Content Management (CMS):** Dynamic control over announcements, gallery items, and Aarti timings without code changes.
*   **Inquiry Handling:** A centralized system to read and respond to devotee messages.
*   **Security Controls:** Admin password management and staff account activation/deactivation.

## Technologies / Tech Stack

### Frontend
*   **HTML5 / CSS3:** Custom styling with modern responsive units (`svh`, `clamp`).
*   **Bootstrap 5.3:** For core grid layout and UI components.
*   **JavaScript (ES6+):** Modular frontend logic and API integration.
*   **AOS (Animate On Scroll):** For smooth scroll-based animations.
*   **SweetAlert2:** For elegant, user-friendly notifications and modals.
*   **Bootstrap Icons:** For professional, scalable iconography.

### Backend (Option 1: Production-Grade)
*   **Java 17:** Long Term Support (LTS) release.
*   **Spring Boot 3.3.3:** Robust micro-framework for the REST API.
*   **Spring Data JPA:** Database abstraction and management.
*   **Spring Security:** Comprehensive JWT-based security implementation.
*   **MySQL 8.0:** Scalable relational database for persistent storage.
*   **SpringDoc OpenAPI:** Automated Swagger UI documentation.
*   **Lombok:** To reduce boilerplate code.

### Backend (Option 2: Development/Fast Prototyping)
*   **Node.js / Bun:** High-performance runtime.
*   **Express / TypeScript:** Typed, lightweight API server.
*   **JWT & Bcryptjs:** Secure authentication and password hashing.
*   **Vite:** Served as a middleware for optimized frontend development.

## Project Structure

```text
├── backend/                # Spring Boot Java Project
│   ├── src/main/java/      # Source code (Controller, Service, Entity, etc.)
│   ├── src/main/resources/ # Configuration (application.properties)
│   ├── pom.xml             # Maven dependencies
│   └── schema.sql          # MySQL database definition
├── public/                 # Static assets (images, logos)
│   └── assets/             # Project images
├── src/                    # React source (Reserved for future expansion)
├── index.html              # Main frontend entry point
├── server.ts               # Node.js/Express development server
├── vite.config.ts          # Frontend build configuration
├── package.json            # Node.js dependencies
└── README.md               # Project documentation
```

## Application Workflow

1.  **Devotee Journey:** Devotee visits the portal → Views live Aarti/Announcements → Fills Booking Form → Receives Request ID → Tracks Status via ID.
2.  **Admin Journey:** Admin logs in (JWT) → Views Dashboard Stats → Manages Bookings (Confirm/Cancel) → Updates Announcements/Aarti timings → Logs out securely.
3.  **Data Flow:** Frontend `fetch()` → Backend API (Spring Boot/Express) → MySQL/In-memory Store → JSON Response → UI Update.
4.  **Documentation:** Developer access to Swagger UI (`/swagger-ui/index.html`) for API testing and integration.

## Installation & Setup

### Prerequisites
*   **Java 21** & **Maven 3.x** (for Spring Boot backend)
*   **Node.js 20+** or **Bun** (for frontend and development server)
*   **MySQL 8.0** (for Spring Boot production use)

### Backend Setup (Spring Boot)
1.  Create a MySQL database: `CREATE DATABASE hariharnath_temple;`
2.  Import `backend/schema.sql` to initialize tables.
3.  Configure `backend/src/main/resources/application.properties` with your DB credentials.
4.  Run the application: `mvn spring-boot:run`

### Frontend & Development Server (Node.js)
1.  Install dependencies: `npm install`
2.  Start the development server: `npm run dev`
3.  Access the portal at `http://localhost:3000`

## API Endpoints Summary

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Staff Authentication | No |
| `GET` | `/api/v1/aarti` | Get active Aarti schedule | No |
| `POST` | `/api/v1/bookings` | Create new darshan booking | No |
| `GET` | `/api/v1/bookings/:id` | Check booking status | No |
| `GET` | `/api/v1/admin/stats` | Dashboard statistics | Yes (JWT) |
| `PUT` | `/api/v1/admin/bookings/:id`| Update booking status | Yes (JWT) |

## Challenges & Technical Considerations

*   **Responsiveness:** Balancing high-quality images with performance on mobile devices required custom CSS viewport management (`100svh`).
*   **Security:** Implementing stateless JWT authentication across two different backend environments (Java and Node) ensured consistency in security protocols.
*   **Data Integrity:** Unique Request ID generation handles high-concurrency booking requests gracefully.

## Future Improvements (Proposed)

*   **Payment Gateway Integration:** Direct payment for puja services via UPI/Netbanking.
*   **Live Streaming:** Integration of a YouTube Live API for virtual aarti darshan.
*   **Multi-language Support:** Adding Hindi and Bhojpuri localization for local devotees.
*   **Mobile App:** A native Android/iOS app using the existing REST API.

## Project Status
**Completed & Production Ready**

## Author
**Rishikesh Kumar**  
*Full Stack Developer*  
Backend: Spring Boot | Frontend: JS/Bootstrap | Database: MySQL
#   h a r i h a r - n a t h - t e m p l e - p o r t a l 
 
 
