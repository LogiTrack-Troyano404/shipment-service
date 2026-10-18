package com.example.shipmentservice.service;

import com.example.shipmentservice.model.EstadoRepartidor;
import com.example.shipmentservice.model.Repartidor;

import java.util.List;
import java.util.UUID;

public interface RepartidorService {

    Repartidor registrarRepartidor(Repartidor repartidor);

    Repartidor buscarPorId(UUID id);

    List<Repartidor> listarRepartidores();

    List<Repartidor> listarPorEstado(EstadoRepartidor estado);

    Repartidor actualizarEstado(UUID id, EstadoRepartidor nuevoEstado);
}