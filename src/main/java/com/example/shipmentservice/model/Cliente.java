package com.example.shipmentservice.model;

import java.util.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Cliente extends AuditoriaBase{
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(length = 36)
	@EqualsAndHashCode.Include
	private UUID id;
	
	@Column(name = "nombre", nullable = false, length = 150)
	private String nombre;
	
	@Column(nullable = false, unique = true, length = 100)
	private String email;
	
	@Column(length = 20)
	private String telefono;
	
	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
	private List<Envio> envios= new ArrayList<>();

}
