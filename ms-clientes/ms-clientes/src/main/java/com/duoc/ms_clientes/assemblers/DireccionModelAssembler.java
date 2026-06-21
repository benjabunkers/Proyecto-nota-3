package com.duoc.ms_clientes.assemblers;

import com.duoc.ms_clientes.controller.ClienteControllerV2;
import com.duoc.ms_clientes.controller.DireccionControllerV2;
import com.duoc.ms_clientes.dto.DireccionDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// Implementa HATEOAS: envuelve el DTO y agrega enlaces relacionados a la respuesta.
@Component
public class DireccionModelAssembler implements RepresentationModelAssembler<DireccionDTO, EntityModel<DireccionDTO>> {

    // Convierte el DTO en un recurso HATEOAS con enlaces self y de navegacion.
    @Override
    public EntityModel<DireccionDTO> toModel(DireccionDTO direccion) {
        return EntityModel.of(direccion,
                linkTo(methodOn(DireccionControllerV2.class).findById(direccion.getId())).withSelfRel(),
                linkTo(methodOn(DireccionControllerV2.class).findAll()).withRel("direcciones"),
                linkTo(methodOn(ClienteControllerV2.class).findById(direccion.getClienteId())).withRel("cliente"));
    }
}