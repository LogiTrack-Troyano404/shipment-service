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

import com.example.shipmentservice.Exception.ClienteNoEncontrado;
import com.example.shipmentservice.dto.ClienteRequestDTO;
import com.example.shipmentservice.mapper.ClienteMapper;
import com.example.shipmentservice.model.Cliente;
import com.example.shipmentservice.service.ClienteService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
@Import(ClienteMapper.class)
public class ClienteControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    private Cliente cliente;
    private UUID clienteId;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();

        cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan@gmail.com");
        cliente.setTelefono("3811234567");
    }

    @Test
    void registrarCliente_devuelve201() throws Exception {
        when(clienteService.registrarCliente(any(Cliente.class)))
                .thenReturn(cliente);

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre": "Juan Pérez",
                                    "email": "juan@gmail.com",
                                    "telefono": "3811234567"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(clienteId.toString()))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.email").value("juan@gmail.com"));
    }

    @Test
    void registrarCliente_sinNombre_devuelve400() throws Exception {
        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "juan@gmail.com",
                                    "telefono": "3811234567"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarPorId_existente_devuelve200() throws Exception {
        when(clienteService.buscarPorId(clienteId)).thenReturn(cliente);

        mockMvc.perform(get("/api/v1/clientes/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.email").value("juan@gmail.com"));
    }

    @Test
    void buscarPorId_inexistente_devuelve404() throws Exception {
        when(clienteService.buscarPorId(clienteId))
                .thenThrow(new ClienteNoEncontrado("Cliente no encontrado"));

        mockMvc.perform(get("/api/v1/clientes/{id}", clienteId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void listarClientes_devuelve200() throws Exception {
        when(clienteService.listarClientes()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"));
    }

    @Test
    void actualizarCliente_devuelve200() throws Exception {
        cliente.setNombre("Juan Carlos Pérez");

        when(clienteService.actualizarCliente(eq(clienteId), any(ClienteRequestDTO.class)))
                .thenReturn(cliente);

        mockMvc.perform(put("/api/v1/clientes/{id}", clienteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre": "Juan Carlos Pérez",
                                    "email": "juan@gmail.com",
                                    "telefono": "3811234567"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Carlos Pérez"));
    }

    @Test
    void eliminarCliente_devuelve204() throws Exception {
        doNothing().when(clienteService).eliminarCliente(clienteId);

        mockMvc.perform(delete("/api/v1/clientes/{id}", clienteId))
                .andExpect(status().isNoContent());

        verify(clienteService).eliminarCliente(clienteId);
    }

}
