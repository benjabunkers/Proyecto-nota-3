package com.duoc.ms_reservas.controller;

import com.duoc.ms_reservas.assemblers.ReservaModelAssembler;
import com.duoc.ms_reservas.dto.ReservaDTO;
import com.duoc.ms_reservas.dto.ReservaRequestDTO;
import com.duoc.ms_reservas.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Operaciones para gestionar reservas de vehiculos")
public class ReservaController {

    private final ReservaService reservaService;
    private final ReservaModelAssembler reservaModelAssembler;

    @GetMapping
    @Operation(summary = "Listar reservas", description = "Obtiene todas las reservas con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida correctamente")
    public ResponseEntity<CollectionModel<EntityModel<ReservaDTO>>> findAll() {
        List<EntityModel<ReservaDTO>> reservas = reservaService.findAll().stream()
                .map(reservaModelAssembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(reservas,
                linkTo(methodOn(ReservaController.class).findAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva por ID", description = "Obtiene una reserva especifica con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Reserva encontrada correctamente")
    @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<EntityModel<ReservaDTO>> findById(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(reservaModelAssembler.toModel(reservaService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Crear reserva",
            description = "Registra una reserva y valida cliente y vehiculo mediante Feign")
    @ApiResponse(responseCode = "201", description = "Reserva creada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos invalidos o vehiculo no disponible",
            content = @Content(schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "404", description = "Estado, cliente o vehiculo no encontrado",
            content = @Content(schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "503", description = "Microservicio externo no disponible",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<EntityModel<ReservaDTO>> save(
            @Valid
            @RequestBody(description = "Datos requeridos para crear la reserva", required = true,
                    content = @Content(schema = @Schema(implementation = ReservaRequestDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody ReservaRequestDTO requestDTO) {
        ReservaDTO reservaCreada = reservaService.save(requestDTO);
        return ResponseEntity
                .created(linkTo(methodOn(ReservaController.class).findById(reservaCreada.getId())).toUri())
                .body(reservaModelAssembler.toModel(reservaCreada));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reserva", description = "Modifica una reserva y valida sus recursos externos")
    @ApiResponse(responseCode = "200", description = "Reserva actualizada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos invalidos",
            content = @Content(schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "404", description = "Reserva, estado, cliente o vehiculo no encontrado",
            content = @Content(schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "503", description = "Microservicio externo no disponible",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<EntityModel<ReservaDTO>> update(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Integer id,
            @Valid
            @RequestBody(description = "Datos requeridos para actualizar la reserva", required = true,
                    content = @Content(schema = @Schema(implementation = ReservaRequestDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody ReservaRequestDTO requestDTO) {
        return ResponseEntity.ok(reservaModelAssembler.toModel(reservaService.update(id, requestDTO)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reserva", description = "Elimina una reserva del sistema")
    @ApiResponse(responseCode = "204", description = "Reserva eliminada correctamente")
    @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Integer id) {
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/desde-fecha")
    @Operation(summary = "Buscar reservas desde una fecha",
            description = "Obtiene reservas desde una fecha con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente")
    @ApiResponse(responseCode = "400", description = "Formato de fecha invalido",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<CollectionModel<EntityModel<ReservaDTO>>> findByFechaInicioDesde(
            @Parameter(description = "Fecha inicial en formato ISO yyyy-MM-dd", example = "2026-06-17")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {
        List<EntityModel<ReservaDTO>> reservas = reservaService.findByFechaInicioDesde(fecha).stream()
                .map(reservaModelAssembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(reservas,
                linkTo(methodOn(ReservaController.class).findByFechaInicioDesde(fecha)).withSelfRel(),
                linkTo(methodOn(ReservaController.class).findAll()).withRel("reservas")));
    }
}