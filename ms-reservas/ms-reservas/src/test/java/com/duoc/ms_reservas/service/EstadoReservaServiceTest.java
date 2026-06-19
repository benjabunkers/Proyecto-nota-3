package com.duoc.ms_reservas.service;

import com.duoc.ms_reservas.dto.EstadoReservaDTO;
import com.duoc.ms_reservas.dto.EstadoReservaRequestDTO;
import com.duoc.ms_reservas.exception.ResourceNotFoundException;
import com.duoc.ms_reservas.mapper.EstadoReservaMapper;
import com.duoc.ms_reservas.model.EstadoReserva;
import com.duoc.ms_reservas.repository.EstadoReservaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstadoReservaServiceTest {

    @Mock
    private EstadoReservaRepository estadoReservaRepository;

    @Mock
    private EstadoReservaMapper estadoReservaMapper;

    @InjectMocks
    private EstadoReservaService estadoReservaService;

    @Test
    void findById_deberiaRetornarEstado() {
        EstadoReserva estado = new EstadoReserva();
        estado.setId(1);
        EstadoReservaDTO esperado = new EstadoReservaDTO();
        esperado.setId(1);

        when(estadoReservaRepository.findById(1)).thenReturn(Optional.of(estado));
        when(estadoReservaMapper.toDTO(estado)).thenReturn(esperado);

        EstadoReservaDTO resultado = estadoReservaService.findById(1);

        assertSame(esperado, resultado);
        verify(estadoReservaRepository).findById(1);
    }

    @Test
    void findById_deberiaFallarCuandoNoExiste() {
        when(estadoReservaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> estadoReservaService.findById(99));
    }

    @Test
    void save_deberiaGuardarEstado() {
        EstadoReservaRequestDTO request = new EstadoReservaRequestDTO();
        EstadoReserva entidad = new EstadoReserva();
        EstadoReservaDTO esperado = new EstadoReservaDTO();

        when(estadoReservaMapper.toEntity(request)).thenReturn(entidad);
        when(estadoReservaRepository.save(entidad)).thenReturn(entidad);
        when(estadoReservaMapper.toDTO(entidad)).thenReturn(esperado);

        EstadoReservaDTO resultado = estadoReservaService.save(request);

        assertSame(esperado, resultado);
        verify(estadoReservaRepository).save(entidad);
    }
}