package com.duoc.ms_reservas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de un estado de reserva para respuestas de la API")
public class EstadoReservaDTO {

    @Schema(description = "ID unico del estado", example = "1")
    private Integer id;

    @Schema(description = "Nombre del estado", example = "Confirmada")
    private String nombre;

    @Schema(description = "Descripcion del estado", example = "Reserva confirmada y pendiente de retiro")
    private String descripcion;

    @Schema(description = "Prioridad de orden del estado", example = "2")
    private Integer prioridad;

    @Schema(description = "Indica si el estado esta activo", example = "true")
    private boolean activo;

    @Schema(description = "Fecha y hora de creacion", example = "2026-06-17T10:30:00")
    private LocalDateTime fechaCreacion;
}