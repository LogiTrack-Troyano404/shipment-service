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

import com.example.shipmentservice.Exception.CancelacionDeEnvio;
import com.example.shipmentservice.Exception.EnvioNoEncontrado;
import com.example.shipmentservice.Exception.AsignacionDeRepartidor;
import com.example.shipmentservice.mapper.EnvioMapper;
import com.example.shipmentservice.model.Cliente;
import com.example.shipmentservice.model.Direccion;
import com.example.shipmentservice.model.Envio;
import com.example.shipmentservice.model.EstadoEnvio;
import com.example.shipmentservice.service.EnvioService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnvioController.class)
@Import(EnvioMapper.class)
class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnvioService envioService;

    private Envio envio;
    private UUID envioId;
    private UUID clienteId;

    @BeforeEach
    void setUp() {
        envioId = UUID.randomUUID();
        clienteId = UUID.randomUUID();

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setNombre("Juan Pérez");

        envio = new Envio();
        envio.setId(envioId);
        envio.setCodigoSeguimiento("ENV-TEST1234");
        envio.setEstado(EstadoEnvio.PENDIENTE);
        envio.setPeso(2.0);
        envio.setAncho(10.0);
        envio.setAlto(5.0);
        envio.setTarifa(1500.0);
        envio.setOrigen(new Direccion("Belgrano", "123", "Jujuy", "4600"));
        envio.setDestino(new Direccion("Ituzaingó", "456", "Jujuy", "4600"));
        envio.setCliente(cliente);
    }

    private String requestBodyValido() {
        return """
                {
                  "clienteId": "%s",
                  "origen": {"calle": "Belgrano", "numero": "123", "ciudad": "Jujuy", "codigoPostal": "4600"},
                  "destino": {"calle": "Ituzaingó", "numero": "456", "ciudad": "Jujuy", "codigoPostal": "4600"},
                  "peso": 2.0,
                  "ancho": 10.0,
                  "alto": 5.0,
                  "tarifa": 1500.0
                }
                """.formatted(clienteId);
    }

    @Test
    void registrarEnvio_devuelve201() throws Exception {
        when(envioService.registrarEnvio(any(Envio.class), eq(clienteId), isNull()))
                .thenReturn(envio);

        mockMvc.perform(post("/api/v1/envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyValido()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(envioId.toString()))
                .andExpect(jsonPath("$.codigoSeguimiento").value("ENV-TEST1234"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.clienteNombre").value("Juan Pérez"));
    }

    @Test
    void registrarEnvio_sinCliente_devuelve400() throws Exception {
        String bodySinCliente = """
                {
                  "origen": {"calle": "Belgrano", "numero": "123", "ciudad": "Jujuy", "codigoPostal": "4600"},
                  "destino": {"calle": "Ituzaingó", "numero": "456", "ciudad": "Jujuy", "codigoPostal": "4600"},
                  "peso": 2.0,
                  "ancho": 10.0,
                  "alto": 5.0,
                  "tarifa": 1500.0
                }
                """;

        mockMvc.perform(post("/api/v1/envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodySinCliente))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").isString());
    }

    @Test
    void buscarPorId_existente_devuelve200() throws Exception {
        when(envioService.buscarPorId(envioId)).thenReturn(envio);

        mockMvc.perform(get("/api/v1/envios/{id}", envioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(envioId.toString()))
                .andExpect(jsonPath("$.codigoSeguimiento").value("ENV-TEST1234"));
    }

    @Test
    void buscarPorId_inexistente_devuelve404() throws Exception {
        when(envioService.buscarPorId(envioId))
                .thenThrow(new EnvioNoEncontrado("Envio no encontrado"));

        mockMvc.perform(get("/api/v1/envios/{id}", envioId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/v1/envios/" + envioId));
    }

    @Test
    void buscarPorCodigoSeguimiento_devuelve200() throws Exception {
        when(envioService.buscarPorCodigoSeguimiento("ENV-TEST1234")).thenReturn(envio);

        mockMvc.perform(get("/api/v1/envios/tracking/{codigo}", "ENV-TEST1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoSeguimiento").value("ENV-TEST1234"));
    }

    @Test
    void listarEnvios_filtradoPorEstado_devuelve200() throws Exception {
        when(envioService.listarEnviosPorEstados(EstadoEnvio.PENDIENTE))
                .thenReturn(List.of(envio));

        mockMvc.perform(get("/api/v1/envios").param("estado", "PENDIENTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(envioId.toString()));
    }

    @Test
    void listarEnvios_sinFiltro_devuelve200() throws Exception {
        when(envioService.listarEnvios()).thenReturn(List.of(envio));

        mockMvc.perform(get("/api/v1/envios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void cancelarEnvio_devuelve200() throws Exception {
        envio.setEstado(EstadoEnvio.CANCELADO);
        when(envioService.cancelarEnvio(envioId)).thenReturn(envio);

        mockMvc.perform(post("/api/v1/envios/{id}/cancelar", envioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CANCELADO"));
    }

    @Test
    void cancelarEnvio_enEstadoInvalido_devuelve422() throws Exception {
        when(envioService.cancelarEnvio(envioId))
                .thenThrow(new CancelacionDeEnvio("Solo se puede cancelar en PENDIENTE"));

        mockMvc.perform(post("/api/v1/envios/{id}/cancelar", envioId))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422));
    }

    @Test
    void asignarRepartidor_devuelve200() throws Exception {
        UUID repartidorId = UUID.randomUUID();
        when(envioService.asignarRepartidor(envioId, repartidorId)).thenReturn(envio);

        mockMvc.perform(post("/api/v1/envios/{id}/asignar-repartidor", envioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"repartidorId\": \"%s\"}".formatted(repartidorId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(envioId.toString()));
    }

    @Test
    void asignarRepartidor_inactivo_devuelve422() throws Exception {
        UUID repartidorId = UUID.randomUUID();
        when(envioService.asignarRepartidor(envioId, repartidorId))
                .thenThrow(new AsignacionDeRepartidor("El repartidor debe estar ACTIVO"));

        mockMvc.perform(post("/api/v1/envios/{id}/asignar-repartidor", envioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"repartidorId\": \"%s\"}".formatted(repartidorId)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("El repartidor debe estar ACTIVO"));
    }
}
