package com.duoc.msvehiculos.assemblers;

import com.duoc.msvehiculos.controller.CategoriaControllerV2;
import com.duoc.msvehiculos.controller.VehiculoControllerV2;
import com.duoc.msvehiculos.dto.VehiculoDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//Implementa HATEOAS: envuelve el DTO y agrega enlaces relacionados a la respuesta.
@Component
public class VehiculoModelAssembler implements RepresentationModelAssembler<VehiculoDTO, EntityModel<VehiculoDTO>> {

    //Convierte el DTO en un recurso HATEOAS con enlaces self y de navegacion.
    @Override
    public EntityModel<VehiculoDTO> toModel(VehiculoDTO vehiculo) {
        return EntityModel.of(vehiculo,
                linkTo(methodOn(VehiculoControllerV2.class).findById(vehiculo.getId())).withSelfRel(),
                linkTo(methodOn(VehiculoControllerV2.class).findAll()).withRel("vehiculos"),
                linkTo(methodOn(CategoriaControllerV2.class).findById(vehiculo.getCategoriaId())).withRel("categoria"));
    }
}