package com.duoc.ms_reservas.controller;

import com.duoc.ms_reservas.dto.ReservaDTO;
import com.duoc.ms_reservas.service.ReservaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaControllerTest {

    @Mock
    private ReservaService reservaService;

    @InjectMocks
    private ReservaController reservaController;

    @Test
    void findById_deberiaRetornarReservaDelService() {
        ReservaDTO esperado = new ReservaDTO();
        when(reservaService.findById(1)).thenReturn(esperado);

        ResponseEntity<ReservaDTO> respuesta = reservaController.findById(1);

        assertEquals(200, respuesta.getStatusCode().value());
        assertSame(esperado, respuesta.getBody());
        verify(reservaService).findById(1);
    }

    @Test
    void buscarDesdeFecha_deberiaDelegarEnService() {
        LocalDate fecha = LocalDate.of(2026, 6, 21);
        List<ReservaDTO> esperado = List.of(new ReservaDTO());
        when(reservaService.findByFechaInicioDesde(fecha)).thenReturn(esperado);

        ResponseEntity<List<ReservaDTO>> respuesta = reservaController.findByFechaInicioDesde(fecha);

        assertSame(esperado, respuesta.getBody());
        verify(reservaService).findByFechaInicioDesde(fecha);
    }

    @Test
    void delete_deberiaEliminarReserva() {
        ResponseEntity<Void> respuesta = reservaController.delete(1);

        assertEquals(204, respuesta.getStatusCode().value());
        verify(reservaService).delete(1);
    }
}