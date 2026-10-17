package com.example.shipmentservice.model;

import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "repartidores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Repartidor extends AuditoriaBase{
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@EqualsAndHashCode.Include
	private UUID id;
	
	@Column(name = "nombre_repartidor" , nullable = false, length = 150)
	private String nombre;
	
	@Column(name = "tipo_de_vehiculo", nullable = false, length = 200)
	private String tipoVehiculo;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private EstadoRepartidor estado;
	
	@OneToMany(mappedBy = "repartidor", fetch = FetchType.LAZY)
	private List<Envio> envios = new ArrayList<>();
	
}
