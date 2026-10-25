package com.example.shipmentservice.dto;

import java.util.UUID;

public record CourierResponseDTO(
        UUID id,
        String nombre,
        String tipoVehiculo,
        String estado
) {
}
