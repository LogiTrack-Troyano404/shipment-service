package com.example.shipmentservice.model;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Direccion {
	
	@Column(nullable = false, length = 200)
	private String calle;
	
	@Column(nullable = false, length = 15)
	private String numero;
	
	@Column(nullable = false, length = 200)
	private String ciudad;
	
	@Column(name = "codigo_postal", nullable = false, length = 15)
	private String codigoPostal;

}
