package com.example.shipmentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DireccionDTO(

        @NotBlank(message = "La calle es obligatoria")
        @Size(max = 200)
        String calle,

        @NotBlank(message = "El número es obligatorio")
        @Size(max = 15)
        String numero,

        @NotBlank(message = "La ciudad es obligatoria")
        @Size(max = 200)
        String ciudad,

        @NotBlank(message = "El código postal es obligatorio")
        @Size(max = 15)
        String codigoPostal
) {
}
