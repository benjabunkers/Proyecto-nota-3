package com.duoc.ms_reservas.controller;

import com.duoc.ms_reservas.dto.ReservaDTO;
import com.duoc.ms_reservas.dto.ReservaRequestDTO;
import com.duoc.ms_reservas.service.ReservaService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.net.URI;
import java.util.List;

// API V1: expone las operaciones REST del recurso.
// Reutiliza el mismo servicio que V2 para conservar exactamente las reglas de negocio.
@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
@Tag(name = "Reserva V1", description = "Operaciones disponibles en la API V1")
public class ReservaController {

    private final ReservaService reservaService;

    @GetMapping
    @Operation(summary = "Listar reservas")
    public ResponseEntity<List<ReservaDTO>> findAll() {
        return ResponseEntity.ok(reservaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Reserva por ID")
    public ResponseEntity<ReservaDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(reservaService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear Reserva")
    public ResponseEntity<ReservaDTO> save(@Valid @RequestBody ReservaRequestDTO request) {
        ReservaDTO created = reservaService.save(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Reserva")
    public ResponseEntity<ReservaDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody ReservaRequestDTO request) {
        return ResponseEntity.ok(reservaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Reserva")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/desde-fecha")
    @Operation(summary = "Buscar reservas desde una fecha")
    public ResponseEntity<List<ReservaDTO>> findByFechaInicioDesde(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(reservaService.findByFechaInicioDesde(fecha));
    }
}