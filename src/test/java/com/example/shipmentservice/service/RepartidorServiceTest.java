package com.example.shipmentservice.service;

import com.example.shipmentservice.service.impl.RepartidorServiceImpl;

import com.example.shipmentservice.Exception.RepartidorNoEncontrado;
import com.example.shipmentservice.model.EstadoRepartidor;
import com.example.shipmentservice.model.Repartidor;
import com.example.shipmentservice.repository.RepartidorRepository;
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
class RepartidorServiceTest {

    @Mock
    private RepartidorRepository repartidorRepository;

    @InjectMocks
    private RepartidorServiceImpl repartidorService;

    private Repartidor repartidor;
    private UUID repartidorId;

    @BeforeEach
    void setUp() {
        repartidorId = UUID.randomUUID();

        repartidor = new Repartidor();
        repartidor.setId(repartidorId);
        repartidor.setNombre("Carlos Gómez");
        repartidor.setTipoVehiculo("Auto");
        repartidor.setEstado(EstadoRepartidor.ACTIVO);
    }

    @Test
    void testRegistrarRepartidorExitoso() {
        when(repartidorRepository.save(any(Repartidor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Repartidor resultado = repartidorService.registrarRepartidor(repartidor);

        assertNotNull(resultado);
        assertEquals("Carlos Gómez", resultado.getNombre());
        assertEquals(EstadoRepartidor.ACTIVO, resultado.getEstado());
        verify(repartidorRepository, times(1)).save(any(Repartidor.class));
    }

    @Test
    void testRegistrarRepartidorSinEstadoAsignaActivoPorDefecto() {
        repartidor.setEstado(EstadoRepartidor.INACTIVO); // Simulamos que llega sin estado o nulo en algunas pruebas
        // Probamos cuando el estado es null explícitamente:
        repartidor.setEstado(null);

        when(repartidorRepository.save(any(Repartidor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Repartidor resultado = repartidorService.registrarRepartidor(repartidor);

        assertNotNull(resultado);
        assertEquals(EstadoRepartidor.ACTIVO, resultado.getEstado()); // Verifica que la lógica por defecto funcione
        verify(repartidorRepository, times(1)).save(any(Repartidor.class));
    }

    @Test
    void testBuscarPorIdExitoso() {
        when(repartidorRepository.findById(repartidorId)).thenReturn(Optional.of(repartidor));

        Repartidor resultado = repartidorService.buscarPorId(repartidorId);

        assertNotNull(resultado);
        assertEquals(repartidorId, resultado.getId());
        verify(repartidorRepository, times(1)).findById(repartidorId);
    }

    @Test
    void testBuscarPorIdNoEncontradoLanzaExcepcion() {
        when(repartidorRepository.findById(repartidorId)).thenReturn(Optional.empty());

        assertThrows(RepartidorNoEncontrado.class, () -> {
            repartidorService.buscarPorId(repartidorId);
        });

        verify(repartidorRepository, times(1)).findById(repartidorId);
    }

    @Test
    void testListarRepartidores() {
        when(repartidorRepository.findAll()).thenReturn(List.of(repartidor));

        List<Repartidor> resultados = repartidorService.listarRepartidores();

        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.size());
        verify(repartidorRepository, times(1)).findAll();
    }

    @Test
    void testListarPorEstado() {
        EstadoRepartidor estado = EstadoRepartidor.ACTIVO;
        when(repartidorRepository.findByEstado(estado)).thenReturn(List.of(repartidor));

        List<Repartidor> resultados = repartidorService.listarPorEstado(estado);

        assertFalse(resultados.isEmpty());
        assertEquals(estado, resultados.get(0).getEstado());
        verify(repartidorRepository, times(1)).findByEstado(estado);
    }

    @Test
    void testActualizarEstadoExitoso() {
        EstadoRepartidor nuevoEstado = EstadoRepartidor.INACTIVO;

        when(repartidorRepository.findById(repartidorId)).thenReturn(Optional.of(repartidor));
        when(repartidorRepository.save(any(Repartidor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Repartidor resultado = repartidorService.actualizarEstado(repartidorId, nuevoEstado);

        assertNotNull(resultado);
        assertEquals(nuevoEstado, resultado.getEstado());
        verify(repartidorRepository, times(1)).findById(repartidorId);
        verify(repartidorRepository, times(1)).save(repartidor);
    }
}