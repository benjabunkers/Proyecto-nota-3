package com.duoc.mssucursales.controller;

import com.duoc.mssucursales.assemblers.RegionModelAssembler;
import com.duoc.mssucursales.dto.RegionDTO;
import com.duoc.mssucursales.dto.RegionRequestDTO;
import com.duoc.mssucursales.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
@Tag(name = "Regiones V2", description = "Operaciones para gestionar regiones")
public class RegionControllerV2 {
    private final RegionService regionService;
    private final RegionModelAssembler regionModelAssembler;

    @GetMapping("/regiones")
    @Operation(summary = "Listar regiones", description = "Obtiene todas las regiones registradas con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Lista de regiones obtenida correctamente")
    public ResponseEntity<CollectionModel<EntityModel<RegionDTO>>> findAll() {
        List<EntityModel<RegionDTO>> regiones = regionService.findAll().stream().map(regionModelAssembler::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(regiones, linkTo(methodOn(RegionControllerV2.class).findAll()).withSelfRel()));
    }

    @GetMapping("/regiones/{id}")
    @Operation(summary = "Buscar region por ID", description = "Obtiene una region especifica con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Region encontrada correctamente")
    @ApiResponse(responseCode = "404", description = "Region no encontrada", content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<EntityModel<RegionDTO>> findById(@Parameter(description = "ID de la region", example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(regionModelAssembler.toModel(regionService.findById(id)));
    }

    @PostMapping("/regiones")
    @Operation(summary = "Crear region", description = "Registra una nueva region en el sistema")
    @ApiResponse(responseCode = "201", description = "Region creada correctamente")
    public ResponseEntity<EntityModel<RegionDTO>> save(@Valid @RequestBody(description = "Datos requeridos para crear la region", required = true, content = @Content(schema = @Schema(implementation = RegionRequestDTO.class))) @org.springframework.web.bind.annotation.RequestBody RegionRequestDTO requestDTO) {
        RegionDTO regionCreada = regionService.save(requestDTO);
        return ResponseEntity.created(linkTo(methodOn(RegionControllerV2.class).findById(regionCreada.getId())).toUri()).body(regionModelAssembler.toModel(regionCreada));
    }

    @PutMapping("/regiones/{id}")
    @Operation(summary = "Actualizar region", description = "Modifica los datos de una region existente")
    @ApiResponse(responseCode = "200", description = "Region actualizada correctamente")
    public ResponseEntity<EntityModel<RegionDTO>> update(@Parameter(description = "ID de la region", example = "1") @PathVariable Integer id, @Valid @RequestBody(description = "Datos requeridos para actualizar la region", required = true, content = @Content(schema = @Schema(implementation = RegionRequestDTO.class))) @org.springframework.web.bind.annotation.RequestBody RegionRequestDTO requestDTO) {
        return ResponseEntity.ok(regionModelAssembler.toModel(regionService.update(id, requestDTO)));
    }

    @DeleteMapping("/regiones/{id}")
    @Operation(summary = "Eliminar region", description = "Elimina una region del sistema")
    @ApiResponse(responseCode = "204", description = "Region eliminada correctamente")
    public ResponseEntity<Void> delete(@Parameter(description = "ID de la region", example = "1") @PathVariable Integer id) {
        regionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

