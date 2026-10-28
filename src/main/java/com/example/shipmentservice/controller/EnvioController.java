package com.example.shipmentservice.controller;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/envios")
@RequiredArgsConstructor
@Tag(name = "Envíos", description = "Gestión del ciclo de vida de envíos")
@SecurityRequirement(name = "bearer-jwt")
public class EnvioController {

    private final EnvioService envioService;
    private final EnvioMapper envioMapper;

    @PostMapping
    @Operation(summary = "Registrar un nuevo envío")
    public ResponseEntity<EnvioResponseDTO> registrarEnvio(@Valid @RequestBody EnvioRequestDTO request) {
        Envio envio = envioMapper.toEntity(request);
        Envio guardado = envioService.registrarEnvio(envio, request.clienteId(), request.repartidorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(envioMapper.toResponse(guardado));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un envío por su ID")
    public EnvioResponseDTO buscarPorId(@PathVariable UUID id) {
        return envioMapper.toResponse(envioService.buscarPorId(id));
    }

    @GetMapping("/tracking/{codigo}")
    @Operation(summary = "Consultar un envío por su código de seguimiento")
    public EnvioResponseDTO buscarPorCodigoSeguimiento(@PathVariable String codigo) {
        return envioMapper.toResponse(envioService.buscarPorCodigoSeguimiento(codigo));
    }

    @GetMapping
    @Operation(summary = "Listar envíos, opcionalmente filtrados por estado")
    public List<EnvioResponseDTO> listarEnvios(@RequestParam(required = false) EstadoEnvio estado) {
        List<Envio> envios = estado != null
                ? envioService.listarEnviosPorEstados(estado)
                : envioService.listarEnvios();
        return envios.stream().map(envioMapper::toResponse).toList();
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar un envío (solo si está PENDIENTE)")
    public EnvioResponseDTO cancelarEnvio(@PathVariable UUID id) {
        return envioMapper.toResponse(envioService.cancelarEnvio(id));
    }

    @PostMapping("/{id}/asignar-repartidor")
    @Operation(summary = "Asignar un repartidor activo a un envío PENDIENTE")
    public EnvioResponseDTO asignarRepartidor(@PathVariable UUID id,
                                              @Valid @RequestBody AsignarRepartidorRequestDTO request) {
        return envioMapper.toResponse(envioService.asignarRepartidor(id, request.repartidorId()));
    }
}
