package com.duoc.ms_clientes.controller;

import com.duoc.ms_clientes.assemblers.DireccionModelAssembler;
import com.duoc.ms_clientes.dto.DireccionDTO;
import com.duoc.ms_clientes.dto.DireccionRequestDTO;
import com.duoc.ms_clientes.service.DireccionService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// API V2: conserva la logica de negocio y agrega enlaces HATEOAS a las respuestas.
@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
@Tag(name = "Direcciones V2", description = "Operaciones para gestionar direcciones de clientes")
public class DireccionControllerV2 {

    private final DireccionService direccionService;
    private final DireccionModelAssembler direccionModelAssembler;

    @GetMapping("/direcciones")
    @Operation(summary = "Listar direcciones", description = "Obtiene todas las direcciones con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Lista de direcciones obtenida correctamente")
    public ResponseEntity<CollectionModel<EntityModel<DireccionDTO>>> findAll() {
        List<EntityModel<DireccionDTO>> direcciones = direccionService.findAll().stream()
                .map(direccionModelAssembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(direcciones,
                linkTo(methodOn(DireccionControllerV2.class).findAll()).withSelfRel()));
    }

    @GetMapping("/direcciones/{id}")
    @Operation(summary = "Buscar direccion por ID", description = "Obtiene una direccion especifica con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Direccion encontrada correctamente")
    @ApiResponse(responseCode = "404", description = "Direccion no encontrada",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<EntityModel<DireccionDTO>> findById(
            @Parameter(description = "ID de la direccion", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(direccionModelAssembler.toModel(direccionService.findById(id)));
    }

    @PostMapping("/direcciones")
    @Operation(summary = "Crear direccion", description = "Registra una direccion asociada a un cliente")
    @ApiResponse(responseCode = "201", description = "Direccion creada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos invalidos",
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Cliente asociado no encontrado",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<EntityModel<DireccionDTO>> save(
            @Valid
            @RequestBody(description = "Datos requeridos para crear la direccion", required = true,
                    content = @Content(schema = @Schema(implementation = DireccionRequestDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody DireccionRequestDTO request) {
        DireccionDTO direccionCreada = direccionService.save(request);
        return ResponseEntity
                .created(linkTo(methodOn(DireccionControllerV2.class).findById(direccionCreada.getId())).toUri())
                .body(direccionModelAssembler.toModel(direccionCreada));
    }

    @PutMapping("/direcciones/{id}")
    @Operation(summary = "Actualizar direccion", description = "Modifica los datos de una direccion existente")
    @ApiResponse(responseCode = "200", description = "Direccion actualizada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos invalidos",
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Direccion o cliente asociado no encontrado",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<EntityModel<DireccionDTO>> update(
            @Parameter(description = "ID de la direccion", example = "1")
            @PathVariable Integer id,
            @Valid
            @RequestBody(description = "Datos requeridos para actualizar la direccion", required = true,
                    content = @Content(schema = @Schema(implementation = DireccionRequestDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody DireccionRequestDTO request) {
        return ResponseEntity.ok(direccionModelAssembler.toModel(direccionService.update(id, request)));
    }

    @DeleteMapping("/direcciones/{id}")
    @Operation(summary = "Eliminar direccion", description = "Elimina una direccion del sistema")
    @ApiResponse(responseCode = "204", description = "Direccion eliminada correctamente")
    @ApiResponse(responseCode = "404", description = "Direccion no encontrada",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la direccion", example = "1")
            @PathVariable Integer id) {
        direccionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}