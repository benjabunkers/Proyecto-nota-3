package com.duoc.ms_reservas.service;

import com.duoc.ms_reservas.dto.ClienteDTO;
import com.duoc.ms_reservas.dto.ReservaDTO;
import com.duoc.ms_reservas.dto.ReservaRequestDTO;
import com.duoc.ms_reservas.dto.VehiculoDTO;
import com.duoc.ms_reservas.exception.ResourceNotFoundException;
import com.duoc.ms_reservas.feign.ClienteClient;
import com.duoc.ms_reservas.feign.VehiculoClient;
import com.duoc.ms_reservas.mapper.ReservaMapper;
import com.duoc.ms_reservas.model.EstadoReserva;
import com.duoc.ms_reservas.model.Reserva;
import com.duoc.ms_reservas.repository.EstadoReservaRepository;
import com.duoc.ms_reservas.repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private EstadoReservaRepository estadoReservaRepository;

    @Mock
    private ReservaMapper reservaMapper;

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private VehiculoClient vehiculoClient;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    void save_deberiaValidarFeignYGuardarReserva() {
        ReservaRequestDTO request = crearRequest();
        ClienteDTO cliente = new ClienteDTO();
        cliente.setId(1);
        cliente.setNombre("Juan");
        cliente.setEmail("juan@ejemplo.com");
        VehiculoDTO vehiculo = new VehiculoDTO();
        vehiculo.setId(1);
        vehiculo.setNombre("Toyota Corolla");
        vehiculo.setDisponible(true);
        EstadoReserva estado = new EstadoReserva();
        estado.setId(1);
        Reserva entidad = new Reserva();
        ReservaDTO esperado = new ReservaDTO();

        when(clienteClient.obtenerClientePorId(1)).thenReturn(cliente);
        when(vehiculoClient.obtenerVehiculoPorId(1)).thenReturn(vehiculo);
        when(estadoReservaRepository.findById(1)).thenReturn(Optional.of(estado));
        when(reservaMapper.toEntity(request, estado)).thenReturn(entidad);
        when(reservaRepository.save(entidad)).thenReturn(entidad);
        when(reservaMapper.toDTO(entidad)).thenReturn(esperado);

        ReservaDTO resultado = reservaService.save(request);

        assertSame(esperado, resultado);
        assertEquals("Juan", entidad.getNombreCliente());
        assertEquals("juan@ejemplo.com", entidad.getCorreoCliente());
        assertEquals("Toyota Corolla", entidad.getNombreVehiculo());
        verify(clienteClient).obtenerClientePorId(1);
        verify(vehiculoClient).obtenerVehiculoPorId(1);
        verify(reservaRepository).save(entidad);
    }

    @Test
    void save_deberiaFallarCuandoVehiculoNoEstaDisponible() {
        ReservaRequestDTO request = crearRequest();
        ClienteDTO cliente = new ClienteDTO();
        cliente.setId(1);
        cliente.setNombre("Juan");
        cliente.setEmail("juan@ejemplo.com");
        VehiculoDTO vehiculo = new VehiculoDTO();
        vehiculo.setId(1);
        vehiculo.setNombre("Toyota Corolla");
        vehiculo.setDisponible(false);

        when(clienteClient.obtenerClientePorId(1)).thenReturn(cliente);
        when(vehiculoClient.obtenerVehiculoPorId(1)).thenReturn(vehiculo);

        assertThrows(IllegalStateException.class, () -> reservaService.save(request));

        verifyNoInteractions(estadoReservaRepository, reservaRepository, reservaMapper);
    }

    @Test
    void findById_deberiaFallarCuandoReservaNoExiste() {
        when(reservaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservaService.findById(99));
    }

    private ReservaRequestDTO crearRequest() {
        ReservaRequestDTO request = new ReservaRequestDTO();
        request.setClienteId(1);
        request.setVehiculoId(1);
        request.setEstadoReservaId(1);
        return request;
    }
}