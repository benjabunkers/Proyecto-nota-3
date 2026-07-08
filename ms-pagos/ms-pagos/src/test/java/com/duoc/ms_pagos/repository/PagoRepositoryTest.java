package com.duoc.ms_pagos.repository;
import com.duoc.ms_pagos.model.Pago;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:pagos_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
})
public class PagoRepositoryTest {

    @Autowired
    private PagoRepository pagoRepository;

    @Test
    void buscarPagosPorRangoMonto_deberiaRetornarPagosOrdenadosPorFechaDesc() {
        pagoRepository.save(crearPago(1, "10000", LocalDate.of(2026, 1, 10)));
        pagoRepository.save(crearPago(2, "50000", LocalDate.of(2026, 3, 15)));
        pagoRepository.save(crearPago(3, "90000", LocalDate.of(2026, 2, 20)));

        List<Pago> resultado = pagoRepository.buscarPagosPorRangoMonto(
                new BigDecimal("20000"),
                new BigDecimal("95000")
        );

        assertEquals(2, resultado.size());
        assertEquals(2, resultado.get(0).getReservaId());
        assertEquals(3, resultado.get(1).getReservaId());
    }

    private Pago crearPago(Integer reservaId, String monto, LocalDate fechaPago) {
        Pago pago = new Pago();
        pago.setReservaId(reservaId);
        pago.setMetodoPago("Tarjeta");
        pago.setMonto(new BigDecimal(monto));
        pago.setCodigoTransaccion("TX-" + reservaId);
        pago.setPagado(true);
        pago.setFechaPago(fechaPago);
        pago.setObservacion("Pago de prueba");
        return pago;
    }
}
