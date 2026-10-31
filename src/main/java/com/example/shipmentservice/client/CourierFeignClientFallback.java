package com.example.shipmentservice.client;

import com.example.shipmentservice.dto.CourierResponseDTO;
import com.example.shipmentservice.Exception.RepartidorNoEncontrado;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class CourierFeignClientFallback implements CourierFeignClient {

    @Override
    public CourierResponseDTO obtenerRepartidorPorId(UUID repartidorId) {
        log.warn("[FALLBACK] El courier-service no está disponible o falló al buscar el ID: {}", repartidorId);
        // Puedes lanzar una excepción controlada de negocio o devolver un objeto por defecto
        throw new RepartidorNoEncontrado("El servicio de repartidores no se encuentra disponible temporalmente. No se pudo validar el ID: " + repartidorId);
    }
}