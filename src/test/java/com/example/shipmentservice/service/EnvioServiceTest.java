package com.example.shipmentservice.service;

import com.example.shipmentservice.service.impl.EnvioServiceImpl;
import com.example.shipmentservice.client.CourierClient;
import com.example.shipmentservice.dto.CourierResponseDTO;
import com.example.shipmentservice.Exception.AsignacionDeRepartidor;
import com.example.shipmentservice.Exception.CancelacionDeEnvio;
import com.example.shipmentservice.Exception.RepartidorNoEncontrado;
import com.example.shipmentservice.model.*;
import com.example.shipmentservice.repository.ClienteRepository;
import com.example.shipmentservice.repository.EnvioRepository;
import com.example.shipmentservice.repository.HistorialEstadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private HistorialEstadoRepository historialEstadoRepository;
    @Mock
    private CourierClient courierClient;

    @InjectMocks
    private EnvioServiceImpl envioService;

    private Envio envio;
    private Cliente cliente;
    private UUID envioId;
    private UUID repartidorId;

    @BeforeEach
    void setUp() {
        envioId = UUID.randomUUID();
        repartidorId = UUID.randomUUID();

        cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan@test.com");

        Direccion origen = new Direccion("Belgrano", "123", "San Salvador de Jujuy", "4600");
        Direccion destino = new Direccion("Ituzaingó", "456", "San Salvador de Jujuy", "4600");

        envio = new Envio();
        envio.setId(envioId);
        envio.setPeso(2.0);
        envio.setAncho(10.0);
        envio.setAlto(10.0);
        envio.setOrigen(origen);
        envio.setDestino(destino);
        envio.setEstado(EstadoEnvio.PENDIENTE);
        envio.setCliente(cliente);
    }

    @Test
    void testRegistrarEnvioExitoso() {
        UUID clienteId = cliente.getId();
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(envioRepository.save(any(Envio.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(historialEstadoRepository.save(any(HistorialEstado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Envio resultado = envioService.registrarEnvio(envio, clienteId, null);

        assertNotNull(resultado);
        assertEquals(EstadoEnvio.PENDIENTE, resultado.getEstado());
        assertNotNull(resultado.getCodigoSeguimiento());
        verify(envioRepository, times(1)).save(any(Envio.class));
        verify(historialEstadoRepository, times(1)).save(any(HistorialEstado.class));
    }

    @Test
    void buscarPorId(){
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envio));

        Envio resultado = envioService.buscarPorId(envioId);

        assertNotNull(resultado);
        assertEquals(envioId, resultado.getId());
        verify(envioRepository, times(1)).findById(envioId);
    }

    @Test
    void buscarPorCodigoSeguimiento(){
        when(envioRepository.findByCodigoSeguimiento(envio.getCodigoSeguimiento())).thenReturn(Optional.of(envio));

        Envio resultado = envioService.buscarPorCodigoSeguimiento(envio.getCodigoSeguimiento());
        assertNotNull(resultado);
        assertEquals(envio.getCodigoSeguimiento(), resultado.getCodigoSeguimiento());
        verify(envioRepository, times(1)).findByCodigoSeguimiento(envio.getCodigoSeguimiento());
    }

    @Test
    void listarEnviosPorEstado(){
        EstadoEnvio estado = EstadoEnvio.PENDIENTE;
        when(envioRepository.findByEstado(estado)).thenReturn(List.of(envio));

        List<Envio> resultados = envioService.listarEnviosPorEstados(estado);

        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.size());
        assertEquals(estado, resultados.get(0).getEstado());
        verify(envioRepository, times(1)).findByEstado(estado);
    }

    @Test
    void CancelarEnvioExitoso() {
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envio));
        when(envioRepository.save(any(Envio.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(historialEstadoRepository.save(any(HistorialEstado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Envio resultado = envioService.cancelarEnvio(envioId);

        assertNotNull(resultado);
        assertEquals(EstadoEnvio.CANCELADO, resultado.getEstado());
        verify(envioRepository, times(1)).save(envio);
        verify(historialEstadoRepository, times(1)).save(any(HistorialEstado.class));
    }

    @Test
    void CancelarEnvioFallaSiNoEstaPendiente() {
        envio.setEstado(EstadoEnvio.EN_RUTA);
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envio));

        assertThrows(CancelacionDeEnvio.class, () -> {
            envioService.cancelarEnvio(envioId);
        });

        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    void asignarRepartidorExitoso() {
        CourierResponseDTO courierDto = new CourierResponseDTO(repartidorId, "Carlos Gómez", "Motocicleta", "ACTIVO");

        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envio));
        when(courierClient.obtenerRepartidorPorId(repartidorId)).thenReturn(courierDto);
        when(envioRepository.save(any(Envio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Envio resultado = envioService.asignarRepartidor(envioId, repartidorId);

        assertNotNull(resultado);
        assertEquals(repartidorId, resultado.getRepartidorId());
        verify(envioRepository, times(1)).save(envio);
        verify(courierClient, times(1)).obtenerRepartidorPorId(repartidorId);
    }

    @Test
    void asignarRepartidorFallaSiEnvioNoEstaPendiente() {
        envio.setEstado(EstadoEnvio.EN_RUTA);
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envio));

        assertThrows(AsignacionDeRepartidor.class, () -> {
            envioService.asignarRepartidor(envioId, repartidorId);
        });

        verify(courierClient, never()).obtenerRepartidorPorId(any(UUID.class));
        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    void asignarRepartidorFallaSiRepartidorNoExiste() {
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envio));
        // Simulamos que el cliente HTTP lanza la excepción porque el courier-service devolvió 404
        when(courierClient.obtenerRepartidorPorId(repartidorId))
                .thenThrow(new RepartidorNoEncontrado("Repartidor no encontrado"));

        assertThrows(RepartidorNoEncontrado.class, () -> {
            envioService.asignarRepartidor(envioId, repartidorId);
        });

        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    void asignarRepartidorFallaSiRepartidorInactivo() {
        CourierResponseDTO courierDto = new CourierResponseDTO(repartidorId, "Carlos Gómez", "Motocicleta", "INACTIVO");

        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envio));
        when(courierClient.obtenerRepartidorPorId(repartidorId)).thenReturn(courierDto);

        assertThrows(AsignacionDeRepartidor.class, () -> {
            envioService.asignarRepartidor(envioId, repartidorId);
        });

        verify(envioRepository, never()).save(any(Envio.class));
    }
}