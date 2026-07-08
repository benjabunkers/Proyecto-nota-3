package com.duoc.ms_reportes.repository;
import com.duoc.ms_reportes.model.Reporte;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:reportes_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false"
})
public class ReporteRepositoryTest {

    @Autowired
    private ReporteRepository reporteRepository;

    @Test
    void findByReservaId_deberiaRetornarReportesDeLaReserva() {
        reporteRepository.save(crearReporte(10, 1, true, LocalDate.of(2026, 1, 10)));
        reporteRepository.save(crearReporte(10, 2, false, LocalDate.of(2026, 1, 12)));
        reporteRepository.save(crearReporte(99, 3, true, LocalDate.of(2026, 1, 15)));

        List<Reporte> resultado = reporteRepository.findByReservaId(10);

        assertEquals(2, resultado.size());
    }

    @Test
    void buscarPorPagoConfirmado_deberiaOrdenarPorFechaGeneracionDesc() {
        reporteRepository.save(crearReporte(10, 1, true, LocalDate.of(2026, 1, 10)));
        reporteRepository.save(crearReporte(20, 2, true, LocalDate.of(2026, 3, 12)));
        reporteRepository.save(crearReporte(30, 3, false, LocalDate.of(2026, 4, 15)));

        List<Reporte> resultado = reporteRepository.buscarPorPagoConfirmado(true);

        assertEquals(2, resultado.size());
        assertEquals(20, resultado.get(0).getReservaId());
        assertEquals(10, resultado.get(1).getReservaId());
    }

    private Reporte crearReporte(Integer reservaId, Integer pagoId, boolean pagoConfirmado, LocalDate fechaGeneracion) {
        Reporte reporte = new Reporte();
        reporte.setReservaId(reservaId);
        reporte.setPagoId(pagoId);
        reporte.setTipoReporte("RESUMEN");
        reporte.setFechaGeneracion(fechaGeneracion);
        reporte.setDescripcion("Reporte de prueba");
        reporte.setTotalReserva(new BigDecimal("100000"));
        reporte.setMontoPagado(new BigDecimal("100000"));
        reporte.setReservaActiva(true);
        reporte.setPagoConfirmado(pagoConfirmado);
        return reporte;
    }
}
