package com.duoc.ms_reservas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de una reserva para respuestas de la API")
public class ReservaDTO {

    @Schema(description = "ID unico de la reserva", example = "1")
    private Integer id;

    @Schema(description = "ID del cliente validado mediante ms-clientes", example = "1")
    private Integer clienteId;

    @Schema(description = "Nombre del cliente obtenido mediante ms-clientes", example = "Juan")
    private String nombreCliente;

    @Schema(description = "Correo del cliente obtenido mediante ms-clientes", example = "juan@ejemplo.com")
    private String correoCliente;

    @Schema(description = "ID del vehiculo validado mediante ms-vehiculos", example = "1")
    private Integer vehiculoId;

    @Schema(description = "Nombre del vehiculo obtenido mediante ms-vehiculos", example = "Toyota Corolla")
    private String nombreVehiculo;

    @Schema(description = "Fecha de inicio de la reserva", example = "2026-06-20")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de termino de la reserva", example = "2026-06-25")
    private LocalDate fechaFin;

    @Schema(description = "Cantidad total de dias reservados", example = "5")
    private Integer cantidadDias;

    @Schema(description = "Monto total de la reserva", example = "225000")
    private BigDecimal montoTotal;

    @Schema(description = "Observacion asociada a la reserva", example = "Retiro durante la manana")
    private String observacion;

    @Schema(description = "Indica si la reserva esta activa", example = "true")
    private boolean activa;

    @Schema(description = "ID del estado de reserva", example = "1")
    private Integer estadoReservaId;

    @Schema(description = "Nombre del estado de reserva", example = "Confirmada")
    private String nombreEstadoReserva;
}