package com.duoc.msvehiculos.service;

import com.duoc.msvehiculos.dto.CategoriaDTO;
import com.duoc.msvehiculos.dto.CategoriaRequestDTO;
import com.duoc.msvehiculos.exception.ResourceNotFoundException;
import com.duoc.msvehiculos.model.Categoria;
import com.duoc.msvehiculos.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void save_deberiaGuardarCategoria() {
        CategoriaRequestDTO request = CategoriaRequestDTO.builder()
                .nombre("SUV")
                .descripcion("Vehiculos familiares")
                .tarifaBase(new BigDecimal("45000"))
                .capacidadPasajeros(5)
                .activa(true)
                .fechaCreacion(LocalDate.now())
                .build();

        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(invocation -> {
            Categoria categoria = invocation.getArgument(0);
            categoria.setId(1);
            return categoria;
        });

        CategoriaDTO resultado = categoriaService.save(request);

        assertEquals(1, resultado.getId());
        assertEquals("SUV", resultado.getNombre());
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void findById_deberiaRetornarCategoria() {
        Categoria categoria = Categoria.builder().id(1).nombre("SUV").build();
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));

        CategoriaDTO resultado = categoriaService.findById(1);

        assertEquals(1, resultado.getId());
        assertEquals("SUV", resultado.getNombre());
    }

    @Test
    void findById_deberiaFallarCuandoNoExiste() {
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoriaService.findById(99));
    }
}