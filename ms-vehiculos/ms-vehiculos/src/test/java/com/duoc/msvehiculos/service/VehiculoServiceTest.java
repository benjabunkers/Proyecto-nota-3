package com.duoc.msvehiculos.service;

import com.duoc.msvehiculos.dto.VehiculoDTO;
import com.duoc.msvehiculos.dto.VehiculoRequestDTO;
import com.duoc.msvehiculos.exception.ResourceNotFoundException;
import com.duoc.msvehiculos.model.Categoria;
import com.duoc.msvehiculos.model.Vehiculo;
import com.duoc.msvehiculos.repository.CategoriaRepository;
import com.duoc.msvehiculos.repository.VehiculoRepository;
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
class VehiculoServiceTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private VehiculoService vehiculoService;

    @Test
    void save_deberiaGuardarVehiculoConCategoria() {
        Categoria categoria = Categoria.builder().id(1).nombre("SUV").build();
        VehiculoRequestDTO request = crearRequest();

        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(vehiculoRepository.save(any(Vehiculo.class))).thenAnswer(invocation -> {
            Vehiculo vehiculo = invocation.getArgument(0);
            vehiculo.setId(10);
            return vehiculo;
        });

        VehiculoDTO resultado = vehiculoService.save(request);

        assertEquals(10, resultado.getId());
        assertEquals("ABCD12", resultado.getPatente());
        assertEquals("SUV", resultado.getCategoriaNombre());
        verify(categoriaRepository).findById(1);
        verify(vehiculoRepository).save(any(Vehiculo.class));
    }

    @Test
    void save_deberiaFallarCuandoCategoriaNoExiste() {
        VehiculoRequestDTO request = crearRequest();
        when(categoriaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehiculoService.save(request));
    }

    @Test
    void findById_deberiaFallarCuandoVehiculoNoExiste() {
        when(vehiculoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehiculoService.findById(99));
    }

    private VehiculoRequestDTO crearRequest() {
        return VehiculoRequestDTO.builder()
                .patente("ABCD12")
                .marca("Toyota")
                .modelo("Yaris")
                .anio(2022)
                .color("Blanco")
                .precioArriendoDiario(new BigDecimal("35000"))
                .kilometraje(20000)
                .disponible(true)
                .fechaIngreso(LocalDate.now())
                .categoriaId(1)
                .build();
    }
}