package com.duoc.ms_reservas.assemblers;

import com.duoc.ms_reservas.controller.EstadoReservaControllerV2;
import com.duoc.ms_reservas.dto.EstadoReservaDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// Implementa HATEOAS: envuelve el DTO y agrega enlaces relacionados a la respuesta.
@Component
public class EstadoReservaModelAssembler implements RepresentationModelAssembler<EstadoReservaDTO, EntityModel<EstadoReservaDTO>> {

    // Convierte el DTO en un recurso HATEOAS con enlaces self y de navegacion.
    @Override
    public EntityModel<EstadoReservaDTO> toModel(EstadoReservaDTO estado) {
        return EntityModel.of(estado,
                linkTo(methodOn(EstadoReservaControllerV2.class).findById(estado.getId())).withSelfRel(),
                linkTo(methodOn(EstadoReservaControllerV2.class).findAll()).withRel("estados-reserva"));
    }
}