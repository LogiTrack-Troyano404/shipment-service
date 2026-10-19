package com.example.shipmentservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shipmentservice.dto.AsignarRepartidorRequestDTO;
import com.example.shipmentservice.dto.EnvioRequestDTO;
import com.example.shipmentservice.dto.EnvioResponseDTO;
import com.example.shipmentservice.mapper.EnvioMapper;
import com.example.shipmentservice.model.Envio;
import com.example.shipmentservice.model.EstadoEnvio;
import com.example.shipmentservice.service.EnvioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/envios")
@RequiredArgsConstructor
public class EnvioController {

    private final EnvioService envioService;
    private final EnvioMapper envioMapper;

    @PostMapping
    public ResponseEntity<EnvioResponseDTO> registrarEnvio(@Valid @RequestBody EnvioRequestDTO request) {
        Envio envio = envioMapper.toEntity(request);
        Envio guardado = envioService.registrarEnvio(envio, request.clienteId(), request.repartidorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(envioMapper.toResponse(guardado));
    }

    @GetMapping("/{id}")
    public EnvioResponseDTO buscarPorId(@PathVariable UUID id) {
        return envioMapper.toResponse(envioService.buscarPorId(id));
    }

    @GetMapping("/tracking/{codigo}")
    public EnvioResponseDTO buscarPorCodigoSeguimiento(@PathVariable String codigo) {
        return envioMapper.toResponse(envioService.buscarPorCodigoSeguimiento(codigo));
    }

    @GetMapping
    public List<EnvioResponseDTO> listarEnvios(@RequestParam(required = false) EstadoEnvio estado) {
        List<Envio> envios = estado != null
                ? envioService.listarEnviosPorEstados(estado)
                : envioService.listarEnvios();
        return envios.stream().map(envioMapper::toResponse).toList();
    }

    @PostMapping("/{id}/cancelar")
    public EnvioResponseDTO cancelarEnvio(@PathVariable UUID id) {
        return envioMapper.toResponse(envioService.cancelarEnvio(id));
    }

    @PostMapping("/{id}/asignar-repartidor")
    public EnvioResponseDTO asignarRepartidor(@PathVariable UUID id,
                                              @Valid @RequestBody AsignarRepartidorRequestDTO request) {
        return envioMapper.toResponse(envioService.asignarRepartidor(id, request.repartidorId()));
    }
}
