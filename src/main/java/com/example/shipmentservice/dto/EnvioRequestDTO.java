package com.example.shipmentservice.dto;

import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record EnvioRequestDTO(

        @NotNull(message = "El cliente es obligatorio")
        UUID clienteId,

        UUID repartidorId,

        @NotNull(message = "La dirección de origen es obligatoria")
        @Valid
        DireccionDTO origen,

        @NotNull(message = "La dirección de destino es obligatoria")
        @Valid
        DireccionDTO destino,

        @Positive(message = "El peso debe ser mayor a cero")
        double peso,

        @Positive(message = "El ancho debe ser mayor a cero")
        double ancho,

        @Positive(message = "El alto debe ser mayor a cero")
        double alto,

        @NotNull(message = "La tarifa es obligatoria")
        @PositiveOrZero(message = "La tarifa no puede ser negativa")
        Double tarifa
) {
}
