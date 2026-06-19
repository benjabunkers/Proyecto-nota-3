package com.duoc.ms_clientes.service;

import com.duoc.ms_clientes.dto.ClienteDTO;
import com.duoc.ms_clientes.dto.ClienteRequestDTO;
import com.duoc.ms_clientes.exception.ResourceNotFoundException;
import com.duoc.ms_clientes.mapper.ClienteMapper;
import com.duoc.ms_clientes.model.Cliente;
import com.duoc.ms_clientes.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void findById_deberiaRetornarCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        ClienteDTO esperado = new ClienteDTO();
        esperado.setId(1);

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDTO(cliente)).thenReturn(esperado);

        ClienteDTO resultado = clienteService.findById(1);

        assertSame(esperado, resultado);
        verify(clienteRepository).findById(1);
        verify(clienteMapper).toDTO(cliente);
    }

    @Test
    void findById_deberiaLanzarExcepcionCuandoNoExiste() {
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.findById(99));

        verify(clienteRepository).findById(99);
        verifyNoInteractions(clienteMapper);
    }

    @Test
    void save_deberiaGuardarYRetornarCliente() {
        ClienteRequestDTO request = new ClienteRequestDTO();
        Cliente entidad = new Cliente();
        ClienteDTO esperado = new ClienteDTO();

        when(clienteMapper.toEntity(request)).thenReturn(entidad);
        when(clienteRepository.save(entidad)).thenReturn(entidad);
        when(clienteMapper.toDTO(entidad)).thenReturn(esperado);

        ClienteDTO resultado = clienteService.save(request);

        assertSame(esperado, resultado);
        verify(clienteRepository).save(entidad);
    }
}