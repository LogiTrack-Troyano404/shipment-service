package com.example.shipmentservice.mapper;

import org.springframework.stereotype.Component;
import com.example.shipmentservice.dto.ClienteRequestDTO;
import com.example.shipmentservice.dto.ClienteResponseDTO;
import com.example.shipmentservice.model.Cliente;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClienteMapper {

	private final EnvioMapper envioMapper;

	public Cliente toEntity(ClienteRequestDTO dto) {
		Cliente cliente = new Cliente();
		cliente.setNombre(dto.nombre());
		cliente.setEmail(dto.email());
		cliente.setTelefono(dto.telefono()); // Corregido: setTelefono
		return cliente;
	}

	public ClienteResponseDTO toResponse(Cliente cliente) {
		return new ClienteResponseDTO(
				cliente.getId(),          // Corregido: agregado ()
				cliente.getNombre(),      // Corregido: agregado ()
				cliente.getEmail(),       // Corregido: agregado ()
				cliente.getTelefono(),    // Corregido: agregado ()
				cliente.getFechaCracion(),
				cliente.getFechaModificacion()
		);
	}
}