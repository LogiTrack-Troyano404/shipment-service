package com.example.shipmentservice.mapper;

import org.springframework.stereotype.Component;
import com.example.shipmentservice.dto.ClienteRequestDTO;
import com.example.shipmentservice.dto.ClienteResponseDTO;
import com.example.shipmentservice.model.Cliente;
import com.example.shipmentservice.mapper.EnvioMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClienteMapper {
	
	private final EnvioMapper envioMapper;
	
	public Cliente toEntity(ClienteRequestDTO dto) {
		
		Cliente cliente = new Cliente();
		cliente.setNombre(dto.nombre());
		cliente.setEmail(dto.email());
		cliente.setTelofono(dto.telefono());
		return cliente;
		
	}
	
	public ClienteResponseDTO toResponse(Cliente cliente) {
		return new ClienteResponseDTO(
				cliente.getId,
				cliente.getNombre,
				cliente.getEmail,
				cliente.getTelefono,
				cliente.getFechaCracion(),
                cliente.getFechaModificacion()
				);
	}
	
}
