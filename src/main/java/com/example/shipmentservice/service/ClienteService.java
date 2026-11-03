package com.example.shipmentservice.service;

import java.util.List;
import java.util.UUID;

import com.example.shipmentservice.dto.ClienteRequestDTO;
import com.example.shipmentservice.model.Cliente;

public interface ClienteService {
	
	Cliente registrarCliente(Cliente cliente);

    List<Cliente> listarClientes();

    Cliente buscarPorId(UUID id);

    Cliente actualizarCliente(UUID id, ClienteRequestDTO cliente);

    void eliminarCliente(UUID id);

}
