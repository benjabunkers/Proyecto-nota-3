package com.duoc.msempleados.controller;

import com.duoc.msempleados.assemblers.EmpleadoModelAssembler;
import com.duoc.msempleados.dto.EmpleadoDTO;
import com.duoc.msempleados.dto.EmpleadoRequestDTO;
import com.duoc.msempleados.service.EmpleadoService;
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
@Tag(name = "Empleados V2", description = "Operaciones para gestionar empleados")
public class EmpleadoControllerV2 {
    private final EmpleadoService empleadoService;
    private final EmpleadoModelAssembler empleadoModelAssembler;

    @GetMapping("/empleados")
    @Operation(summary = "Listar empleados", description = "Obtiene todos los empleados registrados con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida correctamente")
    public ResponseEntity<CollectionModel<EntityModel<EmpleadoDTO>>> findAll() {
        List<EntityModel<EmpleadoDTO>> empleados = empleadoService.findAll().stream().map(empleadoModelAssembler::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(empleados, linkTo(methodOn(EmpleadoControllerV2.class).findAll()).withSelfRel()));
    }

    @GetMapping("/empleados/{id}")
    @Operation(summary = "Buscar empleado por ID", description = "Obtiene un empleado especifico con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Empleado encontrado correctamente")
    @ApiResponse(responseCode = "404", description = "Empleado no encontrado", content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<EntityModel<EmpleadoDTO>> findById(@Parameter(description = "ID del empleado", example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(empleadoModelAssembler.toModel(empleadoService.findById(id)));
    }

    @PostMapping("/empleados")
    @Operation(summary = "Crear empleado", description = "Registra un nuevo empleado en el sistema")
    @ApiResponse(responseCode = "201", description = "Empleado creado correctamente")
    public ResponseEntity<EntityModel<EmpleadoDTO>> save(@Valid @RequestBody(description = "Datos requeridos para crear el empleado", required = true, content = @Content(schema = @Schema(implementation = EmpleadoRequestDTO.class))) @org.springframework.web.bind.annotation.RequestBody EmpleadoRequestDTO request) {
        EmpleadoDTO empleadoCreado = empleadoService.save(request);
        return ResponseEntity.created(linkTo(methodOn(EmpleadoControllerV2.class).findById(empleadoCreado.getId())).toUri()).body(empleadoModelAssembler.toModel(empleadoCreado));
    }

    @PutMapping("/empleados/{id}")
    @Operation(summary = "Actualizar empleado", description = "Modifica los datos de un empleado existente")
    @ApiResponse(responseCode = "200", description = "Empleado actualizado correctamente")
    public ResponseEntity<EntityModel<EmpleadoDTO>> update(@Parameter(description = "ID del empleado", example = "1") @PathVariable Integer id, @Valid @RequestBody(description = "Datos requeridos para actualizar el empleado", required = true, content = @Content(schema = @Schema(implementation = EmpleadoRequestDTO.class))) @org.springframework.web.bind.annotation.RequestBody EmpleadoRequestDTO request) {
        return ResponseEntity.ok(empleadoModelAssembler.toModel(empleadoService.update(id, request)));
    }

    @DeleteMapping("/empleados/{id}")
    @Operation(summary = "Eliminar empleado", description = "Elimina un empleado del sistema")
    @ApiResponse(responseCode = "204", description = "Empleado eliminado correctamente")
    public ResponseEntity<Void> delete(@Parameter(description = "ID del empleado", example = "1") @PathVariable Integer id) {
        empleadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activos/anio/{anio}")
    @Operation(summary = "Listar empleados activos por anio", description = "Obtiene empleados activos ingresados en el anio indicado")
    @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida correctamente")
    public ResponseEntity<CollectionModel<EntityModel<EmpleadoDTO>>> listarActivosPorAnio(@Parameter(description = "Anio de ingreso", example = "2024") @PathVariable Integer anio) {
        List<EntityModel<EmpleadoDTO>> empleados = empleadoService.listarActivosPorAnio(anio).stream().map(empleadoModelAssembler::toModel).toList();
        return ResponseEntity.ok(CollectionModel.of(empleados, linkTo(methodOn(EmpleadoControllerV2.class).listarActivosPorAnio(anio)).withSelfRel(), linkTo(methodOn(EmpleadoControllerV2.class).findAll()).withRel("empleados")));
    }
}

