package com.example.shipmentservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shipmentservice.dto.CambiarEstadoRepartidorDTO;
import com.example.shipmentservice.dto.RepartidorRequestDTO;
import com.example.shipmentservice.dto.RepartidorResponseDTO;
import com.example.shipmentservice.mapper.RepartidorMapper;
import com.example.shipmentservice.model.EstadoRepartidor;
import com.example.shipmentservice.model.Repartidor;
import com.example.shipmentservice.service.RepartidorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/repartidores")
@RequiredArgsConstructor
@Tag(name = "Repartidores", description = "Gestión de repartidores y consulta de disponibilidad")
public class RepartidorController {

    private final RepartidorService repartidorService;
    private final RepartidorMapper repartidorMapper;

    @PostMapping
    @Operation(summary = "Registrar un nuevo repartidor")
    public ResponseEntity<RepartidorResponseDTO> registrarRepartidor(@Valid @RequestBody RepartidorRequestDTO request) {
        Repartidor repartidor = repartidorMapper.toEntity(request);
        Repartidor guardado = repartidorService.registrarRepartidor(repartidor);
        return ResponseEntity.status(HttpStatus.CREATED).body(repartidorMapper.toResponse(guardado));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un repartidor por su ID")
    public RepartidorResponseDTO buscarPorId(@PathVariable UUID id) {
        return repartidorMapper.toResponse(repartidorService.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar repartidores, opcionalmente filtrados por estado")
    public List<RepartidorResponseDTO> listarRepartidores(@RequestParam(required = false) EstadoRepartidor estado) {
        List<Repartidor> repartidores = estado != null
                ? repartidorService.listarPorEstado(estado)
                : repartidorService.listarRepartidores();
        return repartidores.stream().map(repartidorMapper::toResponse).toList();
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar el estado de un repartidor (ACTIVO/INACTIVO)")
    public RepartidorResponseDTO actualizarEstado(@PathVariable UUID id,
                                                  @Valid @RequestBody CambiarEstadoRepartidorDTO request) {
        return repartidorMapper.toResponse(repartidorService.actualizarEstado(id, request.estado()));
    }
}
