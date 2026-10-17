package com.example.shipmentservice.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditoriaBase {
	
	@CreatedDate
	@Column(name = "fecha_creacion", nullable = false, updatable = false)
	private LocalDateTime fechaCracion;
	
	@LastModifiedDate
	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;

}
