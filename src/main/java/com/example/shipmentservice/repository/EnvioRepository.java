package com.example.shipmentservice.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shipmentservice.model.Envio;
import com.example.shipmentservice.model.EstadoEnvio;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, UUID>{
	
	Optional<Envio> findByCodigoSeguimiento(String codigoSeguimiento);
	
	List<Envio> findByEstado(EstadoEnvio estado);
	
}
