package com.duoc.msvehiculos.controller;

import com.duoc.msvehiculos.dto.VehiculoDTO;
import com.duoc.msvehiculos.service.VehiculoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehiculoControllerTest {

    @Mock
    private VehiculoService vehiculoService;

    @InjectMocks
    private VehiculoController vehiculoController;

    @Test
    void findById_deberiaRetornarVehiculoDelService() {
        VehiculoDTO esperado = new VehiculoDTO();
        when(vehiculoService.findById(1)).thenReturn(esperado);

        ResponseEntity<VehiculoDTO> respuesta = vehiculoController.findById(1);

        assertEquals(200, respuesta.getStatusCode().value());
        assertSame(esperado, respuesta.getBody());
        verify(vehiculoService).findById(1);
    }

    @Test
    void buscarDisponiblesPorPrecioMenor_deberiaDelegarEnService() {
        BigDecimal precio = new BigDecimal("50000");
        List<VehiculoDTO> esperado = List.of(new VehiculoDTO());
        when(vehiculoService.buscarDisponiblesPorPrecioMenor(precio)).thenReturn(esperado);

        ResponseEntity<List<VehiculoDTO>> respuesta = vehiculoController.buscarDisponiblesPorPrecioMenor(precio);

        assertSame(esperado, respuesta.getBody());
        verify(vehiculoService).buscarDisponiblesPorPrecioMenor(precio);
    }

    @Test
    void delete_deberiaEliminarVehiculo() {
        ResponseEntity<Void> respuesta = vehiculoController.delete(1);

        assertEquals(204, respuesta.getStatusCode().value());
        verify(vehiculoService).delete(1);
    }
}