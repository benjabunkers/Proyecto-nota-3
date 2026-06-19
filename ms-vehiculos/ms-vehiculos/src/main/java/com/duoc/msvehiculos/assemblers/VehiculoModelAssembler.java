package com.duoc.msvehiculos.assemblers;

import com.duoc.msvehiculos.controller.CategoriaController;
import com.duoc.msvehiculos.controller.VehiculoController;
import com.duoc.msvehiculos.dto.VehiculoDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VehiculoModelAssembler implements RepresentationModelAssembler<VehiculoDTO, EntityModel<VehiculoDTO>> {

    @Override
    public EntityModel<VehiculoDTO> toModel(VehiculoDTO vehiculo) {
        return EntityModel.of(vehiculo,
                linkTo(methodOn(VehiculoController.class).findById(vehiculo.getId())).withSelfRel(),
                linkTo(methodOn(VehiculoController.class).findAll()).withRel("vehiculos"),
                linkTo(methodOn(CategoriaController.class).findById(vehiculo.getCategoriaId())).withRel("categoria"));
    }
}