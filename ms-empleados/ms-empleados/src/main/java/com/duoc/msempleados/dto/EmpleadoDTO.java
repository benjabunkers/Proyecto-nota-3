package com.duoc.msempleados.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoDTO {


    private Integer id;

    private String rut;

    private String nombre;

    private String cargo;

    private String email;

    private BigDecimal sueldo;

    private Boolean activo = true;

    private LocalDate fechaIngreso;

}
