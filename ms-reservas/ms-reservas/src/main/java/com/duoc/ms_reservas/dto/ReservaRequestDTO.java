package com.duoc.ms_reservas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Datos necesarios para crear o actualizar una reserva")
public class ReservaRequestDTO {

    @Schema(description = "ID del cliente que sera validado mediante ms-clientes", example = "1")
    @NotNull(message = "El id del cliente es obligatorio")
    @Min(value = 1, message = "El id del cliente debe ser mayor o igual a 1")
    private Integer clienteId;

    @Schema(description = "ID del vehiculo que sera validado mediante ms-vehiculos", example = "1")
    @NotNull(message = "El id del vehiculo es obligatorio")
    @Min(value = 1, message = "El id del vehiculo debe ser mayor o igual a 1")
    private Integer vehiculoId;

    @Schema(description = "Fecha de inicio de la reserva", example = "2026-06-20")
    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser pasada")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de termino de la reserva", example = "2026-06-25")
    @NotNull(message = "La fecha de fin es obligatoria")
    @FutureOrPresent(message = "La fecha de fin no puede ser pasada")
    private LocalDate fechaFin;

    @Schema(description = "Cantidad total de dias reservados", example = "5")
    @NotNull(message = "La cantidad de dias es obligatoria")
    @Min(value = 1, message = "La cantidad de dias debe ser mayor o igual a 1")
    private Integer cantidadDias;

    @Schema(description = "Monto total de la reserva", example = "225000")
    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto total debe ser mayor a 0")
    private BigDecimal montoTotal;

    @Schema(description = "Observacion asociada a la reserva", example = "Retiro durante la manana")
    @NotBlank(message = "La observacion es obligatoria")
    @Size(min = 3, max = 150, message = "La observacion debe tener entre 3 y 150 caracteres")
    private String observacion;

    @Schema(description = "Indica si la reserva esta activa", example = "true")
    @NotNull(message = "El estado activo de la reserva es obligatorio")
    private Boolean activa;

    @Schema(description = "ID del estado de reserva", example = "1")
    @NotNull(message = "El estado de reserva es obligatorio")
    @Min(value = 1, message = "El id del estado de reserva debe ser mayor o igual a 1")
    private Integer estadoReservaId;
}