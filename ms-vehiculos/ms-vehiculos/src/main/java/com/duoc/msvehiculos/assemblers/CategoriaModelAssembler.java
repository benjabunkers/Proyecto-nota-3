package com.duoc.msvehiculos.assemblers;

import com.duoc.msvehiculos.controller.CategoriaController;
import com.duoc.msvehiculos.dto.CategoriaDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<CategoriaDTO, EntityModel<CategoriaDTO>> {

    @Override
    public EntityModel<CategoriaDTO> toModel(CategoriaDTO categoria) {
        return EntityModel.of(categoria,
                linkTo(methodOn(CategoriaController.class).findById(categoria.getId())).withSelfRel(),
                linkTo(methodOn(CategoriaController.class).findAll()).withRel("categorias"));
    }
}