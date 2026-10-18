package com.example.shipmentservice.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shipmentservice.model.EstadoRepartidor;
import com.example.shipmentservice.model.Repartidor;

@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, UUID>{
	
	List<Repartidor> findByEstado(EstadoRepartidor estado);


	Optional<Repartidor> findById(UUID uuid);
}
