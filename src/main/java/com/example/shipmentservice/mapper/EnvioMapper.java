package com.example.shipmentservice.mapper;

import org.springframework.stereotype.Component;

import com.example.shipmentservice.client.CourierClient;
import com.example.shipmentservice.dto.DireccionDTO;
import com.example.shipmentservice.dto.EnvioRequestDTO;
import com.example.shipmentservice.dto.EnvioResponseDTO;
import com.example.shipmentservice.model.Direccion;
import com.example.shipmentservice.model.Envio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnvioMapper {

    private final CourierClient courierClient;

    public Envio toEntity(EnvioRequestDTO dto) {
        Envio envio = new Envio();
        envio.setOrigen(toEntity(dto.origen()));
        envio.setDestino(toEntity(dto.destino()));
        envio.setPeso(dto.peso());
        envio.setAncho(dto.ancho());
        envio.setAlto(dto.alto());
        envio.setTarifa(dto.tarifa());
        envio.setRepartidorId(dto.repartidorId());
        return envio;
    }

    public EnvioResponseDTO toResponse(Envio envio) {
        String repartidorNombre = null;

        // Si el envío tiene un repartidor asignado, consultamos su nombre mediante el Client
        if (envio.getRepartidorId() != null) {
            try {
                var courierDto = courierClient.obtenerRepartidorPorId(envio.getRepartidorId());
                if (courierDto != null) {
                    repartidorNombre = courierDto.nombre();
                }
            } catch (Exception e) {
                log.warn("No se pudo obtener el nombre del repartidor con ID: {}. Error: {}",
                        envio.getRepartidorId(), e.getMessage());
                repartidorNombre = "Repartidor (ID desincronizado)";
            }
        }

        return new EnvioResponseDTO(
                envio.getId(),
                envio.getCodigoSeguimiento(),
                envio.getPeso(),
                envio.getAncho(),
                envio.getAlto(),
                envio.getEstado(),
                envio.getTarifa(),
                toDTO(envio.getOrigen()),
                toDTO(envio.getDestino()),
                envio.getCliente() != null ? envio.getCliente().getId() : null,
                envio.getCliente() != null ? envio.getCliente().getNombre() : null,
                envio.getRepartidorId(),
                repartidorNombre, // <- Aquí ya va el nombre obtenido dinámicamente
                envio.getFechaCracion(),
                envio.getFechaModificacion());
    }

    private Direccion toEntity(DireccionDTO dto) {
        return new Direccion(dto.calle(), dto.numero(), dto.ciudad(), dto.codigoPostal());
    }

    private DireccionDTO toDTO(Direccion direccion) {
        return new DireccionDTO(
                direccion.getCalle(),
                direccion.getNumero(),
                direccion.getCiudad(),
                direccion.getCodigoPostal());
    }
}