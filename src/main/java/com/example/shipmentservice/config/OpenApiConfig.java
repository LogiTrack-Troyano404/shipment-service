package com.example.shipmentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_JWT = "bearer-jwt";

    @Bean
    public OpenAPI shipmentOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Shipment Service API")
                        .version("v1")
                        .description("Microservicio de gestión de envíos y repartidores — LogiTrack Sprint 1"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_JWT))
                .components(new Components().addSecuritySchemes(BEARER_JWT,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Pegá el token devuelto por POST /auth/login de security-service (sin la palabra Bearer)")));
    }
}
