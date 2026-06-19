package com.duoc.ms_clientes.assemblers;

import com.duoc.ms_clientes.controller.ClienteController;
import com.duoc.ms_clientes.controller.DireccionController;
import com.duoc.ms_clientes.dto.DireccionDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DireccionModelAssembler implements RepresentationModelAssembler<DireccionDTO, EntityModel<DireccionDTO>> {

    @Override
    public EntityModel<DireccionDTO> toModel(DireccionDTO direccion) {
        return EntityModel.of(direccion,
                linkTo(methodOn(DireccionController.class).findById(direccion.getId())).withSelfRel(),
                linkTo(methodOn(DireccionController.class).findAll()).withRel("direcciones"),
                linkTo(methodOn(ClienteController.class).findById(direccion.getClienteId())).withRel("cliente"));
    }
}