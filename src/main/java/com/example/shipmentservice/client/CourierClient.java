package com.example.shipmentservice.client;

import com.example.shipmentservice.Exception.RepartidorNoEncontrado;
import com.example.shipmentservice.dto.CourierResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;

@Component
@Slf4j
public class CourierClient {

    private final RestClient restClient;

    public CourierClient(RestClient.Builder restClientBuilder,
                         @Value("${services.courier.url:http://localhost:8020}") String courierServiceUrl) {
        this.restClient = restClientBuilder.baseUrl(courierServiceUrl).build();
    }

    public CourierResponseDTO obtenerRepartidorPorId(UUID repartidorId) {
        log.info("Consultando al courier-service por el repartidor ID: {}", repartidorId);

        // Extraemos el token Bearer de la petición actual para propagarlo
        String token = obtenerTokenDePeticionActual();

        try {
            var requestSpec = restClient.get()
                    .uri("/api/v1/repartidores/{id}", repartidorId);

            if (token != null && !token.isBlank()) {
                requestSpec.header("Authorization", token);
            }

            return requestSpec.retrieve()
                    .body(CourierResponseDTO.class);

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Repartidor no encontrado en courier-service con ID: {}", repartidorId);
            throw new RepartidorNoEncontrado("Repartidor no encontrado con ID: " + repartidorId);
        } catch (Exception e) {
            log.error("Error de comunicación con el courier-service: {}", e.getMessage());
            throw new RuntimeException("Error al comunicarse con el servicio de repartidores");
        }
    }

    private String obtenerTokenDePeticionActual() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return request.getHeader("Authorization");
            }
        } catch (Exception e) {
            log.warn("No se pudo extraer el token de la petición actual para propagarlo", e);
        }
        return null;
    }
}