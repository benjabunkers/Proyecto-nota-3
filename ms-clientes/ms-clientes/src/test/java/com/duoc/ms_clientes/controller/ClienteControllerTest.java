package com.duoc.ms_clientes.controller;

import com.duoc.ms_clientes.dto.ClienteDTO;
import com.duoc.ms_clientes.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @Test
    void findById_deberiaRetornarClienteDelService() {
        ClienteDTO esperado = new ClienteDTO();
        when(clienteService.findById(1)).thenReturn(esperado);

        ResponseEntity<ClienteDTO> respuesta = clienteController.findById(1);

        assertEquals(200, respuesta.getStatusCode().value());
        assertSame(esperado, respuesta.getBody());
        verify(clienteService).findById(1);
    }

    @Test
    void buscarPorEmail_deberiaDelegarEnService() {
        List<ClienteDTO> esperado = List.of(new ClienteDTO());
        when(clienteService.buscarPorEmail("gmail")).thenReturn(esperado);

        ResponseEntity<List<ClienteDTO>> respuesta = clienteController.buscarPorEmail("gmail");

        assertSame(esperado, respuesta.getBody());
        verify(clienteService).buscarPorEmail("gmail");
    }

    @Test
    void delete_deberiaEliminarCliente() {
        ResponseEntity<Void> respuesta = clienteController.delete(1);

        assertEquals(204, respuesta.getStatusCode().value());
        verify(clienteService).delete(1);
    }
}