package com.duoc.mssucursales.controller;

import com.duoc.mssucursales.assemblers.SucursalModelAssembler;
import com.duoc.mssucursales.dto.SucursalDTO;
import com.duoc.mssucursales.dto.SucursalRequestDTO;
import com.duoc.mssucursales.service.SucursalService;
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
@Tag(name = "Sucursales V2", description = "Operaciones para gestionar sucursales")
public class SucursalControllerV2 {
    private final SucursalService sucursalService;
    private final SucursalModelAssembler sucursalModelAssembler;

    @GetMapping("/sucursales")
    @Operation(summary = "Listar sucursales", description = "Obtiene todas las sucursales registradas con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Lista de sucursales obtenida correctamente")
    public ResponseEntity<CollectionModel<EntityModel<SucursalDTO>>> findAll() {
        List<EntityModel<SucursalDTO>> sucursales = sucursalService.findAll().stream().map(sucursalModelAssembler::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(sucursales, linkTo(methodOn(SucursalControllerV2.class).findAll()).withSelfRel()));
    }

    @GetMapping("/sucursales/{id}")
    @Operation(summary = "Buscar sucursal por ID", description = "Obtiene una sucursal especifica con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Sucursal encontrada correctamente")
    @ApiResponse(responseCode = "404", description = "Sucursal no encontrada", content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<EntityModel<SucursalDTO>> findById(@Parameter(description = "ID de la sucursal", example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(sucursalModelAssembler.toModel(sucursalService.findById(id)));
    }

    @PostMapping("/sucursales")
    @Operation(summary = "Crear sucursal", description = "Registra una nueva sucursal en el sistema")
    @ApiResponse(responseCode = "201", description = "Sucursal creada correctamente")
    public ResponseEntity<EntityModel<SucursalDTO>> save(@Valid @RequestBody(description = "Datos requeridos para crear la sucursal", required = true, content = @Content(schema = @Schema(implementation = SucursalRequestDTO.class))) @org.springframework.web.bind.annotation.RequestBody SucursalRequestDTO requestDTO) {
        SucursalDTO sucursalCreada = sucursalService.save(requestDTO);
        return ResponseEntity.created(linkTo(methodOn(SucursalControllerV2.class).findById(sucursalCreada.getId())).toUri()).body(sucursalModelAssembler.toModel(sucursalCreada));
    }

    @PutMapping("/sucursales/{id}")
    @Operation(summary = "Actualizar sucursal", description = "Modifica los datos de una sucursal existente")
    @ApiResponse(responseCode = "200", description = "Sucursal actualizada correctamente")
    public ResponseEntity<EntityModel<SucursalDTO>> update(@Parameter(description = "ID de la sucursal", example = "1") @PathVariable Integer id, @Valid @RequestBody(description = "Datos requeridos para actualizar la sucursal", required = true, content = @Content(schema = @Schema(implementation = SucursalRequestDTO.class))) @org.springframework.web.bind.annotation.RequestBody SucursalRequestDTO requestDTO) {
        return ResponseEntity.ok(sucursalModelAssembler.toModel(sucursalService.update(id, requestDTO)));
    }

    @DeleteMapping("/sucursales/{id}")
    @Operation(summary = "Eliminar sucursal", description = "Elimina una sucursal del sistema")
    @ApiResponse(responseCode = "204", description = "Sucursal eliminada correctamente")
    public ResponseEntity<Void> delete(@Parameter(description = "ID de la sucursal", example = "1") @PathVariable Integer id) {
        sucursalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sucursales/operativas")
    @Operation(summary = "Listar sucursales operativas", description = "Obtiene sucursales operativas ordenadas")
    @ApiResponse(responseCode = "200", description = "Lista de sucursales obtenida correctamente")
    public ResponseEntity<CollectionModel<EntityModel<SucursalDTO>>> listarOperativasOrdenadas() {
        List<EntityModel<SucursalDTO>> sucursales = sucursalService.listarOperativasOrdenadas().stream().map(sucursalModelAssembler::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(sucursales, linkTo(methodOn(SucursalControllerV2.class).listarOperativasOrdenadas()).withSelfRel(), linkTo(methodOn(SucursalControllerV2.class).findAll()).withRel("sucursales")));
    }
}

