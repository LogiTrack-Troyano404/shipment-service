package com.example.shipmentservice.service;

import com.example.shipmentservice.Exception.*;
import com.example.shipmentservice.model.*;
import com.example.shipmentservice.repository.ClienteRepository;
import com.example.shipmentservice.repository.EnvioRepository;
import com.example.shipmentservice.repository.HistorialEstadoRepository;
import com.example.shipmentservice.repository.RepartidorRepository;
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
    private final RepartidorRepository repartidorRepository;
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
            log.info("Asignando repartidor con ID: {} al envío", repartidorId);

            Repartidor repartidor = repartidorRepository.findById(repartidorId)
                    .orElseThrow(() -> new RepartidorNoEncontrado("Repartidor no encontrado"));
                    log.error("Error al registrar: Repartidor no encontrado con ID: {}", repartidorId);

            if(repartidor.getEstado() != EstadoRepartidor.ACTIVO){
                log.warn("Intento de asignar repartidor inactivo (ID: {} con estado: {})", repartidorId, repartidor.getEstado());
                throw new IllegalStateException("El repatidor debe estar ACTIVO");
            }
            envio.setRepartidor(repartidor);
        }
        String codigoSeguimiento = new StringBuilder("ENV-")
                .append(UUID.randomUUID().toString(), 0, 8)
                .toString()
                .toUpperCase();

        envio.setCodigoSeguimiento(codigoSeguimiento);
        Envio saved = envioRepository.save(envio);
        registrarHistorial(saved, EstadoEnvio.PENDIENTE);

        log.info("Envio registrado correctamente. Codigo de seguimiento: {} ");
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
    @Transactional
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

    @Transactional
    private void registrarHistorial(Envio envio, EstadoEnvio estadoAnterior) {
        log.debug("Registrando auditoría de cambio de estado para envío ID: {}", envio.getId());
        HistorialEstado historial = new HistorialEstado();
        historial.setEstadoAnterior(estadoAnterior);
        historial.setFechaCambio(LocalDateTime.now());
        historial.setEnvio(envio);
        historialEstadoRepository.save(historial);
    }

    @Transactional
    private Envio cambiarEstado(Envio envio, EstadoEnvio estadoNuevo){
        EstadoEnvio estadoAnterior = envio.getEstado();
        envio.setEstado(estadoNuevo);
        Envio saved = envioRepository.save(envio);
        registrarHistorial(saved, estadoAnterior);
        return saved;
    }

}
