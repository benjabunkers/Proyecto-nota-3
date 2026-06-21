package com.duoc.ms_reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoDTO {

    private Integer id;
    private String nombre;
    private String patente;
    private String marca;
    private String modelo;
    private Integer anio;
    private String color;
    private BigDecimal precioArriendoDiario;
    private Integer kilometraje;
    private Boolean disponible;
    private LocalDate fechaIngreso;
    private Integer categoriaId;
    private String categoriaNombre;
}
