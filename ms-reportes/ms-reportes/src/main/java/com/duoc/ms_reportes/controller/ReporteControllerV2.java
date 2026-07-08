package com.duoc.ms_reportes.controller;

import com.duoc.ms_reportes.assemblers.ReporteModelAssembler;
import com.duoc.ms_reportes.dto.ReporteDTO;
import com.duoc.ms_reportes.dto.ReporteRequestDTO;
import com.duoc.ms_reportes.service.ReporteService;
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
@Tag(name = "Reportes V2", description = "Operaciones para gestionar reportes")
public class ReporteControllerV2 {
    private final ReporteService reporteService;
    private final ReporteModelAssembler reporteModelAssembler;

    @GetMapping("/reportes")
    @Operation(summary = "Listar reportes", description = "Obtiene todos los reportes registrados con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Lista de reportes obtenida correctamente")
    public ResponseEntity<CollectionModel<EntityModel<ReporteDTO>>> findAll() {
        List<EntityModel<ReporteDTO>> reportes = reporteService.findAll().stream().map(reporteModelAssembler::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(reportes, linkTo(methodOn(ReporteControllerV2.class).findAll()).withSelfRel()));
    }

    @GetMapping("/reportes/{id}")
    @Operation(summary = "Buscar reporte por ID", description = "Obtiene un reporte especifico con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Reporte encontrado correctamente")
    @ApiResponse(responseCode = "404", description = "Reporte no encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<EntityModel<ReporteDTO>> findById(@Parameter(description = "ID del reporte", example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(reporteModelAssembler.toModel(reporteService.findById(id)));
    }

    @PostMapping("/reportes")
    @Operation(summary = "Crear reporte", description = "Registra un nuevo reporte en el sistema")
    @ApiResponse(responseCode = "201", description = "Reporte creado correctamente")
    public ResponseEntity<EntityModel<ReporteDTO>> save(@Valid @RequestBody(description = "Datos requeridos para crear el reporte", required = true, content = @Content(schema = @Schema(implementation = ReporteRequestDTO.class))) @org.springframework.web.bind.annotation.RequestBody ReporteRequestDTO requestDTO) {
        ReporteDTO reporteCreado = reporteService.save(requestDTO);
        return ResponseEntity.created(linkTo(methodOn(ReporteControllerV2.class).findById(reporteCreado.getId())).toUri()).body(reporteModelAssembler.toModel(reporteCreado));
    }

    @PutMapping("/reportes/{id}")
    @Operation(summary = "Actualizar reporte", description = "Modifica los datos de un reporte existente")
    @ApiResponse(responseCode = "200", description = "Reporte actualizado correctamente")
    public ResponseEntity<EntityModel<ReporteDTO>> update(@Parameter(description = "ID del reporte", example = "1") @PathVariable Integer id, @Valid @RequestBody(description = "Datos requeridos para actualizar el reporte", required = true, content = @Content(schema = @Schema(implementation = ReporteRequestDTO.class))) @org.springframework.web.bind.annotation.RequestBody ReporteRequestDTO requestDTO) {
        return ResponseEntity.ok(reporteModelAssembler.toModel(reporteService.update(id, requestDTO)));
    }

    @DeleteMapping("/reportes/{id}")
    @Operation(summary = "Eliminar reporte", description = "Elimina un reporte del sistema")
    @ApiResponse(responseCode = "204", description = "Reporte eliminado correctamente")
    public ResponseEntity<Void> delete(@Parameter(description = "ID del reporte", example = "1") @PathVariable Integer id) {
        reporteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reportes/reserva/{reservaId}")
    @Operation(summary = "Buscar reportes por reserva", description = "Obtiene reportes asociados a una reserva")
    @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente")
    public ResponseEntity<CollectionModel<EntityModel<ReporteDTO>>> findByReservaId(@Parameter(description = "ID de la reserva", example = "1") @PathVariable Integer reservaId) {
        List<EntityModel<ReporteDTO>> reportes = reporteService.findByReservaId(reservaId).stream().map(reporteModelAssembler::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(reportes, linkTo(methodOn(ReporteControllerV2.class).findByReservaId(reservaId)).withSelfRel(), linkTo(methodOn(ReporteControllerV2.class).findAll()).withRel("reportes")));
    }

    @GetMapping("/reportes/pago-confirmado")
    @Operation(summary = "Buscar reportes por pago confirmado", description = "Obtiene reportes filtrados por estado de pago confirmado")
    @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente")
    public ResponseEntity<CollectionModel<EntityModel<ReporteDTO>>> findByPagoConfirmado(@Parameter(description = "Estado del pago", example = "true") @RequestParam boolean confirmado) {
        List<EntityModel<ReporteDTO>> reportes = reporteService.findByPagoConfirmado(confirmado).stream().map(reporteModelAssembler::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(reportes, linkTo(methodOn(ReporteControllerV2.class).findByPagoConfirmado(confirmado)).withSelfRel(), linkTo(methodOn(ReporteControllerV2.class).findAll()).withRel("reportes")));
    }
}

