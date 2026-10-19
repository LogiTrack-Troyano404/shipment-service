package com.example.shipmentservice.service;

import com.example.shipmentservice.model.Envio;
import com.example.shipmentservice.model.EstadoEnvio;

import java.util.List;
import java.util.UUID;

public interface EnvioService {

    Envio registrarEnvio(Envio envio, UUID clienteId, UUID repartidorId);

    Envio buscarPorId(UUID id);

    Envio buscarPorCodigoSeguimiento(String codigo);

    List<Envio> listarEnvios();

    List<Envio> listarEnviosPorEstados(EstadoEnvio estado);

    Envio cancelarEnvio(UUID id);

    Envio actualizarEstado(UUID id, EstadoEnvio estado);

    Envio asignarRepartidor(UUID envioId, UUID repartidorId);

}
