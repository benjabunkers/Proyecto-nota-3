package com.duoc.ms_clientes.controller;

import com.duoc.ms_clientes.dto.DireccionDTO;
import com.duoc.ms_clientes.service.DireccionService;
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
class DireccionControllerTest {

    @Mock
    private DireccionService direccionService;

    @InjectMocks
    private DireccionController direccionController;

    @Test
    void findById_deberiaRetornarDireccionDelService() {
        DireccionDTO esperado = new DireccionDTO();
        when(direccionService.findById(1)).thenReturn(esperado);

        ResponseEntity<DireccionDTO> respuesta = direccionController.findById(1);

        assertEquals(200, respuesta.getStatusCode().value());
        assertSame(esperado, respuesta.getBody());
        verify(direccionService).findById(1);
    }

    @Test
    void delete_deberiaEliminarDireccion() {
        ResponseEntity<Void> respuesta = direccionController.delete(1);

        assertEquals(204, respuesta.getStatusCode().value());
        verify(direccionService).delete(1);
    }
}