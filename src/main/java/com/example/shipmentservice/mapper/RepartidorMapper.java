package com.example.shipmentservice.mapper;

import org.springframework.stereotype.Component;

import com.example.shipmentservice.dto.RepartidorRequestDTO;
import com.example.shipmentservice.dto.RepartidorResponseDTO;
import com.example.shipmentservice.model.Repartidor;

@Component
public class RepartidorMapper {

    public Repartidor toEntity(RepartidorRequestDTO dto) {
        Repartidor repartidor = new Repartidor();
        repartidor.setNombre(dto.nombre());
        repartidor.setTipoVehiculo(dto.tipoVehiculo());
        repartidor.setEstado(dto.estado());
        return repartidor;
    }

    public RepartidorResponseDTO toResponse(Repartidor repartidor) {
        return new RepartidorResponseDTO(
                repartidor.getId(),
                repartidor.getNombre(),
                repartidor.getTipoVehiculo(),
                repartidor.getEstado(),
                repartidor.getFechaCracion(),
                repartidor.getFechaModificacion());
    }
}
