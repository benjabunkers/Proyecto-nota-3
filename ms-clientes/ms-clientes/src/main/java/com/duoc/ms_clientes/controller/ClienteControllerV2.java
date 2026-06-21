package com.duoc.ms_clientes.controller;

import com.duoc.ms_clientes.assemblers.ClienteModelAssembler;
import com.duoc.ms_clientes.dto.ClienteDTO;
import com.duoc.ms_clientes.dto.ClienteRequestDTO;
import com.duoc.ms_clientes.service.ClienteService;
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

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// API V2: conserva la logica de negocio y agrega enlaces HATEOAS a las respuestas.
@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
@Tag(name = "Clientes V2", description = "Operaciones para gestionar clientes")
public class ClienteControllerV2 {

    private final ClienteService clienteService;
    private final ClienteModelAssembler clienteModelAssembler;

    @GetMapping("/clientes")
    @Operation(summary = "Listar clientes", description = "Obtiene todos los clientes registrados con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida correctamente")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<CollectionModel<EntityModel<ClienteDTO>>> findAll() {
        List<EntityModel<ClienteDTO>> clientes = clienteService.findAll().stream()
                .map(clienteModelAssembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(clientes,
                linkTo(methodOn(ClienteControllerV2.class).findAll()).withSelfRel()));
    }

    @GetMapping("/clientes/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Obtiene un cliente especifico con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado correctamente")
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<EntityModel<ClienteDTO>> findById(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(clienteModelAssembler.toModel(clienteService.findById(id)));
    }

    @PostMapping("/clientes")
    @Operation(summary = "Crear cliente", description = "Registra un nuevo cliente en el sistema")
    @ApiResponse(responseCode = "201", description = "Cliente creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos invalidos",
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<EntityModel<ClienteDTO>> save(
            @Valid
            @RequestBody(description = "Datos requeridos para crear el cliente", required = true,
                    content = @Content(schema = @Schema(implementation = ClienteRequestDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody ClienteRequestDTO request) {
        ClienteDTO clienteCreado = clienteService.save(request);
        return ResponseEntity
                .created(linkTo(methodOn(ClienteControllerV2.class).findById(clienteCreado.getId())).toUri())
                .body(clienteModelAssembler.toModel(clienteCreado));
    }

    @PutMapping("/clientes/{id}")
    @Operation(summary = "Actualizar cliente", description = "Modifica los datos de un cliente existente")
    @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos invalidos",
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<EntityModel<ClienteDTO>> update(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Integer id,
            @Valid
            @RequestBody(description = "Datos requeridos para actualizar el cliente", required = true,
                    content = @Content(schema = @Schema(implementation = ClienteRequestDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody ClienteRequestDTO request) {
        return ResponseEntity.ok(clienteModelAssembler.toModel(clienteService.update(id, request)));
    }

    @DeleteMapping("/clientes/{id}")
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente del sistema")
    @ApiResponse(responseCode = "204", description = "Cliente eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
            content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Integer id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clientes/buscar-email")
    @Operation(summary = "Buscar clientes por email", description = "Busca clientes por correo y agrega enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<CollectionModel<EntityModel<ClienteDTO>>> buscarPorEmail(
            @Parameter(description = "Texto a buscar dentro del email", example = "gmail")
            @RequestParam String texto) {
        List<EntityModel<ClienteDTO>> clientes = clienteService.buscarPorEmail(texto).stream()
                .map(clienteModelAssembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(clientes,
                linkTo(methodOn(ClienteControllerV2.class).buscarPorEmail(texto)).withSelfRel(),
                linkTo(methodOn(ClienteControllerV2.class).findAll()).withRel("clientes")));
    }
}