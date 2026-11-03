package com.example.shipmentservice.client;

import com.example.shipmentservice.dto.CourierResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

// Añadimos el atributo fallback con la clase que acabamos de crear
@FeignClient(name = "courier-service", path = "/api/v1/repartidores", fallback = CourierFeignClientFallback.class)
public interface CourierFeignClient {

    @GetMapping("/{id}")
    CourierResponseDTO obtenerRepartidorPorId(@PathVariable("id") UUID repartidorId);

}