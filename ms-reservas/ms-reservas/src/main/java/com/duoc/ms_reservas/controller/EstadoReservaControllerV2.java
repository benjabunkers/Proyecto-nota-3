package com.duoc.ms_reservas.controller;

import com.duoc.ms_reservas.assemblers.EstadoReservaModelAssembler;
import com.duoc.ms_reservas.dto.EstadoReservaDTO;
import com.duoc.ms_reservas.dto.EstadoReservaRequestDTO;
import com.duoc.ms_reservas.service.EstadoReservaService;
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
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// API V2: conserva la logica de negocio y agrega enlaces HATEOAS a las respuestas.
@RestController
@RequestMapping("/api/v2/estados-reserva")
@RequiredArgsConstructor
@Tag(name = "Estados de reserva V2", description = "Operaciones para gestionar los estados de las reservas")
public class EstadoReservaControllerV2 {

    private final EstadoReservaService estadoReservaService;
    private final EstadoReservaModelAssembler estadoReservaModelAssembler;

    @GetMapping
    @Operation(summary = "Listar estados de reserva", description = "Obtiene todos los estados con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Lista de estados obtenida correctamente")
    public ResponseEntity<CollectionModel<EntityModel<EstadoReservaDTO>>> findAll() {
        List<EntityModel<EstadoReservaDTO>> estados = estadoReservaService.findAll().stream()
                .map(estadoReservaModelAssembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(estados,
                linkTo(methodOn(EstadoReservaControllerV2.class).findAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar estado por ID", description = "Obtiene un estado de reserva con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Estado de reserva encontrado correctamente")
    @ApiResponse(responseCode = "404", description = "Estado de reserva no encontrado",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<EntityModel<EstadoReservaDTO>> findById(
            @Parameter(description = "ID del estado de reserva", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(estadoReservaModelAssembler.toModel(estadoReservaService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Crear estado de reserva", description = "Registra un nuevo estado de reserva")
    @ApiResponse(responseCode = "201", description = "Estado de reserva creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos invalidos",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<EntityModel<EstadoReservaDTO>> save(
            @Valid
            @RequestBody(description = "Datos requeridos para crear el estado", required = true,
                    content = @Content(schema = @Schema(implementation = EstadoReservaRequestDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody EstadoReservaRequestDTO requestDTO) {
        EstadoReservaDTO estadoCreado = estadoReservaService.save(requestDTO);
        return ResponseEntity
                .created(linkTo(methodOn(EstadoReservaControllerV2.class).findById(estadoCreado.getId())).toUri())
                .body(estadoReservaModelAssembler.toModel(estadoCreado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar estado de reserva", description = "Modifica un estado de reserva existente")
    @ApiResponse(responseCode = "200", description = "Estado de reserva actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos invalidos",
            content = @Content(schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "404", description = "Estado de reserva no encontrado",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<EntityModel<EstadoReservaDTO>> update(
            @Parameter(description = "ID del estado de reserva", example = "1")
            @PathVariable Integer id,
            @Valid
            @RequestBody(description = "Datos requeridos para actualizar el estado", required = true,
                    content = @Content(schema = @Schema(implementation = EstadoReservaRequestDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody EstadoReservaRequestDTO requestDTO) {
        return ResponseEntity.ok(estadoReservaModelAssembler.toModel(estadoReservaService.update(id, requestDTO)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar estado de reserva", description = "Elimina un estado de reserva del sistema")
    @ApiResponse(responseCode = "204", description = "Estado de reserva eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Estado de reserva no encontrado",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del estado de reserva", example = "1")
            @PathVariable Integer id) {
        estadoReservaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}