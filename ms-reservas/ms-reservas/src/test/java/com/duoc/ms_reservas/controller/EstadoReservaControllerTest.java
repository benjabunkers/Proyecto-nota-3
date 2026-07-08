package com.duoc.ms_reservas.controller;

import com.duoc.ms_reservas.dto.EstadoReservaDTO;
import com.duoc.ms_reservas.service.EstadoReservaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstadoReservaControllerTest {

    @Mock
    private EstadoReservaService estadoReservaService;

    @InjectMocks
    private EstadoReservaController estadoReservaController;

    @Test
    void findById_deberiaRetornarEstadoDelService() {
        EstadoReservaDTO esperado = new EstadoReservaDTO();
        when(estadoReservaService.findById(1)).thenReturn(esperado);

        ResponseEntity<EstadoReservaDTO> respuesta = estadoReservaController.findById(1);

        assertEquals(200, respuesta.getStatusCode().value());
        assertSame(esperado, respuesta.getBody());
        verify(estadoReservaService).findById(1);
    }

    @Test
    void delete_deberiaEliminarEstadoReserva() {
        ResponseEntity<Void> respuesta = estadoReservaController.delete(1);

        assertEquals(204, respuesta.getStatusCode().value());
        verify(estadoReservaService).delete(1);
    }
}