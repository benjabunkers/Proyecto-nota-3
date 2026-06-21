package com.duoc.ms_clientes.assemblers;

import com.duoc.ms_clientes.controller.ClienteControllerV2;
import com.duoc.ms_clientes.dto.ClienteDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


// Implementa HATEOAS: envuelve el DTO y agrega enlaces relacionados a la respuesta.
@Component
public class  ClienteModelAssembler implements RepresentationModelAssembler<ClienteDTO, EntityModel<ClienteDTO>> {
    // Convierte el DTO en un recurso HATEOAS con enlaces self y de navegacion.
    @Override
    public EntityModel<ClienteDTO> toModel(ClienteDTO cliente) {
        return EntityModel.of(cliente,
                linkTo(methodOn(ClienteControllerV2.class).findById(cliente.getId())).withSelfRel(),
                linkTo(methodOn(ClienteControllerV2.class).findAll()).withRel("clientes")
        );
    }
}
