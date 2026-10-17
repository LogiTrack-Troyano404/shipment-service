package com.example.shipmentservice.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Hitorial_de_estados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class HistorialEstado extends AuditoriaBase{
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@EqualsAndHashCode.Include
	private UUID id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "estado_anterior" ,nullable = false, length = 20)
	private EstadoEnvio estadoAnterior;
	
	@Column(name = "fecha_cambio", nullable = false)
	private LocalDateTime fechaCambio;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "envio_id", nullable = false)
    private Envio envio;


}