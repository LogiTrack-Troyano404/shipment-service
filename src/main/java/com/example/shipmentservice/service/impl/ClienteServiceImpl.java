package com.example.shipmentservice.service.impl;

import java.util.List;
import java.util.UUID;

import com.example.shipmentservice.Exception.ClienteNoEncontrado;
import com.example.shipmentservice.dto.ClienteRequestDTO;
import com.example.shipmentservice.model.Cliente;
import com.example.shipmentservice.repository.ClienteRepository;
import com.example.shipmentservice.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ClienteServiceImpl implements ClienteService{
	
	private final ClienteRepository clienteRepository;
	
	@Override
	@Transactional
	public Cliente registrarCliente(Cliente cliente) {
		// TODO Auto-generated method stub
		log.info("Registrando nuevo Cliente: {}",cliente.getNombre());
		Cliente saved = clienteRepository.save(cliente);
		log.info("Cliente registrado correctamente. ID: {}", saved.getId());
		return saved;
	}
	@Override
	@Transactional
	public List<Cliente> listarClientes() {
		// TODO Auto-generated method stub
		log.info("Listando todos los clientes");
		return clienteRepository.findAll();
	}
	@Override
	@Transactional
	public Cliente buscarPorId(UUID id) {
		// TODO Auto-generated method stub
		log.info("Buscando cliente por ID: {}", id);
		Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al registrar: Cliente no encontrado con ID: {}", id);
                    return new ClienteNoEncontrado("Cliente no encontrado");
                });
		return cliente;
	}
	@Override
	@Transactional
	public Cliente actualizarCliente(UUID id, ClienteRequestDTO dato) {
		// TODO Auto-generated method stub
		log.info("Actualizar cliente con ID: {}", id)
		
		Cliente cliente = buscarPorId(id);
		cliente.setNombre(dato.nombre());
		cliente.setEmail(dato.email());
		cliente.setTelefono(dato.telefono());
		
		return clienteRepository.save(cliente);
	}
	@Override
	public void eliminarCliente(UUID id) {
		// TODO Auto-generated method stub
		log.info("Eliminar cliente con ID: {}", id);
		
		Cliente cliente = buscarPorId(id);
		
		clienteRepository.delete(cliente);
		
		log.info("Cliente eliminado correctamente. ID: {}", id);
	}

	

}
