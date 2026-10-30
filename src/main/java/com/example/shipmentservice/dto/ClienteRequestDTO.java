package com.example.shipmentservice.dto;

import jakarta.validation.constraints.*
;

public record ClienteRequestDTO (
	
	@NotBlank(message="el nombre es obligatorio")
	@Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
	String nombre,
	
	@NotBlank(message="el email es obligatorio")
	@Size(max = 100, message = "El email no puede superar los 100 caracteres")
	String email,
	
	@Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    String telefono
	
) {
	
}

