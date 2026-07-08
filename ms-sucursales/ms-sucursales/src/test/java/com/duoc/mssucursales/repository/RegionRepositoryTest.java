package com.duoc.mssucursales.repository;
import com.duoc.mssucursales.model.Region;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:sucursales_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class RegionRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Test
    void findByCodigo_deberiaRetornarRegionCuandoExiste() {
        regionRepository.save(crearRegion("Metropolitana", "RM", 13));

        Optional<Region> resultado = regionRepository.findByCodigo("RM");

        assertTrue(resultado.isPresent());
        assertEquals("Metropolitana", resultado.get().getNombre());
    }

    private Region crearRegion(String nombre, String codigo, Integer numeroRegion) {
        Region region = new Region();
        region.setNombre(nombre);
        region.setCodigo(codigo);
        region.setNumeroRegion(numeroRegion);
        region.setCapitalRegional("Santiago");
        region.setActiva(true);
        region.setFechaCreacion(LocalDate.now());
        return region;
    }
}
