package com.duoc.msvehiculos.assemblers;

import com.duoc.msvehiculos.controller.CategoriaControllerV2;
import com.duoc.msvehiculos.dto.CategoriaDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//Implementa HATEOAS: envuelve el DTO y agrega enlaces relacionados a la respuesta.
@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<CategoriaDTO, EntityModel<CategoriaDTO>> {

    //Convierte el DTO en un recurso HATEOAS con enlaces self y de navegacion.
    @Override
    public EntityModel<CategoriaDTO> toModel(CategoriaDTO categoria) {
        return EntityModel.of(categoria,
                linkTo(methodOn(CategoriaControllerV2.class).findById(categoria.getId())).withSelfRel(),
                linkTo(methodOn(CategoriaControllerV2.class).findAll()).withRel("categorias"));
    }
}