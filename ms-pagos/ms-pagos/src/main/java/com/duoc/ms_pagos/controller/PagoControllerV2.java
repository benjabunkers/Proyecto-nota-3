package com.duoc.ms_pagos.controller;

import com.duoc.ms_pagos.assemblers.PagoModelAssembler;
import com.duoc.ms_pagos.dto.PagoDTO;
import com.duoc.ms_pagos.dto.PagoRequestDTO;
import com.duoc.ms_pagos.service.PagoService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// API V2: conserva la logica de negocio y agrega enlaces HATEOAS a las respuestas.
@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
@Tag(name = "Pagos V2", description = "Operaciones para gestionar pagos")
public class PagoControllerV2 {

    private final PagoService pagoService;
    private final PagoModelAssembler pagoModelAssembler;

    @GetMapping("/pagos")
    @Operation(summary = "Listar pagos", description = "Obtiene todos los pagos registrados con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida correctamente")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<CollectionModel<EntityModel<PagoDTO>>> findAll() {
        List<EntityModel<PagoDTO>> pagos = pagoService.findAll().stream()
                .map(pagoModelAssembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(pagos,
                linkTo(methodOn(PagoControllerV2.class).findAll()).withSelfRel()));
    }

    @GetMapping("/pagos/{id}")
    @Operation(summary = "Buscar pago por ID", description = "Obtiene un pago especifico con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Pago encontrado correctamente")
    @ApiResponse(responseCode = "404", description = "Pago no encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<EntityModel<PagoDTO>> findById(
            @Parameter(description = "ID del pago", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(pagoModelAssembler.toModel(pagoService.findById(id)));
    }

    @PostMapping("/pagos")
    @Operation(summary = "Crear pago", description = "Registra un nuevo pago en el sistema")
    @ApiResponse(responseCode = "201", description = "Pago creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<EntityModel<PagoDTO>> save(
            @Valid
            @RequestBody(description = "Datos requeridos para crear el pago", required = true,
                    content = @Content(schema = @Schema(implementation = PagoRequestDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody PagoRequestDTO requestDTO) {
        PagoDTO pagoCreado = pagoService.save(requestDTO);
        return ResponseEntity
                .created(linkTo(methodOn(PagoControllerV2.class).findById(pagoCreado.getId())).toUri())
                .body(pagoModelAssembler.toModel(pagoCreado));
    }

    @PutMapping("/pagos/{id}")
    @Operation(summary = "Actualizar pago", description = "Modifica los datos de un pago existente")
    @ApiResponse(responseCode = "200", description = "Pago actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Pago no encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<EntityModel<PagoDTO>> update(
            @Parameter(description = "ID del pago", example = "1")
            @PathVariable Integer id,
            @Valid
            @RequestBody(description = "Datos requeridos para actualizar el pago", required = true,
                    content = @Content(schema = @Schema(implementation = PagoRequestDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody PagoRequestDTO requestDTO) {
        return ResponseEntity.ok(pagoModelAssembler.toModel(pagoService.update(id, requestDTO)));
    }

    @DeleteMapping("/pagos/{id}")
    @Operation(summary = "Eliminar pago", description = "Elimina un pago del sistema")
    @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del pago", example = "1")
            @PathVariable Integer id) {
        pagoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pagos/rango")
    @Operation(summary = "Buscar pagos por rango de monto", description = "Busca pagos entre un monto minimo y maximo")
    @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente")
    public ResponseEntity<CollectionModel<EntityModel<PagoDTO>>> buscarPorRangoMonto(
            @Parameter(description = "Monto minimo", example = "10000") @RequestParam BigDecimal min,
            @Parameter(description = "Monto maximo", example = "50000") @RequestParam BigDecimal max) {
        List<EntityModel<PagoDTO>> pagos = pagoService.buscarPorRangoMonto(min, max).stream()
                .map(pagoModelAssembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(pagos,
                linkTo(methodOn(PagoControllerV2.class).buscarPorRangoMonto(min, max)).withSelfRel(),
                linkTo(methodOn(PagoControllerV2.class).findAll()).withRel("pagos")));
    }
}

