package com.example.shipmentservice.service.impl;

import com.example.shipmentservice.Exception.*;
import com.example.shipmentservice.model.*;
import com.example.shipmentservice.service.EnvioService;
import com.example.shipmentservice.repository.ClienteRepository;
import com.example.shipmentservice.repository.EnvioRepository;
import com.example.shipmentservice.repository.HistorialEstadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class EnvioServiceImpl  implements  EnvioService{

    private final EnvioRepository envioRepository;
    private final ClienteRepository clienteRepository;
    private final HistorialEstadoRepository historialEstadoRepository;

    @Override
    @Transactional
    public Envio registrarEnvio(Envio envio, UUID clienteId, UUID repartidorId) {
        log.info("Iniciando registro de nuevo envío para el cliente ID: {}", clienteId);

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> {
                    log.error("Error al registrar: Cliente no encontrado con ID: {}", clienteId);
                    return new ClienteNoEncontrado("Cliente no encontrado");
                });
        envio.setCliente(cliente);

        if(repartidorId != null){
            log.info("Validacion y asignacion de repartidor ID: {} para el envio", repartidorId);

            envio.setRepartidorId(repartidorId);
        }
        String codigoSeguimiento = new StringBuilder("ENV-")
                .append(UUID.randomUUID().toString(), 0, 8)
                .toString()
                .toUpperCase();

        envio.setCodigoSeguimiento(codigoSeguimiento);
        envio.setEstado(EstadoEnvio.PENDIENTE);
        Envio saved = envioRepository.save(envio);
        registrarHistorial(saved, EstadoEnvio.PENDIENTE);

        log.info("Envio registrado correctamente. Codigo de seguimiento: {}", codigoSeguimiento);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Envio buscarPorId(UUID id){
        log.info("Buscando envio por ID: {}", id);
        return envioRepository.findById(id)
             .orElseThrow(() -> {
                 log.warn("Envio no encontrado con el ID: {}", id);
                 return new EnvioNoEncontrado("Envio no encontrado");
             });
    }

    @Override
    @Transactional
    public Envio buscarPorCodigoSeguimiento(String codigo){
        log.info("Buscando envio por su codigo de seguimiento: {} ", codigo);
        return envioRepository.findByCodigoSeguimiento(codigo)
                .orElseThrow(() -> {
                    log.warn("Envio no encontrado con codigo: {} ", codigo);
                    return new EnvioNoEncontrado("Envio no encontrado con su codigo");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Envio> listarEnvios(){
        log.info("Listando todos los envios registrados");
        return envioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Envio> listarEnviosPorEstados(EstadoEnvio estado){
        log.info("Listando envios filtrados por estado: {}", estado);
        return envioRepository.findByEstado(estado);
    }

    @Override
    @Transactional
    public Envio cancelarEnvio(UUID id){
        log.info("Solicitud de cancelacion para el envio ID: {}", id);
        Envio envio = buscarPorId(id);

        if(envio.getEstado() != EstadoEnvio.PENDIENTE){
            log.warn("No se puede cancelar el envio con el ID {} por su estado {}.",id, envio.getEstado());
            throw new CancelacionDeEnvio("El envio solo se puede cancerla si se esta en estado pendiente");
        }

        Envio cancelado = cambiarEstado(envio, EstadoEnvio.CANCELADO);
        log.info("Envio ID: {} cancelado correctamente", id);
        return cancelado;
    }

    @Override
    @Transactional
    public Envio actualizarEstado(UUID id, EstadoEnvio nuevoEstado){
        log.info("Actualizacion de estado para el envio con id: {} con el estado: {} ", id, nuevoEstado);
        Envio envio = buscarPorId(id);

        if (envio.getEstado() == EstadoEnvio.CANCELADO || envio.getEstado() == EstadoEnvio.ENTREGADO) {
            log.warn("Intento inválido de actualizar el envío ID: {}. El envío ya se encuentra como:  {}", id, envio.getEstado());
            throw new ModificacionDeEstado("No se puede modificar un envío que ya se encuentra " + envio.getEstado());
        }

        Envio actualizado = cambiarEstado(envio, nuevoEstado);
        log.info("Estado del envío ID: {} actualizado exitosamente a: {}", id, nuevoEstado);
        return actualizado;
    }

    @Override
    @Transactional
    public Envio asignarRepartidor(UUID envioId, UUID repartidorId) {
        log.info("Solicitud de asignación del repartidor ID: {} al envío ID: {}", repartidorId, envioId);

        Envio envio = buscarPorId(envioId);

        if (envio.getEstado() != EstadoEnvio.PENDIENTE) {
            log.warn("No se puede asignar repartidor al envío ID: {} porque su estado es {}", envioId, envio.getEstado());
            throw new AsignacionDeRepartidor("El repartidor solo puede asignarse mientras el envío está PENDIENTE");
        }

        envio.setRepartidorId(repartidorId);
        Envio actualizado = envioRepository.save(envio);
        log.info("Repartidor ID: {} asignado correctamente al envío ID: {}", repartidorId, envioId);
        return actualizado;
    }

    private void registrarHistorial(Envio envio, EstadoEnvio estadoAnterior) {
        log.debug("Registrando auditoría de cambio de estado para envío ID: {}", envio.getId());
        HistorialEstado historial = new HistorialEstado();
        historial.setEstadoAnterior(estadoAnterior);
        historial.setFechaCambio(LocalDateTime.now());
        historial.setEnvio(envio);
        historialEstadoRepository.save(historial);
    }

    private Envio cambiarEstado(Envio envio, EstadoEnvio estadoNuevo){
        EstadoEnvio estadoAnterior = envio.getEstado();
        envio.setEstado(estadoNuevo);
        Envio saved = envioRepository.save(envio);
        registrarHistorial(saved, estadoAnterior);
        return saved;
    }

}
