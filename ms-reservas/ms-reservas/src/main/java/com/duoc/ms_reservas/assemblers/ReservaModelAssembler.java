package com.duoc.ms_reservas.assemblers;

import com.duoc.ms_reservas.controller.EstadoReservaController;
import com.duoc.ms_reservas.controller.ReservaController;
import com.duoc.ms_reservas.dto.ReservaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReservaModelAssembler implements RepresentationModelAssembler<ReservaDTO, EntityModel<ReservaDTO>> {

    private final String gatewayUrl;

    public ReservaModelAssembler(@Value("${api.gateway.url}") String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    @Override
    public EntityModel<ReservaDTO> toModel(ReservaDTO reserva) {
        EntityModel<ReservaDTO> model = EntityModel.of(reserva,
                linkTo(methodOn(ReservaController.class).findById(reserva.getId())).withSelfRel(),
                linkTo(methodOn(ReservaController.class).findAll()).withRel("reservas"),
                Link.of(gatewayUrl + "/api/v1/clientes/" + reserva.getClienteId(), "cliente"),
                Link.of(gatewayUrl + "/api/v1/vehiculos/" + reserva.getVehiculoId(), "vehiculo"));

        if (reserva.getEstadoReservaId() != null) {
            model.add(linkTo(methodOn(EstadoReservaController.class)
                    .findById(reserva.getEstadoReservaId())).withRel("estado-reserva"));
        }

        return model;
    }
}
