package com.duoc.msvehiculos.controller;

import com.duoc.msvehiculos.dto.CategoriaDTO;
import com.duoc.msvehiculos.service.CategoriaService;
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
class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    @Test
    void findById_deberiaRetornarCategoriaDelService() {
        CategoriaDTO esperado = new CategoriaDTO();
        when(categoriaService.findById(1)).thenReturn(esperado);

        ResponseEntity<CategoriaDTO> respuesta = categoriaController.findById(1);

        assertEquals(200, respuesta.getStatusCode().value());
        assertSame(esperado, respuesta.getBody());
        verify(categoriaService).findById(1);
    }

    @Test
    void delete_deberiaEliminarCategoria() {
        ResponseEntity<Void> respuesta = categoriaController.delete(1);

        assertEquals(204, respuesta.getStatusCode().value());
        verify(categoriaService).delete(1);
    }
}