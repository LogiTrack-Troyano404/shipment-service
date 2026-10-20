package com.example.shipmentservice.controller;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.shipmentservice.Exception.RepartidorNoEncontrado;
import com.example.shipmentservice.mapper.RepartidorMapper;
import com.example.shipmentservice.model.EstadoRepartidor;
import com.example.shipmentservice.model.Repartidor;
import com.example.shipmentservice.service.RepartidorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RepartidorController.class)
@Import(RepartidorMapper.class)
class RepartidorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RepartidorService repartidorService;

    private Repartidor repartidor;
    private UUID repartidorId;

    @BeforeEach
    void setUp() {
        repartidorId = UUID.randomUUID();

        repartidor = new Repartidor();
        repartidor.setId(repartidorId);
        repartidor.setNombre("Carlos López");
        repartidor.setTipoVehiculo("Motocicleta");
        repartidor.setEstado(EstadoRepartidor.ACTIVO);
    }

    @Test
    void registrarRepartidor_devuelve201() throws Exception {
        when(repartidorService.registrarRepartidor(any(Repartidor.class)))
                .thenReturn(repartidor);

        mockMvc.perform(post("/api/v1/repartidores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"Carlos López\", \"tipoVehiculo\": \"Motocicleta\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(repartidorId.toString()))
                .andExpect(jsonPath("$.nombre").value("Carlos López"))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    @Test
    void registrarRepartidor_sinNombre_devuelve400() throws Exception {
        mockMvc.perform(post("/api/v1/repartidores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipoVehiculo\": \"Motocicleta\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void buscarPorId_existente_devuelve200() throws Exception {
        when(repartidorService.buscarPorId(repartidorId)).thenReturn(repartidor);

        mockMvc.perform(get("/api/v1/repartidores/{id}", repartidorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Carlos López"));
    }

    @Test
    void buscarPorId_inexistente_devuelve404() throws Exception {
        when(repartidorService.buscarPorId(repartidorId))
                .thenThrow(new RepartidorNoEncontrado("Repartidor no encontrado"));

        mockMvc.perform(get("/api/v1/repartidores/{id}", repartidorId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void listarRepartidores_sinFiltro_devuelve200() throws Exception {
        when(repartidorService.listarRepartidores()).thenReturn(List.of(repartidor));

        mockMvc.perform(get("/api/v1/repartidores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void listarRepartidores_filtradoPorEstado_devuelve200() throws Exception {
        when(repartidorService.listarPorEstado(EstadoRepartidor.ACTIVO))
                .thenReturn(List.of(repartidor));

        mockMvc.perform(get("/api/v1/repartidores").param("estado", "ACTIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("ACTIVO"));
    }

    @Test
    void actualizarEstado_devuelve200() throws Exception {
        repartidor.setEstado(EstadoRepartidor.INACTIVO);
        when(repartidorService.actualizarEstado(eq(repartidorId), eq(EstadoRepartidor.INACTIVO)))
                .thenReturn(repartidor);

        mockMvc.perform(patch("/api/v1/repartidores/{id}/estado", repartidorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\": \"INACTIVO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("INACTIVO"));
    }

    @Test
    void actualizarEstado_sinEstado_devuelve400() throws Exception {
        mockMvc.perform(patch("/api/v1/repartidores/{id}/estado", repartidorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
