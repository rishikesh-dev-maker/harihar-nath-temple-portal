# Baba Hariharnath Temple Backend Service

Production-ready backend API service for **Baba Hariharnath Temple, Sonepur, Bihar** ("Where Hari and Har meet in devotion").

Built with **Java 17+**, **Spring Boot 3+**, **Spring Data JPA**, **Spring Security**, **JWT Authentication**, **Jakarta Bean Validation**, and **MySQL 8+**.

---

## 🏛️ System Architecture

```text
Frontend (Bootstrap 5 / Vanilla JS)
       │
       ▼
REST API Client (fetch -> /api/v1/...)
       │
       ▼
Spring Security + JwtAuthenticationFilter
       │
       ▼
Controller Layer (@RestController)
       │
       ▼
DTO Validation & Mapping Layer
       │
       ▼
Service Layer (@Service, @Transactional, EmailService)
       │
       ▼
Repository Layer (Spring Data JPA / Hibernate)
       │
       ▼
MySQL 8+ Database (hariharnath_temple)
```

---

## 🚀 Quick Start

### 1. Prerequisites
* Java JDK 17 or higher (`java -version`)
* Apache Maven 3.8+ (`mvn -version`)
* MySQL 8.0+ running locally or in Docker

### 2. Database Setup
Log in to MySQL and run:
```sql
CREATE DATABASE hariharnath_temple CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
Or execute `backend/schema.sql`:
```bash
mysql -u root -p < backend/schema.sql
```

### 3. Environment Variables
You can configure the backend using environment variables or modify `src/main/resources/application.properties`:

| Variable | Default Value | Description |
|---|---|---|
| `PORT` | `8080` | HTTP port for the Spring Boot application |
| `DB_URL` | `jdbc:mysql://localhost:3306/hariharnath_temple?...` | JDBC MySQL connection URL |
| `DB_USERNAME` | `root` | Database username |
| `DB_PASSWORD` | `root` | Database password |
| `JWT_SECRET` | `(Base64 64-byte key)` | Secret key for signing JWT tokens |
| `JWT_EXPIRATION` | `86400000` (24h) | Access token expiration in ms |
| `CORS_ALLOWED_ORIGINS`| `http://localhost:5500,http://localhost:3000` | Allowed web origins |
| `ADMIN_NAME` | `Temple Administrator` | Initial admin account name |
| `ADMIN_EMAIL` | `admin@hariharnath.in` | Initial admin login email |
| `ADMIN_PASSWORD` | `Admin@123` | Initial admin password (BCrypt hashed on startup) |
| `SMTP_HOST` | `smtp.gmail.com` | SMTP host for booking emails |
| `SMTP_PORT` | `587` | SMTP port |
| `SMTP_USERNAME` | `""` | SMTP user email |
| `SMTP_PASSWORD` | `""` | SMTP App password |

*(Note: If SMTP is left empty, the application runs smoothly without crashing and logs simulated email notices).*

### 4. Build and Run Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

To run with the in-memory H2 dev profile (zero MySQL setup required for quick testing):
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 5. Running Tests
```bash
mvn test
```

---

## 📖 Swagger / OpenAPI Documentation
Once the server is running, explore interactive API documentation and test endpoints at:
```text
http://localhost:8080/swagger-ui/index.html
```
Raw OpenAPI JSON schema:
```text
http://localhost:8080/v3/api-docs
```

---

## 🔐 Authentication & Seeded Credentials
Default administrator created automatically on first run:
* **Email:** `admin@hariharnath.in`
* **Password:** `Admin@123`
* **Role:** `ADMIN`

To obtain a token:
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@hariharnath.in","password":"Admin@123"}'
```

---

## 📡 REST API Catalog

### Public Endpoints
* `POST /api/v1/auth/login` — Devotee / Staff / Admin login, returns JWT
* `POST /api/v1/auth/register` — Staff registration
* `POST /api/v1/auth/refresh` — Refresh access token
* `POST /api/v1/bookings` — Submit a darshan / puja booking
* `GET /api/v1/bookings/{requestId}` — Lookup booking by Request ID (e.g. `BHT-2026-000001`)
* `POST /api/v1/contact` — Devotee contact/enquiry submission
* `GET /api/v1/announcements` — Active announcements for ticker
* `GET /api/v1/services` — Active puja & worship offerings
* `GET /api/v1/aarti` — Daily Aarti & ritual timetable
* `GET /api/v1/gallery` — Temple photos and gallery

### Protected Admin Endpoints (`Authorization: Bearer <token>`)
* `GET /api/v1/admin/dashboard/stats` — Total bookings, pending, confirmed, unread inquiries
* `GET /api/v1/admin/bookings` — Paginated bookings with status/date/search filtering
* `PUT /api/v1/admin/bookings/{id}` — Update status (`PENDING`, `CONFIRMED`, `CANCELLED`, `COMPLETED`)
* `DELETE /api/v1/admin/bookings/{id}` — Delete booking
* `GET /api/v1/admin/contact` — Paginated visitor messages
* `PUT /api/v1/admin/contact/{id}/status` — Change message status (`NEW`, `READ`, `REPLIED`, `ARCHIVED`)
* `DELETE /api/v1/admin/contact/{id}` — Delete message
* `GET /api/v1/admin/announcements` — All announcements
* `POST /api/v1/admin/announcements` — Create announcement
* `PUT /api/v1/admin/announcements/{id}` — Edit announcement
* `DELETE /api/v1/admin/announcements/{id}` — Delete announcement
* `POST /api/v1/admin/services` — Add puja service
* `PUT /api/v1/admin/services/{id}` — Update puja service
* `DELETE /api/v1/admin/services/{id}` — Delete puja service
* `POST /api/v1/admin/aarti` — Add Aarti timing
* `PUT /api/v1/admin/aarti/{id}` — Update Aarti timing
* `DELETE /api/v1/admin/aarti/{id}` — Delete Aarti timing
* `POST /api/v1/admin/gallery` — Add gallery photo
* `PUT /api/v1/admin/gallery/{id}` — Update gallery photo
* `DELETE /api/v1/admin/gallery/{id}` — Delete gallery photo
* `GET /api/v1/admin/users` — Staff/Admin user management
* `PUT /api/v1/admin/users/{id}/status` — Enable/disable staff user
