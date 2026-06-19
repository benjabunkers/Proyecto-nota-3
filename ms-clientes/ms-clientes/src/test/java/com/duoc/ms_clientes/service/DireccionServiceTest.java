package com.duoc.ms_clientes.service;

import com.duoc.ms_clientes.dto.DireccionDTO;
import com.duoc.ms_clientes.dto.DireccionRequestDTO;
import com.duoc.ms_clientes.exception.ResourceNotFoundException;
import com.duoc.ms_clientes.mapper.DireccionMapper;
import com.duoc.ms_clientes.model.Cliente;
import com.duoc.ms_clientes.model.Direccion;
import com.duoc.ms_clientes.repository.ClienteRepository;
import com.duoc.ms_clientes.repository.DireccionRepository;
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
class DireccionServiceTest {

    @Mock
    private DireccionRepository direccionRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private DireccionMapper direccionMapper;

    @InjectMocks
    private DireccionService direccionService;

    @Test
    void save_deberiaGuardarDireccionConCliente() {
        DireccionRequestDTO request = new DireccionRequestDTO();
        request.setClienteId(1);
        Cliente cliente = new Cliente();
        cliente.setId(1);
        Direccion direccion = new Direccion();
        DireccionDTO esperado = new DireccionDTO();

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(direccionMapper.toEntity(request, cliente)).thenReturn(direccion);
        when(direccionRepository.save(direccion)).thenReturn(direccion);
        when(direccionMapper.toDTO(direccion)).thenReturn(esperado);

        DireccionDTO resultado = direccionService.save(request);

        assertSame(esperado, resultado);
        verify(clienteRepository).findById(1);
        verify(direccionRepository).save(direccion);
    }

    @Test
    void save_deberiaFallarCuandoClienteNoExiste() {
        DireccionRequestDTO request = new DireccionRequestDTO();
        request.setClienteId(99);
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> direccionService.save(request));
    }

    @Test
    void findById_deberiaFallarCuandoDireccionNoExiste() {
        when(direccionRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> direccionService.findById(99));
    }
}