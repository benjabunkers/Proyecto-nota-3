package com.duoc.ms_clientes.controller;

import com.duoc.ms_clientes.dto.DireccionDTO;
import com.duoc.ms_clientes.dto.DireccionRequestDTO;
import com.duoc.ms_clientes.service.DireccionService;
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
// Reutiliza el mismo servicio que V2 para conservar exactamente las reglas de negocio.
@RestController
@RequestMapping("/api/v1/direcciones")
@RequiredArgsConstructor
@Tag(name = "Direccion V1", description = "Operaciones disponibles en la API V1")
public class DireccionController {

    private final DireccionService direccionService;

    @GetMapping
    @Operation(summary = "Listar direcciones")
    public ResponseEntity<List<DireccionDTO>> findAll() {
        return ResponseEntity.ok(direccionService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Direccion por ID")
    public ResponseEntity<DireccionDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(direccionService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear Direccion")
    public ResponseEntity<DireccionDTO> save(@Valid @RequestBody DireccionRequestDTO request) {
        DireccionDTO created = direccionService.save(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Direccion")
    public ResponseEntity<DireccionDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody DireccionRequestDTO request) {
        return ResponseEntity.ok(direccionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Direccion")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        direccionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}