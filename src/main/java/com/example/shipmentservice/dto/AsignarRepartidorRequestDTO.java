package com.example.shipmentservice.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AsignarRepartidorRequestDTO(

        @NotNull(message = "El repartidor es obligatorio")
        UUID repartidorId
) {
}
