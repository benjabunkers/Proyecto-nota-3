package com.duoc.msvehiculos.controller;

import com.duoc.msvehiculos.dto.VehiculoDTO;
import com.duoc.msvehiculos.dto.VehiculoRequestDTO;
import com.duoc.msvehiculos.service.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

//Mantiene las reglas de negocio.
@RestController
@RequestMapping("/api/v1/vehiculos")
@RequiredArgsConstructor
@Tag(name = "Vehiculo", description = "Operaciones para gestionar los vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @GetMapping
    @Operation(summary = "Listar vehiculos")
    public ResponseEntity<List<VehiculoDTO>> findAll() {
        return ResponseEntity.ok(vehiculoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Vehiculo por ID")
    public ResponseEntity<VehiculoDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(vehiculoService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear Vehiculo")
    public ResponseEntity<VehiculoDTO> save(@Valid @RequestBody VehiculoRequestDTO request) {
        VehiculoDTO created = vehiculoService.save(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Vehiculo")
    public ResponseEntity<VehiculoDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody VehiculoRequestDTO request) {
        return ResponseEntity.ok(vehiculoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Vehiculo")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        vehiculoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disponibles/precio-menor/{precioMaximo}")
    @Operation(summary = "Buscar vehiculos disponibles por precio")
    public ResponseEntity<List<VehiculoDTO>> buscarDisponiblesPorPrecioMenor(
            @PathVariable BigDecimal precioMaximo) {
        return ResponseEntity.ok(vehiculoService.buscarDisponiblesPorPrecioMenor(precioMaximo));
    }
}