package com.duoc.msempleados.repository;
import com.duoc.msempleados.model.Empleado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:empleados_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class EmpleadoRepositoryTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Test
    void listarEmpleadosActivosPorAnio_deberiaRetornarSoloActivosDelAnio() {
        empleadoRepository.save(crearEmpleado("11111111-1", "Ana", "ana@test.cl", true, LocalDate.of(2026, 3, 10)));
        empleadoRepository.save(crearEmpleado("22222222-2", "Luis", "luis@test.cl", false, LocalDate.of(2026, 4, 12)));
        empleadoRepository.save(crearEmpleado("33333333-3", "Marta", "marta@test.cl", true, LocalDate.of(2025, 4, 12)));

        List<Empleado> resultado = empleadoRepository.listarEmpleadosActivosPorAnio(2026);

        assertEquals(1, resultado.size());
        assertEquals("Ana", resultado.get(0).getNombre());
    }

    private Empleado crearEmpleado(String rut, String nombre, String email, boolean activo, LocalDate fechaIngreso) {
        Empleado empleado = new Empleado();
        empleado.setRut(rut);
        empleado.setNombre(nombre);
        empleado.setCargo("Ejecutivo");
        empleado.setEmail(email);
        empleado.setSueldo(new BigDecimal("850000"));
        empleado.setActivo(activo);
        empleado.setFechaIngreso(fechaIngreso);
        return empleado;
    }
}
