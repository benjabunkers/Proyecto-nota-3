package com.duoc.ms_reservas.controller;

import com.duoc.ms_reservas.dto.EstadoReservaDTO;
import com.duoc.ms_reservas.dto.EstadoReservaRequestDTO;
import com.duoc.ms_reservas.service.EstadoReservaService;
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

import java.net.URI;
import java.util.List;

// API V1: expone las operaciones REST del recurso.
//conservar las reglas de negocio.
@RestController
@RequestMapping("/api/v1/estados-reserva")
@RequiredArgsConstructor
@Tag(name = "EstadoReserva V1", description = "Operaciones disponibles en la API V1")
public class EstadoReservaController {

    private final EstadoReservaService estadoReservaService;

    @GetMapping
    @Operation(summary = "Listar estados-reserva")
    public ResponseEntity<List<EstadoReservaDTO>> findAll() {
        return ResponseEntity.ok(estadoReservaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar EstadoReserva por ID")
    public ResponseEntity<EstadoReservaDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(estadoReservaService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear EstadoReserva")
    public ResponseEntity<EstadoReservaDTO> save(@Valid @RequestBody EstadoReservaRequestDTO request) {
        EstadoReservaDTO created = estadoReservaService.save(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar EstadoReserva")
    public ResponseEntity<EstadoReservaDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody EstadoReservaRequestDTO request) {
        return ResponseEntity.ok(estadoReservaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar EstadoReserva")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        estadoReservaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}