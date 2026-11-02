package com.example.shipmentservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.shipmentservice.model.EstadoEnvio;

public record EnvioResponseDTO(
        UUID id,
        String codigoSeguimiento,
        double peso,
        double ancho,
        double alto,
        EstadoEnvio estado,
        Double tarifa,
        DireccionDTO origen,
        DireccionDTO destino,
        UUID clienteId,
        String clienteNombre,
        UUID repartidorId,
        String repartidorNombre,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {
}
