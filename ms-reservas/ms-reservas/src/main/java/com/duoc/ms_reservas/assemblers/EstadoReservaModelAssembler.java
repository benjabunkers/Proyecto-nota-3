package com.duoc.ms_reservas.assemblers;

import com.duoc.ms_reservas.controller.EstadoReservaController;
import com.duoc.ms_reservas.dto.EstadoReservaDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EstadoReservaModelAssembler implements RepresentationModelAssembler<EstadoReservaDTO, EntityModel<EstadoReservaDTO>> {

    @Override
    public EntityModel<EstadoReservaDTO> toModel(EstadoReservaDTO estado) {
        return EntityModel.of(estado,
                linkTo(methodOn(EstadoReservaController.class).findById(estado.getId())).withSelfRel(),
                linkTo(methodOn(EstadoReservaController.class).findAll()).withRel("estados-reserva"));
    }
}