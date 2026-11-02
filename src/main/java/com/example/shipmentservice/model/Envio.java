package com.example.shipmentservice.model;

import java.util.*;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "envios")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Envio extends AuditoriaBase{
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(length = 36)
	@EqualsAndHashCode.Include
	private UUID id;
	
	@Column(name = "codigo_de_seguimiento", unique = true, nullable = false, length = 15)
	@EqualsAndHashCode.Include
	private String codigoSeguimiento;
	
	@Column(nullable = false)
	private double peso;
	
	@Column(nullable = false)
	private double ancho;
	
	@Column(nullable = false)
	private double alto;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private EstadoEnvio estado;
	
	@Column(nullable = false)
	private Double tarifa;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "calle", column = @Column(name = "origen_calle", nullable = false, length = 200)),
		@AttributeOverride(name = "numero", column = @Column(name = "origen_numero", nullable = false, length = 15)),
		@AttributeOverride(name = "ciudad", column = @Column(name = "origen_ciudad", nullable = false, length = 200)),
		@AttributeOverride(name = "codigoPostal", column = @Column(name = "origen_codigo_postal", nullable = false, length = 15))
	})
	private Direccion origen;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "calle", column = @Column(name = "destino_calle", nullable = false, length = 200)),
		@AttributeOverride(name = "numero", column = @Column(name = "destino_numero", nullable = false, length = 15)),
		@AttributeOverride(name = "ciudad", column = @Column(name = "destino_ciudad", nullable = false, length = 200)),
		@AttributeOverride(name = "codigoPostal", column = @Column(name = "destino_codigo_postal", nullable = false, length = 15))
	})
	private Direccion destino;

	@Column(name = "repartidor_id")
	@JdbcTypeCode(SqlTypes.CHAR)
	private UUID repartidorId;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "cliente_id", nullable = false)
	private Cliente cliente;
	
	@OneToMany(mappedBy = "envio", fetch = FetchType.LAZY)
	private List<HistorialEstado> historialEstados = new ArrayList<>();
}
