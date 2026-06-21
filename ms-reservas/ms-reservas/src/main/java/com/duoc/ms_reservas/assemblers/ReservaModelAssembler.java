package com.duoc.ms_reservas.assemblers;

import com.duoc.ms_reservas.controller.EstadoReservaControllerV2;
import com.duoc.ms_reservas.controller.ReservaControllerV2;
import com.duoc.ms_reservas.dto.ReservaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// Implementa HATEOAS: envuelve el DTO y agrega enlaces relacionados a la respuesta.
@Component
public class ReservaModelAssembler implements RepresentationModelAssembler<ReservaDTO, EntityModel<ReservaDTO>> {

    private final String gatewayUrl;

    public ReservaModelAssembler(@Value("${api.gateway.url}") String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    // Convierte el DTO en un recurso HATEOAS con enlaces self y de navegacion.
    @Override
    public EntityModel<ReservaDTO> toModel(ReservaDTO reserva) {
        EntityModel<ReservaDTO> model = EntityModel.of(reserva,
                linkTo(methodOn(ReservaControllerV2.class).findById(reserva.getId())).withSelfRel(),
                linkTo(methodOn(ReservaControllerV2.class).findAll()).withRel("reservas"),
                Link.of(gatewayUrl + "/api/v2/clientes/" + reserva.getClienteId(), "cliente"),
                Link.of(gatewayUrl + "/api/v2/vehiculos/" + reserva.getVehiculoId(), "vehiculo"));

        if (reserva.getEstadoReservaId() != null) {
            model.add(linkTo(methodOn(EstadoReservaControllerV2.class)
                    .findById(reserva.getEstadoReservaId())).withRel("estado-reserva"));
        }

        return model;
    }
}
