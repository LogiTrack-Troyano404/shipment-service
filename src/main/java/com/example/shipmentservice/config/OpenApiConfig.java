package com.example.shipmentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI shipmentOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Shipment Service API")
                        .version("v1")
                        .description("Microservicio de gestión de envíos y repartidores — LogiTrack Sprint 1"));
    }
}
