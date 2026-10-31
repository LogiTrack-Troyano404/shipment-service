package com.example.shipmentservice.service.impl;

import com.example.shipmentservice.client.CourierFeignClient; // Usamos el nombre correcto de la interfaz
import com.example.shipmentservice.dto.CourierResponseDTO;    // Asegúrate de que esté en este paquete o ajústalo
import com.example.shipmentservice.Exception.RepartidorNoEncontrado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl {

    private final CourierFeignClient courierClient; // Coincide con la interfaz OpenFeign

    public void procesarEnvio(UUID shipmentId, UUID repartidorId) {
        try {
            // Llamada limpia con OpenFeign
            CourierResponseDTO courier = courierClient.obtenerRepartidorPorId(repartidorId);

            // Tu lógica de negocio con el courier...

        } catch (feign.FeignException.NotFound e) {
            // Mapeamos el 404 de OpenFeign a tu excepción de negocio existente
            throw new RepartidorNoEncontrado("Repartidor no encontrado con ID: " + repartidorId);
        } catch (feign.FeignException e) {
            // Cualquier otro error de comunicación con el microservicio
            throw new RuntimeException("Error al comunicarse con el servicio de repartidores");
        }
    }
}