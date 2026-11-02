package com.example.shipmentservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shipmentservice.dto.ClienteRequestDTO;
import com.example.shipmentservice.dto.ClienteResponseDTO;
import com.example.shipmentservice.mapper.ClienteMapper;
import com.example.shipmentservice.model.Cliente;
import com.example.shipmentservice.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Tag(name = "clientes", description = "Gestión de clientes")
public class ClienteController {
	
	private final ClienteService clienteService;
	private final ClienteMapper clienteMapper;
	
	@PostMapping
    @Operation(summary = "Registrar un cliente")
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@Valid @RequestBody ClienteRequestDTO dto) {

        Cliente cliente = clienteMapper.toEntity(dto);
        Cliente registrado = clienteService.registrarCliente(cliente);

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteMapper.toResponse(registrado));
    }
	
	@GetMapping
    @Operation(summary = "Listar todos los clientes")
    public ResponseEntity<List<ClienteResponseDTO>> listarClientes() {

        List<ClienteResponseDTO> clientes = clienteService.listarClientes()
                .stream()
                .map(clienteMapper::toResponse)
                .toList();

        return ResponseEntity.ok(clientes);
    }
	
	@GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable UUID id) {

        Cliente cliente = clienteService.buscarPorId(id);

        return ResponseEntity.ok(clienteMapper.toResponse(cliente));
    }
	
	@PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente")
    public ResponseEntity<ClienteResponseDTO> actualizarCliente(
            @PathVariable UUID id,
            @Valid @RequestBody ClienteRequestDTO dto) {

        Cliente actualizado = clienteService.actualizarCliente(id, dto);

        return ResponseEntity.ok(clienteMapper.toResponse(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente")
    public ResponseEntity<Void> eliminarCliente(@PathVariable UUID id) {

        clienteService.eliminarCliente(id);

        return ResponseEntity.noContent().build();
    }
}


}
