package com.duoc.msvehiculos.controller;

import com.duoc.msvehiculos.assemblers.VehiculoModelAssembler;
import com.duoc.msvehiculos.dto.VehiculoDTO;
import com.duoc.msvehiculos.dto.VehiculoRequestDTO;
import com.duoc.msvehiculos.service.VehiculoService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// API V2: conserva la logica de negocio y agrega enlaces HATEOAS a las respuestas.
@RestController
@RequestMapping("/api/v2/vehiculos")
@RequiredArgsConstructor
@Tag(name = "Vehiculos V2", description = "Operaciones para gestionar los vehiculos")
public class VehiculoControllerV2 {

    private final VehiculoService vehiculoService;
    private final VehiculoModelAssembler vehiculoModelAssembler;

    @GetMapping
    @Operation(summary = "Listar vehiculos", description = "Obtiene todos los vehiculos con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Lista de vehiculos obtenida correctamente")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<CollectionModel<EntityModel<VehiculoDTO>>> findAll() {
        List<EntityModel<VehiculoDTO>> vehiculos = vehiculoService.findAll().stream()
                .map(vehiculoModelAssembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(vehiculos,
                linkTo(methodOn(VehiculoControllerV2.class).findAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vehiculo por ID", description = "Obtiene un vehiculo especifico con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Vehiculo encontrado correctamente")
    @ApiResponse(responseCode = "404", description = "Vehiculo no encontrado",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<EntityModel<VehiculoDTO>> findById(
            @Parameter(description = "ID del vehiculo", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(vehiculoModelAssembler.toModel(vehiculoService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Crear vehiculo", description = "Registra un nuevo vehiculo asociado a una categoria")
    @ApiResponse(responseCode = "201", description = "Vehiculo creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos invalidos",
            content = @Content(schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "404", description = "Categoria asociada no encontrada",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<EntityModel<VehiculoDTO>> save(
            @Valid
            @RequestBody(description = "Datos requeridos para crear el vehiculo", required = true,
                    content = @Content(schema = @Schema(implementation = VehiculoRequestDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody VehiculoRequestDTO dto) {
        VehiculoDTO vehiculoCreado = vehiculoService.save(dto);
        return ResponseEntity
                .created(linkTo(methodOn(VehiculoControllerV2.class).findById(vehiculoCreado.getId())).toUri())
                .body(vehiculoModelAssembler.toModel(vehiculoCreado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar vehiculo", description = "Modifica los datos de un vehiculo existente")
    @ApiResponse(responseCode = "200", description = "Vehiculo actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos invalidos",
            content = @Content(schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "404", description = "Vehiculo o categoria asociada no encontrada",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<EntityModel<VehiculoDTO>> update(
            @Parameter(description = "ID del vehiculo", example = "1")
            @PathVariable Integer id,
            @Valid
            @RequestBody(description = "Datos requeridos para actualizar el vehiculo", required = true,
                    content = @Content(schema = @Schema(implementation = VehiculoRequestDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody VehiculoRequestDTO dto) {
        return ResponseEntity.ok(vehiculoModelAssembler.toModel(vehiculoService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar vehiculo", description = "Elimina un vehiculo del sistema")
    @ApiResponse(responseCode = "204", description = "Vehiculo eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Vehiculo no encontrado",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del vehiculo", example = "1")
            @PathVariable Integer id) {
        vehiculoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disponibles/precio-menor/{precioMaximo}")
    @Operation(summary = "Buscar vehiculos disponibles por precio",
            description = "Obtiene vehiculos disponibles por precio con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente")
    public ResponseEntity<CollectionModel<EntityModel<VehiculoDTO>>> buscarDisponiblesPorPrecioMenor(
            @Parameter(description = "Precio diario maximo", example = "50000")
            @PathVariable BigDecimal precioMaximo) {
        List<EntityModel<VehiculoDTO>> vehiculos = vehiculoService.buscarDisponiblesPorPrecioMenor(precioMaximo).stream()
                .map(vehiculoModelAssembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(vehiculos,
                linkTo(methodOn(VehiculoControllerV2.class).buscarDisponiblesPorPrecioMenor(precioMaximo)).withSelfRel(),
                linkTo(methodOn(VehiculoControllerV2.class).findAll()).withRel("vehiculos")));
    }
}