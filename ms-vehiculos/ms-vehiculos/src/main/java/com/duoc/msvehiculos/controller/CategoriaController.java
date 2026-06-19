package com.duoc.msvehiculos.controller;

import com.duoc.msvehiculos.assemblers.CategoriaModelAssembler;
import com.duoc.msvehiculos.dto.CategoriaDTO;
import com.duoc.msvehiculos.dto.CategoriaRequestDTO;
import com.duoc.msvehiculos.service.CategoriaService;
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

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Operaciones para gestionar categorias de vehiculos")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaModelAssembler categoriaModelAssembler;

    @GetMapping
    @Operation(summary = "Listar categorias", description = "Obtiene todas las categorias con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Lista de categorias obtenida correctamente")
    public ResponseEntity<CollectionModel<EntityModel<CategoriaDTO>>> findAll() {
        List<EntityModel<CategoriaDTO>> categorias = categoriaService.findAll().stream()
                .map(categoriaModelAssembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(categorias,
                linkTo(methodOn(CategoriaController.class).findAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID", description = "Obtiene una categoria especifica con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Categoria encontrada correctamente")
    @ApiResponse(responseCode = "404", description = "Categoria no encontrada",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<EntityModel<CategoriaDTO>> findById(
            @Parameter(description = "ID de la categoria", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(categoriaModelAssembler.toModel(categoriaService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Crear categoria", description = "Registra una nueva categoria de vehiculos")
    @ApiResponse(responseCode = "201", description = "Categoria creada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos invalidos",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<EntityModel<CategoriaDTO>> save(
            @Valid
            @RequestBody(description = "Datos requeridos para crear la categoria", required = true,
                    content = @Content(schema = @Schema(implementation = CategoriaRequestDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody CategoriaRequestDTO dto) {
        CategoriaDTO categoriaCreada = categoriaService.save(dto);
        return ResponseEntity
                .created(linkTo(methodOn(CategoriaController.class).findById(categoriaCreada.getId())).toUri())
                .body(categoriaModelAssembler.toModel(categoriaCreada));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoria", description = "Modifica los datos de una categoria existente")
    @ApiResponse(responseCode = "200", description = "Categoria actualizada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos invalidos",
            content = @Content(schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "404", description = "Categoria no encontrada",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<EntityModel<CategoriaDTO>> update(
            @Parameter(description = "ID de la categoria", example = "1")
            @PathVariable Integer id,
            @Valid
            @RequestBody(description = "Datos requeridos para actualizar la categoria", required = true,
                    content = @Content(schema = @Schema(implementation = CategoriaRequestDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody CategoriaRequestDTO dto) {
        return ResponseEntity.ok(categoriaModelAssembler.toModel(categoriaService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoria", description = "Elimina una categoria del sistema")
    @ApiResponse(responseCode = "204", description = "Categoria eliminada correctamente")
    @ApiResponse(responseCode = "404", description = "Categoria no encontrada",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la categoria", example = "1")
            @PathVariable Integer id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}