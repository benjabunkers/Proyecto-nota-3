package com.duoc.ms_reservas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para crear o actualizar un estado de reserva")
public class EstadoReservaRequestDTO {

    @Schema(description = "Nombre del estado", example = "Confirmada")
    @NotBlank(message = "El nombre del estado es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @Schema(description = "Descripcion del estado", example = "Reserva confirmada y pendiente de retiro")
    @NotBlank(message = "La descripcion es obligatoria")
    @Size(min = 5, max = 150, message = "La descripcion debe tener entre 5 y 150 caracteres")
    private String descripcion;

    @Schema(description = "Prioridad de orden del estado", example = "2")
    @NotNull(message = "La prioridad es obligatoria")
    @Min(value = 1, message = "La prioridad debe ser mayor o igual a 1")
    private Integer prioridad;

    @Schema(description = "Indica si el estado esta activo", example = "true")
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;

    @Schema(description = "Fecha y hora de creacion", example = "2026-06-17T10:30:00")
    @NotNull(message = "La fecha de creacion es obligatoria")
    @PastOrPresent(message = "La fecha de creacion no puede ser futura")
    private LocalDateTime fechaCreacion;
}