package com.example.shipmentservice.service.impl;

import com.example.shipmentservice.Exception.RepartidorNoEncontrado;
import com.example.shipmentservice.model.EstadoRepartidor;
import com.example.shipmentservice.model.Repartidor;
import com.example.shipmentservice.repository.RepartidorRepository;
import com.example.shipmentservice.service.RepartidorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class RepartidorServiceImpl implements RepartidorService {

    private final RepartidorRepository repartidorRepository;

    @Override
    @Transactional
    public Repartidor registrarRepartidor(Repartidor repartidor) {
        log.info("Iniciando registro de nuevo repartidor: {}", repartidor.getNombre());
        if (repartidor.getEstado() == null) {
            repartidor.setEstado(EstadoRepartidor.ACTIVO);
        }
        Repartidor saved = repartidorRepository.save(repartidor);
        log.info("Repartidor registrado exitosamente con ID: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Repartidor buscarPorId(UUID id) {
        log.info("Buscando repartidor por ID: {}", id);
        return repartidorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Repartidor no encontrado con el ID: {}", id);
                    return new RepartidorNoEncontrado("Repartidor no encontrado con ID: " + id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Repartidor> listarRepartidores() {
        log.info("Listando todos los repartidores registrados");
        return repartidorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Repartidor> listarPorEstado(EstadoRepartidor estado) {
        log.info("Listando repartidores filtrados por estado: {}", estado);
        return repartidorRepository.findByEstado(estado);
    }

    @Override
    @Transactional
    public Repartidor actualizarEstado(UUID id, EstadoRepartidor nuevoEstado) {
        log.info("Actualizando estado del repartidor ID: {} a un nuevo estado: {}", id, nuevoEstado);

        Repartidor repartidor = buscarPorId(id);

        if (repartidor.getEstado() == nuevoEstado) {
            log.warn("El repartidor ID: {} ya se encuentra en el estado: {}", id, nuevoEstado);
        }

        repartidor.setEstado(nuevoEstado);
        Repartidor actualizado = repartidorRepository.save(repartidor);

        log.info("Estado del repartidor ID: {} actualizado exitosamente a: {}", id, nuevoEstado);
        return actualizado;
    }
}
