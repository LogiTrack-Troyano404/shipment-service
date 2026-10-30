package com.example.shipmentservice.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ClienteResponseDTO(
	
	UUID id,
	String nombre,
	String email,
	String telefono,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaModificacion
) {	
}
