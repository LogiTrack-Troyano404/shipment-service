package com.example.shipmentservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.shipmentservice.model.EstadoRepartidor;

public record RepartidorResponseDTO(
        UUID id,
        String nombre,
        String tipoVehiculo,
        EstadoRepartidor estado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {
}
