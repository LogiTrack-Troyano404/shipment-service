package com.example.shipmentservice.dto;

import com.example.shipmentservice.model.EstadoRepartidor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RepartidorRequestDTO(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150)
        String nombre,

        @NotBlank(message = "El tipo de vehículo es obligatorio")
        @Size(max = 200)
        String tipoVehiculo,

        EstadoRepartidor estado
) {
}
