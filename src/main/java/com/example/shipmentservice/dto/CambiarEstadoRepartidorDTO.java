package com.example.shipmentservice.dto;

import com.example.shipmentservice.model.EstadoRepartidor;

import jakarta.validation.constraints.NotNull;

public record CambiarEstadoRepartidorDTO(

        @NotNull(message = "El estado es obligatorio")
        EstadoRepartidor estado
) {
}
