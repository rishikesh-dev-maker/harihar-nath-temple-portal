package com.hariharnath.temple.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Baba Hariharnath Temple REST API",
                version = "v1.0.0",
                description = "Production-ready backend API portal for Baba Hariharnath Temple, Sonepur, Bihar. Handles visitor darshan bookings, contact inquiries, rotating announcements, Aarti schedules, and administration.",
                contact = @Contact(
                        name = "Baba Hariharnath Temple Administration",
                        email = "booking@hariharnath.in",
                        url = "https://hariharnath.in"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(url = "/", description = "Default Server URL")
        },
        security = {
                @SecurityRequirement(name = "BearerAuth")
        }
)
@SecurityScheme(
        name = "BearerAuth",
        description = "JWT Authorization header using Bearer scheme. Example: \"Authorization: Bearer {token}\"",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
