package com.example.shipmentservice.mapper;

import org.springframework.stereotype.Component;

import com.example.shipmentservice.dto.DireccionDTO;
import com.example.shipmentservice.dto.EnvioRequestDTO;
import com.example.shipmentservice.dto.EnvioResponseDTO;
import com.example.shipmentservice.model.Direccion;
import com.example.shipmentservice.model.Envio;

@Component
public class EnvioMapper {

    public Envio toEntity(EnvioRequestDTO dto) {
        Envio envio = new Envio();
        envio.setOrigen(toEntity(dto.origen()));
        envio.setDestino(toEntity(dto.destino()));
        envio.setPeso(dto.peso());
        envio.setAncho(dto.ancho());
        envio.setAlto(dto.alto());
        envio.setTarifa(dto.tarifa());
        return envio;
    }

    public EnvioResponseDTO toResponse(Envio envio) {
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
                envio.getRepartidor() != null ? envio.getRepartidor().getId() : null,
                envio.getRepartidor() != null ? envio.getRepartidor().getNombre() : null,
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
